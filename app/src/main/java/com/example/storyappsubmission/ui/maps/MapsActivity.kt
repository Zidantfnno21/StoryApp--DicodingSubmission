package com.example.storyappsubmission.ui.maps

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.model.Story
import com.example.storyappsubmission.databinding.ActivityMapsBinding
import com.example.storyappsubmission.helper.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mMap : GoogleMap
    private lateinit var binding : ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.builder()
    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap : GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getUserLocation().observe(this) {result->
            if (result != null) {
                when(result){
                    is com.example.storyappsubmission.data.Result.Loading -> { }
                    is com.example.storyappsubmission.data.Result.Success -> {
                        addManyMarker(result.data.listStory)
                    }
                    is com.example.storyappsubmission.data.Result.Error -> {
                        Log.e(TAG , result.error)
                        Toast.makeText(this@MapsActivity , R.string.message_fetch_list , Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG , "Style Parsing Failed")
            }
        } catch (exception : Resources.NotFoundException) {
            Log.e(TAG, "Cannot Find Style. Error: ", exception)
        }
    }

    private fun addManyMarker(stories: List<Story>) {
        stories.forEach { tourism ->
            val latLng = LatLng(tourism.lat , tourism.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}