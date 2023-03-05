package com.example.commutual.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.commutual.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {


    // Navigate to EditPost Screen when user clicks on FAB
    fun onAddClick(openScreen: (String) -> Unit) =
//        openScreen("$EDIT_POST_SCREEN?$SCREEN_TITLE={$ST_EDIT_POST}")
    openScreen("$EDIT_POST_SCREEN?$POST_ID=$POST_DEFAULT_ID?$SCREEN_TITLE=${ST_CREATE_POST}")

//    openScreen("$EDIT_POST_SCREEN?$POST_ID={$}?$SCREEN_TITLE=${ST_EDIT_POST}")


}