package com.clpstudio.tvshowtimespent.model;

import android.support.annotation.NonNull;

import com.clpstudio.tvshowtimespent.network.model.Season;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lclapa on 10/26/2015.
 */
public class DbTvShow implements Comparable<DbTvShow>, Serializable{

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
    private List<Season> mSeasonsEpisodes;

    /**
     * Boolean to check if remove button is visible
     */
    private boolean isShownRemove;

    /**
     * The total time of the movie in minutes
     */
    private int mMinutesTotalTime;

    /**
     * The position in list
     * important to know after getting the info from database
     */
    private int mPositionInList;

    /**
     * The constructor
     * @param id The id
     * @param name The name
     */
    public DbTvShow(int id, String name) {
        this.mId = id;
        this.mName = name;
        mEpisodesRunTime = new ArrayList<>();
        mSeasonsEpisodes = new ArrayList<>();
        mMinutesTotalTime = 0;
    }

    /**
     * The default constructor
     */
    public DbTvShow() {
        mEpisodesRunTime = new ArrayList<>();
        mSeasonsEpisodes = new ArrayList<>();
        mMinutesTotalTime = 0;
    }

    /**
     * Get the map containing season name and number of episodes seen by user
     * @return
     */
    public List<Season> getSeasonsEpisodesNumber() {
        return mSeasonsEpisodes;
    }

    public void setSeasonsEpisodes(List<Season> mSeasonsEpisodes) {
        this.mSeasonsEpisodes = mSeasonsEpisodes;
    }

    /**
     * Get the list of episodes time
     * @return List<String>
     */
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

    public void setSeasonsNumber(String season){
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

    public int getMinutesTotalTime() {
        return mMinutesTotalTime;
    }

    public void setMinutesTotalTime(int mMinutesTotalTime) {
        this.mMinutesTotalTime = mMinutesTotalTime;
    }

    public int getPositionInList(){
        return mPositionInList;
    }

    public void setPositionInList(int positionInList){
        this.mPositionInList = positionInList;
    }


    /**
     * Compare based on the order chosen by the user and stored in database
     * @param another The other object to be compared to
     * @return -1 for less than the other, 1 for greater than, 0 for equal
     */
    @Override
    public int compareTo(@NonNull DbTvShow another) {
        if(mPositionInList < another.getPositionInList()){
            return -1;
        }else if(mPositionInList > another.getPositionInList()){
            return 1;
        }
        return 0;
    }
}
