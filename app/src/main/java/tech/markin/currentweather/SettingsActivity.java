package tech.markin.currentweather;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by Dmitry on 05.07.2017.
 */

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (!UpdateScheduler.isAlarmScheduled(this)) {
            UpdateScheduler.updateAndSchedule(this);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
