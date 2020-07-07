package com.baott.trackme.adapters.base

import android.support.v7.widget.RecyclerView
import android.view.View

/*
 * Created by baotran on 10/9/18
 */

abstract class BaseViewHolder<T : Any?>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T?, position: Int)
}

