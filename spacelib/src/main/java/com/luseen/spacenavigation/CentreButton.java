package com.luseen.spacenavigation;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by Chatikyan on 10.11.2016.
 */

public class CentreButton extends RelativeLayout {

    public CentreButton(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if (!result) {
            if(ev.getAction() == MotionEvent.ACTION_UP) {
                cancelLongPress();
            }
            setPressed(false);
        }
        return result;
    }


}
