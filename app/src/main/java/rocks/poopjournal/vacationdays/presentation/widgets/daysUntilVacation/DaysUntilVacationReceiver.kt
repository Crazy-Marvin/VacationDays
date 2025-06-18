package rocks.poopjournal.vacationdays.presentation.widgets.daysUntilVacation

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class DaysUntilVacationReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DaysUntilVacationProvider()
}