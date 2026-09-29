package uz.mahmud.ruschaozbekchalugat2026;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import uz.mahmud.ruschaozbekchalugat2026.adapter.WordAdapter;
import uz.mahmud.ruschaozbekchalugat2026.helper.DatabaseHelper;
import uz.mahmud.ruschaozbekchalugat2026.helper.LocaleHelper;

public class WordListActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.applyLanguage(newBase)
        );
    }

    private ListView listWords;
    private TextInputEditText editText;

    private DatabaseHelper databaseHelper;

    private final ArrayList<HashMap<String, String>> wordList =
            new ArrayList<>();

    private final ArrayList<HashMap<String, String>> filteredWords =
            new ArrayList<>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_list);

        listWords = findViewById(R.id.listWords);
        editText = findViewById(R.id.editText);

        MaterialToolbar toolbar = findViewById(R.id.toolbarWords);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(
                ContextCompat.getColor(this, R.color.statusBarColor)
        );
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
        toolbar.setNavigationIconTint(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> finish());

        databaseHelper = new DatabaseHelper(this);

        String letter = getIntent().getStringExtra("letter");

        toolbar.setNavigationOnClickListener(v -> finish());

        loadWords(letter);

        // =========================
        // QIDIRUV
        // =========================

        editText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        filterWords(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    private void loadWords(String letter) {

        if (letter == null) {
            return;
        }

        Cursor cursor = null;

        try {

            cursor = databaseHelper.getWordsByLetter(letter);

            while (cursor.moveToNext()) {

                String ruWord =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("ru_word")
                        );

                String uzWord =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("uz_word")
                        );

                String wordType =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("word_type")
                        );

                String example =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("example")
                        );

                HashMap<String, String> map =
                        new HashMap<>();

                map.put("ru", ruWord);
                map.put("uz", uzWord);
                map.put(
                        "word_type",
                        wordType == null ? "" : wordType
                );
                map.put(
                        "example",
                        example == null ? "" : example
                );

                wordList.add(map);
            }

            adapter =
                    new SimpleAdapter(
                            this,
                            filteredWords,
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

            listWords.setAdapter(adapter);

            listWords.setOnItemClickListener(
                    (parent, view, position, id) -> {

                        HashMap<String, String> selectedWord =
                                wordList.get(position);

                        Intent intent =
                                new Intent(
                                        WordListActivity.this,
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
                    });

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        filteredWords.addAll(
                wordList);
    }
    // =====================================================
// QIDIRUV / FILTER
// =====================================================

    private void filterWords(String query) {

        filteredWords.clear();

        String searchText =
                query == null
                        ? ""
                        : query.trim().toLowerCase();

        // Qidiruv bo'sh bo'lsa
        // barcha so'zlarni qaytaramiz
        if (searchText.isEmpty()) {

            filteredWords.addAll(
                    wordList
            );

        } else {

            // Barcha so'zlarni tekshiramiz
            for (
                    HashMap<String, String> word
                    : wordList
            ) {

                String ruWord =
                        word.get("ru");

                String uzWord =
                        word.get("uz");

                if (ruWord == null) {
                    ruWord = "";
                }

                if (uzWord == null) {
                    uzWord = "";
                }

                String ruLower =
                        ruWord.toLowerCase();

                String uzLower =
                        uzWord.toLowerCase();

                // Ruscha YOKI o'zbekcha so'zdan qidiradi
                if (
                        ruLower.contains(
                                searchText
                        )
                                ||
                                uzLower.contains(
                                        searchText
                                )
                ) {

                    filteredWords.add(
                            word
                    );
                }
            }
        }

        // ListView'ni yangilash
        if (adapter != null) {

            adapter.notifyDataSetChanged();
        }
    }
}