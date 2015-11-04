package com.clpstudio.tvshowtimespent.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.clpstudio.tvshowtimespent.Utils.UrlConstants;
import com.clpstudio.tvshowtimespent.model.TvShow;
import com.clpstudio.tvshowtimespent.ui.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lclapa on 10/26/2015.
 */
public class DatabaseSuggestionsRetriever {

    /**
     * The context
     */
    private Context mContext;

    /**
     * When finish getting info from server
     */
    public interface OnNetworkLoadFinish {
        void onNetworkLoadFinish(TvShow show);
    }


    /**
     * The constructor
     *
     * @param context The constructor
     */
    public DatabaseSuggestionsRetriever(Context context) {
        mContext = context;
    }

    public List<TvShow> getTvShowsSuggestions(String name) {
        String url = UrlConstants.SEARCH_TV_BASE + name + UrlConstants.API_KEY_PARAMETER;
        url = url.replace(" ", "%20");
        final List<TvShow> data = new ArrayList<>();

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, null, future, future);
        VolleyRequestQueueSingletone.getInstance(mContext).addToRequestQueue(request);

        JSONObject response = null;
        try {
            future.get(10, TimeUnit.SECONDS); //block for maximum 10 seconds
            response = future.get();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //no connection, anyway the user will ge no result toast
        }

        if (response != null) {
            try {
                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    int id = json.getInt("id");
                    String title = json.getString("original_name");
                    data.add(new TvShow(id, title));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void getTvShowById(final String id, final OnNetworkLoadFinish listener) {

        final String url = UrlConstants.FIND_SHOW_BY_ID + id + "?api_key=" + UrlConstants.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String posterPath = jsonObject.getString("backdrop_path");
                    String name = jsonObject.getString("original_name");
                    String seasons = String.valueOf(jsonObject.getInt("number_of_seasons"));
                    int numberOfEpisodes = jsonObject.getInt("number_of_episodes");
                    TvShow show = new TvShow(Integer.parseInt(id), name);

                    //set number of episodes for each season
                    int c =0;
                    JSONArray jsonArray = jsonObject.getJSONArray("seasons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        int seasonNumber = json.getInt("season_number");

                        //We don;t care about seasons 0
                        if (seasonNumber != 0) {
                            String episodeCount = String.valueOf(json.getInt("episode_count"));
                            show.getSeasonsEpisodesNumber().put(String.valueOf(c), episodeCount);
                            c++;
                        }
                    }

                    //set runtime for episodes on each season
                    JSONArray jsonEpisodesRuntimeArray = jsonObject.getJSONArray("episode_run_time");
                    for (int i = 0; i < jsonEpisodesRuntimeArray.length(); i++) {
                        String runtime = String.valueOf(jsonEpisodesRuntimeArray.getString(i));
                        show.getEpisodesRunTime().add(runtime);
                    }

                    show.setPosterUrl(posterPath);
                    show.setSeasonsNumber(seasons);
                    show.setNumberOfEpisoades(numberOfEpisodes);
                    listener.onNetworkLoadFinish(show);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("luci", "ERRROR" + error.getMessage());
            }
        });
        VolleyRequestQueueSingletone.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
