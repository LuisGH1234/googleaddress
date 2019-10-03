package com.example.googleaddress

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.googleaddress.network.ConsorcioApi
import com.example.googleaddress.network.responses.ApiResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Response
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "MapsActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var searchButton: Button
    private lateinit var helperText: TextView

    // UPC San Isidro
    private val defaultZoom = 18f
    private val defaultLatLang = LatLng(-12.087414, -77.050178)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setSupportActionBar(findViewById(R.id.toolbar))
        searchButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener(clickListener)

        helperText = findViewById(R.id.helper_text)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in UPC San Isidro and move the camera
        val upcSI = defaultLatLang
        mMap.addMarker(MarkerOptions().position(upcSI).title("UPC San Isidro"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(upcSI, defaultZoom))

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun onSuccess(response: Response<ApiResponse>) {
        // Log.i(TAG, response.body().toString())
        val text = response.body()?.address
        helperText.text = text
        if (text != null) geoLocate(text)
    }

    private val clickListener = View.OnClickListener {
        Log.i(TAG, "Button: Calling address")
        ConsorcioApi.getShipment(::onSuccess)
    }

    private fun geoLocate(text: String) {
        Log.d(TAG, "Geolocate: geolocating")
        try {
            val geocoder = Geocoder(this)
            val addressList = geocoder.getFromLocationName(text, 1)

            if (addressList.isNotEmpty()) {
                Log.i(TAG, "Geolocate: Result - ${addressList[0]}")
                val address = addressList[0]

                if (address.hasLatitude() && address.hasLongitude()) {
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().title(address.locality)
                        .position(latLng).snippet(address.getAddressLine(0)))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom))
                } else Log.d(TAG,
                    "Geolocate: Result - address has not longitude and/or latitude")
            }
        } catch (ex: IOException) {
            Log.e(TAG, "Geolocate: IOExeption - ${ex.message}")
        }
    }
}
