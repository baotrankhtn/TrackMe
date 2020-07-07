package com.baott.trackme.ui.activities.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.baott.trackme.R
import com.baott.trackme.adapters.base.NewsAdapter
import com.baott.trackme.log.LOG
import com.baott.trackme.ui.base.BaseActivity
import com.baott.trackme.ui.viewholder.NewsListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var mAdapter: NewsAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mViewModel: NewsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
        loadData()
    }

    private fun loadData() {
        mViewModel.requestNewsList("vietnam")
        LOG.d("Load data")
    }

    private fun initView() {
        // RecyclerView
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = NewsAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun initListener() {
        mViewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)
        mViewModel.getNewsList().observe(this, Observer {
            mAdapter.setData(it as MutableList<Any?>)
            LOG.d("setData")
        })

        mButton.setOnClickListener(View.OnClickListener {
            loadData()
        })
    }
}
