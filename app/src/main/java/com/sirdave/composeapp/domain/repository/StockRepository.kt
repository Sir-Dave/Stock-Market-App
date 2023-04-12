package com.sirdave.composeapp.domain.repository

import com.sirdave.composeapp.domain.model.CompanyListing
import com.sirdave.composeapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}