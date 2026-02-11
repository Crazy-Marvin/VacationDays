package rocks.poopjournal.vacationdays.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@HiltWorker
class VacationNotificationWorker @AssistedInject constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        private val vacationRepository: VacationRepository
) : CoroutineWorker(context, params) {


    private val prefs = context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)
    private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")

    override suspend fun doWork(): Result {
        if (!prefs.getBoolean("is_vacation_notification_enabled", false)) return Result.success()

        val vacations = vacationRepository.getAllData().firstOrNull() ?: emptyList()

        // get from repository
        val now = LocalDate.now()
        val vacationDates = vacations
            .filter { it.category == "Vacation" }
            .mapNotNull { runCatching { LocalDate.parse(it.startDate, formatter) }.getOrNull() }

        val pastVacations = vacationDates.filter { it.isBefore(now) }
        val futureVacations = vacationDates.filter { !it.isBefore(now) }

        val message = when {
            vacationDates.isEmpty() ->
                applicationContext.getString(R.string.vacation_none)

            futureVacations.minOrNull() != null && pastVacations.maxOrNull() == null -> {
                val days = ChronoUnit.DAYS.between(now, futureVacations.minOrNull())
                applicationContext.getString(
                    R.string.vacation_first_upcoming,
                    days
                )
            }

            futureVacations.minOrNull() == null && pastVacations.maxOrNull() != null -> {
                val days = ChronoUnit.DAYS.between(pastVacations.maxOrNull(), now)
                applicationContext.getString(
                    R.string.vacation_last_ended,
                    days
                )
            }

            else -> {
                val pastDays = ChronoUnit.DAYS.between(pastVacations.maxOrNull(), now)
                val futureDays = ChronoUnit.DAYS.between(now, futureVacations.minOrNull())
                applicationContext.getString(
                    R.string.vacation_last_and_next,
                    pastDays,
                    futureDays
                )
            }
        }

        showNotification(message)
        return Result.success()
    }


    private fun showNotification(message: String) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "vacation_channel"
        val channel = NotificationChannel(
            channelId,
            "Vacation Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(
                applicationContext.getString(R.string.vacation_notification_title)
            )
                .setContentText(message)
                .setSmallIcon(R.drawable.umbrella)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()

        manager.notify(1001, notification)
    }
}


