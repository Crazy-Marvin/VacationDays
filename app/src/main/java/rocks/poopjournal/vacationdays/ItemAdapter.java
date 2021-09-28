package rocks.poopjournal.vacationdays;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    DB_Controller db;
    Context con;

    ItemAdapter(List<Item> itemList, DB_Controller db, Context con) {
        this.itemList = itemList;
        this.db = db;
        this.con = con;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Item item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(item.getItemTitle());
        itemViewHolder.tvItemTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemViewHolder.tvItemTitle.setTextColor(Color.BLACK);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(con);
                builder1.setMessage("Delete the whole thread?");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String s = itemViewHolder.tvItemTitle.getText().toString();
                                String date = "";
                                Log.d("getmoninnum", "" + s);
                                String[] splt = s.split(" ");
                                switch (splt[0]) {
                                    case "Jan":
                                        date = splt[1] + "-01";
                                        break;
                                    case "Feb":
                                        date = splt[1] + "-02";
                                        break;
                                    case "Mar":
                                        date = splt[1] + "-03";
                                        break;
                                    case "Apr":
                                        date = splt[1] + "-04";
                                        break;
                                    case "May":
                                        date = splt[1] + "-05";
                                        break;
                                    case "Jun":
                                        date = splt[1] + "-06";
                                        break;
                                    case "July":
                                        date = splt[1] + "-07";
                                        break;
                                    case "Aug":
                                        date = splt[1] + "-08";
                                        break;
                                    case "Sept":
                                        date = splt[1] + "-09";
                                        break;
                                    case "Oct":
                                        date = splt[1] + "-10";
                                        break;
                                    case "Nov":
                                        date = splt[1] + "-11";
                                        break;
                                    case "Dec":
                                        date = splt[1] + "-12";
                                        break;
                                }
                                db.delete_mainItem(date);
                                Helper.senddate=date;
                                Log.d("deleted", "deleted : " + date);
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
                                itemViewHolder.tvItemTitle.setTextColor(con.getResources().getColor(R.color.apptheme));
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return true;
            }
        });
        itemViewHolder.img.setVisibility(View.INVISIBLE);
        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());
        // Create sub item view adapter
        Log.d("senddateitem","ye lo : "+Helper.senddate);

        SubItemAdapter subItemAdapter = new SubItemAdapter(item.getSubItemList(),db,con);
        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;
        private ImageView img;
        private CardView cardView;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            img=itemView.findViewById(R.id.img);
            cardView=itemView.findViewById(R.id.card);
        }
    }
}
