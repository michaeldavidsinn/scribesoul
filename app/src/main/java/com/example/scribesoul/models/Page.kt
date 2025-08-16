package com.example.scribesoul.models

import androidx.compose.ui.geometry.Offset
import com.example.scribesoul.ui.screens.ToolMode

data class Page(
    val paths: List<Pair<List<Offset>, ToolMode>>,
    var currentPath : List<Offset>,
    var toolMode : ToolMode
)