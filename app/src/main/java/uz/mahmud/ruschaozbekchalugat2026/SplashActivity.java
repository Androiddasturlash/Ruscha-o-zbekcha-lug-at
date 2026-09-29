package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.mahmud.ruschaozbekchalugat2026.helper.DatabaseHelper;
import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private static final int SPLASH_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        TextView txtLogo = findViewById(R.id.txtLogo);
        TextView txtSubtitle = findViewById(R.id.txtSubtitle);
        txtLogo.setText(getString(R.string.app_name));
        txtSubtitle.setText(getString(R.string.create_by));

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(
                    SplashActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            finish();

        }, SPLASH_TIME);
    }
}