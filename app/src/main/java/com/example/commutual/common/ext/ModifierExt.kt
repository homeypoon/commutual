

package com.example.commutual.common.ext

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.categoryChip(color: Color): Modifier {
  return this
    .border(
      width = 1.dp,
      color = color,
      shape = RoundedCornerShape(5.dp)
    )
    .padding(8.dp, 4.dp)
}

fun Modifier.basicButton(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.fieldModifier(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.toolbarActions(): Modifier {
  return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
  return this.fillMaxWidth().padding(12.dp)
}

