# ROS-Mobile

ROS-Mobile is an [Android](https://www.android.com/) application designed for dynamic control and visualization of mobile robotic system operated by the Robot Operating System ([ROS](http://wiki.ros.org/)). The application uses ROS nodes initializing publisher and subscriber with standard ROS messages. The overall code architecture pattern is Model View ViewModel ([MVVM]([https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel](https://en.wikipedia.org/wiki/Model–view–viewmodel))), which stabilizes the application and makes it highly customizable. 

- Current stable Version: 1.0

## Requirements

- Mobile Android Device with Android Version 5.0 (Lollipop) or higher
- Android Studio Version 3.6.1 or higher (if built from source)

## Installing Instructions

There are three different ways for installing ROS-Mobile onto your mobile device: Built from source, install current apk, download from Google Playstore. We highly recommend to use the download function from the Google Playstore.

##### Built from Source:

- Install Android Studio Version 3.6.1 or higher
- Download the complete repository (Master Branch) and open it via Android Studio
- Built the Code (Make Project Button), connect your mobile device to your PC and install the Software (Run 'app'). Make sure you activated the developer options and adb debugging on your mobile device and installed all required drivers for usb and adb.

##### Install current APK:

- Download the current ROS-Mobile version as apk file and store it in an easy-to-find location onto your mobile device
- Allow third-party apps on your device. Therefore go to **Menu > Settings > Security** and check **Unknown Sources** 
- Go now to the apk file, tap it, then hit install

##### Download from Google Playstore:

- Simply go to the Google Playstore Website of the ROS-Mobile app and download it. It will be installed automatically

## Currently available ROS Nodes

The following Nodes are currently supported by ROS-Mobile. For a comprehensive overview over the functionality of each node have a look into [Nodes Description]() in the wiki. Additional Nodes will be available in upcoming Versions or can be easily added independently. See the [How to add your own Nodes]() section in the wiki. 

- Joystick (geometry_msgs/Twist)
- GridMap (nav_msgs/OccupancyGrid)
- Camera (sensor_msgs/Image)
- Gps (sensor_msgs/NavSatFix)

## Short Example Usage

<p float="left" align="middle">
  <img src="/images/ShortExample01.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample02.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample03.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample04.jpg" width="200 hspace="50" />
</p>


Manually map an apartment environment using a differential drive robot. Therefore, we connected the application with the ROS master running the differential drive robot over wireless LAN by inserting the correct IP address in the *MASTER* configuration tab, first figure. Adding ROS nodes in the *DETAILS* tab, second figure and third figure, enables the control of the differential drive robot via a joystick method sending *geometry\_msgs/Twist* to a *cmd\_vel* topic and the visualization of the generated occupancy grid map by subscribing to the *map* topic via a gridmap method. In the *VIZ* tab, most right figure, the recorded occupancy grid map is displayed as well as the joystick. The joystick can be used via touch for sending control inputs over the *cmd\_vel* topic to the differential drive robot. For a more detailed examples, we refer to our [wiki]().

## Contributor / Maintainer

[Nico Studt](), [Nils Rottmann](https://nrottmann.github.io/)
