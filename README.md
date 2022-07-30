# surface_scanner
An android application implemented by java and built by Gradle as a Cyber Physical System.
It plots surfaces's unevenness(height to distance graph) using embedded sensors.

## Motivation
Surface scanning is a non-invasive method to capture the shape, texture and volume information of a three-dimensional object. In terms of accuracy, it can be as accurate as micro-tomography. Yet, it does not involve any exposure to ionizing radiation.

## Sensors
* `Android Accelometer`: to acquire acceleration in different axises, enabling us to calculate angle with surface by dividing z-axis acceleration to gravity constant
* `Android Gyroscope`: to enhance accuracy of angle calcualtion by accumulation of angular rotation speed given around different axises
* `Android Linear Accelometer`: An accurate accelometer that returns acceleration relevant to the direct path

## Results
* By clicking start, the program starts tracking and plotting.
* Move the phone toward the <a href="https://developer.android.com/guide/topics/sensors/sensors_motion">Y-axis direction</a>. You can highten or lower your phone, and you can observe z-axis fluctuations.
* By re-clicking the button, plottong and tracking stops and the graph freezes its live plot process.

<p align="center">
    <img src="https://user-images.githubusercontent.com/41966479/181920342-ab254528-4995-4b75-a4fa-b3dea26cbd1b.png">
p>

## Systrace
You can track a variety of Android OS operations, system calls, process and threads and activities during execution of a program with Systrace platform tool.
In order to run it, after executing your program via Android Studio, run code below:
```
$ python systrace.py [options] [categories]
```
where a useful option here could be ```-o tracename.html```, and categories should be names of different sensors and drivers that we wish to trace.
<p align="center">
    <img src="https://user-images.githubusercontent.com/41966479/181921042-f129cfba-33d1-4c27-a378-1291b03637c9.png">
p>
