package com.android.hzjy.hzjyproduct;

import android.app.Application;
import android.util.Log;

import com.android.hzjy.hzjyproduct.consts.MainConsts;
import com.android.hzjy.hzjyproduct.util.ActivityStacks;
import com.talkfun.sdk.log.TalkFunLogger;
import com.talkfun.sdk.offline.PlaybackDownloader;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

public class TFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        String rid = JPushInterface.getRegistrationID(this);
        Log.e("rid","rid" + rid);
        //初始化点播下载
        initPlaybackDownLoader();
        TalkFunLogger.setLogLevel(TalkFunLogger.LogLevel.ALL);
        CrashReport.initCrashReport(getApplicationContext(), MainConsts.BUGLY_ID, true);
    }

    public void initPlaybackDownLoader() {
        PlaybackDownloader.getInstance().init(this);
        PlaybackDownloader.getInstance().setDownLoadThreadSize(3);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }

    public static void exit() {
        /**终止应用程序对象时调用，不保证一定被调用 ,退出移除所有的下载任务*/
        ActivityStacks.getInstance().finishAllActivity();
        //释放离线下载资源
        PlaybackDownloader.getInstance().destroy();
        TalkFunLogger.release();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}
