package com.example.functions.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.functions.model.Post
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditPostItem(
    post: Post,
    options: List<String>,
    onActionClick: (String) -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = post.title)
                Text(text = post.description)
            }
        }

        val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,)
        val coroutineScope = rememberCoroutineScope()

        ModalBottomSheetLayout(sheetContent = {
            LazyColumn {
                items(count = 5) {

                    ListItem(
                        modifier = Modifier.clickable {

                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        },
                        text = {
                            Text(text = "Item $it")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite"
                            )
                        }
                    )
                }

            }
        }) {
        }
    }

}