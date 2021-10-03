package rocks.poopjournal.vacationdays;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Settings extends AppCompatActivity {
    DB_Controller db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DB_Controller(getApplicationContext(), "", null, 2);
    }

    public void back(View view) {
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void appearance(View view) {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialogbox);
        Button btndone=d.findViewById(R.id.btndone);
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount=0.9f;
        d.getWindow().setAttributes(lp);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.getMode();
                if(Helper.isnightmodeon.equals("followsys")){

                }
                if(Helper.isnightmodeon.equals("light")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Intent intennt = new Intent(Settings.this,Settings.class);
                    startActivity(intennt);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if(Helper.isnightmodeon.equals("dark")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Intent intennt = new Intent(Settings.this,Settings.class);
                    startActivity(intennt);
                    overridePendingTransition(0, 0);
                    finish();
                }
                d.dismiss();
            }
        });d.show();
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.followsys:
                if (checked)
                {
                    db.update_mode("followsys");
                    Toast.makeText(this, "followsys", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.light:
                if (checked)
                {
                    db.update_mode("light");
                    break;
                }
            case R.id.dark:
                if (checked)
                {
                    db.update_mode("dark");
                    break;
                }
        }
    }

    public void SaveMode(View view) {
        Toast.makeText(this, "+++++++++++++", Toast.LENGTH_SHORT).show();
    }
}