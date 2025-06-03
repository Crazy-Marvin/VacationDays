package rocks.poopjournal.vacationdays.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropDown(
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    var elementHeight by remember { mutableIntStateOf(0) }

    LaunchedEffect(expanded) {
        if (expanded) {
            val selectedIndex = years.indexOf(selectedYear)
            scrollState.scrollTo(selectedIndex * elementHeight)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopEnd // Aligns the dropdown to the top-end
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .width(150.dp)
                    .clickable { expanded = true }
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(10.dp), // Padding for better spacing
                contentAlignment = Alignment.Center // Centers text
            ) {
                Text(
                    text = selectedYear.toString(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.background,
                scrollState = scrollState,
            ) {
                years.forEach { year ->
                    val isCurrent = year == selectedYear
                    DropdownMenuItem(
                        modifier = Modifier.onGloballyPositioned { elementHeight = it.size.height },
                        enabled = !isCurrent,
                        text = {
                            Text(
                                year.toString(),
                                color = when(isCurrent) {
                                    true -> MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha=0.33f)
                                    false -> MaterialTheme.colorScheme.onSecondaryContainer
                                }
                            )
                        },
                        onClick = {
                            expanded = false
                            onYearSelected(year)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}


@Composable
@PreviewLightDark
fun PreviewYearDropdown() {
    MyVacationDays2Theme {
        YearDropDown(years= (1999..2025).toList(), selectedYear = 2025, onYearSelected = {})
    }
}