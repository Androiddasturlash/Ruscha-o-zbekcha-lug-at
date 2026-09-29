package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private LinearLayout layoutLanguage,layoutFav,layoutShare,layoutInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbarSettings);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(this, R.color.statusBarColor)
        );

        toolbar.setTitleTextColor(
                ContextCompat.getColor(this, R.color.white));

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(getString(R.string.settings));

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIconTint(
                Color.WHITE
        );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );


        // =================================
        // Viewlar
        // =================================

        layoutLanguage = findViewById(
                R.id.layoutLanguage
        );
        layoutFav = findViewById(
                R.id.layoutFav
        );
        layoutShare = findViewById(
                R.id.layoutShare
        );
        layoutInfo = findViewById(
                R.id.layoutInfo
        );

        layoutFav.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SettingsActivity.this,
                            FavoriteActivity.class
                    );

            startActivity(intent);
        });
        layoutShare.setOnClickListener(v-> {
            shareApplication();
        });
        layoutLanguage.setOnClickListener(v-> {
            showLanguageDialog();
        });
        layoutInfo.setOnClickListener( v -> {

            Intent intent =
                    new Intent(
                            SettingsActivity.this,
                            InfoActivity.class
                    );

            startActivity(intent);
        });
    }
    private void shareApplication() {
        try {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String apkPath = app.sourceDir;

            // Yangi nom bilan nusxa olish
            File originalApk = new File(apkPath);
            File newApk = new File(getExternalCacheDir(), "Ruscha o'zbekcha lug'at.Apk");

            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(newApk);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            // Share qilish
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("*/*");

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    newApk
            );

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Russian Uzbek dictionary app!");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share Russian Uzbek dictionary"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showLanguageDialog() {

        String[] languages = {
                "O‘zbek tili",
                "English",
                "Русский"
        };

        String currentLanguage =
                LocaleHelper.getLanguage(this);

        int selected = 0;

        if (currentLanguage.equals("en")) {
            selected = 1;
        } else if (currentLanguage.equals("ru")) {
            selected = 2;
        }

        new AlertDialog.Builder(this)
                .setTitle("Tilni tanlang")
                .setSingleChoiceItems(
                        languages,
                        selected,
                        (dialog, which) -> {

                            String language;

                            if (which == 0) {
                                language = "uz";
                            } else if (which == 1) {
                                language = "en";
                            } else {
                                language = "ru";
                            }

                            // Tilni saqlash
                            LocaleHelper.setLocale(
                                    this,
                                    language
                            );

                            dialog.dismiss();

                            // MainActivity ni yangi locale bilan ochish
                            Intent intent = new Intent(
                                    SettingsActivity.this,
                                    MainActivity.class
                            );

                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                            );

                            startActivity(intent);

                            // SettingsActivity yopiladi
                            finish();
                        }
                )
                .show();
    }
}