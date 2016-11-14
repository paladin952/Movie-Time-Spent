package com.clpstudio.tvshowtimespent.datalayer.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.clpstudio.tvshowtimespent.datalayer.persistance.SuggestionDAO.Columns.COLUMN_NAME;
import static com.clpstudio.tvshowtimespent.datalayer.persistance.SuggestionDAO.Columns.TABLE_NAME;

/**
 * Created by clapalucian on 11/14/16.
 */

public class SuggestionDAO {

    public class Columns {

        public static final String TABLE_NAME = "SuggestionTable";

        public static final String COLUMN_NAME = "Name";

        public static final String COLUMN_TIMESTAMP = "TimeStamp";

    }

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
            Columns.COLUMN_NAME,
            Columns.COLUMN_TIMESTAMP,
    };

    /**
     * The constructor
     * Get the db helper
     *
     * @param context The context
     */
    @Inject
    public SuggestionDAO(Context context) {
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

    public synchronized void addSuggestion(String suggestion) {
        if (suggestionExists(suggestion)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Columns.COLUMN_NAME, suggestion);
            contentValues.put(Columns.COLUMN_TIMESTAMP, System.currentTimeMillis());
            mDatabase.update(TABLE_NAME, contentValues, Columns.COLUMN_NAME + "=" + suggestion, null);
        } else {
            ContentValues values = new ContentValues();
            values.put(Columns.COLUMN_NAME, suggestion);
            values.put(Columns.COLUMN_TIMESTAMP, System.currentTimeMillis());
            mDatabase.insert(TABLE_NAME, null, values);
        }
    }

    private boolean suggestionExists(String suggestion) {
        Cursor cursor = mDatabase.query(
                TABLE_NAME,
                new String[]{
                        COLUMN_NAME
                },
                COLUMN_NAME + "= ?",
                new String[]{suggestion},
                null,
                null,
                null,
                null
        );
        int count = cursor.getCount();
        cursor.close();
        return count > 0;

    }

    /**
     * Get a list of items
     *
     * @return List<DBCurrencyModel>
     */
    public List<String> getAllShows() {
        List<String> result = new ArrayList<>();

        Cursor cursor = mDatabase.query(TABLE_NAME,
                mAllColumnsArray, null, null, null, null, Columns.COLUMN_TIMESTAMP + " DESC limit 10");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }


}
