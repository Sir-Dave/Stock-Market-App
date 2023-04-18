package com.sirdave.composeapp.data.mapper

import com.sirdave.composeapp.data.local.IntradayInfoEntity
import com.sirdave.composeapp.data.remote.dto.IntradayInfoDto
import com.sirdave.composeapp.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntradayInfoDto.toIntradayInfo(): IntradayInfo{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )
}

fun IntradayInfo.toIntradayInfoEntity(symbol: String): IntradayInfoEntity{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val formattedDateTime = date.format(formatter)

    return IntradayInfoEntity(
        timestamp = formattedDateTime,
        close = close,
        companySymbol = symbol
    )
}

fun IntradayInfoEntity.toIntradayInfo(): IntradayInfo{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )
}