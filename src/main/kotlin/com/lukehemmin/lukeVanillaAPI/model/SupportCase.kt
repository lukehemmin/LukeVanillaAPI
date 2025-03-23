package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "support_chat_link")
data class SupportCase(
    @Id
    @Column(name = "support_id", length = 36)
    val supportId: String,
    
    @Column(length = 36, nullable = false)
    val uuid: String,
    
    @Column(name = "message_link", nullable = false)
    val messageLink: String,
    
    @Column(name = "case_close", nullable = false)
    var isClosed: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
