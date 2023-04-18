package com.sirdave.composeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CompanyListingEntity::class,
        CompanyInfoEntity::class,
        IntradayInfoEntity::class
],
    version = 1
)
abstract class StockDatabase: RoomDatabase() {
    abstract val dao : StockDao
}