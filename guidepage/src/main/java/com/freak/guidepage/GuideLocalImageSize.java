package com.freak.guidepage;


/**
 * @author Freak
 * @date 2019/11/12.
 * Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
 */
public class GuideLocalImageSize {
    private int maxWidth;
    private int maxHeight;
    private float minWidth;
    private float minHeight;

    public GuideLocalImageSize(int maxWidth, int maxHeight, float minWidth, float minHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }
}
