package mobiledev.unb.ca.recyclerviewlab.util

import android.content.Context
import android.util.Log
import mobiledev.unb.ca.recyclerviewlab.model.Course
import org.json.JSONObject
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class JsonUtils(context: Context) {
    // TIP: lateinit is used to declare properties that
    // are guaranteed to be initialized in the future
    private lateinit var courses: ArrayList<Course>

    // Initializer (constructor) to read our data source (JSON file) into an array of course objects
    init {
        processJSON(context)
    }

    private fun processJSON(context: Context) {
        Log.i("course", "staaaaart")
        // Initialize the lateinit value
        courses = ArrayList()
        try {
            // Create a JSON Object from file contents String
            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

            // Create a JSON Array from the JSON Object
            // This array is the "courses" array mentioned in the lab write-up
            val jsonArray = jsonObject.getJSONArray(KEY_COURSES)
            for (i in 0 until jsonArray.length()) {
                // TODO 1:
                //  Using the JSON array update coursesArray
                //  1. Retrieve the current object by index
                //  2. Add new Course to courses ArrayList
                var course: Course = Course.Builder()
                    .name(jsonArray.getJSONObject(i).get("name").toString())
                    .id(jsonArray.getJSONObject(i).get("courseID").toString())
                    .description(jsonArray.getJSONObject(i).get("description").toString())
                    .build()
                courses.add(course)
                Log.i("course", "blaaaaaaa")
            }
        } catch (e: JSONException) {
            Log.i("course", "darn")
            e.printStackTrace()
        }
    }

    private fun loadJSONFromAssets(context: Context): String? {
        Log.i("json", "helloooooooooooo 1")
        // TODO 2:
        //  1. Obtain an instance of the AssetManager class from the referenced context
        //    (https://developer.android.com/reference/android/content/Context#getAssets())
        //  2. Open the CS_JSON_FILE from the assets folder
        //     (https://developer.android.com/reference/android/content/res/AssetManager)
        //  3. Process the file using an InputStream
        //  HINT:
        //   See step 4 here
        //   (https://www.tutorialspoint.com/how-to-read-files-from-assets-on-android-using-kotlin)
        //   for an example on how to read a file from the assets folder
        val assetManager = context.getAssets();

        try {
            val inputStream: InputStream = assetManager.open(CS_JSON_FILE)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            return String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    // Getter method for courses ArrayList
    fun getCourses(): ArrayList<Course> {
        return courses
    }

    companion object {
        private const val CS_JSON_FILE = "CS.json"
        private const val KEY_COURSES = "courses"
        private const val KEY_COURSE_ID = "courseID"
        private const val KEY_NAME = "name"
        private const val KEY_DESCRIPTION = "description"
    }
}