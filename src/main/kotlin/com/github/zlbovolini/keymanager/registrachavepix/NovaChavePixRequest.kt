package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.comum.validacao.Pix
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Pix
@Introspected
data class NovaChavePixRequest(

    @field:NotNull
    val tipoChave: TipoChave,

    @field:Size(max = 77)
    val chave: String,

    @field:NotBlank
    val tipoConta: TipoConta
) {

    fun toGrpcRequest(clienteId: String): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId)
            .setTipoChave(tipoChave.tipoChaveGrpc)
            .setChave(chave)
            .setTipoConta(tipoConta.tipoContaMessage)
            .build()
    }
}
