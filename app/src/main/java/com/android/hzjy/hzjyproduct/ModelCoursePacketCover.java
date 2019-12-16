package com.android.hzjy.hzjyproduct;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;

import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ModelCoursePacketCover implements View.OnClickListener  ,ModelOrderDetailsInterface{
    private View modelCoursePacket,mListView,mDetailsView;
    private ControlMainActivity mControlMainActivity = null;
    private ModelCoursePacketCoverOnClickListener mModelCoursePacketCoverOnClickListener = null;
    private int height = 1344;
    private int width = 720;
    private String mCurrentTab = "Details";
    private int lastTabIndex = 1;
    private boolean mIsCollect = false;
    private CoursePacketInfo mCoursePacketInfo;
    public void ModelCoursePacketCoverOnClickListenerSet(ModelCoursePacketCoverOnClickListener modelCoursePacketCoverOnClickListener){
        mModelCoursePacketCoverOnClickListener = modelCoursePacketCoverOnClickListener;
    }
    public View ModelCoursePacketCover(Context context,CoursePacketInfo coursePacketInfo){
        mControlMainActivity = (ControlMainActivity) context;
        if (coursePacketInfo == null){
            return null;
        }
        mCoursePacketInfo = new CoursePacketInfo(coursePacketInfo);
        DisplayMetrics dm = context.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        if (modelCoursePacket == null) {
            modelCoursePacket = LayoutInflater.from(context).inflate(R.layout.modelcoursepacket_layout, null);
            modelCoursePacket.setOnClickListener(v -> {
                if (mModelCoursePacketCoverOnClickListener == null || modelCoursePacket == null){
                    return;
                }
                mModelCoursePacketCoverOnClickListener.OnClickListener(v);
                //跳转到课程包的详细界面
                CoursePacketDetailsShow();
            });
        }
        mListView  = LayoutInflater.from(context).inflate(R.layout.modelcoursepacketlist_layout,null);
        if (mDetailsView == null) {
            mDetailsView = LayoutInflater.from(context).inflate(R.layout.modelcoursepacketdetails_layout, null);
            TextView coursepacket_details_label = mDetailsView.findViewById(R.id.coursepacket_details_label);
            TextView coursepacket_details_label1 = mDetailsView.findViewById(R.id.coursepacket_details_label1);
            TextView coursepacket_coursestage_label = mDetailsView.findViewById(R.id.coursepacket_coursestage_label);
            TextView coursepacket_coursestage_label1 = mDetailsView.findViewById(R.id.coursepacket_coursestage_label1);
            TextView coursepacket_teachers_label1 = mDetailsView.findViewById(R.id.coursepacket_teachers_label1);
            TextView coursepacket_teachers_label = mDetailsView.findViewById(R.id.coursepacket_teachers_label);
            Button coursepacket_details_buy_button = mDetailsView.findViewById(R.id.coursepacket_details_buy_button);
            coursepacket_details_buy_button.setOnClickListener(this);
            LinearLayout coursepacket_details_bottomlayout_collect = mDetailsView.findViewById(R.id.coursepacket_details_bottomlayout_collect);
            coursepacket_details_bottomlayout_collect.setOnClickListener(this);
            coursepacket_details_label.setOnClickListener(this);
            coursepacket_details_label1.setOnClickListener(this);
            coursepacket_coursestage_label.setOnClickListener(this);
            coursepacket_coursestage_label1.setOnClickListener(this);
            coursepacket_teachers_label1.setOnClickListener(this);
            coursepacket_teachers_label.setOnClickListener(this);
        }
        HideAllLayout();
        CoursePacketListInit(coursePacketInfo);
        RelativeLayout coursepacket_main = modelCoursePacket.findViewById(R.id.coursepacket_main);
        coursepacket_main.addView(mListView);
        //课程包详情界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CoursePacketDetailsInit(coursePacketInfo);
        }
        return modelCoursePacket;
    }

    public void CoursePacketListInit(CoursePacketInfo coursePacketInfo){
        ControllerCustomRoundAngleImageView imageView = mListView.findViewById(R.id.coursepacketcover);
        imageView.setImageDrawable(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover));//如果没有url，加载默认图片
        if (coursePacketInfo.mCoursePacketCover != null){
            Glide.with(mControlMainActivity).
                    load(coursePacketInfo.mCoursePacketCover).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("Warn","加载失败 errorMsg:"+(e!=null?e.getMessage():"null"));
                    return false;
                }
                @Override
                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("Warn","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                    return false;
                }
            })
                    .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(imageView);
        }
        TextView coursePacketNameTextView = mListView.findViewById(R.id.coursepacketName);
        if (coursePacketInfo.mCoursePacketName != null) {
            coursePacketNameTextView.setText(coursePacketInfo.mCoursePacketName);
        }
        TextView coursepacketcontentTextView = mListView.findViewById(R.id.coursepacketcontent);
        String content = "";
        if (coursePacketInfo.mCoursePacketStageNum != null) {
            content = "阶段" + coursePacketInfo.mCoursePacketStageNum;
        }
        if (coursePacketInfo.mCoursePacketCourseNum != null){
            content = content + "  •  课程" + coursePacketInfo.mCoursePacketCourseNum;
        }
        if (coursePacketInfo.mCoursePacketLearnPersonNum != null){
            content = content + "  •  " + coursePacketInfo.mCoursePacketLearnPersonNum+ "人已学习";
        }
        coursepacketcontentTextView.setText(content);
        TextView coursepacketpriceTextView = mListView.findViewById(R.id.coursepacketprice);
        if (coursePacketInfo.mCoursePacketPrice != null) {
            if (!coursePacketInfo.mCoursePacketPrice.equals("免费")){
                coursepacketpriceTextView.setTextColor(Color.RED);
                coursepacketpriceTextView.setText("¥" + coursePacketInfo.mCoursePacketPrice);
            } else {
                coursepacketpriceTextView.setText(coursePacketInfo.mCoursePacketPrice);
            }
        }
        TextView coursepacketpriceoldTextView = mListView.findViewById(R.id.coursepacketpriceOld);
        //文字栅格化
        coursepacketpriceoldTextView.setPaintFlags(coursepacketpriceoldTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
        if (coursePacketInfo.mCoursePacketPriceOld != null) {
            if (!coursePacketInfo.mCoursePacketPriceOld.equals("免费")){
                coursepacketpriceoldTextView.setText("¥" + coursePacketInfo.mCoursePacketPriceOld);
            }
        }
    }
    public void CoursePacketDetailsShow() {
        if (modelCoursePacket == null) {
            return;
        }
        HideAllLayout();
        RelativeLayout coursepacket_main = modelCoursePacket.findViewById(R.id.coursepacket_main);
        coursepacket_main.addView(mDetailsView);
        //默认显示详情界面
        TextView coursepacket_details_label = mDetailsView.findViewById(R.id.coursepacket_details_label);
        TextView coursepacket_details_label1 = mDetailsView.findViewById(R.id.coursepacket_details_label1);
        coursepacket_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        coursepacket_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        TextView coursepacket_coursestage_label = mDetailsView.findViewById(R.id.coursepacket_coursestage_label);
        TextView coursepacket_coursestage_label1 = mDetailsView.findViewById(R.id.coursepacket_coursestage_label1);
        coursepacket_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        coursepacket_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        TextView coursepacket_teachers_label1 = mDetailsView.findViewById(R.id.coursepacket_teachers_label1);
        TextView coursepacket_teachers_label = mDetailsView.findViewById(R.id.coursepacket_teachers_label);
        coursepacket_teachers_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        coursepacket_teachers_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        //修改body为课程包详情
        LinearLayout coursepacket_details_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_details_label_content_layout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_details_label_content_layout.getLayoutParams();
        LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        coursepacket_details_label_content_layout.setLayoutParams(LP);
        coursepacket_details_label_content_layout.setVisibility(View.VISIBLE);
        LinearLayout coursepacket_coursestage_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_coursestage_label_content_layout);
        LP = (LinearLayout.LayoutParams) coursepacket_coursestage_label_content_layout.getLayoutParams();
        LP.height = 0;
        coursepacket_coursestage_label_content_layout.setLayoutParams(LP);
        coursepacket_coursestage_label_content_layout.setVisibility(View.INVISIBLE);
        ImageView imgv_cursor = mDetailsView.findViewById(R.id.imgv_cursor);
        ImageView imgv_cursor1 = mDetailsView.findViewById(R.id.imgv_cursor1);
        int x = width / 6 - mDetailsView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        imgv_cursor.setX(x);
        imgv_cursor1.setX(x);
    }

    public void HideAllLayout(){
        RelativeLayout coursepacket_main = modelCoursePacket.findViewById(R.id.coursepacket_main);
        coursepacket_main.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coursepacket_details_label:
            case R.id.coursepacket_details_label1: {
                TextView coursepacket_details_label = mDetailsView.findViewById(R.id.coursepacket_details_label);
                TextView coursepacket_details_label1 = mDetailsView.findViewById(R.id.coursepacket_details_label1);
                coursepacket_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                coursepacket_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                TextView coursepacket_coursestage_label = mDetailsView.findViewById(R.id.coursepacket_coursestage_label);
                TextView coursepacket_coursestage_label1 = mDetailsView.findViewById(R.id.coursepacket_coursestage_label1);
                coursepacket_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                TextView coursepacket_teachers_label1 = mDetailsView.findViewById(R.id.coursepacket_teachers_label1);
                TextView coursepacket_teachers_label = mDetailsView.findViewById(R.id.coursepacket_teachers_label);
                coursepacket_teachers_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_teachers_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                //修改body为课程包详情
                LinearLayout coursepacket_details_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_details_label_content_layout);
                LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_details_label_content_layout.getLayoutParams();
                LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                coursepacket_details_label_content_layout.setLayoutParams(LP);
                coursepacket_details_label_content_layout.setVisibility(View.VISIBLE);
                LinearLayout coursepacket_coursestage_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_coursestage_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_coursestage_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_coursestage_label_content_layout.setLayoutParams(LP);
                coursepacket_coursestage_label_content_layout.setVisibility(View.INVISIBLE);
                LinearLayout coursepacket_teachers_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_teachers_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_teachers_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_teachers_label_content_layout.setLayoutParams(LP);
                coursepacket_teachers_label_content_layout.setVisibility(View.INVISIBLE);
                if (!mCurrentTab.equals("Details")) {
                    ImageView imgv_cursor = mDetailsView.findViewById(R.id.imgv_cursor);
                    ImageView imgv_cursor1 = mDetailsView.findViewById(R.id.imgv_cursor1);
                    Animation animation = new TranslateAnimation(( lastTabIndex - 1)  * width / 3,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    imgv_cursor.startAnimation(animation);
                    imgv_cursor1.startAnimation(animation);
                }
                lastTabIndex = 1;
                mCurrentTab = "Details";
                break;
            }
            case R.id.coursepacket_coursestage_label:
            case R.id.coursepacket_coursestage_label1: {
                CoursePacketStageCourseInit(mCoursePacketInfo);
                TextView coursepacket_details_label = mDetailsView.findViewById(R.id.coursepacket_details_label);
                TextView coursepacket_details_label1 = mDetailsView.findViewById(R.id.coursepacket_details_label1);
                coursepacket_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                TextView coursepacket_coursestage_label = mDetailsView.findViewById(R.id.coursepacket_coursestage_label);
                TextView coursepacket_coursestage_label1 = mDetailsView.findViewById(R.id.coursepacket_coursestage_label1);
                coursepacket_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                coursepacket_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                TextView coursepacket_teachers_label1 = mDetailsView.findViewById(R.id.coursepacket_teachers_label1);
                TextView coursepacket_teachers_label = mDetailsView.findViewById(R.id.coursepacket_teachers_label);
                coursepacket_teachers_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_teachers_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                //修改body为课程阶段
                LinearLayout coursepacket_coursestage_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_coursestage_label_content_layout);
                LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_coursestage_label_content_layout.getLayoutParams();
                LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                coursepacket_coursestage_label_content_layout.setLayoutParams(LP);
                coursepacket_coursestage_label_content_layout.setVisibility(View.VISIBLE);
                LinearLayout coursepacket_teachers_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_teachers_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_teachers_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_teachers_label_content_layout.setLayoutParams(LP);
                coursepacket_teachers_label_content_layout.setVisibility(View.INVISIBLE);
                LinearLayout coursepacket_details_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_details_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_details_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_details_label_content_layout.setLayoutParams(LP);
                coursepacket_details_label_content_layout.setVisibility(View.INVISIBLE);
                if (!mCurrentTab.equals("StageCourse")) {
                    ImageView imgv_cursor = mDetailsView.findViewById(R.id.imgv_cursor);
                    ImageView imgv_cursor1 = mDetailsView.findViewById(R.id.imgv_cursor1);
                    Animation animation = new TranslateAnimation(( lastTabIndex - 1)  * width / 3,width / 3 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    imgv_cursor.startAnimation(animation);
                    imgv_cursor1.startAnimation(animation);
                }
                lastTabIndex = 2;
                mCurrentTab = "StageCourse";
                break;
            }
            case R.id.coursepacket_teachers_label:
            case R.id.coursepacket_teachers_label1: {
                CoursePacketTeachersInit(mCoursePacketInfo);
                TextView coursepacket_details_label = mDetailsView.findViewById(R.id.coursepacket_details_label);
                TextView coursepacket_details_label1 = mDetailsView.findViewById(R.id.coursepacket_details_label1);
                coursepacket_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                TextView coursepacket_coursestage_label = mDetailsView.findViewById(R.id.coursepacket_coursestage_label);
                TextView coursepacket_coursestage_label1 = mDetailsView.findViewById(R.id.coursepacket_coursestage_label1);
                coursepacket_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                coursepacket_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                TextView coursepacket_teachers_label1 = mDetailsView.findViewById(R.id.coursepacket_teachers_label1);
                TextView coursepacket_teachers_label = mDetailsView.findViewById(R.id.coursepacket_teachers_label);
                coursepacket_teachers_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                coursepacket_teachers_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                //修改body为师资
                LinearLayout coursepacket_coursestage_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_coursestage_label_content_layout);
                LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_coursestage_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_coursestage_label_content_layout.setLayoutParams(LP);
                coursepacket_coursestage_label_content_layout.setVisibility(View.INVISIBLE);
                LinearLayout coursepacket_details_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_details_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_details_label_content_layout.getLayoutParams();
                LP.height = 0;
                coursepacket_details_label_content_layout.setLayoutParams(LP);
                coursepacket_details_label_content_layout.setVisibility(View.INVISIBLE);
                LinearLayout coursepacket_teachers_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_teachers_label_content_layout);
                LP = (LinearLayout.LayoutParams) coursepacket_teachers_label_content_layout.getLayoutParams();
                LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                coursepacket_teachers_label_content_layout.setLayoutParams(LP);
                coursepacket_teachers_label_content_layout.setVisibility(View.VISIBLE);
                if (!mCurrentTab.equals("Teachers")) {
                    ImageView imgv_cursor = mDetailsView.findViewById(R.id.imgv_cursor);
                    ImageView imgv_cursor1 = mDetailsView.findViewById(R.id.imgv_cursor1);
                    Animation animation = new TranslateAnimation(( lastTabIndex - 1)  * width / 3,width* 2 / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    imgv_cursor.startAnimation(animation);
                    imgv_cursor1.startAnimation(animation);
                }
                lastTabIndex = 3;
                mCurrentTab = "Teachers";
                break;
            }
            case R.id.coursepacket_details_bottomlayout_collect:{
                ImageView coursepacket_details_bottomlayout_collectImage = mDetailsView.findViewById(R.id.coursepacket_details_bottomlayout_collectImage);
                TextView coursepacket_details_bottomlayout_collectText = mDetailsView.findViewById(R.id.coursepacket_details_bottomlayout_collectText);
                if (mIsCollect){
                    mIsCollect = false;
                    coursepacket_details_bottomlayout_collectText.setTextColor(mDetailsView.getResources().getColor(R.color.collectdefaultcolor));
                    coursepacket_details_bottomlayout_collectImage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_disable));
                } else {
                    mIsCollect = true;
                    coursepacket_details_bottomlayout_collectImage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_enable));
                    coursepacket_details_bottomlayout_collectText.setTextColor(mDetailsView.getResources().getColor(R.color.holo_red_dark));
                }
                break;
            }
            case R.id.coursepacket_details_buy_button:{
                HideAllLayout();
                RelativeLayout coursepacket_main = modelCoursePacket.findViewById(R.id.coursepacket_main);
                View view = mControlMainActivity.Page_OrderDetails(this);
                coursepacket_main.addView(view);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onRecive() {
        CoursePacketDetailsShow();
    }

    public interface ModelCoursePacketCoverOnClickListener{
        void OnClickListener(View view);
    }

    //课程包详情界面
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CoursePacketDetailsInit(CoursePacketInfo coursePacketInfo){
        ImageView coursepacket_details_Cover = mDetailsView.findViewById(R.id.coursepacket_details_Cover);
        //课程包界面
        if (coursePacketInfo.mCoursePacketCover != null){
            Glide.with(mControlMainActivity).
                    load(coursePacketInfo.mCoursePacketCover).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("Warn","加载失败 errorMsg:"+(e!=null?e.getMessage():"null"));
                    return false;
                }
                @Override
                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("Warn","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                    return false;
                }
            })
                    .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(coursepacket_details_Cover);
        }
        //课程包详情-课程包名称
        TextView coursepacket_details_Name = mDetailsView.findViewById(R.id.coursepacket_details_Name);
        if (coursePacketInfo.mCoursePacketName != null) {
            coursepacket_details_Name.setText(coursePacketInfo.mCoursePacketName);
        }
        //课程包详情-课程包信息
        TextView coursepacket_details_content0 = mDetailsView.findViewById(R.id.coursepacket_details_content0);
        TextView coursepacket_details_content2 = mDetailsView.findViewById(R.id.coursepacket_details_content2);
        if (coursePacketInfo.mCoursePacketStageNum != null) {
            coursepacket_details_content0.setText("阶段" + coursePacketInfo.mCoursePacketStageNum);
        }
        if (coursePacketInfo.mCoursePacketCourseNum != null){
            coursepacket_details_content2.setText("课程" + coursePacketInfo.mCoursePacketCourseNum);
        }

        if (coursePacketInfo.mCoursePacketLearnPersonNum != null){
            TextView coursepacket_details_learnpersonnum = mDetailsView.findViewById(R.id.coursepacket_details_learnpersonnum);
            coursepacket_details_learnpersonnum.setText(coursePacketInfo.mCoursePacketLearnPersonNum + "人已学习");;
        }
        //课程包价格
        TextView coursepacket_details_price = mDetailsView.findViewById(R.id.coursepacket_details_price);
        if (coursePacketInfo.mCoursePacketPrice != null) {
            if (!coursePacketInfo.mCoursePacketPrice.equals("免费")){
                coursepacket_details_price.setTextColor(Color.RED);
                coursepacket_details_price.setText("¥" + coursePacketInfo.mCoursePacketPrice);
            } else {
                coursepacket_details_price.setText(coursePacketInfo.mCoursePacketPrice);
            }
        }
        //课程包原价
        TextView coursepacket_details_priceOld = mDetailsView.findViewById(R.id.coursepacket_details_priceOld);
        //文字栅格化
        coursepacket_details_priceOld.setPaintFlags(coursepacket_details_priceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
        if (coursePacketInfo.mCoursePacketPriceOld != null) {
            if (!coursePacketInfo.mCoursePacketPriceOld.equals("免费")){
                coursepacket_details_priceOld.setText("¥" + coursePacketInfo.mCoursePacketPriceOld);
            }
        }
        AppBarLayout mAppBarLayout =  mDetailsView.findViewById(R.id.appbar);
        FrameLayout mFLayout =  mDetailsView.findViewById(R.id.fl_layout);
        //课程包名称
        TextView fl_layout_title = mDetailsView.findViewById(R.id.fl_layout_title);
        fl_layout_title.setHint(coursePacketInfo.mCoursePacketName);
        //课程包简介
        TextView coursepacket_details_briefintroductioncontent = mDetailsView.findViewById(R.id.coursepacket_details_briefintroductioncontent);
        coursepacket_details_briefintroductioncontent.setText(coursePacketInfo.mCoursePacketMessage);
        //课程包详情和课程阶段的标签层
        LinearLayout coursepacket_label = mDetailsView.findViewById(R.id.coursepacket_label);
        LinearLayout coursepacket_label1 = mDetailsView.findViewById(R.id.coursepacket_label1);
        //课程包详情和课程阶段的标签层的下方游标
        ImageView imgv_cursor = mDetailsView.findViewById(R.id.imgv_cursor);
        Matrix matrix = new Matrix();
        matrix.postTranslate(width / 3, 0);
        imgv_cursor.setImageMatrix(matrix);// 设置动画初始位置
        ImageView imgv_cursor1 = mDetailsView.findViewById(R.id.imgv_cursor1);
        Matrix matrix1 = new Matrix();
        matrix1.postTranslate(width / 2, 0);
        imgv_cursor1.setImageMatrix(matrix1);// 设置动画初始位置
        //课程包详情的内容  HTML格式
        TextView coursepacket_details_label_content = mDetailsView.findViewById(R.id.coursepacket_details_label_content);
        new ModelHtmlUtils(mControlMainActivity,coursepacket_details_label_content).setHtmlWithPic(coursePacketInfo.mCoursePacketDetails);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout,verticalOffset) -> {
            float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
            ImageView coursepacket_details_return_button = mDetailsView.findViewById(R.id.coursepacket_details_return_button);
            ImageView coursepacket_details_return_button1 = mDetailsView.findViewById(R.id.coursepacket_details_return_button1);
            if (verticalOffset < - coursepacket_details_return_button.getY()){
                Toolbar.LayoutParams fl = (Toolbar.LayoutParams) mFLayout.getLayoutParams();
                fl.height = FrameLayout.LayoutParams.MATCH_PARENT;
                mFLayout.setLayoutParams(fl);
                mFLayout.setAlpha(percent);
                coursepacket_details_return_button1.setVisibility(View.INVISIBLE);
            } else {
                Toolbar.LayoutParams fl = (Toolbar.LayoutParams) mFLayout.getLayoutParams();
                fl.height = 0;
                mFLayout.setLayoutParams(fl);
                mFLayout.setAlpha(0);
                coursepacket_details_return_button1.setVisibility(View.VISIBLE);
            }
            if (verticalOffset <= - coursepacket_details_Name.getY() - coursepacket_details_Name.getHeight()){
                fl_layout_title.setVisibility(View.VISIBLE);
            } else {
                fl_layout_title.setVisibility(View.INVISIBLE);
            }
            if (verticalOffset <= - coursepacket_label1.getY() + coursepacket_label.getHeight() + coursepacket_label.getY()){
                coursepacket_label.setAlpha(percent);
                coursepacket_label1.setAlpha(0);
                imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
                imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
            } else {
                coursepacket_label.setAlpha(0);
                coursepacket_label1.setAlpha(1);
                imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
                imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
            }
        });
    }
    public void CoursePacketStageCourseInit(CoursePacketInfo coursePacketInfo){
        LinearLayout coursepacket_coursestage_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_coursestage_label_content_layout);
        //清空之前添加的阶段课程所有布局
        coursepacket_coursestage_label_content_layout.removeAllViews();
        if (coursePacketInfo.mStageCourseInfoList == null) {
            return;
        }
        for (int i = 0; i < coursePacketInfo.mStageCourseInfoList.size(); i ++){
            StageCourseInfo stageCourseInfo = coursePacketInfo.mStageCourseInfoList.get(i);
            if (stageCourseInfo == null) {
                continue;
            }
            if (stageCourseInfo.mStageCourseName.equals("")){
                if (stageCourseInfo.mCourseInfoList == null){
                    continue;
                }
                for (int num = 0; num < stageCourseInfo.mCourseInfoList.size() ; num ++){
                    CourseInfo courseInfo = stageCourseInfo.mCourseInfoList.get(num);
                    if (courseInfo == null){
                        continue;
                    }
                    ModelCourseCover modelCourseCover = new ModelCourseCover();
                    View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity,courseInfo);
                    coursepacket_coursestage_label_content_layout.addView(modelCourseView);
                }
              continue;
            }
            //如果课程阶段名称不为空，添加阶段课程的布局
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelstagecourse, null);
            LinearLayout coursepacket_coursestage_label_namelayout = view.findViewById(R.id.coursepacket_coursestage_label_namelayout);
            TextView coursepacket_coursestage_label_name = view.findViewById(R.id.coursepacket_coursestage_label_name);
            coursepacket_coursestage_label_name.setText(stageCourseInfo.mStageCourseName);
            ModelExpandView mExpandView = view.findViewById(R.id.coursepacket_coursestage_label_expandView);
            ImageView coursepacket_coursestage_label_arrow_right = view.findViewById(R.id.coursepacket_coursestage_label_arrow_right);
            ImageView coursepacket_coursestage_label_arrow_down = view.findViewById(R.id.coursepacket_coursestage_label_arrow_down);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_right.getLayoutParams();
            ll.width = view.getResources().getDimensionPixelSize(R.dimen.dp6);
            coursepacket_coursestage_label_arrow_right.setLayoutParams(ll);
            ll = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_down.getLayoutParams();
            ll.width = 0;
            coursepacket_coursestage_label_arrow_down.setLayoutParams(ll);
            LinearLayout coursepacket_coursestage_label_card2_content = view.findViewById(R.id.coursepacket_coursestage_label_card2_content);
            coursepacket_coursestage_label_namelayout.setClickable(true);
            coursepacket_coursestage_label_namelayout.setOnClickListener(v->{
                // TODO Auto-generated method stub
                if(mExpandView.isExpand()){
                    mExpandView.collapse();
                    //收缩隐藏布局
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) mExpandView.getLayoutParams();
                    rl.height = 0;
                    mExpandView.setLayoutParams(rl);
                    mExpandView.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_right.getLayoutParams();
                    ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp6);
                    coursepacket_coursestage_label_arrow_right.setLayoutParams(ll1);
                    ll1 = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_down.getLayoutParams();
                    ll1.width = 0;
                    coursepacket_coursestage_label_arrow_down.setLayoutParams(ll1);
                } else{
                    mExpandView.expand();
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) mExpandView.getLayoutParams();
                    rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    mExpandView.setLayoutParams(rl);
                    mExpandView.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_right.getLayoutParams();
                    ll1.width = 0;
                    coursepacket_coursestage_label_arrow_right.setLayoutParams(ll1);
                    ll1 = (LinearLayout.LayoutParams) coursepacket_coursestage_label_arrow_down.getLayoutParams();
                    ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp10);
                    coursepacket_coursestage_label_arrow_down.setLayoutParams(ll1);
                }
            });
            coursepacket_coursestage_label_content_layout.addView(view);
            if (stageCourseInfo.mCourseInfoList == null){
                continue;
            }
            if (coursepacket_coursestage_label_card2_content == null) {
                continue;
            }
            TextView coursepacket_coursestage_label_coursecount = view.findViewById(R.id.coursepacket_coursestage_label_coursecount);
            coursepacket_coursestage_label_coursecount.setText("共" + stageCourseInfo.mCourseInfoList.size() + "讲");
            coursepacket_coursestage_label_card2_content.removeAllViews();
            for (int num = 0; num < stageCourseInfo.mCourseInfoList.size() ; num ++) {
                CourseInfo courseInfo = stageCourseInfo.mCourseInfoList.get(num);
                if (courseInfo == null) {
                    continue;
                }
                View stagecourse = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelstagecourse1, null);
                TextView stagecourselistmain_name = stagecourse.findViewById(R.id.stagecourselistmain_name);
                stagecourselistmain_name.setText(courseInfo.mCourseName);
                TextView stagecourselistmain_price = stagecourse.findViewById(R.id.stagecourselistmain_price);
                if (courseInfo.mCoursePrice.equals("免费")){
                    TextView stagecourselistmain_priceLogo = stagecourse.findViewById(R.id.stagecourselistmain_priceLogo);
                    stagecourselistmain_priceLogo.setText("");
                    stagecourselistmain_price.setTextColor(stagecourse.getResources().getColor(R.color.collectdefaultcolor3));
                } else {
                    TextView stagecourselistmain_priceLogo = stagecourse.findViewById(R.id.stagecourselistmain_priceLogo);
                    stagecourselistmain_priceLogo.setText("¥");
                    stagecourselistmain_price.setTextColor(stagecourse.getResources().getColor(R.color.holo_red_dark));
                }
                stagecourselistmain_price.setText(courseInfo.mCoursePrice);
                coursepacket_coursestage_label_card2_content.addView(stagecourse);
            }
        }
    }
    public void CoursePacketTeachersInit(CoursePacketInfo coursePacketInfo){
        LinearLayout coursepacket_teachers_label_content_layout = mDetailsView.findViewById(R.id.coursepacket_teachers_label_content_layout);
        //清空之前添加的师资所有布局
        coursepacket_teachers_label_content_layout.removeAllViews();
        if (coursePacketInfo.mTeacherInfoList == null) {
            return;
        }
        for (int i = 0; i < coursePacketInfo.mTeacherInfoList.size(); i ++){
            TeacherInfo teacherInfo = coursePacketInfo.mTeacherInfoList.get(i);
            if (teacherInfo == null) {
                continue;
            }
            if (teacherInfo.mTeacherName.equals("")){
                continue;
            }
            //如果教师名称不为空，添加教师的布局
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelteachers, null);
            ControllerCustomRoundAngleImageView teachers_headportrait = view.findViewById(R.id.teachers_headportrait);
            Glide.with(mControlMainActivity).
                    load(teacherInfo.mTeacherCover).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("Wain","加载失败 errorMsg:"+(e!=null?e.getMessage():"null"));
                    return false;
                }

                @Override
                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                    return false;
                }
            })
                    .error(mControlMainActivity.getResources().getDrawable(R.drawable.image_teachersdefault)).into(teachers_headportrait);
            TextView teachers_content_name = view.findViewById(R.id.teachers_content_name);
            teachers_content_name.setText(teacherInfo.mTeacherName);
            TextView teachers_content_message = view.findViewById(R.id.teachers_content_message);
            teachers_content_message.setText(teacherInfo.mTeacherMessage);
            coursepacket_teachers_label_content_layout.addView(view);
        }
    }
}
