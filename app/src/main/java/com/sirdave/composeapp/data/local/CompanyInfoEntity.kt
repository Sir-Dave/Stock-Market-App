package com.sirdave.composeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyInfo")
data class CompanyInfoEntity (
    @PrimaryKey val id: Int? = null,
    val name: String,
    val symbol: String,
    val description: String,
    val industry: String,
    val country: String
)