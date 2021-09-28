package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.grpc.TipoContaMessage

enum class TipoConta(val tipoContaMessage: TipoContaMessage) {
    CONTA_CORRENTE(TipoContaMessage.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoContaMessage.CONTA_POUPANCA)
}
