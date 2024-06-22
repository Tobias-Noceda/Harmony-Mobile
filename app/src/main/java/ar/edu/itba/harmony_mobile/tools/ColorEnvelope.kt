package ar.edu.itba.harmony_mobile.tools

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorEnvelope(
  val color: Color,
  val hexCode: String,
  val fromUser: Boolean,
)