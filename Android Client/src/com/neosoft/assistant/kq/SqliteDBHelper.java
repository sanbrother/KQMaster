package com.neosoft.assistant.kq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.neosoft.assistant.kq.model.UserInfo;

/**
 * Singleton
 * 
 * @author Lakers
 * 
 */
public class SqliteDBHelper extends SQLiteOpenHelper {
    public final static class UserInfoTable {
        // TABLE INFORMATTION
        public static final String TABLE_NAME = "user_info";
        public static final String USER_ID = "id";
        public static final String USER_NAME = "username";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
    }

    public final static String TAG = "SqliteDBHelper";

    // DATABASE INFORMATION
    static final String DB_NAME = "USER_INFO.DB";
    static final int DB_VERSION = 1;

    // TABLE CREATION STATEMENT
    private static final String CREATE_TABLE = "create table " + UserInfoTable.TABLE_NAME + "("
            + UserInfoTable.USER_ID + " INTEGER PRIMARY KEY, " + UserInfoTable.USER_NAME + " TEXT, "
            + UserInfoTable.PASSWORD + " TEXT, " + UserInfoTable.EMAIL + " TEXT);";

    private static SqliteDBHelper singleInstance;

    private SqliteDBHelper() {
        super(ApplicationEx.getInstance().getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    public synchronized static SqliteDBHelper getInstance() {
        if (singleInstance == null) {
            singleInstance = new SqliteDBHelper();
        }
        return singleInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserInfoTable.TABLE_NAME);
        onCreate(db);
    }

    public void updateArticleList(JSONArray jsonArray) {
        final SQLiteDatabase db = this.getWritableDatabase();

        /* TODO : delete all ? merge ? */
        db.delete(UserInfoTable.TABLE_NAME, null, null);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                Iterator<String> iterator = item.keys();

                ContentValues values = new ContentValues();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = item.get(key);

                    if (value instanceof Integer) {
                        values.put(key, (Integer) value);
                    } else if (value instanceof String) {
                        values.put(key, (String) value);
                    }
                }

                db.insert(UserInfoTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addUserInfo(UserInfo userInfo) {
    	final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        
        values.put(UserInfoTable.USER_NAME, userInfo.getUsername());
        values.put(UserInfoTable.PASSWORD, userInfo.getPassword());
        values.put(UserInfoTable.EMAIL, userInfo.getEmail());
        
        db.insert(UserInfoTable.TABLE_NAME, null, values);
    }

    public List<UserInfo> getUserInfoList() {
        List<UserInfo> list = new ArrayList<UserInfo>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + UserInfoTable.TABLE_NAME + " ORDER BY " + UserInfoTable.USER_ID + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Integer internalID = c.getInt(c.getColumnIndex(UserInfoTable.USER_ID));
                String username = c.getString(c.getColumnIndex(UserInfoTable.USER_NAME));
                String password = c.getString(c.getColumnIndex(UserInfoTable.PASSWORD));
                String email = c.getString(c.getColumnIndex(UserInfoTable.EMAIL));
                
                UserInfo userInfo = new UserInfo(internalID, username, password, email);
                list.add(userInfo);
            } while (c.moveToNext());
        }

        return list;
    }
}
