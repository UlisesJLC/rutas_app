package com.example.rutasapp

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import org.osmdroid.util.GeoPoint

object LocationHelper {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, callback: (GeoPoint?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(GeoPoint(location.latitude, location.longitude))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
