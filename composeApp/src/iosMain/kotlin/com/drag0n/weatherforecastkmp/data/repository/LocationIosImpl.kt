package com.drag0n.weatherforecastkmp.data.repository

import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject
import kotlin.coroutines.resume

class LocationIosImpl(
    private val locationInIp: LocationRepository
) : LocationRepository, PermissionRepository {

    private val locationManager = CLLocationManager()
    private var currentDelegate: CLLocationManagerDelegateProtocol? = null

    // 1. Метод специально для iOS (его нет в общем интерфейсе)
    fun requestIosPermission() {
        val status = locationManager.authorizationStatus

        when (status) {
            kCLAuthorizationStatusNotDetermined -> {
                // Первый запрос — показываем системное окно
                locationManager.requestWhenInUseAuthorization()
            }
            kCLAuthorizationStatusDenied, kCLAuthorizationStatusRestricted -> {
                // Если уже запретили — отправляем в настройки iOS
                val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
                if (settingsUrl != null) {
                    UIApplication.sharedApplication.openURL(settingsUrl)
                }
            }
            else -> {
                // Уже разрешено или системное ограничение
            }
        }
    }

    override fun isPermissionGranted(permission: String): Boolean {
        val status = locationManager.authorizationStatus
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    override fun isGpsEnabled(): Boolean {
        return CLLocationManager.locationServicesEnabled()
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getCurrentLocation(): Coord? {
        // Если разрешений нет — сразу используем поиск по IP
        if (!isPermissionGranted("")) {
            return locationInIp.getCurrentLocation()
        }

        return suspendCancellableCoroutine { cont ->
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                    val locations = didUpdateLocations as List<CLLocation>
                    val lastLocation = locations.lastOrNull()
                    manager.stopUpdatingLocation()
                    manager.delegate = null // Очищаем ссылку

                    if (cont.isActive) {
                        cont.resume(lastLocation?.let {
                            Coord(it.coordinate.useContents { latitude.toString() }, it.coordinate.useContents { longitude.toString() })
                        })
                    }
                }

                override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                    manager.stopUpdatingLocation()
                    manager.delegate = null
                    if (cont.isActive) cont.resume(null)
                }
            }
            this.currentDelegate = delegate
            locationManager.delegate = delegate
            locationManager.startUpdatingLocation()

            cont.invokeOnCancellation {
                locationManager.stopUpdatingLocation()
                locationManager.delegate = null
                this.currentDelegate = null
            }
        }
    }
}
