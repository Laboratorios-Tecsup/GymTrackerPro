package pe.edu.tecsup.gymtrackerpro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rutina(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "usuario_id") val usuarioId: Int,
    @ColumnInfo(name = "ejercicio") val ejercicio: String,
    @ColumnInfo(name = "grupo_muscular") val grupoMuscular: String,
    @ColumnInfo(name = "series") val series: Int,
    @ColumnInfo(name = "repeticiones") val repeticiones: Int,
    @ColumnInfo(name = "peso_kg") val pesoKg: Double,
    @ColumnInfo(name = "fecha") val fecha: String
)