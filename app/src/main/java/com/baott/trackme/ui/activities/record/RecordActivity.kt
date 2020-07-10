package com.baott.trackme.ui.activities.record

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.baott.trackme.R
import com.baott.trackme.constants.Constants
import com.baott.trackme.db.RoomManager
import com.baott.trackme.db.callbacks.IInsertSessionCallback
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.helpers.GsonHelper
import com.baott.trackme.log.LOG
import com.baott.trackme.service.LocationForegroundService
import com.baott.trackme.ui.base.BaseActivity
import com.baott.trackme.utils.BitmapUtils
import com.baott.trackme.utils.DateTimeUtils
import com.baott.trackme.utils.LocationUtils
import com.baott.trackme.utils.StorageUtils
import com.blab.moviestv.ui.base.permission.PermissionResult
import com.blab.moviestv.ui.base.permission.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.partial_session_info.*
import java.util.*


class RecordActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var mMapFragment: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mMarkerCurrentPos: Marker? = null

    private var mReceiver: BroadcastReceiver? = null
    private var mIsInForeground = false // Check if activity is in foreground

    private var mSessionInfo: SessionEntity? = null
    private var mIndexRendered: Int = -1 // Index of latest rendered point on map
    private var mTimer: Timer? = null // Update duration every seconds
    private var mCountZoomToFit = 0 // Zoom map to fit all coordinates after n times

    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        initView()
        initListener()
        initBroadcastReceiver()
        startLocationService()
    }

    override fun onStart() {
        super.onStart()
        // Foreground or background
        mIsInForeground = true
    }

    override fun onStop() {
        // Foreground or background
        mIsInForeground = false

        // Timer
        cancelTimer()

        super.onStop()
    }

    override fun onDestroy() {
        stopLocationService()
        mReceiver?.let {
            unregisterReceiver(mReceiver)
        }
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onBackPressed() {
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
    private fun onLocationUpdate(sessionInfo: SessionEntity) {
        // Save for later use
        mSessionInfo = sessionInfo

        // Process points
        val points = sessionInfo.points
        LOG.d("Size: ${points.size}")
        if (points.size > 0) {
            // Show first point marker
            if (mIndexRendered == -1) {
                moveCamera(points[0].lat, points[0].lng)
                showStartMarker(points[0].lat, points[0].lng)
                mIndexRendered = 0 // update render index
            }

            // Render missing points
            if (mIndexRendered < points.size - 1) {
                for (index in (mIndexRendered + 1)..(points.size - 1)) {
                    drawLine(points[index - 1].lat, points[index - 1].lng, points[index].lat, points[index].lng)
                    mIndexRendered = index // update render index
                    LOG.d("Render index: $mIndexRendered")
                }
            }

            // Show current position marker
            showCurrentLocationMarker(points[points.size - 1].lat, points[points.size - 1].lng)

            // Update tracking info
            updateTrackingInfo(sessionInfo.distance, sessionInfo.calculateCurrentSpeed(), sessionInfo.duration)

            // Zoom to fit
            if (mCountZoomToFit >= 5) {
                moveCameraToFitAllCoordinates()
                mCountZoomToFit = 0
            } else {
                mCountZoomToFit++
            }
        }
    }

    private fun moveCamera(lat: Double, lng: Double) {
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 25f))
    }

    /**
     * For zooming camera to fit all coordinates
     */
    private fun moveCameraToFitAllCoordinates(animateCallback: GoogleMap.CancelableCallback? = null) {
        mSessionInfo?.let {
            val points = it.points
            if (points.size > 0) {
                val boundBuilder = LatLngBounds.Builder()
                for (point in points) {
                    boundBuilder.include(LatLng(point.lat, point.lng))
                }
                val padding = 30 // offset from edges of the map in pixels
                val cu = CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), padding)
                mGoogleMap?.animateCamera(cu, animateCallback)
            }
        }
    }

    private fun drawLine(lat: Double, lng: Double, nextLat: Double, nextLng: Double) {
        val options = PolylineOptions().width(10f).color(ContextCompat.getColor(this, R.color.colorPrimary))
        options.add(LatLng(lat, lng))
        options.add(LatLng(nextLat, nextLng))
        mGoogleMap?.addPolyline(options)
    }

    private fun showCurrentLocationMarker(lat: Double, lng: Double) {
        mMarkerCurrentPos?.remove()

        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_my_location
        )
        mMarkerCurrentPos = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .icon(
                    BitmapDescriptorFactory
                        .fromBitmap(BitmapUtils.resizeBitmap(bitmap, 30))
                )
        )
    }

    private fun showStartMarker(lat: Double, lng: Double) {
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_start_location
        )
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
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
                    serviceIntent.action = Constants.Actions.START_LOCATION_SERVICE
                    ContextCompat.startForegroundService(this@RecordActivity, serviceIntent)
                    showState(1)
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

    /**
     * State of running service
     * 0: pause
     * 1: running
     */
    private fun showState(state: Int) {
        when (state) {
            0 -> {
                mBtnPause.visibility = View.GONE
                mBtnResume.visibility = View.VISIBLE
                mBtnStop.visibility = View.VISIBLE
            }

            1 -> {
                mBtnPause.visibility = View.VISIBLE
                mBtnResume.visibility = View.GONE
                mBtnStop.visibility = View.GONE
            }
        }
    }

    private fun cancelTimer() {
        mTimer?.cancel()
        mTimer = null
    }

    private fun initView() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.mMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    private fun initListener() {
        mBtnPause.setOnClickListener {
            // Send event to service
            val serviceIntent = Intent(this@RecordActivity, LocationForegroundService::class.java)
            serviceIntent.action = Constants.Actions.PAUSE_LOCATION_SERVICE
            ContextCompat.startForegroundService(this@RecordActivity, serviceIntent)

            // Cancel timer
            cancelTimer()

            // Update ui
            showState(0)
        }

        mBtnResume.setOnClickListener {
            // Send event to service
            val serviceIntent = Intent(this@RecordActivity, LocationForegroundService::class.java)
            serviceIntent.action = Constants.Actions.RESUME_LOCATION_SERVICE
            ContextCompat.startForegroundService(this@RecordActivity, serviceIntent)

            // Update ui
            showState(1)
        }

        mBtnStop.setOnClickListener {
            // Stop service
            stopLocationService()

            // Save to db
            mSessionInfo?.let { valSessionInfo ->
                if (!valSessionInfo.points.isNullOrEmpty()) {
                    // Id is time in millis
                    valSessionInfo.id = System.currentTimeMillis()

                    RoomManager.insertSession(this@RecordActivity, valSessionInfo, object : IInsertSessionCallback {
                        override fun onSuccess(sessionEntity: SessionEntity) {
                            LOG.d("Insert session done")

                            // Resize map to make smaller image
                            mMapFragment.view?.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                600
                            )

                            // Wait and move camera to fit all points
                            mHandler.postDelayed({
                                moveCameraToFitAllCoordinates(object : GoogleMap.CancelableCallback {
                                    override fun onFinish() {
                                        LOG.d("Google map animates done")

                                        // Create bitmap from google map
                                        mGoogleMap?.snapshot { bitmap ->
                                            // Save bitmap to local: name of bitmap is the id of session
                                            StorageUtils.saveBitmapToInternal(
                                                this@RecordActivity,
                                                bitmap,
                                                valSessionInfo.id.toString()
                                            )

                                            // Notify history screen
                                            val intent = Intent()
                                            intent.action = Constants.Actions.NEW_SAVED_SESSION
                                            intent.putExtra(
                                                Constants.IntentParams.SESSION_INFO,
                                                GsonHelper.getInstance().toJson(sessionEntity)
                                            )
                                            sendBroadcast(intent)

                                            finish()
                                        }
                                    }

                                    override fun onCancel() {
                                        LOG.d("Google map animates error")
                                        finish()
                                    }
                                })
                            }, 500)
                        }

                        override fun onError(throwable: Throwable) {
                            LOG.d("Insert session error")
                            finish()
                        }
                    })
                } else {
                    LOG.d("No session to save")
                    finish()
                }
            } ?: run {
                LOG.d("No session to save")
                finish()
            }
        }
    }

    private fun initBroadcastReceiver() {
        mReceiver = MyReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.Actions.UPDATE_LOCATION)
        registerReceiver(mReceiver, intentFilter)
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            // Check if activity is in foreground
            if (mIsInForeground) {
                intent?.action?.let {
                    when (it) {
                        Constants.Actions.UPDATE_LOCATION -> {
                            // Parse data
                            val strSessionInfo = intent.extras?.getString(Constants.IntentParams.SESSION_INFO, null)
                            val sessionInfo =
                                GsonHelper.getInstance().fromJson(strSessionInfo, SessionEntity::class.java)

                            sessionInfo?.let { valSessionInfo ->
                                onLocationUpdate(valSessionInfo)
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
