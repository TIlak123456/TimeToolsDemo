package com.demo.timetoolsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.demo.timetoolsdemo.ui.theme.TimeToolsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize LaunchDarkly
        LaunchDarklyManager.initialize(applicationContext)
        
        setContent {
            TimeToolsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimeToolsApp()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LaunchDarklyManager.close()
    }
}
