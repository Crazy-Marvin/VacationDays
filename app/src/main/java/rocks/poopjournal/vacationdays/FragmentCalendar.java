package rocks.poopjournal.vacationdays;

        import android.icu.text.SimpleDateFormat;
        import android.media.Image;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.Toast;

        import androidx.annotation.RequiresApi;
        import androidx.fragment.app.Fragment;

        import java.lang.reflect.Array;
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

        int cyear = lastYear.get(Calendar.YEAR);





        ArrayList<String> years= new  ArrayList<>();
        for(int i =10;i>0;i--){
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, -i);
            int iyear = prevYear.get(Calendar.YEAR);
            years.add("" + iyear);
        }

        for(int i =0;i<11;i++){
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, i);
            int iyear = prevYear.get(Calendar.YEAR);
            years.add( "" + iyear);
        }

        Toast.makeText(getActivity(), "current year "+ cyear, Toast.LENGTH_SHORT).show();


        Spinner spinner = v.findViewById(R.id.yearspinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, years);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        Calendar prevYear = Calendar.getInstance();
        int iyear = prevYear.get(Calendar.YEAR);
        String obj = "" + iyear;
        spinner.setSelection(years.indexOf(obj));





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
                .withHighlightedDates(arrayList)
                .displayOnly();
        calendar.scrollToDate(new Date());


        v.findViewById(R.id.spinner_drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();

                if(!tutorialsName.equals("" + iyear)){
                    String dtStart = tutorialsName+"-01-01";
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = format.parse(dtStart);
                        System.out.println(date);
                        calendar.scrollToDate(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });




        return v;
    }
}
