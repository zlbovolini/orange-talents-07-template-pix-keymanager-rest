package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.grpc.TipoChaveMessage
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoChave(
    val tipoChaveGrpc: TipoChaveMessage
) {
    CPF(TipoChaveMessage.CPF) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            if (!chave.matches("^[0-9]{11}\$".toRegex())) {
                return false
            }

            if (!CPFValidator().run {
                    initialize(null)
                    isValid(chave, null)
                }) {
                return false
            }

            return true
        }
    },
    CELULAR(TipoChaveMessage.CELULAR) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(TipoChaveMessage.EMAIL) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    ALEATORIA(TipoChaveMessage.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?): Boolean
}