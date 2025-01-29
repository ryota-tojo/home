package com.example.home.util

import com.example.home.domain.value_object.Constants

data class ValidationResult(
    val result: Boolean,
    val target: String
)

object ValidationCheck {
    fun symbol(str: String?): ValidationResult {
        if (str == null) return ValidationResult(true, "")
        val invalidSymbols = Constants.INVALID_SYMBOL.filter { it in str }
        return if (invalidSymbols.isNotEmpty()) {
            ValidationResult(false, invalidSymbols.joinToString(","))
        } else {
            ValidationResult(true, "")
        }
    }

    fun word(str: String?): ValidationResult {
        if (str == null) return ValidationResult(true, "")
        val invalidWords = Constants.INVALID_WORD.filter { it in str }
        return if (invalidWords.isNotEmpty()) {
            ValidationResult(false, invalidWords.joinToString(","))
        } else {
            ValidationResult(true, "")
        }
    }

    fun symbolAndWord(str: String?): ValidationResult {
        if (str == null) return ValidationResult(true, "")
        val invalidSymbols = Constants.INVALID_SYMBOL.filter { it in str }
        val invalidWords = Constants.INVALID_WORD.filter { it in str }

        return if (invalidSymbols.isNotEmpty() || invalidWords.isNotEmpty()) {
            val combinedInvalidValues = buildString {
                if (invalidSymbols.isNotEmpty()) append("Symbols: ${invalidSymbols.joinToString(",")}")
                if (invalidWords.isNotEmpty()) {
                    if (invalidSymbols.isNotEmpty()) append(" | ")
                    append("Words: ${invalidWords.joinToString(",")}")
                }
            }
            ValidationResult(false, combinedInvalidValues)
        } else {
            ValidationResult(true, "")
        }
    }
}