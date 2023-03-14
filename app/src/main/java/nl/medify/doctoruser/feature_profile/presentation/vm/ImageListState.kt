package nl.medify.doctoruser.feature_profile.presentation.vm

import nl.medify.doctoruser.feature_profile.domain.model.Image

data class ImageListState(
    val isLoading: Boolean = false,
    val imageList: List<Image> = emptyList(),
    val error: String = ""
)