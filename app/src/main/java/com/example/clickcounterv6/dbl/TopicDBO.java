package com.example.clickcounterv6.dbl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class TopicDBO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TopicDB1.db";
    public static final String PROFILE_TABLE = "tbl_profile";
    public static final String TOPIC_TABLE = "tbl_topic";

    public TopicDBO(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table " + PROFILE_TABLE + " (profileid integer Primary key autoincrement, profilefname text not null, profilelname text not null)"
            );
            db.execSQL(
                    "create table " + TOPIC_TABLE + "(topicid integer Primary key autoincrement, topicname text unique not null, topiccount integer,profileid integer)"
            );

        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TOPIC_TABLE);
        onCreate(db);
    }

    public boolean insertTopic(String topicName, int topicCount,long profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int flag = 1;
        boolean returnValue= true;

        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select * from "+ TOPIC_TABLE+" ", null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            String tName = res.getString(res.getColumnIndex("topicname"));
            if(topicName.equalsIgnoreCase(tName)) {
                flag = 0;
                break;
            }
            else
                flag=1;

            res.moveToNext();
        }

        if(flag==0)
        {
            returnValue= false;
        }
        else
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("topicname", topicName);
            contentValues.put("topiccount", topicCount);
            contentValues.put("profileid", profileId);
            db.replace(TOPIC_TABLE, null, contentValues);
            returnValue= true;
        }
        return returnValue;
    }

    public String getTopicCount(String tName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery( "select * from "+ TOPIC_TABLE+" where topicname like " + "'" + tName + "%'", null );
            res.moveToFirst();
            String topicCount = res.getString(res.getColumnIndex("topiccount"));

            return topicCount;
        }catch (Exception ex){
            return "";
        }
    }

    public HashMap<String, String> getAllTopics(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from "+ TOPIC_TABLE +" order by topicname desc", null );
        res.moveToFirst();
        HashMap<String, String> map  = new HashMap<>();

        while(res.isAfterLast() == false) {
            String topicName = res.getString(res.getColumnIndex("topicname"));
            String topicCount = res.getString(res.getColumnIndex("topiccount"));
            map.put(topicName, topicCount);
            res.moveToNext();
        }
        return map;
    }

    public boolean updateTopic(String tName,int n,int userId) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query="UPDATE " + TOPIC_TABLE + " SET topiccount = " + n + " WHERE topicname like " + "'" + tName + "%' ";
            db.execSQL(query);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}

