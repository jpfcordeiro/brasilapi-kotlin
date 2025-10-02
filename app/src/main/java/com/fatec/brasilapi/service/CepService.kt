package com.fatec.brasilapi.service

import com.fatec.brasilapi.model.AddressResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class CepService {
    
    companion object {
        private val httpClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    
    suspend fun getCepInfo(cep: String): Result<AddressResponse> {
        return try {
            val response: HttpResponse = httpClient.get("https://brasilapi.com.br/api/cep/v1/$cep")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val addressResponse: AddressResponse = response.body()
                    Result.success(addressResponse)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("CEP não encontrado"))
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("CEP inválido"))
                }
                else -> {
                    Result.failure(Exception("Erro na requisição: ${response.status}"))
                }
            }
        } catch (e: kotlinx.serialization.SerializationException) {
            Result.failure(Exception("Erro ao processar dados do CEP: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
}
