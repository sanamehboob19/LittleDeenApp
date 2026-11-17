package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TasbeehDao {
    @Query("SELECT * FROM tasbeeh_counts")
    fun getAllTasbeeh(): Flow<List<TasbeehEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbeeh(tasbeeh: TasbeehEntity)

    @Query("UPDATE tasbeeh_counts SET currentCount = :count WHERE dhikrId = :id")
    suspend fun updateCount(id: String, count: Int)

    @Query("UPDATE tasbeeh_counts SET currentCount = 0 WHERE dhikrId = :id")
    suspend fun resetCount(id: String)
}