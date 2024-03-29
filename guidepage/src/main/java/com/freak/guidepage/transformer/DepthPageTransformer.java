package com.freak.guidepage.transformer;

import android.view.View;

/**
 * @author Freak
 * @date 2019/11/12.
 */
public class DepthPageTransformer extends GuidePageTransformer {
    private float mMinScale = 0.8f;

    public DepthPageTransformer() {
    }

    public DepthPageTransformer(float minScale) {
        setMinScale(minScale);
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setAlpha(0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setAlpha(1);
        view.setTranslationX(0);
        view.setScaleX(1);
        view.setScaleY(1);
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setAlpha(1 - position);
        view.setTranslationX(-view.getWidth() * position);
        float scale = mMinScale + (1 - mMinScale) * (1 - position);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    public void setMinScale(float minScale) {
        if (minScale >= 0.6f && minScale <= 1.0f) {
            mMinScale = minScale;
        }
    }

}