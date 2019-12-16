package com.android.hzjy.hzjyproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.talkfun.sdk.HtSdk;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.fragment.NewImagePagerDialogFragment;

public class ModelCourseCover implements View.OnClickListener ,ModelOrderDetailsInterface{
    private View modelCourse,mListView,mDetailsView,mQuestionView,mQuestionViewAdd,mQuestionDetailsView,mDownloadManagerView;
    private RecyclerView mRecyclerView;
    private ArrayList<ControllerPictureBean> mPictureBeansList;
    private ControllerPictureAdapter mPictureAdapter;
    private ArrayList<String> selPhotosPath = null;//选中的图片路径集合
    private ControlMainActivity mControlMainActivity = null;
    private ModelCourseCover.ModelCourseCoverOnClickListener mModelCourseCoverOnClickListener = null;
    private int height = 1344;
    private int width = 720;
    private String mCurrentTab = "Details";  //当前标签为详情还是目录
    private boolean mIsCollect = false;
    private String mCurrentCatalogTab = "Record"; //当前标签是录播还是直播
    private CourseInfo mCourseInfo;
    private Map<String,View> CourseQuestionViewMap = new HashMap<>();
    private Map<String,List<CourseQuestionInfo>> CourseQuestionDetailsViewMap = new HashMap<>();
    private String mPage = "Detail";
    private boolean mQuestionPublishImage = false;
    private boolean mQuestionPublishTitle = false;
    private boolean mQuestionPublishContent = false;
    private ControllerCenterDialog mMyDialog;
    private ControllerPopDialog mCourseDownloadDialog = null;
    private Map<String,CourseRecordPlayDownloadInfo> mCourseRecordPlayDownloadInfoMap = new HashMap<>();
    private ControllerCustomDialog mCustomDialog = null;
    public void ModelCourseCoverOnClickListenerSet(ModelCourseCover.ModelCourseCoverOnClickListener modelCourseCoverOnClickListener){
        mModelCourseCoverOnClickListener = modelCourseCoverOnClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRecive() {
        CourseDetailsShow();
    }

    public interface ModelCourseCoverOnClickListener{
        void OnClickListener(View view,ModelCourseCover modelCourseCover);
    }
    public View ModelCourseCover(Context context,CourseInfo courseInfo){
        mControlMainActivity = (ControlMainActivity) context;
        if (courseInfo == null){
            return null;
        }
        mCourseInfo = new CourseInfo(courseInfo);
        DisplayMetrics dm = context.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        modelCourse = LayoutInflater.from(context).inflate(R.layout.modelcourse_layout, null);
        mListView  = LayoutInflater.from(context).inflate(R.layout.modelcourselist_layout,null);
        if (mDetailsView == null) {
            mDetailsView = LayoutInflater.from(context).inflate(R.layout.modelcoursedetails_layout, null);
            modelCourse.setOnClickListener(v -> {
                if (mModelCourseCoverOnClickListener == null || modelCourse == null || mPage.equals("Question")
                        || mPage.equals("QuestionAdd") || mPage.equals("QuestionDetails") || mPage.equals("DownloadManager")){
                    return;
                }
                mModelCourseCoverOnClickListener.OnClickListener(v,this);
                //跳转到课程的详细界面
                CourseDetailsShow();
            });
            //课程详情按钮
            TextView course_details_label = mDetailsView.findViewById(R.id.course_details_label);
            TextView course_details_label1 = mDetailsView.findViewById(R.id.course_details_label1);
            //课程阶段按钮
            TextView course_coursestage_label = mDetailsView.findViewById(R.id.course_coursestage_label);
            TextView course_coursestage_label1 = mDetailsView.findViewById(R.id.course_coursestage_label1);
            LinearLayout course_details_bottomlayout_collect = mDetailsView.findViewById(R.id.course_details_bottomlayout_collect);
            LinearLayout course_details_bottomlayout_collect1 = mDetailsView.findViewById(R.id.course_details_bottomlayout_collect1);
            LinearLayout course_details_bottomlayout_question = mDetailsView.findViewById(R.id.course_details_bottomlayout_question);
            LinearLayout course_catalog_label_livemain = mDetailsView.findViewById(R.id.course_catalog_label_livemain);
            LinearLayout course_catalog_label_livemain1 = mDetailsView.findViewById(R.id.course_catalog_label_livemain1);
            LinearLayout course_catalog_label_recordmain = mDetailsView.findViewById(R.id.course_catalog_label_recordmain);
            LinearLayout course_catalog_label_recordmain1 = mDetailsView.findViewById(R.id.course_catalog_label_recordmain1);
            ImageView course_fl_layout_title_download = mDetailsView.findViewById(R.id.course_fl_layout_title_download);
            //下载按钮
            ImageView course_details_download_button = mDetailsView.findViewById(R.id.course_details_download_button);
            ImageView course_details_download_button1 = mDetailsView.findViewById(R.id.course_details_download_button1);
            //立即购买按钮
            Button course_details_buy_button = mDetailsView.findViewById(R.id.course_details_buy_button);
            course_details_buy_button.setOnClickListener(this);
            course_details_download_button1.setOnClickListener(this);
            course_details_download_button.setOnClickListener(this);
            course_fl_layout_title_download.setOnClickListener(this);
            course_details_label.setOnClickListener(this);
            course_details_label1.setOnClickListener(this);
            course_coursestage_label.setOnClickListener(this);
            course_coursestage_label1.setOnClickListener(this);
            course_details_bottomlayout_collect.setOnClickListener(this);
            course_details_bottomlayout_collect1.setOnClickListener(this);
            course_details_bottomlayout_question.setOnClickListener(this);
            course_catalog_label_livemain.setOnClickListener(this);
            course_catalog_label_livemain1.setOnClickListener(this);
            course_catalog_label_recordmain.setOnClickListener(this);
            course_catalog_label_recordmain1.setOnClickListener(this);
        }
        HideAllLayout();
        CourseListInit(courseInfo);
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        course_main.addView(mListView);
        //课程详情界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CourseDetailsInit(courseInfo);
        }
        return modelCourse;
    }

    public void CourseDetailsShow() {
        if (modelCourse == null) {
            return;
        }
        mPage = "Detail";
        HideAllLayout();
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        course_main.addView(mDetailsView);
        //默认显示详情界面
        TextView course_details_label = mDetailsView.findViewById(R.id.course_details_label);
        TextView course_details_label1 = mDetailsView.findViewById(R.id.course_details_label1);
        course_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        course_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        TextView course_coursestage_label = mDetailsView.findViewById(R.id.course_coursestage_label);
        TextView course_coursestage_label1 = mDetailsView.findViewById(R.id.course_coursestage_label1);
        course_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        course_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        //修改body为课程详情
        LinearLayout course_details_label_content_layout = mDetailsView.findViewById(R.id.course_details_label_content_layout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_details_label_content_layout.getLayoutParams();
        LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        course_details_label_content_layout.setLayoutParams(LP);
        course_details_label_content_layout.setVisibility(View.VISIBLE);
        LinearLayout course_catalog_label_content_layout_main = mDetailsView.findViewById(R.id.course_catalog_label_content_layout_main);
        LP = (LinearLayout.LayoutParams) course_catalog_label_content_layout_main.getLayoutParams();
        LP.height = 0;
        course_catalog_label_content_layout_main.setLayoutParams(LP);
        course_catalog_label_content_layout_main.setVisibility(View.INVISIBLE);
        ImageView course_imgv_cursor = mDetailsView.findViewById(R.id.course_imgv_cursor);
        ImageView course_imgv_cursor1 = mDetailsView.findViewById(R.id.course_imgv_cursor1);
        int x = width / 4 - mDetailsView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        course_imgv_cursor.setX(x);
        course_imgv_cursor1.setX(x);
    }

    public void CourseQuestionShow() {
        if (modelCourse == null) {
            return;
        }
        mPage = "Question";
        HideAllLayout();
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        if (mQuestionView == null) {
            mQuestionView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_question, null);
        }
        ImageView course_question_layout_return_button1 = mQuestionView.findViewById(R.id.course_question_layout_return_button1);
        ImageView course_question_layout_add_button1 = mQuestionView.findViewById(R.id.course_question_layout_add_button1);
        course_question_layout_return_button1.setOnClickListener(this);
        course_question_layout_add_button1.setOnClickListener(this);
        LinearLayout course_question_layout_content = mQuestionView.findViewById(R.id.course_question_layout_content);
        course_question_layout_content.removeAllViews();
        int count = 0;
        //将问答内容添加到布局中
        for (int i = 0;i < mCourseInfo.mCourseQuestionInfoList.size() ; i ++){
            CourseQuestionInfo courseQuestionInfo = mCourseInfo.mCourseQuestionInfoList.get(i);
            if (courseQuestionInfo == null){
                continue;
            }
            if (courseQuestionInfo.mCourseAnswerId.equals("0")){ //一级问答
                count ++ ;
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_question_child, null);
                //添加头像
                ControllerCustomRoundAngleImageView course_question_child_headportrait = view.findViewById(R.id.course_question_child_headportrait);
                if (courseQuestionInfo.mCourseQuestionCommentHead2 != null){
                    Glide.with(mControlMainActivity).
                            load(courseQuestionInfo.mCourseQuestionCommentHead2).listener(new RequestListener<Drawable>() {
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
                            .error(mControlMainActivity.getResources().getDrawable(R.drawable.image_teachersdefault)).into(course_question_child_headportrait);
                }
                //问题的监听(点击问题查看详情)
                LinearLayout course_question_child_content1 = view.findViewById(R.id.course_question_child_content1);
                course_question_child_content1.setClickable(true);
                course_question_child_content1.setOnClickListener(v->{
                    CourseQuestionDetailsInit(courseQuestionInfo.mCourseQuestionId);
                });
                //回答者名字
                TextView course_question_child_name = view.findViewById(R.id.course_question_child_name);
                course_question_child_name.setText(courseQuestionInfo.mCourseQuestionCommentName2);
                course_question_child_name.setHint(courseQuestionInfo.mCourseQuestionCommentId2);
                //回答内容
                TextView course_question_child_message = view.findViewById(R.id.course_question_child_message);
                course_question_child_message.setText(courseQuestionInfo.mCourseQuestionContent);
                //回答图片？？
                //时间
                TextView course_question_child_time = view.findViewById(R.id.course_question_child_time);
                course_question_child_time.setText(courseQuestionInfo.mCourseQuestionTime);
                //浏览人数
                TextView course_question_child_look = view.findViewById(R.id.course_question_child_look);
                course_question_child_look.setText("浏览" + courseQuestionInfo.mCourseQuestionLookNum);
                LinearLayout course_question_child_function_discuss = view.findViewById(R.id.course_question_child_function_discuss);
                course_question_child_function_discuss.setClickable(true);
                course_question_child_function_discuss.setOnClickListener(v->{
                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + courseQuestionInfo.mCourseQuestionCommentName2,false);
                    mCustomDialog.setOnKeyListener(keylistener);
                    mCustomDialog.show();
                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                        @Override
                        public void publish() {

                        }

                        @Override
                        public void image() {

                        }
                    });
                });
                course_question_layout_content.addView(view);
                CourseQuestionViewMap.put(courseQuestionInfo.mCourseQuestionId,view);
            } else {
                View view = CourseQuestionViewMap.get(courseQuestionInfo.mCourseAnswerId);
                //添加评论个数
                TextView course_question_child_discusstext = view.findViewById(R.id.course_question_child_discusstext);
                int discussNum = Integer.valueOf(course_question_child_discusstext.getText().toString());
                course_question_child_discusstext.setText(String.valueOf(discussNum + 1));
                if ((discussNum + 1) > 2){
                    LinearLayout course_question_child_lookalldiscuss = view.findViewById(R.id.course_question_child_lookalldiscuss);
                    LinearLayout.LayoutParams rl = (LinearLayout.LayoutParams) course_question_child_lookalldiscuss.getLayoutParams();
                    rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    rl.topMargin = view.getResources().getDimensionPixelSize(R.dimen.dp13);
                    rl.bottomMargin = view.getResources().getDimensionPixelSize(R.dimen.dp13);
                    course_question_child_lookalldiscuss.setLayoutParams(rl);
                    course_question_child_lookalldiscuss.setClickable(true);
                    course_question_child_lookalldiscuss.setOnClickListener(v->{
                        CourseQuestionDetailsInit(courseQuestionInfo.mCourseAnswerId);
                    });
                    if (CourseQuestionDetailsViewMap.get(courseQuestionInfo.mCourseAnswerId) == null) {
                        CourseQuestionDetailsViewMap.put(courseQuestionInfo.mCourseAnswerId, new ArrayList<>());
                    }
                    CourseQuestionDetailsViewMap.get(courseQuestionInfo.mCourseAnswerId).add(courseQuestionInfo);
                    continue;
                }
                LinearLayout course_question_child_content = view.findViewById(R.id.course_question_child_content);
                View childView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_question_child1, null);
                TextView course_question_child_style_name = childView.findViewById(R.id.course_question_child_style_name);
                course_question_child_style_name.setText(courseQuestionInfo.mCourseQuestionCommentName2);
                course_question_child_style_name.setHint(courseQuestionInfo.mCourseQuestionCommentId2);
                TextView course_question_child_style_name1 = childView.findViewById(R.id.course_question_child_style_name1);
                course_question_child_style_name1.setText(courseQuestionInfo.mCourseQuestionCommentName1);
                course_question_child_style_name1.setHint(courseQuestionInfo.mCourseQuestionCommentId1);
                TextView course_question_child_style_content = childView.findViewById(R.id.course_question_child_style_content);
                course_question_child_style_content.setText(courseQuestionInfo.mCourseQuestionContent);
                course_question_child_content.addView(childView);
                if (CourseQuestionDetailsViewMap.get(courseQuestionInfo.mCourseAnswerId) == null) {
                    CourseQuestionDetailsViewMap.put(courseQuestionInfo.mCourseAnswerId, new ArrayList<>());
                }
                CourseQuestionDetailsViewMap.get(courseQuestionInfo.mCourseAnswerId).add(courseQuestionInfo);
            }
        }
        TextView course_question_layout_titletext = mQuestionView.findViewById(R.id.course_question_layout_titletext);
        course_question_layout_titletext.setText("精选问答(" + count + ")");
        course_main.addView(mQuestionView);
    }

    public void HideAllLayout(){
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        course_main.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.course_details_label:
            case R.id.course_details_label1: {
                TextView course_details_label = mDetailsView.findViewById(R.id.course_details_label);
                TextView course_details_label1 = mDetailsView.findViewById(R.id.course_details_label1);
                course_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                course_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                TextView course_coursestage_label = mDetailsView.findViewById(R.id.course_coursestage_label);
                TextView course_coursestage_label1 = mDetailsView.findViewById(R.id.course_coursestage_label1);
                course_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                course_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));

                LinearLayout course_catalog_label1 = mDetailsView.findViewById(R.id.course_catalog_label1);
                View coursepacket_details_line6 = mDetailsView.findViewById(R.id.coursepacket_details_line6);
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_label1.getLayoutParams();
                rl.height = 0;
                course_catalog_label1.setLayoutParams(rl);
                rl = (RelativeLayout.LayoutParams) coursepacket_details_line6.getLayoutParams();
                rl.height = 0;
                coursepacket_details_line6.setLayoutParams(rl);
                //修改body为课程详情
                LinearLayout course_details_label_content_layout = mDetailsView.findViewById(R.id.course_details_label_content_layout);
                LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_details_label_content_layout.getLayoutParams();
                LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                course_details_label_content_layout.setLayoutParams(LP);
                course_details_label_content_layout.setVisibility(View.VISIBLE);
                LinearLayout course_catalog_label_content_layout_main = mDetailsView.findViewById(R.id.course_catalog_label_content_layout_main);
                LP = (LinearLayout.LayoutParams) course_catalog_label_content_layout_main.getLayoutParams();
                LP.height = 0;
                course_catalog_label_content_layout_main.setLayoutParams(LP);
                course_catalog_label_content_layout_main.setVisibility(View.INVISIBLE);
                if (!mCurrentTab.equals("Details")) {
                    Animation animation = new TranslateAnimation(width / 2, 0, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    ImageView course_imgv_cursor = mDetailsView.findViewById(R.id.course_imgv_cursor);
                    course_imgv_cursor.startAnimation(animation);
                    ImageView course_imgv_cursor1 = mDetailsView.findViewById(R.id.course_imgv_cursor1);
                    course_imgv_cursor1.startAnimation(animation);
                }
                mCurrentTab = "Details";
                break;
            }
            case R.id.course_coursestage_label:
            case R.id.course_coursestage_label1: {
                TextView course_details_label = mDetailsView.findViewById(R.id.course_details_label);
                TextView course_details_label1 = mDetailsView.findViewById(R.id.course_details_label1);
                course_details_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                course_details_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                TextView course_coursestage_label = mDetailsView.findViewById(R.id.course_coursestage_label);
                TextView course_coursestage_label1 = mDetailsView.findViewById(R.id.course_coursestage_label1);
                course_coursestage_label.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                course_coursestage_label1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                //修改body为目录
                LinearLayout course_catalog_label_content_layout_main = mDetailsView.findViewById(R.id.course_catalog_label_content_layout_main);
                LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_catalog_label_content_layout_main.getLayoutParams();
                LP.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                course_catalog_label_content_layout_main.setLayoutParams(LP);
                course_catalog_label_content_layout_main.setVisibility(View.VISIBLE);
                LinearLayout course_details_label_content_layout = mDetailsView.findViewById(R.id.course_details_label_content_layout);
                LP = (LinearLayout.LayoutParams) course_details_label_content_layout.getLayoutParams();
                LP.height = 0;
                course_details_label_content_layout.setLayoutParams(LP);
                course_details_label_content_layout.setVisibility(View.INVISIBLE);
                //修改body为录播
                CourseCatalogRecordInit(mCourseInfo);
                if (!mCurrentTab.equals("Catalog")) {
                    Animation animation = new TranslateAnimation(0, width / 2, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    ImageView course_imgv_cursor = mDetailsView.findViewById(R.id.course_imgv_cursor);
                    course_imgv_cursor.startAnimation(animation);
                    ImageView course_imgv_cursor1 = mDetailsView.findViewById(R.id.course_imgv_cursor1);
                    course_imgv_cursor1.startAnimation(animation);
                }
                mCurrentTab = "Catalog";
                break;
            }
            case R.id.course_details_bottomlayout_collect1:
            case R.id.course_details_bottomlayout_collect:{
                ImageView course_details_bottomlayout_collectImage = mDetailsView.findViewById(R.id.course_details_bottomlayout_collectImage);
                TextView course_details_bottomlayout_collectText = mDetailsView.findViewById(R.id.course_details_bottomlayout_collectText);
                ImageView course_details_bottomlayout_collectImage1 = mDetailsView.findViewById(R.id.course_details_bottomlayout_collectImage1);
                TextView course_details_bottomlayout_collectText1 = mDetailsView.findViewById(R.id.course_details_bottomlayout_collectText1);
                if (mIsCollect){
                    mIsCollect = false;
                    course_details_bottomlayout_collectText.setTextColor(mDetailsView.getResources().getColor(R.color.collectdefaultcolor));
                    course_details_bottomlayout_collectImage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_disable));
                    course_details_bottomlayout_collectText1.setTextColor(mDetailsView.getResources().getColor(R.color.collectdefaultcolor));
                    course_details_bottomlayout_collectImage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_disable));
                } else {
                    mIsCollect = true;
                    course_details_bottomlayout_collectImage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_enable));
                    course_details_bottomlayout_collectText.setTextColor(mDetailsView.getResources().getColor(R.color.holo_red_dark));
                    course_details_bottomlayout_collectImage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_collect_enable));
                    course_details_bottomlayout_collectText1.setTextColor(mDetailsView.getResources().getColor(R.color.holo_red_dark));
                }
                break;
            }
            case R.id.course_question_add_layout_return_button1:
            case R.id.course_details_bottomlayout_question:{//点击课程问答
                CourseQuestionShow();
                break;
            }
            case R.id.course_catalog_label_livemain:
            case R.id.course_catalog_label_livemain1:{
                if (!mCurrentCatalogTab.equals("Live")){
                    ImageView course_catalog_label_liveimage = mDetailsView.findViewById(R.id.course_catalog_label_liveimage);
                    TextView course_catalog_label_live = mDetailsView.findViewById(R.id.course_catalog_label_live);
                    ImageView course_catalog_label_liveimage1 = mDetailsView.findViewById(R.id.course_catalog_label_liveimage1);
                    TextView course_catalog_label_live1 = mDetailsView.findViewById(R.id.course_catalog_label_live1);
                    course_catalog_label_liveimage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_live_blue));
                    course_catalog_label_live.setTextColor(mDetailsView.getResources().getColor(R.color.blue649cf0));
                    course_catalog_label_liveimage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_live_blue));
                    course_catalog_label_live1.setTextColor(mDetailsView.getResources().getColor(R.color.blue649cf0));
                    ImageView course_catalog_label_recordimage = mDetailsView.findViewById(R.id.course_catalog_label_recordimage);
                    TextView course_catalog_label_record = mDetailsView.findViewById(R.id.course_catalog_label_record);
                    ImageView course_catalog_label_recordimage1 = mDetailsView.findViewById(R.id.course_catalog_label_recordimage1);
                    TextView course_catalog_label_record1 = mDetailsView.findViewById(R.id.course_catalog_label_record1);
                    course_catalog_label_recordimage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_record_gray));
                    course_catalog_label_record.setTextColor(mDetailsView.getResources().getColor(R.color.black999999));
                    course_catalog_label_recordimage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_record_gray));
                    course_catalog_label_record1.setTextColor(mDetailsView.getResources().getColor(R.color.black999999));
                    //修改body为直播
                    CourseCatalogLiveInit(mCourseInfo);
                }
                mCurrentCatalogTab = "Live";
                break;
            }
            case R.id.course_catalog_label_recordmain:
            case R.id.course_catalog_label_recordmain1:{
                if (!mCurrentCatalogTab.equals("Record")){
                    ImageView course_catalog_label_liveimage = mDetailsView.findViewById(R.id.course_catalog_label_liveimage);
                    TextView course_catalog_label_live = mDetailsView.findViewById(R.id.course_catalog_label_live);
                    ImageView course_catalog_label_liveimage1 = mDetailsView.findViewById(R.id.course_catalog_label_liveimage1);
                    TextView course_catalog_label_live1 = mDetailsView.findViewById(R.id.course_catalog_label_live1);
                    course_catalog_label_liveimage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_live_gray));
                    course_catalog_label_live.setTextColor(mDetailsView.getResources().getColor(R.color.black999999));
                    course_catalog_label_liveimage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_live_gray));
                    course_catalog_label_live1.setTextColor(mDetailsView.getResources().getColor(R.color.black999999));
                    ImageView course_catalog_label_recordimage = mDetailsView.findViewById(R.id.course_catalog_label_recordimage);
                    TextView course_catalog_label_record = mDetailsView.findViewById(R.id.course_catalog_label_record);
                    ImageView course_catalog_label_recordimage1 = mDetailsView.findViewById(R.id.course_catalog_label_recordimage1);
                    TextView course_catalog_label_record1 = mDetailsView.findViewById(R.id.course_catalog_label_record1);
                    course_catalog_label_recordimage.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_record_blue));
                    course_catalog_label_record.setTextColor(mDetailsView.getResources().getColor(R.color.blue649cf0));
                    course_catalog_label_recordimage1.setImageDrawable(mDetailsView.getResources().getDrawable(R.drawable.button_record_blue));
                    course_catalog_label_record1.setTextColor(mDetailsView.getResources().getColor(R.color.blue649cf0));
                    //修改body为录播
                    CourseCatalogRecordInit(mCourseInfo);
                }
                mCurrentCatalogTab = "Record";
                break;
            }
            case R.id.course_question_layout_return_button1:{
                CourseDetailsShow();
                break;
            }
            case R.id.course_question_layout_add_button1:{
                CourseQuestionAddInit();
                break;
            }
            case R.id.course_fl_layout_title_download:
            case R.id.course_details_download_button:
            case R.id.course_details_download_button1: {
                int count = 0;
                for (int i = 0; i < mCourseInfo.mCourseChaptersInfoList.size() ; i ++) {
                    CourseChaptersInfo courseChaptersInfo = mCourseInfo.mCourseChaptersInfoList.get(i);
                    if (courseChaptersInfo == null){
                        continue;
                    }
                    for (int num = 0; num < courseChaptersInfo.mCourseSectionsInfoList.size(); num ++){
                        CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(num);
                        if (courseSectionsInfo == null){
                            continue;
                        }
                        //___________________________________________________________________测试进度
                        if (count == 0){
                            CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo = new CourseRecordPlayDownloadInfo();
                            courseRecordPlayDownloadInfo.mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;
                            courseRecordPlayDownloadInfo.mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;
                            courseRecordPlayDownloadInfo.mCourseSectionsId = courseSectionsInfo.mCourseSectionsId;
                            courseRecordPlayDownloadInfo.mCourseSectionsName = courseSectionsInfo.mCourseSectionsName;
                            courseRecordPlayDownloadInfo.mCourseSectionsDownloadSize = "2048";
                            courseRecordPlayDownloadInfo.mCourseSectionsSize = courseSectionsInfo.mCourseSectionsSize;
                            if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId,courseRecordPlayDownloadInfo);
                                }
                            } else {
                                mCourseRecordPlayDownloadInfoMap.put(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo);
                            }
                        } else if (count < 2) {
                            CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo = new CourseRecordPlayDownloadInfo();
                            courseRecordPlayDownloadInfo.mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;
                            courseRecordPlayDownloadInfo.mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;
                            courseRecordPlayDownloadInfo.mCourseSectionsId = courseSectionsInfo.mCourseSectionsId;
                            courseRecordPlayDownloadInfo.mCourseSectionsName = courseSectionsInfo.mCourseSectionsName;
                            courseRecordPlayDownloadInfo.mCourseSectionsDownloadSize = "100";
                            courseRecordPlayDownloadInfo.mCourseSectionsSize = courseSectionsInfo.mCourseSectionsSize;
                            if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId,courseRecordPlayDownloadInfo);
                                }
                            } else {
                                mCourseRecordPlayDownloadInfoMap.put(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo);
                            }
                        }
                        count ++;
                    }
                }
                CourseDownloadInit();
                break;
            }
            case R.id.course_details_buy_button:{ //课程详情购买
                HideAllLayout();
                RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
                View view = mControlMainActivity.Page_OrderDetails(this);
                course_main.addView(view);
                break;
            }
            default:
                break;
        }
    }
    private DialogInterface.OnKeyListener keylistener = (dialog, keyCode, event) -> {
        Log.i("TAG", "键盘code---" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            return false;
        } else if(keyCode == KeyEvent.KEYCODE_DEL){//删除键
            return false;
        }else{
            return true;
        }
    };
    public void CourseListInit(CourseInfo courseInfo){
        ControllerCustomRoundAngleImageView imageView = mListView.findViewById(R.id.coursecover);
        imageView.setImageDrawable(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover));//如果没有url，加载默认图片
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.leftMargin = width / 50;
        lp.rightMargin = width / 50;
        lp.topMargin = width / 25;
        lp.height = height / 4;
        imageView.setLayoutParams(lp);
        if (courseInfo.mCourseCover != null){
            Glide.with(mControlMainActivity).
                    load(courseInfo.mCourseCover).listener(new RequestListener<Drawable>() {
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
                    .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(imageView);
        }
        TextView courseNameTextView = mListView.findViewById(R.id.courseName);
        lp = (RelativeLayout.LayoutParams) courseNameTextView.getLayoutParams();
        lp.leftMargin = width / 25;
//        lp.topMargin = width / 25;
        courseNameTextView.setLayoutParams(lp);
        if (courseInfo.mCourseName != null) {
            courseNameTextView.setText(courseInfo.mCourseName);
        }
        TextView coursecontentTextView = mListView.findViewById(R.id.coursecontent);
        lp = (RelativeLayout.LayoutParams) coursecontentTextView.getLayoutParams();
        lp.leftMargin = width / 25;
        lp.topMargin = width / 50;
        coursecontentTextView.setLayoutParams(lp);
        String content = "";
        if (courseInfo.mCourseType != null) {
            content = courseInfo.mCourseType;
        }
        if (courseInfo.mCourseLearnPersonNum != null){
            content = content + " • " + courseInfo.mCourseLearnPersonNum+ "人已学习";
        }
        coursecontentTextView.setText(content);
        TextView coursepriceTextView = mListView.findViewById(R.id.courseprice);
        lp = (RelativeLayout.LayoutParams) coursepriceTextView.getLayoutParams();
        lp.rightMargin = width / 25;
        lp.topMargin = width / 50;
        coursepriceTextView.setLayoutParams(lp);
        if (courseInfo.mCoursePrice != null) {
            if (!courseInfo.mCoursePrice.equals("免费")){
                coursepriceTextView.setTextColor(Color.RED);
                coursepriceTextView.setText("¥" + courseInfo.mCoursePrice);
            } else {
                coursepriceTextView.setText(courseInfo.mCoursePrice);
            }
        }
        TextView coursepriceOldTextView = mListView.findViewById(R.id.coursepriceOld);
        lp = (RelativeLayout.LayoutParams) coursepriceOldTextView.getLayoutParams();
        lp.rightMargin = width / 25;
        lp.topMargin = width / 50;
        coursepriceOldTextView.setLayoutParams(lp);
        //文字栅格化
        coursepriceOldTextView.setPaintFlags(coursepriceOldTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (courseInfo.mCoursePriceOld != null) {
            if (!courseInfo.mCoursePriceOld.equals("免费")){
                coursepriceOldTextView.setText("¥" + courseInfo.mCoursePriceOld);
            }
        }
    }

    //课程详情界面
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CourseDetailsInit(CourseInfo courseInfo){
        ImageView course_details_Cover = mDetailsView.findViewById(R.id.course_details_Cover);
        //课程界面
        if (courseInfo.mCourseCover != null){
            Glide.with(mControlMainActivity).
                    load(courseInfo.mCourseCover).listener(new RequestListener<Drawable>() {
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
                    .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(course_details_Cover);
        }
        //课程详情-课程名称
        TextView course_details_Name = mDetailsView.findViewById(R.id.course_details_Name);
        if (courseInfo.mCourseName != null) {
            course_details_Name.setText(courseInfo.mCourseName);
        }
        //课程详情-课程信息
        TextView course_details_content0 = mDetailsView.findViewById(R.id.course_details_content0);
        if (courseInfo.mCourseLearnPersonNum != null){
            course_details_content0.setText(courseInfo.mCourseLearnPersonNum+ "人已学习");
        }
        //课程价格
        TextView course_details_price = mDetailsView.findViewById(R.id.course_details_price);
        if (courseInfo.mCoursePrice != null) {
            if (!courseInfo.mCoursePrice.equals("免费")){
                course_details_price.setTextColor(Color.RED);
                course_details_price.setText("¥" + courseInfo.mCoursePrice);
            } else {
                course_details_price.setText(courseInfo.mCoursePrice);
            }
        }
        //课程包原价
        TextView course_details_priceOld = mDetailsView.findViewById(R.id.course_details_priceOld);
        //文字栅格化
        course_details_priceOld.setPaintFlags(course_details_priceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
        if (courseInfo.mCoursePriceOld != null) {
            if (!courseInfo.mCoursePriceOld.equals("免费")){
                course_details_priceOld.setText("¥" + courseInfo.mCoursePriceOld);
            }
        }
        //课程有效期
        TextView course_details_periodofvalidity = mDetailsView.findViewById(R.id.course_details_periodofvalidity);
        course_details_periodofvalidity.setText("课程有效期:" + courseInfo.mCourseValidityPeriod);
        //课程简介
        TextView coursepacket_details_briefintroductioncontent = mDetailsView.findViewById(R.id.coursepacket_details_briefintroductioncontent);
        coursepacket_details_briefintroductioncontent.setText(courseInfo.mCourseMessage);
        AppBarLayout course_details_appbar =  mDetailsView.findViewById(R.id.course_details_appbar);
        FrameLayout course_fl_layout =  mDetailsView.findViewById(R.id.course_fl_layout);
        //课程包名称
        TextView course_fl_layout_title = mDetailsView.findViewById(R.id.course_fl_layout_title);
        course_fl_layout_title.setText(courseInfo.mCourseName);
        //课程包详情和课程阶段的标签层
        LinearLayout course_label = mDetailsView.findViewById(R.id.course_label);
        LinearLayout course_label1 = mDetailsView.findViewById(R.id.course_label1);
        //课程包详情和课程阶段的标签层的下方游标
        ImageView course_imgv_cursor = mDetailsView.findViewById(R.id.course_imgv_cursor);
        Matrix matrix = new Matrix();
        matrix.postTranslate(width / 2, 0);
        course_imgv_cursor.setImageMatrix(matrix);// 设置动画初始位置
        ImageView course_imgv_cursor1 = mDetailsView.findViewById(R.id.course_imgv_cursor1);
        Matrix matrix1 = new Matrix();
        matrix1.postTranslate(width / 2, 0);
        course_imgv_cursor1.setImageMatrix(matrix1);// 设置动画初始位置
        //课程包详情的内容  HTML格式
        TextView course_details_label_content = mDetailsView.findViewById(R.id.course_details_label_content);
        new ModelHtmlUtils(mControlMainActivity,course_details_label_content).setHtmlWithPic(courseInfo.mCourseDetails);
        course_details_appbar.addOnOffsetChangedListener((appBarLayout,verticalOffset) -> {
            ImageView course_details_return_button = mDetailsView.findViewById(R.id.course_details_return_button);
            ImageView course_details_return_button1 = mDetailsView.findViewById(R.id.course_details_return_button1);
            ImageView course_details_download_button = mDetailsView.findViewById(R.id.course_details_download_button);
            ImageView course_details_download_button1 = mDetailsView.findViewById(R.id.course_details_download_button1);
            float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
            if (verticalOffset < - course_details_return_button.getY()){
                course_fl_layout.setAlpha(percent);
                course_details_return_button.setVisibility(View.VISIBLE);
                course_details_return_button1.setVisibility(View.INVISIBLE);
                course_details_download_button.setVisibility(View.VISIBLE);
                course_details_download_button1.setVisibility(View.INVISIBLE);
            } else {
                course_fl_layout.setAlpha(0);
                course_details_return_button.setVisibility(View.INVISIBLE);
                course_details_return_button1.setVisibility(View.VISIBLE);
                course_details_download_button.setVisibility(View.INVISIBLE);
                course_details_download_button1.setVisibility(View.VISIBLE);
            }
            if (verticalOffset <= - course_details_Name.getY() - course_details_Name.getHeight()){
                course_fl_layout_title.setVisibility(View.VISIBLE);
            } else {
                course_fl_layout_title.setVisibility(View.INVISIBLE);
            }
            LinearLayout course_catalog_label1 = mDetailsView.findViewById(R.id.course_catalog_label1);
//            LinearLayout course_catalog_label_content_layout_main = mDetailsView.findViewById(R.id.course_catalog_label_content_layout_main);
            View coursepacket_details_line6 = mDetailsView.findViewById(R.id.coursepacket_details_line6);
            LinearLayout course_catalog_label = mDetailsView.findViewById(R.id.course_catalog_label);
            if (mCurrentTab.equals("Details") ){
                course_catalog_label1.setAlpha(0);
                coursepacket_details_line6.setAlpha(0);
                course_catalog_label.setAlpha(1);
                if (verticalOffset <= -course_label1.getY() + course_label.getHeight() + course_label.getY()) {
                    course_label.setAlpha(percent);
                    course_label1.setAlpha(0);
                    course_imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
                    course_imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
                } else {
                    course_label.setAlpha(0);
                    course_label1.setAlpha(1);
                    course_imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
                    course_imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
                }
            } else if (mCurrentTab.equals("Catalog")){
                course_catalog_label1.setAlpha(0);
                coursepacket_details_line6.setAlpha(0);
                course_catalog_label.setAlpha(1);
                if (verticalOffset <= -course_label1.getY() + course_label.getHeight() + course_label.getY()) {
                    course_label.setAlpha(percent);
                    course_label1.setAlpha(0);
                    course_catalog_label.setAlpha(0);
                    course_catalog_label1.setAlpha(percent);
                    coursepacket_details_line6.setAlpha(percent);
                    course_imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
                    course_imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
                } else {
                    course_label.setAlpha(0);
                    course_label1.setAlpha(1);
                    course_catalog_label.setAlpha(1);
                    course_catalog_label1.setAlpha(0);
                    coursepacket_details_line6.setAlpha(0);
                    course_imgv_cursor.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor_white));
                    course_imgv_cursor1.setBackground(mControlMainActivity.getDrawable(R.drawable.image_cusor));
                }
            }
        });
        LinearLayout course_details_bottomlayout1 = mDetailsView.findViewById(R.id.course_details_bottomlayout1);
        LinearLayout course_details_bottomlayout = mDetailsView.findViewById(R.id.course_details_bottomlayout);
        if (courseInfo.mCourseIsHave.equals("1")){
            //已购买的课程将按钮栏替换掉
            course_details_bottomlayout1.setVisibility(View.VISIBLE);
            course_details_bottomlayout.setVisibility(View.INVISIBLE);
        } else {
            course_details_bottomlayout1.setVisibility(View.INVISIBLE);
            course_details_bottomlayout.setVisibility(View.VISIBLE);
        }
    }
    public void CourseCatalogRecordInit(CourseInfo courseInfo){
        if (courseInfo == null){
            return;
        }
        if (courseInfo.mCourseChaptersInfoList == null){
            return;
        }
        int recordCourseNum = 0;
        LinearLayout course_catalog_label_content_layout = mDetailsView.findViewById(R.id.course_catalog_label_content_layout);
        course_catalog_label_content_layout.removeAllViews();
        for (int i = 0; i < courseInfo.mCourseChaptersInfoList.size() ; i++){
            CourseChaptersInfo courseChaptersInfo = courseInfo.mCourseChaptersInfoList.get(i);
            if (courseChaptersInfo == null){
                continue;
            }
            recordCourseNum = recordCourseNum + courseChaptersInfo.mCourseSectionsInfoList.size();
            View catalog_chapterview = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_chapter, null);
            TextView course_catalog_label_name = catalog_chapterview.findViewById(R.id.course_catalog_label_name);
            course_catalog_label_name.setText(courseChaptersInfo.mCourseChaptersName);
            ImageView course_catalog_label_arrow_down = catalog_chapterview.findViewById(R.id.course_catalog_label_arrow_down);
            ImageView course_catalog_label_arrow_right = catalog_chapterview.findViewById(R.id.course_catalog_label_arrow_right);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_catalog_label_arrow_down.getLayoutParams();
            ll.width = 0;
            course_catalog_label_arrow_down.setLayoutParams(ll);
            LinearLayout course_catalog_label_content = catalog_chapterview.findViewById(R.id.course_catalog_label_content);
            ModelExpandView course_catalog_label_expandView = catalog_chapterview.findViewById(R.id.course_catalog_label_expandView);
            LinearLayout course_catalog_label_namelayout = catalog_chapterview.findViewById(R.id.course_catalog_label_namelayout);
            course_catalog_label_namelayout.setClickable(true);
            course_catalog_label_namelayout.setOnClickListener(v-> {
                // TODO Auto-generated method stub
                if (course_catalog_label_expandView.isExpand()) {
                    course_catalog_label_expandView.collapse();
                    //收缩隐藏布局
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_label_expandView.getLayoutParams();
                    rl.height = 0;
                    course_catalog_label_expandView.setLayoutParams(rl);
                    course_catalog_label_expandView.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_label_arrow_right.getLayoutParams();
                    ll1.width = catalog_chapterview.getResources().getDimensionPixelSize(R.dimen.dp6);
                    course_catalog_label_arrow_right.setLayoutParams(ll1);
                    ll1 = (LinearLayout.LayoutParams) course_catalog_label_arrow_down.getLayoutParams();
                    ll1.width = 0;
                    course_catalog_label_arrow_down.setLayoutParams(ll1);
                } else {
                    if (courseChaptersInfo.mCourseSectionsInfoList.size() == 0){
                        Toast.makeText(mControlMainActivity,"本章节暂时没有课程",Toast.LENGTH_SHORT);
                        return;
                    }
                    course_catalog_label_expandView.expand();
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_label_expandView.getLayoutParams();
                    rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    course_catalog_label_expandView.setLayoutParams(rl);
                    course_catalog_label_expandView.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_label_arrow_right.getLayoutParams();
                    ll1.width = 0;
                    course_catalog_label_arrow_right.setLayoutParams(ll1);
                    ll1 = (LinearLayout.LayoutParams) course_catalog_label_arrow_down.getLayoutParams();
                    ll1.width = catalog_chapterview.getResources().getDimensionPixelSize(R.dimen.dp10);
                    course_catalog_label_arrow_down.setLayoutParams(ll1);
                    CourseCatalogRecordSectionsInit(course_catalog_label_content,courseChaptersInfo.mCourseChaptersId);
                }
            });
            course_catalog_label_content_layout.addView(catalog_chapterview);
            if (courseInfo.mCourseChaptersInfoList.size() - 1 == i){
                //隐藏
                View course_catalog_label_line1 = catalog_chapterview.findViewById(R.id.course_catalog_label_line1);
                course_catalog_label_line1.setVisibility(View.INVISIBLE);
            }
        }
        TextView course_catalog_label_record = mDetailsView.findViewById(R.id.course_catalog_label_record);
        TextView course_catalog_label_record1 = mDetailsView.findViewById(R.id.course_catalog_label_record1);
        course_catalog_label_record1.setText("录播(" + recordCourseNum + ")");
        course_catalog_label_record.setText("录播(" + recordCourseNum + ")");
        TextView course_catalog_label_live = mDetailsView.findViewById(R.id.course_catalog_label_live);
        TextView course_catalog_label_live1 = mDetailsView.findViewById(R.id.course_catalog_label_live1);
        int liveCourseNum = courseInfo.mCourseClassTimeInfoTodayList.size() + courseInfo.mCourseClassTimeInfoBeforeList.size() + courseInfo.mCourseClassTimeInfoAfterList.size();
        course_catalog_label_live.setText("直播(" + liveCourseNum + ")");
        course_catalog_label_live1.setText("直播(" + liveCourseNum + ")");
    }
    public void CourseCatalogRecordSectionsInit(LinearLayout course_catalog_label_content,String id){
        if (mCourseInfo == null){
            return;
        }
        if (mCourseInfo.mCourseChaptersInfoList == null){
            return;
        }
        CourseChaptersInfo courseChaptersInfo = null;
        for (int i = 0; i < mCourseInfo.mCourseChaptersInfoList.size();i ++){
            if (mCourseInfo.mCourseChaptersInfoList.get(i).mCourseChaptersId.equals(id)){
                courseChaptersInfo = mCourseInfo.mCourseChaptersInfoList.get(i);
                break;
            }
        }
        if (courseChaptersInfo == null){
            return;
        }
        course_catalog_label_content.removeAllViews();
        for (int i = 0 ; i < courseChaptersInfo.mCourseSectionsInfoList.size() ; i ++){
            CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(i);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_chapter1, null);
            TextView course_catalog_record_chapter_name = view.findViewById(R.id.course_catalog_record_chapter_name);
            course_catalog_record_chapter_name.setText(courseSectionsInfo.mCourseSectionsName);
            TextView course_catalog_record_chapter_learnprogress = view.findViewById(R.id.course_catalog_record_chapter_learnprogress);
            course_catalog_record_chapter_learnprogress.setText(courseSectionsInfo.mCourseSectionsLearnProgress);
            TextView course_catalog_record_chapter_time = view.findViewById(R.id.course_catalog_record_chapter_time);
            course_catalog_record_chapter_time.setText(courseSectionsInfo.mCourseSectionsTime);
            TextView course_catalog_record_chapter_price = view.findViewById(R.id.course_catalog_record_chapter_price);
            course_catalog_record_chapter_price.setText(courseSectionsInfo.mCourseSectionsPrice);
            view.setOnClickListener(v->{
                //点击暂时为看直播
                CourseCatalogLiveGo();
            });
            course_catalog_label_content.addView(view);
            if (courseChaptersInfo.mCourseSectionsInfoList.size() != 1 && i != (courseChaptersInfo.mCourseSectionsInfoList.size() - 1)){
                //添加横线
                View lineView = new View(mControlMainActivity);
                lineView.setBackgroundColor(view.getResources().getColor(R.color.whitee5e5e5));
                course_catalog_label_content.addView(lineView);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) lineView.getLayoutParams();
                ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
                ll.height = view.getResources().getDimensionPixelSize(R.dimen.dp1);
                lineView.setLayoutParams(ll);
            }
        }
    }
    public void CourseCatalogLiveInit(CourseInfo courseInfo){
        if (courseInfo == null){
            return;
        }
        if (courseInfo.mCourseClassTimeInfoTodayList == null || courseInfo.mCourseClassTimeInfoBeforeList == null || courseInfo.mCourseClassTimeInfoAfterList == null){
            return;
        }
        LinearLayout course_catalog_label_content_layout = mDetailsView.findViewById(R.id.course_catalog_label_content_layout);
        course_catalog_label_content_layout.removeAllViews();
        View catalog_chapter_liveview = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_live_chapter, null);
        //今日
        LinearLayout course_catalog_live_label_namelayout = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_namelayout);
        ImageView course_catalog_live_label_arrow_down = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_down);
        ImageView course_catalog_live_label_arrow_right = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_right);
        ModelExpandView course_catalog_live_label_expandView = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_expandView);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down.getLayoutParams();
        ll.width = 0;
        course_catalog_live_label_arrow_down.setLayoutParams(ll);
        LinearLayout course_catalog_live_label_content = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_content);
        course_catalog_live_label_namelayout.setClickable(true);
        course_catalog_live_label_namelayout.setOnClickListener(v-> {
            // TODO Auto-generated method stub
            if (course_catalog_live_label_expandView.isExpand()) {
                course_catalog_live_label_expandView.collapse();
                //收缩隐藏布局
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView.getLayoutParams();
                rl.height = 0;
                course_catalog_live_label_expandView.setLayoutParams(rl);
                course_catalog_live_label_expandView.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp6);
                course_catalog_live_label_arrow_right.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_down.setLayoutParams(ll1);
            } else {
                if (courseInfo.mCourseClassTimeInfoTodayList.size() == 0){
                    Toast.makeText(mControlMainActivity,"今日暂时没有课程",Toast.LENGTH_SHORT);
                    return;
                }
                course_catalog_live_label_expandView.expand();
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView.getLayoutParams();
                rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                course_catalog_live_label_expandView.setLayoutParams(rl);
                course_catalog_live_label_expandView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_right.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp10);
                course_catalog_live_label_arrow_down.setLayoutParams(ll1);
                CourseCatalogLiveClassTimeInit(course_catalog_live_label_content,"today");
            }
        });
        //后续
        LinearLayout course_catalog_live_label_namelayout1 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_namelayout1);
        ImageView course_catalog_live_label_arrow_down1 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_down1);
        ImageView course_catalog_live_label_arrow_right1 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_right1);
        ModelExpandView course_catalog_live_label_expandView1 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_expandView1);
        ll = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down1.getLayoutParams();
        ll.width = 0;
        course_catalog_live_label_arrow_down1.setLayoutParams(ll);
        LinearLayout course_catalog_live_label_content1 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_content1);
        course_catalog_live_label_namelayout1.setClickable(true);
        course_catalog_live_label_namelayout1.setOnClickListener(v-> {
            // TODO Auto-generated method stub
            if (course_catalog_live_label_expandView1.isExpand()) {
                course_catalog_live_label_expandView1.collapse();
                //收缩隐藏布局
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView1.getLayoutParams();
                rl.height = 0;
                course_catalog_live_label_expandView1.setLayoutParams(rl);
                course_catalog_live_label_expandView1.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right1.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp6);
                course_catalog_live_label_arrow_right1.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down1.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_down1.setLayoutParams(ll1);
            } else {
                if (courseInfo.mCourseClassTimeInfoAfterList.size() == 0){
                    Toast.makeText(mControlMainActivity,"后续暂时没有课程",Toast.LENGTH_SHORT);
                    return;
                }
                course_catalog_live_label_expandView1.expand();
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView1.getLayoutParams();
                rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                course_catalog_live_label_expandView1.setLayoutParams(rl);
                course_catalog_live_label_expandView1.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right1.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_right1.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down1.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp10);
                course_catalog_live_label_arrow_down1.setLayoutParams(ll1);
                CourseCatalogLiveClassTimeInit(course_catalog_live_label_content1,"after");
            }
        });

        LinearLayout course_catalog_live_label_namelayout2 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_namelayout2);
        ImageView course_catalog_live_label_arrow_down2 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_down2);
        ImageView course_catalog_live_label_arrow_right2 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_arrow_right2);
        ModelExpandView course_catalog_live_label_expandView2 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_expandView2);
        ll = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down2.getLayoutParams();
        ll.width = 0;
        course_catalog_live_label_arrow_down2.setLayoutParams(ll);
        LinearLayout course_catalog_live_label_content2 = catalog_chapter_liveview.findViewById(R.id.course_catalog_live_label_content2);
        course_catalog_live_label_namelayout2.setClickable(true);
        course_catalog_live_label_namelayout2.setOnClickListener(v-> {
            // TODO Auto-generated method stub
            if (course_catalog_live_label_expandView2.isExpand()) {
                course_catalog_live_label_expandView2.collapse();
                //收缩隐藏布局
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView2.getLayoutParams();
                rl.height = 0;
                course_catalog_live_label_expandView2.setLayoutParams(rl);
                course_catalog_live_label_expandView2.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right2.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp6);
                course_catalog_live_label_arrow_right2.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down2.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_down2.setLayoutParams(ll1);
            } else {
                if (courseInfo.mCourseClassTimeInfoBeforeList.size() == 0){
                    Toast.makeText(mControlMainActivity,"历史暂时没有课程",Toast.LENGTH_SHORT);
                    return;
                }
                course_catalog_live_label_expandView2.expand();
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) course_catalog_live_label_expandView2.getLayoutParams();
                rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                course_catalog_live_label_expandView2.setLayoutParams(rl);
                course_catalog_live_label_expandView2.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_right2.getLayoutParams();
                ll1.width = 0;
                course_catalog_live_label_arrow_right2.setLayoutParams(ll1);
                ll1 = (LinearLayout.LayoutParams) course_catalog_live_label_arrow_down2.getLayoutParams();
                ll1.width = catalog_chapter_liveview.getResources().getDimensionPixelSize(R.dimen.dp10);
                course_catalog_live_label_arrow_down2.setLayoutParams(ll1);
                CourseCatalogLiveClassTimeInit(course_catalog_live_label_content2,"before");
            }
        });
        course_catalog_label_content_layout.addView(catalog_chapter_liveview);
    }
    private void CourseCatalogLiveClassTimeInit(LinearLayout course_catalog_label_content,String type){
        if (mCourseInfo == null || type == null){
            return;
        }
        if (type.equals("today")){
            if (mCourseInfo.mCourseClassTimeInfoTodayList == null){
                return;
            }
            course_catalog_label_content.removeAllViews();
            CourseClassTimeInfo courseClassTimeInfo = null;
            for (int i = 0; i < mCourseInfo.mCourseClassTimeInfoTodayList.size();i ++){
                courseClassTimeInfo = mCourseInfo.mCourseClassTimeInfoTodayList.get(i);
                if (courseClassTimeInfo == null){
                    continue;
                }
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_live_chapter1, null);
                TextView course_catalog_live_chapter_name = view.findViewById(R.id.course_catalog_live_chapter_name);
                course_catalog_live_chapter_name.setText(courseClassTimeInfo.mCourseClassTimeName);
                TextView course_catalog_live_chapter_time = view.findViewById(R.id.course_catalog_live_chapter_time);
                course_catalog_live_chapter_time.setText(courseClassTimeInfo.mCourseClassTimeStartTime);
                course_catalog_label_content.addView(view);
                if (mCourseInfo.mCourseClassTimeInfoTodayList.size() != 1 && i != (mCourseInfo.mCourseClassTimeInfoTodayList.size() - 1)){
                    //添加横线
                    View lineView = new View(mControlMainActivity);
                    lineView.setBackgroundColor(view.getResources().getColor(R.color.whitee5e5e5));
                    course_catalog_label_content.addView(lineView);
                    LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) lineView.getLayoutParams();
                    ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    ll.height = view.getResources().getDimensionPixelSize(R.dimen.dp1);
                    lineView.setLayoutParams(ll);
                }
            }
        } else if (type.equals("before")){
            if (mCourseInfo.mCourseClassTimeInfoBeforeList == null){
                return;
            }
            course_catalog_label_content.removeAllViews();
            CourseClassTimeInfo courseClassTimeInfo = null;
            for (int i = 0; i < mCourseInfo.mCourseClassTimeInfoBeforeList.size();i ++){
                courseClassTimeInfo = mCourseInfo.mCourseClassTimeInfoBeforeList.get(i);
                if (courseClassTimeInfo == null){
                    continue;
                }
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_live_chapter1, null);
                TextView course_catalog_live_chapter_name = view.findViewById(R.id.course_catalog_live_chapter_name);
                course_catalog_live_chapter_name.setText(courseClassTimeInfo.mCourseClassTimeName);
                TextView course_catalog_live_chapter_time = view.findViewById(R.id.course_catalog_live_chapter_time);
                course_catalog_live_chapter_time.setText(courseClassTimeInfo.mCourseClassTimeStartTime);
                course_catalog_label_content.addView(view);
                if (mCourseInfo.mCourseClassTimeInfoBeforeList.size() != 1 && i != (mCourseInfo.mCourseClassTimeInfoBeforeList.size() - 1)){
                    //添加横线
                    View lineView = new View(mControlMainActivity);
                    lineView.setBackgroundColor(view.getResources().getColor(R.color.whitee5e5e5));
                    course_catalog_label_content.addView(lineView);
                    LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) lineView.getLayoutParams();
                    ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    ll.height = view.getResources().getDimensionPixelSize(R.dimen.dp1);
                    lineView.setLayoutParams(ll);
                }
            }
        } else if (type.equals("after")){
            if (mCourseInfo.mCourseClassTimeInfoAfterList == null){
                return;
            }
            course_catalog_label_content.removeAllViews();
            CourseClassTimeInfo courseClassTimeInfo = null;
            for (int i = 0; i < mCourseInfo.mCourseClassTimeInfoAfterList.size();i ++){
                courseClassTimeInfo = mCourseInfo.mCourseClassTimeInfoAfterList.get(i);
                if (courseClassTimeInfo == null){
                    continue;
                }
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_catalog_live_chapter1, null);
                TextView course_catalog_live_chapter_name = view.findViewById(R.id.course_catalog_live_chapter_name);
                course_catalog_live_chapter_name.setText(courseClassTimeInfo.mCourseClassTimeName);
                TextView course_catalog_live_chapter_time = view.findViewById(R.id.course_catalog_live_chapter_time);
                course_catalog_live_chapter_time.setText(courseClassTimeInfo.mCourseClassTimeStartTime);
                course_catalog_label_content.addView(view);
                if (mCourseInfo.mCourseClassTimeInfoAfterList.size() != 1 && i != (mCourseInfo.mCourseClassTimeInfoAfterList.size() - 1)){
                    //添加横线
                    View lineView = new View(mControlMainActivity);
                    lineView.setBackgroundColor(view.getResources().getColor(R.color.whitee5e5e5));
                    course_catalog_label_content.addView(lineView);
                    LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) lineView.getLayoutParams();
                    ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    ll.height = view.getResources().getDimensionPixelSize(R.dimen.dp1);
                    lineView.setLayoutParams(ll);
                }
            }
        }
    }
    private void CourseQuestionAddInit(){
        mPage = "QuestionAdd";
        HideAllLayout();
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        if (mQuestionViewAdd == null) {
            mQuestionViewAdd = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_question_add, null);
            ImageView course_question_add_layout_return_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_return_button1);
            course_question_add_layout_return_button1.setOnClickListener(this);
        }
        RecyclerView course_question_add_layout_image = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_image);
        course_question_add_layout_image.setLayoutManager(new GridLayoutManager(mControlMainActivity, 3));
        selPhotosPath = new ArrayList<>();
        //=============图片九宫格=========================
        mPictureAdapter = null;
        mPictureBeansList = new ArrayList<>();
        //设置布局管理器
        mRecyclerView = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_image);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mControlMainActivity, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        if(mPictureAdapter == null){
            //设置适配器
            mPictureAdapter = new ControllerPictureAdapter(mControlMainActivity, mPictureBeansList);
            mRecyclerView.setAdapter(mPictureAdapter);
            //添加分割线
            //设置添加删除动画
            //调用ListView的setSelected(!ListView.isSelected())方法，这样就能及时刷新布局
            mRecyclerView.setSelected(true);
        }else{
            mPictureAdapter.notifyDataSetChanged();
        }
        //图片九宫格点击事件
        mPictureAdapter.setOnItemClickLitener(new ControllerPictureAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View v,int position) {
                //打开自定义的图片预览对话框
                List<String> photos = mPictureAdapter.getAllPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);

                NewImagePagerDialogFragment newImagePagerDialogFragment = NewImagePagerDialogFragment.getInstance(mControlMainActivity,photos,position,screenLocation, v.getWidth(),
                        v.getHeight(),false);
                newImagePagerDialogFragment.show(mControlMainActivity.getSupportFragmentManager(),"preview img");
            }

            @Override
            public void onItemAddClick() {
                PhotoPicker.builder()
                        .setPhotoCount(mPictureAdapter.MAX)
                        .setGridColumnCount(3)
//                        .setSelected(selPhotosPath)
                        .start(mControlMainActivity, ControllerGlobals.CHOOSE_PIC_REQUEST_CODE);
                mPictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemDeleteClick(View view, int position){
                mPictureBeansList.remove(position);
                mPictureAdapter.notifyDataSetChanged();
                if (mPictureBeansList.size() == 0) {
                    mQuestionPublishImage = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_blue);
                } else {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_gray);
                }
            }
        });
        ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
        course_question_add_layout_commit_button1.setClickable(true);
        course_question_add_layout_commit_button1.setOnClickListener(v->{
            //点击发布问题
        });
        EditText course_question_add_layout_contentetitledittext = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_contentetitledittext);
        course_question_add_layout_contentetitledittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    mQuestionPublishContent = true;
                } else {
                    mQuestionPublishContent = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_blue);
                } else {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EditText course_question_add_layout_contentedittext = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_contentedittext);
        course_question_add_layout_contentedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    mQuestionPublishTitle = true;
                } else {
                    mQuestionPublishTitle = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_blue);
                } else {
                    ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
                    course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        course_main.addView(mQuestionViewAdd);
    }
    public void ModelCourseCoverQuestionPictureAdd(Intent data){
        //添加图片，发布按钮改为蓝色
        mQuestionPublishImage = true;
        if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
            ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
            course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_blue);
        } else {
            ImageView course_question_add_layout_commit_button1 = mQuestionViewAdd.findViewById(R.id.course_question_add_layout_commit_button1);
            course_question_add_layout_commit_button1.setBackgroundResource(R.drawable.button_publish_gray);
        }
        if (data != null) {
            selPhotosPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
        }
        if (selPhotosPath != null) {

            //下面的代码主要用于这样一个场景，就是注释了.setSelected(selPhotosPath)之后，还想要保证选择的图片不重复
					/*for(String path : selPhotosPath){
						Log.w(TAG,"path="+path);///storage/emulated/0/tempHxzk/IMG_1498034535796.jpg
						boolean existThisPic = false;
						for(int i=0;i<mPictureBeansList.size();i++){
							if(path.equals(mPictureBeansList.get(i).getPicPath())){
								//如果新选择的图片集合中存在之前选中的图片，那么跳过去
								existThisPic = true;
								break;
							}
						}
						if(! existThisPic){
							PictureBean pictureBean = new PictureBean();
							pictureBean.setPicPath(path);
							pictureBean.setPicName(getFileName(path));
							//去掉总数目的限制，这里通过增大MAX的数字来实现
							if (mPictureBeansList.size() < mPictureAdapter.MAX) {
								mPictureBeansList.add(pictureBean);
							} else {
								Toast.makeText(MainActivity.this, "最多可以选择" + mPictureAdapter.MAX + "张图片", Toast.LENGTH_SHORT).show();
								break;
							}
						}
					}*/

            //是常规操作，和上面的代码不可共存
            for (String path : selPhotosPath) {
                ControllerPictureBean pictureBean = new ControllerPictureBean();
                pictureBean.setPicPath(path);
                pictureBean.setPicName(ControllerGlobals.getFileName(path));
                //去掉总数目的限制，这里通过增大MAX的数字来实现
                if (mPictureBeansList.size() < mPictureAdapter.MAX) {
                    mPictureBeansList.add(pictureBean);
                } else {
                    Toast.makeText(mControlMainActivity, "最多可以选择" + mPictureAdapter.MAX + "张图片", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            mPictureAdapter.notifyDataSetChanged();
        }
    }
    private void CourseQuestionDetailsInit(String questionId){
        if (modelCourse == null) {
            return;
        }
        mPage = "QuestionDetails";
        HideAllLayout();
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        if (mQuestionDetailsView == null) {
            mQuestionDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_questiondetails, null);
        }
        for (int i = 0;i < mCourseInfo.mCourseQuestionInfoList.size() ; i ++){
            CourseQuestionInfo courseQuestionInfo = mCourseInfo.mCourseQuestionInfoList.get(i);
            if (courseQuestionInfo == null){
                continue;
            }
            if (courseQuestionInfo.mCourseAnswerId.equals("0") && questionId.equals(courseQuestionInfo.mCourseQuestionId)){ //一级问答
                //添加头像
                ControllerCustomRoundAngleImageView course_questiondetails_child_headportrait = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_headportrait);
                if (courseQuestionInfo.mCourseQuestionCommentHead2 != null){
                    Glide.with(mControlMainActivity).
                            load(courseQuestionInfo.mCourseQuestionCommentHead2).listener(new RequestListener<Drawable>() {
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
                            .error(mControlMainActivity.getResources().getDrawable(R.drawable.image_teachersdefault)).into(course_questiondetails_child_headportrait);
                }
                //回答者名字
                TextView course_questiondetails_child_name = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_name);
                course_questiondetails_child_name.setText(courseQuestionInfo.mCourseQuestionCommentName2);
                course_questiondetails_child_name.setHint(courseQuestionInfo.mCourseQuestionCommentId2);
                //回答内容
                TextView course_questiondetails_child_message = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_message);
                course_questiondetails_child_message.setText(courseQuestionInfo.mCourseQuestionContent);
                //回答图片？？
                //时间
                TextView course_questiondetails_child_time = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_time);
                course_questiondetails_child_time.setText(courseQuestionInfo.mCourseQuestionTime);
                //浏览人数
                TextView course_questiondetails_child_look = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_look);
                course_questiondetails_child_look.setText("浏览" + courseQuestionInfo.mCourseQuestionLookNum);
                break;
            }
        }
        List<CourseQuestionInfo> list = CourseQuestionDetailsViewMap.get(questionId);
        if (list == null){
            ImageView course_questiondetails_layout_return_button1 = mQuestionDetailsView.findViewById(R.id.course_questiondetails_layout_return_button1);
            course_questiondetails_layout_return_button1.setClickable(true);
            course_questiondetails_layout_return_button1.setOnClickListener(v->{
                CourseQuestionShow();
            });
            LinearLayout course_questiondetails_content = mQuestionDetailsView.findViewById(R.id.course_questiondetails_content);
            course_questiondetails_content.removeAllViews();
            course_main.addView(mQuestionDetailsView);
            return;
        }
        TextView course_questiondetails_child_discusstext = mQuestionDetailsView.findViewById(R.id.course_questiondetails_child_discusstext);
        course_questiondetails_child_discusstext.setText( String.valueOf(list.size()));
        LinearLayout course_questiondetails_content = mQuestionDetailsView.findViewById(R.id.course_questiondetails_content);
        course_questiondetails_content.removeAllViews();
        for (int i = 0; i < list.size() ; i ++){
            CourseQuestionInfo courseQuestionInfo = list.get(i);
            if (courseQuestionInfo == null){
                continue;
            }
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_questiondetails1, null);
            view.setOnClickListener(v->{
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + courseQuestionInfo.mCourseQuestionCommentName2,false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {

                    }

                    @Override
                    public void image() {

                    }
                });
            });
            //添加头像
            ControllerCustomRoundAngleImageView course_questiondetails1_child_headportrait = view.findViewById(R.id.course_questiondetails1_child_headportrait);
            if (courseQuestionInfo.mCourseQuestionCommentHead2 != null){
                Glide.with(mControlMainActivity).
                        load(courseQuestionInfo.mCourseQuestionCommentHead2).listener(new RequestListener<Drawable>() {
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
                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.image_teachersdefault)).into(course_questiondetails1_child_headportrait);
            }
            TextView course_questiondetails1_child_name = view.findViewById(R.id.course_questiondetails1_child_name);
            course_questiondetails1_child_name.setText(courseQuestionInfo.mCourseQuestionCommentName2);
            TextView course_questiondetails1_child_time = view.findViewById(R.id.course_questiondetails1_child_time);
            course_questiondetails1_child_time.setText(courseQuestionInfo.mCourseQuestionTime);
            TextView course_questiondetails1_child_message = view.findViewById(R.id.course_questiondetails1_child_message);
            course_questiondetails1_child_message.setText(courseQuestionInfo.mCourseQuestionContent);
            if (i == (list.size() - 1)){
                View course_questiondetails1_child_line = view.findViewById(R.id.course_questiondetails1_child_line);
                course_questiondetails1_child_line.setVisibility(View.INVISIBLE);
            }
            course_questiondetails_content.addView(view);
        }
//        TextView course_questiondetails_layout_titletext = mQuestionDetailsView.findViewById(R.id.course_questiondetails_layout_titletext);
//        course_questiondetails_layout_titletext.setText("全部评论");
        ImageView course_questiondetails_layout_return_button1 = mQuestionDetailsView.findViewById(R.id.course_questiondetails_layout_return_button1);
        course_questiondetails_layout_return_button1.setClickable(true);
        course_questiondetails_layout_return_button1.setOnClickListener(v->{
            CourseQuestionShow();
        });
        course_main.addView(mQuestionDetailsView);
    }
    private void CourseDownloadInit(){
        if (mCourseDownloadDialog != null){
            mCourseDownloadDialog.dismiss();
        }
        mCourseDownloadDialog = new ControllerPopDialog(mControlMainActivity,R.style.customdialogstyle,R.layout.modelcoursedetails_download);
        mCourseDownloadDialog.setOnKeyListener(keylistener);
        mCourseDownloadDialog.show();
//        TextView coursedetails_download_num = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_num);
//        coursedetails_download_num.setText("5");
        LinearLayout coursedetails_download_chapterlist = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_chapterlist);
        View view1 = null;
        int count = 0;
        for (int i = 0; i < mCourseInfo.mCourseChaptersInfoList.size() ; i ++) {
            CourseChaptersInfo courseChaptersInfo = mCourseInfo.mCourseChaptersInfoList.get(i);
            if (courseChaptersInfo == null){
                continue;
            }
            for (int num = 0; num < courseChaptersInfo.mCourseSectionsInfoList.size(); num ++){
                CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(num);
                if (courseSectionsInfo == null){
                    continue;
                }
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download1, null);
                TextView coursedetails_download1_name = view.findViewById(R.id.coursedetails_download1_name);
                coursedetails_download1_name.setText(courseSectionsInfo.mCourseSectionsName);
                coursedetails_download1_name.setHint(courseSectionsInfo.mCourseSectionsId);
                ImageView coursedetails_download1_image = view.findViewById(R.id.coursedetails_download1_image);
                CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo = mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId);
                if (courseRecordPlayDownloadInfo != null) {
                    ControllerRoundProgressBar coursedetails_download1_downloadprogress = view.findViewById(R.id.coursedetails_download1_downloadprogress);
                    LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_download1_downloadprogress.getLayoutParams();
                    ll.width = view.getResources().getDimensionPixelSize(R.dimen.dp15);
                    coursedetails_download1_downloadprogress.setLayoutParams(ll);
                    ll = (LinearLayout.LayoutParams) coursedetails_download1_image.getLayoutParams();
                    ll.width = 0;
                    coursedetails_download1_image.setLayoutParams(ll);
                    int progress = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progress = Math.toIntExact(Long.valueOf(courseRecordPlayDownloadInfo.mCourseSectionsDownloadSize)
                                / Long.valueOf(courseRecordPlayDownloadInfo.mCourseSectionsSize));
                    }
                    coursedetails_download1_downloadprogress.setProgress(progress);
                    if ( progress == 100) {
                        coursedetails_download1_downloadprogress = view.findViewById(R.id.coursedetails_download1_downloadprogress);
                        ll = (LinearLayout.LayoutParams) coursedetails_download1_downloadprogress.getLayoutParams();
                        ll.width = 0;
                        coursedetails_download1_downloadprogress.setLayoutParams(ll);
                        ll = (LinearLayout.LayoutParams) coursedetails_download1_image.getLayoutParams();
                        ll.width = view.getResources().getDimensionPixelSize(R.dimen.dp15);
                        coursedetails_download1_image.setLayoutParams(ll);
                        coursedetails_download1_image.setBackgroundResource(R.drawable.button_download_finish);
                    }
                }
                coursedetails_download1_image.setOnClickListener(v->{  //点击开始下载
                    int id = getV7ImageResourceId(coursedetails_download1_image);
                    if (id == R.drawable.button_download_circle_blue){
                        ControllerRoundProgressBar coursedetails_download1_downloadprogress = view.findViewById(R.id.coursedetails_download1_downloadprogress);
                        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_download1_downloadprogress.getLayoutParams();
                        ll.width = view.getResources().getDimensionPixelSize(R.dimen.dp15);
                        coursedetails_download1_downloadprogress.setLayoutParams(ll);
                        ll = (LinearLayout.LayoutParams) coursedetails_download1_image.getLayoutParams();
                        ll.width = 0;
                        coursedetails_download1_image.setLayoutParams(ll);
                        //进度为0
                        coursedetails_download1_downloadprogress.setProgress(0);
                        CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo1 = new CourseRecordPlayDownloadInfo();
                        courseRecordPlayDownloadInfo1.mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;
                        courseRecordPlayDownloadInfo1.mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;
                        courseRecordPlayDownloadInfo1.mCourseSectionsId = courseSectionsInfo.mCourseSectionsId;
                        courseRecordPlayDownloadInfo1.mCourseSectionsName = courseSectionsInfo.mCourseSectionsName;
                        courseRecordPlayDownloadInfo1.mCourseSectionsDownloadSize = "0";
                        courseRecordPlayDownloadInfo1.mCourseSectionsSize = courseSectionsInfo.mCourseSectionsSize;
                        if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId,courseRecordPlayDownloadInfo1);
                            }
                        } else {
                            mCourseRecordPlayDownloadInfoMap.put(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo1);
                        }
                    }
                });
                coursedetails_download_chapterlist.addView(view);
                view1 = view;
                count ++;
            }
        }
        TextView coursedetails_download_sumnum = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_sumnum);
        coursedetails_download_sumnum.setText("/" + count);
        View line = view1.findViewById(R.id.coursedetails_download1_line1);
        line.setVisibility(View.INVISIBLE);
        //获取手机剩余存储空间
        TextView coursedetails_download_availalesize = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_availalesize);
        long size = getAvailaleSize();
        coursedetails_download_availalesize.setText("剩余空间：" + size + "G");
        TextView coursedetails_download_all = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_all);
        coursedetails_download_all.setOnClickListener(v->{  //点击全部缓存
            int num = coursedetails_download_chapterlist.getChildCount();
            for (int i = 0; i < num; i ++){
                View view = coursedetails_download_chapterlist.getChildAt(i);
                ImageView coursedetails_download1_image = view.findViewById(R.id.coursedetails_download1_image);
                TextView coursedetails_download1_name = view.findViewById(R.id.coursedetails_download1_name);
                int id = getV7ImageResourceId(coursedetails_download1_image);
                if (id == R.drawable.button_download_circle_blue){
                    ControllerRoundProgressBar coursedetails_download1_downloadprogress = view.findViewById(R.id.coursedetails_download1_downloadprogress);
                    LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_download1_downloadprogress.getLayoutParams();
                    ll.width = view.getResources().getDimensionPixelSize(R.dimen.dp15);
                    coursedetails_download1_downloadprogress.setLayoutParams(ll);
                    ll = (LinearLayout.LayoutParams) coursedetails_download1_image.getLayoutParams();
                    ll.width = 0;
                    coursedetails_download1_image.setLayoutParams(ll);
                    for (int mCourseChaptersInfoListNum = 0; mCourseChaptersInfoListNum < mCourseInfo.mCourseChaptersInfoList.size() ; mCourseChaptersInfoListNum ++) {
                        CourseChaptersInfo courseChaptersInfo = mCourseInfo.mCourseChaptersInfoList.get(mCourseChaptersInfoListNum);
                        if (courseChaptersInfo == null) {
                            continue;
                        }
                        boolean m_isFind = false;
                        for (int mCourseSectionsInfoListNum = 0; mCourseSectionsInfoListNum < courseChaptersInfo.mCourseSectionsInfoList.size(); mCourseSectionsInfoListNum++) {
                            CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(mCourseSectionsInfoListNum);
                            if (courseSectionsInfo == null) {
                                continue;
                            }
                            if (coursedetails_download1_name.getHint().toString().equals(courseSectionsInfo.mCourseSectionsId)){
                                m_isFind = true;
                                //将所有的未缓存视频加入缓存列表
                                CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo1 = new CourseRecordPlayDownloadInfo();
                                courseRecordPlayDownloadInfo1.mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;
                                courseRecordPlayDownloadInfo1.mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;
                                courseRecordPlayDownloadInfo1.mCourseSectionsId = courseSectionsInfo.mCourseSectionsId;
                                courseRecordPlayDownloadInfo1.mCourseSectionsName = courseSectionsInfo.mCourseSectionsName;
                                courseRecordPlayDownloadInfo1.mCourseSectionsDownloadSize = "0";
                                courseRecordPlayDownloadInfo1.mCourseSectionsSize = courseSectionsInfo.mCourseSectionsSize;
                                if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId,courseRecordPlayDownloadInfo1);
                                    }
                                } else {
                                    mCourseRecordPlayDownloadInfoMap.put(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo1);
                                }
                                coursedetails_download1_downloadprogress.setProgress(0);
                                break;
                            }
                        }
                        if (m_isFind){
                            break;
                        }
                    }
                }
            }
        });
        TextView coursedetails_download_manager = mCourseDownloadDialog.getWindow().findViewById(R.id.coursedetails_download_manager);
        coursedetails_download_manager.setOnClickListener(v->{ //点击管理缓存
            CourseDownloadManagerInit();
        });
    }
    private void CourseDownloadManagerInit() {
        if (modelCourse == null) {
            return;
        }
        if (mCourseDownloadDialog != null){
            mCourseDownloadDialog.dismiss();
        }
        mPage = "DownloadManager";
        HideAllLayout();
        RelativeLayout course_main = modelCourse.findViewById(R.id.course_main);
        if (mDownloadManagerView == null) {
            mDownloadManagerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager, null);
        }
        int count = 0;
        LinearLayout course_downloadmanager_layout_content = mDownloadManagerView.findViewById(R.id.course_downloadmanager_layout_content);
        course_downloadmanager_layout_content.removeAllViews();
        for (int i = 0; i < mCourseInfo.mCourseChaptersInfoList.size() ; i ++) {
            CourseChaptersInfo courseChaptersInfo = mCourseInfo.mCourseChaptersInfoList.get(i);
            if (courseChaptersInfo == null) {
                continue;
            }
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager_child, null);
            TextView course_downloadmanager_child_titletext = view.findViewById(R.id.course_downloadmanager_child_titletext);
            course_downloadmanager_child_titletext.setText(courseChaptersInfo.mCourseChaptersName);
            course_downloadmanager_child_titletext.setHint(courseChaptersInfo.mCourseChaptersId);
            if (courseChaptersInfo.mCourseSectionsInfoList.size() == 0){
                View course_downloadmanager_child_line1 = view.findViewById(R.id.course_downloadmanager_child_line1);
                course_downloadmanager_child_line1.setVisibility(View.INVISIBLE);
            }
            course_downloadmanager_layout_content.addView(view);
            LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
            course_downloadmanager_child_content.removeAllViews();
            for (int num = 0; num < courseChaptersInfo.mCourseSectionsInfoList.size(); num ++) {
                CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(num);
                if (courseSectionsInfo == null) {
                    continue;
                }
                CourseRecordPlayDownloadInfo info = mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId);
                if (info == null){ //没有添加下载的不做处理
                    continue;
                }
                View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager_child1, null);
                TextView course_downloadmanager_child1_name = view1.findViewById(R.id.course_downloadmanager_child1_name);
                course_downloadmanager_child1_name.setText(courseSectionsInfo.mCourseSectionsName);
                course_downloadmanager_child1_name.setHint(courseSectionsInfo.mCourseSectionsId);
                ProgressBar progress_bar_healthy = view1.findViewById(R.id.progress_bar_healthy);
                int progress = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress = Math.toIntExact(Long.valueOf(info.mCourseSectionsDownloadSize)
                            / Long.valueOf(info.mCourseSectionsSize));
                }
                progress_bar_healthy.setProgress(progress);
                ImageView course_downloadmanager_child1_function = view1.findViewById(R.id.course_downloadmanager_child1_function);
                TextView course_downloadmanager_child_state = view1.findViewById(R.id.course_downloadmanager_child_state);
                course_downloadmanager_child1_function.setOnClickListener(v->{
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_pause_blue){
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_play_blue);
                        course_downloadmanager_child_state.setText("已暂停");
                        progress_bar_healthy.setProgressDrawable(view1.getResources().getDrawable(R.drawable.progressbar_bg1));
                    } else if (id == R.drawable.button_play_blue){
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_pause_blue);
                        course_downloadmanager_child_state.setText("下载中");
                        progress_bar_healthy.setProgressDrawable(view1.getResources().getDrawable(R.drawable.progressbar_bg));
                    }
                });
                course_downloadmanager_child_content.addView(view1);
                count ++;
            }
        }
        TextView course_downloadmanager_num = mDownloadManagerView.findViewById(R.id.course_downloadmanager_num);
        course_downloadmanager_num.setText("0");
        TextView course_downloadmanager_sumnum = mDownloadManagerView.findViewById(R.id.course_downloadmanager_sumnum);
        course_downloadmanager_sumnum.setText("/" + count);
        //获取手机剩余存储空间
        TextView course_downloadmanager_availalesize = mDownloadManagerView.findViewById(R.id.course_downloadmanager_availalesize);
        long size = getAvailaleSize();
        course_downloadmanager_availalesize.setText("剩余空间：" + size + "G");
        ImageView course_downloadmanager_layout_return_button1 = mDownloadManagerView.findViewById(R.id.course_downloadmanager_layout_return_button1);
        course_downloadmanager_layout_return_button1.setOnClickListener(v->{
            CourseDetailsShow();
        });
        TextView course_downloadmanager_all = mDownloadManagerView.findViewById(R.id.course_downloadmanager_all);
        course_downloadmanager_all.setOnClickListener(v->{ //点击全部暂停
            int num = course_downloadmanager_layout_content.getChildCount();
            for (int i = 0; i < num; i ++){
                View view = course_downloadmanager_layout_content.getChildAt(i);
                LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                int childCount = course_downloadmanager_child_content.getChildCount();
                for (int j = 0; j < childCount ; j ++){
                    View childView = course_downloadmanager_child_content.getChildAt(j);
                    ImageView course_downloadmanager_child1_function = childView.findViewById(R.id.course_downloadmanager_child1_function);
                    TextView course_downloadmanager_child_state = childView.findViewById(R.id.course_downloadmanager_child_state);
                    ProgressBar progress_bar_healthy = childView.findViewById(R.id.progress_bar_healthy);
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_pause_blue){
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_play_blue);
                        course_downloadmanager_child_state.setText("已暂停");
                        progress_bar_healthy.setProgressDrawable(childView.getResources().getDrawable(R.drawable.progressbar_bg1));
                    }
                }
            }

        });
        TextView course_downloadmanager_startall = mDownloadManagerView.findViewById(R.id.course_downloadmanager_startall);
        course_downloadmanager_startall.setOnClickListener(v->{ //点击全部开始
            int num = course_downloadmanager_layout_content.getChildCount();
            for (int i = 0; i < num; i ++){
                View view = course_downloadmanager_layout_content.getChildAt(i);
                LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                int childCount = course_downloadmanager_child_content.getChildCount();
                for (int j = 0; j < childCount ; j ++){
                    View childView = course_downloadmanager_child_content.getChildAt(j);
                    ImageView course_downloadmanager_child1_function = childView.findViewById(R.id.course_downloadmanager_child1_function);
                    TextView course_downloadmanager_child_state = childView.findViewById(R.id.course_downloadmanager_child_state);
                    ProgressBar progress_bar_healthy = childView.findViewById(R.id.progress_bar_healthy);
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_play_blue){
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_pause_blue);
                        course_downloadmanager_child_state.setText("下载中");
                        progress_bar_healthy.setProgressDrawable(childView.getResources().getDrawable(R.drawable.progressbar_bg));
                    }
                }
            }
        });
        TextView course_downloadmanager_layout_edit = mDownloadManagerView.findViewById(R.id.course_downloadmanager_layout_edit);
        LinearLayout course_downloadmanager_function = mDownloadManagerView.findViewById(R.id.course_downloadmanager_function);
        LinearLayout course_downloadmanager_function1 = mDownloadManagerView.findViewById(R.id.course_downloadmanager_function1);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_downloadmanager_function.getLayoutParams();
        ll.height = mDownloadManagerView.getResources().getDimensionPixelSize(R.dimen.dp40);
        course_downloadmanager_function.setLayoutParams(ll);
        ll = (LinearLayout.LayoutParams) course_downloadmanager_function1.getLayoutParams();
        ll.height = 0;
        course_downloadmanager_function1.setLayoutParams(ll);
        //编辑
        course_downloadmanager_layout_edit.setText("编辑");
        course_downloadmanager_layout_edit.setOnClickListener(v->{
            if (course_downloadmanager_layout_edit.getText().toString().equals("编辑")) { //跳转到编辑界面
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) course_downloadmanager_function.getLayoutParams();
                llp.height = 0;
                course_downloadmanager_function.setLayoutParams(llp);
                llp = (LinearLayout.LayoutParams) course_downloadmanager_function1.getLayoutParams();
                llp.height = mDownloadManagerView.getResources().getDimensionPixelSize(R.dimen.dp40);
                course_downloadmanager_function1.setLayoutParams(llp);
                course_downloadmanager_layout_edit.setText("完成");
                int num = course_downloadmanager_layout_content.getChildCount();
                for (int i = 0; i < num; i ++) {
                    View view = course_downloadmanager_layout_content.getChildAt(i);
                    LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                    int childCount = course_downloadmanager_child_content.getChildCount();
                    for (int j = 0; j < childCount; j ++) {
                        View childView = course_downloadmanager_child_content.getChildAt(j);
                        ImageView course_downloadmanager_child1_select = childView.findViewById(R.id.course_downloadmanager_child1_select);
                        LinearLayout.LayoutParams LL = (LinearLayout.LayoutParams) course_downloadmanager_child1_select.getLayoutParams();
                        LL.width = childView.getResources().getDimensionPixelSize(R.dimen.dp20);
                        LL.leftMargin = childView.getResources().getDimensionPixelSize(R.dimen.dp13);
                        course_downloadmanager_child1_select.setLayoutParams(LL);
                        course_downloadmanager_child1_select.setOnClickListener(View -> {
                            int id = getV7ImageResourceId(course_downloadmanager_child1_select);
                            if (id == R.drawable.button_select_gray) {
                                course_downloadmanager_child1_select.setBackgroundResource(R.drawable.button_select_red);
                            } else {
                                course_downloadmanager_child1_select.setBackgroundResource(R.drawable.button_select_gray);
                            }
                        });
                    }
                }
            } else if (course_downloadmanager_layout_edit.getText().toString().equals("完成")){
                CourseDownloadManagerInit();
            }
        });
        //全部选择
        TextView course_downloadmanager_allselect = mDownloadManagerView.findViewById(R.id.course_downloadmanager_allselect);
        course_downloadmanager_allselect.setOnClickListener(v->{
            int num = course_downloadmanager_layout_content.getChildCount();
            for (int i = 0; i < num; i++) {
                View view = course_downloadmanager_layout_content.getChildAt(i);
                LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                int childCount = course_downloadmanager_child_content.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childView = course_downloadmanager_child_content.getChildAt(j);
                    ImageView course_downloadmanager_child1_select = childView.findViewById(R.id.course_downloadmanager_child1_select);
                    int id = getV7ImageResourceId(course_downloadmanager_child1_select);
                    if (id == R.drawable.button_select_gray) {
                        course_downloadmanager_child1_select.setBackgroundResource(R.drawable.button_select_red);
                    }
                }
            }
        });
        //删除
        TextView course_downloadmanager_delete = mDownloadManagerView.findViewById(R.id.course_downloadmanager_delete);
        course_downloadmanager_delete.setOnClickListener(v->{
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
            mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view.findViewById(R.id.tip);
            tip.setText("删除所选内容");
            TextView dialog_content = view.findViewById(R.id.dialog_content);
            dialog_content.setText("确定删除所选内容吗？");
            TextView button_cancel = view.findViewById(R.id.button_cancel);
            button_cancel.setText("取消");
            button_cancel.setOnClickListener(View->{
                mMyDialog.cancel();
            });
            TextView button_sure = view.findViewById(R.id.button_sure);
            button_sure.setText("确定");
            button_sure.setOnClickListener(View->{
                int num = course_downloadmanager_layout_content.getChildCount();
                for (int i = 0; i < num; i++) {
                    View childView = course_downloadmanager_layout_content.getChildAt(i);
                    LinearLayout course_downloadmanager_child_content = childView.findViewById(R.id.course_downloadmanager_child_content);
                    int childCount = course_downloadmanager_child_content.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        View childView1 = course_downloadmanager_child_content.getChildAt(j);
                        ImageView course_downloadmanager_child1_select = childView1.findViewById(R.id.course_downloadmanager_child1_select);
                        int id = getV7ImageResourceId(course_downloadmanager_child1_select);
                        if (id == R.drawable.button_select_red) {//将选中的项目缓存全部清除
                            TextView course_downloadmanager_child1_name = childView1.findViewById(R.id.course_downloadmanager_child1_name);
                            if (this.mCourseRecordPlayDownloadInfoMap.get(course_downloadmanager_child1_name.getHint().toString()) != null){
                                this.mCourseRecordPlayDownloadInfoMap.remove(course_downloadmanager_child1_name.getHint().toString());
                            }
                        }
                    }
                }
                mMyDialog.cancel();
                CourseDownloadManagerInit();
            });
        });
        course_main.addView(mDownloadManagerView);
    }
    //获取sdcard可用磁盘大小
    private long getAvailaleSize(){

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 /1024 / 1024;

        //(availableBlocks * blockSize)/1024      KIB 单位

        //(availableBlocks * blockSize)/1024 /1024  MIB单位
    }
    private static int getV7ImageResourceId(ImageView imageView) {
        int imgid = 0;
        Field[] fields=imageView.getClass().getDeclaredFields();
        for(Field f:fields){
            if(f.getName().equals("mBackgroundTintHelper")){
                f.setAccessible(true);
                try {
                    Object obj = f.get(imageView);
                    Field[] fields2=obj.getClass().getDeclaredFields();
                    for(Field f2:fields2){
                        if(f2.getName().equals("mBackgroundResId")){
                            f2.setAccessible(true);
                            imgid = f2.getInt(obj);
                            android.util.Log.d("1111","Image ResourceId:"+imgid);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return imgid;
    }
    private void CourseCatalogLiveGo(){
//1.首先获取容器对象和access_token值
//白板布局容器竖屏模式一般设置4:3
        FrameLayout pptContainer = null; // 白板布局容器
        FrameLayout videoViewContainer = null; // 摄像头视频播放器布局容器
        String access_token = "2d596444ebe62b4b6825bd09a0fc3ac3"; //直播access_token
//2.通过getInstance()方法获取HtSdk对象实例
        HtSdk mHtSdk = HtSdk.getInstance();
//3.通过init()方法传1中的对象值初始化SDK
        mHtSdk.init(pptContainer, videoViewContainer, access_token);
//4.设置进入后台是否暂停（默认是暂停）(可选)
        mHtSdk.setPauseInBackground(true);
//5.调用onResume方法
//SDK在调用HtSdk对象的onResume方法时去加载数据
//如在Activity中调用，可对应Activity的onResume方法
//否则在初始化完成后调用
        mHtSdk.onResume();
    }
}
