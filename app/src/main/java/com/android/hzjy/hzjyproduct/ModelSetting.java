package com.android.hzjy.hzjyproduct;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ModelSetting extends Fragment {
    private static ControlMainActivity mControlMainActivity;
    private TextView mTextView;
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int width = 1024;
    static private UserInfo mUserInfo = new UserInfo();
    private Dialog mCameraDialog = null;
    private static String mContext = "";
    //设置密码是否可见，默认为不可见
    private boolean mOldPasswordIsOpenEye = false;
    private boolean mNewPasswordIsOpenEye = false;
    private boolean mNewAgainPasswordIsOpenEye = false;

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage,UserInfo userInfo){
        mContext = context;
        if (userInfo != null) {
            mUserInfo.mUserHeadUrl = userInfo.mUserHeadUrl;
            mUserInfo.mUserName = userInfo.mUserName;
            mUserInfo.mUserLoginState = userInfo.mUserLoginState;
            mUserInfo.mUserIntroduce = userInfo.mUserIntroduce;
            mUserInfo.mUserId = userInfo.mUserId;
            mUserInfo.mUserEmail = userInfo.mUserEmail;
            mUserInfo.mUserTeleNum = userInfo.mUserTeleNum;
            mUserInfo.mUserIdNum = userInfo.mUserIdNum;
        }
        mControlMainActivity = content;
        ModelSetting myFragment = new ModelSetting();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage,container,false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
//        height = dm.heightPixels;
        width = dm.widthPixels;
        SettingMainInit();
        SettingBaseInfoMainInit();
        SetttingButtonDialogInit();
        SettingPersonalStatementUpdateInit();
        SettingUserNameUpdateInit();
        SettingEmailUpdateInit();
        SettingTelNumberUpdateInit();
        SettingIdNumberUpdateInit();
        SettingPasswordUpdateInit();
        SettingAboutUsInit();
        HideAllLayout();
        if (mContext.equals("设置")) {
            RelativeLayout setting_main = mview.findViewById(R.id.setting_main);
            LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_main.getLayoutParams();
            LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
            LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
            setting_main.setLayoutParams(LP);
            setting_main.setVisibility(View.VISIBLE);
            Button setting_logout_button = mview.findViewById(R.id.setting_logout_button);
            setting_logout_button.setVisibility(View.VISIBLE);
            TextView setting_essentialinformation_textview = mview.findViewById(R.id.setting_essentialinformation_textview);
            setting_essentialinformation_textview.setText(R.string.title_essentialinformation);
            if (mUserInfo.mUserLoginState.equals("0")) {
                //没登录不显示退出登录按钮
                setting_logout_button.setVisibility(View.INVISIBLE);
                //基本信息 后面改为立即登录
                setting_essentialinformation_textview.setText(R.string.title_loginclick);
            }
        } else if (mContext.equals("设置-基本信息")){
            TextView setting_essentialinformation_return_text = mview.findViewById(R.id.setting_essentialinformation_return_text);
            setting_essentialinformation_return_text.setText(R.string.title_myinfo);
            RelativeLayout setting_essentialinformation_main = mview.findViewById(R.id.setting_essentialinformation_main);
            LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_essentialinformation_main.getLayoutParams();
            LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
            LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
            setting_essentialinformation_main.setLayoutParams(LP);
            setting_essentialinformation_main.setVisibility(View.VISIBLE);
            TextView essentialinformation_id_value_textview = mview.findViewById(R.id.essentialinformation_id_value_textview);
            essentialinformation_id_value_textview.setText("");
            TextView essentialinformation_name_value_textview = mview.findViewById(R.id.essentialinformation_name_value_textview);
            essentialinformation_name_value_textview.setText("");
            TextView essentialinformation_sign_value_textview = mview.findViewById(R.id.essentialinformation_sign_value_textview);
            essentialinformation_sign_value_textview.setText("");
            TextView essentialinformation_email_value_textview = mview.findViewById(R.id.essentialinformation_email_value_textview);
            essentialinformation_email_value_textview.setText("");
            TextView essentialinformation_tel_value_textview = mview.findViewById(R.id.essentialinformation_tel_value_textview);
            essentialinformation_tel_value_textview.setText("");
            TextView essentialinformation_idnumber_value_textview = mview.findViewById(R.id.essentialinformation_idnumber_value_textview);
            essentialinformation_idnumber_value_textview.setText("");
            if (mUserInfo.mUserLoginState.equals("1")){ //登录状态
                //账号 后面改为账号
                essentialinformation_id_value_textview.setText(mUserInfo.mUserId);
                //用户名 后面改为用户名
                essentialinformation_name_value_textview.setText(mUserInfo.mUserName);
                //个人说明
                essentialinformation_sign_value_textview.setText(mUserInfo.mUserIntroduce);
                //email
                essentialinformation_email_value_textview.setText(mUserInfo.mUserEmail);
                //电话号码
                essentialinformation_tel_value_textview.setText(mUserInfo.mUserTeleNum);
                //证件号码
                essentialinformation_idnumber_value_textview.setText(mUserInfo.mUserIdNum);
            }
        }
        return mview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void SettingMainShow(UserInfo userInfo ,int returnString){ // returnString:  0:我的
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserHeadUrl = userInfo.mUserHeadUrl;
        mUserInfo.mUserName = userInfo.mUserName;
        mUserInfo.mUserLoginState = userInfo.mUserLoginState;
        mUserInfo.mUserIntroduce = userInfo.mUserIntroduce;
        HideAllLayout();
        RelativeLayout setting_main = mview.findViewById(R.id.setting_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_main.setLayoutParams(LP);
        setting_main.setVisibility(View.VISIBLE);
        Button setting_logout_button = mview.findViewById(R.id.setting_logout_button);
        setting_logout_button.setVisibility(View.VISIBLE);
        TextView setting_essentialinformation_textview = mview.findViewById(R.id.setting_essentialinformation_textview);
        setting_essentialinformation_textview.setText(R.string.title_essentialinformation);
        if (mUserInfo.mUserLoginState.equals("0")){
            //没登录不显示退出登录按钮
            setting_logout_button.setVisibility(View.INVISIBLE);
            //基本信息 后面改为立即登录
            setting_essentialinformation_textview.setText(R.string.title_loginclick);
        }
        if (returnString == 0) {
            TextView setting_return_text = mview.findViewById(R.id.setting_return_text);
            setting_return_text.setText(R.string.title_myinfo);
        }
    }
    //显示设置基本信息的详细界面
    public void SettingBaseInfoMainShow(UserInfo userInfo ,int returnString){ // returnString:  0:我的  1:设置
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserHeadUrl = userInfo.mUserHeadUrl;
        mUserInfo.mUserName = userInfo.mUserName;
        mUserInfo.mUserLoginState = userInfo.mUserLoginState;
        mUserInfo.mUserIntroduce = userInfo.mUserIntroduce;
        mUserInfo.mUserId = userInfo.mUserId;
        mUserInfo.mUserEmail = userInfo.mUserEmail;
        mUserInfo.mUserTeleNum = userInfo.mUserTeleNum;
        mUserInfo.mUserIdNum = userInfo.mUserIdNum;
        HideAllLayout();
        RelativeLayout setting_essentialinformation_main = mview.findViewById(R.id.setting_essentialinformation_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_essentialinformation_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_essentialinformation_main.setLayoutParams(LP);
        setting_essentialinformation_main.setVisibility(View.VISIBLE);
        TextView essentialinformation_id_value_textview = mview.findViewById(R.id.essentialinformation_id_value_textview);
        essentialinformation_id_value_textview.setText("");
        TextView essentialinformation_name_value_textview = mview.findViewById(R.id.essentialinformation_name_value_textview);
        essentialinformation_name_value_textview.setText("");
        TextView essentialinformation_sign_value_textview = mview.findViewById(R.id.essentialinformation_sign_value_textview);
        essentialinformation_sign_value_textview.setText("");
        TextView essentialinformation_email_value_textview = mview.findViewById(R.id.essentialinformation_email_value_textview);
        essentialinformation_email_value_textview.setText("");
        TextView essentialinformation_tel_value_textview = mview.findViewById(R.id.essentialinformation_tel_value_textview);
        essentialinformation_tel_value_textview.setText("");
        TextView essentialinformation_idnumber_value_textview = mview.findViewById(R.id.essentialinformation_idnumber_value_textview);
        essentialinformation_idnumber_value_textview.setText("");
        if (mUserInfo.mUserLoginState.equals("1")){ //登录状态
            //账号 后面改为账号
            essentialinformation_id_value_textview.setText(mUserInfo.mUserId);
            //用户名 后面改为用户名
            essentialinformation_name_value_textview.setText(mUserInfo.mUserName);
            //个人说明
            essentialinformation_sign_value_textview.setText(mUserInfo.mUserIntroduce);
            //email
            essentialinformation_email_value_textview.setText(mUserInfo.mUserEmail);
            //电话号码
            essentialinformation_tel_value_textview.setText(mUserInfo.mUserTeleNum);
            //证件号码
            essentialinformation_idnumber_value_textview.setText(mUserInfo.mUserIdNum);
        }
        if (returnString == 0) {
            TextView setting_essentialinformation_return_text = mview.findViewById(R.id.setting_essentialinformation_return_text);
            setting_essentialinformation_return_text.setText(R.string.title_myinfo);
        } else if (returnString == 1) {
            TextView setting_essentialinformation_return_text = mview.findViewById(R.id.setting_essentialinformation_return_text);
            setting_essentialinformation_return_text.setText(R.string.title_setting);
        }
    }

    //显示设置基本信息的修改用户名界面
    public void SettingUserNameUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserName = userInfo.mUserName;
        HideAllLayout();
        RelativeLayout setting_usernameupdate_main = mview.findViewById(R.id.setting_usernameupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_usernameupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_usernameupdate_main.setLayoutParams(LP);
        setting_usernameupdate_main.setVisibility(View.VISIBLE);
        EditText setting_usernameupdate_deittext = mview.findViewById(R.id.setting_usernameupdate_deittext);
        setting_usernameupdate_deittext.setText(mUserInfo.mUserName);
        setting_usernameupdate_deittext.setEnabled(true);
        setting_usernameupdate_deittext.setFocusable(true);
        setting_usernameupdate_deittext.setFocusableInTouchMode(true);
        setting_usernameupdate_deittext.requestFocus();
        setting_usernameupdate_deittext.setSelection(setting_usernameupdate_deittext.getText().toString().length());
    }

    //显示设置基本信息的修改个人说明界面
    public void SettingPersonalStatementUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserIntroduce = userInfo.mUserIntroduce;
        HideAllLayout();
        RelativeLayout setting_personalstatementupdate_main = mview.findViewById(R.id.setting_personalstatementupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_personalstatementupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_personalstatementupdate_main.setLayoutParams(LP);
        setting_personalstatementupdate_main.setVisibility(View.VISIBLE);
        EditText setting_personalstatementupdate_edittext = mview.findViewById(R.id.setting_personalstatementupdate_edittext);
        setting_personalstatementupdate_edittext.setText(mUserInfo.mUserIntroduce);
        setting_personalstatementupdate_edittext.setEnabled(true);
        setting_personalstatementupdate_edittext.setFocusable(true);
        setting_personalstatementupdate_edittext.setFocusableInTouchMode(true);
        setting_personalstatementupdate_edittext.requestFocus();
        setting_personalstatementupdate_edittext.setSelection(setting_personalstatementupdate_edittext.getText().toString().length());
    }

    //显示设置基本信息的修改邮箱界面
    public void SettingEmailUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserEmail = userInfo.mUserEmail;
        HideAllLayout();
        RelativeLayout setting_emailupdate_main = mview.findViewById(R.id.setting_emailupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_emailupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_emailupdate_main.setLayoutParams(LP);
        setting_emailupdate_main.setVisibility(View.VISIBLE);
        EditText setting_emailupdate_edittext = mview.findViewById(R.id.setting_emailupdate_edittext);
        setting_emailupdate_edittext.setText(mUserInfo.mUserEmail);
        setting_emailupdate_edittext.setEnabled(true);
        setting_emailupdate_edittext.setFocusable(true);
        setting_emailupdate_edittext.setFocusableInTouchMode(true);
        setting_emailupdate_edittext.requestFocus();
        setting_emailupdate_edittext.setSelection(setting_emailupdate_edittext.getText().toString().length());
    }

    //显示设置基本信息的修改手机号码界面
    public void SettingTelNumberUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserTeleNum = userInfo.mUserTeleNum;
        HideAllLayout();
        RelativeLayout setting_telnumberupdate_main = mview.findViewById(R.id.setting_telnumberupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_telnumberupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_telnumberupdate_main.setLayoutParams(LP);
        setting_telnumberupdate_main.setVisibility(View.VISIBLE);
        EditText setting_telnumberupdate_edittext = mview.findViewById(R.id.setting_telnumberupdate_edittext);
        setting_telnumberupdate_edittext.setText(mUserInfo.mUserTeleNum);
        setting_telnumberupdate_edittext.setEnabled(true);
        setting_telnumberupdate_edittext.setFocusable(true);
        setting_telnumberupdate_edittext.setFocusableInTouchMode(true);
        setting_telnumberupdate_edittext.requestFocus();
        setting_telnumberupdate_edittext.setSelection(setting_telnumberupdate_edittext.getText().toString().length());
    }

    //显示设置基本信息的修改证件号码界面
    public void SettingIdNumberUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserIdNum = userInfo.mUserIdNum;
        HideAllLayout();
        RelativeLayout setting_idnumberupdate_main = mview.findViewById(R.id.setting_idnumberupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_idnumberupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_idnumberupdate_main.setLayoutParams(LP);
        setting_idnumberupdate_main.setVisibility(View.VISIBLE);
        EditText setting_idnumberupdate_edittext = mview.findViewById(R.id.setting_idnumberupdate_edittext);
        setting_idnumberupdate_edittext.setText(mUserInfo.mUserIdNum);
        setting_idnumberupdate_edittext.setEnabled(true);
        setting_idnumberupdate_edittext.setFocusable(true);
        setting_idnumberupdate_edittext.setFocusableInTouchMode(true);
        setting_idnumberupdate_edittext.requestFocus();
        setting_idnumberupdate_edittext.setSelection(setting_idnumberupdate_edittext.getText().toString().length());
    }

    //显示设置基本信息的修改用户密码界面
    public void SettingPasswordUpdateShow(UserInfo userInfo) {
        if (mview == null || userInfo == null){
            return;
        }
        mUserInfo.mUserPassword = userInfo.mUserPassword;
        HideAllLayout();
        RelativeLayout setting_passwordupdate_main = mview.findViewById(R.id.setting_passwordupdate_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_passwordupdate_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setting_passwordupdate_main.setLayoutParams(LP);
        setting_passwordupdate_main.setVisibility(View.VISIBLE);
        EditText setting_passwordupdateoldpassword_edittext = mview.findViewById(R.id.setting_passwordupdateoldpassword_edittext);
        setting_passwordupdateoldpassword_edittext.setEnabled(true);
        setting_passwordupdateoldpassword_edittext.setFocusable(true);
        setting_passwordupdateoldpassword_edittext.setFocusableInTouchMode(true);
        setting_passwordupdateoldpassword_edittext.requestFocus();
        setting_passwordupdateoldpassword_edittext.setSelection(setting_passwordupdateoldpassword_edittext.getText().toString().length());
        EditText setting_passwordupdatenew_edittext = mview.findViewById(R.id.setting_passwordupdatenew_edittext);
        EditText setting_passwordupdatenewagain_edittext = mview.findViewById(R.id.setting_passwordupdatenewagain_edittext);
        //设置密码不可见
        mOldPasswordIsOpenEye = false;
        mNewPasswordIsOpenEye = false;
        mNewAgainPasswordIsOpenEye = false;
        setting_passwordupdateoldpassword_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setting_passwordupdatenew_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setting_passwordupdatenewagain_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setting_passwordupdateoldpassword_edittext.setText("");
        setting_passwordupdatenew_edittext.setText("");
        setting_passwordupdatenewagain_edittext.setText("");
    }

    //显示设置-关于我们界面
    public void SettingAboutUsShow() {
        if (mview == null){
            return;
        }
        HideAllLayout();
        RelativeLayout aboutus_main = mview.findViewById(R.id.aboutus_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) aboutus_main.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        aboutus_main.setLayoutParams(LP);
        aboutus_main.setVisibility(View.VISIBLE);
        //查询版本号，如果有新的版本号 在版本检测后面添加红点
    }

    //隐藏所有图层
    private void HideAllLayout(){
        RelativeLayout setting_main = mview.findViewById(R.id.setting_main);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) setting_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_main.setLayoutParams(LP);
        setting_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_essentialinformation_main = mview.findViewById(R.id.setting_essentialinformation_main);
        LP = (LinearLayout.LayoutParams) setting_essentialinformation_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_essentialinformation_main.setLayoutParams(LP);
        setting_essentialinformation_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_usernameupdate_main = mview.findViewById(R.id.setting_usernameupdate_main);
        LP = (LinearLayout.LayoutParams) setting_usernameupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_usernameupdate_main.setLayoutParams(LP);
        setting_usernameupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_personalstatementupdate_main = mview.findViewById(R.id.setting_personalstatementupdate_main);
        LP = (LinearLayout.LayoutParams) setting_personalstatementupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_personalstatementupdate_main.setLayoutParams(LP);
        setting_personalstatementupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_emailupdate_main = mview.findViewById(R.id.setting_emailupdate_main);
        LP = (LinearLayout.LayoutParams) setting_emailupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_emailupdate_main.setLayoutParams(LP);
        setting_emailupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_telnumberupdate_main = mview.findViewById(R.id.setting_telnumberupdate_main);
        LP = (LinearLayout.LayoutParams) setting_telnumberupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_telnumberupdate_main.setLayoutParams(LP);
        setting_telnumberupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_idnumberupdate_main = mview.findViewById(R.id.setting_idnumberupdate_main);
        LP = (LinearLayout.LayoutParams) setting_idnumberupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_idnumberupdate_main.setLayoutParams(LP);
        setting_idnumberupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout setting_passwordupdate_main = mview.findViewById(R.id.setting_passwordupdate_main);
        LP = (LinearLayout.LayoutParams) setting_passwordupdate_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        setting_passwordupdate_main.setLayoutParams(LP);
        setting_passwordupdate_main.setVisibility(View.INVISIBLE);
        RelativeLayout aboutus_main = mview.findViewById(R.id.aboutus_main);
        LP = (LinearLayout.LayoutParams) aboutus_main.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        aboutus_main.setLayoutParams(LP);
        aboutus_main.setVisibility(View.INVISIBLE);
    }

    //初始化设置主界面
    public void SettingMainInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_returnRelativeLayout = mview.findViewById(R.id.setting_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_returnRelativeLayout.setLayoutParams(lp);
        //返回
        ImageView setting_return_button = mview.findViewById(R.id.setting_return_button);
        RelativeLayout.LayoutParams setting_return_buttonLp = (RelativeLayout.LayoutParams) setting_return_button.getLayoutParams();
        setting_return_buttonLp.height = width / 15;
        setting_return_buttonLp.width = width / 15;
        setting_return_button.setLayoutParams(setting_return_buttonLp);
        //功能列表
        //基本信息
        TextView setting_essentialinformation_textview = mview.findViewById(R.id.setting_essentialinformation_textview);
        RelativeLayout.LayoutParams setting_essentialinformation_textviewlp = (RelativeLayout.LayoutParams) setting_essentialinformation_textview.getLayoutParams();
        setting_essentialinformation_textviewlp.topMargin = bottomMargin;
        setting_essentialinformation_textviewlp.height = layoutheight;
        setting_essentialinformation_textviewlp.leftMargin = leftMargin;
        setting_essentialinformation_textviewlp.bottomMargin = bottomMargin;
        setting_essentialinformation_textview.setLayoutParams(setting_essentialinformation_textviewlp);
        TextView essentialinformation_value_textview = mview.findViewById(R.id.essentialinformation_value_textview);
        RelativeLayout.LayoutParams essentialinformation_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_value_textview.getLayoutParams();
        essentialinformation_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_value_textviewlp.height = layoutheight;
        essentialinformation_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_value_textview.setLayoutParams(essentialinformation_value_textviewlp);
        ImageView setting_essentialinformation_go = mview.findViewById(R.id.setting_essentialinformation_go);
        RelativeLayout.LayoutParams setting_essentialinformation_golp = (RelativeLayout.LayoutParams) setting_essentialinformation_go.getLayoutParams();
        setting_essentialinformation_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_essentialinformation_golp.rightMargin = rightMargin;
        setting_essentialinformation_golp.height = width / 25;
        setting_essentialinformation_golp.width = width / 15;
        setting_essentialinformation_golp.bottomMargin = layoutheight / 3;
        setting_essentialinformation_go.setLayoutParams(setting_essentialinformation_golp);
        //清除缓存
        TextView setting_clearcache_textview = mview.findViewById(R.id.setting_clearcache_textview);
        RelativeLayout.LayoutParams setting_clearcache_textviewlp = (RelativeLayout.LayoutParams) setting_clearcache_textview.getLayoutParams();
        setting_clearcache_textviewlp.topMargin = bottomMargin;
        setting_clearcache_textviewlp.height = layoutheight;
        setting_clearcache_textviewlp.leftMargin = leftMargin;
        setting_clearcache_textviewlp.bottomMargin = bottomMargin;
        setting_clearcache_textview.setLayoutParams(setting_clearcache_textviewlp);
        TextView clearcache_value_textview = mview.findViewById(R.id.clearcache_value_textview);
        RelativeLayout.LayoutParams clearcache_value_textviewlp = (RelativeLayout.LayoutParams) clearcache_value_textview.getLayoutParams();
        clearcache_value_textviewlp.topMargin = bottomMargin;
        clearcache_value_textviewlp.height = layoutheight;
        clearcache_value_textviewlp.bottomMargin = bottomMargin;
        clearcache_value_textview.setLayoutParams(clearcache_value_textviewlp);
        ImageView setting_clearcache_go = mview.findViewById(R.id.setting_clearcache_go);
        RelativeLayout.LayoutParams setting_clearcache_golp = (RelativeLayout.LayoutParams) setting_clearcache_go.getLayoutParams();
        setting_clearcache_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_clearcache_golp.rightMargin = rightMargin;
        setting_clearcache_golp.height = width / 25;
        setting_clearcache_golp.width = width / 15;
        setting_clearcache_golp.bottomMargin = layoutheight / 3;
        setting_clearcache_go.setLayoutParams(setting_clearcache_golp);
        //允许非WiFi网络播放/缓存视频
        TextView setting_allownonwifiplay_textview = mview.findViewById(R.id.setting_allownonwifiplay_textview);
        RelativeLayout.LayoutParams setting_allownonwifiplay_textviewlp = (RelativeLayout.LayoutParams) setting_allownonwifiplay_textview.getLayoutParams();
        setting_allownonwifiplay_textviewlp.topMargin = bottomMargin;
        setting_allownonwifiplay_textviewlp.height = layoutheight;
        setting_allownonwifiplay_textviewlp.leftMargin = leftMargin;
        setting_allownonwifiplay_textviewlp.bottomMargin = bottomMargin;
        setting_allownonwifiplay_textview.setLayoutParams(setting_allownonwifiplay_textviewlp);
        TextView allownonwifiplay_value_textview = mview.findViewById(R.id.allownonwifiplay_value_textview);
        RelativeLayout.LayoutParams allownonwifiplay_value_textviewlp = (RelativeLayout.LayoutParams) allownonwifiplay_value_textview.getLayoutParams();
        allownonwifiplay_value_textviewlp.topMargin = bottomMargin;
        allownonwifiplay_value_textviewlp.height = layoutheight;
        allownonwifiplay_value_textviewlp.bottomMargin = bottomMargin;
        allownonwifiplay_value_textview.setLayoutParams(allownonwifiplay_value_textviewlp);
        ModelSwitchButton setting_allownonwifiplay_go = mview.findViewById(R.id.setting_allownonwifiplay_go);
        RelativeLayout.LayoutParams setting_allownonwifiplay_golp = (RelativeLayout.LayoutParams) setting_allownonwifiplay_go.getLayoutParams();
        setting_allownonwifiplay_golp.topMargin = (layoutheight + bottomMargin * 2) / 4;
        setting_allownonwifiplay_golp.rightMargin = rightMargin;
        setting_allownonwifiplay_golp.height = (int) ((layoutheight + bottomMargin * 2) / 1.8);
        setting_allownonwifiplay_golp.width = layoutheight + bottomMargin * 2;
        setting_allownonwifiplay_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 4;
        setting_allownonwifiplay_go.setLayoutParams(setting_allownonwifiplay_golp);
        setting_allownonwifiplay_go.setChecked(true);
        setting_allownonwifiplay_go.setOnCheckedChangeListener((view,isChecked) ->{
            //TODO do your job
            mControlMainActivity.onClickSettingAllowNonWifiPlay(isChecked);
        });
//        //版本
//        TextView setting_version_textview = mview.findViewById(R.id.setting_version_textview);
//        RelativeLayout.LayoutParams setting_version_textviewlp = (RelativeLayout.LayoutParams) setting_version_textview.getLayoutParams();
//        setting_version_textviewlp.topMargin = bottomMargin;
//        setting_version_textviewlp.height = layoutheight;
//        setting_version_textviewlp.leftMargin = leftMargin;
//        setting_version_textviewlp.bottomMargin = bottomMargin;
//        setting_version_textview.setLayoutParams(setting_version_textviewlp);
//        TextView version_value_textview = mview.findViewById(R.id.version_value_textview);
//        RelativeLayout.LayoutParams version_value_textviewlp = (RelativeLayout.LayoutParams) version_value_textview.getLayoutParams();
//        version_value_textviewlp.topMargin = bottomMargin;
//        version_value_textviewlp.height = layoutheight;
//        version_value_textviewlp.bottomMargin = bottomMargin;
//        version_value_textview.setLayoutParams(version_value_textviewlp);
//        ImageView setting_version_go = mview.findViewById(R.id.setting_version_go);
//        RelativeLayout.LayoutParams setting_version_golp = (RelativeLayout.LayoutParams) setting_version_go.getLayoutParams();
//        setting_version_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
//        setting_version_golp.rightMargin = rightMargin;
//        setting_version_golp.height = width / 25;
//        setting_version_golp.width = width / 15;
//        setting_version_golp.bottomMargin = layoutheight / 3;
//        setting_version_go.setLayoutParams(setting_version_golp);
        //关于我们
        TextView setting_aboutus_textview = mview.findViewById(R.id.setting_aboutus_textview);
        RelativeLayout.LayoutParams setting_aboutus_textviewlp = (RelativeLayout.LayoutParams) setting_aboutus_textview.getLayoutParams();
        setting_aboutus_textviewlp.topMargin = bottomMargin;
        setting_aboutus_textviewlp.height = layoutheight;
        setting_aboutus_textviewlp.leftMargin = leftMargin;
        setting_aboutus_textviewlp.bottomMargin = bottomMargin;
        setting_aboutus_textview.setLayoutParams(setting_aboutus_textviewlp);
        TextView aboutus_value_textview = mview.findViewById(R.id.aboutus_value_textview);
        RelativeLayout.LayoutParams aboutus_value_textviewlp = (RelativeLayout.LayoutParams) aboutus_value_textview.getLayoutParams();
        aboutus_value_textviewlp.topMargin = bottomMargin;
        aboutus_value_textviewlp.height = layoutheight;
        aboutus_value_textviewlp.bottomMargin = bottomMargin;
        aboutus_value_textview.setLayoutParams(aboutus_value_textviewlp);
        ImageView setting_aboutus_go = mview.findViewById(R.id.setting_aboutus_go);
        RelativeLayout.LayoutParams setting_aboutus_golp = (RelativeLayout.LayoutParams) setting_aboutus_go.getLayoutParams();
        setting_aboutus_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_aboutus_golp.rightMargin = rightMargin;
        setting_aboutus_golp.height = width / 25;
        setting_aboutus_golp.width = width / 15;
        setting_aboutus_golp.bottomMargin = layoutheight / 3;
        setting_aboutus_go.setLayoutParams(setting_aboutus_golp);
//        //隐私政策
//        TextView setting_privacypolicy_textview = mview.findViewById(R.id.setting_privacypolicy_textview);
//        RelativeLayout.LayoutParams setting_privacypolicy_textviewlp = (RelativeLayout.LayoutParams) setting_privacypolicy_textview.getLayoutParams();
//        setting_privacypolicy_textviewlp.topMargin = bottomMargin;
//        setting_privacypolicy_textviewlp.height = layoutheight;
//        setting_privacypolicy_textviewlp.leftMargin = leftMargin;
//        setting_privacypolicy_textviewlp.bottomMargin = bottomMargin;
//        setting_privacypolicy_textview.setLayoutParams(setting_privacypolicy_textviewlp);
//        TextView privacypolicy_value_textview = mview.findViewById(R.id.privacypolicy_value_textview);
//        RelativeLayout.LayoutParams privacypolicy_value_textviewlp = (RelativeLayout.LayoutParams) privacypolicy_value_textview.getLayoutParams();
//        privacypolicy_value_textviewlp.topMargin = bottomMargin;
//        privacypolicy_value_textviewlp.height = layoutheight;
//        privacypolicy_value_textviewlp.bottomMargin = bottomMargin;
//        privacypolicy_value_textview.setLayoutParams(privacypolicy_value_textviewlp);
//        ImageView setting_privacypolicy_go = mview.findViewById(R.id.setting_privacypolicy_go);
//        RelativeLayout.LayoutParams setting_privacypolicy_golp = (RelativeLayout.LayoutParams) setting_privacypolicy_go.getLayoutParams();
//        setting_privacypolicy_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
//        setting_privacypolicy_golp.rightMargin = rightMargin;
//        setting_privacypolicy_golp.height = width / 25;
//        setting_privacypolicy_golp.width = width / 15;
//        setting_privacypolicy_golp.bottomMargin = layoutheight / 3;
//        setting_privacypolicy_go.setLayoutParams(setting_privacypolicy_golp);
        //设置退出当前账号按钮
        Button setting_logout_button = mview.findViewById(R.id.setting_logout_button);
        lp = (RelativeLayout.LayoutParams) setting_logout_button.getLayoutParams();
        lp.leftMargin = rightMargin;
        lp.rightMargin = rightMargin;
        lp.height = width / 6;
        lp.bottomMargin = bottomMargin;
        setting_logout_button.setLayoutParams(lp);
    }

    //初始化设置-基本信息主界面
    public void SettingBaseInfoMainInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_essentialinformation_returnRelativeLayout = mview.findViewById(R.id.setting_essentialinformation_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_essentialinformation_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_essentialinformation_returnRelativeLayout.setLayoutParams(lp);
        //返回
        ImageView setting_essentialinformation_return_button = mview.findViewById(R.id.setting_essentialinformation_return_button);
        RelativeLayout.LayoutParams setting_essentialinformation_return_buttonLp = (RelativeLayout.LayoutParams) setting_essentialinformation_return_button.getLayoutParams();
        setting_essentialinformation_return_buttonLp.height = width / 15;
        setting_essentialinformation_return_buttonLp.width = width / 15;
        setting_essentialinformation_return_button.setLayoutParams(setting_essentialinformation_return_buttonLp);
        //功能列表
        //头像
        TextView ssentialinformation_icon_textview = mview.findViewById(R.id.ssentialinformation_icon_textview);
        RelativeLayout.LayoutParams ssentialinformation_icon_textviewlp = (RelativeLayout.LayoutParams) ssentialinformation_icon_textview.getLayoutParams();
        ssentialinformation_icon_textviewlp.topMargin = bottomMargin;
        ssentialinformation_icon_textviewlp.height = layoutheight;
        ssentialinformation_icon_textviewlp.leftMargin = leftMargin;
        ssentialinformation_icon_textviewlp.bottomMargin = bottomMargin;
        ssentialinformation_icon_textview.setLayoutParams(ssentialinformation_icon_textviewlp);
        TextView essentialinformation_icon_textview = mview.findViewById(R.id.essentialinformation_icon_textview);
        RelativeLayout.LayoutParams essentialinformation_icon_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_icon_textview.getLayoutParams();
        essentialinformation_icon_textviewlp.topMargin = bottomMargin;
        essentialinformation_icon_textviewlp.height = layoutheight;
        essentialinformation_icon_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_icon_textview.setLayoutParams(essentialinformation_icon_textviewlp);
        ImageView essentialinformation_icon_go = mview.findViewById(R.id.essentialinformation_icon_go);
        RelativeLayout.LayoutParams essentialinformation_icon_golp = (RelativeLayout.LayoutParams) essentialinformation_icon_go.getLayoutParams();
        essentialinformation_icon_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_icon_golp.rightMargin = rightMargin;
        essentialinformation_icon_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_icon_golp.width = width / 15;
        essentialinformation_icon_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_icon_go.setLayoutParams(essentialinformation_icon_golp);
        //账号
        TextView essentialinformation_id_textview = mview.findViewById(R.id.essentialinformation_id_textview);
        RelativeLayout.LayoutParams essentialinformation_id_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_id_textview.getLayoutParams();
        essentialinformation_id_textviewlp.topMargin = bottomMargin;
        essentialinformation_id_textviewlp.height = layoutheight;
        essentialinformation_id_textviewlp.leftMargin = leftMargin;
        essentialinformation_id_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_id_textview.setLayoutParams(essentialinformation_id_textviewlp);
        TextView essentialinformation_id_value_textview = mview.findViewById(R.id.essentialinformation_id_value_textview);
        RelativeLayout.LayoutParams essentialinformation_id_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_id_value_textview.getLayoutParams();
        essentialinformation_id_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_id_value_textviewlp.height = layoutheight;
        essentialinformation_id_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_id_value_textviewlp.rightMargin = rightMargin;
        essentialinformation_id_value_textview.setLayoutParams(essentialinformation_id_value_textviewlp);
        //姓名
        TextView essentialinformation_name_textview = mview.findViewById(R.id.essentialinformation_name_textview);
        RelativeLayout.LayoutParams essentialinformation_name_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_name_textview.getLayoutParams();
        essentialinformation_name_textviewlp.topMargin = bottomMargin;
        essentialinformation_name_textviewlp.height = layoutheight;
        essentialinformation_name_textviewlp.leftMargin = leftMargin;
        essentialinformation_name_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_name_textview.setLayoutParams(essentialinformation_name_textviewlp);
        TextView essentialinformation_name_value_textview = mview.findViewById(R.id.essentialinformation_name_value_textview);
        RelativeLayout.LayoutParams essentialinformation_name_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_name_value_textview.getLayoutParams();
        essentialinformation_name_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_name_value_textviewlp.height = layoutheight;
        essentialinformation_name_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_name_value_textview.setLayoutParams(essentialinformation_name_value_textviewlp);
        ImageView essentialinformation_name_go = mview.findViewById(R.id.essentialinformation_name_go);
        RelativeLayout.LayoutParams essentialinformation_name_golp = (RelativeLayout.LayoutParams) essentialinformation_name_go.getLayoutParams();
        essentialinformation_name_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_name_golp.rightMargin = rightMargin;
        essentialinformation_name_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_name_golp.width = width / 15;
        essentialinformation_name_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_name_go.setLayoutParams(essentialinformation_name_golp);
        //签名
        TextView essentialinformation_sign_textview = mview.findViewById(R.id.essentialinformation_sign_textview);
        RelativeLayout.LayoutParams essentialinformation_sign_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_sign_textview.getLayoutParams();
        essentialinformation_sign_textviewlp.topMargin = bottomMargin;
        essentialinformation_sign_textviewlp.height = layoutheight;
        essentialinformation_sign_textviewlp.leftMargin = leftMargin;
        essentialinformation_sign_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_sign_textview.setLayoutParams(essentialinformation_sign_textviewlp);
        TextView essentialinformation_sign_value_textview = mview.findViewById(R.id.essentialinformation_sign_value_textview);
        RelativeLayout.LayoutParams essentialinformation_sign_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_sign_value_textview.getLayoutParams();
        essentialinformation_sign_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_sign_value_textviewlp.height = layoutheight;
        essentialinformation_sign_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_sign_value_textview.setLayoutParams(essentialinformation_sign_value_textviewlp);
        ImageView essentialinformation_sign_go = mview.findViewById(R.id.essentialinformation_sign_go);
        RelativeLayout.LayoutParams essentialinformation_sign_golp = (RelativeLayout.LayoutParams) essentialinformation_sign_go.getLayoutParams();
        essentialinformation_sign_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_sign_golp.rightMargin = rightMargin;
        essentialinformation_sign_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_sign_golp.width = width / 15;
        essentialinformation_sign_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_sign_go.setLayoutParams(essentialinformation_sign_golp);
        //邮箱
        TextView essentialinformation_email_textview = mview.findViewById(R.id.essentialinformation_email_textview);
        RelativeLayout.LayoutParams essentialinformation_email_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_email_textview.getLayoutParams();
        essentialinformation_email_textviewlp.topMargin = bottomMargin;
        essentialinformation_email_textviewlp.height = layoutheight;
        essentialinformation_email_textviewlp.leftMargin = leftMargin;
        essentialinformation_email_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_email_textview.setLayoutParams(essentialinformation_email_textviewlp);
        TextView essentialinformation_email_value_textview = mview.findViewById(R.id.essentialinformation_email_value_textview);
        RelativeLayout.LayoutParams essentialinformation_email_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_email_value_textview.getLayoutParams();
        essentialinformation_email_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_email_value_textviewlp.height = layoutheight;
        essentialinformation_email_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_email_value_textview.setLayoutParams(essentialinformation_email_value_textviewlp);
        ImageView essentialinformation_email_go = mview.findViewById(R.id.essentialinformation_email_go);
        RelativeLayout.LayoutParams essentialinformation_email_golp = (RelativeLayout.LayoutParams) essentialinformation_email_go.getLayoutParams();
        essentialinformation_email_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_email_golp.rightMargin = rightMargin;
        essentialinformation_email_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_email_golp.width = width / 15;
        essentialinformation_email_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_email_go.setLayoutParams(essentialinformation_email_golp);
        //电话号码
        TextView essentialinformation_tel_textview = mview.findViewById(R.id.essentialinformation_tel_textview);
        RelativeLayout.LayoutParams essentialinformation_tel_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_tel_textview.getLayoutParams();
        essentialinformation_tel_textviewlp.topMargin = bottomMargin;
        essentialinformation_tel_textviewlp.height = layoutheight;
        essentialinformation_tel_textviewlp.leftMargin = leftMargin;
        essentialinformation_tel_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_tel_textview.setLayoutParams(essentialinformation_tel_textviewlp);
        TextView essentialinformation_tel_value_textview = mview.findViewById(R.id.essentialinformation_tel_value_textview);
        RelativeLayout.LayoutParams essentialinformation_tel_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_tel_value_textview.getLayoutParams();
        essentialinformation_tel_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_tel_value_textviewlp.height = layoutheight;
        essentialinformation_tel_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_tel_value_textview.setLayoutParams(essentialinformation_tel_value_textviewlp);
        ImageView essentialinformation_tel_go = mview.findViewById(R.id.essentialinformation_tel_go);
        RelativeLayout.LayoutParams essentialinformation_tel_golp = (RelativeLayout.LayoutParams) essentialinformation_tel_go.getLayoutParams();
        essentialinformation_tel_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_tel_golp.rightMargin = rightMargin;
        essentialinformation_tel_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_tel_golp.width = width / 15;
        essentialinformation_tel_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_tel_go.setLayoutParams(essentialinformation_tel_golp);
        //证件号码
        TextView essentialinformation_idnumber_textview = mview.findViewById(R.id.essentialinformation_idnumber_textview);
        RelativeLayout.LayoutParams essentialinformation_idnumber_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_idnumber_textview.getLayoutParams();
        essentialinformation_idnumber_textviewlp.topMargin = bottomMargin;
        essentialinformation_idnumber_textviewlp.height = layoutheight;
        essentialinformation_idnumber_textviewlp.leftMargin = leftMargin;
        essentialinformation_idnumber_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_idnumber_textview.setLayoutParams(essentialinformation_idnumber_textviewlp);
        TextView essentialinformation_idnumber_value_textview = mview.findViewById(R.id.essentialinformation_idnumber_value_textview);
        RelativeLayout.LayoutParams essentialinformation_idnumber_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_idnumber_value_textview.getLayoutParams();
        essentialinformation_idnumber_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_idnumber_value_textviewlp.height = layoutheight;
        essentialinformation_idnumber_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_idnumber_value_textview.setLayoutParams(essentialinformation_idnumber_value_textviewlp);
        ImageView essentialinformation_idnumber_go = mview.findViewById(R.id.essentialinformation_idnumber_go);
        RelativeLayout.LayoutParams essentialinformation_idnumber_golp = (RelativeLayout.LayoutParams) essentialinformation_idnumber_go.getLayoutParams();
        essentialinformation_idnumber_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_idnumber_golp.rightMargin = rightMargin;
        essentialinformation_idnumber_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_idnumber_golp.width = width / 15;
        essentialinformation_idnumber_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_idnumber_go.setLayoutParams(essentialinformation_idnumber_golp);
        //修改密码
        TextView essentialinformation_updatapassword_textview = mview.findViewById(R.id.essentialinformation_updatapassword_textview);
        RelativeLayout.LayoutParams essentialinformation_updatapassword_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_updatapassword_textview.getLayoutParams();
        essentialinformation_updatapassword_textviewlp.topMargin = bottomMargin;
        essentialinformation_updatapassword_textviewlp.height = layoutheight;
        essentialinformation_updatapassword_textviewlp.leftMargin = leftMargin;
        essentialinformation_updatapassword_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_updatapassword_textview.setLayoutParams(essentialinformation_updatapassword_textviewlp);
        TextView essentialinformation_updatapassword_value_textview = mview.findViewById(R.id.essentialinformation_updatapassword_value_textview);
        RelativeLayout.LayoutParams essentialinformation_updatapassword_value_textviewlp = (RelativeLayout.LayoutParams) essentialinformation_updatapassword_value_textview.getLayoutParams();
        essentialinformation_updatapassword_value_textviewlp.topMargin = bottomMargin;
        essentialinformation_updatapassword_value_textviewlp.height = layoutheight;
        essentialinformation_updatapassword_value_textviewlp.bottomMargin = bottomMargin;
        essentialinformation_updatapassword_value_textview.setLayoutParams(essentialinformation_updatapassword_value_textviewlp);
        ImageView essentialinformation_updatapassword_go = mview.findViewById(R.id.essentialinformation_updatapassword_go);
        RelativeLayout.LayoutParams essentialinformation_updatapassword_golp = (RelativeLayout.LayoutParams) essentialinformation_updatapassword_go.getLayoutParams();
        essentialinformation_updatapassword_golp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_updatapassword_golp.rightMargin = rightMargin;
        essentialinformation_updatapassword_golp.height = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_updatapassword_golp.width = width / 15;
        essentialinformation_updatapassword_golp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        essentialinformation_updatapassword_go.setLayoutParams(essentialinformation_updatapassword_golp);
    }

    private void SetttingButtonDialogInit() {
        mCameraDialog = new Dialog(mControlMainActivity, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(mControlMainActivity).inflate(
                R.layout.modelsetting_buttondialog, null);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
    }
    public void SetttingButtonDialogShow(){
        if (mCameraDialog != null){
            mCameraDialog.show();
        }
    }

    public void SetttingButtonDialogCancel(){
        if (mCameraDialog != null){
            mCameraDialog.cancel();
        }
    }

    private void SettingUserNameUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_usernameupdate_returnRelativeLayout = mview.findViewById(R.id.setting_usernameupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_usernameupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_usernameupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_usernameupdate_return_text = mview.findViewById(R.id.setting_usernameupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_usernameupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_usernameupdate_return_text.setLayoutParams(lp);
        //修改名称的书写框
        EditText setting_usernameupdate_deittext = mview.findViewById(R.id.setting_usernameupdate_deittext);
        lp = (RelativeLayout.LayoutParams) setting_usernameupdate_deittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_usernameupdate_deittext.setLayoutParams(lp);
        setting_usernameupdate_deittext.setText(mUserInfo.mUserName);
        setting_usernameupdate_deittext.setEnabled(true);
        setting_usernameupdate_deittext.setFocusable(true);
        setting_usernameupdate_deittext.setFocusableInTouchMode(true);
        setting_usernameupdate_deittext.requestFocus();
        setting_usernameupdate_deittext.setSelection(setting_usernameupdate_deittext.getText().toString().length());
        //完成
        Button setting_usernameupdate_finish = mview.findViewById(R.id.setting_usernameupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_usernameupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_usernameupdate_finish.setLayoutParams(lp);
        //清空全部文字
        ImageView setting_usernameupdate_clearbutton = mview.findViewById(R.id.setting_usernameupdate_clearbutton);
        lp = (RelativeLayout.LayoutParams) setting_usernameupdate_clearbutton.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_usernameupdate_clearbutton.setLayoutParams(lp);
    }

    public void SettingUserNameUpdateClear(){
        if (mview == null){
            return;
        }
        EditText setting_usernameupdate_deittext = mview.findViewById(R.id.setting_usernameupdate_deittext);
        setting_usernameupdate_deittext.setText("");
    }

    public String UserNameGet(){
        if (mview == null){
            return mUserInfo.mUserName;
        }
        EditText setting_usernameupdate_deittext = mview.findViewById(R.id.setting_usernameupdate_deittext);
        return setting_usernameupdate_deittext.getText().toString();
    }

    private void SettingPersonalStatementUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_personalstatementupdate_returnRelativeLayout = mview.findViewById(R.id.setting_personalstatementupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_personalstatementupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_personalstatementupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_personalstatementupdate_return_text = mview.findViewById(R.id.setting_personalstatementupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_personalstatementupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_personalstatementupdate_return_text.setLayoutParams(lp);
        //修改名称的书写框
        EditText setting_personalstatementupdate_edittext = mview.findViewById(R.id.setting_personalstatementupdate_edittext);
        lp = (RelativeLayout.LayoutParams) setting_personalstatementupdate_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = (int) ((layoutheight + bottomMargin * 2) * 1.5);
        setting_personalstatementupdate_edittext.setLayoutParams(lp);
        setting_personalstatementupdate_edittext.setText(mUserInfo.mUserIntroduce);
        setting_personalstatementupdate_edittext.setEnabled(true);
        setting_personalstatementupdate_edittext.setFocusable(true);
        setting_personalstatementupdate_edittext.setFocusableInTouchMode(true);
        setting_personalstatementupdate_edittext.requestFocus();
        //设置EditText的显示方式为多行文本输入
        setting_personalstatementupdate_edittext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        setting_personalstatementupdate_edittext.setSingleLine(false);
        //水平滚动设置为False
        setting_personalstatementupdate_edittext.setHorizontallyScrolling(false);
        setting_personalstatementupdate_edittext.setSelection(setting_personalstatementupdate_edittext.getText().toString().length());
        //完成
        Button setting_personalstatementupdate_finish = mview.findViewById(R.id.setting_personalstatementupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_personalstatementupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_personalstatementupdate_finish.setLayoutParams(lp);
    }

    public String PersonalStatementGet(){
        if (mview == null){
            return mUserInfo.mUserIntroduce;
        }
        EditText setting_personalstatementupdate_edittext = mview.findViewById(R.id.setting_personalstatementupdate_edittext);
        return setting_personalstatementupdate_edittext.getText().toString();
    }

    private void SettingEmailUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_emailupdate_returnRelativeLayout = mview.findViewById(R.id.setting_emailupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_emailupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_emailupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_emailupdate_return_text = mview.findViewById(R.id.setting_emailupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_emailupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_emailupdate_return_text.setLayoutParams(lp);
        //修改邮箱的书写框
        EditText setting_emailupdate_edittext = mview.findViewById(R.id.setting_emailupdate_edittext);
        lp = (RelativeLayout.LayoutParams) setting_emailupdate_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_emailupdate_edittext.setLayoutParams(lp);
        setting_emailupdate_edittext.setText(mUserInfo.mUserEmail);
        setting_emailupdate_edittext.setEnabled(true);
        setting_emailupdate_edittext.setFocusable(true);
        setting_emailupdate_edittext.setFocusableInTouchMode(true);
        setting_emailupdate_edittext.requestFocus();
        setting_emailupdate_edittext.setSelection(setting_emailupdate_edittext.getText().toString().length());
        //完成
        Button setting_emailupdate_finish = mview.findViewById(R.id.setting_emailupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_emailupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_emailupdate_finish.setLayoutParams(lp);
        //清空全部文字
        ImageView setting_emailupdate_clearbutton = mview.findViewById(R.id.setting_emailupdate_clearbutton);
        lp = (RelativeLayout.LayoutParams) setting_emailupdate_clearbutton.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_emailupdate_clearbutton.setLayoutParams(lp);
    }

    public void SettingEmailUpdateClear(){
        if (mview == null){
            return;
        }
        EditText setting_emailupdate_edittext = mview.findViewById(R.id.setting_emailupdate_edittext);
        setting_emailupdate_edittext.setText("");
    }

    public String EmailGet(){
        if (mview == null){
            return mUserInfo.mUserEmail;
        }
        EditText setting_emailupdate_edittext = mview.findViewById(R.id.setting_emailupdate_edittext);
        return setting_emailupdate_edittext.getText().toString();
    }

    private void SettingTelNumberUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_telnumberupdate_returnRelativeLayout = mview.findViewById(R.id.setting_telnumberupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_telnumberupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_telnumberupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_telnumberupdate_return_text = mview.findViewById(R.id.setting_telnumberupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_telnumberupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_telnumberupdate_return_text.setLayoutParams(lp);
        //修改手机号的书写框
        EditText setting_telnumberupdate_edittext = mview.findViewById(R.id.setting_telnumberupdate_edittext);
        lp = (RelativeLayout.LayoutParams) setting_telnumberupdate_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_telnumberupdate_edittext.setLayoutParams(lp);
        setting_telnumberupdate_edittext.setText(mUserInfo.mUserTeleNum);
        setting_telnumberupdate_edittext.setEnabled(true);
        setting_telnumberupdate_edittext.setFocusable(true);
        setting_telnumberupdate_edittext.setFocusableInTouchMode(true);
        setting_telnumberupdate_edittext.requestFocus();
        setting_telnumberupdate_edittext.setSelection(setting_telnumberupdate_edittext.getText().toString().length());
        //完成
        Button setting_telnumberupdate_finish = mview.findViewById(R.id.setting_telnumberupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_telnumberupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_telnumberupdate_finish.setLayoutParams(lp);
        //清空全部文字
        ImageView setting_telnumber_clearbutton = mview.findViewById(R.id.setting_telnumberupdate_clearbutton);
        lp = (RelativeLayout.LayoutParams) setting_telnumber_clearbutton.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_telnumber_clearbutton.setLayoutParams(lp);
    }

    public void SettingTelNumberUpdateClear(){
        if (mview == null){
            return;
        }
        EditText setting_telnumberupdate_edittext = mview.findViewById(R.id.setting_telnumberupdate_edittext);
        setting_telnumberupdate_edittext.setText("");
    }

        public String TelNumberGet(){
        if (mview == null){
            return mUserInfo.mUserTeleNum;
        }
        EditText setting_telnumberupdate_edittext = mview.findViewById(R.id.setting_telnumberupdate_edittext);
        return setting_telnumberupdate_edittext.getText().toString();
    }

    private void SettingIdNumberUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_idnumberupdate_returnRelativeLayout = mview.findViewById(R.id.setting_idnumberupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_idnumberupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_idnumberupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_idnumberupdate_return_text = mview.findViewById(R.id.setting_idnumberupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_idnumberupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_idnumberupdate_return_text.setLayoutParams(lp);
        //修改证件号码的书写框
        EditText setting_idnumberupdate_edittext = mview.findViewById(R.id.setting_idnumberupdate_edittext);
        lp = (RelativeLayout.LayoutParams) setting_idnumberupdate_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_idnumberupdate_edittext.setLayoutParams(lp);
        setting_idnumberupdate_edittext.setText(mUserInfo.mUserTeleNum);
        setting_idnumberupdate_edittext.setEnabled(true);
        setting_idnumberupdate_edittext.setFocusable(true);
        setting_idnumberupdate_edittext.setFocusableInTouchMode(true);
        setting_idnumberupdate_edittext.requestFocus();
        setting_idnumberupdate_edittext.setSelection(setting_idnumberupdate_edittext.getText().toString().length());
        //完成
        Button setting_idnumberupdate_finish = mview.findViewById(R.id.setting_idnumberupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_idnumberupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_idnumberupdate_finish.setLayoutParams(lp);
        //清空全部文字
        ImageView setting_idnumberupdate_clearbutton = mview.findViewById(R.id.setting_idnumberupdate_clearbutton);
        lp = (RelativeLayout.LayoutParams) setting_idnumberupdate_clearbutton.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_idnumberupdate_clearbutton.setLayoutParams(lp);
    }

    public void SettingIdNumberUpdateClear(){
        if (mview == null){
            return;
        }
        EditText setting_idnumberupdate_edittext = mview.findViewById(R.id.setting_idnumberupdate_edittext);
        setting_idnumberupdate_edittext.setText("");
    }

    public String IdNumberGet(){
        if (mview == null){
            return mUserInfo.mUserIdNum;
        }
        EditText setting_idnumberupdate_edittext = mview.findViewById(R.id.setting_idnumberupdate_edittext);
        return setting_idnumberupdate_edittext.getText().toString();
    }

    private void SettingPasswordUpdateInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout setting_passwordupdate_returnRelativeLayout = mview.findViewById(R.id.setting_passwordupdate_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) setting_passwordupdate_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        setting_passwordupdate_returnRelativeLayout.setLayoutParams(lp);
        //取消
        TextView setting_passwordupdate_return_text = mview.findViewById(R.id.setting_passwordupdate_return_text);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdate_return_text.getLayoutParams();
        lp.height = layoutheight;
        setting_passwordupdate_return_text.setLayoutParams(lp);
        //完成
        Button setting_passwordupdate_finish = mview.findViewById(R.id.setting_passwordupdate_finish);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdate_finish.getLayoutParams();
        lp.height = layoutheight - bottomMargin;
        lp.width = (layoutheight - bottomMargin) * 2;
        setting_passwordupdate_finish.setLayoutParams(lp);
        //修改密码的书写框（旧密码）
        EditText setting_passwordupdateoldpassword_edittext = mview.findViewById(R.id.setting_passwordupdateoldpassword_edittext);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdateoldpassword_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_passwordupdateoldpassword_edittext.setLayoutParams(lp);
        setting_passwordupdateoldpassword_edittext.setEnabled(true);
        setting_passwordupdateoldpassword_edittext.setFocusable(true);
        setting_passwordupdateoldpassword_edittext.setFocusableInTouchMode(true);
        setting_passwordupdateoldpassword_edittext.requestFocus();
        setting_passwordupdateoldpassword_edittext.setSelection(setting_passwordupdateoldpassword_edittext.getText().toString().length());
        //旧密码是否明码显示
        ImageView setting_passwordupdateoldpassword_isopeneye = mview.findViewById(R.id.setting_passwordupdateoldpassword_isopeneye);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdateoldpassword_isopeneye.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.width = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_passwordupdateoldpassword_isopeneye.setLayoutParams(lp);
        //修改密码的书写框（新密码）
        EditText setting_passwordupdatenew_edittext = mview.findViewById(R.id.setting_passwordupdatenew_edittext);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdatenew_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_passwordupdatenew_edittext.setLayoutParams(lp);
        setting_passwordupdatenew_edittext.setEnabled(true);
        setting_passwordupdatenew_edittext.setFocusable(true);
        setting_passwordupdatenew_edittext.setFocusableInTouchMode(true);
        setting_passwordupdatenew_edittext.requestFocus();
        setting_passwordupdatenew_edittext.setSelection(setting_passwordupdatenew_edittext.getText().toString().length());
        //新密码是否明码显示
        ImageView setting_passwordupdatenew_isopeneye = mview.findViewById(R.id.setting_passwordupdatenew_isopeneye);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdatenew_isopeneye.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.width = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_passwordupdatenew_isopeneye.setLayoutParams(lp);
        //修改密码的书写框（确认密码）
        EditText setting_passwordupdatenewagain_edittext = mview.findViewById(R.id.setting_passwordupdatenewagain_edittext);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdatenewagain_edittext.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.height = layoutheight + bottomMargin * 2;
        setting_passwordupdatenewagain_edittext.setLayoutParams(lp);
        setting_passwordupdatenewagain_edittext.setEnabled(true);
        setting_passwordupdatenewagain_edittext.setFocusable(true);
        setting_passwordupdatenewagain_edittext.setFocusableInTouchMode(true);
        setting_passwordupdatenewagain_edittext.requestFocus();
        setting_passwordupdatenewagain_edittext.setSelection(setting_passwordupdatenewagain_edittext.getText().toString().length());
        //确认密码是否明码显示
        ImageView setting_passwordupdatenewagain_isopeneye = mview.findViewById(R.id.setting_passwordupdatenewagain_isopeneye);
        lp = (RelativeLayout.LayoutParams) setting_passwordupdatenewagain_isopeneye.getLayoutParams();
        lp.height = (layoutheight + bottomMargin * 2) / 3;
        lp.width = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = rightMargin;
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        setting_passwordupdatenewagain_isopeneye.setLayoutParams(lp);
        //设置密码不可见
        setting_passwordupdateoldpassword_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setting_passwordupdatenew_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setting_passwordupdatenewagain_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //密码是否可见
        setting_passwordupdateoldpassword_isopeneye.setOnClickListener(v ->{
            if(!mOldPasswordIsOpenEye) {
                setting_passwordupdateoldpassword_isopeneye.setSelected(true);
                mOldPasswordIsOpenEye = true;
                //密码可见
                setting_passwordupdateoldpassword_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                setting_passwordupdateoldpassword_isopeneye.setSelected(false);
                mOldPasswordIsOpenEye = false;
                //密码不可见
                setting_passwordupdateoldpassword_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
            setting_passwordupdateoldpassword_edittext.setSelection(setting_passwordupdateoldpassword_edittext.getText().toString().length());
        });
        setting_passwordupdatenew_isopeneye.setOnClickListener(v ->{
            if(!mNewPasswordIsOpenEye) {
                setting_passwordupdatenew_isopeneye.setSelected(true);
                mNewPasswordIsOpenEye = true;
                //密码可见
                setting_passwordupdatenew_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                setting_passwordupdatenew_isopeneye.setSelected(false);
                mNewPasswordIsOpenEye = false;
                //密码不可见
                setting_passwordupdatenew_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
            setting_passwordupdatenew_edittext.setSelection(setting_passwordupdatenew_edittext.getText().toString().length());
        });
        setting_passwordupdatenewagain_isopeneye.setOnClickListener(v ->{
            if(!mNewAgainPasswordIsOpenEye) {
                setting_passwordupdatenewagain_isopeneye.setSelected(true);
                mNewAgainPasswordIsOpenEye = true;
                //密码可见
                setting_passwordupdatenewagain_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                setting_passwordupdatenewagain_isopeneye.setSelected(false);
                mNewAgainPasswordIsOpenEye = false;
                //密码不可见
                setting_passwordupdatenewagain_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
            setting_passwordupdatenewagain_edittext.setSelection(setting_passwordupdatenewagain_edittext.getText().toString().length());
        });
    }

    public int NewPasswordSave(){ //返回值  -1：其他错误 1：原密码输入不正确 2：两次新密码不一致 0：保存新密码
        if (mview == null){
            return -1;
        }
        EditText setting_passwordupdateoldpassword_edittext = mview.findViewById(R.id.setting_passwordupdateoldpassword_edittext);
        EditText setting_passwordupdatenew_edittext = mview.findViewById(R.id.setting_passwordupdatenew_edittext);
        EditText setting_passwordupdatenewagain_edittext = mview.findViewById(R.id.setting_passwordupdatenewagain_edittext);
        if (!setting_passwordupdateoldpassword_edittext.getText().toString().equals(mUserInfo.mUserPassword)){ //原密码输入错误
            return 1;
        }
        if (!setting_passwordupdatenew_edittext.getText().toString().equals(setting_passwordupdatenewagain_edittext.getText().toString())){ //两次新密码输入不一致
            return 2;
        }
        mUserInfo.mUserPassword = setting_passwordupdatenewagain_edittext.getText().toString();
        return 0;
    }

    public String NewPasswordGet(){
        return mUserInfo.mUserPassword;
    }

    private void SettingAboutUsInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout aboutus_returnRelativeLayout = mview.findViewById(R.id.aboutus_returnRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) aboutus_returnRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = rightMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        aboutus_returnRelativeLayout.setLayoutParams(lp);
        //返回
        ImageView aboutus_return_button = mview.findViewById(R.id.aboutus_return_button);
        lp = (RelativeLayout.LayoutParams) aboutus_return_button.getLayoutParams();
        lp.height = width / 15;
        lp.width = width / 15;
        aboutus_return_button.setLayoutParams(lp);
        //火种logo
        ControllerCustomRoundAngleImageView aboutus_huozhonglogo = mview.findViewById(R.id.aboutus_huozhonglogo);
        aboutus_huozhonglogo.setImageDrawable(getResources().getDrawable(R.mipmap.logo2));
        lp = (RelativeLayout.LayoutParams) aboutus_huozhonglogo.getLayoutParams();
        lp.topMargin = width / 11;
        lp.height = width / 3;
        lp.width = width / 3;
        aboutus_huozhonglogo.setLayoutParams(lp);
        //应用名称
        TextView aboutus_appname = mview.findViewById(R.id.aboutus_appname);
        lp = (RelativeLayout.LayoutParams) aboutus_appname.getLayoutParams();
        lp.topMargin = width / 11;
        aboutus_appname.setLayoutParams(lp);
        //应用版本号
        PackageManager manager = mControlMainActivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mControlMainActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView aboutus_version = mview.findViewById(R.id.aboutus_version);
        if (info != null) {
            aboutus_version.setText(getResources().getString(R.string.title_version) + " " + info.versionName);
        }
        lp = (RelativeLayout.LayoutParams) aboutus_version.getLayoutParams();
        lp.bottomMargin = width / 11;
        aboutus_version.setLayoutParams(lp);
        //功能列表
        //软件版本
        TextView aboutus_checknewversion_textview = mview.findViewById(R.id.aboutus_checknewversion_textview);
        lp = (RelativeLayout.LayoutParams) aboutus_checknewversion_textview.getLayoutParams();
        lp.topMargin = bottomMargin;
        lp.height = layoutheight;
        lp.leftMargin = leftMargin;
        lp.bottomMargin = bottomMargin;
        aboutus_checknewversion_textview.setLayoutParams(lp);
        ImageView aboutus_checknewversion_new = mview.findViewById(R.id.aboutus_checknewversion_new);
        lp = (RelativeLayout.LayoutParams) aboutus_checknewversion_new.getLayoutParams();
        lp.topMargin = (layoutheight + bottomMargin * 2 - width / 12) / 2;
        lp.width = width / 12;
        lp.height = width / 12;
        lp.leftMargin = leftMargin / 2;
        lp.bottomMargin = (layoutheight + bottomMargin * 2 - width / 12) / 2;
        aboutus_checknewversion_new.setLayoutParams(lp);
        TextView aboutus_checknewversion_new_textview = mview.findViewById(R.id.aboutus_checknewversion_new_textview);
        lp = (RelativeLayout.LayoutParams) aboutus_checknewversion_new_textview.getLayoutParams();
        lp.topMargin = (layoutheight + bottomMargin * 2) / 3;
        lp.rightMargin = leftMargin / 2;
        lp.bottomMargin = (layoutheight + bottomMargin * 2) / 3;
        aboutus_checknewversion_new_textview.setLayoutParams(lp);
        ImageView aboutus_checknewversion_go = mview.findViewById(R.id.aboutus_checknewversion_go);
        lp = (RelativeLayout.LayoutParams) aboutus_checknewversion_go.getLayoutParams();
        lp.topMargin = (layoutheight + bottomMargin * 2 - width / 35) / 2;
        lp.rightMargin = rightMargin;
        lp.height = width / 25;
        lp.width = width / 15;
        lp.bottomMargin = (layoutheight + bottomMargin * 2 - width / 25) / 2;
        aboutus_checknewversion_go.setLayoutParams(lp);
        //版权
        LinearLayout aboutus_agreeTerms_layout = mview.findViewById(R.id.aboutus_agreeTerms_layout);
        lp = (RelativeLayout.LayoutParams) aboutus_agreeTerms_layout.getLayoutParams();
        lp.bottomMargin = layoutheight;
        aboutus_agreeTerms_layout.setLayoutParams(lp);
        TextView aboutus_agreeTerms = mview.findViewById(R.id.aboutus_agreeTerms);
        setHtmlStyle(aboutus_agreeTerms);
        aboutus_agreeTerms.setAutoLinkMask(0);
        aboutus_agreeTerms.setLinkTextColor(getResources().getColor(R.color.blue));
        aboutus_agreeTerms.setMovementMethod(LinkMovementMethod.getInstance()); //设置超链接为可点击状态
        TextView aboutus_agreeTerms_1 = mview.findViewById(R.id.aboutus_agreeTerms_1);
        setHtmlStyle(aboutus_agreeTerms_1);
        aboutus_agreeTerms_1.setAutoLinkMask(0);
        aboutus_agreeTerms_1.setLinkTextColor(getResources().getColor(R.color.blue));
        aboutus_agreeTerms_1.setMovementMethod(LinkMovementMethod.getInstance()); //设置超链接为可点击状态
    }

    //去掉链接的下划线
    private static void setHtmlStyle(TextView textView) {
        Spannable s = new Spannable.Factory().newSpannable(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            super.onClick(widget);
        }
    }

}
