package com.example.productivityui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey val id: String,
    val text: String,
    val createdAt: Long
)
