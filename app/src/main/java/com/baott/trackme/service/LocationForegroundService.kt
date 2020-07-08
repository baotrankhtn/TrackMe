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
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.baott.trackme.log.LOG
import com.baott.trackme.ui.activities.record.RecordActivity
import com.google.android.gms.location.*
import java.util.*


/*
 * Created by baotran on 2020-07-07 
 */

class LocationForegroundService : Service() {
    val ID_NOTIFICATION_CHANNEL = "ID_1234"

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest

    private var mTimer: Timer? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification  = createNotification()
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
                locationResult ?: return
                for (location in locationResult.locations) {
                    LOG.d(location.latitude.toString() + "/" + location.longitude.toString())
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