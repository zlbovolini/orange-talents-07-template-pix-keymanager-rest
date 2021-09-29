package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixResponse
import io.micronaut.core.annotation.Introspected

@Introspected
data class NovaChavePixHttpResponse(
    val pixId: String
) {
    constructor(response: RegistraChavePixResponse) :
            this(response.pixId)
}