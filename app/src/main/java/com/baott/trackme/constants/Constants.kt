package com.baott.trackme.constants

/*
 * Created by baotran on 2019-07-09
 */

class Constants {
    companion object {
        const val DEVELOPMENT_MODE = DevelopmentMode.DEVELOPMENT
    }

    // Development mode
    object DevelopmentMode {
        const val DEVELOPMENT = 0
        const val PRODUCTION = 1
    }

    // Connection
    object Connection {
        const val TIME_OUT_CONNECTION: Long = 30
        const val TIME_OUT_READ: Long = 30
        const val TIME_OUT_WRITE: Long = 30
    }

    // Load more
    object LoadMore {
        const val TIME_WAITING: Long = 300 // ms
    }
}
