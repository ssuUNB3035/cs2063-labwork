package mobiledev.unb.ca.threadinglab

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * An activity representing a list of GeoData. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [GeoDataDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class GeoDataListActivity : AppCompatActivity() {
    // Indicator if the activity is in two-pane mode (for example: running on a tablet)
    var isTwoPane = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager = supportFragmentManager
        setContentView(R.layout.activity_geodata_list)

        // Test if we're on a tablet
        if (findViewById<View?>(R.id.geodata_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true

            // Create a new detail fragment if one does not exist
            var geoDataDetailFragment =
                fragmentManager.findFragmentByTag("Detail") as GeoDataDetailFragment?
            if (geoDataDetailFragment == null) {
                // Init new detail fragment
                geoDataDetailFragment = GeoDataDetailFragment()
                val args = Bundle()
                geoDataDetailFragment.arguments = args
                fragmentManager.beginTransaction()
                    .replace(R.id.geodata_detail_container, geoDataDetailFragment, "Detail")
                    .commit()
            }
        }
        val mBgButton = findViewById<Button>(R.id.button)
        mBgButton.setOnClickListener {
            // Update the geo data
            downloadGeoData()
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
            Snackbar.make(
                view!!, "I'm working!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun downloadGeoData() {
        // TODO
        //  1. Using the isNetworkAvailable method below check whether there is a network connection
        //  2. If there is create an instance of DownLoaderTask using this activity in the constructor
        //  and use the setters to pass in the objects needed during execution
        //  3. If there isn't a connection create a Toast message indicating that there is no network connection.
        //  HINT 1:
        //    Read this for help on checking network connectivity:
        //    https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
        //  HINT 2: 
        //    Read this for help with Toast:
        //    http://developer.android.com/guide/topics/ui/notifiers/toasts.html
        // DONE
        if (isNetworkAvailable(applicationContext)) {
            val downloaderTask = DownloaderTask(this)
            downloaderTask.setRefreshButton(findViewById(R.id.button))
            downloaderTask.setProgressBar(findViewById(R.id.progressBar))
            downloaderTask.setRecyclerView(findViewById(R.id.geodata_list))
            downloaderTask.execute()
        } else {
            Toast.makeText(applicationContext, "Cannot connect", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnectedOrConnecting
        }

        return false
    }
}