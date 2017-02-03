package ca.stefanm.webtodo.models

import android.support.annotation.Keep
import java.util.*

/**
 * Created by Stefan on 1/30/2017.
 */
@Keep
data class TodoItem(
        var completed : Boolean = false,
        var contents : String = "",
        var modifiedOn : Date = Date(),
        var creator : User?
)