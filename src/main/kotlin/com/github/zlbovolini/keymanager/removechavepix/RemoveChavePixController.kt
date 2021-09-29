package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.grpc.RemoveChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.validation.constraints.NotBlank

@Validated
@Controller("/api/clientes/{clienteId}")
class RemoveChavePixController(
    val grpcClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub
) {

    @Delete("/pix/{pixId}")
    fun remove(@PathVariable clienteId: String, @PathVariable @NotBlank pixId: String): HttpResponse<Unit> {

        val grpcRequest = RemoveChavePixRequest.newBuilder()
            .setChaveId(pixId)
            .setClienteId(clienteId)
            .build()

        grpcClient.remove(grpcRequest)

        return HttpResponse.noContent()
    }
}