package com.example.a20201018_applogin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a20201018_applogin.bean.UserInfo;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserDBHelper";
    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    public static final String TABLE_NAME = "user_info";

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private UserDBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public static UserDBHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new UserDBHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    public void closeLink() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        String drop_sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        Log.d(TAG, "drop_sql:" + drop_sql);
        db.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                + "name VARCHAR NOT NULL," + "age INTEGER NOT NULL,"
                + "height LONG NOT NULL," + "weight FLOAT NOT NULL,"
                + "married INTEGER NOT NULL," + "update_time VARCHAR NOT NULL"
                + ",phone VARCHAR" + ",pwd VARCHAR"
                + ");";
        Log.d(TAG, "create_sql:" + create_sql);
        db.execSQL(create_sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        if (newVersion > 1) {
            String alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "add_time VARCHAR;";
            Log.d(TAG, "alter_sql:" + alter_sql);
            db.execSQL(alter_sql);
            alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "is_deleted INTEGER;";
            Log.d(TAG, "alter_sql:" + alter_sql);
            db.execSQL(alter_sql);
        }
    }

    public int delete(String condition) {
        return mDB.delete(TABLE_NAME, condition, null);
    }

    public int deleteAll() {
        return mDB.delete(TABLE_NAME, "1=1", null);
    }

    public long insert(UserInfo info) {
        ArrayList<UserInfo> infoArray = new ArrayList<UserInfo>();
        infoArray.add(info);
        return insert(infoArray);
    }

    public long insert(ArrayList<UserInfo> infoArray) {
        long result = -1;
        for (int i = 0; i < infoArray.size(); i++) {
            UserInfo info = infoArray.get(i);
            ArrayList<UserInfo> tempArray = new ArrayList<UserInfo>();
            if (info.name != null && info.name.length() > 0) {
                String condition = String.format("name='%s'", info.name);
                tempArray = query(condition);
                if (tempArray.size() > 0) {
                    update(info, condition);
                    result = tempArray.get(0).rowid;
                    continue;
                }
            }
            if (info.phone != null && info.phone.length() > 0) {
                String condition = String.format("phone='%s'", info.phone);
                tempArray = query(condition);
                if (tempArray.size() > 0) {
                    update(info, condition);
                    result = tempArray.get(0).rowid;
                    continue;
                }
            }
            ContentValues cv = new ContentValues();
            cv.put("name", info.name);
            cv.put("age", info.age);
            cv.put("height", info.height);
            cv.put("weight", info.weight);
            cv.put("married", info.married);
            cv.put("update_time", info.update_time);
            cv.put("phone", info.phone);
            cv.put("pwd", info.pwd);
            result = mDB.insert(TABLE_NAME, "", cv);
            if (result == -1) {
                return result;
            }
        }
        return result;
    }

    public int update(UserInfo info, String condition) {
        ContentValues cv = new ContentValues();
        cv.put("name", info.name);
        cv.put("age", info.age);
        cv.put("height", info.height);
        cv.put("weight", info.weight);
        cv.put("married", info.married);
        cv.put("update_time", info.update_time);
        cv.put("phone", info.phone);
        cv.put("pwd", info.pwd);
        return mDB.update(TABLE_NAME, cv, condition, null);
    }

    public int update(UserInfo info) {
        return update(info, "rowid=" + info.rowid);
    }

    public ArrayList<UserInfo> query(String condition) {
        String sql = String.format("select rowid,_id,name,age,height,weight,married,update_time," +
                "phone,pwd from %s where %s;", TABLE_NAME, condition);
        Log.d(TAG, "query sql: " + sql);
        ArrayList<UserInfo> infoArray = new ArrayList<UserInfo>();
        Cursor cursor = mDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            UserInfo info = new UserInfo();
            info.rowid = cursor.getLong(0);
            info.sn = cursor.getInt(1);
            info.name = cursor.getString(2);
            info.age = cursor.getInt(3);
            info.height = cursor.getLong(4);
            info.weight = cursor.getFloat(5);
            //SQLite没有布尔型，用0表示false，用1表示true
            info.married = (cursor.getInt(6) == 0) ? false : true;
            info.update_time = cursor.getString(7);
            info.phone = cursor.getString(8);
            info.pwd = cursor.getString(9);
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }

    public UserInfo queryByPhone(String phone) {
        UserInfo info = null;
        ArrayList<UserInfo> infoArray = query(String.format("phone='%s'", phone));
        if (infoArray.size() > 0) {
            info = infoArray.get(0);
        }
        return info;
    }

}