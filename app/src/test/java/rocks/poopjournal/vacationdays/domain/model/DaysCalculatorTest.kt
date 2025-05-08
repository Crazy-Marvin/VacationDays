package rocks.poopjournal.vacationdays.domain.model

import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DaysCalculatorTest {
    private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy") // Match the saved format

    private fun String.toLocalDate() = LocalDate.parse(this, formatter)

    private fun diffAssert(value : Int, expected: Int) {
        assert(value == expected) { "$value != $expected" }
    }

    @Test
    fun `sun to sat with weekends is 7 days`() {

        diffAssert(calculateDaysBetween(
            "04/05/2025".toLocalDate(),
            "10/05/2025".toLocalDate(),
            excludeWeekends = false,
        ), 7)
    }

    @Test
    fun `sun to sat without weekends is 5 days`() {
        diffAssert(calculateDaysBetween(
            "04/05/2025".toLocalDate(),
            "10/05/2025".toLocalDate(),
            excludeWeekends = true,
        ), 5)
    }

    @Test
    fun `mon to fri without weekends is 5 days`() {
        diffAssert(calculateDaysBetween(
            "05/05/2025".toLocalDate(),
            "09/05/2025".toLocalDate(),
            excludeWeekends = true,
        ), 5)
    }

    @Test
    fun `mon to next fri without weekends is 10 days`() {
        diffAssert(calculateDaysBetween(
            "05/05/2025".toLocalDate(),
            "16/05/2025".toLocalDate(),
            excludeWeekends = true,
        ), 10)
    }

    @Test
    fun `mon to next fri with weekends is 14 days`() {
        diffAssert(calculateDaysBetween(
            "05/05/2025".toLocalDate(),
            "16/05/2025".toLocalDate(),
            excludeWeekends = false,
        ), 12)
    }
}