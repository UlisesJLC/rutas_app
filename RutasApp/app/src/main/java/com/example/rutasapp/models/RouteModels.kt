package com.example.rutasapp.models

data class RouteRequestBody(val coordinates: List<List<Double>>)
data class RouteResponse(
    val features: List<Feature> = emptyList()
)

data class Feature(
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<List<Double>>
)
