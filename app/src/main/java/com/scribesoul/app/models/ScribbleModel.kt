package com.scribesoul.app.models

import kotlinx.serialization.Serializable

data class FirestoreScribbleMeta(
    val id: String = "",
    val uid: String = "",
    val title: String = "Untitled Scribble",
    val lastUpdated: Long = 0L,
    val storageFilePath: String = ""
)

@Serializable
data class ScribbleDataDTO(
    val id: String,
    val paths: List<DrawablePathDTO> = emptyList(),
    val shapes: List<ShapeItemDTO> = emptyList(),
    val imageLayers: List<ImageLayerDTO> = emptyList(),
    val texts: List<EditableTextDTO> = emptyList()
)