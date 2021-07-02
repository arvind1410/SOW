package com.silicontechnologies.plantix.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.WheatActivity
import com.silicontechnologies.plantix.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_locate.*



class LocateFragment : BaseFragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_locate, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
        iv_punjab.setOnClickListener {
            startActivity(Intent(activity, WheatActivity::class.java))
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        googleMap.getUiSettings().setZoomControlsEnabled(true)
        googleMap.setMyLocationEnabled(true)
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.getUiSettings().setCompassEnabled(true)
        val cameraPosition = CameraPosition.Builder().target(
                LatLng(30.900965, 75.857277))
                .zoom(14f)
                .bearing(90f)
                .tilt(40f)
                .build()
        val cameraUpdateFactory = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap.addMarker(
                MarkerOptions().position(LatLng(30.900965, 75.857277))
                        .title("Punjab"))
        googleMap.animateCamera(cameraUpdateFactory)

    }

}