package com.freak.guidepage.transformer;

import android.view.View;

/**
 * @author Freak
 * @date 2019/11/12.
 */
public class FadePageTransformer extends GuidePageTransformer {

    @Override
    public void handleInvisiblePage(View view, float position) {
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setTranslationX(-view.getWidth() * position);
        view.setAlpha(1 + position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setTranslationX(-view.getWidth() * position);
        view.setAlpha(1 - position);
    }

}