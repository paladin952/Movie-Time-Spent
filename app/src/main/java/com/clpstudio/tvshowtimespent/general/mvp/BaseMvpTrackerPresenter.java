package com.clpstudio.tvshowtimespent.general.mvp;


import com.clpstudio.tvshowtimespent.general.mvp.abstraction.BaseMvpPresenter;
import com.clpstudio.tvshowtimespent.general.mvp.abstraction.IBaseMvpTrackerPresenter;

public class BaseMvpTrackerPresenter<VIEW> extends BaseMvpPresenter<VIEW> implements IBaseMvpTrackerPresenter {

    @Override
    public void trackPage() {
        //to be implemented in concrete class if needed
    }
}
