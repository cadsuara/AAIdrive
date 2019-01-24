package me.hufman.androidautoidrive.carapp.maps

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import java.io.Serializable


data class LatLong(val latitude: Double, val longitude: Double): Serializable
data class MapResult(val id: String, val name: String,
                val address: String? = null,
                val location: LatLong? = null): Serializable {
	override fun toString(): String {
		return name
	}
}

const val INTENT_MAP_RESULT = "me.hufman.androidautoidrive.maps.INTENT_RESULT"
const val INTENT_MAP_RESULTS = "me.hufman.androidautoidrive.maps.INTENT_RESULTS"
const val EXTRA_MAP_RESULT = "me.hufman.androidautoidrive.maps.EXTRA_RESULT"
const val EXTRA_MAP_RESULTS = "me.hufman.androidautoidrive.maps.EXTRA_RESULTS"
interface MapResultsController {
	/** Initial search results */
	fun onSearchResults(results: Array<MapResult>)
	/** Further details about the result */
	fun onPlaceResult(result: MapResult)
}

class MapResultsSender(val context: Context): MapResultsController {
	override fun onSearchResults(results: Array<MapResult>) {
		val intent = Intent(INTENT_MAP_RESULTS).putExtra(EXTRA_MAP_RESULTS, results)
				.setPackage(context.packageName)
		context.sendBroadcast(intent)
	}

	override fun onPlaceResult(result: MapResult) {
		val intent = Intent(INTENT_MAP_RESULT).putExtra(EXTRA_MAP_RESULT, result)
				.setPackage(context.packageName)
		context.sendBroadcast(intent)
	}
}