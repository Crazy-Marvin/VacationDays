package rocks.poopjournal.vacationdays.presentation.widgets.vacationInfo

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class VacationInfoReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = VacationInfoProvider()
}