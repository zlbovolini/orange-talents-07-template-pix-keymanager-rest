package com.github.zlbovolini.keymanager.listachavepix

import com.github.zlbovolini.keymanager.comum.GrpcClientFactory
import com.github.zlbovolini.keymanager.grpc.*
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
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
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.Instant
import java.util.*

@MicronautTest
internal class ListaChavesPixControllerTest(
    @Client("/")
    private val httpClient: HttpClient,
    private val grpcClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub
) {

    val yuriClienteId = UUID.randomUUID().toString()
    val yuriPixId = UUID.randomUUID().toString()
    val yuriChave = UUID.randomUUID().toString()
    val yuriPixIdDois = UUID.randomUUID().toString()
    val yuriChaveDois = UUID.randomUUID().toString()

    val ponteClienteId = UUID.randomUUID().toString()
    val pontePixId = UUID.randomUUID().toString()
    val ponteChave = UUID.randomUUID().toString()

    val criadoEm = Instant.now().toString()

    @BeforeEach
    fun setUp() {
        Mockito.reset(grpcClient)
    }

    @Test
    fun `deve retornar lista de chaves registradas`() {

        val grpcRequest = ListaChavesPixRequest.newBuilder()
            .setClienteId(yuriClienteId)
            .build()

        given(grpcClient.lista(grpcRequest)).willReturn(listaChavesYuri())

        val request = HttpRequest.GET<Collection<DetalhesChavePixResponse>>("/api/clientes/$yuriClienteId/pix")

        val response = httpClient.toBlocking().exchange(request, Argument.listOf(DetalhesChavePixResponse::class.java))

        assertNotNull(response.body())
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(2, listaChavesYuri().chavesList.size)
        assertEquals(listaChavesYuri().chavesList.map { DetalhesChavePixResponse.of(it) }, response.body())
    }

    @Test
    fun `nao deve retornar chaves de outro cliente`() {

        val clientIdSemChaves = UUID.randomUUID().toString()

        val grpcRequest = ListaChavesPixRequest.newBuilder()
            .setClienteId(clientIdSemChaves)
            .build()

        given(grpcClient.lista(grpcRequest)).willReturn(ListaChavesPixResponse.newBuilder().build())

        val request =
            HttpRequest.GET<Collection<DetalhesChavePixResponse>>("/api/clientes/$clientIdSemChaves/pix")

        val response = httpClient.toBlocking().exchange(request, Collection::class.java)

        assertNotNull(response.body())
        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body().isEmpty())
    }

    @Test
    fun `nao deve retornar dados quando requisicao invalida`() {

        val grpcRequest = ListaChavesPixRequest.newBuilder()
            .setClienteId("0")
            .build()

        given(grpcClient.lista(grpcRequest)).willThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val request = HttpRequest.GET<Any>("/api/clientes/0/pix")

        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
    }

    private fun listaChavesYuri(): ListaChavesPixResponse {
        return ListaChavesPixResponse.newBuilder()
            .addAllChaves(
                listOf(
                    ListaChavesPixResponse.ChavePixResponse.newBuilder()
                        .setPixId(yuriPixId)
                        .setClienteId(yuriClienteId)
                        .setTipoChave(TipoChaveMessage.ALEATORIA)
                        .setChave(yuriChave)
                        .setTipoConta(TipoContaMessage.CONTA_POUPANCA)
                        .setCriadoEm(criadoEm)
                        .build(),
                    ListaChavesPixResponse.ChavePixResponse.newBuilder()
                        .setPixId(yuriPixIdDois)
                        .setClienteId(yuriClienteId)
                        .setTipoChave(TipoChaveMessage.ALEATORIA)
                        .setChave(yuriChaveDois)
                        .setTipoConta(TipoContaMessage.CONTA_CORRENTE)
                        .setCriadoEm(criadoEm)
                        .build()
                )
            )
            .build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class StubFactory {

        @Singleton
        fun listaGrpc() = Mockito.mock(ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub::class.java)
    }
}