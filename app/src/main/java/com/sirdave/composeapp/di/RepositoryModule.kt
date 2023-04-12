package com.sirdave.composeapp.di

import com.sirdave.composeapp.data.csv.CSVParser
import com.sirdave.composeapp.data.csv.CompanyListingsParser
import com.sirdave.composeapp.data.repository.StockRepositoryImpl
import com.sirdave.composeapp.domain.model.CompanyListing
import com.sirdave.composeapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParse(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}