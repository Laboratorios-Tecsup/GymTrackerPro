package pe.edu.tecsup.gymtrackerpro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.edu.tecsup.gymtrackerpro.screens.login.LoginScreen
import pe.edu.tecsup.gymtrackerpro.screens.registro.RegistroScreen
import pe.edu.tecsup.gymtrackerpro.screens.menu.MenuPrincipalScreen
import pe.edu.tecsup.gymtrackerpro.screens.rutinas.AgregarRutinaScreen
import pe.edu.tecsup.gymtrackerpro.screens.rutinas.ListaRutinasScreen
import pe.edu.tecsup.gymtrackerpro.screens.rutinas.DetalleRutinaScreen
import pe.edu.tecsup.gymtrackerpro.screens.perfil.PerfilUsuarioScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        // Pantalla de Registro
        composable(Routes.REGISTRO) {
            RegistroScreen(navController = navController)
        }

        // Pantalla de Menu Principal
        composable(
            route = Routes.MENU_PRINCIPAL,
            arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
            MenuPrincipalScreen(navController = navController, usuarioId = usuarioId)
        }

        // Pantalla de Agregar Rutina
        composable(
            route = Routes.AGREGAR_RUTINA,
            arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
            AgregarRutinaScreen(navController = navController, usuarioId = usuarioId)
        }

        // Pantalla de Lista de Rutinas
        composable(
            route = Routes.LISTA_RUTINAS,
            arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
            ListaRutinasScreen(navController = navController, usuarioId = usuarioId)
        }

        // Pantalla de Detalle de Rutina
        composable(
            route = Routes.DETALLE_RUTINA,
            arguments = listOf(navArgument("rutinaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getInt("rutinaId") ?: 0
            DetalleRutinaScreen(navController = navController, rutinaId = rutinaId)
        }

        // Pantalla de Perfil
        composable(
            route = Routes.PERFIL_USUARIO,
            arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
            PerfilUsuarioScreen(navController = navController, usuarioId = usuarioId)
        }

        //
    }
}