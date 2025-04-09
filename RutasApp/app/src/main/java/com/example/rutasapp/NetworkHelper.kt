package com.example.rutasapp

import com.example.rutasapp.models.GeocodeResponse
import com.example.rutasapp.models.RouteRequestBody
import com.example.rutasapp.models.RouteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

object NetworkHelper {
    suspend fun getRoute(apiKey: String, origin: GeoPoint, destination: GeoPoint): List<GeoPoint> {
        return withContext(Dispatchers.IO) {
            try {
                val api = Retrofit.Builder()
                    .baseUrl("https://api.openrouteservice.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpenRouteServiceApi::class.java)

                val start = "${origin.longitude},${origin.latitude}"
                val end = "${destination.longitude},${destination.latitude}"

                val response = api.getRoute(apiKey, start, end)

                println("🚀 Respuesta de la API: $response")

                val coordinates = response.features.firstOrNull()?.geometry?.coordinates

                if (coordinates.isNullOrEmpty()) {
                    println("⚠️ No se encontraron coordenadas para la ruta.")
                    emptyList()
                } else {
                    coordinates.map {
                        GeoPoint(it[1], it[0])
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    suspend fun geocode(apiKey: String, address: String): GeoPoint? {
        return withContext(Dispatchers.IO) {
            try {
                val api = Retrofit.Builder()
                    .baseUrl("https://api.openrouteservice.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpenRouteServiceApi::class.java)

                val response = api.geocode(apiKey, address)

                val coordinates = response.features.firstOrNull()?.geometry?.coordinates
                if (coordinates != null && coordinates.size >= 2) {
                    GeoPoint(coordinates[1], coordinates[0])
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}


interface OpenRouteServiceApi {
    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Header("Authorization") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): RouteResponse
    @GET("geocode/search")
    suspend fun geocode(
        @Header("Authorization") apiKey: String,
        @Query("text") address: String
    ): GeocodeResponse

}

