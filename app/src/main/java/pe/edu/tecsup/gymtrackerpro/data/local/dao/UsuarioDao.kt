package pe.edu.tecsup.gymtrackerpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    // INSERTAR USUARIO
    @Insert
    suspend fun insertar(usuario: Usuario)

    // LOGIN
    @Query("""
        SELECT * FROM Usuario
        WHERE nombre_usuario = :usuario
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(
        usuario: String,
        password: String
    ): Usuario?

    // VALIDAR SI USUARIO YA EXISTE
    @Query("""
        SELECT * FROM Usuario
        WHERE nombre_usuario = :usuario
        LIMIT 1
    """)
    suspend fun buscarPorUsuario(
        usuario: String
    ): Usuario?

    // OBTENER USUARIO POR ID
    @Query("""
        SELECT * FROM Usuario
        WHERE id = :id
    """)
    suspend fun buscarPorId(
        id: Int
    ): Usuario?
}