package rocks.poopjournal.vacationdays.domain.service.holidays

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DateNagerAtService {
    @GET("api/v3/PublicHolidays/{year}/{countryCode}")
    suspend fun getPublicHolidays(@Path("year") year: Int, @Path("countryCode") countryCode: String): Response<List<PublicHolidayV3Dto>>
}