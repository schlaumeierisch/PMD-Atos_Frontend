package nl.medify.doctoruser.feature_profile.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.doctoruser.feature_profile.domain.model.Image
import nl.medify.doctoruser.feature_profile.domain.use_case.ImageUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageUseCases: ImageUseCases
) : ViewModel() {
    lateinit var image: Image

    private val _imageList = MutableStateFlow(ImageListState())
    var imageList: StateFlow<ImageListState> = _imageList

    fun getImagesByPatientId(id: String) = viewModelScope.launch(Dispatchers.IO) {
        imageUseCases.getImageByPatientIdUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _imageList.value = ImageListState(imageList = it.data ?: emptyList())
                    Log.d("Success", it.toString())
                }
                is Resource.Loading -> {
                    _imageList.value = ImageListState(isLoading = true)
                }
                is Resource.Error -> {
                    _imageList.value = ImageListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "How tf did it even get here")
                }
            }
        }
    }
}