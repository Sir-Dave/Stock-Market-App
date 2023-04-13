package com.sirdave.composeapp.data.csv

import com.opencsv.CSVReader
import com.sirdave.composeapp.data.mapper.toIntradayInfo
import com.sirdave.composeapp.data.remote.dto.IntradayInfoDto
import com.sirdave.composeapp.domain.model.CompanyListing
import com.sirdave.composeapp.domain.model.IntradayInfo
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor(): CSVParser<IntradayInfo> {
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return csvReader.readAll().drop(1).mapNotNull { line ->
            val timestamp = line.getOrNull(0) ?: return@mapNotNull null
            val close = line.getOrNull(4) ?: return@mapNotNull null

            val dto = IntradayInfoDto(
                timestamp = timestamp,
                close = close.toDouble()
            )
            dto.toIntradayInfo()

        }
            .filter {
                it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
            }
            .sortedBy {
                it.date.hour
            }
            .also { csvReader.close() }
    }
}