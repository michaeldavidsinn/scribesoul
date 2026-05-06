package com.scribesoul.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.scribesoul.app.ui.navigation.AppNavigation

import com.scribesoul.app.ui.theme.ScribesoulTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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