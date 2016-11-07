package com.dudinskyi.oleksandr.firebasechat.presenter;

import com.dudinskyi.oleksandr.firebasechat.view.View;

public abstract class Presenter<T extends View> {
    T view;

    public void addView(T view) {
        this.view = view;
    }

    public abstract void init();

    public abstract void destroy();
}
