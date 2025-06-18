package rocks.poopjournal.vacationdays.presentation.widgets.takenAndAvailable

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TakenAndAvailableReciever : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TakenAndAvailableProvider()
}