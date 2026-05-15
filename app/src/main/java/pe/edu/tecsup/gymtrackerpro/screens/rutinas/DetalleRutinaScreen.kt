package pe.edu.tecsup.gymtrackerpro.screens.rutinas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase

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

    val grupos = listOf("Pecho", "Espalda", "Hombros", "Bíceps", "Tríceps", "Piernas", "Abdomen", "Glúteos")

    // Cargar datos de la rutina
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
    }

    // AlertDialog eliminar
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar rutina") },
            text = { Text("¿Estás seguro de eliminar '$ejercicio'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val rutina = db.rutinaDao().buscarPorId(rutinaId)
                            rutina?.let { db.rutinaDao().eliminar(it) }
                            mostrarDialogoEliminar = false
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar rutina #$rutinaId", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogoEliminar = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Banner informativo
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFE3F2FD)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Modificando registro existente",
                        color = Color(0xFF1565C0),
                        fontSize = 13.sp
                    )
                }
            }

            // Ejercicio
            Text("Ejercicio", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = ejercicio,
                onValueChange = { ejercicio = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Grupo muscular
            Text("Grupo muscular", fontWeight = FontWeight.Medium)
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido }
            ) {
                OutlinedTextField(
                    value = grupoMuscular,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
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

            // Series y Repeticiones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Series", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = series,
                        onValueChange = { series = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Repeticiones", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = repeticiones,
                        onValueChange = { repeticiones = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Peso
            Text("Peso (kg)", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = pesoKg,
                onValueChange = { pesoKg = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Fecha
            Text("Fecha", fontWeight = FontWeight.Medium)

            OutlinedTextField(
                value = fecha,
                onValueChange = {
                    fecha = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón actualizar
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
                        }
                        Toast.makeText(context, "Rutina actualizada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text("Actualizar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}