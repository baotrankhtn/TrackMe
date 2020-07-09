package com.baott.trackme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baott.trackme.R
import com.baott.trackme.adapters.base.BaseAdapter
import com.baott.trackme.adapters.base.BaseViewHolder
import com.baott.trackme.adapters.base.LoadingViewHolder
import com.baott.trackme.entities.BaseLoadingEntity
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.utils.DateTimeUtils
import kotlinx.android.synthetic.main.partial_session_info.view.*


/* 
 * Created by baotran on 2020-07-09 
 */

class SessionHistoryAdapter(context: Context) : BaseAdapter<Any?>(context) {
    private var mListener: IOnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        var holder: RecyclerView.ViewHolder = LoadingViewHolder(View(mContext))
        when (viewType) {
            ViewType.NORMAL -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_session_history, parent, false)
                holder = MyViewHolder(view)
            }
            ViewType.LOADING -> {
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val item = mDataList[position]
            if (item is SessionEntity?) {
                item?.let {
                    holder.bind(item, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is BaseLoadingEntity) {
            return ViewType.LOADING
        } else {
            return ViewType.NORMAL
        }
    }

    fun setListener(listener: IOnItemClickListener) {
        mListener = listener
    }

    inner class MyViewHolder(itemView: View) : BaseViewHolder<SessionEntity?>(itemView) {
        override fun bind(item: SessionEntity?, position: Int) {
            item?.let { valItem ->
                // Image
//                valItem.posterPath?.let {
//                    Glide.with(itemView.context)
//                        .load(StringUtils.generatePosterUrl(it))
//                        .into(itemView.mIvPoster)
//                }

                // Distance
                itemView.mTvDistance.text =
                    String.format(mContext.getString(R.string.cm_format_km), valItem.distance / 1000)

                // Avg speed
                itemView.mTvSpeed.text =
                    String.format(mContext.getString(R.string.cm_format_kmph), valItem.calculateAverageSpeed())
                itemView.mTvSpeedTitle.text = mContext.getString(R.string.session_avg_speed)

                // Duration
                itemView.mTvDuration.text = DateTimeUtils.convertMillisToHms(valItem.duration)

                // Listener
                itemView.setOnClickListener {
                    mListener?.onItemClick(valItem)
                }
            }
        }
    }

    interface IOnItemClickListener {
        fun onItemClick(entity: SessionEntity)
    }
}