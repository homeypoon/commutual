

package com.example.commutual.ui.screens.profile_post

import androidx.annotation.DrawableRes
import com.example.commutual.R

enum class PostBottomSheetOption(
  val title: String,
  @DrawableRes val iconId: Int,
) {
  Edit("Edit", R.drawable.ic_edit),
  Delete("Delete", R.drawable.ic_delete)
}
