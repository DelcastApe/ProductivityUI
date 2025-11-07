package com.example.productivityui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NoteEntity::class,
        TodoEntity::class,
        CalendarTaskEntity::class
    ],
    version = 2, // ⬅️ subimos versión (modo dev)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
    abstract fun calendarDao(): CalendarDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "productivity.db"
                )
                    // En dev, si cambias schema, borra y recrea.
                    // Cuando estabilicemos el schema, implementamos migraciones.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}

