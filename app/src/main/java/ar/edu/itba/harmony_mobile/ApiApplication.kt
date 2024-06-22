package ar.edu.itba.harmony_mobile

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationManagerCompat
import ar.edu.itba.harmony_mobile.receivers.ServerEventReceiver
import ar.edu.itba.harmony_mobile.remote.DeviceRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.HomeRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.RoomRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.RoutineRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.api.RetrofitClient
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.repository.HomeRepository
import ar.edu.itba.harmony_mobile.repository.RoomRepository
import ar.edu.itba.harmony_mobile.repository.RoutineRepository

class ApiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        collectServerEvents()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "smth"
            val descriptionText = "smth else"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            with(NotificationManagerCompat.from(this)) {
                createNotificationChannel(channel)
            }
        }
    }

    private fun collectServerEvents() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ServerEventReceiver::class.java)

        var pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if(pendingIntent != null){
            alarmManager.cancel(pendingIntent)
        }

        pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            SystemClock.currentThreadTimeMillis(),
            INTERVAL,
            pendingIntent
        )
    }

    private val roomRemoteDataSource: RoomRemoteDataSource
        get() = RoomRemoteDataSource(RetrofitClient.roomService)

    private val deviceRemoteDataSource: DeviceRemoteDataSource
        get() = DeviceRemoteDataSource(RetrofitClient.deviceService)

    private val homeRemoteDataSource: HomeRemoteDataSource
        get() = HomeRemoteDataSource(RetrofitClient.homeService)

    private val routineRemoteDataSource: RoutineRemoteDataSource
        get() = RoutineRemoteDataSource(RetrofitClient.routineService)

    val roomRepository: RoomRepository
        get() = RoomRepository(roomRemoteDataSource)

    val deviceRepository: DeviceRepository
        get() = DeviceRepository(deviceRemoteDataSource)

    val homeRepository: HomeRepository
        get() = HomeRepository(homeRemoteDataSource)

    val routineRepository: RoutineRepository
        get() = RoutineRepository(routineRemoteDataSource)


    companion object {
        const val CHANNEL_ID = "device"
        const val INTERVAL: Long = 1000 * 5
    }
}