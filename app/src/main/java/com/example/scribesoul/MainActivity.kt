package com.example.scribesoul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.scribesoul.ui.navigation.AppNavigation
import com.example.scribesoul.ui.theme.ScribesoulTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ScribesoulTheme {
                AppNavigation(navController)
            }
        }
    }
}

