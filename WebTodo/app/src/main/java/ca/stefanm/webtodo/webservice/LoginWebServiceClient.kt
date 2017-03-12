package ca.stefanm.webtodo.webservice

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import ca.stefanm.webtodo.R
import ca.stefanm.webtodo.localstorage.Session
import ca.stefanm.webtodo.models.User
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.Charset

/**
 * Created by Stefan on 2/11/2017.
 */

object LoginWebServiceClient : IWebServiceClient<LoginWebService> {

    val TAG = "LoginWebServiceClient"

    //Need to manipulate the headers and session appropriately.
    //OkHTTP interceptors.

    //These should be blocking calls so I can demo AsyncTasks.
    public fun registerNewUser(context: Context, user: User, desiredPassword : String) : Boolean{

        val (body, success) = performBlockingNetworkCall(TAG, getClient(context, userpassTob64Header(user.username, desiredPassword)).registerNewUser(user) )

        if (success && body != null){
            val returnedUser = body

            if (returnedUser != null){
                Session(context).currentUser = returnedUser
                return true
            } else {
                return false
            }
        } else {
            Log.d(TAG, "User registration failed: Response:" + body?.toString())
            return false
        }
    }

    public fun loginExistingUser(context: Context, user: User, enteredPassword : String) : Boolean{

        val (body, success) = performBlockingNetworkCall(TAG, getClient(context, userpassTob64Header(user.username, enteredPassword)).loginExistingUser(user) )

        if (success && body != null){
            val returnedUser = body

            if (returnedUser != null){
                Session(context).currentUser = returnedUser
                return true
            } else {
                return false
            }
        } else {
            Log.d(TAG, "User login failed: Response:" + body?.toString())
            return false
        }

    }


    private fun userpassTob64Header(username : String, password : String) : String{
        return Base64.encodeToString(
                (username + ":" + password).toByteArray(Charset.defaultCharset()),
                Base64.NO_WRAP
        )
    }


    override fun getClient(context: Context): LoginWebService {
        throw NotImplementedError()
    }

    //Private methods to get the client
    fun getClient(context: Context, basicAuthHeader : String) : LoginWebService {
        var retrofit = Retrofit.Builder()
                .baseUrl(context.applicationContext.getString(R.string.api_url))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(
                        OkHttpClient.Builder().addInterceptor({
                            chain : Interceptor.Chain ->
                            val request = chain.request()
                            val newReq = request.newBuilder()
                                    /* Add the JWT to the request header */
                                    .addHeader("Authorization",
                                            "Basic " + basicAuthHeader)
                                    .build()
                            val response = chain.proceed(newReq)
                            response //Implicit return line from lambda.
                        })
                        /* https://github.com/mrmike/Ok2Curl */
                        .addInterceptor(CurlInterceptor(Loggable { message -> Log.v("Ok2Curl", message) }))
                        .build()
                )
                .build()

        return retrofit.create(LoginWebService::class.java)
    }
}