# Клиентская части мобильного приложения "Спортик и точка"
"Sportik i Tochka" is an app that will make practicing sports easy and convenient!
<p align="center">
  <table>
    <tr>
      <td align="center">
        <b>Main</b><br>
        <img src="screenshots/gif/main.gif" width="90%">
      </td>
      <td align="center">
        <b>Create reminder</b><br>
        <img src="screenshots/gif/creating_reminder.gif" width="90%">
      </td>
      <td align="center">
        <b>Working with notifications</b><br>
        <img src="screenshots/gif/notifications.gif" width="90%">
      </td>
    </tr>
  </table>
</p>

## Features

* Jogging, swimming and bicycle rides
* Tracking user location and drawing user path on the map
* Displaying a notification with the option to pause a sporting activity to pause it
* Reviewing your sports activities
* Viewing the user rating list
* View detailed statistics on activities and more
* Availability of premium user roles(via fake subscription) and admin roles
* Possibility to ban/unban and issue premium by administrator

## Tech Stack

* Support for Android 8.0+ (SDK 26+)
* Fully Kotlin
* [MVVM](https://developer.android.com/topic/libraries/architecture) for the architecture
* [XML](https://developer.android.com/reference/android/util/Xml) for the UI
* [Material3](https://developer.android.com/jetpack/compose/designsystems/material3) support for dynamic themes using Material 3
* [Koin](https://github.com/InsertKoinIO/koin) for DI pattern
* [Gson](https://github.com/google/gson) for serialization and deserialization
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for async
* [Room](https://developer.android.com/jetpack/androidx/releases/room) for local database
* [Retrofit 2]([https://developer.android.com/jetpack/androidx/releases/room](https://square.github.io/retrofit/)) for network

