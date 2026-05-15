package pe.edu.tecsup.gymtrackerpro.screens.registro

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Usuario
import pe.edu.tecsup.gymtrackerpro.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

    var nombreCompleto by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Únete a la Élite", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = White)
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
                        Text(
                            text = "Crea tu perfil fitness",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        CustomTextField(
                            value = nombreCompleto,
                            onValueChange = { 
                                nombreCompleto = it
                                errorNombre = if (it.isBlank()) "Campo requerido" else null
                            },
                            label = "Nombre Completo",
                            icon = Icons.Default.Badge,
                            error = errorNombre
                        )

                        CustomTextField(
                            value = usuario,
                            onValueChange = { 
                                usuario = it
                                errorUsuario = if (it.length < 3) "Mínimo 3 caracteres" else null
                            },
                            label = "Nombre de Usuario",
                            icon = Icons.Default.Person,
                            error = errorUsuario
                        )

                        CustomTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                errorEmail = if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) "Email inválido" else null
                            },
                            label = "Correo Electrónico",
                            icon = Icons.Default.Email,
                            error = errorEmail,
                            keyboardType = KeyboardType.Email
                        )

                        CustomTextField(
                            value = edad,
                            onValueChange = { 
                                edad = it
                                val e = it.toIntOrNull()
                                errorEdad = if (e == null || e <= 0 || e > 110) "Edad no válida" else null
                            },
                            label = "Edad",
                            icon = Icons.Default.CalendarMonth,
                            error = errorEdad,
                            keyboardType = KeyboardType.Number
                        )

                        CustomTextField(
                            value = password,
                            onValueChange = { 
                                password = it
                                errorPassword = if (it.length < 6) "Mínimo 6 caracteres" else null
                            },
                            label = "Contraseña",
                            icon = Icons.Default.Lock,
                            error = errorPassword,
                            isPassword = true,
                            passwordVisible = passwordVisible,
                            onTogglePassword = { passwordVisible = !passwordVisible }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (validarCampos(nombreCompleto, usuario, email, edad, password)) {
                                    scope.launch {
                                        val existente = db.usuarioDao().buscarPorUsuario(usuario.trim())
                                        if (existente != null) {
                                            errorUsuario = "Usuario ya existe"
                                        } else {
                                            val nuevo = Usuario(
                                                nombreUsuario = usuario.trim(),
                                                password = password.trim(),
                                                nombreCompleto = nombreCompleto.trim(),
                                                email = email.trim(),
                                                edad = edad.toInt(),
                                                fechaRegistro = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                            )
                                            db.usuarioDao().insertar(nuevo)
                                            Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Comenzar Ahora", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onTogglePassword) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error != null,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MediumGrey.copy(alpha = 0.5f)
            )
        )
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

private fun validarCampos(n: String, u: String, e: String, ed: String, p: String): Boolean {
    return n.isNotBlank() && u.length >= 3 && Patterns.EMAIL_ADDRESS.matcher(e).matches() && ed.toIntOrNull() != null && p.length >= 6
}
