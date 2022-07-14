package com.dent.denttask.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dent.denttask.databinding.ActivityMainBinding
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}