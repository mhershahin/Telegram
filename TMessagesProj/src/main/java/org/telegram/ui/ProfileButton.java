package org.telegram.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;


public class ProfileButton extends LinearLayout {

    private final int textColor = Color.WHITE;
    private final int iconColor = ColorUtils.blendARGB(Color.WHITE, Color.BLACK, 0.15f);

    private int backgroundColor;


    private ImageView iconImage;
    public TextView textView;
    GradientDrawable mainBackground;
    private Theme.ResourcesProvider resourcesProvider;

    public ProfileButton(Context context) {
        super(context);
    }

    public ProfileButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ProfileButton(Context context, int buttonsBackgroundColor, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        this.backgroundColor = buttonsBackgroundColor;
        mainBackground = new GradientDrawable();
        mainBackground.setColor(backgroundColor);
        mainBackground.setCornerRadius(40);
        this.setBackground(mainBackground);


        iconImage = new ImageView(getContext());
        iconImage.setScaleType(ImageView.ScaleType.CENTER);
        iconImage.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
        addView(iconImage, LayoutHelper.createLinear(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        textView = new TextView(context);
        textView.setLines(1);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        addView(textView, LayoutHelper.createLinear(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f, Gravity.CENTER));
    }


    public void setTextAndIcon(int icon, CharSequence text) {
        setText(text);
        setInitIconImage(icon);
    }

    private void setText(CharSequence text) {
        if (textView == null) {
            textView = new TextView(getContext());
            textView.setLines(1);
            textView.setSingleLine(true);
            textView.setGravity(Gravity.LEFT);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(getThemedColor(Theme.key_groupcreate_sectionText));
            textView.setVisibility(GONE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            addView(textView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        }
        textView.setText(text);
    }

    private void setInitIconImage(int icon) {
        if (iconImage == null) {
            iconImage = new ImageView(getContext());
            iconImage.setScaleType(ImageView.ScaleType.CENTER);
            iconImage.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
            addView(iconImage, LayoutHelper.createLinear(50, 50, Gravity.CENTER_VERTICAL));
        }
        if (icon == 0) {
            iconImage.setVisibility(View.GONE);
        } else {
            iconImage.setVisibility(View.VISIBLE);
            iconImage.setImageResource(icon);
        }
    }

    private int getThemedColor(int key) {
        return Theme.getColor(key, resourcesProvider);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        updatePressedState(pressed);
    }

    private void updatePressedState(boolean pressed) {
        mainBackground.setCornerRadius(40);
        if (pressed) {
            mainBackground.setColor(ColorUtils.blendARGB(backgroundColor, Color.WHITE, 0.3f));
        } else {
            mainBackground.setColor(backgroundColor);
        }
        this.setBackground(mainBackground);
    }
}