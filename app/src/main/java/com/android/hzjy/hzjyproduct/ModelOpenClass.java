package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hzjy.hzjyproduct.consts.PlayType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ModelOpenClass extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int height = 1344;
    private int width = 720;
    private int mLastTabIndex = 1;
    private String mCurrentTab = "all";

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelOpenClass myFragment = new ModelOpenClass();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage,container,false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        ModelPtrFrameLayout openclass_content_frame = mview.findViewById(R.id.openclass_content_frame);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        openclass_content_frame.addPtrUIHandler(header);
        openclass_content_frame.setHeaderView(header);
        openclass_content_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                openclass_content_frame.refreshComplete();
            }
        });
        TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
        TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
        TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
        TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
        openclass_tab_all.setOnClickListener(this);
        openclass_tab_haveinhand.setOnClickListener(this);
        openclass_tab_begininaminute.setOnClickListener(this);
        openclass_tab_over.setOnClickListener(this);
        CourseMainShow();
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void CourseMainShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        //默认游标位置在全部
        ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
        int x = width / 8 - mview.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        openclass_cursor1.setX(x);
        //默认选中的为全部
        mLastTabIndex = 1;
        mCurrentTab = "all";
        TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
        TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
        TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
        TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
        openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
        openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        LinearLayout openclass_content = mview.findViewById(R.id.openclass_content);
        openclass_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.openclass_layout1, null);
        openclass_content.addView(view);
        //加载公开课封面
        ImageView openclass_cover = view.findViewById(R.id.openclass_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }
            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(openclass_cover);
        //公开课名称
        TextView openclass_classname = view.findViewById(R.id.openclass_classname);
        openclass_classname.setText("案例分析建筑防火知识");
        //公开课的开始和结束时间
        TextView openclass1_time = view.findViewById(R.id.openclass1_time);
        openclass1_time.setText("2019-11-12 12:00-14:00");
        //公开课当前状态
        ImageView openclass1_logo = view.findViewById(R.id.openclass1_logo);
        TextView openclass1_state = view.findViewById(R.id.openclass1_state);
//        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_start));
//        openclass1_state.setTextColor(view.getResources().getColor(R.color.holo_red_dark));
//        openclass1_state.setText("即将开始");

//        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_over));
//        openclass1_state.setTextColor(view.getResources().getColor(R.color.color_69));
//        openclass1_state.setText("已结束");
        //为每个公开课设置监听
        view.setOnClickListener(v->{
//            mControlMainActivity.LoginLiveOrPlayback("391068","dadada","",PlayType.LIVE);
                        mControlMainActivity.LoginLiveOrPlayback("365061","dadada","799723", PlayType.PLAYBACK);
        });
    }
    //隐藏所有图层
    private void HideAllLayout(){
//        RelativeLayout course_mainLayout = mview.findViewById(R.id.course_mainLayout);
//        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_mainLayout.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_mainLayout.setLayoutParams(LP);
//        course_mainLayout.setVisibility(View.INVISIBLE);
//        RelativeLayout course_searchlayout = mview.findViewById(R.id.course_searchlayout);
//        LP = (LinearLayout.LayoutParams) course_searchlayout.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_searchlayout.setLayoutParams(LP);
//        course_searchlayout.setVisibility(View.INVISIBLE);
//        RelativeLayout course_details1 = mview.findViewById(R.id.course_details1);
//        LP = (LinearLayout.LayoutParams) course_details1.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_details1.setLayoutParams(LP);
//        course_details1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openclass_tab_all:{
                if (!mCurrentTab.equals("all")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mLastTabIndex = 1;
                mCurrentTab = "all";
                break;
            }
            case R.id.openclass_tab_haveinhand:{
                if (!mCurrentTab.equals("haveinhand")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width / 4 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mLastTabIndex = 2;
                mCurrentTab = "haveinhand";
                break;
            }
            case R.id.openclass_tab_begininaminute:{
                if (!mCurrentTab.equals("begininaminute")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width * 2 / 4, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mLastTabIndex = 3;
                mCurrentTab = "begininaminute";
                break;
            }
            case R.id.openclass_tab_over:{
                if (!mCurrentTab.equals("over")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width * 3 / 4, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mLastTabIndex = 4;
                mCurrentTab = "over";
                break;
            }
            default:{
                break;
            }
        }
    }
}
