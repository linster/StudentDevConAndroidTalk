package ca.stefanm.webtodo.models

import android.support.annotation.Keep

/**
 * Created by Stefan on 1/30/2017.
 */
@Keep
data class TodoItem(
        /* This ID is set by the server. By setting this as a non-nullable type,
         * we limit the app to only being able to create items if there is
         * a network connection */
        val id : Int = 0,
        var completed : Boolean = false,
        var contents : String = "",
        var modifiedOn : Long = 0L, /* Unix time stamp */
        var creator : User,
        var geoLat : Double = 0.0,
        var geoLng : Double = 0.0
)