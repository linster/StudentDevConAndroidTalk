package ca.stefanm.webtodo.models

import android.support.annotation.Keep

/**
 * Created by Stefan on 1/30/2017.
 */
@Keep
data class User(
        val username : String = "",
        val friendlyName : String = "",
        val email : String = "",
        val authToken : String? /* The server does not send this to other collaborators */
)