package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Dmitry on 08.07.2017.
 */

public class ForceUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateScheduler.updateAndSchedule(context);
        Toast.makeText(context, context.getResources().getString(R.string.updating_weather_toast),
                       Toast.LENGTH_SHORT)
                .show();
    }
}
