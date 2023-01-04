package com.example.functions.common.composable

import android.view.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.functions.ui.screens.profile_post.PostBottomSheetOption

import androidx.compose.ui.Modifier
import com.example.functions.ui.screens.item.BottomSheetOptionItem

@Composable
fun BottomSheetComposable() {

    val bottomSheetOptions = listOf(
        PostBottomSheetOption.Edit,
        PostBottomSheetOption.Delete
    )

    LazyColumn {
        items(bottomSheetOptions) {
            Surface(modifier = Modifier.clickable {
//                viewModel.onPostClick(openScreen, postItem)
            }) {
                BottomSheetOptionItem(it)
            }
        }

    }

}
