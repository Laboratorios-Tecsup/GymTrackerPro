package pe.edu.tecsup.gymtrackerpro.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.tecsup.gymtrackerpro.data.local.dao.RutinaDao
import pe.edu.tecsup.gymtrackerpro.data.local.dao.UsuarioDao
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Rutina
import pe.edu.tecsup.gymtrackerpro.data.local.entity.Usuario

@Database(
    entities = [
        Usuario::class,
        Rutina::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun rutinaDao(): RutinaDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymtracker_db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}