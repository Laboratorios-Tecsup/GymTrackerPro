package pe.edu.tecsup.gymtrackerpro.screens.rutinas

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRutinaScreen(navController: NavController, rutinaId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    var ejercicio by remember { mutableStateOf("") }
    var grupoMuscular by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var repeticiones by remember { mutableStateOf("") }
    var pesoKg by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    
    var expandido by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val grupos = listOf("Pecho", "Espalda", "Hombros", "Bíceps", "Tríceps", "Piernas", "Abdomen", "Glúteos")

    LaunchedEffect(rutinaId) {
        val rutina = db.rutinaDao().buscarPorId(rutinaId)
        rutina?.let {
            ejercicio = it.ejercicio
            grupoMuscular = it.grupoMuscular
            series = it.series.toString()
            repeticiones = it.repeticiones.toString()
            pesoKg = it.pesoKg.toString()
            fecha = it.fecha
        }
        isLoading = false
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("¿Eliminar Rutina?", style = MaterialTheme.typography.headlineLarge) },
            text = { Text("Esta acción eliminará permanentemente este registro.", style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val rutina = db.rutinaDao().buscarPorId(rutinaId)
                            rutina?.let { db.rutinaDao().eliminar(it) }
                            mostrarDialogoEliminar = false
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Registro", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = White)
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogoEliminar = true }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Eliminar", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = White)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Actualizar Información",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            OutlinedTextField(
                                value = ejercicio,
                                onValueChange = { ejercicio = it },
                                label = { Text("Ejercicio") },
                                leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandido,
                                onExpandedChange = { expandido = !expandido }
                            ) {
                                OutlinedTextField(
                                    value = grupoMuscular,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Grupo Muscular") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandido,
                                    onDismissRequest = { expandido = false }
                                ) {
                                    grupos.forEach { grupo ->
                                        DropdownMenuItem(
                                            text = { Text(grupo) },
                                            onClick = {
                                                grupoMuscular = grupo
                                                expandido = false
                                            }
                                        )
                                    }
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = series,
                                    onValueChange = { series = it },
                                    label = { Text("Series") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                OutlinedTextField(
                                    value = repeticiones,
                                    onValueChange = { repeticiones = it },
                                    label = { Text("Reps") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }

                            OutlinedTextField(
                                value = pesoKg,
                                onValueChange = { pesoKg = it },
                                label = { Text("Peso (kg)") },
                                leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )

                            Box(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }) {
                                OutlinedTextField(
                                    value = fecha,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = false,
                                    label = { Text("Fecha") },
                                    leadingIcon = { Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        if (ejercicio.isBlank() || grupoMuscular.isBlank() ||
                                            series.isBlank() || repeticiones.isBlank() || pesoKg.isBlank()
                                        ) {
                                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                                            return@launch
                                        }
                                        val rutina = db.rutinaDao().buscarPorId(rutinaId)
                                        rutina?.let {
                                            db.rutinaDao().actualizar(
                                                it.copy(
                                                    ejercicio = ejercicio.trim(),
                                                    grupoMuscular = grupoMuscular,
                                                    series = series.toIntOrNull() ?: it.series,
                                                    repeticiones = repeticiones.toIntOrNull() ?: it.repeticiones,
                                                    pesoKg = pesoKg.toDoubleOrNull() ?: it.pesoKg,
                                                    fecha = fecha
                                                )
                                            )
                                            Toast.makeText(context, "¡Rutina actualizada!", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Guardar Cambios", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
