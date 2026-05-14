package pe.edu.tecsup.gymtrackerpro.screens.menu

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

    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Cargar datos del usuario
    LaunchedEffect(usuarioId) {
        val usuario = db.usuarioDao().buscarPorId(usuarioId)
        nombreCompleto = usuario?.nombreCompleto ?: ""
        email = usuario?.email ?: ""
    }

    // Iniciales del usuario para el avatar
    val iniciales = nombreCompleto
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")
        .uppercase()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))

                // Avatar con iniciales
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = nombreCompleto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Items del Drawer
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } }
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Agregar rutina") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.agregarRutina(usuarioId))
                    }
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Mis rutinas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.listaRutinas(usuarioId))
                    }
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Mi perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.perfilUsuario(usuarioId))
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider()

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("GymTracker Pro") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
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
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Hola,", fontSize = 16.sp)
                Text(
                    text = nombreCompleto,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Grid 2x2 de tarjetas
                Row(modifier = Modifier.fillMaxWidth()) {
                    MenuCard(
                        icon = Icons.Default.Add,
                        titulo = "Agregar rutina",
                        color = Color(0xFFE3F2FD),
                        iconColor = Color(0xFF1565C0),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.agregarRutina(usuarioId)) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    MenuCard(
                        icon = Icons.Default.List,
                        titulo = "Mis rutinas",
                        color = Color(0xFFE8F5E9),
                        iconColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.listaRutinas(usuarioId)) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MenuCard(
                        icon = Icons.Default.Person,
                        titulo = "Mi perfil",
                        color = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.perfilUsuario(usuarioId)) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    MenuCard(
                        icon = Icons.Default.ExitToApp,
                        titulo = "Cerrar sesión",
                        color = Color(0xFFFFEBEE),
                        iconColor = Color(0xFFC62828),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    icon: ImageVector,
    titulo: String,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = iconColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = iconColor
            )
        }
    }
}