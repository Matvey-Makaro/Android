package com.example.tabatatimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.Training
import com.example.tabatatimer.database.models.TrainingDao

@Database(entities = [Training::class, Interval::class], version = 1)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun trainingDao() : TrainingDao

    companion object {
        @Volatile
        private var instance : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            return instance ?: synchronized(this)
            {
                val tmp = Room.databaseBuilder(context, AppDatabase::class.java,
                    "app_database").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                instance = tmp
                tmp
            }
        }
    }
}