package com.example.commutual

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.commutual.ui.theme.CommutualTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommutualActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            CommutualTheme {
                CommutualApp()
            }

        }
    }
}
