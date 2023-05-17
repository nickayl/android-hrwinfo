# AndroidHrwInfo
[![](https://jitpack.io/v/cyclonesword/android-hrwinfo.svg)](https://jitpack.io/#cyclonesword/android-hrwinfo)


AndroidHrwInfo is a utility library that helps developers to know the internal hardware features of an Android device.
It is divided in 5 major categories :

  - CPU
  - GPU
  - Battery
  - Device
  - SystemInfo

If you want an high-level view of the features of this library, you can download the app that is built upon this library:

<a href="https://play.google.com/store/apps/details?id=com.domenicoaiello.devicespecs">
    <img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="drawing" width="150"/>
</a>

## Installation instructions

#### Gradle
First you have to add the jitpack repository to your global build.gradle file:
``` groovy
allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```


Then, add the dependency to your project-local build.gradle :
``` groovy
implementation 'com.github.cyclonesword:android-hrwinfo:1.0.1.RC'
```

#### Maven
Add the jitpack repository to your pom.xml: 
``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
If you have a multi-module project, make sure you add it in your parent pom (the superpom).
<br />

Then, add the dependency to your pom.xml :
``` xml
<dependency>
    <groupId>com.github.cyclonesword</groupId>
    <artifactId>android-hrwinfo</artifactId>
    <version>1.0.1.RC</version>
</dependency>
```

## Usage

### CPU Frequency
##### _Note that the CPU listener does not work on the emulator. Only for real devices._

You can listen to CPU frequency change simply by attaching a listener and invoking the relative start method:
``` java
StringBuilder sb = new StringBuilder();
AndroidHrwInfo
    .getInstance() // Get the singleton instance
    .cpu()  // Get the CPU object
    .setOnFrequencyChangeListener(cores -> { // Add your listener
        sb.replace(0,sb.length(), "[");
        for(CPU.Core core: cores) {
           sb.append(String.format("#%d %d ",core.getCoreNumber(), core.getCurFrequency()));
        }
        sb.append("] Mhz\n");

        Log.d(TAG, sb.toString());
    })
.startCpuFrequencyMonitor(1000); // Don't forget to start the monitor!
```

#### Battery Status
You can get the battery status informations and listen to changes:

``` java
Battery battery = AndroidHrwInfo
                .getInstance()
                .battery(activity);

String capacity = battery.getCapacity();
String chargingStatus = battery.getChargingStatus();
String health = battery.getHealth();
String percentage = battery.getPercentage();
String temperature = battery.getTemperature();
String voltage = battery.getVoltage();
String pluggedState = battery.getPlugged();
String technology = battery.getTechnology();

battery.setOnChangeEventListener(b -> {
    // Invoked on every battery status change
  
    // It gives a formatted value, for example : 38°
    String temp = b.getTemperature();
    
    // If you want a numeric value:
    double tempDouble = Double.parseDouble(b.getTemperature().replace("°C", "").trim());
});
```

#### System information
The SystemInfo interface contains useful information on the android system the device is running:

``` java
SystemInfo systemInfo = AndroidHrwInfo
                .getInstance()
                .systemInfo(this);

systemInfo.getAndroidVersion();
systemInfo.getApiLevel();
systemInfo.getOpenGL();
systemInfo.getBootLoader();
systemInfo.getJavaVM();
systemInfo.getRootAccess();
systemInfo.getSecurityPatchLevel();
systemInfo.getKernelArchitecture();
systemInfo.getBuildId();
systemInfo.getKernelVersion();

// Returns how long the device is running
systemInfo.setSystemUptimeMonitor((days, hours, minutes, seconds) -> {
    System.out.format("The device is running from %d days, %d hours, %d minutes and %d seconds",days,hours,minutes,seconds);
});
```

