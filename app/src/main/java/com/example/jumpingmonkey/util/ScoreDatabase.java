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

    // Singleton access
    public static synchronized ScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ScoreDatabase(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
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
        db = helper.getWritableDatabase();
    }

    // Get score by rank (1 = best, 2 = second best, etc.)
    public int getScore(int rank) {
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"score"},
                null,
                null,
                null,
                null,
                "score DESC",
                String.valueOf(rank));

        int result = -1;
        if (cursor.moveToLast()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    // Get name by rank
    public String getName(int rank) {
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"name"},
                null,
                null,
                null,
                null,
                "score DESC",
                String.valueOf(rank));

        String result = null;
        if (cursor.moveToLast()) {
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    // Update or insert high score
    public void updateScore(String name, int score) {
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"score"},
                "name = ?",
                new String[]{name},
                null,
                null,
                null);

        boolean exists = cursor.moveToFirst();
        if (exists) {
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
        cursor.close();
    }
}
