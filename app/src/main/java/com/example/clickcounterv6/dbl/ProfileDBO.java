package com.example.clickcounterv6.dbl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clickcounterv6.model.CMSData;
import com.example.clickcounterv6.vm.MyApplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProfileDBO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TopicDB1.db";
    public static final String PROFILE_TABLE = "tbl_profile";
    public static final String TOPIC_TABLE = "tbl_topic";
    public static final String CMS_TABLE = "tbl_cms";

    public ProfileDBO(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table " + PROFILE_TABLE + " (profileid integer Primary key autoincrement, profilefname text not null, profilelname text not null, profileCellNo text not null)"
            );
            db.execSQL(
                    "create table " + TOPIC_TABLE + "(topicid integer Primary key autoincrement, topicname text unique not null, topiccount integer,profileid integer)"
            );
            db.execSQL(
                    "create table " + CMS_TABLE + "(cmsid integer Primary key autoincrement, cmspage text not null,cmskeyname text unique not null,cmsvalEngname text unique not null )"

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

    public long insertProfile(String fName, String lName,String telNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long profileId = 0;

         ContentValues contentValues = new ContentValues();
         contentValues.put("profilefname", fName);
         contentValues.put("profilelname", lName);
         contentValues.put("profileCellNo", telNo);
         profileId = db.insert(PROFILE_TABLE, "", contentValues) ;

         return profileId;
    }

    public boolean isUserExist() {

        boolean isExist = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor profileCursor = db.rawQuery("select * from " + PROFILE_TABLE + "", null);
        profileCursor.moveToFirst();

        if (null != profileCursor) {
            if (profileCursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            profileCursor.close();
        }
        return isExist;
    }





}
