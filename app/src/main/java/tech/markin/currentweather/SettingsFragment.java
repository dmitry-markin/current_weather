/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment
                              implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Set location summary
        EditTextPreference prefLocation = (EditTextPreference)findPreference(Preferences.KEY_PREF_LOCATION);
        prefLocation.setSummary(prefLocation.getText());

        // Set units summary
        ListPreference prefUnits = (ListPreference)findPreference(Preferences.KEY_PREF_UNITS);
        prefUnits.setSummary(prefUnits.getEntry());

        // Set update interval summary
        ListPreference prefInterval = (ListPreference)findPreference(Preferences.KEY_PREF_INTERVAL);
        prefInterval.setSummary(prefInterval.getEntry());

        // Set Credits item on click listener
        final Preference creditsPref = findPreference(Preferences.KEY_PREF_CREDITS);
        creditsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(creditsPref.getContext(), LegalActivity.class));
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (key.equals(Preferences.KEY_PREF_LOCATION)) {
            preference.setSummary(sharedPreferences.getString(key, ""));
        } else if (key.equals(Preferences.KEY_PREF_UNITS)){
            ListPreference prefUnits = (ListPreference)preference;
            prefUnits.setSummary(prefUnits.getEntry());
        } else if (key.equals(Preferences.KEY_PREF_INTERVAL)) {
            ListPreference prefInterval = (ListPreference)preference;
            prefInterval.setSummary(prefInterval.getEntry());

        } else if (key.equals(Preferences.KEY_PREF_SHOW_LOCKED)
                   || key.equals(Preferences.KEY_PREF_SHOW_UNLOCKED)) {
            Preferences prefs = new Preferences(sharedPreferences);

            if (prefs.showUnlocked() != prefs.showLocked()) {
                ScreenStateMonitoringService.start(findPreference(key).getContext());
            } else {
                ScreenStateMonitoringService.stop(findPreference(key).getContext());
            }

            if (prefs.showUnlocked()) {
                WeatherNotification.show(findPreference(key).getContext());
            } else {
                WeatherNotification.hide(findPreference(key).getContext());
            }
        }

        UpdateScheduler.updateAndSchedule(preference.getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
