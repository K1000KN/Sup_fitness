package com.example.sup_fitness

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment() {

    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null
    private var cameraPosition: CameraPosition? = null



    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this.requireContext()))
        getLocationPermission()
        updateLocationUI(googleMap)
        getDeviceLocation(googleMap)
        MarkButton.setOnClickListener{
            addMarkerOnCurrentLocation(googleMap)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        Places.initialize(this.requireContext(), "AIzaSyBvP6gtIo0hRIxI1DyqHLFpCjaYtqMpxGE")
        placesClient = Places.createClient(this.requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun getDeviceLocation(map: GoogleMap) {

        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(ContentValues.TAG, "Current location is null. Using defaults.")
                        Log.e(ContentValues.TAG, "Exception: %s", task.exception)
                        map.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray,) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
    }

    private fun updateLocationUI(map: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun addMarkerOnCurrentLocation(map: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener(this.requireActivity()
                    ) { location ->
                        if (location != null) {
                            val currLat = location.latitude
                            val currLong = location.longitude
                            val currLocation = LatLng(currLat,currLong)
                            val currSpeed = location.speed

                            map.addMarker(MarkerOptions().position(currLocation).title("MyPosition").snippet(
                                "Latitude : $currLat\nLongitude : $currLong\nSpeed : $currSpeed"
                            ))
                        }
                    }
            } else {
                return
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }

}

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("InflateParams")
    var mWindow: View = (context as Activity).layoutInflater.inflate(R.layout.custom_marker_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}