package com.clpstudio.tvshowtimespent.persistance.model;

/**
 * Created by lclapa on 10/28/2015.
 */
public class DBTvShowModel {

    /**
     * The id = position from database
     */
    private int mDbId;

    /**
     * The name
     */
    private String mName;

    /**
     * Number of season
     */
    private String mNumberOfSeasons;

    /**
     * The total time of the movie in minutes
     */
    private String mMinutesTotalTime;

    /**
     * Url to poster image
     */
    private String mPosterUrl;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getNumberOfSeasons() {
        return mNumberOfSeasons;
    }

    public void setNumberOfSeasons(String mNumberOfSeasons) {
        this.mNumberOfSeasons = mNumberOfSeasons;
    }

    public String getMinutesTotalTime() {
        return mMinutesTotalTime;
    }

    public void setMinutesTotalTime(String mMinutesTotalTime) {
        this.mMinutesTotalTime = mMinutesTotalTime;
    }

    public int getDbId() {
        return mDbId;
    }

    public void setDbId(int mDbId) {
        this.mDbId = mDbId;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public String getPosterUrl(){
        return mPosterUrl;
    }


}
