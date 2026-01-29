package com.bukuwarung.edc.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.bukuwarung.edc.sample.ui.theme.SampleBukuEDCTheme
import com.bukuwarung.edc.ui.HomeScreen
import com.bukuwarung.edc.ui.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setContent {
            SampleBukuEDCTheme {
                HomeScreen(viewModel = homeViewModel)
            }
        }
    }
}

