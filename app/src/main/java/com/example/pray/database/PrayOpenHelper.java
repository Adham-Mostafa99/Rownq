package com.example.pray.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pray.database.PrayContract.MonthPrayEntry;
import com.example.pray.database.PrayContract.DayPrayEntry;


import androidx.annotation.Nullable;

public class PrayOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pray_time.db";
    private static final int DATABASE_VERSION = 1;

    public static final String CREATE_MONTH_PRAY_TABLE =
            "CREATE TABLE " + MonthPrayEntry.TABLE_NAME + "(" +
                    MonthPrayEntry.DAY_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MonthPrayEntry.DATE + " TEXT NOT NULL"
                    + ");";

    public static final String CREATE_DAY_PRAY_TABLE =
            "CREATE TABLE " + DayPrayEntry.TABLE_NAME + "(" +
                    DayPrayEntry.Name_Prayer + " TEXT NOT NULL, " +
                    DayPrayEntry.TIME + " TEXT NOT NULL," +
                    DayPrayEntry.DAY_NUMBER + " INTEGER NOT NULL ," +
                    "FOREIGN KEY ( " + DayPrayEntry.DAY_NUMBER + ")" +
                    "REFERENCES " + MonthPrayEntry.TABLE_NAME + "(" + MonthPrayEntry.DAY_NUMBER + ")"
                    + ");";

    public PrayOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MONTH_PRAY_TABLE);
        sqLiteDatabase.execSQL(CREATE_DAY_PRAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
