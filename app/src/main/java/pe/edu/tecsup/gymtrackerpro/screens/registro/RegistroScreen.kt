package pe.edu.tecsup.gymtrackerpro.screens.registro

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.tecsup.gymtrackerpro.data.local.database.AppDatabase
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Usuario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

    var nombreCompleto by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }

    // Estados para errores
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var errorGenero by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Crear cuenta")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Registro Usuario",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre Completo
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = {
                    nombreCompleto = it
                    errorNombre = if (it.isBlank()) "El nombre es obligatorio" else null
                },
                label = { Text("Nombre Completo") },
                isError = errorNombre != null,
                supportingText = { errorNombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                    errorUsuario = when {
                        it.isBlank() -> "El usuario es obligatorio"
                        it.length < 3 -> "Mínimo 3 caracteres"
                        else -> null
                    }
                },
                label = { Text("Usuario") },
                isError = errorUsuario != null,
                supportingText = { errorUsuario?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorEmail = when {
                        it.isBlank() -> "El email es obligatorio"
                        !Patterns.EMAIL_ADDRESS.matcher(it)
                            .matches() -> "Formato de email inválido (falta @ o dominio)"

                        else -> null
                    }
                },
                label = { Text("Email") },
                isError = errorEmail != null,
                supportingText = { errorEmail?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Edad
            OutlinedTextField(
                value = edad,
                onValueChange = {
                    edad = it
                    val edadInt = it.toIntOrNull()
                    errorEdad = when {
                        it.isBlank() -> "La edad es obligatoria"
                        edadInt == null -> "Debe ser un número"
                        edadInt <= 0 || edadInt > 120 -> "Edad no válida"
                        else -> null
                    }
                },
                label = { Text("Edad") },
                isError = errorEdad != null,
                supportingText = { errorEdad?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gnero
            OutlinedTextField(
                value = genero,
                onValueChange = {
                    genero = it
                    errorGenero = if (it.isBlank()) "El genero es obligatorio" else null
                },
                label = { Text("Escribe tu genero") },
                isError = errorGenero != null,
                supportingText = { errorGenero?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorPassword = when {
                        it.isBlank() -> "La contraseña es obligatoria"
                        it.length < 6 -> "Mínimo 6 caracteres"
                        else -> null
                    }
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = errorPassword != null,
                supportingText = { errorPassword?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Validación final antes de enviar
                    val isNombreValid = nombreCompleto.isNotBlank()
                    val isUsuarioValid = usuario.isNotBlank() && usuario.length >= 3
                    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val isEdadValid = edad.toIntOrNull() != null && (edad.toIntOrNull() ?: 0) > 0
                    val isPasswordValid = password.length >= 6

                    if (!isNombreValid) errorNombre = "El nombre es obligatorio"
                    if (!isUsuarioValid) errorUsuario = "Usuario inválido"
                    if (!isEmailValid) errorEmail = "Email inválido"
                    if (!isEdadValid) errorEdad = "Edad inválida"
                    if (!isPasswordValid) errorPassword = "Contraseña muy corta"

                    if (isNombreValid && isUsuarioValid && isEmailValid && isEdadValid && isPasswordValid) {
                        scope.launch {
                            val usuarioExistente = db.usuarioDao().buscarPorUsuario(usuario.trim())
                            if (usuarioExistente != null) {
                                errorUsuario = "Este usuario ya está registrado"
                            } else {
                                val nuevoUsuario = Usuario(
                                    nombreUsuario = usuario.trim(),
                                    password = password.trim(),
                                    nombreCompleto = nombreCompleto.trim(),
                                    email = email.trim(),
                                    edad = edad.toInt(),
                                    genero = genero.trim(),
                                    fechaRegistro = SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(Date())
                                )
                                db.usuarioDao().insertar(nuevoUsuario)
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT)
                                    .show()
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }
        }
    }
}
