package rocks.poopjournal.vacationdays.presentation.widgets.takenPlannedUntilVacation

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TakenPlannedUntilVacationReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TakenPlannedUntilVacationProvider()
}