package com.github.zlbovolini.keymanager.listachavepix

import com.github.zlbovolini.keymanager.grpc.ListaChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.ListaChavesPixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated

@Validated
@Controller("/api/clientes/{clienteId}")
class ListaChavesPixController(
    val grpcClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub
) {

    @Get("/pix")
    fun lista(@PathVariable clienteId: String): HttpResponse<Collection<DetalhesChavePixResponse>> {

        val grpcRequest = ListaChavesPixRequest.newBuilder()
            .setClienteId(clienteId)
            .build()

        val response = grpcClient.lista(grpcRequest)

        return HttpResponse.ok(response.chavesList.map { DetalhesChavePixResponse.of(it) })
    }
}