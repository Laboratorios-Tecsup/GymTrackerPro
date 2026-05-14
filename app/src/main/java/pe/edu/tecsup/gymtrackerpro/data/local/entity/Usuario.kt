package pe.edu.tecsup.gymtrackerpro.data.local.entity

import androidx.room.*

@Entity(tableName = "Usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre_usuario") val nombreUsuario: String = "",
    @ColumnInfo(name = "password") val password: String = "",
    @ColumnInfo(name = "nombre_completo") val nombreCompleto: String = "",
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "edad") val edad: Int = 0,
    @ColumnInfo(name = "fecha_registro") val fechaRegistro: String = ""
)