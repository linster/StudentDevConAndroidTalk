package ca.stefanm.webtodo.models

import android.support.annotation.Keep

/**
 * Created by Stefan on 1/30/2017.
 */
@Keep
data class TodoList(
        var title : String = "",
        var items : Array<TodoItem> = arrayOf(),
        var collaborators : Array<User> = arrayOf()
)