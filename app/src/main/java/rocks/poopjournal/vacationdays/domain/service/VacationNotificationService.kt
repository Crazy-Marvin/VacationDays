package rocks.poopjournal.vacationdays.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class VacationNotificationService : Service() {

    @Inject
    lateinit var vacationRepository: VacationRepository

    companion object {
        private const val CHANNEL_ID = "vacation_notification_channel"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_NAME = "Vacation Notifications"
        private const val DATE_PATTERN = "d/MM/yyyy"
    }

    private val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification("Loading Vacation Info..."))
            updateNotification() // initial update
        return START_STICKY
    }

    private fun buildNotification(message: String): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Upcoming Vacation")
            .setContentText(message)
            .setSmallIcon(R.drawable.umbrella)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setAutoCancel(false)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun updateNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            val message = getVacationMessage()

            val notification = buildNotification(message)
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        }
    }

    private suspend fun getVacationMessage(): String {
        val vacations = vacationRepository.getAllData().firstOrNull() ?: return "No vacation data available."
        val now = LocalDate.now()

        val vacationDates = vacations
            .filter { it.category == "Vacation" }
            .mapNotNull { runCatching { LocalDate.parse(it.startDate, formatter) }.getOrNull() }

        val pastVacations = vacationDates.filter { it.isBefore(now) }
        val futureVacations = vacationDates.filter { !it.isBefore(now) }

        val last = pastVacations.maxOrNull()
        val next = futureVacations.minOrNull()

        return when {
            vacationDates.isEmpty() ->
                "You have no planned vacations! 😱"
            next != null && last == null ->
                "Your first vacation will be in ${ChronoUnit.DAYS.between(now, next)} days. Enjoy! 🏖️"
            next == null && last != null ->
                "Your last vacation ended ${ChronoUnit.DAYS.between(last, now)} days ago and you did not plan a new one yet! 🙈"
            next != null && last != null ->
                "Your last vacation ended ${ChronoUnit.DAYS.between(last, now)} days ago and your next vacation will be in ${ChronoUnit.DAYS.between(now, next)} days. Enjoy! 🏖️"
            else ->
                "Stay balanced, plan your break! 🧘‍♂️"
        }
    }


    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            if (manager?.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Notifications about your upcoming vacations."
                }
                manager?.createNotificationChannel(channel)
            }
        }
    }
}


