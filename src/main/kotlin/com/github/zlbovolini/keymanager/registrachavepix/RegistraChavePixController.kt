package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import java.net.URI
import javax.validation.Valid

@Validated
@Controller("/api/clientes/{clienteId}")
class RegistraChavePixController(
    val grpcClient: RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub
) {

    @Post("/pix")
    fun registra(
        @PathVariable clienteId: String,
        @Valid @Body request: NovaChavePixHttpRequest
    ): HttpResponse<NovaChavePixHttpResponse> {

        val grpcRequest = request.toGrpcRequest(clienteId)
        val response = grpcClient.registra(grpcRequest)
        val location = URI.create("/api/clientes/${clienteId}/pix/${response.pixId}")

        return HttpResponse.created(NovaChavePixHttpResponse(response), location)
    }
}