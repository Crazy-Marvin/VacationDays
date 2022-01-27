package rocks.poopjournal.myvacationdays;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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

public class Settings extends AppCompatActivity {
    rocks.poopjournal.myvacationdays.DB_Controller db;
    TextView modetitle;
    //*******STORAGE PERMISSION**********
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storagepermission;
    String file_name="Mydata.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DB_Controller(getApplicationContext(), "", null, 2);
        storagepermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        modetitle = findViewById(R.id.modetitle);
//        Log.d("heyyyymode",""+AppCompatDelegate.getDefaultNightMode()+"  Night :"+AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        switch (rocks.poopjournal.myvacationdays.Helper.isnightmodeon) {
            case "followsys":
                modetitle.setText("Follow System");
                break;
            case "light":
                modetitle.setText("Light");
                break;
            case "dark":
                modetitle.setText("Dark");
                break;
        }

    }

    private boolean checkStoragePermission() {
        //check if storage permission is availbale or not, return true/false
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;

    }

    private void requestStoragePermissionImport() {
        //request storage permission for import
        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST_CODE_IMPORT);
    }

    private void requestStoragePermissionExport() {
        //request storage permission for import
        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST_CODE_EXPORT);
    }
    private void importCSV(){
        String FileandPathname= Environment.getExternalStorageDirectory()+"/SQliteBackup/"+"SQLite_Backup.csv";
        File csvfile= new File(FileandPathname);
        if(csvfile.exists()){
            try{
                CSVReader csvReader=new  CSVReader(new FileReader(csvfile.getAbsoluteFile()));
                ArrayList<String[]> restoreData=new ArrayList<>();

                int k=0;
                for(int i=0;i<Helper.data.size();i++){
                    String[] nextline;
                    while((nextline=csvReader.readNext())!=null){
                        Log.d("readingcsvlength","Length"+nextline.length);
                        Log.d("readingcsv[0]",""+ nextline[0]);
                        Log.d("readingcsv[1]",""+ nextline[1]);
                        Log.d("readingcsv[2]",""+ nextline[2]);
                        Log.d("readingcsv[3]",""+ nextline[3]);
                        String id=nextline[0];
                        String title=nextline[1];
                        String monthyear=nextline[2];
                        String dates=nextline[3];
                        db.insert_data(id, title,monthyear,dates);
                        k++;
                    }
                }
                db.show_data();
                Helper.whichTabSelected=0;
                Toast.makeText(this, Helper.whichTabSelected+"Restored...."+Helper.data.size(), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Settings.this,MainActivity.class);
                startActivity(in);
                finish();
            }catch (Exception e){
                Toast.makeText(this, "Error!!! "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
        else{
            Toast.makeText(this, "No backup found", Toast.LENGTH_LONG ).show();

        }
    }
    private void exportCSV(){
        //PATH OF CSV
        File folder= new File(Environment.getExternalStorageDirectory()+"/"+"SQliteBackup");
        boolean isFolderCreated = false;
        if(!folder.exists()){
            isFolderCreated=folder.mkdir();//create a folder if it doesnot exists.
        }
        Log.d("CSC_TAG","exportCSV"+isFolderCreated);
        String csvFilename="SQLite_Backup.csv";
        String FileandPathname=folder.toString()+ "/" +csvFilename;
        db.show_data();
        try{
            //write csv
            FileWriter fw= new FileWriter(FileandPathname);
            for(int i=0;i<Helper.data.size();i++){
                fw.append(Helper.data.get(i)[0]);
                fw.append(",");
                fw.append(Helper.data.get(i)[1]);
                fw.append(",");
                fw.append(Helper.data.get(i)[2]);
                fw.append(",");
                fw.append(Helper.data.get(i)[3]);
                fw.append(",");
                fw.append("\n");
                //Toast.makeText(this, "Backup Exported "+FileandPathname, Toast.LENGTH_LONG ).show();
            }
            final Dialog d = new Dialog(this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.backup_dialogbox);
            Button btnok = d.findViewById(R.id.btnok);
            TextView path= d.findViewById(R.id.path);
            path.setText("Backup Exported : "+FileandPathname);
            WindowManager.LayoutParams lp = d.getWindow().getAttributes();
            lp.dimAmount = 0.9f;
            d.getWindow().setAttributes(lp);
            d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });
            d.show();
            fw.flush();
            fw.close();
        }catch (Exception e){
            Toast.makeText(this, "ErrorExport : "+e.getMessage(), Toast.LENGTH_LONG ).show();

        }

    }

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
                Log.d("modeisbuttondone:", "" + rocks.poopjournal.myvacationdays.Helper.isnightmodeon);
                if (rocks.poopjournal.myvacationdays.Helper.isnightmodeon.equals("followsys")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    modetitle.setText("Follow System");
                    d.dismiss();
//                    Intent intennt = new Intent(Settings.this,Settings.class);
//                    startActivity(intennt);
//                    overridePendingTransition(0, 0);
//                    finish();
                }
                if (rocks.poopjournal.myvacationdays.Helper.isnightmodeon.equals("light")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    modetitle.setText("Light");
                    d.dismiss();
//                    Intent intennt = new Intent(Settings.this,Settings.class);
//                    startActivity(intennt);
//                    overridePendingTransition(0, 0);
//                    finish();
                }
                if (rocks.poopjournal.myvacationdays.Helper.isnightmodeon.equals("dark")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    modetitle.setText("Dark");
                    d.dismiss();
//                    Intent intennt = new Intent(Settings.this,Settings.class);
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
                    Log.d("modeisf:", "" + rocks.poopjournal.myvacationdays.Helper.isnightmodeon);
                    break;
                }
            case R.id.light:
                if (checked) {
                    modetitle.setText("Light");
                    db.update_mode("light");
                    Log.d("modeisl:", "" + rocks.poopjournal.myvacationdays.Helper.isnightmodeon);
                    break;
                }
            case R.id.dark:
                if (checked) {
                    modetitle.setText("Dark");
                    db.update_mode("dark");
                    Log.d("modeisd:", "" + rocks.poopjournal.myvacationdays.Helper.isnightmodeon);
                    break;
                }
        }
    }


    public void backup(View view) {
        if(checkStoragePermission()){
            exportCSV();
        }
        else{
            requestStoragePermissionExport();
        }
    }

    public void restore(View view) {
//        String text="Bismillah di barkat";
//        savefile(file_name,text);
        if(checkStoragePermission()){
            importCSV();

        }
        else{
            requestStoragePermissionImport();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //handle permission result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE_EXPORT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    exportCSV();
                } else {
                    //permission denied
                    Toast.makeText(this, "Storage permission required...", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case STORAGE_REQUEST_CODE_IMPORT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    importCSV();
                } else {
                    //permission denied
                    Toast.makeText(this, "Storage permission required...", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

}