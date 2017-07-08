package tech.markin.currentweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Dmitry on 05.07.2017.
 */

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
