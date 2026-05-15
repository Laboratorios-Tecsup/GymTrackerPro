package pe.edu.tecsup.gymtrackerpro.screens.rutinas

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Rutina
import pe.edu.tecsup.gymtrackerpro.navigation.Routes
import pe.edu.tecsup.gymtrackerpro.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutinasScreen(navController: NavController, usuarioId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    val rutinas by db.rutinaDao().listarRutinasPorUsuario(usuarioId).collectAsState(initial = emptyList())
    var rutinaAEliminar by remember { mutableStateOf<Rutina?>(null) }

    if (rutinaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { rutinaAEliminar = null },
            title = { Text("¿Eliminar Rutina?", style = MaterialTheme.typography.headlineLarge) },
            text = { Text("Esta acción no se puede deshacer.", style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            rutinaAEliminar?.let { db.rutinaDao().eliminar(it) }
                            rutinaAEliminar = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { rutinaAEliminar = null }) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Rutinas", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.agregarRutina(usuarioId)) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = White, modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            if (rutinas.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(rutinas) { rutina ->
                        PremiumRutinaCard(
                            rutina = rutina,
                            onEdit = { navController.navigate(Routes.detalleRutina(rutina.id)) },
                            onDelete = { rutinaAEliminar = rutina }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MediumGrey.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(64.dp), tint = MediumGrey)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No hay registros aún", style = MaterialTheme.typography.headlineLarge, color = PremiumBlack)
        Text("Tu viaje fitness comienza hoy", style = MaterialTheme.typography.bodyMedium, color = MediumGrey)
    }
}

@Composable
fun PremiumRutinaCard(rutina: Rutina, onEdit: () -> Unit, onDelete: () -> Unit) {
    val accentColor = when (rutina.grupoMuscular.lowercase()) {
        "pecho" -> Color(0xFF3B82F6)
        "espalda" -> Color(0xFF10B981)
        "piernas" -> Color(0xFFF59E0B)
        "hombros" -> Color(0xFF8B5CF6)
        else -> MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = accentColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = rutina.ejercicio, style = MaterialTheme.typography.titleLarge, color = PremiumBlack)
                    Text(text = rutina.grupoMuscular, style = MaterialTheme.typography.labelLarge, color = accentColor)
                }
                IconButton(onClick = onEdit) { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MediumGrey) }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MediumGrey.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem(Icons.Default.Refresh, "${rutina.series} series")
                StatItem(Icons.Default.Repeat, "${rutina.repeticiones} reps")
                StatItem(Icons.Default.Scale, "${rutina.pesoKg} kg")
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp), tint = MediumGrey)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = rutina.fecha, style = MaterialTheme.typography.bodySmall, color = MediumGrey)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = ErrorRed, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = PremiumBlack)
    }
}
