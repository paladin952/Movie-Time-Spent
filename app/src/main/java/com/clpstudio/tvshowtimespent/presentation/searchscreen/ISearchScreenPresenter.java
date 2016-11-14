package com.clpstudio.tvshowtimespent.presentation.searchscreen;

import com.clpstudio.tvshowtimespent.general.mvp.abstraction.IBaseMvpPresenter;

/**
 * Created by clapalucian on 11/12/16.
 */

public interface ISearchScreenPresenter extends IBaseMvpPresenter<ISearchScreenPresenter.View> {

    void search(String query);

    public interface View {

    }

}
