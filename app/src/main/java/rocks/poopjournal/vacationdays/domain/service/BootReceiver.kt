package rocks.poopjournal.vacationdays.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            val prefs = context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)
            val isEnabled = prefs.getBoolean("is_vacation_notification_enabled", false)
            if (isEnabled) {
                // Start service immediately
                val serviceIntent = Intent(context, VacationNotificationService::class.java)
                ContextCompat.startForegroundService(context, serviceIntent)

                // Also reschedule alarm
                scheduleRepeatingAlarm(context)
            }
        }
    }

    private fun scheduleRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, VacationNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 1000,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}

