package com.sirdave.composeapp.data.mapper

import com.sirdave.composeapp.data.local.CompanyInfoEntity
import com.sirdave.composeapp.data.local.CompanyListingEntity
import com.sirdave.composeapp.data.remote.dto.CompanyInfoDto
import com.sirdave.composeapp.domain.model.CompanyInfo
import com.sirdave.composeapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoEntity.toCompanyInfo(): CompanyInfo{
    return CompanyInfo(
        name = name,
        symbol = symbol,
        description = description,
        industry = industry,
        country = country
    )
}

fun CompanyInfoDto.toCompanyInfoEntity(): CompanyInfoEntity{
    return CompanyInfoEntity(
        name = name ?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        industry = industry ?: "",
        country = country ?: ""
    )
}