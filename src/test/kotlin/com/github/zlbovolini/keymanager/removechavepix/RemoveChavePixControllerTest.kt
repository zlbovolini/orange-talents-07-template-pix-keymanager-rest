package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.comum.GrpcClientFactory
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixServiceGrpc
import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class RemoveChavePixControllerTest(
    @Client("/")
    private val httpClient: HttpClient,
    private val grpcClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub
) {

    @BeforeEach
    fun setUp() {
        Mockito.reset(grpcClient)
    }

    @Test
    fun `deve remover chave pix registrada`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val request = HttpRequest.DELETE<Any>("/api/clientes/$clienteId/pix/$pixId")

        given(grpcClient.remove(any())).willReturn(Empty.newBuilder().build())

        val response = httpClient.toBlocking().exchange(request, HttpResponse::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.status)
    }

    @Test
    fun `deve retornar 404 quando chave nao existe`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val request = HttpRequest.DELETE<Any>("/api/clientes/$clienteId/pix/$pixId")

        given(grpcClient.remove(any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, HttpResponse::class.java)
        }

        assertNotNull(error)
        assertEquals(HttpStatus.NOT_FOUND, error.status)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class StubFactory {

        @Singleton
        fun removeGrpc() = Mockito.mock(RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub::class.java)
    }
}