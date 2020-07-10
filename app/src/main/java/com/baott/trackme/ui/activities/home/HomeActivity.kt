package com.baott.trackme.ui.activities.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baott.trackme.R
import com.baott.trackme.adapters.SessionHistoryAdapter
import com.baott.trackme.constants.Constants
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.helpers.EndlessRecyclerViewScrollListener
import com.baott.trackme.helpers.GsonHelper
import com.baott.trackme.log.LOG
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
    private lateinit var mScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var mAdapter: SessionHistoryAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mViewModel: SessionViewModel
    private var mReceiver: MyReceiver? = null
    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initListener()
        initBroadcastReceiver()
        initViewModel()
        loadData(System.currentTimeMillis())
    }

    override fun onDestroy() {
        mReceiver?.let {
            unregisterReceiver(it)
        }
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    /**
     * Get sessions whose id < afterId
     */
    private fun loadData(afterId: Long) {
        mViewModel.requestSessionHistory(afterId, 7)
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
        // Load more
        mScrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!mAdapter.isLoadingMore()) {
                    LOG.d("Load more")
                    val lastItem = mAdapter.getItem(mAdapter.itemCount - 1)
                    if (lastItem is SessionEntity) {
                        mAdapter.addLoadingMore()
                        mHandler.postDelayed({
                            loadData(lastItem.id)
                        }, 500)
                    }
                }
            }
        }
        mRecyclerView.addOnScrollListener(mScrollListener)

        // Record
        mBtnRecord.setOnClickListener {
            mBtnRecord.isEnabled = false
            if (LocationUtils.isGpsAvailable()) {
                askCompactPermissions(arrayOf(
                    PermissionUtils.Manifest_ACCESS_FINE_LOCATION
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

            // Avoid double click
            mHandler.postDelayed({
                mBtnRecord.isEnabled = true
            }, 300)
        }
    }

    private fun initBroadcastReceiver() {
        mReceiver = MyReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.Actions.NEW_SAVED_SESSION)
        registerReceiver(mReceiver, intentFilter)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        mViewModel.getSessionHistory().observe(this, Observer {
            // Remove loading more if needed
            mAdapter.removeLoading()

            // Set data
            if (!it.isNullOrEmpty()) {
                mAdapter.addData(it as MutableList<Any?>)
            }

            if (mAdapter.itemCount > 0) {
                mTvNoHistory.visibility = View.GONE
            } else {
                mTvNoHistory.visibility = View.VISIBLE
            }

            // Remove loading screen
            if (mViewLoading.visibility == View.VISIBLE) {
                mHandler.postDelayed({
                    mViewLoading.visibility = View.GONE
                }, 500)
            }
        })
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.action?.let {
                when (it) {
                    Constants.Actions.NEW_SAVED_SESSION -> {
                        val strSessionInfo = intent.extras?.getString(Constants.IntentParams.SESSION_INFO, null)
                        val sessionInfo = GsonHelper.getInstance().fromJson(strSessionInfo, SessionEntity::class.java)

                        sessionInfo?.let { valSessionInfo ->
                            mTvNoHistory.visibility = View.GONE
                            mAdapter.insertItem(valSessionInfo, 0)

                            // Scroll to top
                            mLayoutManager.scrollToPositionWithOffset(0, 0)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }
}
