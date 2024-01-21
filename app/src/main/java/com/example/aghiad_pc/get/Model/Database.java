package com.example.aghiad_pc.get.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GET_DB";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {

            db.execSQL("CREATE TABLE user\n" +
                    "(\n" +
                    "userName TEXT Not NULL,\n" +
                    "password TEXT NOT NULL,\n" +
                    "name TEXT NOT NULL,\n" +
                    "home TEXT NOT NULL,\n" +
                    "status TEXT NOT NULL,\n" +
                    "type TEXT NOT NULL\n" +
                    ");");

            db.execSQL("CREATE TABLE notification\n" +
                    "(\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "title TEXT Not NULL,\n" +
                    "text TEXT NOT NULL,\n" +
                    "date TEXT DEFAULT CURRENT_TIMESTAMP\n" +
                    ");");

        } catch (Exception ex)
        {
            //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS notification");

            // Create tables again
            onCreate(db);

        } catch (Exception ex) {

        }
    }

    public void saveLogin(String name, String password,String status, String type,String user,String home) {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("SELECT userName FROM user WHERE userName=?", new String[]{name});
            ContentValues values = new ContentValues();
            if (cursor == null || cursor.getCount() == 0 )
            {
                values.put("userName", name);
                values.put("password", password);
                values.put("status", status);
                values.put("type", type);
                values.put("name", user);
                values.put("home", home);
                db.insert("user", null, values);
            }
        } catch (Exception ex) {

        }
        db.close();
    }
    public boolean getIsUserLogged() {
        String result = getSetting();

        if (result == "" || result == null || !result.equals("logged"))
            return false;
        else
            return true;
    }
    public String getSetting() {
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT status FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }

        db.close();
        return result;
    }
    public String getUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT userName FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String getPassword(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT password FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String gettype(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT type FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String getName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT name FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String getHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT home FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }

    public void addNotification(String title,String text,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("text", text);
            values.put("date", date);
            db.insert("notification", null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
    }
    public List<Notification> getNotification(){

        SQLiteDatabase db = this.getWritableDatabase();
        List<Notification> notificationList=new ArrayList<>();
        try
        {
            Cursor cursor = db.rawQuery("SELECT * FROM notification",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return null;
            else if (cursor != null && cursor.moveToFirst())
            {
                int i = 0;
                do
                {
                    Notification noti=new Notification(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                            cursor.getString(3));
                    notificationList.add(noti);
                    i++;
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        return notificationList;
    }
    public int deleteNotification(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete("notification", "id=" + Integer.toString(Id), null);
        db.close();

        return result;
    }
}
