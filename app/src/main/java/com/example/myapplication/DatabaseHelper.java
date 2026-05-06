package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoritesDB";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_FAVORITES = "favorites";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_USER_ID = "user_id";

    private FirebaseAuth auth;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        auth = FirebaseAuth.getInstance();
    }

    private String getUserId() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return "guest";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_PRICE + " INTEGER,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_USER_ID + " TEXT,"
                + "PRIMARY KEY(" + KEY_ID + ", " + KEY_USER_ID + ")"
                + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
public void addFavorite(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, product.getId());
        values.put(KEY_TITLE, product.getTitle());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_IMAGE_URL, product.getImageUrl());
        values.put(KEY_USER_ID, getUserId());

        db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void removeFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_FAVORITES,
                KEY_ID + "=? AND " + KEY_USER_ID + "=?",
                new String[]{String.valueOf(id), getUserId()});

        db.close();
    }

    public boolean isFavorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{KEY_ID},
                KEY_ID + "=? AND " + KEY_USER_ID + "=?",
                new String[]{String.valueOf(id), getUserId()},
                null, null, null);

        boolean exists = cursor.moveToFirst();

        cursor.close();
        return exists;
    }

    public List<Product> getAllFavorites() {

        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES,
                null,
                KEY_USER_ID + "=?",
                new String[]{getUserId()},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                );
                list.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public void clearUserFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_USER_ID + "=?", new String[]{getUserId()});
        db.close();
    }
}