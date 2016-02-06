package com.fastaccess.permission.base.model;

import org.parceler.Parcel;

import lombok.Data;

@Data
@Parcel
public class PermissionModel {
    int imageResourceId;
    int layoutColor;
    int textColor, textSize;
    int previousIcon, requestIcon, nextIcon;
    boolean canSkip;
    String title, message, explanationMessage;
    String permissionName;
    String fontType;
}