package com.example.rutasapp

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun OsmMap(routePoints: List<GeoPoint>) {
    AndroidView(
        factory = { context: Context ->
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            MapView(context).apply {
                controller.setZoom(13.0)
                controller.setCenter(routePoints.firstOrNull() ?: GeoPoint(20.139579519656575, -101.15073143314598))
            }
        },
        update = { mapView ->
            mapView.overlays.clear()

            if (routePoints.isNotEmpty()) {
                val polyline = Polyline().apply {
                    setPoints(routePoints)
                }
                mapView.overlays.add(polyline)
                mapView.invalidate()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
