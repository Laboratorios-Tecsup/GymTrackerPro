package pe.edu.tecsup.gymtrackerpro.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipalScreen(navController: NavController, usuarioId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var nombreCompleto by remember { mutableStateOf("Usuario") }
    var email by remember { mutableStateOf("") }
    var totalRutinas by remember { mutableStateOf(0) }

    // Cargar datos de forma segura
    LaunchedEffect(usuarioId) {
        val usuario = db.usuarioDao().buscarPorId(usuarioId)
        if (usuario != null) {
            nombreCompleto = usuario.nombreCompleto
            email = usuario.email
        }
        
        // Observar cantidad de rutinas
        db.rutinaDao().listarRutinasPorUsuario(usuarioId).collect { lista ->
            totalRutinas = lista.size
        }
    }

    // Calcular iniciales de forma segura
    val iniciales = remember(nombreCompleto) {
        if (nombreCompleto.isNotBlank() && nombreCompleto != "Usuario") {
            nombreCompleto.split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first() }
                .joinToString("")
                .uppercase()
        } else "?"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF1565C0), Color(0xFF1E88E5))))
                        .padding(24.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = iniciales, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = nombreCompleto, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = email, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = true,
                    icon = { Icon(Icons.Default.Home, null) },
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Añadir Rutina") },
                    selected = false,
                    icon = { Icon(Icons.Default.AddCircle, null) },
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.agregarRutina(usuarioId)) 
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis Rutinas") },
                    selected = false,
                    icon = { Icon(Icons.Default.History, null) },
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.listaRutinas(usuarioId)) 
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, null) },
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.perfilUsuario(usuarioId)) 
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, null) },
                    onClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("GYMTRACKER PRO", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null)
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column {
                        Text(text = "Hola,", color = Color.Gray, fontSize = 16.sp)
                        Text(text = nombreCompleto, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Tu progreso", color = Color.White.copy(alpha = 0.7f))
                                Text("$totalRutinas Rutinas", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            }
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                    }
                }

                item { Text("Acciones Rápidas", fontWeight = FontWeight.Bold, fontSize = 18.sp) }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ActionCard("Añadir", Icons.Default.Add, Color(0xFF1565C0), Modifier.weight(1f)) {
                            navController.navigate(Routes.agregarRutina(usuarioId))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ActionCard("Historial", Icons.Default.History, Color(0xFF43A047), Modifier.weight(1f)) {
                            navController.navigate(Routes.listaRutinas(usuarioId))
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ActionCard("Perfil", Icons.Default.Person, Color(0xFFFB8C00), Modifier.weight(1f)) {
                            navController.navigate(Routes.perfilUsuario(usuarioId))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ActionCard("Salir", Icons.AutoMirrored.Filled.Logout, Color(0xFFE53935), Modifier.weight(1f)) {
                            navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun ActionCard(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
