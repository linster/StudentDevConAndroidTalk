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

            //You may want to uncomment this for debugging to get a full stack trace. In the
            //real world, it's typically bad to have the app crash due to a failed web request.
            // (what if the user just didn't have data that instant?).
            //
            //throw e

            return Pair(null, false)
        } catch (e : RuntimeException){
            Log.e(tag, e.toString())
            Log.d(tag, "Error preparing request/response. \n Exception message: ${e.message} \n")

            //You may want to uncomment this for debugging to get a full stack trace. In the
            //real world, it's typically bad to have the app crash due to a failed web request.
            // (what if the user just didn't have data that instant?).
            //
            //throw e;

            return Pair(null, false)
        }

    }

}