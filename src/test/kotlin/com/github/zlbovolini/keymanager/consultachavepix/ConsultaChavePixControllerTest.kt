package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.comum.GrpcClientFactory
import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.grpc.*
import io.grpc.Status
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class ConsultaChavePixControllerTest(
    @Client("/")
    private val httpClient: HttpClient,
    private val grpcClient: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub
) {

    val clienteId = UUID.randomUUID().toString()
    val pixId = UUID.randomUUID().toString()
    val chave = UUID.randomUUID().toString()

    @BeforeEach
    fun setUp() {
        Mockito.reset(grpcClient)
    }

    @Test
    fun `deve retornar dados de chave registrada`() {

        val grpcRequest = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setClienteId(clienteId)
                    .setPixId(pixId)
                    .build()
            )
            .build()

        given(grpcClient.consulta(grpcRequest)).willReturn(dadosChave())
        val request = HttpRequest.GET<HttpResponse<DadosChavePixHttpResponse>>("/api/clientes/$clienteId/pix/$pixId")

        val response = httpClient.toBlocking().exchange(request, DadosChavePixHttpResponse::class.java)

        assertEquals(DadosChavePixHttpResponse.of(dadosChave()), response.body())
    }

    @Test
    fun `nao deve retornar dados de chave nao registrada`() {

        val pixIdNaoRegistrado = UUID.randomUUID().toString()

        val grpcRequest = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setClienteId(clienteId)
                    .setPixId(pixIdNaoRegistrado)
                    .build()
            )
            .build()

        given(grpcClient.consulta(grpcRequest)).willThrow(Status.NOT_FOUND.asRuntimeException())

        val request = HttpRequest.GET<HttpResponse<DadosChavePixHttpResponse>>("/api/clientes/$clienteId/pix/$pixIdNaoRegistrado")


        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, DadosChavePixHttpResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, error.status)
    }

    @Test
    fun `nao deve retornar dados quando requisicao invalida`() {

        val grpcRequest = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setClienteId("0")
                    .setPixId("0")
                    .build()
            )
            .build()

        given(grpcClient.consulta(grpcRequest)).willThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val request = HttpRequest.GET<Any>("/api/clientes/0/pix/0")

        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
    }

    private fun dadosChave(): DadosChavePixResponse {
        val titular = TitularResponse.newBuilder()
            .setNome("Yuri")
            .setCpf("02467781054")
            .build()

        val conta = DadosContaResponse.newBuilder()
            .setInstituicao("ITAÚ UNIBANCO S.A.")
            .setTipoConta(TipoConta.CONTA_CORRENTE.name)
            .setAgencia("0001")
            .setNumero("123213")
            .build()

        return DadosChavePixResponse.newBuilder()
            .setChaveId(pixId)
            .setClienteId(clienteId)
            .setTipoChave(TipoChave.ALEATORIA.name)
            .setChave(chave)
            .setTitular(titular)
            .setConta(conta)
            .build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class StubFactory {

        @Singleton
        fun consultaGrpc() = Mockito.mock(ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub::class.java)
    }
}