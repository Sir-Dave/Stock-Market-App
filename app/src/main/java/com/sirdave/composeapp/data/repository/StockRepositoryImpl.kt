package com.sirdave.composeapp.data.repository

import android.util.Log
import com.sirdave.composeapp.data.csv.CSVParser
import com.sirdave.composeapp.data.local.StockDatabase
import com.sirdave.composeapp.data.mapper.toCompanyInfo
import com.sirdave.composeapp.data.mapper.toCompanyInfoEntity
import com.sirdave.composeapp.data.mapper.toCompanyListing
import com.sirdave.composeapp.data.mapper.toCompanyListingEntity
import com.sirdave.composeapp.data.remote.StockApi
import com.sirdave.composeapp.domain.model.CompanyInfo
import com.sirdave.composeapp.domain.model.CompanyListing
import com.sirdave.composeapp.domain.model.IntradayInfo
import com.sirdave.composeapp.domain.repository.StockRepository
import com.sirdave.composeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    db: StockDatabase,
    private val api: StockApi,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
): StockRepository {

    private val dao = db.dao

    private val TAG = "StockRepositoryImpl"

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)

            emit(Resource.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            }
            catch (ex: IOException){
                ex.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }
            catch (ex: HttpException){
                ex.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )

                dao.searchCompanyListing("").map { it.toCompanyListing() }
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val localListing = dao.getCompanyBySymbol(symbol)

            if (localListing == null){
                val result = api.getCompanyInfo(symbol)
                dao.insertCompanyInfo(result.toCompanyInfoEntity())
                val newLocalListing = dao.getCompanyBySymbol(symbol)
                Resource.Success(newLocalListing?.toCompanyInfo())
            }
            else Resource.Success(localListing.toCompanyInfo())

        }

        catch (ex: IOException){
            ex.printStackTrace()
            Resource.Error("Couldn't load company info")
        }
        catch (ex: HttpException){
            ex.printStackTrace()
            Resource.Error("Couldn't load company info")
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        }

        catch (ex: IOException){
            ex.printStackTrace()
            Resource.Error("Couldn't load intraday info")
        }
        catch (ex: HttpException){
            ex.printStackTrace()
            Resource.Error("Couldn't load intraday info")
        }
    }
}