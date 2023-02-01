/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.commutual.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicToolbar(@StringRes title: Int) {
  CenterAlignedTopAppBar(title = { Text(
    stringResource(title),
    color = MaterialTheme.colorScheme.onSecondary,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    style = MaterialTheme.typography.titleLarge) },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      MaterialTheme.colorScheme.secondary),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionToolbar(
  @StringRes title: Int,
  @DrawableRes endActionIcon: Int,
  modifier: Modifier,
  endAction: () -> Unit
) {
  CenterAlignedTopAppBar(
    title = {
      Text(
        stringResource(title),
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.titleLarge,
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
//  TopAppBar(
//    title = { Text(stringResource(title),
//      style = MaterialTheme.typography.h3) },
//    backgroundColor = toolbarColor(),
//    actions = {
//      Box(modifier) {
//        IconButton(onClick = endAction) {
//          Icon(painter = painterResource(endActionIcon), contentDescription = "Action")
//        }
//      }
//    }
//  )
//}
//
//@Composable
//private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
//  return if (darkTheme) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primaryVariant
//}
