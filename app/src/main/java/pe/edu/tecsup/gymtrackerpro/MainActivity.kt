package pe.edu.tecsup.gymtrackerpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pe.edu.tecsup.gymtrackerpro.navigation.AppNavigation
import pe.edu.tecsup.gymtrackerpro.ui.theme.GymTrackerProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymTrackerProTheme {
                AppNavigation()
            }
        }
    }
}