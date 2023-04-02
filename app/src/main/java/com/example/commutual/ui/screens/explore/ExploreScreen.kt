package com.example.commutual.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.composable.DialogConfirmButton
import com.example.commutual.common.composable.SearchField
import com.example.commutual.model.CategoryEnum
import com.example.commutual.ui.screens.item.PostItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun ExploreScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel()
) {


    val posts = viewModel.posts.collectAsStateWithLifecycle(emptyList())


    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.showProgressIndicator(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.clearFocus(focusManager)
                })
            },
    ) {
        BasicToolbar(title = stringResource(R.string.explore))

        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
            text = stringResource(R.string.search),
            value = viewModel.searchText,
            onNewValue = viewModel::onSearchTextChange,
            onSearchClick = viewModel::onSearchClick,
            onFilterClick = viewModel::onFilterClick,
            openScreen = openScreen,
            capitalization = KeyboardCapitalization.None,
            focusManager = focusManager
        )

        if (viewModel.filterChipCategory != CategoryEnum.ANY) {

            LazyRow {
                item {
                    AssistChip(
                        shape = RoundedCornerShape(6.dp),
                        onClick = { },
                        label = {
                            Text(
                                stringResource(viewModel.filterChipCategory.categoryStringRes),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.onCloseFilterChipClick(focusManager) },
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                                    contentDescription = null,
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)

                    )
                }
            }
        }

        if (!viewModel.showProgressIndicator) {

            Divider()

            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            ) {

                items(posts.value, key = { it.postId }) { postItem ->
                    Surface(modifier = Modifier.clickable {
                        viewModel.onPostClick(openScreen, postItem)
                    }) {
                        PostItem(
                            post = postItem
                        )
                    }
                }

            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }
        }


        FullScreenDialog(viewModel)

    }
}

@Composable
fun FullScreenDialog(
    viewModel: ExploreViewModel
) {

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val categories = enumValues<CategoryEnum>()
        .filter { it != CategoryEnum.NONE }

    if (viewModel.showFiltersDialog) {

        AlertDialog(
            title = {
                Text(stringResource(R.string.filters),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {

                    Column(
                        Modifier.selectableGroup()
                    ) {
                        Text(
                            text = stringResource(R.string.category),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        categories.forEach { category ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .selectable(
                                        selected = (category == viewModel.selectedCategory),
                                        onClick = { viewModel.onCategorySelected(category) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (category == viewModel.selectedCategory),
                                    onClick = null // null recommended for accessibility with screenreaders
                                )
                                Text(
                                    text = stringResource(category.categoryStringRes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )

                            }
                        }

                        Button(
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 8.dp),
                            onClick = {
                                viewModel.resetFilters()
                            }) {
                            Text(text = stringResource(R.string.reset_filters))
                        }
                    }
                }

            },
            confirmButton = {
                DialogConfirmButton(R.string.apply_filters,
                    action = {
                        viewModel.onApplyFilterClick(focusManager)
                    }
                )
            },
            onDismissRequest = { viewModel.setShowFiltersDialog(false) },
        )

    }
}