package com.baott.trackme.ui.activities.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baott.trackme.R
import com.baott.trackme.adapters.SessionHistoryAdapter
import com.baott.trackme.ui.activities.record.RecordActivity
import com.baott.trackme.ui.base.BaseActivity
import com.baott.trackme.ui.viewmodel.NewsListViewModel
import com.baott.trackme.ui.viewmodel.SessionViewModel
import com.baott.trackme.utils.IntentUtils
import com.baott.trackme.utils.LocationUtils
import com.blab.moviestv.ui.base.permission.PermissionResult
import com.blab.moviestv.ui.base.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {
    private lateinit var mAdapter: SessionHistoryAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mViewModel: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initListener()
        initViewModel()
        loadData()
    }

    private fun loadData() {
        mViewModel.requestSessionHistory(0, 20)
    }

    private fun initView() {
        // RecyclerView
        mLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = SessionHistoryAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun initListener() {
        mBtnRecord.setOnClickListener {
            if (LocationUtils.isGpsAvailable()) {
                askCompactPermissions(arrayOf(
                    PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
                    PermissionUtils.Manifest_ACCESS_COARSE_LOCATION
                ), object : PermissionResult {
                    override fun permissionGranted() {
                        IntentUtils.openActivity(this@HomeActivity, RecordActivity::class.java, null, false)
                    }

                    override fun permissionDenied() {
                    }

                    override fun permissionForeverDenied() {
                    }

                })
            } else {
                showDialogGps()
            }
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        mViewModel.getSessionHistory().observe(this, Observer {
            if (it.isNullOrEmpty()) {
                mTvNoHistory.visibility = View.VISIBLE
            } else {
                mTvNoHistory.visibility = View.GONE
                mAdapter.setData(it as MutableList<Any?>)
            }
        })
    }
}
