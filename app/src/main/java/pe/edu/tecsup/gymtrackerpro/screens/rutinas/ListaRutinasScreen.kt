package pe.edu.tecsup.gymtrackerpro.screens.rutinas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutinasScreen(navController: NavController, usuarioId: Int) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    // Corregido: Nombre de función coincide con RutinaDao
    val rutinas by db.rutinaDao().listarRutinasPorUsuario(usuarioId).collectAsState(initial = emptyList())

    // Rutina seleccionada para eliminar
    var rutinaAEliminar by remember { mutableStateOf<Rutina?>(null) }

    // AlertDialog de confirmación de eliminación
    rutinaAEliminar?.let { rutina ->
        AlertDialog(
            onDismissRequest = { rutinaAEliminar = null },
            title = { Text("Eliminar rutina") },
            text = { Text("¿Estás seguro de eliminar '${rutina.ejercicio}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            db.rutinaDao().eliminar(rutina)
                            rutinaAEliminar = null
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { rutinaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis rutinas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.agregarRutina(usuarioId)) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar rutina", tint = Color.White)
            }
        }
    ) { paddingValues ->
        if (rutinas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes rutinas aún",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Toca + para agregar una",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(rutinas) { rutina ->
                    RutinaCard(
                        rutina = rutina, // Corregido: se pasa el item individual
                        onEditar = {
                            navController.navigate(Routes.detalleRutina(rutina.id))
                        },
                        onEliminar = {
                            rutinaAEliminar = rutina // Corregido: se asigna el item individual
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RutinaCard(
    rutina: Rutina,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    // Color por grupo muscular
    val colorGrupo = when (rutina.grupoMuscular.lowercase()) {
        "pecho" -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        "espalda" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "hombros" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        "bíceps", "biceps" -> Color(0xFFF3E5F5) to Color(0xFF6A1B9A)
        "tríceps", "triceps" -> Color(0xFFE0F7FA) to Color(0xFF00695C)
        "piernas", "pierna" -> Color(0xFFE8F5E9) to Color(0xFF1B5E20)
        "abdomen" -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
        "glúteos", "gluteos" -> Color(0xFFFCE4EC) to Color(0xFF880E4F)
        else -> Color(0xFFF5F5F5) to Color(0xFF424242)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rutina.ejercicio,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Row {
                    IconButton(
                        onClick = onEditar,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onEliminar,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Badge grupo muscular
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colorGrupo.first
            ) {
                Text(
                    text = rutina.grupoMuscular,
                    color = colorGrupo.second,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${rutina.series} series × ${rutina.repeticiones} reps · ${rutina.pesoKg} kg · ${rutina.fecha}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}
