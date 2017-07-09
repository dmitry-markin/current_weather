Current Weather
===============

Description
-----------

Simple Android app that shows current outdoor temperature in the system notification. Notification is optionally restricted to show only when device is locked or unlocked. The weather data are retreived using [OpenWeatherMap API.](https://openweathermap.org/api)

Android Permissions
-------------------

```
android.permission.INTERNET
android.permission.RECEIVE_BOOT_COMPLETED
```

Build
-----

Register [OpenWeatherMap account](https://home.openweathermap.org/users/sign_up) and get [API key (APPID).](https://home.openweathermap.org/api_keys) Put the API key into `apikey.gradle` file (see `apikey.gradle.example`.) Install Android Platform 25+, Platform Tools 25+, Build Tools 25+, Support Repository, and build using Gradle or Android Studio.

License
-------

Current Weather is free software. You can redistribute it and/or modify under the terms of [Mozilla Public License v.2.0](https://www.mozilla.org/en-US/MPL/2.0/) or under the terms of any subsequent version of Mozilla Public License. 