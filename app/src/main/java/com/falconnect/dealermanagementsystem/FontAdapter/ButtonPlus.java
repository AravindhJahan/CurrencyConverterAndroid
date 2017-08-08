package com.falconnect.dealermanagementsystem.FontAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Hashtable;

public class ButtonPlus extends Button {

    public ButtonPlus(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.get("fonts/sanz.ttf", context);
        setTypeface(customFont);
    }
}

