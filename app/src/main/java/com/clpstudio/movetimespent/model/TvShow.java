package com.clpstudio.movetimespent.model;

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
}
