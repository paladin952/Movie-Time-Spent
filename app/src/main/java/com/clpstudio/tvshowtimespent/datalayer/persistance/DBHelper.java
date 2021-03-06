package com.clpstudio.tvshowtimespent.datalayer.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    /**
     * The name of the database
     */
    private static final String DATABASE_NAME = "movie_time_spent.db";

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 3;

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
     * The position in list
     */
    public static final String COLUMN_POSITION_IN_LIST = "PositionInList";

    /**
     * Column for images url
     */
    public static final String COLUMN_IMAGE_URL = "ImageUrl";

    /**
     * The mInstance of DBHelper for singletone
     */
    private static DBHelper mInstance = null;

    /**
     * Query for creating table
     */
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_TV_SHOW_NAME +
            "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SHOW_NAME + " text, "
            + COLUMN_SHOW_SEASONS_NUMBER + " text, "
            + COLUMN_SHOW_TIME_SPENT + " text, "
            + COLUMN_IMAGE_URL + " text,"
            + COLUMN_POSITION_IN_LIST + " integer);";

    private static final String CREATE_SUGGESTIONS_TABLE = "create table if not exists "
            + SuggestionDAO.Columns.TABLE_NAME
            + "(" + SuggestionDAO.Columns.COLUMN_NAME + " text, "
            +  SuggestionDAO.Columns.COLUMN_TIMESTAMP + " integer);";

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
        db.execSQL(DATABASE_CREATE + CREATE_SUGGESTIONS_TABLE);
    }

    /**
     * Upgrade the database
     * @param db The database
     * @param oldVersion Old version number
     * @param newVersion New version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

        if (newVersion == 3) {
            db.execSQL(CREATE_SUGGESTIONS_TABLE);
        }
    }
}
