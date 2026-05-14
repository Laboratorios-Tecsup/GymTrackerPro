package pe.edu.tecsup.gymtrackerpro.navigation


object Routes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MENU_PRINCIPAL = "menu_principal/{usuarioId}"
    const val AGREGAR_RUTINA = "agregar_rutina/{usuarioId}"
    const val LISTA_RUTINAS = "lista_rutinas/{usuarioId}"
    const val DETALLE_RUTINA = "detalle_rutina/{rutinaId}"
    const val PERFIL_USUARIO = "perfil_usuario/{usuarioId}"

    // Funciones para construir las rutas con los argumentos
    fun menuPrincipal(usuarioId: Int) = "menu_principal/$usuarioId"
    fun agregarRutina(usuarioId: Int) = "agregar_rutina/$usuarioId"
    fun listaRutinas(usuarioId: Int) = "lista_rutinas/$usuarioId"
    fun detalleRutina(rutinaId: Int) = "detalle_rutina/$rutinaId"
    fun perfilUsuario(usuarioId: Int) = "perfil_usuario/$usuarioId"
}