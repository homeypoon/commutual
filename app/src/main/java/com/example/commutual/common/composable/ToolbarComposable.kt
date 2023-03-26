

package com.example.commutual.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicToolbar(title: String) {
  CenterAlignedTopAppBar(title = { Text(
    title,
    color = MaterialTheme.colorScheme.onSecondary,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    style = MaterialTheme.typography.displayLarge) },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      MaterialTheme.colorScheme.secondary),
  )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionToolbar(
  title: String,
  @DrawableRes endActionIcon: Int,
  modifier: Modifier = Modifier,
  endAction: () -> Unit
) {
  CenterAlignedTopAppBar(
    title = {
      Text(
        title,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.displayLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      MaterialTheme.colorScheme.secondary),
    actions = {
        IconButton(onClick = endAction) {
          Icon(painter = painterResource(endActionIcon),
            contentDescription = "Action",
            tint = MaterialTheme.colorScheme.onSecondary)
      }
    }
  )
}
