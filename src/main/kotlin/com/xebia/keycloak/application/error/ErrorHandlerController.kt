package com.xebia.keycloak.application.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ErrorHandlerController {
    private val logger = LoggerFactory.getLogger(ErrorHandlerController::class.java)

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(exception: Exception): ResponseEntity<ErrorDto> {
        logger.error(exception.message, exception)
        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(ErrorDto(INTERNAL_SERVER_ERROR.value(), exception.message))
    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleRequestNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        logger.warn(exception.message)

        val errors = exception.bindingResult.fieldErrors
            .map {
                "Error on field ${it.field} : ${it.defaultMessage}"
            }

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(ErrorDto(BAD_REQUEST.value(), errors))
    }

    @ExceptionHandler(HttpMessageConversionException::class)
    fun handleBadRequestException(exception: HttpMessageConversionException): ResponseEntity<ErrorDto> {
        logger.warn(exception.message)

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(ErrorDto(BAD_REQUEST.value(), exception.rootCause?.message ?: exception.message))
    }

    data class ErrorDto(
        val status: Int,
        val error: Any?
    )
}
