package com.fatec.brasilapi.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fatec.brasilapi.model.AddressResponse
import com.fatec.brasilapi.ui.screen.AddressCard
import com.fatec.brasilapi.ui.theme.BrasilapiTheme

@Preview(showBackground = true)
@Composable
fun AddressCardPreview() {
    BrasilapiTheme {
        AddressCard(
            address = AddressResponse(
                cep = "01310100",
                state = "SP",
                city = "São Paulo",
                district = "Bela Vista",
                street = "Avenida Paulista"
            )
        )
    }
}
