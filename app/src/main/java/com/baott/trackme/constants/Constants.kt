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

    // Broadcast receiver
    object Actions {
        const val UPDATE_LOCATION = "UPDATE_LOCATION"
        const val START_LOCATION_SERVICE = "START_LOCATION_SERVICE"
        const val PAUSE_LOCATION_SERVICE = "PAUSE_LOCATION_SERVICE"
        const val RESUME_LOCATION_SERVICE = "RESUME_LOCATION_SERVICE"
        const val REQUEST_UPDATE_LOCATION_SERVICE = "REQUEST_UPDATE_LOCATION_SERVICE"
        const val NEW_SAVED_SESSION = "NEW_SAVED_SESSION"
    }

    // IntentParam
    object IntentParams {
        const val SESSION_INFO = "SESSION_INFO"
    }
}
