package com.baott.trackme.ui.base

import android.view.View
import com.blab.moviestv.ui.base.permission.FragmentPermissionManager


/* 
 * Created by baotran on 2019-07-09 
 */

class BaseFragment : FragmentPermissionManager() {
    protected var mView: View? = null
}