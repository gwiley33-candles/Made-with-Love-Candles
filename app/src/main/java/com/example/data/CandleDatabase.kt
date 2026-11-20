package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedCandleEntity::class,
        CartItemEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CandleDatabase : RoomDatabase() {

    abstract fun candleDao(): CandleDao

    companion object {
        @Volatile
        private var INSTANCE: CandleDatabase? = null

        fun getDatabase(context: Context): CandleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CandleDatabase::class.java,
                    "wick_and_glow.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
