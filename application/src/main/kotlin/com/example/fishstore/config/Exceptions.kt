package com.example.fishstore.config

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

class NotFoundException(devMessage: String, userMessage: String? = null, exception: Exception? = null) :
    WebApiException(devMessage = devMessage, userMessage = userMessage, httpStatus = HttpStatus.NOT_FOUND, cause = exception)

/**
 * General class for all api exceptions
 */
open class WebApiException(
    /**
     * Error message to present to developer
     */
    val devMessage: String,
    /**
     * Error message to present to user
     */
    val userMessage: String? = null,
    /**
     * Unique error code
     */
    val code: String? = null,
    /**
     * Response http-status
     */
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    /**
     * the cause (which is saved for later retrieval by the [Throwable::getCause] method).
     */
    cause: Throwable? = null
) : RuntimeException(devMessage, cause)

@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(value = [WebApiException::class])
    fun onApiException(ex: WebApiException, response: HttpServletResponse): Unit =
        response.sendError(ex.httpStatus.value(), ex.message)

    @ExceptionHandler(value = [NotImplementedError::class])
    fun onNotImplemented(ex: NotImplementedError, response: HttpServletResponse): Unit =
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value())

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun onConstraintViolation(ex: ConstraintViolationException, response: HttpServletResponse): Unit =
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.constraintViolations.joinToString(", ") { it.message })
}
