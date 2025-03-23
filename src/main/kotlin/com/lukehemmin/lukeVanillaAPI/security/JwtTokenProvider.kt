package com.lukehemmin.lukeVanillaAPI.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    private lateinit var secretString: String

    @Value("\${jwt.expiration}")
    private var expirationTime: Long = 0

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretString.toByteArray())
    }

    fun createToken(username: String, roles: List<String>): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles

        val now = Date()
        val validity = Date(now.time + expirationTime)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities = (claims["roles"] as List<*>).map { SimpleGrantedAuthority("ROLE_$it") }

        return UsernamePasswordAuthenticationToken(claims.subject, "", authorities)
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            
            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }

    fun getUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}
