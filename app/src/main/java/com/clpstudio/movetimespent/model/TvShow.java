package com.clpstudio.movetimespent.model;

import android.widget.TextView;

/**
 * Created by lclapa on 10/26/2015.
 */
public class TvShow {

    /**
     * The id
     */
    private int mId;

    /**
     * The name
     */
    private String mName;

    /**
     * Url to poster
     */
    private String mPosterUrl;

    /**
     * Number of season
     */
    private String mSeason;

    /**
     * The url for getting the information
     */
    private String mLastUsedUrl;

    /**
     * The constructor
     * @param id The id
     * @param name The name
     */
    public TvShow(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season){
        mSeason = season;
    }

    public String getLastUsedUrl() {
        return mLastUsedUrl;
    }

    public void setLastUsedUrl(String mLastUsedUrl) {
        this.mLastUsedUrl = mLastUsedUrl;
    }
}
