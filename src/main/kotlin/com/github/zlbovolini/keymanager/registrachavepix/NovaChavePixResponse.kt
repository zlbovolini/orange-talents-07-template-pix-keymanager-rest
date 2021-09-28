package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixResponse

data class NovaChavePixResponse(
    val pixId: String
) {
    constructor(response: RegistraChavePixResponse) :
            this(response.pixId)
}