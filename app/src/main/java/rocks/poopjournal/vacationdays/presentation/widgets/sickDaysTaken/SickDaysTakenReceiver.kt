package rocks.poopjournal.vacationdays.presentation.widgets.sickDaysTaken

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SickDaysTakenReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = SickDaysTakenProvider()
}