package pe.edu.tecsup.gymtrackerpro.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(navController: NavController, usuarioId: Int) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var nombreCompleto by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(0) }
    var fechaRegistro by remember { mutableStateOf("") }
    var totalRutinas by remember { mutableStateOf(0L) }
    var volumenTotal by remember { mutableStateOf(0.0) }
    var genero by remember { mutableStateOf("") }

    // Cargar datos del usuario y estadísticas
    LaunchedEffect(usuarioId) {
        val usuario = db.usuarioDao().buscarPorId(usuarioId)
        usuario?.let {
            nombreCompleto = it.nombreCompleto
            nombreUsuario = it.nombreUsuario
            email = it.email
            edad = it.edad
            fechaRegistro = it.fechaRegistro
            genero = it.genero
        }
        // Corregido: Nombres de funciones coinciden con RutinaDao
        totalRutinas = db.rutinaDao().contarRutinasPorUsuario(usuarioId).toLong()
        volumenTotal = db.rutinaDao().volumenTotal(usuarioId) ?: 0.0
    }

    // Iniciales para el avatar
    val iniciales = if (nombreCompleto.isNotBlank()) {
        nombreCompleto
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.toString() }
            .joinToString("")
            .uppercase()
    } else "?"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nombreCompleto,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@$nombreUsuario",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EstadisticaCard(
                    valor = totalRutinas.toString(),
                    etiqueta = "Rutinas",
                    modifier = Modifier.weight(1f)
                )
                EstadisticaCard(
                    valor = String.format("%.0f", volumenTotal),
                    etiqueta = "kg totales",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoItem(
                        icon = Icons.Default.Email,
                        etiqueta = "Email",
                        valor = email
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoItem(
                        icon = Icons.Default.Person,
                        etiqueta = "Edad",
                        valor = "$edad años"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoItem(
                        icon = Icons.Default.DateRange,
                        etiqueta = "Miembro desde",
                        valor = fechaRegistro
                    )
                    InfoItem(
                        icon = Icons.Default.DateRange,
                        etiqueta = "Genero",
                        valor = genero
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón cerrar sesión
            OutlinedButton(
                onClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color.Red)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EstadisticaCard(
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = etiqueta,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
