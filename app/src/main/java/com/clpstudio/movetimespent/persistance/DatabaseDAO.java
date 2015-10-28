package com.clpstudio.movetimespent.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.clpstudio.movetimespent.persistance.model.DBTvShowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lclapa on 10/28/2015.
 */
public class DatabaseDAO {

    /**
     * The mDatabase
     */
    private SQLiteDatabase mDatabase;

    /**
     * The db helper
     */
    private DBHelper mDbHelper;

    /**
     * Array of column names
     */
    private String[] mAllColumnsArray = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_SHOW_NAME,
            DBHelper.COLUMN_SHOW_SEASONS_NUMBER,
            DBHelper.COLUMN_SHOW_TIME_SPENT
    };

    /**
     * The constructor
     * Get the db helper
     *
     * @param context The context
     */
    public DatabaseDAO(Context context) {
        mDbHelper = DBHelper.getInstance(context);
    }

    /**
     * Open the database for writing
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    /**
     * Close the database
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Add an item in database
     *
     * @param state         The state name
     * @param seasonsNumber The number of seasons to be calculated later
     * @param time          The value
     */
    public void addCurrencyItem(String state, String seasonsNumber, String time) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SHOW_NAME, state);
        values.put(DBHelper.COLUMN_SHOW_SEASONS_NUMBER, seasonsNumber);
        values.put(DBHelper.COLUMN_SHOW_TIME_SPENT, time);
        mDatabase.insert(DBHelper.TABLE_TV_SHOW_NAME, null, values);
    }

    /**
     * Upgrade an item based on id
     *
     * @param id    The item id
     * @param state The state name
     * @param value The value of item
     * @return The row where introduced
     */
    public int updateCurrencyItem(long id, String state, String seasonsNumber, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_SHOW_NAME, state); //These Fields should be your String values of actual column names
        contentValues.put(DBHelper.COLUMN_SHOW_SEASONS_NUMBER, seasonsNumber);
        contentValues.put(DBHelper.COLUMN_SHOW_TIME_SPENT, value);
        return mDatabase.update(DBHelper.TABLE_TV_SHOW_NAME, contentValues, "_id " + "=" + id, null);
    }

    /**
     * Get a list of items
     *
     * @return List<DBCurrencyModel>
     */
    public List<DBTvShowModel> getAllShows() {
        List<DBTvShowModel> currencies = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_TV_SHOW_NAME,
                mAllColumnsArray, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBTvShowModel item = cursorToCurrencyItem(cursor);
            currencies.add(item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return currencies;
    }

    /**
     * Get the cursor of the current item
     *
     * @param cursor The cursor
     * @return Item model
     */
    private DBTvShowModel cursorToCurrencyItem(Cursor cursor) {
        DBTvShowModel item = new DBTvShowModel();
        item.setName(cursor.getString(1));
        item.setNumberOfSeasons(cursor.getString(2));
        item.setMinutesTotalTime(cursor.getString(3));
        return item;
    }
}
