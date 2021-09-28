package com.github.zlbovolini.keymanager.comum.validacao

import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [ChavePixValidator::class])
annotation class Pix(
    val message: String = "Chave Pix inválida"
)
