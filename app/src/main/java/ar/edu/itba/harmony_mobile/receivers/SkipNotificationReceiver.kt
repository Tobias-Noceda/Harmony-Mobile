package ar.edu.itba.harmony_mobile.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ar.edu.itba.harmony_mobile.MainActivity
import ar.edu.itba.harmony_mobile.MyIntent

class SkipNotificationReceiver(private val deviceId: String) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(MyIntent.SHOW_NOTIFICATION) &&
            (intent.getStringExtra(MyIntent.DEVICE_ID)
                .equals(MainActivity.showingDeviceId) && MainActivity.showingDeviceId != "")
        ) {
            Log.d(TAG, "Skipping notification send (${MainActivity.showingDeviceId})")
            abortBroadcast()
        }
    }

    companion object {
        private const val TAG = "SkipNotificationReceiver"
    }
}