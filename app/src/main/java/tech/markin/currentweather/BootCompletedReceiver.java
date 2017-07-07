package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateScheduler.updateNow(context, UpdateWeatherReceiver.ACTION_UPDATE
                                           | UpdateWeatherReceiver.ACTION_SCHEDULE);
    }
}
