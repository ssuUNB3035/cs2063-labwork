package mobiledev.unb.ca.threadinglab.model

/**
 * This makes use of the data class pattern (https://kotlinlang.org/docs/data-classes.html)
 * NOTES:
 *  Each value has a getter and setter
 */
data class GeoData(var title: String? = null,
                   var longitude: String? = null,
                   var latitude: String? = null)
