package com.fastaccess.permission.base.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

/**
 * Created by Kosh on 16/11/15 9:20 PM. copyrights @ Innov8tif
 */
public class PermissionModel implements Parcelable {

    private String permissionName;
    private int imageResourceId;
    private int layoutColor;
    private int textColor;
    private int textSize;
    private String explanationText;
    private int skipIcon;
    private int requestIcon;
    private boolean canSkip;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(@NonNull String permissionName) {
        this.permissionName = permissionName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(@IdRes int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getLayoutColor() {
        return layoutColor;
    }

    public void setLayoutColor(@ColorInt int layoutColor) {
        this.layoutColor = layoutColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(@IdRes int textSize) {
        this.textSize = textSize;
    }

    public String getExplanationText() {
        return explanationText;
    }

    public void setExplanationText(@NonNull String explanationText) {
        this.explanationText = explanationText;
    }

    public boolean isCanSkip() {
        return canSkip;
    }

    public void setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
    }

    public int getRequestIcon() {
        return requestIcon;
    }

    public void setRequestIcon(@IdRes int requestIcon) {
        this.requestIcon = requestIcon;
    }

    public int getSkipIcon() {
        return skipIcon;
    }

    public void setSkipIcon(@IdRes int skipIcon) {
        this.skipIcon = skipIcon;
    }

    public PermissionModel() {}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.permissionName);
        dest.writeInt(this.imageResourceId);
        dest.writeInt(this.layoutColor);
        dest.writeInt(this.textColor);
        dest.writeInt(this.textSize);
        dest.writeString(this.explanationText);
        dest.writeInt(this.skipIcon);
        dest.writeInt(this.requestIcon);
        dest.writeByte(canSkip ? (byte) 1 : (byte) 0);
    }

    protected PermissionModel(Parcel in) {
        this.permissionName = in.readString();
        this.imageResourceId = in.readInt();
        this.layoutColor = in.readInt();
        this.textColor = in.readInt();
        this.textSize = in.readInt();
        this.explanationText = in.readString();
        this.skipIcon = in.readInt();
        this.requestIcon = in.readInt();
        this.canSkip = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PermissionModel> CREATOR = new Parcelable.Creator<PermissionModel>() {
        public PermissionModel createFromParcel(Parcel source) {return new PermissionModel(source);}

        public PermissionModel[] newArray(int size) {return new PermissionModel[size];}
    };
}
