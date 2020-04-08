package com.frontiertechnologypartners.mykyat_topup;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.frontiertechnologypartners.mykyat_topup.di.component.ApplicationComponent;
import com.frontiertechnologypartners.mykyat_topup.di.component.DaggerApplicationComponent;
import com.frontiertechnologypartners.mykyat_topup.widget.LocaleManager;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;


public class App extends DaggerApplication {
    private static App mInstance;
    private static Resources res;
    // for the sake of simplicity. use DI in real apps instead
    public static LocaleManager localeManager;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        //font override
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/pyidaungsu.ttf");
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return res;
    }

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
    }

    @Override
    protected AndroidInjector<? extends dagger.android.support.DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);

        return component;
    }
}
