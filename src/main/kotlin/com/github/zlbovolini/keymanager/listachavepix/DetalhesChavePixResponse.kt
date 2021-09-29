package com.github.zlbovolini.keymanager.listachavepix

import com.fasterxml.jackson.annotation.JsonFormat
import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.grpc.ListaChavesPixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant

@Introspected
data class DetalhesChavePixResponse(
    val pixId: String,
    val clienteId: String,
    val tipoChave: TipoChave,
    val chave: String,
    val tipoConta: TipoConta,
    //@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
    val criadoEm: Instant
) {

    companion object {
        fun of(response: ListaChavesPixResponse.ChavePixResponse): DetalhesChavePixResponse {
            return with(response) {
                DetalhesChavePixResponse(
                    pixId,
                    clienteId,
                    TipoChave.valueOf(tipoChave.name),
                    chave,
                    TipoConta.valueOf(tipoConta.name),
                    Instant.parse(criadoEm)
                )
            }
        }
    }

}