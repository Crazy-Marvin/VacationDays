package rocks.poopjournal.vacationdays.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class VacationNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("is_vacation_notification_enabled", false)
        if (isEnabled) {
            val serviceIntent = Intent(context, VacationNotificationService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}