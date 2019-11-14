package com.freak.guidebanner.guide;

import android.widget.ImageView;
import android.widget.TextView;

import com.freak.guidebanner.MainActivity;
import com.freak.guidebanner.R;
import com.freak.guidebanner.app.App;
import com.freak.guidebanner.base.BaseAbstractSimpleActivity;
import com.freak.guidebanner.constants.Constants;
import com.freak.guidebanner.utils.PackageUtils;
import com.freak.guidebanner.utils.SPUtils;
import com.freak.guidepage.GuideBanner;
import com.freak.guidepage.GuideLocalImageSize;

import butterknife.BindView;

public class GuideActivity extends BaseAbstractSimpleActivity {

    @BindView(R.id.banner_guide_background)
    GuideBanner bannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    GuideBanner bannerGuideForeground;
    @BindView(R.id.text_view_guide_skip)
    TextView textViewGuideSkip;
    @BindView(R.id.text_view_guide_enter)
    TextView textViewGuideEnter;


    @Override
    protected int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onDestroyRelease() {

    }

    @Override
    protected void initView() {
        setListener();
        processLogic();
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        bannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.text_view_guide_enter, R.id.text_view_guide_skip, new GuideBanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                SPUtils.put(App.getInstance().getApplicationContext(), Constants.VERSION_CODE,String.valueOf(PackageUtils.getVersionCode(App.getInstance().getApplicationContext())));
                gotoActivity(MainActivity.class,true);
            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        GuideLocalImageSize localImageSize = new GuideLocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        bannerGuideBackground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.ic_guide_page_1,
                R.mipmap.ic_guide_page_2,
                R.mipmap.ic_guide_page_3
        );

        bannerGuideForeground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.ic_guide_page_official_1,
                R.mipmap.ic_guide_page_official_2,
                R.mipmap.ic_guide_page_official_3
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        bannerGuideBackground.setBackgroundResource(android.R.color.white);
    }


}