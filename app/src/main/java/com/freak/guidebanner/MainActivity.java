package com.freak.guidebanner;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.freak.guidepage.GuideBanner;
import com.freak.httphelper.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GuideBanner.Delegate<ImageView, String>, GuideBanner.Adapter<ImageView, String> {

    @BindView(R.id.banner_main_default)
    GuideBanner bannerMainDefault;
    @BindView(R.id.banner_main_cube)
    GuideBanner bannerMainCube;
    @BindView(R.id.banner_main_accordion)
    GuideBanner bannerMainAccordion;
    @BindView(R.id.banner_main_flip)
    GuideBanner bannerMainFlip;
    @BindView(R.id.banner_main_rotate)
    GuideBanner bannerMainRotate;
    @BindView(R.id.banner_main_alpha)
    GuideBanner bannerMainAlpha;
    @BindView(R.id.banner_main_zoomFade)
    GuideBanner bannerMainZoomFade;
    @BindView(R.id.banner_main_fade)
    GuideBanner bannerMainFade;
    @BindView(R.id.banner_main_zoomCenter)
    GuideBanner bannerMainZoomCenter;
    @BindView(R.id.banner_main_zoom)
    GuideBanner bannerMainZoom;
    @BindView(R.id.banner_main_stack)
    GuideBanner bannerMainStack;
    @BindView(R.id.banner_main_zoomStack)
    GuideBanner bannerMainZoomStack;
    @BindView(R.id.banner_main_depth)
    GuideBanner bannerMainDepth;
    private List<String> list;
    private List<String> list1;
    private List<String> titleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("文案1");
        titleList.add("文案2");
        titleList.add("文案3");
        setListener();
        loadData();
    }

    private void setListener() {
        bannerMainDefault.setDelegate(this);
        bannerMainCube.setDelegate(this);
    }

    private void loadData() {
        loadData(bannerMainDefault, 3);
        loadData(bannerMainCube, 4);
        loadData(bannerMainAccordion, 4);
        loadData(bannerMainFlip, 4);
        loadData(bannerMainRotate, 5);
        loadData(bannerMainAlpha, 6);
        loadData(bannerMainZoomFade, 4);
        loadData(bannerMainFade, 4);
        loadData(bannerMainZoomCenter, 5);
        loadData(bannerMainZoom, 6);
        loadData(bannerMainStack, 4);
        loadData(bannerMainZoomStack, 4);
        loadData(bannerMainDepth, 5);
    }

    private void loadData(final GuideBanner banner, final int count) {
        LogUtil.e("图片地址  " + getDrawableUrl(R.drawable.banner1));
        Log.e("TAG", "图片地址  " + getDrawableUrl(R.drawable.banner1));

        if (count == 1) {
            list1.add(getDrawableUrl(R.drawable.banner1));
            /**
             * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
             * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
             */
            banner.setAutoPlayAble(list1.size() > 1);

            banner.setAdapter(MainActivity.this);
            banner.setData(list1, null);
        } else {
            if (count <= 3) {
                list.add(getDrawableUrl(R.drawable.banner1));
                list.add(getDrawableUrl(R.drawable.banner2));
                list.add(getDrawableUrl(R.drawable.banner3));
            }

            /**
             * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
             * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
             */
            banner.setAutoPlayAble(list.size() > 1);

            banner.setAdapter(MainActivity.this);
            banner.setData(list, titleList);
        }

    }

    @Override
    public void onBannerItemClick(GuideBanner banner, ImageView itemView, @Nullable String model, int position) {
        Toast.makeText(banner.getContext(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillBannerItem(GuideBanner banner, ImageView itemView, @Nullable String model, int position) {
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.holder).error(R.drawable.holder).dontAnimate().centerCrop())
                .into(itemView);
    }

    private String getDrawableUrl(@DrawableRes int drawable) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getResources().getResourcePackageName(drawable) + "/"
                + getResources().getResourceTypeName(drawable) + "/"
                + getResources().getResourceEntryName(drawable));
        return imageUri.toString();
    }
}
