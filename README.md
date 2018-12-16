# AndroidHrwInfo
[![](https://jitpack.io/v/cyclonesword/android-hrwinfo.svg)](https://jitpack.io/#cyclonesword/android-hrwinfo)

AndroidHrwInfo is a utility library that helps developers to know the internal hardware features of an Android device.
It is divided in 5 major categories :

  - CPU
  - GPU
  - Battery
  - Device
  - SystemInfo

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
implementation 'com.github.cyclonesword:android-hrwinfo:0.8.1-BETA'
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
    <version>0.8.1-BETA</version>
</dependency>
```

## Usage

#### CPU Frequency
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