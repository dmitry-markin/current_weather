package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenStateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Preferences prefs = new Preferences(context);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (prefs.showLocked()) {
                WeatherNotification.show(context);
            } else {
                WeatherNotification.hide(context);
            }
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            if (prefs.showUnlocked()) {
                WeatherNotification.show(context);
            } else {
                WeatherNotification.hide(context);
            }
        }
    }
}
