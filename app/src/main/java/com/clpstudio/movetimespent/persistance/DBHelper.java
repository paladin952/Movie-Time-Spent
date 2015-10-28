package com.clpstudio.movetimespent.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lclapa on 10/28/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * The name of the database
     */
    private static final String DATABASE_NAME = "movie_time_spent.db";

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Currency table name
     */
    public static final String TABLE_TV_SHOW_NAME = "tv_show";

    /**
     * The id column name
     */
    public static final String COLUMN_ID = "_id";

    /**
     * The states column name
     */
    public static final String COLUMN_SHOW_NAME = "Name";

    /**
     * The column for seasons number
     */
    public static final String COLUMN_SHOW_SEASONS_NUMBER = "Seasons";

    /**
     * The values column name
     */
    public static final String COLUMN_SHOW_TIME_SPENT = "Time";

    /**
     * The mInstance of DBHelper for singletone
     */
    private static DBHelper mInstance = null;

    /**
     * Query for creating table
     */
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_TV_SHOW_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_SHOW_NAME
            + " text, " + COLUMN_SHOW_SEASONS_NUMBER
            + "text, "
            + COLUMN_SHOW_TIME_SPENT + " text);";


    /**
     * Private constructor for singleton
     * @param context The context
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get the mInstance of the helper
     * @param context The context
     * @return This
     */
    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    /**
     * Create the database
     * @param db The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Upgrade the database
     * @param db The database
     * @param oldVersion Old version number
     * @param newVersion New version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TV_SHOW_NAME);
        onCreate(db);
    }
}
