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
        db.execSQL("CREATE TABLE RECORD(ID TEXT,TITLE TEXT, MONTHYEAR TEXT,DATES TEXT,NOOFHOLIDAYS INT);");
        db.execSQL("CREATE TABLE CHECKMODE(MODE TEXT);");
        db.execSQL("CREATE TABLE NOOFVACATIONS(VACATIONS TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS RECORD;");
        db.execSQL("DROP TABLE IF EXISTS CHECKMODE;");
        db.execSQL("DROP TABLE IF EXISTS NOOFVACATIONS;");
    }

    public void setMode(String a) {
        ContentValues con = new ContentValues();
        con.put("MODE", a);
        this.getWritableDatabase().insertOrThrow("CHECKMODE", "", con);
    }

    public void getMode() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM CHECKMODE", null);
        while (cursor.moveToNext()) {
            Helper.isnightmodeon = cursor.getString(0);
        }
    }

    public void update_mode(String a) {
        this.getWritableDatabase().execSQL("UPDATE CHECKMODE SET MODE='" + a + "'");
        Helper.isnightmodeon = a;
    }

    public void setnoofholidays(int noofvac){
        ContentValues con = new ContentValues();
        con.put("VACATIONS", noofvac);
        this.getWritableDatabase().insertOrThrow("NOOFVACATIONS", "", con);
        Helper.totalHolidays=noofvac;
        Log.d("hahahuhu",""+noofvac);
    }
    public void getnoofholidays() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM NOOFVACATIONS", null);
        while (cursor.moveToNext()) {
            Helper.totalHolidays = Integer.parseInt(String.valueOf(cursor.getInt(0)));

        }
    }
    public void update_mode(int a) {
        this.getWritableDatabase().execSQL("UPDATE NOOFVACATIONS SET MODE='" + a + "'");
        Helper.totalHolidays = a;
    }

    public void insert_data(String id, String title, String monthyear, String dates, int noofholidays) {
        ContentValues con = new ContentValues();
        con.put("ID", id);
        con.put("TITLE", title);
        con.put("MONTHYEAR", monthyear);
        con.put("DATES", dates);
        con.put("NOOFHOLIDAYS", noofholidays);
        this.getWritableDatabase().insertOrThrow("RECORD", "", con);
//                Log.d("bakwaasDatCon",""+con);
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM RECORD", null);
    }

    public void show_data() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM RECORD", null);
        Helper.data = new ArrayList<>();
        ArrayList<String[]> dataTEMP=new ArrayList<>();
        while (cursor.moveToNext()) {
            String[] temp = new String[5];
            temp[0] = (cursor.getString(0));
            String str=(cursor.getString(1));
            if(str.contains("geodhola")){
                str=str.replace("geodhola","'");
            }
            temp[1] = str;
            temp[2] = (cursor.getString(2));
            temp[3] = (cursor.getString(3));
            temp[4] = String.valueOf((cursor.getInt(4)));
            dataTEMP.add(temp);
            Helper.data.add(temp);
        }
        if(dataTEMP.size()>0){

        }
//        for(int i=0;i<Helper.data.size();i++){
//            for(int j=i+1;j<Helper.data.size();j++){
//                if(Helper.data.get(i)[0].equals(Helper.data.get(j)[0])){
//                    Helper.data.remove(j);
//                }
//            }
//        }

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
        this.getWritableDatabase().delete("RECORD", "ID='" + s + "'", null);
    }

    public void updateHabitsIdsForDeletion(String subItemTitle, String monthyear, String
            updatedid) {
        this.getWritableDatabase().execSQL("UPDATE RECORD SET ID='" + updatedid + "' " +
                "WHERE TITLE='" + subItemTitle + "' AND MONTHYEAR='" + monthyear + "'");
    }


}