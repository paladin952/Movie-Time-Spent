package com.clpstudio.movetimespent.persistance.model;

/**
 * Created by lclapa on 10/28/2015.
 */
public class DBTvShowModel {

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
}
