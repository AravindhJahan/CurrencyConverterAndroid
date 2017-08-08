package com.falconnect.dealermanagementsystem.FontAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class MyCheckBox extends CheckBox {

    // Constructors
    public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyCheckBox(Context context) {
        super(context);
        init();
    }

    // This class requires myfont.ttf to be in the assets/fonts folder
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/robotoregular.ttf");
        setTypeface(tf);
    }
}