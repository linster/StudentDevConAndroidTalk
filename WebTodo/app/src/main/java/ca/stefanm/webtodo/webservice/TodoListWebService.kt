package ca.stefanm.webtodo.webservice


import ca.stefanm.webtodo.models.TodoItem
import ca.stefanm.webtodo.models.TodoList
import ca.stefanm.webtodo.models.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Square's Retrofit library combines OkHTTP and Moshi (an HTTP client and JSON parser library,
 * respectively) to remove all the boiler plate surrounding making web requests.
 *
 * Typically when making code that calls your own webservice, you write a singleton AbstractWebApi
 * class that has protected methods for building up and sending HTTP requests. These methods within the
 * abstract class then return strings. Then, when you want to make real api calls, you'd have a class
 * that inherits from this abstract class and provides public methods that parse the JSON object on
 * the request and response. As well, these methods are responsible for generating the URL that the
 * request is sent to.
 *
 * Rolling your own works for very small APIs, but it's more error-prone.
 *
 * What Retrofit does, however, is it takes this interface, and uses reflection to generate a bunch
 * of functions that prepare, parse, send, and receive the web requests. There's a factory method
 * that takes in this interface plus a base url, and generates code that is callable and works.
 *
 * Note that this interface should match what your server gives out as results.
 */
interface TodoListWebService {


    /* We want to implement a CRUD interface for todo items.
     * To limit scope creep, creating and deleting multiple todo lists is not part
     * of this example project: we just assume there is only one global todo list
     * that the server cares about.
     *
     * CRUD: Create, Read, Update, Delete
     */

    //Create
    @POST("todo/new")
    fun postNewTodoItemToList(@Body todoItem: TodoItem) : Call<TodoList>

    //Read
    @GET("todo/")
    fun getAllTodoItems() : Call<List<TodoItem>>

    @GET("todo/{id}")
    fun getTodoItemById(@Path("id") id : Int) : Call<TodoItem>


    //Update
    @PUT("todo/{id}")
    fun updateTodoItemById(@Path("id") id : Int) : Call<TodoItem>

    /* Deletion.*/
    @DELETE("todo/{id}")
    fun deleteTodoItemById(@Path("id") id: Int) : Call<TodoList>

}