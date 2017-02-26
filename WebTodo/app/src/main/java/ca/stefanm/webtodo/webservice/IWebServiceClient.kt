package ca.stefanm.webtodo.webservice

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

/**
 * Created by Stefan on 2/19/2017.
 */
interface IWebServiceClient<T> {

    fun getClient(context: Context) : T

    fun <T> performBlockingNetworkCall(tag: String, method : Call<T>) : Pair<T?, Boolean> {

        try {

            val response : Response<T> = method.execute()

            return Pair(response.body(), response.isSuccessful)
        } catch (e : IOException){
            Log.d(tag, "Error talking to server. Exception message: ${e} \t ${e.message}")
            throw e
            return Pair(null, false)
        } catch (e : RuntimeException){
            Log.e(tag, e.toString())
            Log.d(tag, "Error preparing request/response. \n Exception message: ${e.message} \n")
            throw e;
            return Pair(null, false)
        }

    }

}