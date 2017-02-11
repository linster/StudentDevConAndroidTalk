package ca.stefanm.webtodo.webservice

import android.content.Context
import ca.stefanm.webtodo.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * This is a singleton that gives us an HTTP Client + JSON parser as discussed in the comments
 * in TodoListWebService.
 *
 * The getClient() method returns an object that implements the TodoListWebService interface. The
 * object returned, has implementations for the methods that do the web request, populating the body,
 * trapping exceptions, performing the request, and parsing the response.
 */

object TodoListWebServiceClient{

    fun getClient(context: Context) : TodoListWebService {
        var retrofit = Retrofit.Builder()
                .baseUrl(context.applicationContext.getString(R.string.api_url))
                .client(OkHttpClient())
                .build()

        //TODO JWT?! in headers

        return retrofit.create(TodoListWebService::class.java)
    }

}


