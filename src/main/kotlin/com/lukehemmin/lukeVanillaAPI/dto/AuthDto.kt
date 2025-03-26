package com.lukehemmin.lukeVanillaAPI.dto

data class AuthCodeDto(
    val uuid: String,
    val authCode: String
)

data class AuthValidationDto(
    val uuid: String,
    val isAuth: Boolean,
    val authCode: String
)

data class AuthRequestDto(
    val uuid: String
)

data class ValidateAuthRequestDto(
    val authCode: String,
    val discordId: String
)
