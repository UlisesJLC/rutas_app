package com.example.rutasapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rutasapp.ui.theme.RutasAppTheme
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : ComponentActivity() {

    private val university = GeoPoint(20.139579519656575, -101.15073143314598)
    private val home = GeoPoint(20.125787484312617, -101.19862697792448)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = "5b3ce3597851110001cf6248601754f0c2034e81b4e6ab7f467d9c90"
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                println("Permission: ${it.key} = ${it.value}")
            }
        }

// Solicitar permisos de ubicación en tiempo de ejecución
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            RutasAppTheme {
                MapView(
                    university = university,
                    home = home,
                    apiKey = apiKey,
                    getCurrentLocation = { callback ->
                        LocationHelper.getCurrentLocation(this) { location ->
                            callback(location)
                        }
                    },
                    showMessage = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
