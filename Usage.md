# Short version #

To install and use Fermata, download both the Fermata.apk and the Fermata.jar and open/run them on your Android and PC respectively.

# Details #

## Android ##
Installation: Download the APK from the project website on your Android device (http://code.google.com/p/rpi-fermata/downloads/list). Once it has been downloaded select the APK and click install on the following page.

### Usage ###
> Launch the application. Connect in one of two methods described below. Once connected, press the menu button again two new options are available. Select X filter, and Select Y filter. Select the filters as you see fit. Now just drag your finger around the screen to change filter parameters - happy filtering.

#### Bluetooth ####
> For a Bluetooth connection ensure your device is paired with your computer. Select the Bluetooth option. A list will appear with all paired computers. Select the computer that is running the PC tool. A message notifying the success (or failure) of the connection will appear in a few seconds.

#### TCP/IP ####
> For a TCP/IP connection ensure both devices are on the same network. Select the connect via IP option. Enter the IP (or domain) of the server followed by a colon and the port. Example: `<128.0.0.1>:<9876>`. A message notifying the success (or failure) of the connection will appear in a few seconds.


## PC ##
Installation: Download the .jar file from the Fermata project website (http://code.google.com/p/rpi-fermata/downloads/list). Make sure that you have the most recent version of Java on your computer. To run the application:

> In terminal use the command:
> java -jar Fermata\ v1p0.jar

> In Windows:
> double click the .jar file

### Usage ###
Launch the PC application to start the server (NOTE: an Android phone cannot connect unless the server is running on a PC) Once running, different actions can be performed as listed below.

#### MIDI ####
Connect a USB MIDI device (keyboard) to the PC. Select the MIDI device you wish to use in the devices list. Once selected, check the “Enable MIDI” box in order to start MIDI playback using your MIDI device.

#### OSC Pass-through ####
Enable this feature by checking the “Enable OSC pass-through” box. Once enabled, fill in the port that you will be using for Open Sound Control messages. The default is 7777. The default address is ‘localhost’. If you wish to use a different address, un-check the “Use LocalHost” box, and fill in the IP address you would like to use.

#### Sound File Playback ####
First click the ‘Browse’ button to select a sound file (must be a mono signed 16 bit .wav file). Once selected, press the ‘Play’ button to play back the file. To stop playback of the file, click the ‘Stop’ button.

#### Microphone Playthough ####
Check the ‘Microphone (line in) playthrough enabed?’ box to enable microphone playthough. Make sure that your PC’s microphone 		drivers are current and functioning first.