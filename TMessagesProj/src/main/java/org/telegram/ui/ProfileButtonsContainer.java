package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

@SuppressLint("ViewConstructor")
public class ProfileButtonsContainer extends LinearLayout {

    private final Theme.ResourcesProvider resourcesProvider;
    
    private int buttonsBackgroundColor;
    private float expandProgress;

    private ProfileButtonsClickCallBack  profileButtonsClickCallBack;


    public ProfileButtonsContainer(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
    }

    public ProfileButtonsContainer(Context context, @Nullable AttributeSet attrs, Theme.ResourcesProvider resourcesProvider) {
        super(context, attrs);
        this.resourcesProvider = resourcesProvider;
    }

    public ProfileButtonsContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Theme.ResourcesProvider resourcesProvider) {
        super(context, attrs, defStyleAttr);
        this.resourcesProvider = resourcesProvider;
    }


    public void setProfileButtonsClickCallBack(ProfileButtonsClickCallBack profileButtonsClickCallBack) {
        this.profileButtonsClickCallBack = profileButtonsClickCallBack;
    }

    public void setButtonsBackgroundColor(int backgroundColor) {
        this.buttonsBackgroundColor = backgroundColor;
    }

    public void setExpandProgress(float progress) {
        if (this.expandProgress != progress) {
            this.expandProgress = progress;
            invalidate();
        }
    }
    
    public ProfileButton addButton(int id, int icon, CharSequence text) {
        return addButtonItem(id, icon,  text, resourcesProvider);
    }    
    
    public ProfileButton addButtonItem(int id, int icon,CharSequence text, Theme.ResourcesProvider resourcesProvider) {
        ProfileButton button = new ProfileButton(getContext(),buttonsBackgroundColor,resourcesProvider);
        button.setTextAndIcon(icon,text);
        button.setTag(id);
        button.setOrientation(LinearLayout.VERTICAL);
        button.setPadding(AndroidUtilities.dp(8f),AndroidUtilities.dp(8f),AndroidUtilities.dp(8f),AndroidUtilities.dp(8f));


        LayoutParams layoutParams= LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT,1f, Gravity.CENTER_VERTICAL);
        layoutParams.setMargins(AndroidUtilities.dp(8f),0,AndroidUtilities.dp(8f),0);
        addView(button, layoutParams);
        button.setOnClickListener(view -> {
            if (profileButtonsClickCallBack!=null && (getScaleX()>0.01f && getScaleY()>0.01f) ){
                profileButtonsClickCallBack.onItemClick((Integer) view.getTag());
            }
        });

        return button;
    }

    public void removeButton(int id) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ProfileButton && id == (int) child.getTag()) {
                removeViewAt(i);
                invalidate();
                return;
            }
        }
    }

    public void updateButton(int id,int icon,CharSequence text) {
        for (int i = 0; i < getChildCount(); i++) {
            View child =getChildAt(i);
            if (child instanceof ProfileButton && id == (int) child.getTag()) {
                ProfileButton profileButton =(ProfileButton) getChildAt(i);
                profileButton.setTextAndIcon(icon,text);
                invalidate();
                return;
            }
        }
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
    }
}