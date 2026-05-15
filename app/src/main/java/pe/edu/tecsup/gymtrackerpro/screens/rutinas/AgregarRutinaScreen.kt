package pe.edu.tecsup.gymtrackerpro.screens.rutinas

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Rutina
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarRutinaScreen(navController: NavController, usuarioId: Int) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    var ejercicio by remember { mutableStateOf("") }
    var grupoMuscular by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var repeticiones by remember { mutableStateOf("") }
    var pesoKg by remember { mutableStateOf("") }
    
    // Corregido: Ahora la fecha es un estado para poder cambiarla
    var fecha by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }

    // Configuración del Selector de Fecha
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

    // Dropdown grupo muscular
    val grupos = listOf("Pecho", "Espalda", "Hombros", "Bíceps", "Tríceps", "Piernas", "Abdomen", "Glúteos")
    var expandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva rutina", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            if (ejercicio.isBlank() || grupoMuscular.isBlank() ||
                                series.isBlank() || repeticiones.isBlank() || pesoKg.isBlank()
                            ) {
                                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                                return@launch
                            }
                            val rutina = Rutina(
                                usuarioId = usuarioId,
                                ejercicio = ejercicio.trim(),
                                grupoMuscular = grupoMuscular,
                                series = series.toIntOrNull() ?: 0,
                                repeticiones = repeticiones.toIntOrNull() ?: 0,
                                pesoKg = pesoKg.toDoubleOrNull() ?: 0.0,
                                fecha = fecha
                            )
                            db.rutinaDao().insert(rutina)
                            Toast.makeText(context, "Rutina guardada", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
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
            // Ejercicio
            Text("Ejercicio", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = ejercicio,
                onValueChange = { ejercicio = it },
                placeholder = { Text("Press banca") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Grupo muscular - Dropdown
            Text("Grupo muscular", fontWeight = FontWeight.Medium)
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido }
            ) {
                OutlinedTextField(
                    value = grupoMuscular,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona") },
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

            // Series y Repeticiones en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Series", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = series,
                        onValueChange = { series = it },
                        placeholder = { Text("4") },
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
                        placeholder = { Text("12") },
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
                placeholder = { Text("60.5") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Fecha (Corregido: Ahora se puede cambiar al hacer clic)
            Text("Fecha", fontWeight = FontWeight.Medium)
            Box(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false, // Deshabilitamos la escritura directa pero el Box captura el clic
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón guardar
            Button(
                onClick = {
                    scope.launch {
                        if (ejercicio.isBlank() || grupoMuscular.isBlank() ||
                            series.isBlank() || repeticiones.isBlank() || pesoKg.isBlank()
                        ) {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        val rutina = Rutina(
                            usuarioId = usuarioId,
                            ejercicio = ejercicio.trim(),
                            grupoMuscular = grupoMuscular,
                            series = series.toIntOrNull() ?: 0,
                            repeticiones = repeticiones.toIntOrNull() ?: 0,
                            pesoKg = pesoKg.toDoubleOrNull() ?: 0.0,
                            fecha = fecha
                        )
                        db.rutinaDao().insert(rutina)
                        Toast.makeText(context, "Rutina guardada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar rutina", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
