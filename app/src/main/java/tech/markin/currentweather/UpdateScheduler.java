/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

class UpdateScheduler {
    static boolean isAlarmScheduled(Context context) {
        Intent intent = new Intent(context, UpdateAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent
                .getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return alarmIntent != null;
    }

    static void updateAndSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, UpdateAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Preferences prefs = new Preferences(context);
        int interval = prefs.interval();
        if (interval != 0 && (prefs.showLocked() || prefs.showUnlocked())) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                             SystemClock.elapsedRealtime() + interval * 60 * 1000,
                                             interval * 60 * 1000, alarmIntent);
            context.sendBroadcast(intent);
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            WeatherNotification.hide(context);
        }
    }
}
