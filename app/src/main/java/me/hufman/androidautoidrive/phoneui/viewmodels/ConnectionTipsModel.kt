package me.hufman.androidautoidrive.phoneui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.hufman.androidautoidrive.R
import me.hufman.androidautoidrive.connections.CarConnectionDebugging

class ConnectionTip(text: Context.() -> String, drawable: Context.() -> Drawable?, val condition: CarConnectionDebugging.() -> Boolean): Tip(text, drawable)

class ConnectionTipsModel(val connection: CarConnectionDebugging): TipsModel() {
	class Factory(val appContext: Context) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			val connection = CarConnectionDebugging(appContext) {
				// no need to call model.update()
				// we only update the Tips List on a new screen draw, not as the connection changes
			}
			// can't unittest CarConnectionDebugging registration

			// only register to the device status specifically
			// don't need to register to BCL updates
			connection._registerBtStatus()
			connection._registerUsbStatus()
			val model = ConnectionTipsModel(connection)
			model.update()
			return model as T
		}
	}

	val TIPS = listOf(
		ConnectionTip({getString(R.string.tip_usb_chargingmode)}, {ContextCompat.getDrawable(this, R.drawable.tip_phone_chargemode)}) {
			isUsbConnected && !isUsbAccessoryConnected
		},
		ConnectionTip({getString(R.string.tip_connassistant)}, {ContextCompat.getDrawable(this, R.drawable.tip_connassistant_bmw)}) {
			isBMWInstalled
		},
		ConnectionTip({getString(R.string.tip_connassistant)}, {ContextCompat.getDrawable(this, R.drawable.tip_connassistant_mini)}) {
			isMiniInstalled
		},
		ConnectionTip({getString(R.string.tip_btapp)}, {ContextCompat.getDrawable(this, R.drawable.tip_btapp_bmw)}) {
			val btApps = !isBTConnected || (isBTConnected && !isSPPAvailable)
			isBMWInstalled && btApps
		},
		ConnectionTip({getString(R.string.tip_btapp)}, {ContextCompat.getDrawable(this, R.drawable.tip_btapp_mini)}) {
			val btApps = !isBTConnected || (isBTConnected && !isSPPAvailable)
			isMiniInstalled && btApps
		},
		ConnectionTip({getString(R.string.tip_batterymode_mybmw)}, {ContextCompat.getDrawable(this, R.drawable.tip_batterymode_mybmw)}) {
			isBMWMineInstalled
		},
		ConnectionTip({getString(R.string.tip_batterymode_mymini)}, {ContextCompat.getDrawable(this, R.drawable.tip_batterymode_mymini)}) {
			isMiniMineInstalled
		}
	)

	override fun update() {
		currentTips.clear()
		currentTips.addAll(TIPS.filter { connection.run(it.condition) })
	}
}