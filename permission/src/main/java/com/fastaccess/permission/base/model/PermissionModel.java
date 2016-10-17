package com.fastaccess.permission.base.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * please use {@link PermissionModelBuilder} for more convince
 */
public class PermissionModel implements Parcelable {

    private String permissionName;
    private int imageResourceId;
    private int layoutColor;
    private int textColor;
    private int textSize;
    private String explanationMessage;
    private int previousIcon;
    private int nextIcon;
    private int requestIcon;
    private boolean canSkip;
    private String message;
    private String title;
    private String fontType;

    public String getPermissionName() {
        return permissionName;

    }

    public void setPermissionName(@NonNull String permissionName) {
        this.permissionName = permissionName;
    }

    @DrawableRes public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(@DrawableRes int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    @ColorInt public int getLayoutColor() {
        return layoutColor;
    }

    public void setLayoutColor(@ColorInt int layoutColor) {
        this.layoutColor = layoutColor;
    }

    @ColorInt public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    @DimenRes public int getTextSize() {
        return textSize;
    }

    public void setTextSize(@DimenRes int textSize) {
        this.textSize = textSize;
    }

    public String getExplanationMessage() {
        return explanationMessage;
    }

    public void setExplanationMessage(@NonNull String explanationMessage) {
        this.explanationMessage = explanationMessage;
    }

    public boolean isCanSkip() {
        return canSkip;
    }

    public void setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
    }

    @DrawableRes public int getRequestIcon() {
        return requestIcon;
    }

    public void setRequestIcon(@DrawableRes int requestIcon) {
        this.requestIcon = requestIcon;
    }

    @DrawableRes public int getPreviousIcon() {
        return previousIcon;
    }

    public void setPreviousIcon(@DrawableRes int previousIcon) {
        this.previousIcon = previousIcon;
    }

    @DrawableRes public int getNextIcon() {
        return nextIcon;
    }

    public void setNextIcon(@DrawableRes int nextIcon) {
        this.nextIcon = nextIcon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getFontType() {
        return fontType;
    }

    /**
     * @param fontType
     *         ex: (fonts/my_custom_text.ttf);
     */
    public void setFontType(@NonNull String fontType) {
        this.fontType = fontType;
    }

    /**
     * please use {@link PermissionModelBuilder} for more convince
     */
    @Deprecated public PermissionModel() {}

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.permissionName);
        dest.writeInt(this.imageResourceId);
        dest.writeInt(this.layoutColor);
        dest.writeInt(this.textColor);
        dest.writeInt(this.textSize);
        dest.writeString(this.explanationMessage);
        dest.writeInt(this.previousIcon);
        dest.writeInt(this.nextIcon);
        dest.writeInt(this.requestIcon);
        dest.writeByte(canSkip ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeString(this.title);
        dest.writeString(this.fontType);
    }

    protected PermissionModel(Parcel in) {
        this.permissionName = in.readString();
        this.imageResourceId = in.readInt();
        this.layoutColor = in.readInt();
        this.textColor = in.readInt();
        this.textSize = in.readInt();
        this.explanationMessage = in.readString();
        this.previousIcon = in.readInt();
        this.nextIcon = in.readInt();
        this.requestIcon = in.readInt();
        this.canSkip = in.readByte() != 0;
        this.message = in.readString();
        this.title = in.readString();
        this.fontType = in.readString();
    }

    public static final Parcelable.Creator<PermissionModel> CREATOR = new Parcelable.Creator<PermissionModel>() {
        public PermissionModel createFromParcel(Parcel source) {return new PermissionModel(source);}

        public PermissionModel[] newArray(int size) {return new PermissionModel[size];}
    };
}
