package com.clpstudio.tvshowtimespent.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lclapa on 10/26/2015.
 */
public class VolleyRequestQueueSingletone {
    private static VolleyRequestQueueSingletone mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyRequestQueueSingletone(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueueSingletone getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueueSingletone(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
