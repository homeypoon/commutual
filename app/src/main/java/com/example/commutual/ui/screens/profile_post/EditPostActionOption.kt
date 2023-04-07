

package com.example.commutual.ui.screens.profile_post

import androidx.annotation.DrawableRes
import com.example.commutual.R

enum class ProfilePostBottomSheetOption(
  val title: String,
  @DrawableRes val iconId: Int,
) {
  Edit("Edit", R.drawable.ic_edit),
  Delete("Delete", R.drawable.ic_delete),
}

enum class PostDetailsBottomSheetOption(
  val title: String,
  @DrawableRes val iconId: Int,
) {
  Report("Report", R.drawable.ic_report)
}

enum class ReportBottomSheetOption(
  val title: String,
) {
  Spam("Spam"),
  Nudity("Nudity or Sexual Activity"),
  Hate("Hate Speech or Symbols"),
  FalseInformation("False Information"),
  Scam("Scam or Fraud"),
}
