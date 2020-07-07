package com.baott.trackme.adapters.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baott.trackme.R
import com.baott.trackme.entities.BaseLoadingEntity
import com.baott.trackme.entities.NewsEntity
import kotlinx.android.synthetic.main.item_news.view.*


/* 
 * Created by baotran on 2019-07-11 
 */

class NewsAdapter(context: Context) : BaseAdapter<Any?>(context) {
    private var mListener: IOnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        var holder: RecyclerView.ViewHolder = LoadingViewHolder(View(mContext))
        when (viewType) {
            ViewType.NORMAL -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
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
            if (item is NewsEntity?) {
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

    inner class MyViewHolder(itemView: View) : BaseViewHolder<NewsEntity?>(itemView) {
        override fun bind(item: NewsEntity?, position: Int) {
            item?.let { valItem ->
                //                // Poster
//                valItem.posterPath?.let {
//                    Glide.with(itemView.context)
//                        .load(StringUtils.generatePosterUrl(it))
//                        .into(itemView.mIvPoster)
//                }

                // Title
                valItem.title?.let {
                    itemView.mTvTitle.text = it
                }

                // Content
                valItem.description?.let {
                    itemView.mTvContent.text = it
                }

                // Listener
                itemView.setOnClickListener {
                    mListener?.let {
                        it.onItemClick(valItem)
                    }
                }
            }
        }
    }

    interface IOnItemClickListener {
        fun onItemClick(entity: NewsEntity)
    }
}