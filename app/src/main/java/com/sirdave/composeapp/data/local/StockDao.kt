package com.sirdave.composeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(companyListingEntity: List<CompanyListingEntity>)

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    @Query("""SELECT * FROM
        companylistingentity WHERE
        LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
        UPPER(:query) == symbol
    """)
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyInfo(companyInfoEntity: CompanyInfoEntity)

    @Query("SELECT * FROM companyInfo WHERE symbol == :symbol")
    suspend fun getCompanyBySymbol(symbol: String): CompanyInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntraDayInfo(intradayInfoEntities: List<IntradayInfoEntity>)

    @Transaction
    @Query("SELECT * FROM companyInfo WHERE symbol == :symbol")
    suspend fun getCompanyAndIntradayInfo(symbol: String): CompanyInfoWithIntradayInfo?
}