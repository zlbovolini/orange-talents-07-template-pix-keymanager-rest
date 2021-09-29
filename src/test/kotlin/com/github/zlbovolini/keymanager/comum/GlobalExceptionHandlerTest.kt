package com.github.zlbovolini.keymanager.comum

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    private val request = HttpRequest.GET<Any>("?")

    @Test
    fun `deve retornar 400 quando argumento invalido`() {

        val error = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Argumento Inválido"))

        val response = GlobalExceptionHandler().handle(request, error)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertNotNull(response.body())
    }

    @Test
    fun `deve retornar 404 quando nao econtrado`() {

        val error = StatusRuntimeException(Status.NOT_FOUND.withDescription("Nao encontrado"))

        val response = GlobalExceptionHandler().handle(request, error)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertNotNull(response.body())
    }

    @Test
    fun `deve retornar 422 quando ja existir`() {

        val error = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("Já existe"))

        val response = GlobalExceptionHandler().handle(request, error)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertNotNull(response.body())
    }

    @Test
    fun `deve retornar 500 quando outro erro interno`() {

        val error = StatusRuntimeException(Status.INTERNAL.withDescription("Erro interno"))

        val response = GlobalExceptionHandler().handle(request, error)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNotNull(response.body())
    }
}