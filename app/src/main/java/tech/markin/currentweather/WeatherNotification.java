/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

class WeatherNotification {
    static final int WEATHER_NOTIFICATION_ID = 1;
    private static final String LAST_WEATHER_SHARED_PREFS = "tech.markin.currentweather_LastWeather";
    private static final String WEATHER_KEY = "weather";
    private static final String ERROR_KEY = "error";
    private static final String WHEN_KEY = "when";

    static void setWeather(Context context, String weatherLine) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastWeather.edit();
        editor.putString(WEATHER_KEY, weatherLine);
        editor.putString(ERROR_KEY, "");  // clear error
        editor.putLong(WHEN_KEY, System.currentTimeMillis());
        editor.apply();
    }

    static void setError(Context context, String errorLine) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastWeather.edit();
        editor.putString(ERROR_KEY, errorLine);
        editor.apply();
    }

    static void show(Context context) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        String weather = lastWeather.getString(WEATHER_KEY, "");
        String error = lastWeather.getString(ERROR_KEY, "");
        long when = lastWeather.getLong(WHEN_KEY, 0);

        Intent forceUpdateIntent = new Intent(context, ForceUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, forceUpdateIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(weather.isEmpty() ? "--- °C" : weather)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setWhen(when);

        if (!error.isEmpty()) {
            builder.setContentText(error);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(error));
        }

        Notification notification = builder.build(); // we need to support API v.15
        notification.flags |= Notification.FLAG_NO_CLEAR;

        NotificationManager notificationMgr =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.notify(WEATHER_NOTIFICATION_ID, notification);
    }

    static void hide(Context context) {
        NotificationManager notificationMgr =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.cancel(WEATHER_NOTIFICATION_ID);
    }
}
