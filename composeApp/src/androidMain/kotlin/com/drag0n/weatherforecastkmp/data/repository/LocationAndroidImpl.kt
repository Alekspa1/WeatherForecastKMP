package com.drag0n.weatherforecastkmp.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.huawei.hms.api.HuaweiApiAvailability
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class LocationAndroidImpl(private val context: Context, private val locationInIp: LocationRepository) : LocationRepository, PermissionRepository {

    private val gmsClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val hmsClient by lazy {
        com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(
            context
        )
    }

    override fun isPermissionGranted(permission : String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun isGpsEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override suspend fun getCurrentLocation(): Coord? {


        val coords = when {
            isGoogleAvailable() -> getLocationGoogle()
            isHuaweiAvailable() -> getHmsLocation()
            else -> {
                locationInIp.getCurrentLocation()

            }
        }
        return coords
    }



    private fun isGoogleAvailable() = GoogleApiAvailability.getInstance()

        .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS


    private fun isHuaweiAvailable() = HuaweiApiAvailability.getInstance()

        .isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS

    private suspend fun getLocationGoogle(): Coord? = suspendCancellableCoroutine { cont ->
        val ct = CancellationTokenSource()
        // 1. Проверка разрешений
        val hasFine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            cont.resume(null) // Или cont.resumeWithException(SecurityException("No permissions"))
            return@suspendCancellableCoroutine
        }

        // 2. Запрос локации
        gmsClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnSuccessListener { loc ->
                // Важно: проверяем, активна ли еще корутина
                if (cont.isActive) {
                    cont.resume(loc?.let { Coord(it.latitude.toString(), it.longitude.toString()) })
                }
            }
            .addOnFailureListener {
                if (cont.isActive) cont.resume(null)
            }

        cont.invokeOnCancellation {
            ct.cancel()
        }
    }



    private suspend fun getLastLocationHuawei(): Coord? = suspendCancellableCoroutine { cont ->
        hmsClient.lastLocation
            .addOnSuccessListener { loc ->
                if (cont.isActive) {
                    cont.resume(loc?.let { Coord(it.latitude.toString(), it.longitude.toString()) })
                }
            }
            .addOnFailureListener {
                if (cont.isActive) cont.resume(null)
            }
    }

    private suspend fun getLocationHuawei(): Coord? {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) return null

        return suspendCancellableCoroutine { cont ->

            val locationRequest = com.huawei.hms.location.LocationRequest.create()
                .setPriority(com.huawei.hms.location.LocationRequest.PRIORITY_HIGH_ACCURACY) // Для точности, как в GMS примере
                .setNumUpdates(1)

            val callback = object : com.huawei.hms.location.LocationCallback() {
                override fun onLocationResult(result: com.huawei.hms.location.LocationResult?) {
                    val loc = result?.lastLocation
                    if (cont.isActive) {
                        cont.resume(loc?.let {
                            Coord(
                                it.latitude.toString(),
                                it.longitude.toString()
                            )
                        })
                    }
                    // Важно: HMS автоматически не всегда удаляет callback при setNumUpdates(1)
                    // в некоторых версиях SDK, лучше подстраховаться
                    hmsClient.removeLocationUpdates(this)
                }
            }

            hmsClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
                .addOnFailureListener {
                    if (cont.isActive) cont.resume(null)
                }

            // Обязательно: отменяем запрос, если корутина отменена
            cont.invokeOnCancellation {
                hmsClient.removeLocationUpdates(callback)
            }
        }
    }

    private suspend fun getHmsLocation(): Coord? {
        val last = getLastLocationHuawei()
        return last ?: getLocationHuawei()
    }






}