/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenStateMonitoringService extends Service {
    ScreenStateChangeReceiver mScreenStateChangeReceiver = new ScreenStateChangeReceiver();

    public static void start(Context context) {
        context.startService(new Intent(context, ScreenStateMonitoringService.class));
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, ScreenStateMonitoringService.class));
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenStateChangeReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(mScreenStateChangeReceiver);
    }
}
