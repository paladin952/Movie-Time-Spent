package com.clpstudio.tvshowtimespent.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.clpstudio.tvshowtimespent.model.DbTvShow;
import com.clpstudio.tvshowtimespent.persistance.DatabaseDAO;

import java.util.Collections;
import java.util.List;

/**
 * Created by lclapa on 10/28/2015.
 */
public class DatabaseLoader extends AsyncTaskLoader<List<DbTvShow>> {
    public static final int LOADER_ID = 1;
    protected List<DbTvShow> data;
    protected Context context;

    /**
     * The constructor
     * @param context The context
     */
    public DatabaseLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<DbTvShow> loadInBackground() {
        data = query();
        /**show the items based on the order chosen by the user and stored in database*/
        Collections.sort(data);
        return data;
    }

    /**
     * Query the database for all objects
     * @return
     */
    public List<DbTvShow> query() {
        DatabaseDAO dao = new DatabaseDAO(context);
        dao.open();
        return dao.getAllShows();
    }

    @Override
    public void deliverResult(List<DbTvShow> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        List<DbTvShow> oldData = data;
        this.data = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != this.data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged() || data == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();
        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (data != null) {
            releaseResources(data);
            data = null;
        }

//        // The Loader is being reset, so we should stop monitoring for changes.
//        if (mObserver != null) {
//            // TODO: unregister the observer
//            mObserver = null;
//        }
    }

    @Override
    public void onCanceled(List<DbTvShow> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }


    private void releaseResources(List<DbTvShow> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }
}
