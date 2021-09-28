package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import java.net.URI
import javax.validation.Valid

@Controller("/api/clientes/{clienteId}")
@Validated
class RegistraChavePixController(
    val grpcClient: RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub
) {

    @Post("/pix")
    fun registra(
        @PathVariable clienteId: String,
        @Valid @Body request: NovaChavePixRequest
    ): HttpResponse<NovaChavePixResponse> {

        val grpcRequest = request.toGrpcRequest(clienteId)
        val response = grpcClient.registra(grpcRequest)
        val location = URI.create("/api/clientes/${clienteId}/pix/${response.pixId}")

        return HttpResponse.created(NovaChavePixResponse(response), location)
    }
}