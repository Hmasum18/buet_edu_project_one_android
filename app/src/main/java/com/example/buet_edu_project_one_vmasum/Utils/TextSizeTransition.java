package com.example.buet_edu_project_one_vmasum.Utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextSizeTransition extends Transition {
    public static  final  String TEXTVIEW_FONT = "TextViewTransition:textSize";

    private static final String[] PROPERTIES = { TEXTVIEW_FONT};

    private static final Property<TextView, Float> TEXT_SIZE_PROPERTY =
            new Property<TextView, Float>(Float.class, "textSize") {
                @Override
                public Float get(TextView textView) {
                    return textView.getTextSize();
                }

                @Override
                public void set(TextView textView, Float textSizePixels) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePixels);
                }
            };
    public static final String TAG = "TextResize:";


    @Override
    public String[] getTransitionProperties() {
       // return super.getTransitionProperties();
        return PROPERTIES;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureTransitionValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureTransitionValues(transitionValues);
    }

    public void captureTransitionValues(TransitionValues transitionValues)
    {
        if(! (transitionValues.view instanceof TextView) )
            return;

        TextView textView = (TextView) transitionValues.view;

        float fontSize = textView.getTextSize();  //get the font size in source activity
        transitionValues.values.put(TEXTVIEW_FONT,fontSize);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
      //  return super.createAnimator(sceneRoot, startValues, endValues);
        Log.w(TAG,"start");
        if(startValues == null && endValues == null) ///if there is no view
            return null;

        float startFontSize = (float) startValues.values.get(TEXTVIEW_FONT);
        Log.w(TAG,"start"+startFontSize);
        float endFontSize = (float) endValues.values.get(TEXTVIEW_FONT);
        Log.w(TAG,"end"+endFontSize);
        TextView textView = (TextView) endValues.view;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,startFontSize);
        return ObjectAnimator.ofFloat(textView,TEXT_SIZE_PROPERTY,startFontSize,endFontSize);
    }


}
