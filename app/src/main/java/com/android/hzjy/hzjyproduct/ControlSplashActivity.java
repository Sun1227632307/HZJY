package com.android.hzjy.hzjyproduct;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by dayuer on 19/7/2.
 * 功能 启动页
 */
public class ControlSplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelStatusBarUtil.setStatusBarColor(this,R.color.white);
//        ModelViewUtils.setImmersionStateMode(this);
        setContentView(R.layout.startup_homepage);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("guide",MODE_PRIVATE);
                boolean isfirst = preferences.getBoolean("isfirst",false);
                if (!isfirst) {
                    startActivity(new Intent(ControlSplashActivity.this, ControlGuideActivity.class));
                    SharedPreferences.Editor editor =
                            getSharedPreferences("guide",MODE_PRIVATE).edit();
                    editor.putBoolean("isfirst",true);
                    editor.commit();
                    finish();
                }else{
                    startActivity(new Intent(ControlSplashActivity.this,ControlMainActivity.class));
                    finish();
                }
            }
        },2000);
//        },0);
    }
}
