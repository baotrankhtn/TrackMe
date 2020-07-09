package com.baott.trackme.ui.activities.record

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.baott.trackme.R
import com.baott.trackme.constants.Constants
import com.baott.trackme.entities.TrackInfoEntity
import com.baott.trackme.helpers.GsonHelper
import com.baott.trackme.service.LocationForegroundService
import com.baott.trackme.ui.base.BaseActivity
import com.baott.trackme.utils.BitmapUtils
import com.baott.trackme.utils.DateTimeUtils
import com.baott.trackme.utils.LocationUtils
import com.blab.moviestv.ui.base.permission.PermissionResult
import com.blab.moviestv.ui.base.permission.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.partial_session_info.*
import java.util.*

class RecordActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var mMapFragment: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null

    private var mReceiver: BroadcastReceiver? = null
    private var mIndexRendered: Int = -1 // Index of latest rendered point on map
    private var mIsInForeground = false // Check if activity is in foreground

    private var mTimer: Timer? = null // Update duration every seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        initView()
        initBroadcastReceiver()
    }

    override fun onStart() {
        super.onStart()
        // Foreground or background
        mIsInForeground = true

        startLocationService()
    }

    override fun onResume() {
        super.onResume()
        checkGps()
    }

    override fun onStop() {
        // Foreground or background
        mIsInForeground = false

        // Timer
        mTimer?.cancel()
        mTimer = null

        super.onStop()
    }

    override fun onDestroy() {
        stopLocationService()
        mReceiver?.let {
            unregisterReceiver(mReceiver)
        }
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mGoogleMap = it
        }
    }

    /**
     * Location update from service
     * Maybe some points are not rendered when this activity is in background
     */
    private fun onLocationUpdate(trackInfo: TrackInfoEntity) {
        val points = trackInfo.points
        if (points.size > 0) {
            // First point: show marker
            if (mIndexRendered == -1) {
                moveCamera(points[0].lat, points[0].lng)
                showStartMarker(LatLng(points[0].lat, points[0].lng))
                mIndexRendered = 0 // update render index
            }

            // Render missing points
            if (mIndexRendered < points.size - 1) {
                for (index in mIndexRendered + 1 until points.size - 1) {
                    drawLine(points[index - 1].lat, points[index - 1].lng, points[index].lat, points[index].lng)
                    mIndexRendered = index // update render index
                }
            }

            // Update tracking info
            updateTrackingInfo(trackInfo.distance, trackInfo.calculateCurrentSpeed(), trackInfo.duration)
        }
    }

    private fun moveCamera(lat: Double, lng: Double) {
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 23f))
    }

    private fun drawLine(lat: Double, lng: Double, nextLat: Double, nextLng: Double) {
        val options = PolylineOptions().width(8f).color(ContextCompat.getColor(this, R.color.colorPrimary))
        options.add(LatLng(lat, lng))
        options.add(LatLng(nextLat, nextLng))
        mGoogleMap?.addPolyline(options)
    }

    private fun showStartMarker(latlng: LatLng) {
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_start_location
        )
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(latlng)
                .title("Wrong Turn!")
                .icon(
                    BitmapDescriptorFactory
                        .fromBitmap(BitmapUtils.resizeBitmap(bitmap, 100))
                )
        )
    }

    private fun updateTrackingInfo(distance: Float, currentSpeed: Float, duration: Long) {
        // Distance
        mTvDistance.text = String.format(getString(R.string.cm_format_km), distance / 1000)

        // Current speed
        mTvSpeed.text = String.format(getString(R.string.cm_format_kmph), currentSpeed)

        // Duration: check timer
        if (mTimer == null) {
            var timerDuration = duration
            mTimer = Timer()
            mTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    timerDuration += 1000
                    mTvDuration.text = DateTimeUtils.convertMillisToHms(timerDuration)
                }
            }, 0, 1000)
        }
    }

    private fun startLocationService() {
        askCompactPermissions(arrayOf(
            PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
            PermissionUtils.Manifest_ACCESS_COARSE_LOCATION
        ), object : PermissionResult {
            override fun permissionGranted() {
                if (LocationUtils.isGpsAvailable()) {
                    val serviceIntent = Intent(this@RecordActivity, LocationForegroundService::class.java)
                    ContextCompat.startForegroundService(this@RecordActivity, serviceIntent)
                }
            }

            override fun permissionDenied() {
            }

            override fun permissionForeverDenied() {
            }

        })
    }

    private fun stopLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        stopService(serviceIntent)
    }

    private fun initView() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.mMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    private fun initBroadcastReceiver() {
        mReceiver = MyReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.Actions.BROADCAST_FROM_LOCATION_SERVICE)
        registerReceiver(mReceiver, intentFilter)
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            // Check if activity is in foreground
            if (mIsInForeground) {
                intent?.action?.let {
                    when (it) {
                        Constants.Actions.BROADCAST_FROM_LOCATION_SERVICE -> {
                            // Parse data
                            val strTrackInfo = intent.extras?.getString(Constants.IntentParams.TRACKINFO, null)
                            val trackInfo = GsonHelper.getInstance().fromJson(strTrackInfo, TrackInfoEntity::class.java)

                            trackInfo?.let { valTrackInfo ->
                                onLocationUpdate(valTrackInfo)
                            }
                        }
                        else -> {

                        }
                    }

                }
            }
        }
    }
}
