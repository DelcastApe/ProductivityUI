package com.example.productivityui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * date => "YYYY-MM-DD" (ej. 2025-11-07)
 */
@Entity(tableName = "calendar_task")
data class CalendarTaskEntity(
    @PrimaryKey val id: String,
    val date: String,
    val text: String,
    val createdAt: Long
)
