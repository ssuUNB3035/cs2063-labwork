package mobiledev.unb.ca.roompersistencelab.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import mobiledev.unb.ca.roompersistencelab.R
import android.widget.TextView
import mobiledev.unb.ca.roompersistencelab.entity.Item

class ItemsAdapter(context: Context, items: List<Item>) : ArrayAdapter<Item>(
    context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val item = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        var currView = convertView
        if (currView == null) {
            currView = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false)
        }

        // Lookup view for data population
        val tvName = currView!!.findViewById<TextView>(R.id.item_textview)
        val tvNum = currView.findViewById<TextView>(R.id.num_textview)

        // TODO
        //  Set the text used by tvName and tvNum using the data object
        //  This will need to updated once the entity model has been updated
        tvName.text = item?.itemName
        tvNum.text = item?.itemNumber.toString()


        // Return the completed view to render on screen
        return currView
    }
}