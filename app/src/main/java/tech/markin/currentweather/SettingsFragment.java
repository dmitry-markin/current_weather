package tech.markin.currentweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

/**
 * Created by Dmitry on 05.07.2017.
 */

public class SettingsFragment extends PreferenceFragment
                              implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_LOCATION = "pref_location";
    public static final String KEY_PREF_INTERVAL = "pref_interval";
    public static final String KEY_PREF_SHOW_LOCKED = "pref_show_locked";
    public static final String KEY_PREF_SHOW_UNLOCKED = "pref_show_unlocked";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Set location summary
        EditTextPreference prefLocation = (EditTextPreference)findPreference(KEY_PREF_LOCATION);
        prefLocation.setSummary(prefLocation.getText());

        // Set update interval summary
        ListPreference prefInterval = (ListPreference)findPreference(KEY_PREF_INTERVAL);
        prefInterval.setSummary(prefInterval.getEntry());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_LOCATION)) {
            Preference prefLocation = findPreference(key);
            prefLocation.setSummary(sharedPreferences.getString(key, ""));
            UpdateScheduler.updateNow(prefLocation.getContext(),
                                      UpdateWeatherReceiver.ACTION_UPDATE
                                      | UpdateWeatherReceiver.ACTION_SCHEDULE);

        } else if (key.equals(KEY_PREF_INTERVAL)) {
            ListPreference prefInterval = (ListPreference)findPreference(key);
            int index = prefInterval.findIndexOfValue(sharedPreferences.getString(key, ""));
            prefInterval.setSummary(index >= 0
                                        ? prefInterval.getEntries()[index]
                                        : null);
            UpdateScheduler.scheduleUpdates(prefInterval.getContext());
        }
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
