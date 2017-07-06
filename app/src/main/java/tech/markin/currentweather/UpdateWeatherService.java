package tech.markin.currentweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class UpdateWeatherService extends Service {
    private static final int NOTIFICATION_ID = 1;
    static final String APPID = "949c2afc0a004b0908a6dbfdd4e2c9ef";

    public UpdateWeatherService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String location = preferences.getString(SettingsFragment.KEY_PREF_LOCATION, "");
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" +
                         URLEncoder.encode(location, "UTF-8") +
                         "&APPID=" + APPID;

            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double temp = response.getJSONObject("main").getDouble("temp") - 273.15;
                            String tempString = String.format(Locale.ENGLISH, "%.1f °C", temp);
                            String place = response.getString("name");
                            String country = response.getJSONObject("sys").getString("country");
                            updateNotification(UpdateWeatherService.this, tempString,
                                               "in " + place + ", " + country);
                        } catch (JSONException e) {
                            updateNotification(UpdateWeatherService.this,
                                    getResources().getString(R.string.error),
                                    getResources().getString(R.string.unknown_error));
                        }

                        stopSelf(startId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String message;
                            if (error instanceof NoConnectionError) {
                                message = getResources().getString(R.string.no_connection_error);
                            } else if (error instanceof NetworkError) {
                                message = getResources().getString(R.string.network_error);
                            } else if (error instanceof TimeoutError) {
                                message = getResources().getString(R.string.timeout_error);
                            } else if (error instanceof ServerError) {
                                message = getResources().getString(R.string.server_error);
                            } else if (error.networkResponse != null) {
                                try {
                                    String body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject response = new JSONObject(body);
                                    message = response.getString("message");
                                } catch (JSONException e) {
                                    message = getResources().getString(R.string.unknown_error);
                                }
                            } else {
                                message = getResources().getString(R.string.unknown_error);
                            }

                            updateNotification(UpdateWeatherService.this,
                                    getResources().getString(R.string.error), message);

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        stopSelf(startId);
                    }
                });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjRequest);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static void updateNotification(Context context, CharSequence title, CharSequence text) {
        Notification notification  = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        NotificationManager notificationMgr =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.notify(NOTIFICATION_ID, notification);
    }

    public static void hideNotification(Context context) {
        NotificationManager notificationMgr =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.cancel(NOTIFICATION_ID);
    }
}
