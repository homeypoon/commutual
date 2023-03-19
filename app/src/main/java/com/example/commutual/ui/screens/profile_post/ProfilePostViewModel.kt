

package com.example.commutual.ui.screens.profile_post

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import com.example.commutual.*
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.model.Post
import com.example.commutual.model.service.ConfigurationService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class ProfilePostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : CommutualViewModel(logService) {

    val post = mutableStateOf(Post())

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()
            }
        }
    }

    fun onIconClick(coroutineScope: CoroutineScope, bottomSheetState: ModalBottomSheetState) {
        coroutineScope.launch {
            if (!bottomSheetState.isVisible)
                bottomSheetState.show()
        }
    }

    fun onEditPostClick(
        openScreen: (String) -> Unit,
        post: Post,
        coroutineScope: CoroutineScope,
        bottomSheetState: ModalBottomSheetState
    ) {
        openScreen("$EDIT_POST_SCREEN?$POST_ID={${post.postId}}?$SCREEN_TITLE=${ST_EDIT_POST}")
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    fun onDeletePostClick(
        popUpScreen: () -> Unit,
        post: Post,
        coroutineScope: CoroutineScope,
        bottomSheetState: ModalBottomSheetState
    ) {
        launchCatching { storageService.deletePost(post.postId) }
        coroutineScope.launch {
            if (bottomSheetState.isVisible)
                bottomSheetState.hide()
        }
        popUpScreen()

    }


}
