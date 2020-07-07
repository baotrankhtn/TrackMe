package com.baott.trackme.adapters.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.animation.AlphaAnimation
import com.baott.trackme.entities.BaseLoadingEntity
import com.baott.trackme.utils.ListUtils


/* 
 * Created by baotran on 10/9/18 
 */

abstract class BaseAdapter<T : Any?> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    companion object {
        const val DURATION_ANIMATION_FADING : Long = 400
    }

    object ViewType {
        const val NORMAL = 0
        const val LOADING = 1
    }

    protected val mContext: Context
    protected val mDataList: MutableList<T?>

    constructor(context: Context) {
        mContext = context
        mDataList = ArrayList()
    }

    constructor(context: Context, list: MutableList<T?>) {
        mContext = context
        if (!ListUtils.isNullOrEmpty(list)) {
            mDataList = list
        } else {
            mDataList = ArrayList()
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setData(list: MutableList<T?>) {
        if (!ListUtils.isNullOrEmpty(list)) {
            mDataList.clear()
            mDataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addData(list: MutableList<T?>) {
        removeLoading()
        if (!ListUtils.isNullOrEmpty(list)) {
            mDataList.addAll(list)
            notifyItemRangeChanged(mDataList.size, list.size)
        }
    }

    fun insertItem(item: T?) {
        mDataList.add(item)
        notifyItemInserted(mDataList.size)
    }

    fun insertItem(item: T?, index: Int) {
        if (index <= mDataList.size) {
            mDataList.add(index, item)
            notifyItemInserted(index)
        }
    }

    fun removeItem(index: Int) {
        if (index >= 0 && index < mDataList.size) {
            mDataList.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, getItemCount())
        }
    }

    fun clear() {
        if (mDataList.size != 0) {
            mDataList.clear()
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): T? {
        if (position >= 0 && position < mDataList.size) {
            return mDataList[position]
        }
        return null
    }

    /**
     * Fading animation when items appear
     */
    fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = DURATION_ANIMATION_FADING
        view.startAnimation(anim)
    }

    /**
     * Remove loading before setting or adding data
     */
    fun removeLoading() {
        val lastIndex = mDataList.size - 1
        val lastItem : T? = mDataList[lastIndex]
        if (lastItem is BaseLoadingEntity) {
            removeItem(lastIndex)
        }
    }

    /**
     * Check is list is loading more
     */
    fun isLoadingMore() : Boolean {
        val lastIndex = mDataList.size - 1
        val lastItem : T? = mDataList[lastIndex]
        if (lastItem is BaseLoadingEntity) {
            return true
        }
        return false
    }

    /**
     * Add loading more
     */
    fun addLoadingMore() {
        insertItem(BaseLoadingEntity() as T)
    }
}
