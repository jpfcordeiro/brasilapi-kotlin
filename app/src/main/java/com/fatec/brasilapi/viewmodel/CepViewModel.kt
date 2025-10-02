package com.fatec.brasilapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatec.brasilapi.service.CepService
import com.fatec.brasilapi.ui.state.CepScreenState
import com.fatec.brasilapi.utils.isValidCep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CepViewModel : ViewModel() {
    
    private val cepService = CepService()
    
    private val _uiState = MutableStateFlow(CepScreenState())
    val uiState: StateFlow<CepScreenState> = _uiState.asStateFlow()
    
    fun updateCepInput(cep: String) {
        _uiState.value = _uiState.value.copy(
            inputCep = cep,
            errorMessage = null
        )
    }
    
    fun searchCep() {
        val cep = _uiState.value.inputCep
        
        if (cep.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Digite um CEP válido"
            )
            return
        }
        
        if (!cep.isValidCep()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "CEP deve conter 8 dígitos"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                addressResponse = null
            )
            
            cepService.getCepInfo(cep)
                .onSuccess { addressResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        addressResponse = addressResponse,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message,
                        addressResponse = null
                    )
                }
        }
    }
}
