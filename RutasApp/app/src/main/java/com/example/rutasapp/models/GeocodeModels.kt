package com.example.rutasapp.models

data class GeocodeResponse(
    val features: List<GeocodeFeature>
)

data class GeocodeFeature(
    val geometry: GeocodeGeometry
)

data class GeocodeGeometry(
    val coordinates: List<Double>
)
