# SAMI sample app SamiLocator
This sample Android app demonstrates how to send geolocation data using [SAMI WebSockets](https://developer.samsungsami.io/sami/sami-documentation/sending-and-receiving-data.html). It also illustrates how to watch for the location in real time using the same or other devices, and how to get and display on a map the locations in historical mode.

Introduction
-------------

The blog post [Quick Apps: Plot Your Location in Real-Time With SAMI](https://blog.samsungsami.io/mobile/development/2015/03/10/quick-apps-plot-your-location-in-real-time-with-sami.html) at http://blog.samsungsami.io/ describes what the app does and how it is implemented.


Installation
-------------

 * Import in Android Studio IDE
 * Create a device of type `DemoGPS` through our User Portal at https://portal.samsungsami.io and make a note of the device ID.
 * Open the newly created device details, and create a device token for it. Make a note of the device token.
 * Edit `SamiLocator/src/main/java/com/samsung/locator/Config.java` and change the value of `DEVICE_ID` and `ACCESS_TOKEN` with the ID of the device you just created and the device token.

More about SAMI
---------------

If you are not familiar with SAMI we have extensive documentation at http://developer.samsungsami.io

The full SAMI API specification with examples can be found at http://developer.samsungsami.io/sami/api-spec.html

We blog about advanced sample applications at http://blog.samsungsami.io/

To create and manage your services and devices on SAMI visit developer portal at http://devportal.samsungsami.io

License and Copyright
---------------------

Licensed under the Apache License. See LICENSE.

Copyright (c) 2015 Samsung Electronics Co., Ltd.