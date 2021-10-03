package rocks.poopjournal.vacationdays;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentTimeline extends Fragment {
    RecyclerView rv;
    DB_Controller db;
    TextView monyear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        rv = v.findViewById(R.id.rv);
        db = new DB_Controller(getActivity(), "", null, 2);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ItemAdapter itemAdapter = new ItemAdapter(buildItemList(),db,getContext());
//        new ItemTouchHelper(itemtouchhelper).attachToRecyclerView(rv);
        rv.setAdapter(itemAdapter);
        rv.setLayoutManager(layoutManager);
        return v;
    }

    private List<Item> buildItemList() {
        Log.d("iiitem", "method");

        db.show_data();
        ArrayList<String> monthyear = new ArrayList<>();
        //getting titles
        for (int i = 0; i < Helper.data.size(); i++) {
            monthyear.add(Helper.data.get(i)[2]);
            Log.d("checkmon", "" + monthyear.get(i));
        }
        Log.d("checkmont", "" + monthyear.size());

        List<String> newList = new ArrayList<>();
        for (String name : monthyear) {
            if (!newList.contains(name)) {
                newList.add(name);
            }
        }
        /////////////////////decsending order////////////////////////
        Collections.sort(newList, new Comparator<String>() {
            public int compare(String date1, String date2) {
                String month1 = date1.substring(5);
                String year1 = date1.substring(0,4);

                String month2 = date2.substring(5);
                String year2 = date2.substring(0,4);

                // Condition to check the year
                if (year2.compareTo(year1) > 0)
                    return -1;
                else if (year2.compareTo(year1) < 0)
                    return 1;

                    // Condition to check the month
                 if (month2.compareTo(month1) > 0)
                    return -1;
                else
                    return 1;
            }
        });
        /////////////////
        List<Item> itemList = new ArrayList<>();
        for (int i=0; i<newList.size(); i++) {
            String monthname=getMonthName(newList.get(i));
            Log.d("loopofmonthsname",""+monthname);
                Item item = new Item(""+monthname, buildSubItemList(newList.get(i)));
                itemList.add(item);
        }
        return itemList;
    }


    private List<SubItem> buildSubItemList(String s) {
        db.showMonthYear(s);
        Log.d("checkvalofmonthyear",""+Helper.monthyear);
        ArrayList<String> tareekhen = new ArrayList<>();
        for (int j = 0; j < Helper.dataformonthyear.size(); j++) {
            tareekhen.add(Helper.dataformonthyear.get(j)[3]);
        }
        List<SubItem> subItemList = new ArrayList<>();
        for (int i = 0; i < Helper.dataformonthyear.size(); i++) {
            String monthyear=Helper.data.get(i)[2];
            Log.d("trackingmonthyear",""+monthyear);
            String[] strt = Helper.dataformonthyear.get(i)[3].split(",");
            String start_date = strt[0];
            String end_date = strt[strt.length - 1];
            String[] st = start_date.split("-");
            String[] ed = end_date.split("-");
            SubItem subItem = new SubItem("" + Helper.dataformonthyear.get(i)[1], st[2], ed[2],Helper.dataformonthyear.get(i)[2]);
            subItemList.add(subItem);
        }
        return subItemList;
    }
    private String getMonthName(String s) {
        String date="";
        Log.d("getmon",""+s);
        String[] splt=s.split("-");
        switch (splt[1]){
            case "01":
                date="Jan "+splt[0];
                break;
            case "02":
                date="Feb "+splt[0];
                break;
            case "03":
                date="Mar "+splt[0];
                break;
            case "04":
                date="Apr "+splt[0];
                break;
            case "05":
                date="May "+splt[0];
                break;
            case "06":
                date="Jun "+splt[0];
                break;
            case "07":
                date="July "+splt[0];
                break;
            case "08":
                date="Aug "+splt[0];
                break;
            case "09":
                date="Sept "+splt[0];
                break;
            case "10":
                date="Oct "+splt[0];
                break;
            case "11":
                date="Nov "+splt[0];
                break;
            case "12":
                date="Dec "+splt[0];
                break;


        }
        return date;
    }

}
