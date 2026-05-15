package pe.edu.tecsup.gymtrackerpro.screens.menu

import androidx.compose.animation.AnimatedVisibility
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
import pe.edu.tecsup.gymtrackerpro.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipalScreen(navController: NavController, usuarioId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var totalRutinas by remember { mutableStateOf(0) }

    LaunchedEffect(usuarioId) {
        val usuario = db.usuarioDao().buscarPorId(usuarioId)
        nombreCompleto = usuario?.nombreCompleto ?: "Atleta"
        email = usuario?.email ?: ""
        db.rutinaDao().listarRutinasPorUsuario(usuarioId).collect {
            totalRutinas = it.size
        }
    }

    val iniciales = nombreCompleto.split(" ").filter { it.isNotBlank() }.take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(White.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = iniciales, color = White, style = MaterialTheme.typography.headlineLarge)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = nombreCompleto, style = MaterialTheme.typography.titleLarge, color = White)
                        Text(text = email, style = MaterialTheme.typography.bodyMedium, color = White.copy(alpha = 0.8f))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                DrawerItem(Icons.Default.Dashboard, "Dashboard", true) { scope.launch { drawerState.close() } }
                DrawerItem(Icons.Default.AddCircle, "Nueva Rutina", false) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.agregarRutina(usuarioId))
                }
                DrawerItem(Icons.Default.History, "Mis Rutinas", false) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.listaRutinas(usuarioId))
                }
                DrawerItem(Icons.Default.AccountCircle, "Perfil", false) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.perfilUsuario(usuarioId))
                }
                
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                DrawerItem(Icons.AutoMirrored.Filled.Logout, "Cerrar Sesión", false) {
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.LOGIN) { inclusive = true } }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("GYMTRACKER PRO", style = MaterialTheme.typography.headlineLarge.copy(letterSpacing = 2.sp)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.NotificationsNone, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column {
                        Text(text = "Bienvenido de nuevo,", style = MaterialTheme.typography.bodyLarge, color = MediumGrey)
                        Text(text = nombreCompleto, style = MaterialTheme.typography.displaySmall, color = PremiumBlack)
                    }
                }

                item {
                    SummaryCard(totalRutinas)
                }

                item {
                    Text(text = "Acciones Rápidas", style = MaterialTheme.typography.headlineLarge)
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DashboardActionCard(
                            title = "Añadir Rutina",
                            icon = Icons.Default.Add,
                            color = PrimaryBlue,
                            modifier = Modifier.weight(1f)
                        ) { navController.navigate(Routes.agregarRutina(usuarioId)) }
                        Spacer(modifier = Modifier.width(16.dp))
                        DashboardActionCard(
                            title = "Ver Historial",
                            icon = Icons.Default.History,
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        ) { navController.navigate(Routes.listaRutinas(usuarioId)) }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DashboardActionCard(
                            title = "Mi Perfil",
                            icon = Icons.Default.Person,
                            color = WarningOrange,
                            modifier = Modifier.weight(1f)
                        ) { navController.navigate(Routes.perfilUsuario(usuarioId)) }
                        Spacer(modifier = Modifier.width(16.dp))
                        DashboardActionCard(
                            title = "Configuración",
                            icon = Icons.Default.Settings,
                            color = MediumGrey,
                            modifier = Modifier.weight(1f)
                        ) { }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun SummaryCard(count: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Progreso Actual", color = White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
                Text(text = "$count Rutinas", color = White, style = MaterialTheme.typography.displayMedium)
                Text(text = "¡Sigue así, campeón!", color = White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun DashboardActionCard(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp), color = PremiumBlack)
        }
    }
}
