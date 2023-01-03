package com.example.functions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.functions.screens.home.HomeScreen
import com.example.functions.ui.theme.FunctionsTheme

@Composable
fun FunctionsApp() {
    FunctionsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = HOME,
            ) {
                functionsGraph(appState = FunctionsAppState(navController))
            }
        }
    }
}

fun NavGraphBuilder.functionsGraph(appState: FunctionsAppState) {
    composable(HOME) {
        HomeScreen(
            openScreen = { route -> appState.navigate(route)}
        )
    }

}