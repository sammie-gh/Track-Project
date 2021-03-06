package com.apps.norris.paywithslydepay.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.apps.norris.paywithslydepay.R;

/**
 * Created by norrisboateng on 2/1/17.
 */

public class CustomTextView extends AppCompatTextView {
    
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTextColor(getContext().getResources().getColor(R.color.textColor));
        }
    }
}
