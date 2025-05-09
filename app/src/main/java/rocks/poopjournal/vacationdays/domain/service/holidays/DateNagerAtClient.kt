package rocks.poopjournal.vacationdays.domain.service.holidays

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object DateNagerAtClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://date.nager.at/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().build()
                )
            )
            .build()
    }

    val service: DateNagerAtService by lazy { retrofit.create(DateNagerAtService::class.java) }
}