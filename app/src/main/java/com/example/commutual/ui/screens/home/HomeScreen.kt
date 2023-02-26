package com.example.commutual.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar

@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
    floatingActionButton = {

        ExtendedFloatingActionButton(
            text = {
                Text(text = stringResource(R.string.add_post),
                    color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.displayMedium)
            },
            icon = {
                Icon(Icons.Filled.Add, "Add",
                    tint = MaterialTheme.colorScheme.onTertiary)
            },
            onClick = { viewModel.onAddClick(openScreen) },
            modifier = modifier.padding(16.dp),
//            expanded = sharedViewModel.expandedFab.value,
            containerColor = MaterialTheme.colorScheme.tertiary,
        )
    }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            BasicToolbar(
                title = stringResource(R.string.home)
            )
        }
    }
}