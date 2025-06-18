package rocks.poopjournal.vacationdays.presentation.widgets.daysSinceVacationAndSick

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class DaysSinceVacationAndSickReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DaysSinceVacationAndSickProvider()
}