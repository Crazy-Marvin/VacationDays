package rocks.poopjournal.vacationdays;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.BuildConfig;

public class About extends AppCompatActivity {
    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        version=findViewById(R.id.versiontext);
        version.setText(BuildConfig.VERSION_NAME+" "+this.getString(R.string.apache) );
}

    public void contact_codeaquaria(View view) {
        switch(view.getId()){
            case R.id.btnmail_codeaquaria:
                String mailto = "mailto:codeaquaria20@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast toast = Toast.makeText(this, "    Error to open Email    ", Toast.LENGTH_SHORT);
                    View v = toast.getView();
                    v.setBackground(this.getResources().getDrawable(R.drawable.roundbutton));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.btngit_codeaquaria:
                Uri uri = Uri.parse("https://github.com/arafaatqureshi"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btntwitter_codeaquaria:
                Uri ui = Uri.parse("https://www.facebook.com/Code-Aquaria-109834144196326"); // missing 'http://' will cause crashed
                Intent it = new Intent(Intent.ACTION_VIEW, ui);
                startActivity(it);
                break;

        }
    }

    public void contact_marvin(View view) {
        switch(view.getId()){
            case R.id.btnmail_crazymarvin:
                String mailto = "mailto:marvin@poopjournal.rocks";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast toast = Toast.makeText(this, "    Error to open Email    ", Toast.LENGTH_SHORT);
                    View v = toast.getView();
                    v.setBackground(this.getResources().getDrawable(R.drawable.roundbutton));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.btngit_crazymarvin:
                Uri uri = Uri.parse("https://github.com/Crazy-Marvin"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btntwitter_crazymarvin:
                Uri u = Uri.parse("https://twitter.com/CrazyMarvinApps"); // missing 'http://' will cause crashed
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
                break;

        }
    }

    public void translate(View view) {
        Uri u = Uri.parse("https://hosted.weblate.org/engage/vacation-days/");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void report(View view) {
        Uri u = Uri.parse("https://github.com/Crazy-Marvin/VacationDays/issues/");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void viewsource(View view) {
        Uri u = Uri.parse("https://github.com/Crazy-Marvin/VacationDays/");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void back(View view) {
        Intent i = new Intent(About.this, MainActivity.class);
        finishAffinity();
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void jetpack(View view) {
        Uri u = Uri.parse("https://developer.android.com/jetpack");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void logoclicked(View view) {
        Uri u = Uri.parse("https://crazymarvin.com/vacation-days/");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void feather(View view) {
        Uri u = Uri.parse("https://feathericons.com/");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void apacheee(View view) {
        Uri u = Uri.parse("https://github.com/Crazy-Marvin/VacationDays/blob/development/LICENSE");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }

    public void daterangepicker(View view) {
        Uri u = Uri.parse("https://github.com/savvisingh/DateRangePicker/blob/master/LICENSE.txt");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }


    public void Kotlin(View view) {
        Uri u = Uri.parse("https://github.com/JetBrains/kotlin/blob/master/license/LICENSE.txt");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }
    public void Java(View view) {
        Uri u = Uri.parse("http://openjdk.java.net/legal/gplv2+ce.html");
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
    }
        public void nobobutton(View view) {
            Uri u = Uri.parse("https://github.com/alex31n/NoboButton/blob/master/LICENSE");
            Intent i = new Intent(Intent.ACTION_VIEW, u);
            startActivity(i);
        }
        public void cImgButton(View view) {
            Uri u = Uri.parse("https://github.com/hdodenhof/CircleImageView/blob/master/LICENSE.txt");
            Intent i = new Intent(Intent.ACTION_VIEW, u);
            startActivity(i);
        }
}