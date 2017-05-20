package ca.stefanm.webtodo

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast


class ToDoListGPS : LocationListener {

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters

    // The minimum time between updates in milliseconds
    private val MIN_UPDATES_TIME = (1000 * 10).toLong() // 10 seconds

    private val locationManager: LocationManager?

    private var currentLocation: Location? = null

    private var isNull = true

    constructor(activity: Activity){
        locationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast(activity).setText("Permission Error")
            return
        }
        isNull = false
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_UPDATES_TIME,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
        this)

            currentLocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)

    }


    override fun onProviderDisabled(provider: String?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onLocationChanged(location: Location?) {
        currentLocation = location
    }

    fun getCurrentLocation(): Location?{
        return currentLocation
    }

    fun isNull() : Boolean{
        return isNull
    }
}