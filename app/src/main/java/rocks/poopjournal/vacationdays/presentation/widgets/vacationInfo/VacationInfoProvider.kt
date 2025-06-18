package rocks.poopjournal.vacationdays.presentation.widgets.vacationInfo

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.presentation.widgets.AppWidgetViewModel
import rocks.poopjournal.vacationdays.presentation.widgets.WidgetTheme

class VacationInfoProvider : GlanceAppWidget() {
    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val viewModel =
            EntryPoints.get(
                appContext,
                AppWidgetEntryPoint::class.java,
            ).getViewModel()

        provideContent {
            val vacationDaysTaken = viewModel.vacationDaysTaken.collectAsState()
            val vacationDaysPlanned = viewModel.vacationDaysPlanned.collectAsState()
            val sickDaysTaken = viewModel.sickStats.collectAsState()
            val daysUntilVacation = viewModel.countdownToNextVacation.collectAsState()
            WidgetTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ImageProvider(R.drawable.app_widget_background))
                        .appWidgetBackground()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.umbrella),
                            contentDescription = "Umbrella Icon",
                            modifier = GlanceModifier.size(24.dp),
                        )
                        Spacer(GlanceModifier.width(5.dp))
                        Text(
                           context.getString(R.string.app_name), style = TextStyle(
                                color = GlanceTheme.colors.onBackground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Column(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Text(
                                context.getString(R.string.vacation_days_taken),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSecondary,
                                    fontSize = 15.sp,
                                ),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                vacationDaysTaken.value?.toString() ?: "0", style = TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = GlanceModifier.wrapContentWidth()
                            )
                        }

                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Text(
                                context.getString(R.string.vacation_days_planned),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSecondary,
                                    fontSize = 15.sp,
                                ),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                vacationDaysPlanned.value?.toString() ?: "0", style = TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = GlanceModifier.wrapContentWidth()
                            )
                        }
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Text(
                                context.getString(R.string.sick_days_taken),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSecondary,
                                    fontSize = 15.sp,
                                ),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                sickDaysTaken.value?.toString() ?: "0", style = TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = GlanceModifier.wrapContentWidth()
                            )
                        }
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Text(
                                context.getString(R.string.daysUntilVacation),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSecondary,
                                    fontSize = 15.sp,
                                ),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                daysUntilVacation.value?.toString() ?: "0", style = TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = GlanceModifier.wrapContentWidth()
                            )
                        }
                    }
                }
            }
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppWidgetEntryPoint {
        fun getViewModel(): AppWidgetViewModel
    }
}