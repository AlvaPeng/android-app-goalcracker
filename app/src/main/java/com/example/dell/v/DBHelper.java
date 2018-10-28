package com.example.dell.v;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mydatabase.db";  //数据库名
    public static final String TABLE_NAME = "goals";    //goals表格名
    public static final String CHALLENGE_TABLE_NAME = "challenges";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TABLE_NAME + "(GoalName varchar(50),StartMonth int,StartDay int,EndMonth int,EndDay int,Icon varchar(50),IconID int, CheckedDays int,LastCheckedMonth int,LastCheckedDay int,CheckedToday int, IsChallenge int)";
        sqLiteDatabase.execSQL(sql);

        String sql_c = "create table if not exists " + CHALLENGE_TABLE_NAME + "(ChallengeName varchar(50), Duration int, IconId int, ChallengeBG int, Task1 varchar(60),Task2 varchar(60),Task3 varchar(60),Task4 varchar(60),Task5 varchar(60),Task6 varchar(60),Task7 varchar(60),Task8 varchar(60),Task9 varchar(60),Task10 varchar(60)," +
                "Task11 varchar(60),Task12 varchar(60),Task13 varchar(60),Task14 varchar(60),Task15 varchar(60),Task16 varchar(60),Task17 varchar(60),Task18 varchar(60),Task19 varchar(60),Task20 varchar(60)," +
                "Task21 varchar(60),Task22 varchar(60),Task23 varchar(60),Task24 varchar(60),Task25 varchar(60),Task26 varchar(60),Task27 varchar(60),Task28 varchar(60),Task29 varchar(60),Task30 varchar(60))";
        sqLiteDatabase.execSQL(sql_c);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);

        String sql_c = "DROP TABLE IF EXISTS " + CHALLENGE_TABLE_NAME;
        sqLiteDatabase.execSQL(sql_c);

        onCreate(sqLiteDatabase);
    }
}
