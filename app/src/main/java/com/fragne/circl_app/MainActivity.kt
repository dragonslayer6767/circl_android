package com.fragne.circl_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fragne.circl_app.ui.navigation.RootNavigation
import com.fragne.circl_app.ui.theme.Circl_appTheme
// import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for Circl App
 * Entry point for the Android application
 */
// @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Circl_appTheme {
                RootNavigation()
            }
        }
    }
}
