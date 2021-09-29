package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.grpc.ConsultaChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.ConsultaPorIdOuChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated

@Validated
@Controller("/api/clientes/{clienteId}")
class ConsultaChavePixController(
    val grpcClient: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub
) {

    @Get("/pix/{pixId}")
    fun consulta(
        @PathVariable clienteId: String,
        @PathVariable pixId: String
    ): HttpResponse<DadosChavePixHttpResponse> {

        val grpcRequest = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setClienteId(clienteId)
                    .setPixId(pixId)
                    .build()
            )
            .build()

        val response = grpcClient.consulta(grpcRequest)

        return HttpResponse.ok(DadosChavePixHttpResponse.of(response))
    }
}