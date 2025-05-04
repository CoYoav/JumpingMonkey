package com.example.jumpingmonkey.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDatabase {

    private static final String DB_NAME = "highscores.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "players";

    private static ScoreDatabase instance;
    private final SQLiteDatabase db;

    // Singleton pattern
    public static synchronized ScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ScoreDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private ScoreDatabase(Context context) {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                        "name TEXT PRIMARY KEY, " +
                        "score INTEGER NOT NULL)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            }
        };
        this.db = helper.getWritableDatabase();
    }

    // Get score of player at specific rank (1 = highest)
    public int getScore(int rank) {
        String limit = String.valueOf(rank);
        try (Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"score"},
                null,
                null,
                null,
                null,
                "score DESC",
                limit)) {

            return cursor.moveToLast() ? cursor.getInt(0) : -1;
        }
    }

    // Get name of player at specific rank
    public String getName(int rank) {
        String limit = String.valueOf(rank);
        try (Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"name"},
                null,
                null,
                null,
                null,
                "score DESC",
                limit)) {

            return cursor.moveToLast() ? cursor.getString(0) : null;
        }
    }

    // Insert or update player's score
    public void updateScore(String name, int score) {
        try (Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"score"},
                "name = ?",
                new String[]{name},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                int currentScore = cursor.getInt(0);
                if (score > currentScore) {
                    ContentValues values = new ContentValues();
                    values.put("score", score);
                    db.update(TABLE_NAME, values, "name = ?", new String[]{name});
                }
            } else {
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("score", score);
                db.insert(TABLE_NAME, null, values);
            }
        }
    }
}
