package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import uz.mahmud.ruschaozbekchalugat2026.helper.DatabaseHelper;
import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private GridView gridLetters;

    private String[] letters;
    private String[] queryLetters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String[] letters = getResources().getStringArray(
                R.array.russian_letters
        );

        String[] queryLetters = getResources().getStringArray(
                R.array.query_letters
        );

        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(this, R.color.statusBarColor)
        );
        getSupportActionBar().setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        Drawable overflowIcon = toolbar.getOverflowIcon();

        if (overflowIcon != null) {
            overflowIcon.setTint(Color.WHITE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Orqaga tugma
        }
        toolbar.setNavigationIconTint(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> finish());

        gridLetters = findViewById(R.id.gridLetters);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.letter_item,
                R.id.txtLetter,
                letters
        );

        gridLetters.setAdapter(adapter);

        gridLetters.setOnItemClickListener((parent, view, position, id) -> {

            String selectedLetter = queryLetters[position];
            String titleLetter = letters[position];

            int count = databaseHelper.getWordCountByLetter(selectedLetter);

            Toast.makeText(
                    MainActivity.this,
                    titleLetter + " — " + count + " ta so'z",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    MainActivity.this,
                    WordListActivity.class
            );

            intent.putExtra("letter", selectedLetter);
            intent.putExtra("title", titleLetter);

            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_menu,
                menu
        );

        MenuItem settingsItem =
                menu.findItem(R.id.action_settings);

        settingsItem.setIcon(
                R.drawable.settings
        );

        settingsItem.setShowAsAction(
                MenuItem.SHOW_AS_ACTION_NEVER
        );

        // Icon rangini berish
        Drawable icon = settingsItem.getIcon();

        if (icon != null) {
            icon = icon.mutate();
            icon.setTint(Color.BLACK);
            settingsItem.setIcon(icon);
        }

        // Overflow menyudagi iconni ko'rsatish
        try {

            java.lang.reflect.Field field =
                    menu.getClass()
                            .getDeclaredField("mOptionalIconsVisible");

            field.setAccessible(true);
            field.setBoolean(menu, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}