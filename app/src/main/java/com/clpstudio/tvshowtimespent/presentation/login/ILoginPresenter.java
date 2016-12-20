package com.clpstudio.tvshowtimespent.presentation.login;

import com.clpstudio.tvshowtimespent.general.mvp.abstraction.IBaseMvpPresenter;

/**
 * Created by clapalucian on 12/20/16.
 */

public interface ILoginPresenter extends IBaseMvpPresenter<ILoginPresenter.View> {

    void login(String username, String password);

    public interface View {

        void showError();

        void gotoMainPage();

        void showProgress();

        void hideProgress();
    }

}
