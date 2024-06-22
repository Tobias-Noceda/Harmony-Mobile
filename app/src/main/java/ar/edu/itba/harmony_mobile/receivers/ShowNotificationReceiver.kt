package ar.edu.itba.harmony_mobile.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ar.edu.itba.harmony_mobile.ApiApplication
import ar.edu.itba.harmony_mobile.MainActivity
import ar.edu.itba.harmony_mobile.MyIntent
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.remote.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.Locale

class ShowNotificationReceiver : BroadcastReceiver() {

    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    fun String.camelToNormalText(): String {
        return camelRegex.replace(this) { " ${it.value}" }
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MyIntent.SHOW_NOTIFICATION) {
            val deviceId: String? = intent.getStringExtra(MyIntent.DEVICE_ID)
            val eventName: String? = intent.getStringExtra(MyIntent.EVENT_NAME)
            Log.d(TAG, "Show notification intent received {$deviceId)")

            showNotification(context, deviceId!!, eventName!!)
        }
    }

    var deviceService = RetrofitClient.deviceService
    private fun showNotification(context: Context, deviceId: String, eventName: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MyIntent.DEVICE_ID, deviceId)
        }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        var device: Device? = null
        runBlocking {
            val tasks = listOf(
                async(Dispatchers.IO) {
                    device =
                        deviceService.getDevices().body()?.result?.find { it.id == deviceId }?.asModel()
                },
            )
            tasks.awaitAll()
        }

        val builder = NotificationCompat.Builder(context, ApiApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(
                try {
                    device?.name
                } catch (e: Exception) {
                    deviceId
                }
            )
            .setContentText(eventName.camelToNormalText())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            if (notificationManager.areNotificationsEnabled())
                notificationManager.notify(deviceId.hashCode(), builder.build())
        } catch (e: SecurityException) {
            Log.d(TAG, "Notification permission not granted $e")
        }
    }

    companion object {
        private const val TAG = "ShowNotificationReceiver"
    }
}