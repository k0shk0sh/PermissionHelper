# PermissionHelper
Android Library to help you with your runtime Permissions.

Nexus 6 (M)
=======
<p align="center">
<img alt="Nexus 6" src="https://github.com/k0shk0sh/PermissionHelper/blob/master/art/nexus6.jpg"/>
</p>

Nexus 7 (L)
=======
<p align="center">
<img alt="Nexus 7" src="https://github.com/k0shk0sh/PermissionHelper/blob/master/art/nexus7.jpg"/>
</p>
Nexus 10 (L)
=======
<p align="center">
<img alt="Nexus 10" src="https://github.com/k0shk0sh/PermissionHelper/blob/master/art/nexus10.jpg"/>
</p>


# Installation

```
    compile 'com.github.k0shk0sh:PermissionHelper:1.0.4'
```

Usage
=====

# Ask Permissions in Style

_Have you wondered what will give you a higher chance of letting the user accepts your permission?_

>The answer is simple: **(UI)** that explains why you need to use that particular `permission`. 

* All you need to do is extending <a href="https://github
.com/k0shk0sh/PermissionHelper/blob/master/permission/src/main/java/com/fastaccess/permission/base/activity/BasePermissionActivity
.java">BasePermissionActivity</a>. 

  * By extending `BasePermissionActivity` you'll have control over (**Features**): 
    * Permissions that being asked and their Explanation if its needed.
    * Each `Screen` Background color (`DarkPrimaryColor` of that background will be generated automatically).
    * Each `Screen` Image Resource. 
    * Each `Screen` Title & Message.
    * Each `Screen` Title & Message Text Size.
    * Each `Screen` Text & Message `FontType`, yes you heard me right, each `Screen` can have their own `FontType`.
    * Each `Screen` Next, Previous & Request Buttons Icon Resources. 
    * Your Own `Theme`.
    * Your Own Implementation of `ViewPager.PageTransformer` or use the default one. 
    * You can defined for instance that a particular permission can't be skipped until the Explanation `Dialog` is showed.(follow example code below
     to know 
    how).
    * `BasePermissionActivity` support Portrait & Landscape modes for both Mobile Phones & Tablets _(as showing in above images)_.
    
* And Finally **Let The Library Do The Job For You. in Style.**

> For Better Understanding, please have a look at the example code at <a href="https://github.com/k0shk0sh/PermissionHelper/blob/master/app/src/main/java/com/fastaccess/permission/sample/SamplePagerActivity.java">SamplePagerActivity</a>

# Take Control.

Your ```Activity/Presenter```  should implement ```OnPermissionCallback``` which in return will give you access to

```
    void onPermissionGranted(String[] permissionName);

    void onPermissionDeclined(String[] permissionName);

    void onPermissionPreGranted(String permissionsName);

    void onPermissionNeedExplanation(String permissionName);  
    
    void onPermissionReallyDeclined(String permissionName);//user has ticked don't show again and deny the permission

    void onNoPermissionNeeded(); // fallback to api < M
```

to request a permission all you have to do is:

```
permissionHelper
     .setForceAccepting(false)// true if you had like force reshowing the permission dialog on Deny (not recommended)
     .request(isSingle ? SINGLE_PERMISSION : MULTIPLE_PERMISSIONS);
```

and finally in your `Activity`
```
onRequestPermissionsResult(....)
``` 
call 
``` 
permissionHelper.onRequestPermissionsResult(....)
```

# Extra

```
public static String declinedPermission(@NonNull Context context, @NonNull String[])
```

```
public static String[] declinedPermissions(@NonNull Context context, @NonNull String[] permissions)
```

```
public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission)
```

```
public static boolean isPermissionDeclined(@NonNull Context context, @NonNull String permission)
```

```
public static boolean isExplanationNeeded(@NonNull Activity context, @NonNull String permissionName)
```

```
public static void openSettingsScreen(Context context)//useful when we can't request for the permission due to user ticked don't show again.
```

```
public static boolean permissionExists(@NonNull Context context, @NonNull String permissionName)
```

```
public static boolean isPermissionPermanentlyDenied(@NonNull Activity context, @NonNull String permission)
```

```
public void requestAfterExplanation(String permissionName)// to be used if the permission needs explanation
```

```
public void requestAfterExplanation(String[] permissions)//to be used if the permission needs explanation
```

```
> all of the above static methods you can still access them with PermissionHelper instance.
```



> **To understand more how taking control would look like please go through the <a href="https://github
.com/k0shk0sh/PermissionHelper/tree/master/app/src/main/java/com/fastaccess/permission/sample/SampleActivity.java">sample 
app</a>**

# Why this library?

* Its simple to use.
* Its Unique, Customizable & read back first point. 
* You have two choices, do it your way through `callbacks`, or let the `Library` do it for you with your look & Feel.
* Minimum API is 14, but it'll probably work in API 11 and above, just make sure you test it out.  

> If you're using this library drop me an email at kosh20111@gmail.com to include in the list.

# Dependency

Android Support library ```v23.1.1```


# Images

Images used inside the demo are by <a href="http://www.materialup.com/maxKeppeler">Maximilian Keppeler</a>

# Copyright Notice

Copyright (C) 2013-2015 Kosh.
Licensed under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
license (see the LICENSE file).