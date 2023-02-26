package com.example.commutual.ui.screens.edit_post

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.ActionToolbar
import com.example.commutual.common.composable.BasicField
import com.example.commutual.common.composable.DescriptionField
import com.example.commutual.common.composable.DropDownField
import com.example.commutual.common.ext.fieldModifier
import com.example.commutual.common.ext.spacer
import com.example.commutual.common.ext.toolbarActions
import com.example.commutual.model.CategoryEnum
import com.example.commutual.R.drawable as AppIcon
import com.example.commutual.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    popUpScreen: () -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: EditPostViewModel = hiltViewModel()
) {
    val post by viewModel.post

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val categories = enumValues<CategoryEnum>()
        .filter { it != CategoryEnum.DEFAULT }

    var categoryResourceId by remember { mutableStateOf(R.string.empty_string) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (viewModel.expandedDropDownMenu)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    LaunchedEffect(Unit) { viewModel.initialize(postId) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        ActionToolbar(
            title = AppText.create_post,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen, focusManager) }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()

        BasicField(
            AppText.post_title,
            post.title,
            viewModel::onTitleChange, fieldModifier,
            ImeAction.Next,
            KeyboardCapitalization.Words,
            focusManager
        )
        DescriptionField(
            AppText.post_description,
            post.description,
            viewModel::onDescriptionChange,
            fieldModifier,
            KeyboardCapitalization.Sentences,
            focusManager
        )

        Column(Modifier.padding(16.dp, 0.dp)) {

            DropDownField(
                value = stringResource(categoryResourceId),
//                onValueChange = {selectedText = it },
                onValueChange = {},
                labelText = stringResource(R.string.category),
                icon = icon,
                expanded = viewModel.expandedDropDownMenu,
                setExpandedDropDownMenu = { viewModel.setExpandedDropDownMenu(it) } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    }
            )

            DropdownMenu(
                expanded = viewModel.expandedDropDownMenu,
                onDismissRequest = { viewModel.setExpandedDropDownMenu(false) },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            categoryResourceId = category.categoryResourceId
                            viewModel.onCategoryButtonClick(category)
                            viewModel.setExpandedDropDownMenu(false)
                        },
                        text = {
                            Text(
                                text = stringResource(category.categoryResourceId)
                            )
                        }
                    )
                }
            }
        }
    }

}