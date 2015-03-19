SAMI WebSockets & geolocation app
===========================

This sample Android app was created to demonstrate how to send geolocation data using [SAMI WebSockets](https://developer.samsungsami.io/sami/sami-documentation/sending-and-receiving-data.html). It also demonstrates how to watch for the location in real time using the same or other devices, and how to get and display on a map the locations in historical mode.

Prerequisites
-------------


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