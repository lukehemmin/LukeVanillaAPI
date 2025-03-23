package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.SupportCase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupportCaseRepository : JpaRepository<SupportCase, String> {
    fun findByUuidAndIsClosed(uuid: String, isClosed: Boolean): List<SupportCase>
    fun findAllByIsClosed(isClosed: Boolean): List<SupportCase>
}
