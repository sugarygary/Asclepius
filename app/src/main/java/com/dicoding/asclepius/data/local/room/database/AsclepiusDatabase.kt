package com.dicoding.asclepius.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.room.dao.ScanDao
import com.dicoding.asclepius.data.local.room.entity.Scan

@Database(
    entities = [Scan::class], version = 1, exportSchema = false
)
abstract class AsclepiusDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}