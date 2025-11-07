package com.example.productivityui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar_task WHERE date = :date ORDER BY createdAt DESC")
    fun streamTasksByDate(date: String): Flow<List<CalendarTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: CalendarTaskEntity)

    @Query("DELETE FROM calendar_task WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM calendar_task")
    suspend fun clearAll()
}
