package tech.markin.currentweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

/**
 * Created by Dmitry on 05.07.2017.
 */

class UpdateScheduler {
    static boolean isAlreadyScheduled(Context context) {
        Intent intent = new Intent(context, UpdateWeatherReceiver.class);
        PendingIntent alarmIntent = PendingIntent
                .getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return alarmIntent != null;
    }

    static void scheduleUpdates(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int interval = Integer.parseInt(
                preferences.getString(SettingsFragment.KEY_PREF_INTERVAL, "0"));

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateWeatherReceiver.class);
        intent.putExtra(UpdateWeatherReceiver.ACTIONS_EXTRA_KEY,
                        UpdateWeatherReceiver.ACTION_UPDATE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (interval != 0) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                             SystemClock.elapsedRealtime() + interval * 60 * 1000,
                                             interval * 60 * 1000, alarmIntent);
        } else {
            alarmManager.cancel(alarmIntent);
            UpdateWeatherService.hideNotification(context);
        }
    }

    static void updateNow(Context context, int actions) {
        Intent intent = new Intent(context, UpdateWeatherReceiver.class);
        intent.putExtra(UpdateWeatherReceiver.ACTIONS_EXTRA_KEY, actions);
        context.sendBroadcast(intent);
    }
}
