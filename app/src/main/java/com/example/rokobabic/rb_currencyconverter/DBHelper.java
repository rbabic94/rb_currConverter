package com.example.rokobabic.rb_currencyconverter;

/**
 * Created by Roko Babic on 7.9.2016..
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "values.db";
    public static final String VALUTE_TABLE_NAME = "value";
    public static final String VALUTE_COLUMN_ID = "id";
    public static final String VALUTE_COLUMN_NAME = "value_names";
    public static final String VALUTE_COLUMN_DATE = "review_date";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table value " +
                        "(id integer primary key, value_names text, review_date text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS value");
        onCreate(db);
    }

    public boolean insertValue (String name, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value_names", name);
        contentValues.put("review_date", date);
        db.insert("value", null, contentValues);
        return true;
    }

    public Cursor getData(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from value where review_date='"+date+"'", null );
        return res;
    }

}
