package com.example.productivityui.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.productivityui.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class NotesViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)
    private val noteDao: NoteDao = db.noteDao()
    private val todoDao: TodoDao = db.todoDao()
    private val calendarDao: CalendarDao = db.calendarDao()

    // -------- Notes --------
    val notes: StateFlow<List<NoteEntity>> =
        noteDao.streamNotes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(text: String) = viewModelScope.launch {
        val e = NoteEntity(
            id = UUID.randomUUID().toString(),
            text = text.trim(),
            createdAt = System.currentTimeMillis()
        )
        noteDao.insert(e)
    }

    fun deleteNote(id: String) = viewModelScope.launch {
        noteDao.deleteById(id)
    }

    // -------- Todos --------
    val todos: StateFlow<List<TodoEntity>> =
        todoDao.streamTodos()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTodo(text: String) = viewModelScope.launch {
        val t = TodoEntity(
            id = UUID.randomUUID().toString(),
            text = text.trim(),
            createdAt = System.currentTimeMillis()
        )
        todoDao.insert(t)
    }

    fun completeTodos(ids: List<String>) = viewModelScope.launch {
        if (ids.isNotEmpty()) todoDao.deleteMany(ids)
    }

    fun deleteTodo(id: String) = viewModelScope.launch {
        todoDao.deleteById(id)
    }

    // -------- Calendar --------
    private val df = DateTimeFormatter.ISO_LOCAL_DATE
    private val _selectedDate = MutableStateFlow(LocalDate.now().format(df))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Tareas del día seleccionado (flujo reactivo a cambios de fecha)
    val dayTasks: StateFlow<List<CalendarTaskEntity>> =
        selectedDate.flatMapLatest { date ->
            calendarDao.streamTasksByDate(date)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun addCalendarTask(text: String) = viewModelScope.launch {
        val t = CalendarTaskEntity(
            id = UUID.randomUUID().toString(),
            date = _selectedDate.value,
            text = text.trim(),
            createdAt = System.currentTimeMillis()
        )
        calendarDao.insert(t)
    }

    fun deleteCalendarTask(id: String) = viewModelScope.launch {
        calendarDao.deleteById(id)
    }
}
