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

    fun <T> performBlockingNetworkCall(tag: String, method : Call<T>) : Pair<Response<T>?, Boolean> {
        try {

            val response = method.execute()

            if (response != null && response.isSuccessful){
                return Pair(response, true)
            } else {
                Log.d(tag, "Response was not successful. Response: ${response.toString()}")
                return Pair(response, false)
            }
        } catch (e : IOException){
            Log.d(tag, "Error talking to server. No network?")
            return Pair(null, false)
        } catch (e :RuntimeException){
            Log.d(tag, "Error preparing request/response. \n ${e.message}")
            return Pair(null, false)
        }

    }

}