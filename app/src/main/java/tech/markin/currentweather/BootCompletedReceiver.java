package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateScheduler.updateAndSchedule(context);

        Preferences prefs = new Preferences(context);
        if (prefs.showLocked() != prefs.showUnlocked()) {
            ScreenStateMonitoringService.start(context);
        }
    }
}
