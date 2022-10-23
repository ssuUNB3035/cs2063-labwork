package mobiledev.unb.ca.threadinglab

import android.os.Bundle
import android.app.Activity
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * A fragment representing a single Course detail screen.
 * This fragment is either contained in a [GeoDataListActivity]
 * in two-pane mode (on tablets) or a [GeoDataDetailActivity]
 * on handsets.
 */
class GeoDataDetailFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : Fragment() {
    /**
     * The dummy content this fragment is presenting.
     */
    private var title: String? = null
    private var lng: String? = null
    private var lat: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null && arguments.containsKey(LNG)) {
            title = arguments.getString(TITLE)
            lng = arguments.getString(LNG)
            lat = arguments.getString(LAT)
            val activity: Activity? = this.activity

            if (activity != null) {
                val appBarLayout: CollapsingToolbarLayout = activity.findViewById(R.id.toolbar_layout)
                appBarLayout.title = getString(R.string.earthquake_details)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.geodata_detail, container, false)

        // Show the dummy content as text in a TextView.
        if (lat != null && lng != null) {
            val text = getString(R.string.detail_fragment_content, title, lng, lat)
            (rootView.findViewById<View>(R.id.geodata_detail) as TextView).text = text
        }
        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val TITLE = "title"
        const val LNG = "item_longitude"
        const val LAT = "item_latitude"
    }
}