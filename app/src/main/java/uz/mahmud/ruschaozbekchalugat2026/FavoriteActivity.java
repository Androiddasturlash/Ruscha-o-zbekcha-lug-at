package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;

import uz.mahmud.ruschaozbekchalugat2026.helper.DatabaseHelper;
import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private ListView listFavorite;
    private LinearLayout emptyFavorite;

    private DatabaseHelper databaseHelper;

    private final ArrayList<HashMap<String, String>> favoriteList =
            new ArrayList<>();

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        MaterialToolbar toolbar = findViewById(R.id.toolbarFavorite);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(this, R.color.statusBarColor)
        );

        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(getString(R.string.favorite));

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIconTint(
                Color.WHITE
        );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        listFavorite =
                findViewById(R.id.listFavorite);
        emptyFavorite
                = findViewById( R.id.emptyFavorite );

        databaseHelper =
                new DatabaseHelper(this);

        loadFavorites();
    }
    // =====================================================
    // TOOLBAR MENU
    // =====================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_favorite_menu,
                menu
        );

        return true;
    }

    // =====================================================
    // TOOLBAR MENU BOSILGANDA
    // =====================================================

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item
    ) {

        if (item.getItemId() ==
                R.id.action_delete_all) {

            // Sevimlilar bo'lmasa hech narsa qilmaymiz
            if (favoriteList.isEmpty()) {

                new AlertDialog.Builder(this)

                        .setTitle(
                                getString(R.string.favorite)
                        )

                        .setMessage(
                                getString(R.string.no_favorite_found)
                        )

                        .setPositiveButton(
                                getString(R.string.yes),
                                null
                        )

                        .show();
                return true;
            }

            showConfirmation();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // =====================================================
    // TASDIQLASH OYNASI
    // =====================================================

    private void showConfirmation() {

        new AlertDialog.Builder(this)

                .setTitle(
                        getString(R.string.delete_favorites_title)
                )

                .setMessage(
                        getString(R.string.delete_favorites_message)
                )

                .setNegativeButton(
                        getString(R.string.cancel),
                        null
                )

                .setPositiveButton(
                        getString(R.string.delete),
                        (dialog, which) -> {

                            MediaPlayer mediaPlayer = MediaPlayer.create(
                                    FavoriteActivity.this,
                                    R.raw.delete );
                            if (mediaPlayer != null) {
                                mediaPlayer.setOnCompletionListener(mp ->
                                        mp.release()
                                );
                                mediaPlayer.start();
                            }

                            databaseHelper
                                    .deleteAllFavorites();

                            loadFavorites();
                        }
                )

                .show();
    }

    private void loadFavorites() {

        favoriteList.clear();

        Cursor cursor = null;

        try {

            cursor =
                    databaseHelper.getAllFavorites();

            while (cursor.moveToNext()) {

                String ruWord =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "ru_word"
                                )
                        );

                String uzWord =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "uz_word"
                                )
                        );

                String wordType =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "word_type"
                                )
                        );

                String example =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "example"
                                )
                        );

                HashMap<String, String> map =
                        new HashMap<>();

                map.put("ru", ruWord);
                map.put("uz", uzWord);

                map.put(
                        "word_type",
                        wordType == null
                                ? ""
                                : wordType
                );

                map.put(
                        "example",
                        example == null
                                ? ""
                                : example
                );

                favoriteList.add(map);
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        // =================================================
        // BO'SH YOKI BOR HOLAT
        // =================================================

        if (favoriteList.isEmpty()) {

            // ListView yashiriladi
            listFavorite.setVisibility(
                    View.GONE
            );

            // Bo'sh holat ko'rsatiladi
            emptyFavorite.setVisibility(
                    View.VISIBLE
            );

        } else {

            // Bo'sh holat yashiriladi
            emptyFavorite.setVisibility(
                    View.GONE
            );

            // ListView ko'rsatiladi
            listFavorite.setVisibility(
                    View.VISIBLE
            );
        }

        adapter =
                new SimpleAdapter(
                        this,
                        favoriteList,
                        R.layout.word_item,
                        new String[]{
                                "ru",
                                "uz"
                        },
                        new int[]{
                                R.id.txtRussian,
                                R.id.txtUzbek
                        }
                );

        listFavorite.setAdapter(adapter);


        // So'zni bosganda batafsil oynani ochish
        listFavorite.setOnItemClickListener(
                (parent, view, position, id) -> {

                    HashMap<String, String> selectedWord =
                            favoriteList.get(position);

                    Intent intent =
                            new Intent(
                                    FavoriteActivity.this,
                                    WordDetailActivity.class
                            );

                    intent.putExtra(
                            "ru_word",
                            selectedWord.get("ru")
                    );

                    intent.putExtra(
                            "uz_word",
                            selectedWord.get("uz")
                    );

                    intent.putExtra(
                            "word_type",
                            selectedWord.get("word_type")
                    );

                    intent.putExtra(
                            "example",
                            selectedWord.get("example")
                    );

                    startActivity(intent);
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadFavorites();
        }
    }
}