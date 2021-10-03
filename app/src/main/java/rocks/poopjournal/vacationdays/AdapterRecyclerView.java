package rocks.poopjournal.vacationdays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rocks.poopjournal.vacationdays.R;


public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.RecyclerViewHolder> {


     List titles = new ArrayList<>();
    Context con;
    DB_Controller db;

    public AdapterRecyclerView(Context con, List<String> titles, DB_Controller db) {
        this.titles = titles;
        this.con = con;
        this.db = db;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recyclerview_layout, viewGroup, false);

        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, @SuppressLint("RecyclerView") final int i) {
        recyclerViewHolder.tv1.setText(""+titles.get(i));
//        db.showMonthYear((String) titles.get(i));
//        db.show_data();
//
        //        recyclerViewHolder.tv1.setText(""+titles.get(i));
//        TextView tv=new TextView(con);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        tv.setLayoutParams(params);
//        db.show_data();
//        ArrayList<String> my = new ArrayList<>();
//        for (int j = 0; j < Helper.data.size(); j++) {
//        my.add(Helper.data.get(j)[2]);
//        }
//        Log.d("check size before",""+my.size());
//        for(int k=0;k<my.size();k++){
//        for(int l=1;l<my.size()-1;l++){
//        if(my.get(k).equals(my.get(l))){
//        Log.d("check","k"+my.get(k)+" l"+my.get(l));
//
//        my.remove(l);
//        }
//        }
//        }
//        months((Helper.data.get(i)[2]));
//        tv.setText(""+Helper.data.get(i)[2]);
//        tv.setGravity(Gravity.CENTER);
////            tv.setTextColor();
//        recyclerViewHolder.relativeLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.TOP);
//        recyclerViewHolder.linearLayout.setPadding(0,50,0,0);
//        recyclerViewHolder.relativeLayout.addView(tv);
//
//
//                String title = titles.get(i);
//        recyclerViewHolder.tv1.setText(title);
//        Log.d("marjaa", "" + Helper.data.get(i)[2]);
//        ArrayList<String> tareekhen = new ArrayList<>();
//        for (int j = 0; j < Helper.data.size(); j++) {
//            Log.d("showCalRecorddata",""+ Helper.data.get(j)[2]);
//            tareekhen.add(Helper.data.get(j)[3]);
//        }
//        String getdates = tareekhen.get(i);
//        Log.d("adaptertareekhen", "" + tareekhen.get(i));
//        String[] splitdates = getdates.split(",");
//        String start_date=splitdates[0];
//        String end_date=splitdates[splitdates.length-1];
//        String[] s = start_date.split("-");
//        String[] e = end_date.split("-");
//        Log.d("haankinn"," S:"+start_date+" E:"+end_date);
//        if(start_date==end_date){
//            recyclerViewHolder.start.setText(s[2]);
//            recyclerViewHolder.to.setHeight(0);
//            recyclerViewHolder.end.setHeight(0);
//        }
//        else{
//
//            recyclerViewHolder.start.setText(s[2]);
//            recyclerViewHolder.to.setBackgroundResource(R.drawable.arrow_down);
//            recyclerViewHolder.end.setText(e[2]);
//        }
//
//        for(int k=0;k<splitdates.length;k++){
//            Log.d("adaptertareekhen",""+splitdates[k] );
//        }



    }


    @Override
    public int getItemCount() {
        return titles.size();
    }
    public void months(String s){

    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView childrv;
        TextView tv1, start, to, end;
//      RelativeLayout relativeLayout;
//      LinearLayout linearLayout;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            childrv=itemView.findViewById(R.id.childrv);
            tv1 = itemView.findViewById(R.id.monyear);
//            start = itemView.findViewById(R.id.startdate);
//            to = itemView.findViewById(R.id.to);
//            end = itemView.findViewById(R.id.enddate);
//            relativeLayout=itemView.findViewById(R.id.body);
//            linearLayout=itemView.findViewById(R.id.linearlayout);
        }
    }


}

//    ArrayList<String> my = new ArrayList<>();
//        for (int j = 0; j < Helper.data.size(); j++) {
//        my.add(Helper.data.get(j)[2]);
//        }
//        Log.d("check size before",""+my.size());
//        for(int k=0;k<my.size();k++){
//        for(int l=1;l<my.size()-1;l++){
//        if(my.get(k).equals(my.get(l))){
//        Log.d("check","k"+my.get(k)+" l"+my.get(l));
//
//        my.remove(l);
//        }
//        }
//        }
//        for(int k=0;k<my.size();k++){
//        for(int l=1;l<my.size()-1;l++){
//        int year1=Integer.parseInt((my.get(k).toString()).substring(0,4));
//        int year2=Integer.parseInt((my.get(l).toString()).substring(0,4));
//        if(year2<year1){
//        Collections.swap(my,0,1);
//        }
//        }
//        }
//        for (int j = 0; j < my.size(); j++) {
//        Log.d("checkelements", my.get(j));
//        }
//        Log.d("check size after",""+my.size());
