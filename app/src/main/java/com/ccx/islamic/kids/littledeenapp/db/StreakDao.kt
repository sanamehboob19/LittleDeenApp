package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {

    @Query("SELECT * FROM learning_streak WHERE id = 1")
    fun getStreakFlow(): Flow<StreakEntity?>

    @Query("SELECT * FROM learning_streak WHERE id = 1")
    suspend fun getStreakSync(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStreak(streak: StreakEntity)
}