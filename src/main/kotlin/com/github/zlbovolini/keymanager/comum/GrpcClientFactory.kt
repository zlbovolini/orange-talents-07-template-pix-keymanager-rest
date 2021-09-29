package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory(
    @GrpcChannel("key-manager") val channel: ManagedChannel
) {

    @Singleton
    fun registraChavePixGrpc() = RegistraChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChavePixGrpc() = RemoveChavePixServiceGrpc.newBlockingStub(channel)
}