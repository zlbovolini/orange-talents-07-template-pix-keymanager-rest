package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
internal class GrpcClientFactoryTest(
    private val grpcClient: RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub
) {

    @Test
    fun `deve criar servico gRPC de registro chave pix`() {
        assertNotNull(grpcClient)
    }
}