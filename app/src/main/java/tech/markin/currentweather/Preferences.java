/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Preferences {
    static final String KEY_PREF_LOCATION = "pref_location";
    static final String KEY_PREF_UNITS = "pref_units";
    static final String KEY_PREF_INTERVAL = "pref_interval";
    static final String KEY_PREF_SHOW_LOCKED = "pref_show_locked";
    static final String KEY_PREF_SHOW_UNLOCKED = "pref_show_unlocked";
    static final String KEY_PREF_CREDITS = "pref_credits";

    private SharedPreferences mPreferences;

    Preferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    Preferences(SharedPreferences sharedPrefs) {
        mPreferences = sharedPrefs;
    }

    String location() {
        return mPreferences.getString(KEY_PREF_LOCATION, "");
    }

    void setLocation(String location) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Preferences.KEY_PREF_LOCATION, location);
        editor.apply();
    }

    String units() {
        return mPreferences.getString(KEY_PREF_UNITS, "metric");
    }

    int interval() {
        return Integer.parseInt(mPreferences.getString(KEY_PREF_INTERVAL, "0"));
    }

    boolean showLocked() {
        return mPreferences.getBoolean(KEY_PREF_SHOW_LOCKED, true);
    }

    boolean showUnlocked() {
        return mPreferences.getBoolean(KEY_PREF_SHOW_UNLOCKED, false);
    }
}
