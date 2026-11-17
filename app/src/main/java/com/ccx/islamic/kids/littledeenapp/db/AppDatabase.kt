package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Database
import androidx.room.RoomDatabase



@Database(
    entities = [
        SavedVideoEntity::class,
        SavedChannelEntity::class,
        PlaylistEntity::class,
        PlaylistVideoCrossRef::class,
        CachedVideoEntity::class,
        StreakEntity::class ,
        TasbeehEntity::class// 👈 Added here
    ],
    version = 5, // Bumped version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun streakDao(): StreakDao // 👈 Added here
    abstract fun tasbeehDao(): TasbeehDao
}