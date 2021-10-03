package rocks.poopjournal.vacationdays;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.N)
public class FragmentCalendar extends Fragment {
    DB_Controller db;
    com.savvi.rangedatepicker.CalendarPickerView calendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        ArrayList<String> dates=new ArrayList<String>();
        db = new DB_Controller(getActivity(), "", null, 2);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);

        calendar = v.findViewById(R.id.calendar_view);
        db.show_data();

        //calendar.deactivateDates(list);
        ArrayList<Date> arrayList = new ArrayList<>();
        ArrayList<String> tareekhen = new ArrayList<>();
        for (int i = 0; i < Helper.data.size(); i++) {
            tareekhen.add(Helper.data.get(i)[3]);
        }
        for (int i = 0; i < tareekhen.size(); i++) {
            String getdates = tareekhen.get(i);
            String[] splitdates = getdates.split(",");
            for (int j = 0; j < splitdates.length; j++) {
                String[] arrangedates=splitdates[j].split("-");
                for(int k=0;k<arrangedates.length;k++){
                    dates.add(arrangedates[2]+"-"+arrangedates[1]+"-"+arrangedates[0]);
                }
            }
        }
        try{
            for (int i = 0; i < dates.size(); i++) {

                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                Date newdate = dateformat.parse(dates.get(i));
                    arrayList.add(newdate);
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.init(lastYear.getTime(), nextYear.getTime(), new java.text.SimpleDateFormat("MMMM yyyy"))
                .withHighlightedDates(arrayList);
        calendar.scrollToDate(new Date());
        return v;
    }
}