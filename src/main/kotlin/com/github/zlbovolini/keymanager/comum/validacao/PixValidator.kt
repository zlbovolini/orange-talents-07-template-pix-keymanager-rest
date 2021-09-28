package com.github.zlbovolini.keymanager.comum.validacao

import com.github.zlbovolini.keymanager.registrachavepix.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

private const val TAMANHO_MAXIMO_CHAVE = 77

@Singleton
class ChavePixValidator : ConstraintValidator<Pix, NovaChavePixRequest> {

    override fun isValid(
        request: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<Pix>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (request == null || request.chave.length > TAMANHO_MAXIMO_CHAVE) {
            return false
        }

        return request.tipoChave.valida(request.chave)
    }
}