package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // =========================
        // TOOLBAR
        // =========================

        MaterialToolbar toolbar =
                findViewById(R.id.toolbarInfo);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(this, R.color.statusBarColor)
        );

        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(
                    getString(R.string.about)
            );

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIconTint(
                Color.WHITE
        );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );


        // =========================
        // TELEGRAM
        // =========================

        View layoutTelegram =
                findViewById(R.id.layoutTelegram);

        layoutTelegram.setOnClickListener(
                v -> showLinkDialog(
                        getString(R.string.telegram_title),
                        getString(R.string.telegram_message),
                        R.drawable.telegram,
                        getString(R.string.telegram)
                )
        );


        // =========================
        // GITHUB
        // =========================

        View layoutGithub =
                findViewById(R.id.layoutGithub);

        layoutGithub.setOnClickListener(
                v -> showLinkDialog(
                        getString(R.string.github_title),
                        getString(R.string.github_message),
                        R.drawable.github,
                        getString(R.string.github)
                )
        );
        View layoutDeveloper =
                findViewById(R.id.layoutDeveloper);

        layoutDeveloper.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    getString(R.string.DeveloperMessage),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void showLinkDialog(
            String title,
            String message,
            int iconResId,
            String url
    ) {

        new AlertDialog.Builder(this)

                .setTitle(title)

                .setMessage(message)

                .setIcon(iconResId)

                .setPositiveButton(
                        getString(R.string.yes),
                        (dialog, which) -> openLink(url)
                )

                .setNegativeButton(
                        getString(R.string.cancel),
                        (dialog, which) -> dialog.dismiss()
                )

                .show();
    }

    // =========================
    // HAVOLANI OCHISH
    // =========================

    private void openLink(String url) {

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    getString(R.string.link_open_error),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}