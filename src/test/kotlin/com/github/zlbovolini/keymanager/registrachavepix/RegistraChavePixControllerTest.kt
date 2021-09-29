package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.GrpcClientFactory
import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixResponse
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.any
import java.util.*

@MicronautTest
internal class RegistraChavePixControllerTest(
    private val grpcClient: RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub,

    @Client(value = "/")
    private val httpClient: HttpClient
) {

    @Test
    fun `deve registrar chave pix com requisicao valida`() {

        val clienteId = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val novaChavePix = NovaChavePixHttpRequest(TipoChave.ALEATORIA, " ", TipoConta.CONTA_CORRENTE)
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = RegistraChavePixResponse.newBuilder()
            .setPixId(pixId)
            .build()

        given(grpcClient.registra(any())).willReturn(grpcResponse)

        val request = HttpRequest.POST("/api/clientes/$clienteId/pix", novaChavePix)
        val response = httpClient.toBlocking().exchange(request, NovaChavePixHttpResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class StubFactory {

        @Singleton
        fun registraGrpc() = Mockito.mock(RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub::class.java)
    }
}