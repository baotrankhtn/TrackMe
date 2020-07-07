package com.baott.trackme.ui.activities.record

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.baott.trackme.R
import com.baott.trackme.service.LocationForegroundService
import com.baott.trackme.ui.base.BaseActivity
import com.baott.trackme.ui.dialog.ConfirmDialog
import com.baott.trackme.utils.IntentUtils
import com.baott.trackme.utils.LocationUtils
import com.blab.moviestv.ui.base.permission.PermissionResult
import com.blab.moviestv.ui.base.permission.PermissionUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class RecordActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        initView()
    }

    override fun onResume() {
        super.onResume()
        checkGps()
    }

    override fun onDestroy() {
        stopLocationService()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mGoogleMap = it
        }
    }

    private fun startRecording() {

        startLocationService()
    }

    private fun startLocationService() {
        askCompactPermissions(arrayOf(
            PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
            PermissionUtils.Manifest_ACCESS_COARSE_LOCATION
        ), object : PermissionResult {
            override fun permissionGranted() {
                if (!LocationUtils.isGpsAvailable()) {
                    ConfirmDialog(this@RecordActivity, getString(R.string.dialog_gps_title), object : ConfirmDialog.IOnClickListener {
                        override fun onBtnOkClick() {
                            IntentUtils.openGpsSetting(this@RecordActivity)
                        }
                    })
                } else {
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

    private fun checkGps() {
        if (!LocationUtils.isGpsAvailable()) {
            ConfirmDialog(this, getString(R.string.dialog_gps_title), object : ConfirmDialog.IOnClickListener {
                override fun onBtnOkClick() {
                    IntentUtils.openGpsSetting(this@RecordActivity)
                }
            })
        }
    }

    private fun initView() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.mMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }
}
