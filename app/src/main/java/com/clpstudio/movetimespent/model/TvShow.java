package com.clpstudio.movetimespent.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String mNumberOfSeasons;

    /**
     * The total number of episodes
     */
    private int mNumberOfEpisoades;

    /**
     * The url for getting the information
     */
    private String mLastUsedUrl;

    /**
     * The runtime of the episodes per season
     * If more than 1 value than the runtime is different for each season
     */
    private List<String>mEpisodesRunTime;

    /**
     * Store the number of episodes on each season
     */
    private Map<String, String>mSeasonsEpisodes;

    private boolean isShownRemove;

    /**
     * The constructor
     * @param id The id
     * @param name The name
     */
    public TvShow(int id, String name) {
        this.mId = id;
        this.mName = name;
        mEpisodesRunTime = new ArrayList<>();
        mSeasonsEpisodes = new HashMap<>();
    }

    public Map<String, String> getSeasonsEpisodesNumber() {
        return mSeasonsEpisodes;
    }

    public void setSeasonsEpisodes(Map<String, String> mSeasonsEpisodes) {
        this.mSeasonsEpisodes = mSeasonsEpisodes;
    }

    public List<String> getEpisodesRunTime() {
        return mEpisodesRunTime;
    }

    public void setEpisodesRunTime(List<String> mEpisodesRunTime) {
        this.mEpisodesRunTime = mEpisodesRunTime;
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

    public String getNumberOfSeasons() {
        return mNumberOfSeasons;
    }

    public void setSeason(String season){
        mNumberOfSeasons = season;
    }

    public String getLastUsedUrl() {
        return mLastUsedUrl;
    }

    public void setLastUsedUrl(String mLastUsedUrl) {
        this.mLastUsedUrl = mLastUsedUrl;
    }

    public boolean isShownRemove() {
        return isShownRemove;
    }

    public void setIsShownRemove(boolean isShownRemove) {
        this.isShownRemove = isShownRemove;
    }

    public int getNumberOfEpisoades() {
        return mNumberOfEpisoades;
    }

    public void setNumberOfEpisoades(int mNumberOfEpisoades) {
        this.mNumberOfEpisoades = mNumberOfEpisoades;
    }
}
