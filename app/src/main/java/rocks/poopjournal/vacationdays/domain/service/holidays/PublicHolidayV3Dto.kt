package rocks.poopjournal.vacationdays.domain.service.holidays

import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class PublicHolidayV3Dto(
    val date: String,
    val localName: String,
    val name: String,
    val types: List<HolidayType>?
) {
    val localDate by lazy { LocalDate.parse(date, formatter) }

    enum class HolidayType {
        Public,
        Bank,
        School,
        Authorities,
        Optional,
        Observance
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    }
}