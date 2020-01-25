@file:Suppress("MoveLambdaOutsideParentheses")

package me.hufman.androidautoidrive.phoneui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import me.hufman.androidautoidrive.R
import java.lang.IllegalStateException

class SpotifyDowngradeDialog: DialogFragment() {
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			AlertDialog.Builder(it)
					.setMessage(R.string.musicAppNotes_oldSpotifyInstructions)
					.setPositiveButton(R.string.lbl_ok, { _, _ ->
						val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apkhere.com/down/com.spotify.music_8.4.96.953_free"))
						startActivity(intent)
					})
					.setNegativeButton(R.string.lbl_cancel, { _, _ ->
					})
					.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}
}