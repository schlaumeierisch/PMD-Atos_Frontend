package nl.medify.patientuser.feature_permission_linking.feature_care_providers.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case.CareProviderUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class CareProviderViewModel @Inject constructor(
    private val careProviderUseCases: CareProviderUseCases
) :ViewModel() {
    private val _careProviderList = MutableStateFlow(CareProviderListState())
    val careProviderList: StateFlow<CareProviderListState> = _careProviderList

    fun getAllCareProviders(id: String) = viewModelScope.launch(Dispatchers.IO) {
        careProviderUseCases.getAllCareProvidersUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _careProviderList.value = CareProviderListState(careProviderList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _careProviderList.value = CareProviderListState(isLoading = true)
                }
                is Resource.Error -> {
                    _careProviderList.value = CareProviderListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}