package com.android.hzjy.hzjyproduct;
import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hzjy.hzjyproduct.activity.LoginJumpActivity;
import com.android.hzjy.hzjyproduct.consts.MainConsts;
import com.android.hzjy.hzjyproduct.consts.PlayType;
import com.android.hzjy.hzjyproduct.entity.PlaybackDataConverter;
import com.android.hzjy.hzjyproduct.entity.PrePlaybackEntity;
import com.android.hzjy.hzjyproduct.net.HttpRequest;
import com.android.hzjy.hzjyproduct.util.ActivityUtil;
import com.talkfun.sdk.http.PreDataRequestManager;
import com.talkfun.sdk.model.PreDataForPlaybackInitModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dayuer on 19/7/2.
 * 主程序
 */
public class ControlMainActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    //继承Activity 不会显示APP头上的标题
    private Fragment mModelHomePage,mModelMy,mModelOpenClass,mModelLogIn,mModelSetting,mModelCoursePacket,mModelCourse,mModelClassCheduleCard
            ,mModelQuestionBank,mModelNews,mModelCommunityAnswer;
    private String mPage = "";
    private String mBeforePage = "";
    private ControlMainActivity mThis;
    private String mSMSCode = ""; //验证码
    private UserInfo mUserInfo = new UserInfo();
    private BottomNavigationView mBottomNavigationView;
//    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 6.0版本检测并申请开启摄像头、音频录制、扩展卡读写等权限*/
    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO};
    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri
    private long firstTime = 0;
    //订单
    private ModelOrderDetails modelOrderDetails = null;

    private class MenuItemInfo {
        String mName;  //按钮名称
        int mItemId;   //按钮标识（1：首页 2：课程包 3：课程表 4：我的）
        int mOrder;    //按钮排序
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelStatusBarUtil.setStatusBarColor(this,R.color.white);
//        ModelViewUtils.setImmersionStateMode(this);
        setContentView(R.layout.activity_main);
        mUserInfo.mUserLoginState = "1"; //登录状态  0:未登录  1：已登录
        mUserInfo.mUserHeadUrl = ""; //头像
        mUserInfo.mUserName = "dayuer"; //用户名
        mUserInfo.mUserIntroduce = "一名程序员"; //个人介绍
        mUserInfo.mUserId = "15116936402";
        mUserInfo.mUserEmail = "xiewenyu@huozhongjiaoyu.com";
        mUserInfo.mUserTeleNum = "13520093734";
        mUserInfo.mUserIdNum = "230516395463633251";
        mThis = this;
        mBottomNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED); //同时显示底部菜单的图标和文字
        //判断底部菜单使用哪些菜单
        List<MenuItemInfo> menuItemList = new ArrayList<>();
        MenuItemInfo MenuItemInfo1 = new MenuItemInfo();
        MenuItemInfo1.mItemId = 1;
        MenuItemInfo1.mOrder = 1;
        MenuItemInfo1.mName = "首页";
        menuItemList.add(MenuItemInfo1);
        MenuItemInfo MenuItemInfo2 = new MenuItemInfo();
        MenuItemInfo2.mItemId = 2;
        MenuItemInfo2.mOrder = 2;
        MenuItemInfo2.mName = "课程包";
        menuItemList.add(MenuItemInfo2);
        MenuItemInfo MenuItemInfo3 = new MenuItemInfo();
        MenuItemInfo3.mItemId = 3;
        MenuItemInfo3.mOrder = 3;
        MenuItemInfo3.mName = "课程表";
        menuItemList.add(MenuItemInfo3);
        MenuItemInfo MenuItemInfo4 = new MenuItemInfo();
        MenuItemInfo4.mItemId = 4;
        MenuItemInfo4.mOrder = 4;
        MenuItemInfo4.mName = "我的";
        menuItemList.add(MenuItemInfo4);
        if (menuItemList.size() == 0){  //如果从后台查询的菜单为0，添加默认菜单
            MenuItemInfo MenuItemInfoDefault1 = new MenuItemInfo();
            MenuItemInfoDefault1.mItemId = 1;
            MenuItemInfoDefault1.mOrder = 1;
            MenuItemInfoDefault1.mName = "首页";
            menuItemList.add(MenuItemInfoDefault1);
            MenuItemInfo MenuItemInfoDefault2 = new MenuItemInfo();
            MenuItemInfoDefault2.mItemId = 2;
            MenuItemInfoDefault2.mOrder = 2;
            MenuItemInfoDefault2.mName = "课程包";
            menuItemList.add(MenuItemInfoDefault2);
            MenuItemInfo MenuItemInfoDefault3 = new MenuItemInfo();
            MenuItemInfoDefault3.mItemId = 3;
            MenuItemInfoDefault3.mOrder = 3;
            MenuItemInfoDefault3.mName = "课程表";
            menuItemList.add(MenuItemInfoDefault3);
            MenuItemInfo MenuItemInfoDefault4 = new MenuItemInfo();
            MenuItemInfoDefault4.mItemId = 4;
            MenuItemInfoDefault4.mOrder = 4;
            MenuItemInfoDefault4.mName = "我的";
            menuItemList.add(MenuItemInfoDefault4);
        }
        for (int i = 0; i < menuItemList.size(); i ++) {
            MenuItemInfo menuItemInfo = menuItemList.get(i);
            if (menuItemInfo == null){
                continue;
            }
            Menu menu = mBottomNavigationView.getMenu();
            if (menu == null){
                continue;
            }
            menu.add(0, menuItemInfo.mItemId, menuItemInfo.mOrder, menuItemInfo.mName);//设置菜单标题(0,编号,顺序,名称)
            MenuItem item = menu.findItem(menuItemInfo.mItemId);
            if (menuItemInfo.mItemId == 1){ //使用首页的图标
                item.setIcon(R.drawable.bottom_menu_homepage);//设置菜单图片
            } else if (menuItemInfo.mItemId == 2){ //使用课程包的图标
                item.setIcon(R.drawable.bottom_menu_coursepacket);//设置菜单图片
            } else if (menuItemInfo.mItemId == 3){ //使用课程表的图标
                item.setIcon(R.drawable.bottom_menu_classschedulecard);//设置菜单图片
            } else if (menuItemInfo.mItemId == 4){ //使用我的图标
                item.setIcon(R.drawable.bottom_menu_my);//设置菜单图片
            }
            item.setOnMenuItemClickListener(item1 -> {
                Log.d("123", "onNavigationItemSelected is click: ");
                if (menuItemInfo.mItemId == 1){ //跳转首页的界面
                    Page_HomePage();
                } else if (menuItemInfo.mItemId == 2){ //跳转课程包的界面
                    Page_MoreCoursePacket();
                } else if (menuItemInfo.mItemId == 3){ //跳转课程表的界面
                    Page_ClassCheduleCard();
                } else if (menuItemInfo.mItemId == 4){ //跳转我的界面
                    Page_My();
                }
                return false;
            });
        }
        Menu menu = mBottomNavigationView.getMenu();
        if (menu != null) {
            MenuItem MenuItem = menu.getItem(0);
            if (MenuItem != null) {
                //初次进入app 默认显示menuItemList中的第一个
                if (MenuItem.getItemId() == 1) {
                    Page_HomePage();
                } else if (MenuItem.getItemId() == 2) {
                    Page_MoreCoursePacket();
                } else if (MenuItem.getItemId() == 3) {
                    Page_ClassCheduleCard();
                } else if (MenuItem.getItemId() == 4) {
                    Page_My();
                }
            }
        }
        getPermission();
    }

//    public void FragmentAddCallback(View view,String context){
//    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(mModelHomePage!= null){
            transaction.hide(mModelHomePage);
        }
        if(mModelMy != null){
            transaction.hide(mModelMy);
        }
        if(mModelClassCheduleCard != null){
            transaction.hide(mModelClassCheduleCard);
        }
        if (mModelLogIn != null){
            transaction.hide(mModelLogIn);
        }
        if (mModelSetting != null){
            transaction.hide(mModelSetting);
        }
        if (mModelCoursePacket != null){
            transaction.hide(mModelCoursePacket);
        }
        if (mModelCourse != null){
            transaction.hide(mModelCourse);
        }
        if (mModelQuestionBank != null){
            transaction.hide(mModelQuestionBank);
        }
        if (mModelOpenClass != null){
            transaction.hide(mModelOpenClass);
        }
        if (mModelNews != null){
            transaction.hide(mModelNews);
        }
        if (mModelCommunityAnswer != null){
            transaction.hide(mModelCommunityAnswer);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
        /**
      * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
      *
      * @param v
      * @param event
      * @return
      */
        private boolean isShouldHideInput(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                        + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    return false;
                } else {
                    return true;
                }
            }
            // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
            return false;
        }
    /**
      * 多种隐藏软件盘方法的其中一种
      *
      * @param token
      */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected void onDestroy() {
        if (mModelHomePage != null){
            mModelHomePage.onDestroy();
        }
        if (mModelMy != null) {
            mModelMy.onDestroy();
        }
        if (mModelLogIn != null){
            mModelLogIn.onDestroy();
        }
        if (mModelSetting != null){
            mModelSetting.onDestroy();
        }
        if (mModelCoursePacket != null){
            mModelCoursePacket.onDestroy();
        }
        if (mModelCourse != null){
            mModelCourse.onDestroy();
        }
        if (mModelQuestionBank != null){
            mModelQuestionBank.onDestroy();
        }
        if(mModelClassCheduleCard != null){
            mModelClassCheduleCard.onDestroy();
        }
        if (mModelOpenClass != null){
            mModelOpenClass.onDestroy();
        }
        if (mModelNews != null){
            mModelNews.onDestroy();
        }
        if (mModelCommunityAnswer != null){
            mModelCommunityAnswer.onDestroy();
        }
        super.onDestroy();
    }

    //点击更多课程
    public void onClickMoreCourse(View view) { Page_Course(); }

    //点击更多课程包
    public void onClickMoreCoursePacket(View view) {
        Page_MoreCoursePacket();
    }

    //点击课程包详情的返回界面
    public void onClickCoursePacketDetailsReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        if (beforePageS[beforePageS.length - 1].equals("课程包")){ //说明上个界面是课程包界面
            Page_MoreCoursePacket();
        } else if (beforePageS[beforePageS.length - 1].equals("首页")){ //说明上个界面是首页
            Page_HomePage();
        } else if (beforePageS[beforePageS.length - 1].equals("我的课程包")){ //说明上个界面是我的课程包界面
            mPage = "我的课程包";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyClassPacketShow();
            }
        } else if (beforePageS[beforePageS.length - 1].equals("我的收藏")){ //说明上个界面是我的收藏界面
            mPage = "我的收藏";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyCollectShow();
            }
        }
    }

    //点击课程包详情界面
    public void onClickCoursePacketDetails() {
        mBeforePage = mBeforePage + "/" + mPage ;
        if (mBeforePage.contains("课程包详情")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/课程包详情"));
        }
        mPage = "课程包详情";
        if (mBottomNavigationView != null){
            mBottomNavigationView.setVisibility(View.INVISIBLE);
        }
    }

    //点击课程详情界面de返回界面
    public void onClickCourseDetailsReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        if (beforePageS[beforePageS.length - 1].equals("课程")){ //说明上个界面是课程界面
            Page_Course();
        } else if (beforePageS[beforePageS.length - 1].equals("首页")){ //说明上个界面是首页界面
            Page_HomePage();
        } else if (beforePageS[beforePageS.length - 1].equals("我的课程")){//说明上个界面是我的课程界面
            mPage = "我的课程";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyClassShow();
            }
        } else if (beforePageS[beforePageS.length - 1].equals("我的收藏")){ //说明上个界面是我的收藏界面
            mPage = "我的收藏";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyCollectShow();
            }
        }
    }

    //点击课程详情界面
    public void onClickCourseDetails() {
        mBeforePage = mBeforePage + "/" + mPage ;
        if (mBeforePage.contains("课程详情")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/课程详情"));
        }
        mPage = "课程详情";
        //隐藏所有的底部按钮
        mBottomNavigationView.setVisibility(View.INVISIBLE);
    }

//    //点击课程问答
//    public void onClickCourseQuestion(View view) {
//        mBeforePage = mBeforePage + "/" + mPage ; //上个界面是课程详情界面
//        mPage = "课程问答";
//        if (mModelCourse != null){
//            ((ModelCourse)mModelCourse).CourseSearchInit();
//        }
//    }

//    //购买-选择优惠券-返回
//    public void onClickCouponChooseReturn(View view) {
//    }

    //点击公开课列表的返回
    public void onClickOpenClassReturn(View view) {
        if (mPage.equals("公开课") && mBeforePage.equals("首页")) { //如果当前界面是公开课，点击返回按钮，应该返回到首页
            Page_HomePage();
            mBottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    //点击立即登录
    public void onClickImmediatelyLogin(View view) {
        if (mUserInfo.mUserLoginState.equals("0")){
            mPage = "登录";
            mBeforePage = "我的";
            Page_LogIn();//弹出登录界面
        } else { //弹出学员信息界面
            mPage = "基本信息";
            mBeforePage = "我的";
            //隐藏所有的底部按钮
            mBottomNavigationView.setVisibility(View.INVISIBLE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            if(mModelSetting == null){
                mModelSetting = ModelSetting.newInstance(mThis,"设置-基本信息",R.layout.modelsetting,mUserInfo);//"设置"
                transaction.add(R.id.framepage,mModelSetting);
            } else {
                transaction.show(mModelSetting);
                ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,0);
            }
            transaction.commit();
        }
    }

    //点击登录
    public void onClickLogin(View view) {
        //获取登录界面的信息
        String userId = ((ModelLogIn)mModelLogIn).UserIdGet();
        if (userId.equals("")){
            Toast.makeText(this,"账户不允许为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String userPassword = ((ModelLogIn)mModelLogIn).UserPasswordGet();
        if (userPassword.equals("")){
            Toast.makeText(this,"密码不允许为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //将账户和密码发给服务器确认
    }

    //点击设置
    public void onClickSetting(View view) {
        mPage = "设置";
        mBeforePage = "我的";
        //隐藏所有的底部按钮
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelSetting == null){
            mModelSetting = ModelSetting.newInstance(mThis,"设置",R.layout.modelsetting,mUserInfo);//"设置"
            transaction.add(R.id.framepage,mModelSetting);
        }else{
            transaction.show(mModelSetting);
            ((ModelSetting)mModelSetting).SettingMainShow(mUserInfo,0);
        }
        transaction.commit();
    }

    //点击设置-返回
    public void onClickSettingReturn(View view) {
        Page_My();
    }

    //点击我的课程
    public void onClickMyCourse(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的课程";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyClassShow();
        }
    }

    //我的课程-返回
    public void onClickMyClassReturn(View view) {
        if (mPage.equals("我的课程") && mBeforePage.equals("我的")) { //如果当前界面是我的课程，点击返回按钮，应该返回到我的
            Page_My();
        }
    }

    //我的课程-课程表
    public void onClickMyCourseClassCheduleCard(View view) {
        mPage = "课程表";
        mBeforePage = "我的课程";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelClassCheduleCard == null){
            mModelClassCheduleCard = ModelClassCheduleCard.newInstance(mThis,"",R.layout.fragment_classchedulecard);//"课程表"
            transaction.add(R.id.framepage,mModelClassCheduleCard);
        }else{
            transaction.show(mModelClassCheduleCard);
            ((ModelClassCheduleCard)mModelClassCheduleCard).ClassCheduleCardMainInit("");
        }
        transaction.commit();
    }

    //课程表-返回
    public void onClickClassCheduleCardReturn(View view) {
        if (mPage.equals("课程表") && mBeforePage.equals("我的课程")) { //如果当前界面是我的课程，点击返回按钮，应该返回到我的
            if(mModelMy != null){
                Page_My();
                mBottomNavigationView.setVisibility(View.INVISIBLE);
                mPage = "我的课程";
                mBeforePage = "我的";
                ((ModelMy) mModelMy).MyClassShow();
            }
        }
    }

    //点击登录主界面的返回
    public void onClickLoginReturn(View view) {
        if (mPage.equals("登录") && mBeforePage.equals("我的")){
            Page_My();
        } else if (mPage.equals("登录") && mBeforePage.equals("设置")) { //如果当前界面是登录，点击返回按钮，应该返回到设置
            if (mModelSetting != null) {
                mPage = "设置";
                mBeforePage = "我的";
                //隐藏所有的底部按钮
                mBottomNavigationView.setVisibility(View.INVISIBLE);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                if(mModelSetting == null){
                    mModelSetting = ModelSetting.newInstance(mThis,"设置-基本信息",R.layout.modelsetting,mUserInfo);//"设置"
                    transaction.add(R.id.framepage,mModelSetting);
                } else {
                    transaction.show(mModelSetting);
                    ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,0);
                }
                transaction.commit();
            }
        }
    }

    //点击忘记密码
    public void onClickForgetPassword(View view) {
        mPage = "忘记密码";
        mBeforePage = "登录";
        ((ModelLogIn)mModelLogIn).ForgetPasswordShow();
    }

    //点击忘记密码-返回
    public void onClickForgetPasswordReturn(View view) {
        ((ModelLogIn)mModelLogIn).LogInMainShow();
    }

    //点击注册
    public void onClickRegister(View view) {
        mPage = "注册";
        mBeforePage = "登录";
        ((ModelLogIn)mModelLogIn).RegisterShow();
    }

    //点击开始注册
    public void onClickRegisterStart(View view) {
        //判断验证码是否一致
        //判断密码是否为6~12位数字或字母
        //向服务器发送新密码
    }

    //点击获取验证码
    public void onClickSMSCodeGet(View view) {
        mSMSCode = "";
        //向服务器发命令获取验证码
        //修改界面为倒计时
        ((ModelLogIn)mModelLogIn).SMSCodeGet();
    }

    //注册-获取验证码
    public void onClickRegisterSMSCodeGet(View view) {
        mSMSCode = "";
        //向服务器发命令获取验证码
        //修改界面为倒计时
        ((ModelLogIn)mModelLogIn).RegisterSMSCodeGet();
    }

    //点击重置密码
    public void onClickResetPassword(View view) {
        //判断验证码是否一致
        //判断密码是否为6~12位数字或字母
        //向服务器发送新密码
    }

    //点击设置-基本信息
    public void onClickSettingEssentialInformation(View view) {
        if (mUserInfo.mUserLoginState.equals("0")){
            mPage = "登录";
            mBeforePage = "设置";
            //跳转到登录界面
            Page_LogIn();
        } else {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击设置-基本信息-返回
    public void onClickSettingBaseInfoReturn(View view) {
        if (mPage.equals("基本信息") && mBeforePage.equals("我的")){
            Page_My();
        } else if (mPage.equals("基本信息") && mBeforePage.equals("设置")){
            if (mModelSetting != null) {
                mPage = "设置";
                mBeforePage = "我的";
                ((ModelSetting) mModelSetting).SettingMainShow(mUserInfo,0);
            }
        }
    }

    //点击上传头像
    public void onClickSettingEssentialInformationIcon(View view) {
        if (mModelSetting != null) {
            ((ModelSetting) mModelSetting).SetttingButtonDialogShow();
        }
    }

    //点击修改名称
    public void onClickSettingEssentialInformationName(View view) {
        if (mModelSetting != null) {
            mPage = "修改名称";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingUserNameUpdateShow(mUserInfo);
        }
    }

    //点击修改名称-返回(取消修改，无需保存)
    public void onClickSettingUpdateUserNameReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改名称-保存
    public void onClickSettingUpdateUserNameSave(View view) {
        if (mModelSetting != null) {
            String userName = ((ModelSetting)mModelSetting).UserNameGet();
            //将userName 存储与本地和服务器
            mUserInfo.mUserName = userName;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改名称-清除所有
    public void onClickSettingUpdateUserNameClear(View view) {
        if (mModelSetting != null) {
            ((ModelSetting)mModelSetting).SettingUserNameUpdateClear();
        }
    }

    //点击修改签名
    public void onClickSettingEssentialInformationSign(View view) {
        if (mModelSetting != null) {
            mPage = "修改签名";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingPersonalStatementUpdateShow(mUserInfo);
        }
    }

    //点击修改签名-返回(取消修改，无需保存)
    public void onClickSettingUpdatePersonalStatementReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改签名-保存
    public void onClickSettingUpdatePersonalStatementSave(View view) {
        if (mModelSetting != null) {
            String personalStatement = ((ModelSetting)mModelSetting).PersonalStatementGet();
            //将userName 存储与本地和服务器
            mUserInfo.mUserIntroduce = personalStatement;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改邮箱
    public void onClickSettingEssentialInformationEmail(View view) {
        if (mModelSetting != null) {
            mPage = "修改邮箱";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingEmailUpdateShow(mUserInfo);
        }
    }

    //点击修改邮箱-返回(取消修改，无需保存)
    public void onClickSettingUpdateEmailReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改邮箱-保存
    public void onClickSettingUpdateEmailSave(View view) {
        if (mModelSetting != null) {
            String email = ((ModelSetting)mModelSetting).EmailGet();
            if (!email.equals("")){ //如果邮箱不为空的时候，判断邮箱地址是否正确
                if (!isEmail(email)){
                    Toast.makeText(this,"邮箱格式不正确，请重新输入！",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //将userName 存储与本地和服务器
            mUserInfo.mUserEmail = email;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改邮箱-清除所有
    public void onClickSettingUpdateEmailClear(View view) {
        if (mModelSetting != null) {
            ((ModelSetting)mModelSetting).SettingEmailUpdateClear();
        }
    }

    //点击修改电话号码
    public void onClickSettingEssentialInformationTel(View view) {
        if (mModelSetting != null) {
            mPage = "修改电话号码";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingTelNumberUpdateShow(mUserInfo);
        }
    }

    //点击修改电话号码-返回(取消修改，无需保存)
    public void onClickSettingUpdateTelNumberReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改电话号码-保存
    public void onClickSettingUpdateTelNumberSave(View view) {
        if (mModelSetting != null) {
            String telNumber = ((ModelSetting)mModelSetting).TelNumberGet();
            if (!telNumber.equals("")){ //如果电话号码不为空的时候，判断电话号码是否正确
                if (!isTelNumber(telNumber)){
                    Toast.makeText(this,"电话号码不正确，请重新输入！",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //将telNumber 存储与本地和服务器
            mUserInfo.mUserTeleNum = telNumber;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改电话号码-清除所有
    public void onClickSettingUpdateTelNumberClear(View view) {
        if (mModelSetting != null) {
            ((ModelSetting)mModelSetting).SettingTelNumberUpdateClear();
        }
    }

    //点击修改证件号码
    public void onClickSettingEssentialInformationIdNum(View view) {
        if (mModelSetting != null) {
            mPage = "修改证件号码";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingIdNumberUpdateShow(mUserInfo);
        }
    }

    //点击修改证件号码-返回(取消修改，无需保存)
    public void onClickSettingUpdateIdNumberReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改证件号码-保存
    public void onClickSettingUpdateIdNumberSave(View view) {
        if (mModelSetting != null) {
            String idNumber = ((ModelSetting)mModelSetting).IdNumberGet();
            if (!idNumber.equals("")){ //如果证件号码不为空的时候，判断证件号码是否正确
                if (!isIdNumber(idNumber)){
                    Toast.makeText(this,"证件号码不正确，请重新输入！",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //将idNumber 存储与本地和服务器
            mUserInfo.mUserIdNum = idNumber;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改证件号码-清除所有
    public void onClickSettingUpdateIdNumberClear(View view) {
        if (mModelSetting != null) {
            ((ModelSetting)mModelSetting).SettingIdNumberUpdateClear();
        }
    }

    //点击修改密码
    public void onClickSettingEssentialInformationPasswordUpdata(View view) {
        if (mModelSetting != null) {
            mPage = "修改密码";
            mBeforePage = "基本信息";
            ((ModelSetting) mModelSetting).SettingPasswordUpdateShow(mUserInfo);
        }
    }

    //点击修改密码-返回(取消修改，无需保存)
    public void onClickSettingUpdatePasswordReturn(View view) {
        if (mModelSetting != null) {
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        }
    }

    //点击修改密码-保存密码
    public void onClickSettingUpdatePasswordSave(View view) {
        int returnCode = ((ModelSetting)mModelSetting).NewPasswordSave();
        if (returnCode == 0){
            String newPassword = ((ModelSetting)mModelSetting).NewPasswordGet();
            //将idNumber 存储与本地和服务器
            mUserInfo.mUserPassword = newPassword;
            mPage = "基本信息";
            mBeforePage = "设置";
            ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
        } else if (returnCode == 1){
            Toast.makeText(this,"原密码输入不正确，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        } else if (returnCode == 2){
            Toast.makeText(this,"新密码两次输入不一致，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        }
    }

    //点击设置界面的清理缓存
    public void onClickSettingClearCache(View view) {
        final ModelCommonDialog dialog = new ModelCommonDialog(this);
        dialog.setMessage("确认清除缓存吗？")
//                .setImageResId(R.mipmap.ic_launcher)
                .setTitle("提示")
                .setSingle(false).setOnClickBottomListener(new ModelCommonDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                //确定按钮，清空本地缓存
                dialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
            }
        }).show();
    }

    //点击设置界面的允许非WiFi网络播放和缓存视频
    public void onClickSettingAllowNonWifiPlay(boolean isEnable) {
        //isEnable 是否允许在非WiFi网络播放和缓存视频
    }

//    //点击设置界面的版本
//    public void onClickSettingVersion(View view) {
//    }

    //点击设置界面的关于我们
    public void onClickSettingAboutUs(View view) {
        if (mModelSetting != null) {
            mPage = "关于我们";
            mBeforePage = "设置";
            ((ModelSetting) mModelSetting).SettingAboutUsShow();
        }
    }

    //点击设置界面的关于我们-返回(取消修改，无需保存)
    public void onClickSettingAboutUsReturn(View view) {
        if (mModelSetting != null) {
            mPage = "设置";
            mBeforePage = "我的";
            ((ModelSetting) mModelSetting).SettingMainShow(mUserInfo,0);
        }
    }

    //点击设置界面的关于我们-检查新版本
    public void onClickSettingCheckVersion(View view){

    }
//    //点击设置界面的隐私政策
//    public void onClickSettingPrivacyPolicy(View view) {
//    }

    //点击设置界面的退出登录
    public void onClickLogout(View view) {
    }

    //点击打开照相机
    public void onClickOpenCamera(View view) {
        goCamera();
    }

    //点击选择本地图片
    public void onClickChooseImg(View view) {
        goPhotoAlbum();
    }

    //点击关闭选择图片对话框
    public void onClickButtonDialogCancel(View view) {
        if (mModelSetting != null) {
            ((ModelSetting) mModelSetting).SetttingButtonDialogCancel();
        }
    }

    //点击课程包主界面的返回按钮
    public void onClickCoursePacketMainReturn(View view){
        Page_HomePage();
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    //点击课程包主界面查询按钮
    public void onCoursePacketMainSearch(View view){
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "课程包搜索";
        //将底部菜单隐藏
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        if (mModelCoursePacket != null){
            ((ModelCoursePacket)mModelCoursePacket).CoursePacketMainSearchShow();
        }
    }

    //点击课程主界面的返回按钮
    public void onClickCourseMainReturn(View view){
        Page_HomePage();
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    //点击课程主界面查询按钮
    public void onCourseMainSearch(View view){
        if (mModelCourse != null){
            ((ModelCourse)mModelCourse).CourseMainSearchShow();
        }
    }

    //我的课程包
    public void onClickMyCoursePacket(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的课程包";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyClassPacketShow();
        }
    }

    //我的课程包-返回
    public void onClickMyClassPacketReturn(View view) {
        if (mPage.equals("我的课程包") && mBeforePage.equals("我的")) { //如果当前界面是我的课程包，点击返回按钮，应该返回到我的
            Page_My();
        }
    }

    //我的收藏
    public void onClickMyCollect(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的收藏";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyCollectShow();
        }
    }

    //我的收藏-返回
    public void onClickMyCollectReturn(View view) {
        if (mPage.equals("我的收藏") && mBeforePage.equals("我的")) { //如果当前界面是我的收藏，点击返回按钮，应该返回到我的
            Page_My();
        }
    }

    //我的题库
    public void onClickMyQuestionBank(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的题库";
        mBeforePage = "我的";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelQuestionBank == null){
            mModelQuestionBank = ModelQuestionBank.newInstance(mThis,"我的题库:" + mBeforePage,R.layout.fragment_questionbank);//"我的题库"
            transaction.add(R.id.framepage,mModelQuestionBank);
        } else{
            transaction.show(mModelQuestionBank);
            ((ModelQuestionBank) mModelQuestionBank).QuestionBankMainShow("我的题库");
        }
        transaction.commit();
    }

    //我的订单
    public void onClickMyOrder(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的订单";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyOrderShow();
        }
    }

    //我的订单-返回
    public void onClickMyOrderReturn(View view) {
        if (mPage.equals("我的订单") && mBeforePage.equals("我的")) { //如果当前界面是我的订单，点击返回按钮，应该返回到我的
            Page_My();
        } else if (mPage.equals("订单详情") && mBeforePage.equals("我的订单")) { //如果当前界面是订单详情，点击返回按钮，应该返回到我的订单
            mPage = "我的订单";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyOrderShow();
            }
        }
    }

    //我的订单-订单详情
    public void onClickMyOrderDetails() {
        mPage = "订单详情";
        mBeforePage = "我的订单";
    }

    //我的消息
    public void onClickMyMessage(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的消息";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyMessageShow();
        }
    }

    //我的消息-返回
    public void onClickMyMessageReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        if (mPage.equals("我的消息") && mBeforePage.equals("我的")) { //如果当前界面是我的消息，点击返回按钮，应该返回到我的
            Page_My();
        } else if (mPage.equals("消息详情") && beforePageS[beforePageS.length - 1].equals("我的消息")){//如果当前界面是消息详情，点击返回按钮，应该返回到我的消息
            mPage = "我的消息";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyMessageShow();
            }
        }
    }

    //我的消息-消息详情
    public void onClickMyMessageDetails() {
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "消息详情";
    }

    //我的问答
    public void onClickMyAnswer(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的问答";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyAnswerShow();
        }
    }

    //我的问答-返回
    public void onClickMyAnswerReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        if (mPage.equals("我的问答") && mBeforePage.equals("我的")) { //如果当前界面是我的问答，点击返回按钮，应该返回到我的
            Page_My();
        } else if (mPage.equals("问答详情") && beforePageS[beforePageS.length - 1].equals("我的问答")){//如果当前界面是消息详情，点击返回按钮，应该返回到我的消息
            mPage = "我的问答";
            mBeforePage = "我的";
            if(mModelMy != null){
                ((ModelMy) mModelMy).MyAnswerShow();
            }
        }
    }

    //跳转我的问答-问答详情
    public void Page_AnswerDetails(){
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "问答详情";
    }

    //我的缓存
    public void onClickMyCache(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的缓存";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyCacheShow();
        }
    }

    //我的缓存-返回
    public void onClickMyCacheReturn(View view) {
        if (mPage.equals("我的缓存") && mBeforePage.equals("我的")) { //如果当前界面是我的缓存，点击返回按钮，应该返回到我的
            Page_My();
        }
    }

    //我的优惠券
    public void onClickMyCoupon(View view) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "我的优惠券";
        mBeforePage = "我的";
        if(mModelMy != null){
            ((ModelMy) mModelMy).MyCouponShow();
        }
    }

    //我的优惠券-返回
    public void onClickMyCouponReturn(View view) {
        if (mPage.equals("我的优惠券") && mBeforePage.equals("我的")) { //如果当前界面是我的优惠券，点击返回按钮，应该返回到我的
            Page_My();
        }
    }

    //我的余额
    public void onClickMyBalance(View view) {
    }

    //跳转订单详情
    public View Page_OrderDetails(ModelOrderDetailsInterface modelOrderDetailsInterface){
        if (modelOrderDetails == null){
            modelOrderDetails = new ModelOrderDetails();
        }
        View view = modelOrderDetails.ModelOrderDetails(modelOrderDetailsInterface,this);
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "订单详情";
        return view;
    }

    //订单详情-返回
    public void onClickOrderDetailsReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        if (beforePageS[beforePageS.length - 1].equals("课程详情")){ //说明上个界面是课程详情界面
            mBeforePage = "";
            for (int i = 0 ; i < beforePageS.length - 1; i ++){
                if (i == beforePageS.length - 2){
                    mBeforePage = mBeforePage + beforePageS[i];
                } else {
                    mBeforePage = mBeforePage + beforePageS[i] + "/";
                }
            }
            mPage = beforePageS[beforePageS.length - 1];
        } else if (beforePageS[beforePageS.length - 1].equals("课程包详情")){ //说明上个界面是课程包详情界面
            mBeforePage = "";
            for (int i = 0 ; i < beforePageS.length - 1; i ++){
                if (i == beforePageS.length - 2){
                    mBeforePage = mBeforePage + beforePageS[i];
                } else {
                    mBeforePage = mBeforePage + beforePageS[i] + "/";
                }
            }
            mPage = beforePageS[beforePageS.length - 1];
        }
        modelOrderDetails.onClickOrderDetailsReturn();
    }

    //跳转订单详情-选择优惠券
    public void Page_OrderDetailsChooseCoupon(){
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "选择优惠券";
    }

    //跳转订单详情-选择优惠券-返回
    public void Page_OrderDetailsChooseCouponReturn(){
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        mBeforePage = "";
        for (int i = 0 ; i < beforePageS.length - 1; i ++){
            if (i == beforePageS.length - 2){
                mBeforePage = mBeforePage + beforePageS[i];
            } else {
                mBeforePage = mBeforePage + beforePageS[i] + "/";
            }
        }
        mPage = beforePageS[beforePageS.length - 1];
    }

    //跳转订单详情-银行卡支付
    public void Page_OrderDetailsBankCard(){
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "银行卡支付";
    }

    //跳转订单详情-银行卡支付-返回
    public void Page_OrderDetailsBankCardReturn(){
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        mBeforePage = "";
        for (int i = 0 ; i < beforePageS.length - 1; i ++){
            if (i == beforePageS.length - 2){
                mBeforePage = mBeforePage + beforePageS[i];
            } else {
                mBeforePage = mBeforePage + beforePageS[i] + "/";
            }
        }
        mPage = beforePageS[beforePageS.length - 1];
    }

    //跳转课程包
    public void Page_MoreCoursePacket(){
        mBottomNavigationView.setVisibility(View.VISIBLE);
        Menu menu = mBottomNavigationView.getMenu();
        if (menu == null){
            return;
        }
        mPage = "课程包";
        mBeforePage = "";
        MenuItem menuItem = menu.findItem(2);
        if (menuItem != null){
            menuItem.setChecked(true);
            mBottomNavigationView.setVisibility(View.VISIBLE);
        } else { //说明底部菜单中没有课程包按钮，将所有按钮隐藏
            mBottomNavigationView.setVisibility(View.INVISIBLE);
            mBeforePage = "首页";
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelCoursePacket == null){
            mModelCoursePacket = ModelCoursePacket.newInstance(mThis,"课程包:" + mBeforePage,R.layout.fragment_coursepacket);//"课程包"
            transaction.add(R.id.framepage,mModelCoursePacket);
        }else{
            transaction.show(mModelCoursePacket);
            if (mBeforePage.equals("")) {
                ((ModelCoursePacket) mModelCoursePacket).CoursePacketMainShow(1);
            } else {
                ((ModelCoursePacket) mModelCoursePacket).CoursePacketMainShow(0);
            }
        }
        transaction.commit();
    }

    //跳转题库
    public void Page_QuestionBank(){
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "题库";
        mBeforePage = "首页";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelQuestionBank == null){
            mModelQuestionBank = ModelQuestionBank.newInstance(mThis,"题库:" + mBeforePage,R.layout.fragment_questionbank);//"题库"
            transaction.add(R.id.framepage,mModelQuestionBank);
        } else{
            transaction.show(mModelQuestionBank);
            ((ModelQuestionBank) mModelQuestionBank).QuestionBankMainShow("题库");
        }
        transaction.commit();
    }

    //题库-返回
    public void onClickQuestionBankReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 1){
            if (mBeforePage.equals("首页") && mPage.equals("题库")){ //说明上个界面是首页
                Page_HomePage();
                mBottomNavigationView.setVisibility(View.VISIBLE);
            } else if (mBeforePage.equals("我的") && mPage.equals("我的题库")){ //说明上个界面是首页
                Page_My();
            }
            return;
        }
        if (beforePageS[beforePageS.length - 1].equals("首页")){ //说明上个界面是首页
            Page_HomePage();
            mBottomNavigationView.setVisibility(View.VISIBLE);
        } else if (beforePageS[beforePageS.length - 1].equals("我的")){ //说明上个界面是我的
            Page_My();
        } else if (beforePageS[beforePageS.length - 1].equals("题库") || beforePageS[beforePageS.length - 1].equals("我的题库")
                || beforePageS[beforePageS.length - 1].equals("题库更多") || beforePageS[beforePageS.length - 1].equals("题库详情")
                || beforePageS[beforePageS.length - 1].equals("做题设置")){ //说明上个界面是题库、做题设置、题库详情、题库更多或我的题库
            Page_QuestionBank();
            mBeforePage = "";
            for (int i = 0 ; i < beforePageS.length - 1; i ++){
                if (i == beforePageS.length - 2){
                    mBeforePage = mBeforePage + beforePageS[i];
                } else {
                    mBeforePage = mBeforePage + beforePageS[i] + "/";
                }
            }
            mPage = beforePageS[beforePageS.length - 1];
        }
    }

    //跳转到题库更多界面
    public void onClickQuestionBankMore() {
        if (mBeforePage.contains("题库更多")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/题库更多"));
        } else {
            mBeforePage = mBeforePage + "/" + mPage ;
        }
        mPage = "题库更多";
    }

    //跳转到题库详情
    public void onClickQuestionBankDetails() {
        if (mBeforePage.contains("题库详情")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/题库详情"));
        } else {
            mBeforePage = mBeforePage + "/" + mPage ;
        }
        mPage = "题库详情";
    }

    //跳转到做题设置
    public void onClickQuestionBankSetting() {
        if (mBeforePage.contains("做题设置")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/做题设置"));
        } else {
            mBeforePage = mBeforePage + "/" + mPage ;
        }
        mPage = "做题设置";
    }

    //跳转题库答题卡界面
    public void onClickQuestionBankAnswerQuestionCard(){
        if (mBeforePage.contains("题库答题卡")){
            mBeforePage = mBeforePage.substring(0,mBeforePage.indexOf("/题库答题卡"));
        } else {
            mBeforePage = mBeforePage + "/" + mPage ;
        }
        mPage = "题库答题卡";
    }

    //关闭题库答题卡界面
    public void onClickQuestionBankAnswerQuestionCardReturn(View view) {
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        }
        mBeforePage = mBeforePage.substring(0,mBeforePage.lastIndexOf("/"));
        mPage = beforePageS[beforePageS.length - 1];
        if (mModelQuestionBank != null) {
            ((ModelQuestionBank) mModelQuestionBank).QuestionBankDetailsQuestionModeShow();
        }
    }

    //跳转首页
    public void Page_HomePage(){
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mPage = "首页";
        mBeforePage = "";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelHomePage == null){
            mModelHomePage = ModelHomePage.newInstance(mThis,"首页",R.layout.homepage_layout);
            transaction.add(R.id.framepage,mModelHomePage);
        }else{
            ((ModelHomePage)mModelHomePage).HomePageShow();
            transaction.show(mModelHomePage);
        }
        transaction.commit();
    }

    //跳转社区问答
    public void Page_CommunityAnswer(){
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "社区问答";
        mBeforePage = "首页";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelCommunityAnswer == null){
            mModelCommunityAnswer = ModelCommunityAnswer.newInstance(mThis,"社区问答:" + mBeforePage,R.layout.fragment_communityanswer);//"社区问答"
            transaction.add(R.id.framepage,mModelCommunityAnswer);
        }else{
            transaction.show(mModelCommunityAnswer);
            ((ModelCommunityAnswer) mModelCommunityAnswer).CommunityAnswerMainShow();
        }
        transaction.commit();
    }

    //跳转社区问答-搜索界面
    public void Page_onCommunityAnswerSearch(){
        mPage = "问答搜索";
        mBeforePage = "社区问答";
    }

    //跳转社区问答-添加问答界面
    public void Page_onCommunityAnswerAdd(){
        mPage = "添加问答";
        mBeforePage = "社区问答";
    }
    //跳转社区问答-选择标签界面
    public void Page_onCommunityAnswerChooseSign(){
        mPage = "选择标签";
        mBeforePage = "添加问答";
    }

    //社区问答-返回
    public void onClickCommunityAnswerReturn(View view) {
        if (mPage.equals("社区问答") && mBeforePage.equals("首页")) { //如果当前界面是社区问答，点击返回按钮，应该返回到首页
            Page_HomePage();
            mBottomNavigationView.setVisibility(View.VISIBLE);
        } else if (mPage.equals("问答搜索") && mBeforePage.equals("社区问答")) { //如果当前界面是问答搜索，点击返回按钮，应该返回到社区问答
            Page_CommunityAnswer();
        } else if (mPage.equals("添加问答") && mBeforePage.equals("社区问答")) { //如果当前界面是添加问答，点击返回按钮，应该返回到社区问答
            mPage = "社区问答";
            mBeforePage = "首页";
            if (mModelCommunityAnswer != null) {
                ((ModelCommunityAnswer) mModelCommunityAnswer).CommunityAnswerAddReturn();
            }
        } else if (mPage.equals("选择标签") && mBeforePage.equals("添加问答")) { //如果当前界面是选择标签，点击返回按钮，应该返回到添加问答
            if (mModelCommunityAnswer != null) {
                ((ModelCommunityAnswer) mModelCommunityAnswer).CommunityAnswerAddInit();
            }
            mPage = "添加问答";
            mBeforePage = "社区问答";
        }
    }

    //跳转课程表
    public void Page_ClassCheduleCard(){
        mBottomNavigationView.setVisibility(View.VISIBLE);
        Menu menu = mBottomNavigationView.getMenu();
        if (menu == null){
            return;
        }
        mPage = "课程表";
        mBeforePage = "";
        MenuItem menuItem = menu.findItem(3);
        if (menuItem != null){
            menuItem.setChecked(true);
        } else { //说明底部菜单中没有课程包按钮，将所有按钮隐藏
            mBottomNavigationView.setVisibility(View.INVISIBLE);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelClassCheduleCard == null){
            mModelClassCheduleCard = ModelClassCheduleCard.newInstance(mThis,"首页",R.layout.fragment_classchedulecard);//"课程表"
            transaction.add(R.id.framepage,mModelClassCheduleCard);
        }else{
            transaction.show(mModelClassCheduleCard);
            ((ModelClassCheduleCard)mModelClassCheduleCard).ClassCheduleCardMainInit("首页");
        }
        transaction.commit();
    }

    //跳转课程
    public void Page_Course(){
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "课程";
        mBeforePage = "首页";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelCourse == null){
            mModelCourse = ModelCourse.newInstance(mThis,"课程:" + mBeforePage,R.layout.fragment_course);//"课程"
            transaction.add(R.id.framepage,mModelCourse);
        }else{
            transaction.show(mModelCourse);
            ((ModelCourse) mModelCourse).CourseMainShow(0);
        }
        transaction.commit();
    }

    //跳转我的
    public void Page_My(){
        mBottomNavigationView.setVisibility(View.VISIBLE);
        Menu menu = mBottomNavigationView.getMenu();
        if (menu == null){
            return;
        }
        mPage = "我的";
        mBeforePage = "";
        MenuItem menuItem = menu.findItem(4);
        if (menuItem != null){
            menuItem.setChecked(true);
        } else { //说明底部菜单中没有课程包按钮，将所有按钮隐藏
            mBottomNavigationView.setVisibility(View.INVISIBLE);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelMy == null){
            mModelMy = ModelMy.newInstance(mThis,"我的",R.layout.my_layout,mUserInfo);//"我的"
            ((ModelMy)mModelMy).ModelMyUserInfoSet(mUserInfo);
            transaction.add(R.id.framepage,mModelMy);
        }else{
            ((ModelMy)mModelMy).ModelMyUserInfoSet(mUserInfo);
            transaction.show(mModelMy);
        }
        transaction.commit();
    }

    //跳转登录界面
    public void Page_LogIn(){
        //隐藏所有的底部按钮
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelLogIn == null){
            mModelLogIn = ModelLogIn.newInstance(mThis,"登录",R.layout.modellogin);//"登录"
            transaction.add(R.id.framepage,mModelLogIn);
        }else{
            transaction.show(mModelLogIn);
            ((ModelLogIn)mModelLogIn).LogInMainShow();
        }
        transaction.commit();
    }

    //跳转公开课
    public void Page_OpenClass(){
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "公开课";
        mBeforePage = "首页";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelOpenClass == null){
            mModelOpenClass = ModelOpenClass.newInstance(mThis,"公开课",R.layout.openclass_layout);
            transaction.add(R.id.framepage,mModelOpenClass);
        }else{
            ((ModelOpenClass)mModelOpenClass).CourseMainShow();
            transaction.show(mModelOpenClass);
        }
        transaction.commit();
    }

    //跳转新闻资讯
    public void Page_News(){
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mPage = "新闻资讯";
        mBeforePage = "首页";
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(mModelNews == null){
            mModelNews = ModelNews.newInstance(mThis,"新闻资讯",R.layout.news_layout);
            transaction.add(R.id.framepage,mModelNews);
        }else{
            ((ModelNews)mModelNews).NewsMainShow();
            transaction.show(mModelNews);
        }
        transaction.commit();
    }

    //跳转新闻资讯-新闻详情
    public void Page_NewsDetails(){
        mBeforePage = mBeforePage + "/" + mPage ;
        mPage = "新闻详情";
    }

    //点击新闻的返回按钮
    public void onClickNewsReturn(View view) { //新闻的返回
        String beforePageS[] = mBeforePage.split("/");
        if (beforePageS.length <= 0){
            return;
        } else if (beforePageS.length >= 1){
            if (mPage.equals("新闻详情") && beforePageS[beforePageS.length - 1].equals("新闻资讯")){
                Page_News();
            }
        } else {
            if (mPage.equals("新闻资讯") && mBeforePage.equals("首页")) { //如果当前界面是新闻资讯，点击返回按钮，应该返回到首页
                Page_HomePage();
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String beforePageS[] = mBeforePage.split("/");
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (beforePageS.length <= 0){
                return false;
            } else if (beforePageS.length >= 1){
                if (beforePageS[beforePageS.length - 1].equals("课程包") && mPage.equals("课程包详情")){//说明上个界面是课程包界面
                    Page_MoreCoursePacket();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("课程包") && mPage.equals("课程包搜索")){//说明上个界面是课程包界面
                    Page_MoreCoursePacket();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("课程包详情")){ //说明上个界面是首页
                    Page_HomePage();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("我的课程包") && mPage.equals("课程包详情")){ //说明上个界面是我的课程包界面
                    mPage = "我的课程包";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyClassPacketShow();
                    }
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("我的收藏") && mPage.equals("课程包详情")){ //说明上个界面是我的收藏界面
                    mPage = "我的收藏";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyCollectShow();
                    }
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("课程") && mPage.equals("课程详情")){ //说明上个界面是课程界面
                    Page_Course();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("课程详情")){ //说明上个界面是首页界面
                    Page_HomePage();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("课程")){ //说明上个界面是首页界面
                    Page_HomePage();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("课程包")){ //说明上个界面是首页界面
                    Page_HomePage();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("我的课程") && mPage.equals("课程详情")){//说明上个界面是我的课程界面
                    mPage = "我的课程";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyClassShow();
                    }
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("我的收藏") && mPage.equals("课程详情")){ //说明上个界面是我的收藏界面
                    mPage = "我的收藏";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyCollectShow();
                    }
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("题库") || beforePageS[beforePageS.length - 1].equals("我的题库")
                        || beforePageS[beforePageS.length - 1].equals("题库更多") || beforePageS[beforePageS.length - 1].equals("题库详情")
                        || beforePageS[beforePageS.length - 1].equals("做题设置")){ //说明上个界面是题库、做题设置、题库详情、题库更多或我的题库
                    Page_QuestionBank();
                    mBeforePage = "";
                    for (int i = 0 ; i < beforePageS.length - 1; i ++){
                        if (i == beforePageS.length - 2){
                            mBeforePage = mBeforePage + beforePageS[i];
                        } else {
                            mBeforePage = mBeforePage + beforePageS[i] + "/";
                        }
                    }
                    mPage = beforePageS[beforePageS.length - 1];
                    return true;
                } else if (mPage.equals("消息详情") && beforePageS[beforePageS.length - 1].equals("我的消息")){//如果当前界面是消息详情，点击返回按钮，应该返回到我的消息
                    mPage = "我的消息";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyMessageShow();
                    }
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("课程详情")){ //说明上个界面是课程详情界面
                    mBeforePage = "";
                    for (int i = 0 ; i < beforePageS.length - 1; i ++){
                        if (i == beforePageS.length - 2){
                            mBeforePage = mBeforePage + beforePageS[i];
                        } else {
                            mBeforePage = mBeforePage + beforePageS[i] + "/";
                        }
                    }
                    mPage = beforePageS[beforePageS.length - 1];
                    modelOrderDetails.onClickOrderDetailsReturn();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("订单详情") && mPage.equals("选择优惠券")){ //说明上个界面是订单详情界面
                    mBeforePage = "";
                    for (int i = 0 ; i < beforePageS.length - 1; i ++){
                        if (i == beforePageS.length - 2){
                            mBeforePage = mBeforePage + beforePageS[i];
                        } else {
                            mBeforePage = mBeforePage + beforePageS[i] + "/";
                        }
                    }
                    mPage = beforePageS[beforePageS.length - 1];
                    modelOrderDetails.onClickOrderDetailsChooseCouponReturn();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("订单详情") && mPage.equals("银行卡支付")){ //说明上个界面是订单详情界面
                    mBeforePage = "";
                    for (int i = 0 ; i < beforePageS.length - 1; i ++){
                        if (i == beforePageS.length - 2){
                            mBeforePage = mBeforePage + beforePageS[i];
                        } else {
                            mBeforePage = mBeforePage + beforePageS[i] + "/";
                        }
                    }
                    mPage = beforePageS[beforePageS.length - 1];
                    modelOrderDetails.onClickOrderDetailsBankCardReturn();
                    return true;
                } else if (beforePageS[beforePageS.length - 1].equals("课程包详情")){ //说明上个界面是课程包详情界面
                    mBeforePage = "";
                    for (int i = 0 ; i < beforePageS.length - 1; i ++){
                        if (i == beforePageS.length - 2){
                            mBeforePage = mBeforePage + beforePageS[i];
                        } else {
                            mBeforePage = mBeforePage + beforePageS[i] + "/";
                        }
                    }
                    mPage = beforePageS[beforePageS.length - 1];
                    modelOrderDetails.onClickOrderDetailsReturn();
                    return true;
                } else if (mPage.equals("新闻详情") && beforePageS[beforePageS.length - 1].equals("新闻资讯")){//如果当前界面是新闻详情，点击返回按钮，应该返回到新闻资讯
                    Page_News();
                    return true;
                } else if (mPage.equals("登录") && mBeforePage.equals("我的")) { //如果当前界面是登录界面，点击返回按钮，应该返回到我的界面
                    Page_My();
                    return true;
                } else if (mPage.equals("注册") && mBeforePage.equals("登录")) { //如果当前界面是注册，点击返回按钮，应该返回到登录界面
                    mPage = "登录";
                    mBeforePage = "我的";
                    Page_LogIn();
                    return true;
                } else if (mPage.equals("忘记密码") && mBeforePage.equals("登录")) { //如果当前界面是忘记密码，点击返回按钮，应该返回到登录界面
                    mPage = "登录";
                    mBeforePage = "我的";
                    Page_LogIn();
                    return true;
                } else if (mPage.equals("设置") && mBeforePage.equals("我的")) { //如果当前界面是设置，点击返回按钮，应该返回到我的界面
                    Page_My();
                    return true;
                } else if (mPage.equals("基本信息") && mBeforePage.equals("设置")) { //如果当前界面是设置-基本信息，点击返回按钮，应该返回到设置
                    if (mModelSetting != null) {
                        mPage = "设置";
                        mBeforePage = "我的";
                        ((ModelSetting) mModelSetting).SettingMainShow(mUserInfo,0);
                    }
                    return true;
                } else if (mPage.equals("基本信息") && mBeforePage.equals("我的")) { //如果当前界面是设置-基本信息，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("登录") && mBeforePage.equals("设置")) { //如果当前界面是登录，点击返回按钮，应该返回到设置
                    if (mModelSetting != null) {
                        mPage = "设置";
                        mBeforePage = "我的";
                        //隐藏所有的底部按钮
                        mBottomNavigationView.setVisibility(View.INVISIBLE);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        hideAllFragment(transaction);
                        if(mModelSetting == null){
                            mModelSetting = ModelSetting.newInstance(mThis,"设置-基本信息",R.layout.modelsetting,mUserInfo);//"设置"
                            transaction.add(R.id.framepage,mModelSetting);
                        } else {
                            transaction.show(mModelSetting);
                        }
                        transaction.commit();
                    }
                    return true;
                } else if (mPage.equals("修改名称") && mBeforePage.equals("基本信息")) { //如果当前界面是修改名称，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("修改签名") && mBeforePage.equals("基本信息")) { //如果当前界面是修改签名，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("修改邮箱") && mBeforePage.equals("基本信息")) { //如果当前界面是修改邮箱，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("修改电话号码") && mBeforePage.equals("基本信息")) { //如果当前界面是修改电话号码，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("修改证件号码") && mBeforePage.equals("基本信息")) { //如果当前界面是修改证件号码，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("修改密码") && mBeforePage.equals("基本信息")) { //如果当前界面是修改密码，点击返回按钮，应该返回到基本信息
                    if (mModelSetting != null) {
                        mPage = "基本信息";
                        mBeforePage = "设置";
                        ((ModelSetting)mModelSetting).SettingBaseInfoMainShow(mUserInfo,1);
                    }
                    return true;
                } else if (mPage.equals("关于我们") && mBeforePage.equals("设置")) { //如果当前界面是关于我们，点击返回按钮，应该返回到设置
                    if (mModelSetting != null) {
                        mPage = "设置";
                        mBeforePage = "我的";
                        ((ModelSetting) mModelSetting).SettingMainShow(mUserInfo,0);
                    }
                    return true;
                } else if (mPage.equals("课程") && mBeforePage.equals("首页")) { //如果当前界面是课程，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("课程包") && mBeforePage.equals("首页")) { //如果当前界面是课程包，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("题库") && mBeforePage.equals("首页")) { //如果当前界面是题库，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("题库答题卡") && mBeforePage.equals("题库详情")) { //如果当前界面是题库详情，点击返回按钮，应该返回到题库详情
                    mPage = "题库详情";
                    mBeforePage = "题库";
                    if (mModelQuestionBank != null) {
                        ((ModelQuestionBank) mModelQuestionBank).QuestionBankDetailsQuestionModeShow();
                    }
                    return true;
                } else if (mPage.equals("公开课") && mBeforePage.equals("首页")) { //如果当前界面是公开课，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("社区问答") && mBeforePage.equals("首页")) { //如果当前界面是社区问答，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("问答搜索") && mBeforePage.equals("社区问答")) { //如果当前界面是问答搜索，点击返回按钮，应该返回到社区问答
                    Page_CommunityAnswer();
                    return true;
                } else if (mPage.equals("添加问答") && mBeforePage.equals("社区问答")) { //如果当前界面是添加问答，点击返回按钮，应该返回到社区问答
                    mPage = "社区问答";
                    mBeforePage = "首页";
                    if (mModelCommunityAnswer != null) {
                        ((ModelCommunityAnswer) mModelCommunityAnswer).CommunityAnswerAddReturn();
                    }
                    return true;
                } else if (mPage.equals("选择标签") && mBeforePage.equals("添加问答")) { //如果当前界面是选择标签，点击返回按钮，应该返回到添加问答
                    if (mModelCommunityAnswer != null) {
                        ((ModelCommunityAnswer) mModelCommunityAnswer).CommunityAnswerAddInit();
                    }
                    mPage = "添加问答";
                    mBeforePage = "社区问答";
                    return true;
                } else if (mPage.equals("我的课程") && mBeforePage.equals("我的")) { //如果当前界面是我的课程，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("我的课程包") && mBeforePage.equals("我的")) { //如果当前界面是我的课程包，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("我的收藏") && mBeforePage.equals("我的")) { //如果当前界面是我的收藏，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("我的题库") && mBeforePage.equals("我的")) { //如果当前界面是我的题库，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("课程表") && mBeforePage.equals("我的课程")) { //如果当前界面是我的课程，点击返回按钮，应该返回到我的
                    if(mModelMy != null){
                        Page_My();
                        mBottomNavigationView.setVisibility(View.INVISIBLE);
                        mPage = "我的课程";
                        mBeforePage = "我的";
                        ((ModelMy) mModelMy).MyClassShow();
                    }
                    return true;
                } else  if (mPage.equals("我的缓存") && mBeforePage.equals("我的")) { //如果当前界面是我的缓存，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("我的订单") && mBeforePage.equals("我的")) { //如果当前界面是我的订单，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("订单详情") && mBeforePage.equals("我的订单")) { //如果当前界面是订单详情，点击返回按钮，应该返回到我的订单
                    mPage = "我的订单";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyOrderShow();
                    }
                    return true;
                } else if (mPage.equals("我的优惠券") && mBeforePage.equals("我的")) { //如果当前界面是我的优惠券，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("我的消息") && mBeforePage.equals("我的")) { //如果当前界面是我的消息，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("新闻资讯") && mBeforePage.equals("首页")) { //如果当前界面是新闻资讯，点击返回按钮，应该返回到首页
                    Page_HomePage();
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                } else if (mPage.equals("我的问答") && mBeforePage.equals("我的")) { //如果当前界面是我的问答，点击返回按钮，应该返回到我的
                    Page_My();
                    return true;
                } else if (mPage.equals("问答详情") && beforePageS[beforePageS.length - 1].equals("我的问答")){//如果当前界面是消息详情，点击返回按钮，应该返回到我的消息
                    mPage = "我的问答";
                    mBeforePage = "我的";
                    if(mModelMy != null){
                        ((ModelMy) mModelMy).MyAnswerShow();
                    }
                    return true;
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //判断邮箱格式是否正确
    public static boolean isEmail(String email){
        if (null==email || "".equals(email))
            return false;
        Pattern p =  Pattern.compile("^^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //判断手机号码是否正确
    public static boolean isTelNumber(String telNumber){
        if (null == telNumber || "".equals(telNumber))
            return false;
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(telNumber);
        return m.find();//boolean
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr
     *            身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static boolean isIdNumber(String IDStr) throws ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        // =====================(end)=====================
        return true;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    private static boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的摄像头、音频录制、扩展卡读写使用权限", 1, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
//        processExtraData();
//        checkUpdateVersion();
    }
    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }
    //激活相机操作
    private void goCamera() {
        if (mModelSetting != null){
            ((ModelSetting) mModelSetting).SetttingButtonDialogCancel();
        }
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            uri = FileProvider.getUriForFile(ControlMainActivity.this, "com.android.hzjy.hzjyproduct.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ControlMainActivity.this.startActivityForResult(intent, CAMERA);
    }
    //激活相册操作
    private void goPhotoAlbum() {
        if (mModelSetting != null){
            ((ModelSetting) mModelSetting).SetttingButtonDialogCancel();
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Log.w("", "{onActivityResult}resultCode="+resultCode);
        Log.w("", "{onActivityResult}requestCode="+requestCode);
        if (resultCode == Activity.RESULT_OK) {
            //选择照片
            if(requestCode == ControllerGlobals.CHOOSE_PIC_REQUEST_CODE){
                String beforePageS[] = mBeforePage.split("/");
                if (beforePageS.length < 1){
                    return;
                }
                if (mModelCourse != null && beforePageS[beforePageS.length - 1].equals("课程") && mPage.equals("课程详情")){
                    ((ModelCourse)mModelCourse).ModelCourseCoverQuestionPictureAdd(data);
                } else if (mModelHomePage != null && beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("课程详情")){
                    ((ModelHomePage)mModelHomePage).ModelCourseCoverQuestionPictureAdd(data);
                } else if (mModelCommunityAnswer != null && beforePageS[beforePageS.length - 1].equals("首页") && mPage.equals("社区问答")){
                    ((ModelCommunityAnswer)mModelCommunityAnswer).CommunityAnswerPictureAdd(data);
                }
            }
        }

        switch (requestCode) {
            // 裁剪相机照片
            case CAMERA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    picPath = String.valueOf(cameraSavePath);
                } else {
                    picPath = uri.getEncodedPath();
                }
                Log.d("返回图片路径拍照:", picPath);
                mOutImage = new File(picPath);
                setCropPhoto();
                break;
            //裁剪本地相册
            case ALBUM:
                picPath = ModelGetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                Log.d("返回图片路径相册:", picPath);
                mOutImage = new File(picPath);
                setCropPhoto();
                break;

            //裁剪完成
            case CUPREQUEST:
                if (data == null) {
                    return;
                }
//                picPath = ModelGetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                Bundle extras = data.getExtras();
                String s = "";
                if (Build.MANUFACTURER.contains("Xiaomi")){
                    if (uritempFile !=null){
                        s = uritempFile.getPath();
                    }
                } else {
                    if (extras != null){
                        Bitmap photo = extras.getParcelable("data");
                        Drawable drawable = new BitmapDrawable(null, photo);
                    }
                }
                //s : 裁剪后的图片路径  将界面直接跳转到我的，并且将剪切好的头像上传到服务器
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCropPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0剪裁图片 同理 需要把uri包装
            //通过FileProvider创建一个content类型的Uri
            Uri inputUri = FileProvider.getUriForFile(ControlMainActivity.this,
                    "com.android.hzjy.hzjyproduct.fileprovider", mOutImage);
            startPhotoZoom(inputUri);//设置输入类型
        } else {
            Uri inputUri = Uri.fromFile(mOutImage);
            startPhotoZoom(inputUri);
        }
    }
    private final        int CUPREQUEST        = 50;
    private final        int CAMERA            = 10;
    private final        int ALBUM             = 20;
    private Uri uritempFile;
    private String          picPath;
    private File            mOutImage;
    private HttpRequest httpRequest;

    //裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //去除默认的人脸识别，否则和剪裁匡重叠
            intent.putExtra("noFaceDetection", false);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 宽高的比例
        //华为特殊处理 不然会显示圆
        if (android.os.Build.MODEL.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //miui系统 特殊处理 return-data的方式只适用于小图。
        if (Build.MANUFACTURER.contains("Xiaomi")){//裁剪后的图片Uri路径，uritempFile为Uri类变量
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "tt.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        } else {
            intent.putExtra("return-data", true);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CUPREQUEST);
    }

    /**
     * 登录-直播或回放
     */
    public void LoginLiveOrPlayback(String id,String name,String password,int type) {
        //服务器请求
        String params = type == PlayType.LIVE ? String.format(MainConsts.LIVE_LOGIN_PARAM, id, password, name, type) : String.format(MainConsts.PLAYBACK_LOGIN_PARAM, id, password, type);
        if (httpRequest == null)
            httpRequest = new HttpRequest(this);
        httpRequest.sendRequestWithPost(MainConsts.LOGIN_URL, params, new HttpRequest.IHttpRequestListener() {
            @Override
            public void onRequestCompleted(String responseStr) {
                try {
                    JSONObject jsonObject = new JSONObject(responseStr);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        if (data != null) {
                            String token = data.optString("access_token");
                            String logo = data.optString("logo");
                            String title = data.optString("title");
                            int modeType = data.optInt("modetype");
                            int smallType = data.optInt("smallType");
                            Bundle bundle = new Bundle();
                            bundle.putString(LoginJumpActivity.TOKEN_PARAM, token);
                            bundle.putString(LoginJumpActivity.LOG0_PARAM, logo);
                            bundle.putString(LoginJumpActivity.TITLE_PARAM, title);
                            bundle.putInt(LoginJumpActivity.TYPE_PARAM, type);
                            bundle.putString(LoginJumpActivity.ID_PARAM, id);
                            bundle.putInt(LoginJumpActivity.MODE_TYPE, modeType);
                            bundle.putInt(LoginJumpActivity.SMALL_TYPE, smallType);
                            if (type == PlayType.LIVE) {
                                ActivityUtil.jump(ControlMainActivity.this, LoginJumpActivity.class, bundle);
                            } else {
                                requestPlaybackType(token, bundle);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOError(String errorStr) {
            }
        });
    }

    private void requestPlaybackType(String token, final Bundle bundle) {
        PreDataRequestManager preDataRequestManager = new PreDataRequestManager(this);
        preDataRequestManager.requestPlaybackData(token, new PlaybackDataConverter(), new PreDataForPlaybackInitModel.Callback<PrePlaybackEntity>() {
            @Override
            public void success(PrePlaybackEntity result) {
                bundle.putString(LoginJumpActivity.VIDEO_TYPE, result.getVideoType());
                ActivityUtil.jump(ControlMainActivity.this, LoginJumpActivity.class, bundle);
            }

            @Override
            public void failed(int code, String msg) {
//                tvErrorTip.setText(msg);
            }
        });
    }
}