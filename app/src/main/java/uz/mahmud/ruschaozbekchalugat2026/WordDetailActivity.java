package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import uz.mahmud.ruschaozbekchalugat2026.helper.DatabaseHelper;
import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class WordDetailActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private MaterialToolbar toolbar;

    private TextView txtRussian;
    private TextView txtUzbek;
    private TextView txtWordType;
    private TextView txtExample;
    private ImageView Fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        toolbar = findViewById(R.id.toolbarDetail);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(
                        this,
                        R.color.statusBarColor
                )
        );
        toolbar.setTitleTextColor(
                ContextCompat.getColor(
                        this,
                        R.color.white
                )
        );
        toolbar.setNavigationIconTint(Color.WHITE);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true); // Orqaga tugma
            getSupportActionBar()
                    .setTitle(
                            getString(R.string.app_name)
                    );
        }

        toolbar.setNavigationOnClickListener(
                v -> finish());

        txtRussian = findViewById(R.id.txtRussianDetail);
        txtUzbek = findViewById(R.id.txtUzbekDetail);
        txtWordType = findViewById(R.id.txtWordType);
        txtExample = findViewById(R.id.txtExample);

        String ruWord =
                getIntent().getStringExtra("ru_word");

        String uzWord =
                getIntent().getStringExtra("uz_word");

       String wordType =
                getIntent().getStringExtra("word_type");

       String example =
                getIntent().getStringExtra("example");

        if (ruWord == null) {
            ruWord = "";
        }

        if (uzWord == null) {
            uzWord = "";
        }

        if (wordType == null) {
            wordType = "";
        }

        if (example == null) {
            example = "";
        }

        // ⭐ MANA SHU YERGA
        final String favoriteRuWord = ruWord;
        final String favoriteUzWord = uzWord;
        final String favoriteWordType = wordType;
        final String favoriteExample = example;

        txtRussian.setText(ruWord);
        txtUzbek.setText(uzWord);

        if (wordType.trim().isEmpty()) {

            txtWordType.setVisibility(View.GONE);

        } else {

            txtWordType.setVisibility(View.VISIBLE);

            txtWordType.setText(wordType);
        }

        if (example.trim().isEmpty()) {

            txtExample.setVisibility(View.GONE);

        } else {

            txtExample.setVisibility(View.VISIBLE);

            txtExample.setText(example);
        }
    }
    // =========================
    // TOOLBAR MENU
    // =========================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_detail_menu,
                menu
        );

        updateFavoriteIcon(menu);

        return true;
    }

    // =========================
    // FAVORITE ICON
    // =========================

    private void updateFavoriteIcon(Menu menu) {

        MenuItem item =
                menu.findItem(
                        R.id.action_favorite
                );

        if (item == null) {
            return;
        }

        String ruWord =
                getIntent().getStringExtra("ru_word");

        if (ruWord == null) {
            ruWord = "";
        }

        DatabaseHelper db =
                new DatabaseHelper(this);

        boolean isFavorite =
                db.isFavorite(ruWord);

        if (isFavorite) {

            item.setIcon(
                    R.drawable.ic_favorite
            );

        } else {

            item.setIcon(
                    R.drawable.ic_favorite_border
            );
        }

        // Toolbar ko'k bo'lsa, yurak oq bo'ladi
        if (item.getIcon() != null) {

            item.getIcon().setTint(
                    Color.WHITE
            );
        }
    }

    // =========================
    // FAVORITE BOSILGANDA
    // =========================

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item
    ) {

        if (item.getItemId()
                == R.id.action_favorite) {

            String ruWord =
                    getIntent().getStringExtra(
                            "ru_word"
                    );

            String uzWord =
                    getIntent().getStringExtra(
                            "uz_word"
                    );

            String wordType =
                    getIntent().getStringExtra(
                            "word_type"
                    );

            String example =
                    getIntent().getStringExtra(
                            "example"
                    );

            if (ruWord == null) {
                ruWord = "";
            }

            if (uzWord == null) {
                uzWord = "";
            }

            if (wordType == null) {
                wordType = "";
            }

            if (example == null) {
                example = "";
            }

            DatabaseHelper db =
                    new DatabaseHelper(this);

            boolean added =
                    db.toggleFavorite(
                            ruWord,
                            uzWord,
                            wordType,
                            example
                    );

            if (added) {

                Toast.makeText(
                        this,
                        getString(
                                R.string.favorite_added
                        ),
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        getString(
                                R.string.favorite_removed
                        ),
                        Toast.LENGTH_SHORT
                ).show();
            }

            // Yurak belgisini yangilash
            invalidateOptionsMenu();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}