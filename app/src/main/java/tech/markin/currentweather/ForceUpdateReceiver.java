/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ForceUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateScheduler.updateAndSchedule(context);
        Toast.makeText(context, context.getResources().getString(R.string.updating_weather_toast),
                       Toast.LENGTH_SHORT)
                .show();
    }
}
