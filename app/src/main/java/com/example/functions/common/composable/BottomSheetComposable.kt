package com.example.functions.common.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetComposable(
    options: List<String>,
    onNewValue: (String) -> Unit
    ) {
    // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ModalBottomSheetLayout(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material.ModalBottomSheetState,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function0)

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,)
    val scope = rememberCoroutineScope()


    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onNewValue(selectionOption)
//                        isExpanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = { scope.launch { state.show() } }) {
                Text("Click to show sheet")
            }
        }


    }


}