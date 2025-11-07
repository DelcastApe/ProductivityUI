package com.example.productivityui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun streamNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM notes")
    suspend fun clearAll()
}
