package tech.markin.currentweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Dmitry on 08.07.2017.
 */

class WeatherNotification {
    static final int WEATHER_NOTIFICATION_ID = 1;
    private static final String LAST_WEATHER_SHARED_PREFS = "tech.markin.currentweather_LastWeather";
    private static final String TITLE_KEY = "title";
    private static final String TEXT_KEY = "text";

    static void setWeatherLine(Context context, String weatherLine) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastWeather.edit();
        editor.putString(TITLE_KEY, weatherLine);
        editor.apply();
    }

    static void setErrorLine(Context context, String errorLine) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastWeather.edit();
        editor.putString(TEXT_KEY, errorLine);
        editor.apply();
    }

    static void show(Context context) {
        SharedPreferences lastWeather = context.getSharedPreferences(
                LAST_WEATHER_SHARED_PREFS, Context.MODE_PRIVATE);
        String title = lastWeather.getString(TITLE_KEY, "");
        String text = lastWeather.getString(TEXT_KEY, "");

        Intent forceUpdateIntent = new Intent(context, ForceUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, forceUpdateIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title.isEmpty() ? "--- °C" : title)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        if (!text.isEmpty()) {
            builder.setContentText(text);
        }

        Notification notification = builder.build();
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