package com.freak.guidebanner.guide;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.freak.guidebanner.MainActivity;
import com.freak.guidebanner.R;
import com.freak.guidebanner.app.App;
import com.freak.guidebanner.base.BaseAbstractSimpleActivity;
import com.freak.guidebanner.base.IActivityStatusBar;
import com.freak.guidebanner.utils.StatusBarUtil;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseAbstractSimpleActivity implements IActivityStatusBar {

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onDestroyRelease() {

    }

    @Override
    protected void initView() {
        if (!App.hasNotchInScreen(this)) {
            StatusBarUtil.setTransparentForWindow(this);
        }
        new InnerThread(SplashActivity.this).start();
    }


    @Override
    public int getStatusBarColor() {
        return 0;
    }

    @Override
    public int getDrawableStatusBar() {
        return 0;
    }

    private static class InnerThread extends Thread {

        WeakReference<SplashActivity> mReference;

        InnerThread(SplashActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            SplashActivity activity = mReference.get();
            if (activity != null) {
                activity.sendMsg();
            }
        }
    }

    public void sendMsg() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.prepare();
        new InnerHandler(SplashActivity.this).obtainMessage().sendToTarget();
        Looper.loop();
    }


    public void startMain() {
        gotoActivity(MainActivity.class, true);
    }

    public void startGuide() {
        gotoActivity(GuideActivity.class, true);
    }

    private static class InnerHandler extends Handler {

        WeakReference<SplashActivity> mReference;

        InnerHandler(SplashActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mReference.get();
            if (activity != null) {
                //只在软件第一次安装或者更新之后显示引导页
//                if (TextUtils.isEmpty((String) SPUtils.get(App.getInstance().getApplicationContext(), Constants.VERSION_CODE, ""))) {
//                    activity.startGuide();
//                } else {
//                    if (TextUtils.equals(String.valueOf(PackageUtils.getVersionCode(App.getInstance().getApplicationContext())), (String) SPUtils.get(App.getInstance().getApplicationContext(), Constants.VERSION_CODE, ""))) {
//                        activity.startMain();
//                    } else {
//                        activity.startGuide();
//                    }
//                }

                activity.startGuide();
            }
        }
    }
}
