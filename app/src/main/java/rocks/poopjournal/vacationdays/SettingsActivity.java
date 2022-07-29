package rocks.poopjournal.vacationdays;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SettingsActivity extends AppCompatActivity {
    rocks.poopjournal.vacationdays.DB_Controller db;
    TextView modetitle;
    //**STORAGE PERMISSION***
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storagepermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DB_Controller(getApplicationContext(), "", null, 2);
        storagepermission = new String[]{WRITE_EXTERNAL_STORAGE};
        modetitle = findViewById(R.id.modetitle);
        switch (rocks.poopjournal.vacationdays.Helper.isnightmodeon) {
            case "followsys":
                modetitle.setText("Follow System");
                break;
            case "light":
                modetitle.setText("Light");
                break;
            case "dark":
                modetitle.setText("Dark");
                break;
            default:
                modetitle.setText("Follow System");

        }

    }

//    private boolean checkStoragePermission() {
//        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
//            Log.d("IncheckStorIf","hello"+Environment.isExternalStorageManager());
//            return Environment.isExternalStorageManager();
//
//        }
//        else{
//
//            int result = ContextCompat.checkSelfPermission(SettingsActivity.this, READ_EXTERNAL_STORAGE);
//            int result1 = ContextCompat.checkSelfPermission(SettingsActivity.this, WRITE_EXTERNAL_STORAGE);
//            Log.d("IncheckStorElse","hello"+result);
//
//            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//        }
//    }

//    private void requestStoragePermissionImport() {
//        //request storage permission for import
//        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST_CODE_IMPORT);
//    }
//
//    private void requestStoragePermissionExport() {
//        //request storage permission for import
//        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST_CODE_EXPORT);
//    }
//    private void importCSV(){
//        String FileandPathname= Environment.getExternalStorageDirectory()+"/SQliteBackup/"+"SQLite_Backup.csv";
//        File csvfile= new File(FileandPathname);
//        if(csvfile.exists()){
//            try{
//                CSVReader csvReader=new  CSVReader(new FileReader(csvfile.getAbsoluteFile()));
//                ArrayList<String[]> restoreData=new ArrayList<>();
//
//                int k=Helper.data.size();
//                int counter;
//                if(k==0){
//                    counter=1;
//                }
//                else{
//                    counter=Helper.data.size();
//                }
//                for(int i=0;i<counter;i++){
//                    Log.d("bakwaasinloop","i am in"+counter);
//                    String[] nextline;
//                    while((nextline=csvReader.readNext())!=null){
//                        String id=nextline[0];
//                        String title=nextline[1];
//                        String monthyear=nextline[2];
//                        String dates=nextline[3];
//                        String newdates=dates.replaceAll("geodholaz",",");
//                        db.insert_data(id, title,monthyear,newdates);
//                        db.show_data();
//
//                        Helper.holidayTitle="";
//                        k++;
//                    }
//                }
//
//                Helper.whichTabSelected=0;
////                Toast.makeText(this, Helper.whichTabSelected+"Restored...."+Helper.data.size(), Toast.LENGTH_SHORT).show();
//                Intent in=new Intent(SettingsActivity.this,MainActivity.class);
//                startActivity(in);
//                finish();
//            }catch (Exception e){
//                Toast.makeText(this, "Error!!! "+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//
//        }
//        else{
//            Toast.makeText(this, "No backup found", Toast.LENGTH_LONG ).show();
//
//        }
//    }
//    private void exportCSV(){
//        //PATH OF CSV
//        File folder= new File(Environment.getExternalStorageDirectory()+"/"+"SQliteBackup");
//        boolean isFolderCreated = false;
//        if(!folder.exists()){
//            isFolderCreated=folder.mkdir();//create a folder if it doesnot exists.
//        }
//        Log.d("CSC_TAG","exportCSV"+isFolderCreated);
//        String csvFilename="SQLite_Backup.csv";
//        String FileandPathname=folder.toString()+ "/" +csvFilename;
//        db.show_data();
//        try{
//            //write csv
//            FileWriter fw= new FileWriter(FileandPathname);
//            for(int i=0;i<Helper.data.size();i++){
//                fw.append(Helper.data.get(i)[0]);
//                fw.append(",");
//                fw.append(Helper.data.get(i)[1]);
//                fw.append(",");
//                fw.append(Helper.data.get(i)[2]);
//                fw.append(",");
//                String newdate= Helper.data.get(i)[3];
//                String new1=newdate.replaceAll(",","geodholaz");
//                Log.d("formatingdate",""+new1);
//                fw.append(new1);
//                fw.append(",");
//                fw.append("\n");
//                //Toast.makeText(this, "Backup Exported "+FileandPathname, Toast.LENGTH_LONG ).show();
//            }
//            final Dialog d = new Dialog(this);
//            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            d.setContentView(R.layout.backup_dialogbox);
//            Button btnok = d.findViewById(R.id.btnok);
//            TextView path= d.findViewById(R.id.path);
//            path.setText("Backup Exported : "+FileandPathname);
//            WindowManager.LayoutParams lp = d.getWindow().getAttributes();
//            lp.dimAmount = 0.9f;
//            d.getWindow().setAttributes(lp);
//            d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//            btnok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    d.dismiss();
//                }
//            });
//            d.show();
//            fw.flush();
//            fw.close();
//        }catch (Exception e){
//            Toast.makeText(this, "ErrorExport : "+e.getMessage(), Toast.LENGTH_LONG ).show();
//
//        }
//
//    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void appearance(View view) {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialogbox);
        Button btndone = d.findViewById(R.id.btndone);
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        d.getWindow().setAttributes(lp);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.getMode();
                Log.d("modeisbuttondone:", "" + rocks.poopjournal.vacationdays.Helper.isnightmodeon);
                if (rocks.poopjournal.vacationdays.Helper.isnightmodeon.equals("followsys")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    modetitle.setText("Follow System");
                    d.dismiss();
//                    Intent intennt = new Intent(SettingsActivity.this,SettingsActivity.class);
//                    startActivity(intennt);
//                    overridePendingTransition(0, 0);
//                    finish();
                }
                if (rocks.poopjournal.vacationdays.Helper.isnightmodeon.equals("light")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    modetitle.setText("Light");
                    d.dismiss();
//                    Intent intennt = new Intent(SettingsActivity.this,SettingsActivity.class);
//                    startActivity(intennt);
//                    overridePendingTransition(0, 0);
//                    finish();
                }
                if (rocks.poopjournal.vacationdays.Helper.isnightmodeon.equals("dark")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    modetitle.setText("Dark");
                    d.dismiss();
//                    Intent intennt = new Intent(SettingsActivity.this,SettingsActivity.class);
//                    startActivity(intennt);
//                    overridePendingTransition(0, 0);
//                    finish();
                }

            }
        });
        d.show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.followsys:
                if (checked) {
                    modetitle.setText("Follow System");
                    db.update_mode("followsys");
                    db.setMode("followsys");
                    Log.d("modeisf:", "" + rocks.poopjournal.vacationdays.Helper.isnightmodeon);
                    break;
                }
            case R.id.light:
                if (checked) {
                    modetitle.setText("Light");
                    db.update_mode("light");
                    db.setMode("light");
                    Log.d("modeisl:", "" + rocks.poopjournal.vacationdays.Helper.isnightmodeon);
                    break;
                }
            case R.id.dark:
                if (checked) {
                    modetitle.setText("Dark");
                    db.update_mode("dark");
                    db.setMode("dark");
                    Log.d("modeisd:", "" + rocks.poopjournal.vacationdays.Helper.isnightmodeon);
                    break;
                }
        }
    }


    public void backup(View view) {
        Toast.makeText(this, "This functionality will be available in next version", Toast.LENGTH_SHORT).show();

//        if(checkStoragePermission()){
//            Log.d("IntakePerExport","hello");
//
//            exportCSV();
//        }
//        else{
//            Log.d("IntakePerRequest","hello");
//
//            requestStoragePermissionExport();
//        }
    }
//
    public void restore(View view) {
        Toast.makeText(this, "This functionality will be available in next version", Toast.LENGTH_SHORT).show();

//        String text="Bismillah di barkat";
////        savefile(file_name,text);
//        if(checkStoragePermission()){
//            importCSV();
//
//        }
//        else{
//            requestStoragePermissionImport();
//        }
//
//
    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //handle permission result
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case STORAGE_REQUEST_CODE_EXPORT: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //permission granted
//                    exportCSV();
//                } else {
//                    //permission denied
//                    takePermission();
//                    Toast.makeText(this, "Storage permission required...", Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//            case STORAGE_REQUEST_CODE_IMPORT: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //permission granted
//                    importCSV();
//                } else {
//                    //permission denied
//                    Toast.makeText(this, "Storage permission required...", Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//
//        }
//    }
//
//    private void takePermission() {
//        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
//            Log.d("IntakePer","hello");
//            try{
//                Intent intent=new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
//                startActivityForResult(intent,2000);
//            } catch (Exception e) {
//                Intent obj=new Intent();
//                obj.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                startActivityForResult(obj,2000);
//
////                e.printStackTrace();
//            }
//        }
//        else{
//            Log.d("IntakePerelse","hello");
//
//            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{
//                    WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},101);
//        }
//    }

}
