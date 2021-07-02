package com.silicontechnologies.plantix.view.activity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.silicontechnologies.plantix.GetNearbyPlacesData
import com.silicontechnologies.plantix.LocationHelper
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.base.BaseActivity

class NearByActivity : BaseActivity(), OnMapReadyCallback, LocationHelper.LocationListener {

    private val PROXIMITY_RADIUS = 10000
    private var toolbar: Toolbar? = null
    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private val REQUEST_CODE_ASK_LOCATION_PERMISSIONS = 123
    private var locationHelper: LocationHelper? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearbypharmacy)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(
                true)
        locationHelper = LocationHelper(this, this)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        googleMap.getUiSettings().setZoomControlsEnabled(true)
        googleMap.setMyLocationEnabled(true)
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.getUiSettings().setCompassEnabled(true)
        locationHelper!!.init()
    }

    override
    fun onLocationUpdate(location: Location) {
        this.currentLocation = location
        if (currentLocation == null) return
        locationHelper!!.stopLocationUpdates()
        if (googleMap!!.cameraPosition.zoom < 15) {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    15f)
            googleMap!!.moveCamera(cameraUpdate)
        }
        val cameraPosition = CameraPosition.Builder().target(
                LatLng(location.latitude, location.longitude))
                .zoom(14f)
                .bearing(90f)
                .tilt(40f)
                .build()
        val cameraUpdateFactory = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap!!.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                        .title("Your Current Location"))
        googleMap!!.animateCamera(cameraUpdateFactory)
        getNearByPlaces(currentLocation!!.latitude, currentLocation!!.longitude)

    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode != REQUEST_CODE_ASK_LOCATION_PERMISSIONS) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (locationHelper != null) {
            locationHelper!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override
    fun onReadyForLocationUpdates() {
        locationHelper!!.startLocationUpdates()
    }

    override
    fun onUnRecoverableError(what: Int, errorMsg: String?) {
        Toast.makeText(applicationContext, "Can not retrieve your location",
                Toast.LENGTH_SHORT).show()
    }

    override
    fun onUserDeclineRequest(what: Int) {
        Toast.makeText(applicationContext,
                if (what == LocationHelper.CONST_API_CONNECTION)
                    "This feature requires location services."
                else
                    "This feature requires location settings to be enabled. Please enable location setting.",
                Toast.LENGTH_SHORT).show()
    }

    fun clearMap() {
        if (googleMap != null) {
            googleMap!!.clear()
        }
    }

    override fun onStop() {
        super.onStop()
        if (locationHelper != null) locationHelper!!.terminate()
    }

    fun getNearByPlaces(latitude: Double, longitude: Double) {
        val url = getUrl(latitude, longitude)
        val getNearbyPlacesData = GetNearbyPlacesData(googleMap, url)
        getNearbyPlacesData.execute()
    }


    fun getUrl(latitude: Double, longitude: Double): String {

        val googlePlacesUrl = StringBuilder(
                "https://maps.googleapis.com/maps/api/place/textsearch/json?")
        googlePlacesUrl.append("query=Krishi%20Vigyan")
        googlePlacesUrl.append("&location=$latitude,$longitude")
        googlePlacesUrl.append("&radius=50000")
        googlePlacesUrl.append("&sensor=true")
        googlePlacesUrl.append("&key=" + "AIzaSyCHcXCp6JHSMOsi0RzozmrA2DUH2dql1Vs")
        Log.d("getUrl", googlePlacesUrl.toString())
        return googlePlacesUrl.toString()
    }

}