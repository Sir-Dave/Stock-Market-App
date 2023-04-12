package com.sirdave.composeapp.data.repository

import com.sirdave.composeapp.data.csv.CSVParser
import com.sirdave.composeapp.data.local.StockDatabase
import com.sirdave.composeapp.data.mapper.toCompanyListing
import com.sirdave.composeapp.data.mapper.toCompanyListingEntity
import com.sirdave.composeapp.data.remote.StockApi
import com.sirdave.composeapp.domain.model.CompanyListing
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
    val db: StockDatabase,
    val api: StockApi,
    val companyListingParser: CSVParser<CompanyListing>
): StockRepository {

    val dao = db.dao

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
}