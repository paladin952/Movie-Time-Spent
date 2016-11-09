package com.clpstudio.tvshowtimespent.datalayer.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.clpstudio.tvshowtimespent.presentation.model.DbTvShow;
import com.clpstudio.tvshowtimespent.datalayer.persistance.model.DBTvShowModel;

import java.util.ArrayList;
import java.util.List;


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
            DBHelper.COLUMN_SHOW_TIME_SPENT,
            DBHelper.COLUMN_IMAGE_URL,
            DBHelper.COLUMN_POSITION_IN_LIST
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
    public int addTvShowItem(String state, String seasonsNumber, String time, String imageUrl, int positionInList) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SHOW_NAME, state);
        values.put(DBHelper.COLUMN_SHOW_SEASONS_NUMBER, seasonsNumber);
        values.put(DBHelper.COLUMN_SHOW_TIME_SPENT, time);
        values.put(DBHelper.COLUMN_IMAGE_URL, imageUrl);
        values.put(DBHelper.COLUMN_POSITION_IN_LIST, positionInList);
        long lastInsertedId = mDatabase.insert(DBHelper.TABLE_TV_SHOW_NAME, null, values);
        return (int) lastInsertedId;
    }

    public void deleteTvShow(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ID, id);

        int res = mDatabase.delete(DBHelper.TABLE_TV_SHOW_NAME, DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     * Upgrade an item based on id
     *
     * @param id       The item id
     * @param name     The name
     * @param time     The value of item
     * @param imageUrl The url to donwload image
     * @return The row where introduced
     */
    public int updateCurrencyItem(long id, String name, String seasonsNumber, String time, String imageUrl, int positionInList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_SHOW_NAME, name); //These Fields should be your String values of actual column names
        contentValues.put(DBHelper.COLUMN_SHOW_SEASONS_NUMBER, seasonsNumber);
        contentValues.put(DBHelper.COLUMN_SHOW_TIME_SPENT, time);
        contentValues.put(DBHelper.COLUMN_IMAGE_URL, imageUrl);
        contentValues.put(DBHelper.COLUMN_POSITION_IN_LIST, positionInList);
        return mDatabase.update(DBHelper.TABLE_TV_SHOW_NAME, contentValues, "_id " + "=" + id, null);
    }

    /**
     * Get a list of items
     *
     * @return List<DBCurrencyModel>
     */
    public List<DbTvShow> getAllShows() {
        List<DbTvShow> currencies = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_TV_SHOW_NAME,
                mAllColumnsArray, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DbTvShow item = cursorToTvShowItem(cursor);
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
    private DbTvShow cursorToTvShowItem(Cursor cursor) {
        DBTvShowModel item = new DBTvShowModel();
        item.setDbId((int) cursor.getLong(0));
        item.setName(cursor.getString(1));
        item.setNumberOfSeasons(cursor.getString(2));
        item.setMinutesTotalTime(cursor.getString(3));
        item.setPosterUrl(cursor.getString(4));
        item.setPositionInList(cursor.getInt(5));

        //now convert it to DbTvShow type
        DbTvShow resultDbTvShow = new DbTvShow(item.getDbId(), item.getName());
        resultDbTvShow.setMinutesTotalTime(Integer.parseInt(item.getMinutesTotalTime()));
        resultDbTvShow.setSeasonsNumber(item.getNumberOfSeasons());
        resultDbTvShow.setPosterUrl(item.getPosterUrl());
        resultDbTvShow.setPositionInList(item.getPositionInList());
        return resultDbTvShow;
    }

    public void rearangeDataInDb(List<DbTvShow> data) {
        /**If are changes in list must be stored in database*/
        /**recalculate each show the position in db*/
        for (DbTvShow show : data) {
            updateCurrencyItem(show.getId(), show.getName(), show.getNumberOfSeasons(),
                    String.valueOf(show.getMinutesTotalTime()), show.getPosterUrl(), show.getPositionInList());
        }
    }
}
