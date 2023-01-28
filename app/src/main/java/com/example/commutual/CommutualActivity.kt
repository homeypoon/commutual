package com.example.commutual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.commutual.ui.theme.CommutualTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommutualActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CommutualApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CommutualTheme {
//        CommutualApp()
    }
}