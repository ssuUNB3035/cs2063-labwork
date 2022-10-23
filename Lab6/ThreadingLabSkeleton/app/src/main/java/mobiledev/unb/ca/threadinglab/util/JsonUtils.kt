package mobiledev.unb.ca.threadinglab.util

import android.util.Log
import mobiledev.unb.ca.threadinglab.model.GeoData
import javax.json.stream.JsonParser
import javax.json.Json
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class JsonUtils {
    // Getter method for geoDataArray
    private lateinit var geoDataArray: ArrayList<GeoData>

    private fun processJSON() {
        // Initialize the data array
        geoDataArray = ArrayList()

        // Process the JSON response from the URL
        val jsonString = loadJSONFromURL()
        try {
            val parser = Json.createParser(StringReader(jsonString))
            var titleTrigger = false
            var coordinateTrigger = false
            var count = 0
            var coordinateCount = 0

            while (parser.hasNext()) {
                when (parser.next()) {
                    JsonParser.Event.KEY_NAME -> if (parser.string == JSON_KEY_TITLE) {
                        titleTrigger = true
                    } else if (parser.string == JSON_KEY_COORDINATES) {
                        coordinateTrigger = true
                    }
                    JsonParser.Event.VALUE_STRING -> if (titleTrigger && parser.string.startsWith("M")) {
                        val geoData = GeoData()
                        geoData.title = parser.string
                        geoDataArray.add(geoData)
                        titleTrigger = false
                    }
                    JsonParser.Event.VALUE_NUMBER -> {
                        if (coordinateTrigger && coordinateCount == 0) {
                            val geoData = geoDataArray[count]
                            geoData.longitude = parser.string
                            coordinateCount++
                        } else if (!coordinateTrigger && coordinateCount == 1) {
                            val geoData = geoDataArray[count]
                            geoData.latitude = parser.string
                            coordinateCount = 0
                            count++
                        }
                        coordinateTrigger = false
                    }
                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getGeoData(): ArrayList<GeoData> {
        return geoDataArray
    }

    private fun loadJSONFromURL(): String? {
        val url = URL(REQUEST_URL)
        var connection: HttpURLConnection? = null
        try {
           // TODO
            //  Establish an HttpURLConnection to REQUEST_URL (defined as a constant)
            //  Hint: 
            //    See https://github.com/hpowell20/cs2063-fall-2022-examples/tree/master/Lecture6/NetworkingURL
            //    for an example of how to do this
            //    Also see documentation here: http://developer.android.com/training/basics/network-ops/connecting.html
            // DONE
            connection = url.openConnection() as HttpURLConnection?
            val streamIn = BufferedInputStream(connection?.getInputStream())
            return convertStreamToString(streamIn)
        } catch (exception: MalformedURLException) {
            Log.e(TAG, "MalformedURLException")
            return null
        } catch (exception: IOException) {
            Log.e(TAG, "IOException")
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            connection?.disconnect()
        }
    }

    private fun convertStreamToString(`in`: InputStream): String {
        val data = StringBuilder()
        try {
            BufferedReader(InputStreamReader(`in`)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    data.append(line)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "IOException")
        }
        return data.toString()
    }

    // Initializer to read our data source (JSON file) into an array of course objects
    init {
        processJSON()
    }

    companion object {
        private const val TAG = "JsonUtils"
        private const val REQUEST_URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson"
        private const val JSON_KEY_TITLE = "title"
        private const val JSON_KEY_COORDINATES = "coordinates"
    }
}