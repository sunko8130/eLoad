package com.frontiertechnologypartners.mykyat_topup.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceUtils {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    @SuppressLint("CommitPrefEdits")
    public PreferenceUtils(Context mContext) {
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsEditor = prefs.edit();
    }

    private SharedPreferences getPrefs() {
        return prefs;
    }

    private SharedPreferences.Editor getPrefsEditor() {
        return prefsEditor;
    }

    public void toPreference(String key, String value) {
        getPrefsEditor().putString(key, value);
        getPrefsEditor().commit();
    }

    public String fromPreference(String key, String value) {
        return getPrefs().getString(key, value);
    }

    public void toPreference(String key, boolean value) {
        getPrefsEditor().putBoolean(key, value);
        getPrefsEditor().commit();
    }

    public boolean fromPreference(String key, boolean value) {
        return getPrefs().getBoolean(key, value);
    }

    public void toPreference(String key, int value) {
        getPrefsEditor().putInt(key, value);
        getPrefsEditor().commit();
    }

    public int fromPreference(String key, int value) {
        return getPrefs().getInt(key, value);
    }

    public void clearPreferences() {
        getPrefsEditor().clear();
        getPrefsEditor().commit();
    }

    public void removePreference(String key) {
        getPrefsEditor().remove(key);
        getPrefsEditor().commit();
    }

}