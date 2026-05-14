package pe.edu.tecsup.gymtrackerpro.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Rutina

@Dao
interface RutinaDao {

    @Insert
    suspend fun insert(rutina: Rutina)

    @Update
    suspend fun actualizar(rutina: Rutina)

    @Delete
    suspend fun eliminar(rutina: Rutina)

    @Query("SELECT * FROM Rutina WHERE usuario_id = :usuarioId ORDER BY fecha DESC")
    fun listarRutinasPorUsuario(usuarioId: Int): Flow<List<Rutina>>

    @Query("SELECT * FROM Rutina WHERE id = :rutinaId")
    suspend fun buscarPorId(rutinaId: Int): Rutina?

    @Query("SELECT COUNT(*) FROM Rutina WHERE usuario_id = :usuarioId")
    suspend fun contarRutinasPorUsuario(usuarioId: Int): Int

    @Query("SELECT SUM(peso_kg * series * repeticiones) FROM Rutina WHERE usuario_id = :usuarioId")
    suspend fun volumenTotal(usuarioId: Int): Double?
}
