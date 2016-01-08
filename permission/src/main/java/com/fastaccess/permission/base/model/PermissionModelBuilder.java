package com.fastaccess.permission.base.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.fastaccess.permission.R;


public class PermissionModelBuilder {
    private final Resources res;
    private final PermissionModel permissionModel;

    private PermissionModelBuilder(@NonNull Context context) {
        this.res = context.getResources();
        this.permissionModel = new PermissionModel(); // Generate sane default values
        withTextColor(Color.WHITE);
        withTextSize(res.getDimensionPixelSize(R.dimen.text_size));
        withRequestIcon(R.drawable.ic_arrow_done);
        withPreviousIcon(R.drawable.ic_arrow_left);
        withNextIcon(R.drawable.ic_arrow_right);
    }

    public static PermissionModelBuilder withContext(@NonNull Context context) {
        return new PermissionModelBuilder(context);
    }

    public PermissionModel build() {
        return permissionModel;
    }

    public PermissionModelBuilder withPermissionName(@NonNull String permissionName) {
        this.permissionModel.setPermissionName(permissionName);
        return this;
    }

    public PermissionModelBuilder withImageResourceId(@DrawableRes int imageResourceId) {
        this.permissionModel.setImageResourceId(imageResourceId);
        return this;
    }

    public PermissionModelBuilder withLayoutColor(@ColorInt int layoutColor) {
        this.permissionModel.setLayoutColor(layoutColor);
        return this;
    }

    public PermissionModelBuilder withLayoutColorRes(@ColorRes int layoutColor) {
        this.permissionModel.setLayoutColor(res.getColor(layoutColor));
        return this;
    }

    public PermissionModelBuilder withTextColor(@ColorInt int textColor) {
        this.permissionModel.setTextColor(textColor);
        return this;
    }

    public PermissionModelBuilder withTextColorRes(@ColorRes int textColor) {
        this.permissionModel.setTextColor(res.getColor(textColor));
        return this;
    }

    public PermissionModelBuilder withTextSize(@DimenRes int textSize) {
        this.permissionModel.setTextSize(textSize);
        return this;
    }

    public PermissionModelBuilder withExplanationMessage(@NonNull String explanationMessage) {
        this.permissionModel.setExplanationMessage(explanationMessage);
        return this;
    }

    public PermissionModelBuilder withExplanationMessage(@StringRes int explanationMessage) {
        this.permissionModel.setExplanationMessage(res.getString(explanationMessage));
        return this;
    }

    public PermissionModelBuilder withCanSkip(boolean canSkip) {
        this.permissionModel.setCanSkip(canSkip);
        return this;
    }

    public PermissionModelBuilder withRequestIcon(@DrawableRes int requestIcon) {
        this.permissionModel.setRequestIcon(requestIcon);
        return this;
    }

    public PermissionModelBuilder withPreviousIcon(@DrawableRes int previousIcon) {
        this.permissionModel.setPreviousIcon(previousIcon);
        return this;
    }

    public PermissionModelBuilder withNextIcon(@DrawableRes int nextIcon) {
        this.permissionModel.setNextIcon(nextIcon);
        return this;
    }

    public PermissionModelBuilder withMessage(@NonNull String message) {
        this.permissionModel.setMessage(message);
        return this;
    }

    public PermissionModelBuilder withMessage(@StringRes int message) {
        this.permissionModel.setMessage(res.getString(message));
        return this;
    }

    public PermissionModelBuilder withTitle(@NonNull String title) {
        this.permissionModel.setTitle(title);
        return this;
    }

    public PermissionModelBuilder withTitle(@StringRes int title) {
        this.permissionModel.setTitle(res.getString(title));
        return this;
    }

    /**
     * @param fontType
     *         ex: (fonts/my_custom_text.ttf);
     */
    public PermissionModelBuilder withFontType(@NonNull String fontType) {
        this.permissionModel.setFontType(fontType);
        return this;
    }
}
