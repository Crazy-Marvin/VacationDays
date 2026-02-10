package rocks.poopjournal.vacationdays.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            val prefs = context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)
            if (prefs.getBoolean("is_vacation_notification_enabled", false)) {
                scheduleVacationWorker(context)
            }
        }
    }

    private fun scheduleVacationWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<VacationNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "vacation_notifications",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}

