/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (!UpdateScheduler.isAlarmScheduled(this)) {
            UpdateScheduler.updateAndSchedule(this);
        }

        Preferences prefs = new Preferences(this);
        if (prefs.showLocked() != prefs.showUnlocked()) {
            ScreenStateMonitoringService.start(this);
        }
        if (prefs.showUnlocked()) {
            WeatherNotification.show(this);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
