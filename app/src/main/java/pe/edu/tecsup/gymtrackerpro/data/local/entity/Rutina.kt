package pe.edu.tecsup.gymtrackerpro.data.local.entity

import androidx.room.*

@Entity(
    tableName = "Rutina",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["usuario_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Rutina(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "usuario_id") val usuarioId: Int = 0,
    @ColumnInfo(name = "ejercicio") val ejercicio: String = "",
    @ColumnInfo(name = "grupo_muscular") val grupoMuscular: String = "",
    @ColumnInfo(name = "series") val series: Int = 0,
    @ColumnInfo(name = "repeticiones") val repeticiones: Int = 0,
    @ColumnInfo(name = "peso_kg") val pesoKg: Double = 0.0,
    @ColumnInfo(name = "fecha") val fecha: String = ""
)