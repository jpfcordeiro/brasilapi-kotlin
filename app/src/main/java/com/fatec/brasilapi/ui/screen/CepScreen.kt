package com.fatec.brasilapi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fatec.brasilapi.model.AddressResponse
import com.fatec.brasilapi.ui.state.CepScreenState
import com.fatec.brasilapi.utils.formatCep
import com.fatec.brasilapi.viewmodel.CepViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CepScreen(
    modifier: Modifier = Modifier,
    viewModel: CepViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        // Título
        Text(
            text = "Consulta CEP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Campo de entrada do CEP
        OutlinedTextField(
            value = uiState.inputCep,
            onValueChange = { cep ->
                if (cep.length <= 8 && cep.all { it.isDigit() }) {
                    viewModel.updateCepInput(cep)
                }
            },
            label = { Text("CEP (8 dígitos)") },
            placeholder = { Text("12345678") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.searchCep() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Botão de busca
        Button(
            onClick = { viewModel.searchCep() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            enabled = !uiState.isLoading
        ) {
            Text("Buscar CEP")
        }
        
        // Loading
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Buscando informações...",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Mensagem de erro
        uiState.errorMessage?.let { errorMessage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Resultado da busca
        uiState.addressResponse?.let { address ->
            AddressCard(address = address)
        }
    }
}

@Composable
fun AddressCard(address: AddressResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Informações do Endereço",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            AddressInfoRow(label = "CEP:", value = address.cep.formatCep())
            address.state?.let { AddressInfoRow(label = "Estado:", value = it) }
            address.city?.let { AddressInfoRow(label = "Cidade:", value = it) }
            address.district?.let { AddressInfoRow(label = "Bairro:", value = it) }
            address.street?.let { AddressInfoRow(label = "Rua:", value = it) }
        }
    }
}

@Composable
fun AddressInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f)
        )
    }
}
