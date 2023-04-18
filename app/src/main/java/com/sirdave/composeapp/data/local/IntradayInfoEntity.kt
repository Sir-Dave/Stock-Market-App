package com.sirdave.composeapp.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "intradayInfo")
data class IntradayInfoEntity(
    @PrimaryKey val id: Int? = null,
    val timestamp: String,
    val close: Double,
    val companySymbol: String
)


data class CompanyInfoWithIntradayInfo(
    @Embedded val company: CompanyInfoEntity,
    @Relation(
        parentColumn = "symbol",
        entityColumn = "companySymbol"
    )
    val intradayInfoEntities: List<IntradayInfoEntity>
)

