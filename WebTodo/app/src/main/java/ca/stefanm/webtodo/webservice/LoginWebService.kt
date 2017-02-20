package ca.stefanm.webtodo.webservice

import ca.stefanm.webtodo.models.User
import retrofit2.Call
import retrofit2.http.*


/**
 * I've separated out the Login Web Service from the TodoListWebService so that if you're building
 * your own web-enabled Android app with login, you can easily use this code in your project.
 *
 * The overall flow of the registration (new account creation) is this:
 *
 * The user can create a new account in the system by posting to the login/register endpoint. The user
 * will post to the register endpoint with the desired username/password in the HTTP headers. It's important
 * to not post a user object containing usernames/passwords because many server-side logging systems store
 * the POST bodies of requests, meaning that as users sign up, their username/passwords are in plain text
 * in the server log files! (Even if encrypted in the database)
 *
 * The server will then return a User object containing a JSON web token. The JWT will be used in the http headers for
 * all of the requests to the TodoListWebService. The server will know what to do with the JWT to uniquely
 * identify that the request came from the user.
 *
 * For login, after the account has been created on the server, a similar flow happens.
 *
 */
interface LoginWebService {


    @POST("login/register")
    fun registerNewUser(@Body user: User) : Call<User>

    //LoginActivity for Logging in. Sends a request with username/pass in headers to login endpoint,
//gets back JWT that is to be sent on future requests.

    @PUT("login/")
    fun loginExistingUser(@Body user: User) : Call<User>

}