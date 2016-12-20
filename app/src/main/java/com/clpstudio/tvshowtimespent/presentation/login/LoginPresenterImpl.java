package com.clpstudio.tvshowtimespent.presentation.login;

import android.support.annotation.NonNull;

import com.clpstudio.tvshowtimespent.bussiness.login.Session;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.UserRepository;
import com.clpstudio.tvshowtimespent.general.mvp.abstraction.BaseMvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by clapalucian on 12/20/16.
 */

public class LoginPresenterImpl extends BaseMvpPresenter<ILoginPresenter.View> implements ILoginPresenter {

    private UserRepository userRepository;

    private Session session;

    @Inject
    public LoginPresenterImpl(UserRepository userRepository, Session session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        view.showProgress();
        if (session.isLoggedIn()) {
            view.gotoMainPage();
        }
        view.hideProgress();
    }

    @Override
    public void login(String username, String password) {
        view().showProgress();
        userRepository.login(username, password)
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        //ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view() == null) {
                            return;
                        }
                        view().hideProgress();
                        view().showError();
                    }

                    @Override
                    public void onNext(Boolean isLoggedIn) {
                        if (view() == null) {
                            return;
                        }
                        if (isLoggedIn) {
                            session.setCurrentUser(username);
                            view().gotoMainPage();
                        } else {
                            view().showError();
                        }
                        view().hideProgress();
                    }
                });
    }
}
