package com.example.productivityui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY createdAt DESC")
    fun streamTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM todo WHERE id IN (:ids)")
    suspend fun deleteMany(ids: List<String>)

    @Query("DELETE FROM todo")
    suspend fun clearAll()
}
