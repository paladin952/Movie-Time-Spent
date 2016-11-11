package com.clpstudio.tvshowtimespent.general.mvp.abstraction;

import android.support.annotation.NonNull;

public interface IBaseMvpPresenter<VIEW> {

    void bindView(@NonNull VIEW view);

    void unbindView();
}
