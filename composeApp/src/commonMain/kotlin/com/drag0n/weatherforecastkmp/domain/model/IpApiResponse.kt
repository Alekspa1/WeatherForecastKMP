package com.drag0n.weatherforecastkmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class IpApiResponse(
    val lat: Double,
    val lon: Double,
    val status: String
)
