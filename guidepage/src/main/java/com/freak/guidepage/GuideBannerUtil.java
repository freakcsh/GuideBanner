package com.freak.guidepage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author Freak
 * @date 2019/11/12.
 */
public class GuideBannerUtil {

    private GuideBannerUtil() {
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static ImageView getItemImageView(Context context, @DrawableRes int resId, GuideLocalImageSize localImageSize, ImageView.ScaleType scaleType) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(getScaledImageFromResource(context, resId, localImageSize.getMaxWidth(), localImageSize.getMaxHeight(), localImageSize.getMinWidth(), localImageSize.getMinHeight()));
        imageView.setClickable(true);
        imageView.setScaleType(scaleType);
        return imageView;
    }

    public static void resetPageTransformer(List<? extends View> views) {
        if (views == null) {
            return;
        }

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1);
            view.setPivotX(view.getMeasuredWidth() * 0.5f);
            view.setPivotY(view.getMeasuredHeight() * 0.5f);
            view.setTranslationX(0);
            view.setTranslationY(0);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setRotationX(0);
            view.setRotationY(0);
            view.setRotation(0);
        }
    }

    public static boolean isIndexNotOutOfBounds(int position, Collection collection) {
        return isCollectionNotEmpty(collection) && position < collection.size();
    }

    public static boolean isCollectionEmpty(Collection collection, Collection... args) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        for (Collection arg : args) {
            if (arg == null || arg.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollectionNotEmpty(Collection collection, Collection... args) {
        return !isCollectionEmpty(collection, args);
    }

    /**
     * 从资源获取 Bitmap
     * bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
     */
    @Nullable
    public static Bitmap getScaledImageFromResource(@NonNull Context context, int resId, int maxWidth, int maxHeight, float minWidth, float minHeight) {
        LoadBitmapPair<Throwable> result;
        do {
            result = getImageFromResource(context, resId, maxWidth, maxHeight);
            if (result != null && (result.first != null)) {
                break;
            }
            maxWidth /= 2;
            maxHeight /= 2;
        } while (result != null && result.second instanceof OutOfMemoryError && maxWidth > minWidth && maxHeight > minHeight);

        if (result == null) {
            return null;
        } else {
            return result.first;
        }
    }

    /**
     * 从资源获取Bitmap
     * 最大宽高为 maxWidth maxHeight
     */
    public static LoadBitmapPair<Throwable> getImageFromResource(@NonNull Context context, int resId, int maxWidth, int maxHeight) {
        LoadBitmapPair<Throwable> result;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        InputStream is = null;

        try {
            if (maxWidth == 0 && maxHeight == 0) {
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                result = new LoadBitmapPair<>(BitmapFactory.decodeStream(is, null, decodeOptions), null);
                is.close();
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                BitmapFactory.decodeStream(is, null, decodeOptions);
                is.reset();
                is.close();

                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode to.
                int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = calculateInSampleSize(decodeOptions, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                Bitmap tempBitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                    result = new LoadBitmapPair<>(Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true), null);
                    tempBitmap.recycle();
                } else {
                    result = new LoadBitmapPair<>(tempBitmap, null);
                }
            }
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            result = new LoadBitmapPair(null, exception);
        } catch (Exception exception) {
            exception.printStackTrace();
            result = new LoadBitmapPair(null, exception);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static class LoadBitmapPair<S extends Throwable> extends Pair<Bitmap, S> {
        LoadBitmapPair(@Nullable Bitmap first, @Nullable S second) {
            super(first, second);
        }
    }

    public static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}