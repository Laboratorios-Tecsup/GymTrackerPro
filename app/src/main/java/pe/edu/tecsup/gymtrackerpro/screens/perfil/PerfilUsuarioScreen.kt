package pe.edu.tecsup.gymtrackerpro.screens.perfil

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.navigation.Routes
import pe.edu.tecsup.gymtrackerpro.ui.theme.*
import java.util.Locale

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

    LaunchedEffect(usuarioId) {
        val usuario = db.usuarioDao().buscarPorId(usuarioId)
        usuario?.let {
            nombreCompleto = it.nombreCompleto
            nombreUsuario = it.nombreUsuario
            email = it.email
            edad = it.edad
            fechaRegistro = it.fechaRegistro
        }
        totalRutinas = db.rutinaDao().contarRutinasPorUsuario(usuarioId).toLong()
        volumenTotal = db.rutinaDao().volumenTotal(usuarioId) ?: 0.0
    }

    val iniciales = if (nombreCompleto.isNotBlank()) {
        nombreCompleto.split(" ").filter { it.isNotBlank() }.take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase()
    } else "?"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil Elite", style = MaterialTheme.typography.headlineLarge) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Header Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(PrimaryBlue, AccentBlue)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = iniciales,
                                style = MaterialTheme.typography.displayMedium,
                                color = White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = nombreCompleto,
                            style = MaterialTheme.typography.displaySmall,
                            color = PremiumBlack
                        )
                        Text(
                            text = "@$nombreUsuario",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MediumGrey
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatMiniCard("Rutinas", totalRutinas.toString(), Icons.Default.FitnessCenter)
                            StatMiniCard("Volumen", String.format(Locale.getDefault(), "%.0f kg", volumenTotal), Icons.Default.Scale)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Information Section
                Text(
                    text = "Información Personal",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileInfoItem(Icons.Default.Email, "Email", email)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = LightGrey)
                        ProfileInfoItem(Icons.Default.Cake, "Edad", "$edad años")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = LightGrey)
                        ProfileInfoItem(Icons.Default.CalendarToday, "Miembro desde", fechaRegistro)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed.copy(alpha = 0.1f), contentColor = ErrorRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Cerrar Sesión", style = MaterialTheme.typography.titleLarge)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatMiniCard(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        Text(text = value, style = MaterialTheme.typography.headlineLarge, color = PremiumBlack)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MediumGrey)
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = MediumGrey)
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = PremiumBlack)
        }
    }
}
