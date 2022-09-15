## Testing Android Projects ##

#### Using a Physical Device

_Unlocking Developer Mode for Device:_

1. Navigate to your device Settings menu
2. Find About Device
3. Locate the Build number and click it 7 times
4. You will receive notice that dev mode has been unlocked
5. Navigate back one menu to About Device to access the new Developer options menu
6. Turn on USB debugging.

NOTE:
* The location of the build number and number of clicks may vary between devices.  If needed you may need to look up the steps.

7. With Android Studio open, attach via USB cable an Android device to your workstation
	* You will need to "eject" your device from the host operating system
	* In the Android Studio VM select Devices → USB and select your device
	* Detailed instructions on this process are available on the desktop of the Android Studio VM in _**"How to connect your device.txt"**_.

8. On your device, press **OK** to allow USB debugging
9. Run the application by clicking Run → Run 'app'
	* You will be presented with the Choose Device screen * Ensure your device is selected and click **OK**

#### Using an Emulator

###### 1a. Android Studio Device Emulator

If you have Android Studio installed on your own computer, you'll want to configure an Android emulator and try running your app on that. This will be very helpful for future testing.

Android Studio comes equipped with built-in device emulators and options for emulating real world devices for testing the applications you develop, to ensure functionality is as you expect it across a range of device types.

To access these emulators run the following steps:

1. Locate the Run menu along the File menu bar along the top of Android Studio and click Run ‘app’
	* You might need to click Stop first
	* You can also run the emulator by clicking the familiar green Play button along the icon toolbar
2. Run the application now, and select "Create New Virtual Device"
3. Create whatever type of virtual device you'd like to try, and run your app on this

###### 1b. Android x86 OS (Lab Computers)

The lab computers do not have an Android Emulator installed by default.  They can be accessed through another virtual machine.

1. From the Linux machine select Applications → FCS VMs → Android OS VM
2. Once the VM is launched follow the instructions as shown on the desktop of the Android Studio VM to connect to it
3. You should be given the option to connect the debugger to the emulator

NOTE:
* This VM isn't as good as an Android Emulator as an emulator lets us simulate things like low battery state and receiving a text message, which this VM doesn't
* It does, however, let us run apps and test some basic functionality
