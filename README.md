# FoursquareNearBy


A mobile app for displaying realtime nearby places around you using Foursquare API 


This app also showcases following the Android Architecture Components working together: [Room](https://developer.android.com/topic/libraries/architecture/room.html), [ViewModels](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html), [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html).


## Screenshots

| Walkthrough | Places list | Empty view | No connection |
|:-:|:-:|:-:|:-:|
| ![First](/art/walkthrow.gif?raw=true) | ![Sec](/art/venues_list.png?raw=true) | ![Third](/art/empty.png?raw=true) | ![Fourth](/art/no_network.png?raw=true) |

Introduction
------------

### Features
App has two operational modes, “Realtime" and “Single Update”.
“Realtime” allows app to always display to the user the current near by
places based on his location, data should be seamlessly updated if user
moved by 500 m from the location of last retrieved pl
aces.
“Single update” mode means the app is updated once in app is launched
and doesn’t update again
User should be able to switch between the two modes, default mode is
“Realtime”. App should remember user choices for next launches

#### Presentation layer
The app uses a Model-View-ViewModel (MVVM) architecture for the presentation layer. Every screen corresponds to a MVVM View. The View and ViewModel communicate using LiveData.

In the view model RxJava is used to handle the network request and map the response to a sutable representation that could be handled by the ui.

#### Data layer
When the network response succeed it writes to a Room database, which exposes a LiveData connection to the view which means the app is offline ready.
The bases of this architecture is https://github.com/android/architecture-components-samples 

#### Third Party Libraries Used

  * [Retrofit][1] Type-safe HTTP client for Android and Java by Square.
  * [Architecture Components][2] help me design robust, testable, and maintainable apps..
  * [BaseRecyclerViewAdapterHelper][3] for making RecyclerViews as simple as possible.
  * [PermissionsDispatcher][4] Simple annotation-based API to handle runtime permissions.
  * [play-services-location][5] facilitate adding location awareness to the app with automated location tracking.
  * [RxJava][6] responsible for coordinating the reload button animation and updating the text on main screen cards periodically.
  * [ReactiveNetwork][7] For listening network to connection state.
  * [KotlinAndroidUtils][8] Common utitlies used in all of my apps.
  * [NetworkErrorView][9] A custom view for displaying a network error view with beautiful and simple animation.

[1]: https://github.com/square/retrofit
[2]: https://developer.android.com/topic/libraries/architecture/
[3]: https://github.com/CymChad/BaseRecyclerViewAdapterHelper
[4]: https://github.com/permissions-dispatcher/PermissionsDispatcher
[5]: https://developers.google.com/android/guides/releases
[6]: https://github.com/ReactiveX/RxJava
[7]: https://github.com/pwittchen/ReactiveNetwork
[8]: https://github.com/humazed/KotlinAndroidUtils/
[9]: https://github.com/humazed/NetworkErrorView


### Reporting Issues

Issues and Pull Requests are welcome.
You can report [here](https://github.com/humazed/FoursquareNearBy/issues).

License
-------

Copyright 2020 humazed.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.



