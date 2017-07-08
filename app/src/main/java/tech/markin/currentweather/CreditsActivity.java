/*
 * Copyright © 2017 Dmitry Markin
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tech.markin.currentweather;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CreditsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        WebView webView = (WebView)findViewById(R.id.credits_web_view);
        webView.loadData(getString(R.string.credits_html), "text/html", "UTF-8");
    }
}
