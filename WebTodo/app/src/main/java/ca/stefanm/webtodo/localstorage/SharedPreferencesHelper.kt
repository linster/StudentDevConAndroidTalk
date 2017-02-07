package ca.stefanm.webtodo.localstorage

import android.content.Context
import com.squareup.moshi.Moshi


/**
 * Store and retrieve (small) arbitrary objects from the Shared Preferences. This class is very
 * handy for loading and storing data locally on the device. Larger data should be stored in files
 * that are application-private, however.
 *
 * https://developer.android.com/training/basics/data-storage/shared-preferences.html
 *
 * From the Android Docs:
 *
 * If you have a relatively small collection of key-values that you'd like to save,
 * you should use the SharedPreferences APIs. A SharedPreferences object points to a
 * file containing key-value pairs and provides simple methods to read and write them.
 * Each SharedPreferences file is managed by the framework and can be private or shared.
 */
object SharedPreferencesHelper {
    private val sharedPreferenceFile : String = "shared_preferences"

    fun setObject(context : Context, item : Any, key : String) : Unit {

        val sharedPref = context.applicationContext.getSharedPreferences(sharedPreferenceFile,
                Context.MODE_PRIVATE)

        var sharedPrefEditor = sharedPref.edit()

        when (item) {
            is String   -> sharedPrefEditor.putString(key, item)
            is Long     -> sharedPrefEditor.putLong(key, item)
            is Boolean  -> sharedPrefEditor.putBoolean(key, item)
            is Float    -> sharedPrefEditor.putFloat(key, item)
            else -> {
                val moshi = Moshi.Builder().build()
                sharedPrefEditor.putString(key,
                        moshi.adapter(item.javaClass).toJson(item))
            }
        }
    }


    fun getObject(context: Context, key : String, type : Any) : Any {
        val sharedPreferences = context.getSharedPreferences(
                sharedPreferenceFile, Context.MODE_PRIVATE)

        when (type){
            is Long     -> return sharedPreferences.getLong(key, 0L)
            is String   -> return sharedPreferences.getString(key, "")
            is Boolean  -> return sharedPreferences.getBoolean(key, false)
            is Float    -> return sharedPreferences.getFloat(key, 0F)
            else -> {
                val moshi = Moshi.Builder().build()
                return moshi.adapter(type.javaClass).fromJson(sharedPreferences.getString(key, ""))
            }
        }
    }
}