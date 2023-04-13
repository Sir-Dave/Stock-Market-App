package com.sirdave.composeapp.domain.repository

import com.sirdave.composeapp.domain.model.CompanyInfo
import com.sirdave.composeapp.domain.model.CompanyListing
import com.sirdave.composeapp.domain.model.IntradayInfo
import com.sirdave.composeapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>
}