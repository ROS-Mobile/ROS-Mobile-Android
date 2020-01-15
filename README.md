# ROS Android Application for Mobile Robots

The ROS Android Application is designed for dynamic control and visualization for mobile robots. The application uses standard ROS messages (for a list of maintained message types see below). ...

## Requirements

- Android API 15+
- Android Studio V3.4 (for building the apk from the source files)

## Installing Instructions

- Install via Android Studio (make sure you activated the developer options, adb debugging and installed all required drivers for usb and adb)

## Example Usage

Open the application and create a new device

<img src="\images\exampleLayout1.jpeg" style="zoom:33%;" />

You can choose between different devices

<img src="\images\exampleLayout2.jpeg" style="zoom:33%;" />

## Supported ROS Messages

The following Messages are currently supported by the ROS Android Application. Additional Messages will be available in upcoming Versions.

- nav_msgs/OccupancyGrid
- geometry_msgs/Twist

## Contributor / Maintainer

Nico Studt (nico.studt@student.uni-luebeck.de), Nils Rottmann (nils.rottmann@rob.uni-luebeck.de)