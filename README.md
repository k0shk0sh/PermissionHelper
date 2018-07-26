# PermissionHelper
Android Library to help you with your runtime Permissions.

Demo 
======

**Android M** <a href="https://youtu.be/ypFH4yxjppQ">Watch it in action.</a>
<br/>
**Pre M** <a href="https://www.youtube.com/watch?v=n2dKAu5fR6M">Watch it in action.</a>
Nexus 6 (M)
=======
![Nexus 6](https://raw.github.com/k0shk0sh/PermissionHelper/master/art/nexus6.jpg)

Nexus 7 (L)
=======
![ScreenShot](https://raw.github.com/k0shk0sh/PermissionHelper/master/art/nexus7.jpg)

Nexus 10 (L)
=======
![ScreenShot](https://raw.github.com/k0shk0sh/PermissionHelper/master/art/nexus10.jpg)



# Installation

#### Gradle
```groovy
    compile 'com.github.k0shk0sh:PermissionHelper:1.1.0'
```

#### Maven
```xml
<dependency>
  <groupId>com.github.k0shk0sh</groupId>
  <artifactId>PermissionHelper</artifactId>
  <version>1.1.0</version>
  <type>aar</type>
</dependency>
```

Usage
=====

# Ask Permissions in Style

_Have you wondered what will give you a higher chance of letting the user accepts your permission?_

>The answer is simple: **(Educating UI)** that explains why you need to use that particular `permission`. 

* All you need to do is extending <a href="https://github
.com/k0shk0sh/PermissionHelper/blob/master/permission/src/main/java/com/fastaccess/permission/base/activity/BasePermissionActivity
.java">BasePermissionActivity</a>. 

  * By extending `BasePermissionActivity` you'll have control over (**Features**): 
    * Permissions that being asked and their Explanation if its needed.
    * Each `Screen` Background color (`DarkPrimaryColor` of that background will be generated automatically).
    * Each `Screen` Image Resource. 
    * Each `Screen` Title & Message.
    * Each `Screen` Title & Message Text Color.
    * Each `Screen` Title & Message Text Size.
    * Each `Screen` Text & Message `FontType`, yes you heard me right, each `Screen` can have their own `FontType`.
    * Each `Screen` Next, Previous & Request Buttons Icon Resources. 
    * Your Own `Theme`.
    * Your Own Implementation of `ViewPager.PageTransformer` or use the default one. 
    * You can defined for instance that a particular permission can't be skipped until the Explanation `Dialog` is showed.(follow example code below
     to know 
    how).
    * `BasePermissionActivity` support Portrait & Landscape modes for both Mobile Phones & Tablets _(as showing in above images)_.
    * `SYSTEM_ALERT_WINDOW Permission`  is being automatically handled if you ever pass it along other permissions ;) . 
* And Finally **Let The Library Do The Job For You. in Style.**

**Notice** 
> You still can use the library to explain why you used the permission in your app, the library will never try ask the permission if 
Android is smaller than **M**, it will just do like any Intro library does. as you can see in **Nexus 7 & Nexus 10** screens running Android **L**, 
request button is hidden ;). 

> For Better Understanding, please have a look at the example code at <a href="https://github.com/k0shk0sh/PermissionHelper/blob/master/app/src/main/java/com/fastaccess/permission/sample/SamplePagerActivity.java">SamplePagerActivity</a>

# Take Control.

Your ```Activity/Fragment/Presenter```  should implement ```OnPermissionCallback``` which in return will give you access to

```java
    void onPermissionGranted(String[] permissionName);

    void onPermissionDeclined(String[] permissionName);

    void onPermissionPreGranted(String permissionsName);

    void onPermissionNeedExplanation(String permissionName);  
    
    void onPermissionReallyDeclined(String permissionName);//user has ticked don't show again and deny the permission

    void onNoPermissionNeeded(); // fallback to api < M
```

to request a permission all you have to do is:

- Activity

```java
permissionHelper
     .setForceAccepting(false)// true if you had like force reshowing the permission dialog on Deny (not recommended)
     .request(isSingle ? SINGLE_PERMISSION : MULTIPLE_PERMISSIONS);
```

- Fragment 

```java
permissionFragmentHelper
     .setForceAccepting(false)// true if you had like force reshowing the permission dialog on Deny (not recommended)
     .request(isSingle ? SINGLE_PERMISSION : MULTIPLE_PERMISSIONS);
```

To request a permission without ask for explanation message even if the user press decline:
```java
permissionHelper
     .setSkipExplanation(true)// true if you don't want to show expalanation message
     .request(CAMERA_PERMISSION);


and finally in your `Activity/Fragment`
```java
onRequestPermissionsResult(....)
``` 
call 
```java
permissionHelper.onRequestPermissionsResult(....)
```

# Extra Activity/Fragment

* Depends on whether you using `PermissionHelper` or `PermissionFragmentHelper` you can call these methods

```java
public static String declinedPermission(@NonNull Context/Fragment context, @NonNull String[])
```

```java
public static String[] declinedPermissions(@NonNull Context/Fragment context, @NonNull String[] permissions)
```

```java
public static boolean isPermissionGranted(@NonNull Context/Fragment context, @NonNull String permission)
```

```java
public static boolean isPermissionDeclined(@NonNull Context/Fragment context, @NonNull String permission)
```

```java
public static boolean isExplanationNeeded(@NonNull Activity/Fragment context, @NonNull String permissionName)
```


```java
public static boolean permissionExists(@NonNull Context/Fragment context, @NonNull String permissionName)
```

```java
public static boolean isPermissionPermanentlyDenied(@NonNull Activity/Fragment context, @NonNull String permission)
```

```java
public static void openSettingsScreen(Context/Fragment context)//useful when we can't request for the permission due to user ticked don't show again.
```

```java
@TargetApi(Build.VERSION_CODES.M)
public static boolean isSystemAlertGranted(@NonNull Context/Fragment context)// special case for SYSTEM_ALERT_WINDOW permission.
```


> all of the above static methods you can still access them with PermissionHelper or PermissionFragmentHelper instance.



* To understand more how taking control would look like please go through the 
    - `Activity` <a href="https://github.com/k0shk0sh/PermissionHelper/tree/master/app/src/main/java/com/fastaccess/permission/sample/SampleActivity.java">`SampleActivity`</a>
    - `Fragment` <a href="https://github.com/k0shk0sh/PermissionHelper/tree/master/app/src/main/java/com/fastaccess/permission/sample/SampleFragment.java">`SampleFragment`</a>

# Why this library?

* Its simple to use.
* Its Unique, Customizable & read back first point. 
* You have two choices, do it your way through `callbacks`, or let the `Library` do it for you with your look & Feel.
* Minimum API is 14, but it'll probably work in API 11 and above, just make sure you test it out.  

> If you're using this library drop me an email at kosh20111@gmail.com to include in the list.

# Dependency

Android Support library ``v24.2.1``

CirclePageIndicator by **JakeWharton** (integrated within the library).

# Images

Images used inside the demo are by <a href="http://www.materialup.com/maxKeppeler">Maximilian Keppeler</a>

# Copyright Notice

Copyright (C) 2015 Kosh.
Licensed under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
license (see the LICENSE file).
