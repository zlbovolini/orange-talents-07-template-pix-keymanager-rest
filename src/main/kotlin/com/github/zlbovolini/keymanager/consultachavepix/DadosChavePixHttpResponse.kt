package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.grpc.DadosChavePixResponse

data class DadosChavePixHttpResponse(
    val chaveId: String,
    val clienteId: String,
    val tipoChave: String,
    val chave: String,
    val titular: DadosTitular,
    val conta: DadosConta
) {
    companion object {
        fun of(response: DadosChavePixResponse): DadosChavePixHttpResponse {
            return with(response) {
                val titular = DadosTitular(titular.nome, titular.cpf)
                val conta = DadosConta(conta.instituicao, conta.tipoConta, conta.agencia, conta.numero)

                DadosChavePixHttpResponse(chaveId, clienteId, tipoChave, chave, titular, conta)
            }
        }
    }
}


data class DadosTitular(
    val nome: String,
    val cpf: String
)

data class DadosConta(
    val instituicao: String,
    val tipoConta: String,
    val agencia: String,
    val numero: String
)