package ca.stefanm.webtodo.localstorage

import android.content.Context
import ca.stefanm.webtodo.models.TodoList
import ca.stefanm.webtodo.models.User

/**
 * This class contains the items that we wish to store in the shared preferences. It uses
 * the SharedPreferencesHelper to load and store these items from the shared preferences.
 *
 * Usage in Kotlin: val user = Session(getApplicationContext()).currentUser
 */
class Session(val context: Context) {

    var currentUser : User
        get() = SharedPreferencesHelper.getObject(context, "session_current_user", User::class.java ) as User
        set(user) = SharedPreferencesHelper.setObject(context, user, "session_current_user")

    var todoList : TodoList
        get() = SharedPreferencesHelper.getObject(context, "session_current_todoList", TodoList::class.java) as TodoList
        set(todoList) = SharedPreferencesHelper.setObject(context, todoList, "session_current_todoList")


}