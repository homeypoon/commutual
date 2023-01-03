package com.example.functions.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.functions.R.string as AppText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.functions.common.composable.ActionToolbar
import com.example.functions.common.composable.BasicToolbar
import com.example.functions.common.ext.toolbarActions

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
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = modifier.padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add")
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