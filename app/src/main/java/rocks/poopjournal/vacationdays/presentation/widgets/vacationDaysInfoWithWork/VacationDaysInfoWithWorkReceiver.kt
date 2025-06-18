package rocks.poopjournal.vacationdays.presentation.widgets.vacationDaysInfoWithWork

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import rocks.poopjournal.vacationdays.presentation.widgets.vacationInfo.VacationInfoProvider

class VacationDaysInfoWithWorkReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = VacationDaysInfoWithWorkProvider()
}