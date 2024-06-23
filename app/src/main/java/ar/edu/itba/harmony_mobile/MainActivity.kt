package ar.edu.itba.harmony_mobile

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import ar.edu.itba.harmony_mobile.receivers.SkipNotificationReceiver
import ar.edu.itba.harmony_mobile.ui.theme.HarmonyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    private lateinit var receiver: SkipNotificationReceiver

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HarmonyTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionState =
                        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                    if (!permissionState.status.isGranted) {
                        LaunchedEffect(true) {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }

                val deviceId = intent?.getStringExtra(MyIntent.DEVICE_ID)

                HarmonyApp(deviceId) { id ->
                    showingDeviceId = id
                    Log.i("Tobi", "New id: $id")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        receiver = SkipNotificationReceiver(showingDeviceId)
        IntentFilter(MyIntent.SHOW_NOTIFICATION)
            .apply { priority = 1 }
            .also {
                var flags = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    flags = Context.RECEIVER_NOT_EXPORTED

                registerReceiver(receiver, it, flags)
            }
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(receiver)
    }

    companion object{
        var showingDeviceId = ""
    }
}
