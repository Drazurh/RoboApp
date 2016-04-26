#RoboApp
Team project for CS 4560

![alt tag](https://scan.coverity.com/projects/8169/badge.svg?flat=1)

The project, led by <a href="https://www.ohio.edu/engineering/about/people/profiles.cfm?profile=zhuj">Dr. Jim Zhu</a>, has the goal of creating a sentient robotic bobcat.
The end product will be a robotic bobcat which can navigate an environment based on sight.  The cat, called Robocat from here, will be able to find food, in the form of a charging station, as well as form affinities for people it is familiar with.  Robocat, beyond visual input, will also  accept audio input.  Robocat will be able to recognize and react to  its own name as well as several other phrases and the volume of the audio.

##Current Progress
Currently, Robocat has basic audio recognition based on the level of the volume.  Robocat also currently has basic color recognition and facial detection provided through <a href="http://opencv.org/">OpenCV.</a>

Also currently available in the app are activities which allow terminal contact with the pololu, for log purposes, and to manually send values to the servos (The servos are what allow the bobcat to maneuver through the environment).

Although many of the basic building blocks are currently available, they are all placed in different activities and not integrated into the Robocat itself. 

##Current Goals
Goals include integrating color detection with facial detection, improving audio recognition (currently it is only based on sound level), and creating an intermediate layer of functions which will allow the communication to the physical components to be better abstracted.
