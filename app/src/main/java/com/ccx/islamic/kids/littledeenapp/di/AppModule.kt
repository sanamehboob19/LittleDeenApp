package com.ccx.islamic.kids.littledeenapp.di


import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.ccx.islamic.kids.littledeenapp.api.YouTubeOEmbedApi
import com.ccx.islamic.kids.littledeenapp.db.AppDatabase
import com.ccx.islamic.kids.littledeenapp.db.StreakDao
import com.ccx.islamic.kids.littledeenapp.db.TasbeehDao
import com.ccx.islamic.kids.littledeenapp.db.VideoDao
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideYouTubeOEmbedApi(): YouTubeOEmbedApi {
        return Retrofit.Builder()
            .baseUrl(YouTubeOEmbedApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YouTubeOEmbedApi::class.java)
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "little_deen_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(database: AppDatabase): VideoDao {
        return database.videoDao()
    }

    @Provides
    @Singleton
    fun provideStreakDao(database: AppDatabase): StreakDao {
        return database.streakDao()
    }


    @Provides
    @Singleton
    fun provideTasbeehDao(database: AppDatabase): TasbeehDao {
        return database.tasbeehDao()
    }




    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)
    }
}