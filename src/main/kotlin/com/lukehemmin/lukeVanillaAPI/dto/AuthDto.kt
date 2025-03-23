package com.lukehemmin.lukeVanillaAPI.dto

data class AuthCodeRequest(
    val uuid: String
)

data class AuthCodeValidationRequest(
    val authCode: String,
    val discordId: String
)

data class AuthResponse(
    val authCode: String
)
