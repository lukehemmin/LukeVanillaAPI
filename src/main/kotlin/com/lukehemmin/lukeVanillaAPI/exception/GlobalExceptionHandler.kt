package com.lukehemmin.lukeVanillaAPI.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ex.message ?: "Resource not found",
            request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ex.message ?: "Invalid credentials",
            request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.UNAUTHORIZED)
    }
    
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ex.message ?: "Illegal state",
            request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ex.message ?: "Internal server error",
            request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorDetails(
    val timestamp: LocalDateTime,
    val message: String,
    val details: String
)
