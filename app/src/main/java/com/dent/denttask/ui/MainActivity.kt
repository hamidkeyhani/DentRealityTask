package com.dent.denttask.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dent.denttask.databinding.ActivityMainBinding
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

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


    }

    private fun onListResponseStateReceived(state: CountriesState) {
        if (state.list.isNotEmpty()) {
            Log.i(TAG, "onListResponseStateReceived: " + state.list.size)
            state.list.forEach { addPointer(it.latlng[0], it.latlng[1]) }
        }
    }

    private fun addPointer(lat: Double, long: Double) {
        binding.mapView.also { map ->
            val annotationApi = map.annotations
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager(map)
            val pointAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
                .withPoint(Point.fromLngLat(lat, long))
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