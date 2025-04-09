package com.example.rutasapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

@Composable
fun MapView(
    university: GeoPoint,
    home: GeoPoint,
    apiKey: String,
    getCurrentLocation: ((GeoPoint?) -> Unit) -> Unit,
    showMessage: (String) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var addressInput by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = addressInput,
                    onValueChange = { addressInput = it },
                    label = { Text("Dirección de tu casa") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            loading = true
                            scope.launch {
                                routePoints = NetworkHelper.getRoute(apiKey, home, university)
                                loading = false
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ruta Casa - Uni")
                    }

                    Button(
                        onClick = {
                            loading = true
                            getCurrentLocation { location ->
                                if (location != null) {
                                    scope.launch {
                                        routePoints = NetworkHelper.getRoute(apiKey, location, university)
                                        loading = false
                                    }
                                } else {
                                    loading = false
                                    showMessage("No se pudo obtener la ubicación")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ubicación Actual")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (addressInput.isNotBlank()) {
                            loading = true
                            scope.launch {
                                val geocodedLocation = NetworkHelper.geocode(apiKey, addressInput)
                                if (geocodedLocation != null) {
                                    routePoints = NetworkHelper.getRoute(apiKey, geocodedLocation, university)
                                } else {
                                    showMessage("No se pudo encontrar la dirección")
                                }
                                loading = false
                            }
                        } else {
                            showMessage("Por favor ingresa una dirección")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Ruta por dirección")
                }

                if (loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }

        // Espacio para el mapa
        OsmMap(routePoints)
    }


}
