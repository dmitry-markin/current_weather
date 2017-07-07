package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateWeatherReceiver extends BroadcastReceiver {
    static final String ACTIONS_EXTRA_KEY = "actions";
    static final int ACTION_UPDATE = 1;
    static final int ACTION_SCHEDULE = 2;
    static final int ACTION_NOTIFY_USER = 4;

    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra(ACTIONS_EXTRA_KEY, 0);
        if ((action & ACTION_UPDATE) != 0) {
            context.startService(new Intent(context, UpdateWeatherService.class));
        }
        if ((action & ACTION_SCHEDULE) != 0) {
            UpdateScheduler.scheduleUpdates(context);
        }
        if ((action & ACTION_NOTIFY_USER) != 0) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.updating_weather_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
