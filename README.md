# PermissionHelper
Android Library to help you with your runtime Permissions.


Installation
=====

```
    compile 'com.github.k0shk0sh:PermissionHelper:1.0.3'
```

Usage
=====

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

Extra
======

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
public static void openSettingsScreen(Context context)
```

```
public void requestAfterExplanation(String permissionName)// to be used if the permission needs explanation
```

```
public void requestAfterExplanation(String[] permissions)//to be used if the permission needs explanation
```

```
public boolean isExplanationNeeded(@NonNull String permissionName)
```


> **To understand more please go through the <a href="https://github
.com/k0shk0sh/PermissionHelper/tree/master/app/src/main/java/com/fastaccess/permission/sample">sample 
app</a>**

Why this library?
=====

* Its small (three classes)
* Its simple to use
* And finally its there because there might be someone who's suffering copying the same code again and again in many projects.

Dependency
======

Android Support library ```v23.1.1```


Images
=====

Images used inside the demo are by +Adrian Aisemberg

# Copyright Notice

Copyright (C) 2013-2015 Kosh.
Licensed under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
license (see the LICENSE file).