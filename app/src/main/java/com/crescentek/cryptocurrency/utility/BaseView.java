package com.crescentek.cryptocurrency.utility;

public interface BaseView {

    void showProgress(String message);

    void hideProgress();

    void showError(String message);
}
