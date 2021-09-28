package rocks.poopjournal.vacationdays;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatePicker extends AppCompatActivity {
    DB_Controller db;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
        db = new DB_Controller(this, "", null, 2);
        db.show_data();
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText(Helper.holidayTitle);
        builder.setTheme(R.style.CustomMaterialCalendarFullscreenTheme);

//        (R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "Date Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//              then obtain the startDate & endDate from the range
                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;
                List<LocalDate> date = new ArrayList<>();
                DateTimeFormatter df = null;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MMM-yyyy");
                //Long datediff=null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    df = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                    LocalDate d1 = LocalDate.parse(simpleFormat.format(startDate), df);
                    LocalDate d2 = LocalDate.parse(simpleFormat.format(endDate), df);

                   // datediff = ChronoUnit.DAYS.between(d1, d2);
                    while (!d1.isAfter(d2)) {
                        date.add(d1);
                        d1 = d1.plusDays(1);
                    }
                }
                //adding dates to db
                String allDates="";
                for(int i=0;i<date.size();i++){
                    allDates=allDates.concat(date.get(i).toString()).concat(",");
                }
                String monthyear=(date.get(0).toString()).substring(0,7);
                db.insert_data((Helper.data.size()+""),Helper.holidayTitle,monthyear,allDates);
                db.show_data();
                Helper.holidayTitle="";
                Intent i=new Intent(DatePicker.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}