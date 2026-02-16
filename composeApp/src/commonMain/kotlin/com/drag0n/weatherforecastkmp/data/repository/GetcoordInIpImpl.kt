package com.drag0n.weatherforecastkmp.data.repository

import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.model.IpApiResponse
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


class GetcoordInIpImpl(val ktor: HttpClient) : LocationRepository {
    override suspend fun getCurrentLocation(): Coord? {
        return try {
            val response: IpApiResponse = ktor.get("http://ip-api.com/json/").body()
            if (response.status == "success") {
                Coord(lat = response.lat.toString(), lon = response.lon.toString())

            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message.toString())
            null
        }
    }
}