package com.skytoph.taski.presentation.core.semantics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val ColorRes = SemanticsPropertyKey<Color>("ColorRes")
var SemanticsPropertyReceiver.backgroundColor by ColorRes