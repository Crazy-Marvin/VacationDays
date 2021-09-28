package rocks.poopjournal.vacationdays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DB_Controller extends SQLiteOpenHelper {
    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "a.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RECORD(ID TEXT,TITLE TEXT, MONTHYEAR TEXT,DATES TEXT);");
        db.execSQL("CREATE TABLE CHECKMODE(MODE TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS RECORD;");
        db.execSQL("DROP TABLE IF EXISTS CHECKMODE;");
    }

    public void setMode(String a) {
        ContentValues con = new ContentValues();
        con.put("MODE", a);
        this.getWritableDatabase().insertOrThrow("CHECKMODE", "", con);
    }
    public void getMode(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM CHECKMODE", null);
        while (cursor.moveToNext()){
            Helper.isnightmodeon=cursor.getString(0);
        }
    }
    public void update_mode(String a) {
        this.getWritableDatabase().execSQL("UPDATE CHECKMODE SET MODE='" + a + "'");
        Helper.isnightmodeon=a;
    }


    public void insert_data(String id, String title, String monthyear, String dates) {
        ContentValues con = new ContentValues();
        con.put("ID", id);
        con.put("TITLE", title);
        con.put("MONTHYEAR", monthyear);
        con.put("DATES", dates);
        this.getWritableDatabase().insertOrThrow("RECORD", "", con);

    }

    public void show_data() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM RECORD", null);
        Helper.data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String[] temp = new String[4];
            temp[0] = (cursor.getString(0));
            Log.d("show0", "" + temp[0]);
            temp[1] = (cursor.getString(1));
            temp[2] = (cursor.getString(2));
            temp[3] = (cursor.getString(3));
            Helper.data.add(temp);
        }
        for (int i = 0; i < Helper.data.size(); i++) {
            for (int j = 0; j <= 3; j++) {
            }
        }

    }

    public void showMonthYear(String monthyear) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM RECORD WHERE MONTHYEAR ='" + monthyear + "'", null);
        Helper.dataformonthyear = new ArrayList<>();

        while (cursor.moveToNext()) {
            String[] temp = new String[4];
            temp[0] = (cursor.getString(0));
            temp[1] = (cursor.getString(1));
            temp[2] = (cursor.getString(2));
            temp[3] = (cursor.getString(3));
            Helper.dataformonthyear.add(temp);
        }

    }
    public void delete_mainItem(String s) {
        this.getWritableDatabase().delete("RECORD", "MONTHYEAR='" + s + "'", null);
    }
    public void delete_subItem(String s) {
        Log.d("muhbandkrle",""+s);
        this.getWritableDatabase().delete("RECORD", "ID='" + s + "'", null);
    }
    public void updateHabitsIdsForDeletion(String subItemTitle, String monthyear, String updatedid) {
        this.getWritableDatabase().execSQL("UPDATE RECORD SET ID='" + updatedid + "' " +
                "WHERE TITLE='" + subItemTitle + "' AND MONTHYEAR='" + monthyear + "'");
    }


}