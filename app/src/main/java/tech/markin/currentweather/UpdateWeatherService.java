/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
    static final String APPID = BuildConfig.API_KEY;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        try {
            final Preferences prefs = new Preferences(this);
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" +
                         URLEncoder.encode(prefs.location(), "UTF-8") +
                         "&APPID=" + APPID +
                         "&units=" + prefs.units();

            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double temp = response.getJSONObject("main").getDouble("temp");
                            String format = prefs.units().equals("metric") ? "%.1f °C" : "%.0f °F";
                            String tempString = String.format(Locale.ENGLISH, format, temp);
                            WeatherNotification.setWeather(UpdateWeatherService.this, tempString);

                            // Update location preference according to server's response
                            String place = response.getString("name");
                            String country = response.getJSONObject("sys").getString("country");
                            prefs.setLocation(place + ", " + country);

                        } catch (JSONException e) {
                            WeatherNotification.setError(UpdateWeatherService.this,
                                         getResources().getString(R.string.parsing_error) + " " +
                                         getResources().getString(R.string.update_now_hint));
                        }

                        if (shouldShowNow()) {
                            WeatherNotification.show(UpdateWeatherService.this);
                        }

                        stopSelf(startId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorStr;
                            if (error instanceof NoConnectionError) {
                                errorStr = getResources().getString(R.string.no_connection_error);
                            } else if (error instanceof NetworkError) {
                                errorStr = getResources().getString(R.string.network_error);
                            } else if (error instanceof TimeoutError) {
                                errorStr = getResources().getString(R.string.timeout_error);
                            } else if (error instanceof ServerError) {
                                errorStr = getResources().getString(R.string.server_error);
                            } else if (error.networkResponse != null) {
                                try {
                                    String body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject response = new JSONObject(body);
                                    errorStr = response.getString("message");
                                } catch (JSONException e) {
                                    errorStr = getResources().getString(R.string.unknown_error);
                                }
                            } else {
                                errorStr = getResources().getString(R.string.unknown_error);
                            }
                            WeatherNotification.setError(UpdateWeatherService.this, errorStr + " "
                                         + getResources().getString(R.string.update_now_hint));
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        if (shouldShowNow()) {
                            WeatherNotification.show(UpdateWeatherService.this);
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

    private boolean isScreenLocked() {
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    private boolean shouldShowNow() {
        Preferences prefs = new Preferences(this);
        return isScreenLocked() ? prefs.showLocked() : prefs.showUnlocked();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
