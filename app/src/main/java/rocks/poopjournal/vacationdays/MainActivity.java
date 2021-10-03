package rocks.poopjournal.vacationdays;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.ornach.nobobutton.NoboButton;

public class MainActivity extends AppCompatActivity {
    NoboButton timeline, calendar;
    Toolbar toolbar;
    DB_Controller db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB_Controller(this, "", null, 2);
        db.show_data();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timeline = findViewById(R.id.timeline);
        calendar = findViewById(R.id.calendar);
        if(Helper.whichTabSelected==0){
            timeline.setBackgroundColor(getResources().getColor(R.color.tabs));
            calendar.setBackgroundColor(Color.TRANSPARENT);
            timeline.setTextColor(getResources().getColor(R.color.textcolorblack));
            calendar.setTextColor(getResources().getColor(R.color.textcolorwhite));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new FragmentTimeline());
            ft.commit();
        }
        else if(Helper.whichTabSelected==1){
            timeline.setBackgroundColor(Color.TRANSPARENT);
            calendar.setBackgroundColor(getResources().getColor(R.color.tabs));
            calendar.setTextColor(getResources().getColor(R.color.textcolorblack));
            timeline.setTextColor(getResources().getColor(R.color.textcolorwhite));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new FragmentCalendar());
            ft.commit();
        }

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.whichTabSelected=0;
                timeline.setBackgroundColor(getResources().getColor(R.color.tabs));
                timeline.setTextColor(getResources().getColor(R.color.textcolorblack));
                calendar.setBackgroundColor(Color.TRANSPARENT);
                calendar.setTextColor(getResources().getColor(R.color.textcolorwhite));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.fade_in,R.anim.fade_out
                );
                ft.replace(R.id.container, new FragmentTimeline());
                ft.commit();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Helper.whichTabSelected=1;
                calendar.setBackgroundColor(getResources().getColor(R.color.tabs));
                calendar.setTextColor(getResources().getColor(R.color.textcolorblack));
                timeline.setBackgroundColor(Color.TRANSPARENT);
                timeline.setTextColor(getResources().getColor(R.color.textcolorwhite));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(0,0);
                ft.replace(R.id.container, new FragmentCalendar());
                ft.commit();
            }
        });
    }

    public void floatingbuttonclicked(View view) {
        final Dialog d = new Dialog(this);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.addholiday);
        Button btnsave=d.findViewById(R.id.saveHolidayTitle);
        EditText editText=d.findViewById(R.id.holidayTitle);
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount=0.9f;
        d.getWindow().setAttributes(lp);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("")){
                    editText.setError("Can't be empty");
                }
                else{
                    Helper.holidayTitle=""+editText.getText().toString();
                    d.dismiss();
                    Intent i = new Intent(MainActivity.this, DatePicker.class);
                    finishAffinity();
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            }
        });d.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
                return  true;
            case R.id.about:
                Intent in = new Intent(MainActivity.this, About.class);
                startActivity(in);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}