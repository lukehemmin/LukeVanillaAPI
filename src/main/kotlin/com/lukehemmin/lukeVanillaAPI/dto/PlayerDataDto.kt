package com.lukehemmin.lukeVanillaAPI.dto

data class PlayerDataDto(
    val uuid: String,
    val nickname: String,
    val discordId: String? = null
)
