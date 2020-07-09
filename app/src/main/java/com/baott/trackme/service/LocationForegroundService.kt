package com.baott.trackme.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.Notification
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.baott.trackme.constants.Constants
import com.baott.trackme.entities.TrackInfoEntity
import com.baott.trackme.helpers.GsonHelper
import com.baott.trackme.log.LOG
import com.baott.trackme.ui.activities.record.RecordActivity
import com.google.android.gms.location.*
import java.util.*


/*
 * Created by baotran on 2020-07-07 
 */

class LocationForegroundService : Service() {
    val ID_NOTIFICATION_CHANNEL = "ID_1234"

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest

    private val mTrackInfo = TrackInfoEntity()
    private var mTimeLastSave: Long = System.currentTimeMillis()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
        setupLocationClient()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopLocationUpdates()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    /**
     * Process location update
     */
    private fun onLocationUpdate(lat: Double, lng: Double) {
        LOG.d("$lat/$lng")

        // Add point
        mTrackInfo.addPoint(lat, lng, mTimeLastSave, System.currentTimeMillis())
        mTimeLastSave = System.currentTimeMillis()

        // Send broadcast
        val bundle = Bundle()
        val intent = Intent()
        intent.action = Constants.Actions.BROADCAST_FROM_LOCATION_SERVICE
        bundle.putString(Constants.IntentParams.TRACKINFO, GsonHelper.getInstance().toJson(mTrackInfo))
        intent.putExtras(bundle)
        sendBroadcast(intent)
    }

    private fun setupLocationClient() {
        // Get location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up request and callback
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    if (!it.locations.isNullOrEmpty()) {
                        val location = it.locations[it.locations.size - 1]
                        onLocationUpdate(location.latitude, location.longitude)
                    }
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    private fun createNotification(): Notification {
        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                ID_NOTIFICATION_CHANNEL,
                "TrackMe Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(this, RecordActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        return NotificationCompat.Builder(this, ID_NOTIFICATION_CHANNEL)
            .setContentText("TrackMe Service is running")
            .setSmallIcon(com.baott.trackme.R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }
}