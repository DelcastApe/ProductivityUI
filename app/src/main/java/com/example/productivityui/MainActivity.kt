package com.example.productivityui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productivityui.data.CalendarTaskEntity
import com.example.productivityui.data.NoteEntity
import com.example.productivityui.data.TodoEntity
import com.example.productivityui.ui.NotesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ProductivityUIScreen() }
    }
}

@Composable
fun ProductivityUIScreen(vm: NotesViewModel = viewModel()) {
    // Colores base
    val bg = Color(0xFF0E1722)
    val cardColor = Color(0xFF121E2B)
    val borderColor = Color(0xFF1F2B3A)
    val primaryText = Color(0xFFE7EEF9)
    val secondaryText = Color(0xFF9FB2CC)
    val accent = Color(0xFF4D79BD)

    // Avatar (en memoria)
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) avatarUri = uri }

    // Estados VM
    val notes: List<NoteEntity> = vm.notes.collectAsStateWithLifecycle().value
    val todos: List<TodoEntity> = vm.todos.collectAsStateWithLifecycle().value
    val selectedDate: String = vm.selectedDate.collectAsStateWithLifecycle().value
    val dayTasks: List<CalendarTaskEntity> = vm.dayTasks.collectAsStateWithLifecycle().value

    // Dialog flags
    val showNewNote = remember { mutableStateOf(false) }
    val showNewTodo = remember { mutableStateOf(false) }
    val showNewCalendarTask = remember { mutableStateOf(false) }

    // Dimensiones adaptativas
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val outerPad: Dp
    val gap: Dp
    val cardPad: Dp
    val titleSize: TextUnit
    val bodySize: TextUnit
    val btnH: Dp
    val avatarSize: Dp
    when {
        screenWidthDp < 900 -> {
            outerPad = 12.dp; gap = 10.dp; cardPad = 12.dp
            titleSize = 18.sp; bodySize = 13.sp
            btnH = 40.dp; avatarSize = 40.dp
        }
        screenWidthDp < 1100 -> {
            outerPad = 16.dp; gap = 12.dp; cardPad = 14.dp
            titleSize = 20.sp; bodySize = 14.sp
            btnH = 44.dp; avatarSize = 48.dp
        }
        else -> {
            outerPad = 20.dp; gap = 16.dp; cardPad = 18.dp
            titleSize = 22.sp; bodySize = 14.sp
            btnH = 48.dp; avatarSize = 52.dp
        }
    }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(outerPad)) {

            // Header (SIN la “X”)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(Color(0xFF0B1120))
                        .border(1.dp, borderColor, CircleShape)
                        .clickable { pickImage.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(avatarUri),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("＋", color = secondaryText, fontSize = 20.sp)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "PRODUCTIVITY UI",
                    color = primaryText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(gap))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(gap)) {

                NotesCard(
                    notes = notes,
                    onAddClick = { showNewNote.value = true },
                    onDelete = { id -> vm.deleteNote(id) },
                    cardColor = cardColor,
                    borderColor = borderColor,
                    accent = accent,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    cardPad = cardPad,
                    titleSize = titleSize,
                    bodySize = bodySize,
                    btnHeight = btnH,
                    modifier = Modifier.weight(1f)
                )

                TodoCard(
                    todos = todos,
                    onAddClick = { showNewTodo.value = true },
                    onDeleteOne = { id -> vm.deleteTodo(id) },
                    onComplete = { ids -> vm.completeTodos(ids) },
                    cardColor = cardColor,
                    borderColor = borderColor,
                    accent = accent,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    cardPad = cardPad,
                    titleSize = titleSize,
                    bodySize = bodySize,
                    btnHeight = btnH,
                    modifier = Modifier.weight(1f)
                )

                CalendarCard(
                    selectedDate = selectedDate,
                    dayTasks = dayTasks,
                    onSelectDate = { date -> vm.setSelectedDate(date) },
                    onAddClick = { showNewCalendarTask.value = true },
                    onDelete = { id -> vm.deleteCalendarTask(id) },
                    cardColor = cardColor,
                    borderColor = borderColor,
                    accent = accent,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    cardPad = cardPad,
                    titleSize = titleSize,
                    bodySize = bodySize,
                    btnHeight = btnH,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Diálogos
    if (showNewNote.value) {
        TextInputDialog(
            title = "New Note",
            placeholder = "Write a note...",
            onDismiss = { showNewNote.value = false },
            onConfirm = { txt -> vm.addNote(txt); showNewNote.value = false }
        )
    }
    if (showNewTodo.value) {
        TextInputDialog(
            title = "New Todo",
            placeholder = "e.g., Finish report",
            onDismiss = { showNewTodo.value = false },
            onConfirm = { txt -> vm.addTodo(txt); showNewTodo.value = false }
        )
    }
    if (showNewCalendarTask.value) {
        TextInputDialog(
            title = "New Task for Day",
            placeholder = "e.g., Call supplier",
            onDismiss = { showNewCalendarTask.value = false },
            onConfirm = { txt -> vm.addCalendarTask(txt); showNewCalendarTask.value = false }
        )
    }
}

/* ----- Notes Card ----- */
@Composable
private fun NotesCard(
    notes: List<NoteEntity>,
    onAddClick: () -> Unit,
    onDelete: (String) -> Unit,
    cardColor: Color,
    borderColor: Color,
    accent: Color,
    primaryText: Color,
    secondaryText: Color,
    cardPad: Dp,
    titleSize: TextUnit,
    bodySize: TextUnit,
    btnHeight: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(Modifier.padding(cardPad)) {
            Text("QUICK NOTES", color = primaryText, fontWeight = FontWeight.Medium, fontSize = titleSize)
            Spacer(Modifier.height(8.dp))
            if (notes.isEmpty()) {
                Text("No notes yet", color = secondaryText, fontSize = bodySize)
            } else {
                notes.forEach { n ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("• ${n.text}", color = secondaryText, fontSize = bodySize, modifier = Modifier.weight(1f))
                        TextButton(onClick = { onDelete(n.id) }) { Text("🗑") }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(btnHeight)
            ) { Text("+ New Note") }
        }
    }
}

/* ----- Todo Card ----- */
@Composable
private fun TodoCard(
    todos: List<TodoEntity>,
    onAddClick: () -> Unit,
    onDeleteOne: (String) -> Unit,
    onComplete: (List<String>) -> Unit,
    cardColor: Color,
    borderColor: Color,
    accent: Color,
    primaryText: Color,
    secondaryText: Color,
    cardPad: Dp,
    titleSize: TextUnit,
    bodySize: TextUnit,
    btnHeight: Dp,
    modifier: Modifier = Modifier
) {
    val selected = remember(todos) { mutableStateOf(setOf<String>()) }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(Modifier.padding(cardPad)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("TO-DO LIST", color = primaryText, fontWeight = FontWeight.Medium, fontSize = titleSize)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onAddClick) { Text("+") }
            }
            Spacer(Modifier.height(8.dp))

            if (todos.isEmpty()) {
                Text("No todos yet", color = secondaryText, fontSize = bodySize)
            } else {
                todos.forEach { t ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isChecked = selected.value.contains(t.id)
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { v ->
                                selected.value = if (v) selected.value + t.id else selected.value - t.id
                            }
                        )
                        Text(t.text, color = secondaryText, fontSize = bodySize, modifier = Modifier.weight(1f))
                        TextButton(onClick = { onDeleteOne(t.id) }) { Text("🗑") }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { onComplete(selected.value.toList()); selected.value = emptySet() },
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(btnHeight)
            ) { Text("Mark as Completed") }
        }
    }
}

/* ----- Calendar Card ----- */
@Composable
private fun CalendarCard(
    selectedDate: String,
    dayTasks: List<CalendarTaskEntity>,
    onSelectDate: (String) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (String) -> Unit,
    cardColor: Color,
    borderColor: Color,
    accent: Color,
    primaryText: Color,
    secondaryText: Color,
    cardPad: Dp,
    titleSize: TextUnit,
    bodySize: TextUnit,
    btnHeight: Dp,
    modifier: Modifier = Modifier
) {
    val df = DateTimeFormatter.ISO_LOCAL_DATE
    val base = LocalDate.parse(selectedDate, df)
    fun weekAround(date: LocalDate) = (0..6).map { date.minusDays(3).plusDays(it.toLong()) }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(Modifier.padding(cardPad), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("CALENDAR", color = primaryText, fontWeight = FontWeight.Medium, fontSize = titleSize)
            Spacer(Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                weekAround(base).forEach { d ->
                    val isSelected = d == base
                    val label = d.dayOfWeek.name.first().toString()
                    Box(
                        modifier = Modifier
                            .size(if (btnHeight < 44.dp) 32.dp else 40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) accent else Color.Transparent)
                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable { onSelectDate(d.format(df)) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color = if (isSelected) Color.White else secondaryText,
                            fontWeight = FontWeight.Bold,
                            fontSize = bodySize
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Tasks for $base", color = secondaryText, fontSize = bodySize)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onAddClick) { Text("+") }
            }

            if (dayTasks.isEmpty()) {
                Text("No tasks this day", color = secondaryText, fontSize = bodySize)
            } else {
                dayTasks.forEach { t ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("• ${t.text}", color = secondaryText, fontSize = bodySize, modifier = Modifier.weight(1f))
                        TextButton(onClick = { onDelete(t.id) }) { Text("🗑") }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { /* future: open week view */ },
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(btnHeight)
            ) { Text("View Week") }
        }
    }
}

/* ----- Diálogo genérico ----- */
@Composable
private fun TextInputDialog(
    title: String,
    placeholder: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { v -> input = v },
                placeholder = { Text(placeholder) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (input.isNotBlank()) onConfirm(input.trim()) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
