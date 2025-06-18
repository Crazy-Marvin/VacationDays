package rocks.poopjournal.vacationdays.presentation.widgets.takenPlannedVacationSick

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TakenPlannedVacationSickReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TakenPlannedVacationSickProvider()
}