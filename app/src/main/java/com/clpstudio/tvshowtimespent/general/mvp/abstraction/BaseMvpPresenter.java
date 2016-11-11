package com.clpstudio.tvshowtimespent.general.mvp.abstraction;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BaseMvpPresenter<VIEW> implements IBaseMvpPresenter<VIEW> {
    private WeakReference<VIEW> view;

    protected void resetState() {
    }

    @Override
    public void bindView(@NonNull VIEW view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    protected VIEW view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    protected boolean setupDone() {
        return view() != null;
    }
}