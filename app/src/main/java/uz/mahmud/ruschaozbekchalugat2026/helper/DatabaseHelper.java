package uz.mahmud.ruschaozbekchalugat2026.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database assets ichidan olinadi
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {
    }

    private void copyDatabaseIfNeeded() throws IOException {

        File databasePath = context.getDatabasePath(DATABASE_NAME);

        if (databasePath.exists()) {
            return;
        }

        File parent = databasePath.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        InputStream inputStream =
                context.getAssets().open(DATABASE_NAME);

        OutputStream outputStream =
                new FileOutputStream(databasePath);

        byte[] buffer = new byte[1024];

        int length;

        while ((length = inputStream.read(buffer)) > 0) {

            outputStream.write(
                    buffer,
                    0,
                    length
            );
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public SQLiteDatabase openDatabase() {

        try {

            copyDatabaseIfNeeded();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return SQLiteDatabase.openDatabase(
                context.getDatabasePath(DATABASE_NAME).getPath(),
                null,
                SQLiteDatabase.OPEN_READONLY
        );
    }

    public Cursor getWordsByLetter(String letter) {

        SQLiteDatabase db = openDatabase();

        return db.query(
                "dictionary",
                new String[]{
                        "ru_word",
                        "uz_word",
                        "word_type",
                        "example"
                },
                "ru_word LIKE ?",
                new String[]{
                        letter + "%"
                },
                null,
                null,
                "ru_word ASC"
        );
    }
    public int getWordCount() {

        SQLiteDatabase db = openDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM dictionary",
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }
    public int getWordCountByLetter(String letter) {

        SQLiteDatabase db = openDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM dictionary WHERE ru_word LIKE ?",
                new String[]{letter + "%"}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }
    // =========================
// FAVORITE DATABASE
// =========================

    private static final String FAVORITE_DATABASE_NAME = "favorites.db";
    private static final int FAVORITE_DATABASE_VERSION = 1;

    private SQLiteDatabase getFavoriteDatabase() {

        return context.openOrCreateDatabase(
                FAVORITE_DATABASE_NAME,
                Context.MODE_PRIVATE,
                null
        );
    }

    // Favorite jadvalini yaratish
    private void createFavoriteTable() {

        SQLiteDatabase db = getFavoriteDatabase();

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS favorites (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ru_word TEXT UNIQUE, " +
                        "uz_word TEXT, " +
                        "word_type TEXT, " +
                        "example TEXT)"
        );

        db.close();
    }
    public boolean isFavorite(String ruWord) {

        createFavoriteTable();

        SQLiteDatabase db = getFavoriteDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM favorites WHERE ru_word = ?",
                new String[]{ruWord}
        );

        boolean favorite = cursor.moveToFirst();

        cursor.close();
        db.close();

        return favorite;
    }
    public void addFavorite(
            String ruWord,
            String uzWord,
            String wordType,
            String example
    ) {

        createFavoriteTable();

        SQLiteDatabase db = getFavoriteDatabase();

        db.execSQL(
                "INSERT OR IGNORE INTO favorites " +
                        "(ru_word, uz_word, word_type, example) " +
                        "VALUES (?, ?, ?, ?)",
                new Object[]{
                        ruWord,
                        uzWord,
                        wordType,
                        example
                }
        );

        db.close();
    }
    public void removeFavorite(String ruWord) {

        createFavoriteTable();

        SQLiteDatabase db = getFavoriteDatabase();

        db.delete(
                "favorites",
                "ru_word = ?",
                new String[]{ruWord}
        );

        db.close();
    }
    public boolean toggleFavorite(
            String ruWord,
            String uzWord,
            String wordType,
            String example
    ) {

        if (isFavorite(ruWord)) {

            removeFavorite(ruWord);

            return false;

        } else {

            addFavorite(
                    ruWord,
                    uzWord,
                    wordType,
                    example
            );

            return true;
        }
    }
    public Cursor getAllFavorites() {

        createFavoriteTable();

        SQLiteDatabase db =
                getFavoriteDatabase();

        return db.query(
                "favorites",
                new String[]{
                        "ru_word",
                        "uz_word",
                        "word_type",
                        "example"
                },
                null,
                null,
                null,
                null,
                "ru_word ASC"
        );
    }
    public void deleteAllFavorites() {

        createFavoriteTable();

        SQLiteDatabase db =
                getFavoriteDatabase();

        db.delete(
                "favorites",
                null,
                null
        );

        db.close();
    }
}