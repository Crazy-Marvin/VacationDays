package rocks.poopjournal.vacationdays;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private List<SubItem> subItemList;
    DB_Controller db;
    Context con;
    SubItemAdapter(List<SubItem> subItemList, DB_Controller db, Context con) {
        this.subItemList = subItemList;
        this.db=db;
        this.con=con;
    }



    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        SubItem subItem = subItemList.get(i);
        Log.d("shaistacheck","position : "+i+" title : "+ subItem.getSubItemTitle() + " monthyear : "+subItem.getMonthyear());
        String updatedid= i+subItem.getSubItemTitle()+subItem.getMonthyear();
        db.show_data();
        subItemViewHolder.tvSubItemTitle.setText(subItem.getSubItemTitle());
        db.updateHabitsIdsForDeletion(subItem.getSubItemTitle(),subItem.getMonthyear(),updatedid);
        Log.d("showbreak","...............................................................");
        db.show_data();
        if(subItem.getStart().equals(subItem.getEnd())){
            subItemViewHolder.startdate.setVisibility(View.INVISIBLE);
            subItemViewHolder.to.setText(subItem.getStart());
            subItemViewHolder.enddate.setVisibility(View.INVISIBLE);
        }
        else{
            subItemViewHolder.startdate.setText(subItem.getStart());
            subItemViewHolder.to.setBackgroundResource(R.drawable.arrow_down);
            subItemViewHolder.enddate.setText(subItem.getEnd());
        }
        subItemViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(con);
                builder1.setMessage("Do you want to delete?");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("muhbandkr",""+updatedid);
                                db.delete_subItem(updatedid);
                                Intent intent = new Intent(con, MainActivity.class);
                                con.startActivity(intent);
                                ((Activity) con).overridePendingTransition(0, 0);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle, startdate, to, enddate;
        ImageView img;

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            startdate = itemView.findViewById(R.id.startdate);
            to = itemView.findViewById(R.id.to);
            enddate = itemView.findViewById(R.id.enddate);
            img=itemView.findViewById(R.id.btndel);
        }
    }
}
