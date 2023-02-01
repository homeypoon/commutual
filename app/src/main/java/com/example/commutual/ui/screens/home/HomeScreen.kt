package com.example.commutual.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.R.string as AppText

@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
    floatingActionButton = {
        FloatingActionButton(
            onClick = { viewModel.onAddClick(openScreen) },
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            modifier = modifier.padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add",
                tint = MaterialTheme.colorScheme.onTertiary)
        }
    }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            BasicToolbar(
                title = AppText.home
            )
        }
    }
}