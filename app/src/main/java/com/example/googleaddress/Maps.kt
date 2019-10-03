package com.example.googleaddress

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

abstract class Maps : AppCompatActivity(), OnMapReadyCallback {

    protected lateinit var mMap: GoogleMap
    // UPC San Isidro
    protected val defaultZoom = 18f
    private val defaultLatLang = LatLng(-12.087414, -77.050178)
    protected var mLocationPermissionGranted = false
    private val permissionRequestAccessFineLocation = 1

    private val geocoder get() = Geocoder(this)

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in UPC San Isidro and move the camera
        val upcSI = defaultLatLang
        mMap.addMarker(MarkerOptions().position(upcSI).title("UPC San Isidro"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(upcSI, defaultZoom))

        mMap.uiSettings.isZoomControlsEnabled = true

        // Request permission
        getLocationPermission()
    }

    protected fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback
         * onRequestPermissionResult.
         */
        mLocationPermissionGranted = false
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionRequestAccessFineLocation)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when(requestCode) {
            permissionRequestAccessFineLocation -> {
                // If request is cancelled, the result arrays are empty
                if(grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    protected fun getFromLocationName(searchValue: String, maxResults: Int = 1): MutableList<Address> {
        return geocoder.getFromLocationName(searchValue, maxResults)
    }

    protected fun addressHasLatLng(address: Address): Boolean {
        return address.hasLatitude() && address.hasLongitude()
    }
}