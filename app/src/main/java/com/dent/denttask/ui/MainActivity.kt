package com.dent.denttask.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dent.denttask.R
import com.dent.denttask.databinding.ActivityMainBinding
import com.dent.denttask.databinding.ItemCalloutViewBinding
import com.dent.denttask.domain.model.Country
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.OnCircleAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var countryList = emptyList<Country>()
    private lateinit var circleAnnotationManager: CircleAnnotationManager
    private lateinit var mViewAnnotationManager: ViewAnnotationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this)).apply {
            viewModel = mainViewModel
            setContentView(root)
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        }

        mainViewModel.apply {
            fetchList().launchIn(lifecycleScope)
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.countriesState.collect(::onListResponseStateReceived)
        }

        binding.mapView.apply {
            val annotationApi = annotations
            circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            mViewAnnotationManager = viewAnnotationManager
        }

        circleAnnotationManager.apply {
            addClickListener(
                OnCircleAnnotationClickListener {
                    val viewAnnotation = mViewAnnotationManager.addViewAnnotation(
                        resId = R.layout.item_callout_view,
                        options = viewAnnotationOptions {
                            geometry(it.point)
                            allowOverlap(false)
                        }
                    )
                    ItemCalloutViewBinding.bind(viewAnnotation).apply {
                        name.text = countryList[it.id.toInt()].name
                        capital.text = countryList[it.id.toInt()].capital
                    }
                    false
                }
            )
        }


    }

    private fun onListResponseStateReceived(state: CountriesState) {
        state.list.apply {
            if (isNotEmpty()) {
                countryList = this
                forEach { country ->
                    addPointer(country.latlng[0], country.latlng[1])
                }
            }
        }
    }

    private fun addPointer(lat: Double, long: Double) {
        binding.mapView.also { map ->
            val pointAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
                .withPoint(Point.fromLngLat(long, lat))
                .withCircleRadius(CIRCLE_RADIUS)
                .withCircleColor(CIRCLE_COLOR)
                .withCircleStrokeWidth(CIRCLE_STROKE)
                .withCircleStrokeColor(CIRCLE_STROKE_COLOR)
            circleAnnotationManager.create(pointAnnotationOptions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    companion object {
        private const val CIRCLE_RADIUS = 8.0
        private const val CIRCLE_COLOR = "#ee4e8b"
        private const val CIRCLE_STROKE = 2.0
        private const val CIRCLE_STROKE_COLOR = "#ffffff"
    }
}