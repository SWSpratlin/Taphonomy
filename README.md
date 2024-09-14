# TAPHONOMY (A Digital Interactive Installation) Testing Grounds

This is a testing Ground for "The Rot" (Working Title) using the Sarxos Webcam Capture library. This mostly works. 

Currently running successfully on MacM1 (Multiple Stations) 

Current Problems:

- Importinc processing.core on Pi via Terminal. 

To run, follow these steps based on Platform

## Mac Silicon

1) Install JRE or JDK Minimum Version 17. 22 is preferable, but the whole program works in 17 even though it was written in 22. Nothing breaks by downscaling. Have not tested 8, likely will not.
2) Clone this repo
3) Open Terminal
4) run `cd {/wherever/this/repo/exists/Taphonomy/}`
5) run `mvn clean package dependency:copy-dependencies`
6) run `cd target/classes/`
7) run `java -cp "../Taphonomy-{VERSION}.jar:../dependency/*":. rot.main.Main`
8) Follow the instructions on the Terminal

This runs the program on any Mac Silicon Machine. Pi and Windows is in development. 

## Raspberry Pi (Debian Bookworm, Pi 5)

1) Install GStreamer 1.0 (Latest version, tested on 1.22)
2) Install Maven
3) Clone this repo
4) `cd {repo/location/Taphonomy}`
5) `mvn clean package dependency:copy-dependencies -DskipTests=true`
6) `cd target/classes/`
7) `java -cp "../Taphonomy-{VERSION}.jar:../dependency/*":. rot.main.Main`

## TODO

Features still working on

- [X] Fullscreen
- [X] Pi Functionality
- [X] GStreamer Integration
- [X] Config Handler to run the first time on the machine to choose image files.
- [ ] Windows Driver
- [ ] In-program declaration of Auto-shudown
- [ ] Fix mvnRunLocal version on Pi

## ChangeLog: 

1.0 FIRST STABLE VERSION
    - GStreamer Integration, Fullscreen capabilities, switch to a new source for Processing
    - New updater algorithm using a pseudo-random updater set. Grows slower, but more circular
    - Cleaned up test-cases
    - Added Uptime counter to Utils

0.2.0-SNAPSHOT -  Switch to JavaCV Library. Still not fully functional.
