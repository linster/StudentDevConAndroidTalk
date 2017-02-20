package ca.stefanm.webtodo.webservice

import android.content.Context
import android.util.Log
import ca.stefanm.webtodo.R
import ca.stefanm.webtodo.localstorage.Session
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * This is a singleton that gives us an HTTP Client + JSON parser as discussed in the comments
 * in TodoListWebService.
 *
 * The getClient() method returns an object that implements the TodoListWebService interface. The
 * object returned, has implementations for the methods that do the web request, populating the body,
 * trapping exceptions, performing the request, and parsing the response.
 */

object TodoListWebServiceClient : IWebServiceClient<TodoListWebService>{

    override fun getClient(context: Context) : TodoListWebService {
        var retrofit = Retrofit.Builder()
                .baseUrl(context.applicationContext.getString(R.string.api_url))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(
                        OkHttpClient.Builder().addInterceptor({
                            chain : Interceptor.Chain ->
                                val request = chain.request()
                                val newReq = request.newBuilder()
                                        /* Add the JWT to the request header */
                                        .addHeader("Authorization ",
                                                "Basic " + Session(context).currentUser.authToken ?: "Og==" /* base64encode(":") */)
                                        .build()
                                val response = chain.proceed(newReq)
                                response //Implicit return line from lambda.
                    })
                    /* https://github.com/mrmike/Ok2Curl */
                    .addInterceptor(CurlInterceptor(object : Loggable {
                        override fun log(message: String) {
                            Log.v("Ok2Curl", message)
                        }
                    }))
                    .build()
                )
                .build()

        return retrofit.create(TodoListWebService::class.java)
    }

}


