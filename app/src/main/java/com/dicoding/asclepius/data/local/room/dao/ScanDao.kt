package com.dicoding.asclepius.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.room.entity.Scan

@Dao
interface ScanDao {
    @Query("SELECT * FROM scans")
    suspend fun getALl(): List<Scan>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScan(scan: Scan)

    @Delete
    suspend fun deleteScan(scan: Scan)
}