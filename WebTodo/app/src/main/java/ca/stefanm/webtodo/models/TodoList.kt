package ca.stefanm.webtodo.models

import android.support.annotation.Keep
import java.util.*

/**
 * The TodoList acts like a "holder" for TodoItems. In this demo, I make the simplifying
 * assumption that there is only one global todo list on the server. With a model like this,
 * it's simple to extend the application to have multiple todolists.
 */
@Keep
data class TodoList(
        /* Challenge to the reader:
         *  - Uncomment the id and title fields
         *  - Add some functions into TodoListWebService to fetch todoLists containing todoItems.
         *  - Add a todo list switcher activity that allows the user to switch between available todo lists
         *  - Update the storage controller to handle todo list switching. (Simplest design: when switching
         *    to another todo list, just overwrite the local copy of the todolist in the session)
         *
         */

        //val id : Int = 1,
        //var title : String = "",
        var items : MutableList<TodoItem> = ArrayList<TodoItem>()
)