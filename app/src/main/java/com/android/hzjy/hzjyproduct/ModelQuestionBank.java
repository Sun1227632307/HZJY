package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.talkfun.utils.HandlerUtil.runOnUiThread;

public class ModelQuestionBank extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext = "xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview;
    private int height = 1344;
    private int width = 720;
    private String mCurrentTab = "ChapterExercises", mQuestionRecordCurrentTab = "ChapterExercises";
    private int mLastTabIndex = 1, mQuestionRecordLastTabIndex = 1;
    private View mModelQuestionBankView = null, mModelQuestionBankDetailsView = null, mModelQuestionBankSettingView = null,
            mModelQuestionBankAnswerPaperView = null, mModelQuestionBankAnswerQuestionCardView = null, mModelQuestionBankHandInView = null, mModelQuestionBankHandInAnalysisView = null, mModelQuestionBankWrongQuestionView = null, mModelQuestionBankMyCollectionQuestionView = null, mModelQuestionBankQuestionTypeView = null, mModelQuestionBankQuestionRecordView = null;
    private String mSingleChoiceState = "disable";  //单选状态
    private String mMultiChoiceState = "disable";  //多选状态
    private String mShortAnswerState = "disable";  //简答状态
    private String mMaterialQuestionState = "disable";  //材料题状态
    private String mAllQuestionState = "disable";  //全部题状态
    private String mNotDoneQuestionState = "disable";  //未做题状态
    private String mWrongQuestionState = "disable";  //错题状态
    private String mQuestionType = "AllQuestion";  //做题分类
    private String mTenQuestionState = "disable";  //10道题状态
    private String mTwentyQuestionState = "disable";  //20道题状态
    private String mHundredQuestionState = "disable";  //100道题状态
    private String mQuestionCount = "TenQuestion";  //做题题量
    private String mIbs_id = "";
    private PopupWindow mPopupWindow, mPointoutPopupWindow;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
    private ModelAnimUtil animUtil, mPointoutAnimUtil;
    private float bgAlpha = 1f, bgPointoutAlpha = 1f;
    private boolean bright = false, bPointoutRight = false;
    private boolean mIsSign = false;  //此题是否被标记
    private String mFontSize = "nomal"; //当前界面的字号大小
    private String mCurrentChapterName = "";//当前选中的章或节的名称
    private int mCurrentIndex = 0;//当前显示的题索引
    private String mCurrentAnswerMode = "test";//当前做题模式
    private boolean mIsCollect = false;  //此题是否被收藏
    private SmartRefreshLayout mSmart_model_questionbank;
    private SmartRefreshLayout mSmart_model_questionbank_sub_detials;
    private static final String TAG = "ModelQuestionBank";
    private SmartRefreshLayout mSmart_model_questionbank_questionrecord;
    private LinearLayout questionbank_main_content;
    private boolean misEmpty;
    private LinearLayout questionbank_sub_details_content;


    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    private Timer mTimer2 = null;
    private TimerTask mTask2 = null;
    private int mTime = 0;

    public static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage) {
        mContext = context;
        mControlMainActivity = content;
        ModelQuestionBank myFragment = new ModelQuestionBank();
        FragmentPage = iFragmentPage;
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage, container, false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        QuestionBankMainShow(mContext);
        //题库的布局刷新控件
        mSmart_model_questionbank = mview.findViewById(R.id.Smart_model_questionbank);
        mSmart_model_questionbank.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmart_model_questionbank.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmart_model_questionbank.finishRefresh();
            }
        });
        //让布局向上移来显示软键盘
        mControlMainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return mview;
    }

    //题库界面的主要显示
    public void QuestionBankMainShow(String context) {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankView == null) {
            mModelQuestionBankView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankView);
        //题库标题和我的题库标题
        TextView questionbank_main_titletext = mModelQuestionBankView.findViewById(R.id.questionbank_main_titletext);
        if (context.contains("我的题库")) {//如果是我的题库，显示的题库只有我购买所有课程所包含的项目和科目
            questionbank_main_titletext.setText("我的题库");
        } else {
            questionbank_main_titletext.setText("题库");
        }
        mIbs_id = "";
        mCurrentTab = "ChapterExercises";   //练习章
        //题库列表
        ScrollView questionbank_main_content_scroll_view = mModelQuestionBankView.findViewById(R.id.questionbank_main_content_scroll_view);
        questionbank_main_content_scroll_view.scrollTo(0, 0);
        //题库界面
        questionbank_main_content = mModelQuestionBankView.findViewById(R.id.questionbank_main_content);
        questionbank_main_content.removeAllViews();
        boolean misEmpty = false;    //判断当前界面是否为空
        //如果没有数据，显示空界面
        LinearLayout questionbank_main_nodata = mModelQuestionBankView.findViewById(R.id.questionbank_main_nodata);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_main_nodata.getLayoutParams();
        rl.height = 0;
        questionbank_main_nodata.setLayoutParams(rl);
        if (misEmpty) {
            questionbank_main_nodata.removeAllViews();
            View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_pointout, null);
            questionbank_main_nodata.addView(view2);
            rl.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            questionbank_main_nodata.setLayoutParams(rl);
        } else {
            //getQuestionBankBeanList();   //首页题库和字体库请求
        }
    }

    //题库列表传值列表
    public void QuestionBankMainMoreShow(String questionBankId, String item_bank_name, String brief_introduction) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankMore();
//        HideAllLayout();
//        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
//        if (mModelQuestionBankView == null) {
//            mModelQuestionBankView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank, null);
//        }
//        fragmentquestionbank_main.addView(mModelQuestionBankView);
        LinearLayout questionbank_main_content = mModelQuestionBankView.findViewById(R.id.questionbank_main_content);
        questionbank_main_content.removeAllViews();
        //题库列表的描述
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1, null);
        TextView modelquestionbank_mainquestionbank_id = view.findViewById(R.id.modelquestionbank_mainquestionbank_id);
        modelquestionbank_mainquestionbank_id.setText(questionBankId);
        //基金法律标题
        TextView modelquestionbank_mainquestionbank_name = view.findViewById(R.id.modelquestionbank_mainquestionbank_name);
        modelquestionbank_mainquestionbank_name.setText(item_bank_name);
        //基金法律message内容
        TextView modelquestionbank_mainquestionbank_describ = view.findViewById(R.id.modelquestionbank_mainquestionbank_describ);
        modelquestionbank_mainquestionbank_describ.setText(brief_introduction);
        //更多
        LinearLayout modelquestionbank_mainquestionbank_more = view.findViewById(R.id.modelquestionbank_mainquestionbank_more);
        modelquestionbank_mainquestionbank_more.setVisibility(View.INVISIBLE);
//        //子题库赋值
//        GridLayout modelquestionbank_mainquestionbank_list = view.findViewById(R.id.modelquestionbank_mainquestionbank_list);
//        //子标题id和name
//        String ibs_name = "我是标题的名字";
//        String ibs_id = "id";
//        View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1_1, null);
//        TextView modelquestionbank_subquestionbank1 = view1.findViewById(R.id.modelquestionbank_subquestionbank1);
//        modelquestionbank_subquestionbank1.setHint("id");
//        //子题库名称
//        modelquestionbank_subquestionbank1.setText("ibs_name");
//        modelquestionbank_mainquestionbank_list.addView(view1);
//        //查询子题库的名称
//// }
//        //判断题库是否有做题权限，如果没有不可点击颜色变灰
//        if (!ibs_name.equals("科目二")) {
//            modelquestionbank_subquestionbank1.setClickable(true);
//            modelquestionbank_subquestionbank1.setOnClickListener(v -> {
//                //问答详情  传值id和name
//                QuestionBankDetailsShow(ibs_id, ibs_name);
//            });
//            modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.collectdefaultcolor));
//        } else {
//            modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.black999999));
//        }
//        questionbank_main_content.addView(view);
    }

    //题库详情------子题库传值界面
    public void QuestionBankDetailsShow(String ibs_id, String ibs_name) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankDetailsView == null) {
            //做题的三种模式
            mModelQuestionBankDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials, null);
            //章节练习
            TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
            //快速做题
            TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
            //模拟真题
            TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);

            ImageView questionbank_sub_details_buttonmore = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_buttonmore);
            questionbank_sub_details_buttonmore.setClickable(true);
            questionbank_sub_details_buttonmore.setOnClickListener(this);
            questionbank_sub_details_tab_chapterexercises.setOnClickListener(this);
            questionbank_sub_details_tab_quicktask.setOnClickListener(this);
            questionbank_sub_details_tab_simulated.setOnClickListener(this);
            mPopupWindow = new PopupWindow(mControlMainActivity);
            animUtil = new ModelAnimUtil();
            //子题库的界面刷新
            mSmart_model_questionbank_sub_detials = mModelQuestionBankDetailsView.findViewById(R.id.Smart_model_questionbank_sub_detials);
            mSmart_model_questionbank_sub_detials.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_questionbank_sub_detials.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    //刷新界面
                    // QuestionBankDetailsShow(ibs_id,ibs_name);
                    mSmart_model_questionbank_sub_detials.finishRefresh();
                }
            });

        }
        fragmentquestionbank_main.addView(mModelQuestionBankDetailsView);
        //子题库名称标题
        TextView questionbank_sub_details_titletext = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_titletext);
        questionbank_sub_details_titletext.setText(ibs_name);
        //默认游标位置在章节练习
        ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
        int x = width / 6 - mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        questionbank_sub_details_cursor1.setX(x);
        //默认选中的为章节练习
        mLastTabIndex = 1;
        mCurrentTab = "ChapterExercises";
        //章节练习
        TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
        //快速做题
        TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
        //模拟真题
        TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
        questionbank_sub_details_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        questionbank_sub_details_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        questionbank_sub_details_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        //判断当前的图库ID为null
        mIbs_id = ibs_id;
        //如果没有此子题库的做题记录，请将底部功能按钮层隐藏（继续做题）
        LinearLayout questionbank_sub_details_bottomfunction = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_bottomfunction);
        questionbank_sub_details_bottomfunction.setVisibility(View.INVISIBLE);
        //题库详情-章节练习
        QuestionBankDetailsChapterShow();
    }

    //题库详情-章节练习赋值
    private void QuestionBankDetailsChapterShow() {
        //子题库标题
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("自由选择章节知识点各个突破");
        //子题库界面
        LinearLayout questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
         //题库章节考点网络请求赋值
        getMyQuestionBankChapterTest();
    }

    //题库详情-快速做题
    private void QuestionBankDetailsQuickTaskShow() {
        //小标题
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("随机抽取一定量的试题 碎片化学习更方便");
        LinearLayout questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
        //点击开始
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_quicktask, null);
        ImageView questionbank_sub_details_quicktask_start = view.findViewById(R.id.questionbank_sub_details_quicktask_start);
        questionbank_sub_details_quicktask_start.setClickable(true);
        questionbank_sub_details_quicktask_start.setOnClickListener(v -> {
            //点击开始答题
            Toast.makeText(mControlMainActivity, "点击开始答题", Toast.LENGTH_SHORT).show();
            //网络请求开始做题
        });
        questionbank_sub_details_content.addView(view);
    }

    //题库详情-快速做题
    private void QuestionBankDetailsSimulatedShow() {
        //题库标题
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("模拟真实考试场景知识点综合评测");
        questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
           misEmpty = true;
        if (misEmpty) {  //没数据展示空提示界面
            View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_pointout, null);
            questionbank_sub_details_content.addView(view3);
        }else {
            //模拟真题界面
              initBankDetailsSimulatedShow();
        }
    }

    private void initBankDetailsSimulatedShow() {
          misEmpty = false;
        //模拟真题界面
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_simulate, null);
        //模拟真题界面-name   title标题
        TextView questionbank_simulated_name = view.findViewById(R.id.questionbank_simulated_name);
        questionbank_simulated_name.setText("test_paper_name");
        questionbank_simulated_name.setHint("test_paper_id");
        //时间长度
        TextView questionbank_simulated_time = view.findViewById(R.id.questionbank_simulated_time);
        questionbank_simulated_time.setText("answer_time" + "分钟");
        //总分
        TextView questionbank_simulated_score = view.findViewById(R.id.questionbank_simulated_score);
        questionbank_simulated_score.setText("total_score" + "分");
        //点击去做题
        TextView questionbank_simulated_go = view.findViewById(R.id.questionbank_simulated_go);
        questionbank_simulated_go.setClickable(true);
        questionbank_simulated_go.setOnClickListener(v -> {
            //开始真题做题
            Toast.makeText(mControlMainActivity, "真题实做", Toast.LENGTH_SHORT).show();
        });
        //点击去做题
        ImageView questionbank_simulated_goimage = view.findViewById(R.id.questionbank_simulated_goimage);
        questionbank_simulated_goimage.setClickable(true);
        questionbank_simulated_goimage.setOnClickListener(v -> {
            //开始真题做题
            Toast.makeText(mControlMainActivity, "真题实做", Toast.LENGTH_SHORT).show();
        });
        questionbank_sub_details_content.addView(view);
    }

    //显示章 展开下面的节或考点
    private void QuestionBankDetailsChapterExerisesShow(View view, String ibs_id, String jie_test_point_id) {
        boolean isFind = false;
        LinearLayout questionbank_sub_details_chapterexercises_content = view.findViewById(R.id.questionbank_sub_details_chapterexercises_content);
        questionbank_sub_details_chapterexercises_content.removeAllViews();
        View questionbank_sub_details_chapterexercises1_line1 = null;
          isFind=true;
//        while (cursor1.moveToNext()) {
//            isFind = true;
//            int chapter_test_point_idIndex1 = cursor1.getColumnIndex("chapter_test_point_id");
//            int nameIndex1 = cursor1.getColumnIndex("name");
//            String chapter_test_point_id1 = cursor1.getString(chapter_test_point_idIndex1);
//            mCurrentChapterName = cursor1.getString(nameIndex1);
        //节或者考点的网络请求    节的id或者name
        View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_chapterexercises1, null);
        //title
        TextView questionbank_sub_details_chapterexercises1_name = view1.findViewById(R.id.questionbank_sub_details_chapterexercises1_name);
        questionbank_sub_details_chapterexercises1_name.setHint(jie_test_point_id);
        questionbank_sub_details_chapterexercises1_name.setText(mCurrentChapterName);

        questionbank_sub_details_chapterexercises1_line1 = view1.findViewById(R.id.questionbank_sub_details_chapterexercises1_line1);
        questionbank_sub_details_chapterexercises1_name.setClickable(true);
        //点击节或考点名称，进行节或考点抽题
        questionbank_sub_details_chapterexercises1_name.setOnClickListener(v -> {
            //初始化做题设置界面并展示
            QuestionBankQuestionSettingShow("exercises", jie_test_point_id);
        });
        questionbank_sub_details_chapterexercises_content.addView(view1);
        //最后一条线隐藏
        if (questionbank_sub_details_chapterexercises1_line1 != null) {
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_sub_details_chapterexercises1_line1.getLayoutParams();
            rl.topMargin = 0;
            rl.height = 0;
            rl.bottomMargin = 0;
            questionbank_sub_details_chapterexercises1_line1.setLayoutParams(rl);
            questionbank_sub_details_chapterexercises1_line1.setVisibility(View.INVISIBLE);
        }
        ImageView questionbank_sub_details_chapterexercises_arrow_right = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_right);
        ImageView questionbank_sub_details_chapterexercises_arrow_down = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_down);
        ModelExpandView questionbank_sub_details_chapterexercises_expandView = view.findViewById(R.id.questionbank_sub_details_chapterexercises_expandView);

        if (!isFind) {
            Toast.makeText(mControlMainActivity, "本章下面没有节或考点", Toast.LENGTH_SHORT);
            //收缩隐藏布局
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_sub_details_chapterexercises_expandView.getLayoutParams();
            rl.height = 0;
            questionbank_sub_details_chapterexercises_expandView.setLayoutParams(rl);
            questionbank_sub_details_chapterexercises_expandView.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
            ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp6);
            questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll1);
            ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_down.getLayoutParams();
            ll1.width = 0;
            questionbank_sub_details_chapterexercises_arrow_down.setLayoutParams(ll1);
            return;
        }
        questionbank_sub_details_chapterexercises_expandView.expand();
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_sub_details_chapterexercises_expandView.getLayoutParams();
        rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        questionbank_sub_details_chapterexercises_expandView.setLayoutParams(rl);
        questionbank_sub_details_chapterexercises_expandView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
        ll1.width = 0;
        questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll1);
        ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_down.getLayoutParams();
        ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp10);
        questionbank_sub_details_chapterexercises_arrow_down.setLayoutParams(ll1);
    }

    //初始化做题设置界面并展示
    public void QuestionBankQuestionSettingShow(String type, String id) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankSetting();
        HideAllLayout();
        //全部题
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankSettingView == null) {
            mModelQuestionBankSettingView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questionsetting, null);
            //练习模式
            LinearLayout questionbank_questionsetting_questionmode_test = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questionmode_test);
            //考题模式
            LinearLayout questionbank_questionsetting_questionmode_exam = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questionmode_exam);
            questionbank_questionsetting_questionmode_test.setClickable(true);
            questionbank_questionsetting_questionmode_test.setOnClickListener(v -> {
                  //显示当前的题索引值
                mCurrentIndex = 0;
                //练习模式的详情
                QuestionBankDetailsQuestionModeTestShow();
            });
            questionbank_questionsetting_questionmode_exam.setOnClickListener(v -> {
                mCurrentIndex = 0;
                //考试模式的详情
                QuestionBankDetailsQuestionModeExamShow();
            });
        }
        fragmentquestionbank_main.addView(mModelQuestionBankSettingView);
        String keyString = "";
        if ("chapter".equals(type)) {
            keyString = "chapter_id='" + id + "'";
        } else {
            keyString = "section_id='" + id + "'";
        }
        //题库单选题
        TextView questionbank_questionsetting_singlechoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_singlechoice);
        //题库多选题
        TextView questionbank_questionsetting_multichoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_multichoice);
        //题库简答题
        TextView questionbank_questionsetting_shortanswerchoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_shortanswerchoice);
        //题库材料题
        TextView questionbank_questionsetting_materialquestion = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_materialquestion);
        //查询此章节下的题-单选题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        int count = 0;
        if (count == 0) { //没有单选题
            mSingleChoiceState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mSingleChoiceState = "select";
            questionbank_questionsetting_singlechoice.setText("单选题(" + count + ")");
        }
        //查询此章节下的题-多选题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
//        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
//                "select count(*) as count from test_questions_edu where question_type='2' and state='3' and tf_delete='2'and " + keyString, null);
//        count = 0;
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("count");
//            count = cursor.getInt(countIndex);
//        }
//        cursor.close();
        if (count == 0) { //没有多选题
            mMultiChoiceState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mMultiChoiceState = "select";
            questionbank_questionsetting_multichoice.setText("多选题(" + count + ")");
        }
        //查询此章节下的题-简答题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        if (count == 0) { //没有简答题
            mShortAnswerState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mShortAnswerState = "select";
            questionbank_questionsetting_shortanswerchoice.setText("简答题(" + count + ")");
        }

        //查询此章节下的题-材料题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
//        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
//                "select count(*) as count from test_questions_edu where question_type='7' and state='3' and tf_delete='2'and " + keyString, null);
//        count = 0;
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("count");
//            count = cursor.getInt(countIndex);
//        }
//        cursor.close();
        if (count == 0) { //没有材料题
            mMaterialQuestionState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_materialquestion.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_materialquestion.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mMaterialQuestionState = "select";
            questionbank_questionsetting_materialquestion.setText("材料题(" + count + ")");
        }
        //设置点击事件
        questionbank_questionsetting_singlechoice.setClickable(true);
        questionbank_questionsetting_multichoice.setClickable(true);
        questionbank_questionsetting_shortanswerchoice.setClickable(true);
        questionbank_questionsetting_materialquestion.setClickable(true);
        questionbank_questionsetting_singlechoice.setOnClickListener(v -> {
            if (mSingleChoiceState.equals("select")) {
                //全选改为未选
                questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mSingleChoiceState = "unselect";
            } else if (mSingleChoiceState.equals("unselect")) {
                //未选改为全选
                questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mSingleChoiceState = "select";
            } else if (mSingleChoiceState.equals("disable")) {
                //不可选
                mSingleChoiceState = "disable";
            }
        });
        questionbank_questionsetting_multichoice.setOnClickListener(v -> {
            if (mMultiChoiceState.equals("select")) {
                //全选改为未选
                questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mMultiChoiceState = "unselect";
            } else if (mMultiChoiceState.equals("unselect")) {
                //未选改为全选
                questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mMultiChoiceState = "select";
            } else if (mMultiChoiceState.equals("disable")) {
                //不可选
                mMultiChoiceState = "disable";
            }
        });
        questionbank_questionsetting_shortanswerchoice.setOnClickListener(v -> {
            if (mShortAnswerState.equals("select")) {
                //全选改为未选
                questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mShortAnswerState = "unselect";
            } else if (mShortAnswerState.equals("unselect")) {
                //未选改为全选
                questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mShortAnswerState = "select";
            } else if (mShortAnswerState.equals("disable")) {
                //不可选
                mShortAnswerState = "disable";
            }
        });
        questionbank_questionsetting_materialquestion.setOnClickListener(v -> {
            if (mMaterialQuestionState.equals("select")) {
                //全选改为未选
                questionbank_questionsetting_materialquestion.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_materialquestion.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mMaterialQuestionState = "unselect";
            } else if (mMaterialQuestionState.equals("unselect")) {
                //未选改为全选
                questionbank_questionsetting_materialquestion.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_materialquestion.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mMaterialQuestionState = "select";
            } else if (mMaterialQuestionState.equals("disable")) {
                //不可选
                mMaterialQuestionState = "disable";
            }
        });
        //判断分类、如果此章/节/考点 有全部题/未做题/错题,将其状态置为可选
        //全部
        TextView questionbank_questionsetting_all = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_all);
        //未做
        TextView questionbank_questionsetting_notdone = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_notdone);
        //错题
        TextView questionbank_questionsetting_wrong = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_wrong);
        //查询此章节下的全部题
//        count = 0;
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("count");
//            count = cursor.getInt(countIndex);
//        }
//        cursor.close();
        if (count == 0) { //没有题
            mAllQuestionState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
            questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
            questionbank_questionsetting_all.setText("全部题(" + count + ")");
            mAllQuestionState = "enable";
        }

        TextView questionbank_questionsetting_count = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_count);
        questionbank_questionsetting_count.setText("共" + count + "道题");
        //设置点击事件
        questionbank_questionsetting_all.setClickable(true);
        questionbank_questionsetting_notdone.setClickable(true);
        questionbank_questionsetting_wrong.setClickable(true);
        mNotDoneQuestionState = "enable";
        questionbank_questionsetting_all.setOnClickListener(v -> {
            if (mAllQuestionState.equals("enable")) {
                mQuestionType = "AllQuestion";
                questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mNotDoneQuestionState.equals("disable")) {
                    questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mWrongQuestionState.equals("disable")) {
                    questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mAllQuestionState.equals("disable")) {
                //不可选

            }
        });
        questionbank_questionsetting_notdone.setOnClickListener(v -> {
            if (mNotDoneQuestionState.equals("enable")) {
                mQuestionType = "NotDoneQuestion";
                questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mAllQuestionState.equals("disable")) {
                    questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mWrongQuestionState.equals("disable")) {
                    questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mNotDoneQuestionState.equals("disable")) {
                //不可选
            }
        });
        questionbank_questionsetting_wrong.setOnClickListener(v -> {
            if (mWrongQuestionState.equals("enable")) {
                mQuestionType = "WrongQuestion";
                questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mAllQuestionState.equals("disable")) {
                    questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mNotDoneQuestionState.equals("disable")) {
                    questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mWrongQuestionState.equals("disable")) {
                //不可选
            }
        });
        //判断题量、如果此章/节/考点 有10道题/20道题/100道题,将其状态置为可选
        TextView questionbank_questionsetting_questioncount1 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount1);
        TextView questionbank_questionsetting_questioncount2 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount2);
        TextView questionbank_questionsetting_questioncount3 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount3);
        if (count > 0) {
            //默认将其可选的改为全选
            questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
            questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
            mTenQuestionState = "enable";
        }
        if (count > 10) {
            questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
            questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
            mTwentyQuestionState = "enable";
        }
        if (count > 20) {
            questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
            questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
            mHundredQuestionState = "enable";
        }
        //设置点击事件
        questionbank_questionsetting_questioncount1.setClickable(true);
        questionbank_questionsetting_questioncount2.setClickable(true);
        questionbank_questionsetting_wrong.setClickable(true);

        questionbank_questionsetting_questioncount1.setOnClickListener(v -> {
            if (mTenQuestionState.equals("enable")) {
                mQuestionCount = "TenQuestion";
                questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTwentyQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mHundredQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mTenQuestionState.equals("disable")) {
                //不可选
            }
        });
        //题量20
        questionbank_questionsetting_questioncount2.setOnClickListener(v -> {
            if (mTwentyQuestionState.equals("enable")) {
                mQuestionCount = "TwentyQuestion";
                questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTenQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mHundredQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mTwentyQuestionState.equals("disable")) {
                //不可选
            }
        });
        questionbank_questionsetting_questioncount3.setOnClickListener(v -> {
            if (mHundredQuestionState.equals("enable")) {
                mQuestionCount = "HundredQuestion";
                questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTenQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mTwentyQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mHundredQuestionState.equals("disable")) {
                //不可选
            }
        });
    }

    //交卷界面展示
    public void QuestionBankDetailsHandInPaperShow(String question_id_group) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        //交题科目5
        mModelQuestionBankHandInView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper, null);
        fragmentquestionbank_main.addView(mModelQuestionBankHandInView);
        ControllerRoundProgressBar coursedetails_handinpaper_accuracyrateprogress = mModelQuestionBankHandInView.findViewById(R.id.coursedetails_handinpaper_accuracyrateprogress);
        //进度
        coursedetails_handinpaper_accuracyrateprogress.setProgress(30);
        coursedetails_handinpaper_accuracyrateprogress.setMax(100);
        TextView questionbank_handinpaper__main_titletext = mModelQuestionBankHandInView.findViewById(R.id.questionbank_handinpaper__main_titletext);
        questionbank_handinpaper__main_titletext.setText(mCurrentChapterName + "解析的title name");
        LinearLayout coursedetails_handinpaper_details = mModelQuestionBankHandInView.findViewById(R.id.coursedetails_handinpaper_details);
        //查找题型
        coursedetails_handinpaper_details.removeAllViews();
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
        }
        //获取题型的种类根据题型的种类进行判断
        View singleView = null;
        View mutilView = null;
        View shortAnswerView = null;
        View materialView = null;
        int count = 0;
        //问题的类型
//        for (int i = 0; i < question_id_groupS.length ; i ++) {
//        while (cursor.moveToNext()) {
//            int question_typeIndex = cursor.getColumnIndex("question_type");
////            int tf_collectionIndex = cursor.getColumnIndex("tf_collection");
//            String question_type = cursor.getString(question_typeIndex);
////            String tf_collection = cursor.getString(tf_collectionIndex);
        //  question_type    问题的类型
        if ("question_type".equals("1")) {
            if (singleView == null) {
                //单选题界面标题
                singleView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper1, null);
                coursedetails_handinpaper_details.addView(singleView);
            }
            GridLayout coursedetails_handinpaper1_questionnumber = singleView.findViewById(R.id.coursedetails_handinpaper1_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper2, null);
            coursedetails_handinpaper1_questionnumber.addView(view);
            TextView questionbank_handin2_select = view.findViewById(R.id.questionbank_handin2_select);
            questionbank_handin2_select.setText("" + (count + 1));
//                if (tf_collection != null){
//                    if (tf_collection.equals("1")){//收藏此题
//                        ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
//                        questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
//                    questionbank_handin2_select.setTextColor(view.getResources().getColor(R.color.white));
//                    questionbank_handin2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_red));
//                }
            int finalCount = count;
            questionbank_handin2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeHandInShow();
            });
        } else if ("question_type".equals("2")) {
            if (mutilView == null) {
                mutilView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper1, null);
                coursedetails_handinpaper_details.addView(mutilView);
                //单选框
                TextView coursedetails_handinpaper1_questiontype = mutilView.findViewById(R.id.coursedetails_handinpaper1_questiontype);
                coursedetails_handinpaper1_questiontype.setText("多选题");
            }
            GridLayout coursedetails_handinpaper1_questionnumber = mutilView.findViewById(R.id.coursedetails_handinpaper1_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper2, null);
            coursedetails_handinpaper1_questionnumber.addView(view);
            TextView questionbank_handin2_select = view.findViewById(R.id.questionbank_handin2_select);
            questionbank_handin2_select.setText("" + (count + 1));
//                if (tf_marked != null){
//                    if (tf_marked.equals("1")){//标记此题
//                        ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
//                        questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
//                    questionbank_handin2_select.setTextColor(view.getResources().getColor(R.color.white));
//                    questionbank_handin2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_red));
//                }
            int finalCount = count;
            questionbank_handin2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeHandInShow();
            });
        } else if ("question_type".equals("4")) {
            if (shortAnswerView == null) {
                shortAnswerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper1, null);
                coursedetails_handinpaper_details.addView(shortAnswerView);
                TextView coursedetails_handinpaper1_questiontype = shortAnswerView.findViewById(R.id.coursedetails_handinpaper1_questiontype);
                coursedetails_handinpaper1_questiontype.setText("简答题");
            }
            GridLayout coursedetails_handinpaper1_questionnumber = shortAnswerView.findViewById(R.id.coursedetails_handinpaper1_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper2, null);
            coursedetails_handinpaper1_questionnumber.addView(view);
            TextView questionbank_handin2_select = view.findViewById(R.id.questionbank_handin2_select);
            questionbank_handin2_select.setText("" + (count + 1));
//                if (tf_marked != null){
//                    if (tf_marked.equals("1")){//标记此题
//                        ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
//                        questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
//                    questionbank_handin2_select.setTextColor(view.getResources().getColor(R.color.white));
//                    questionbank_handin2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
//                }
            int finalCount = count;
            questionbank_handin2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeHandInShow();
            });
        } else if ("question_type".equals("7")) {
            if (materialView == null) {
                materialView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper1, null);
                coursedetails_handinpaper_details.addView(materialView);
                TextView coursedetails_handinpaper1_questiontype = materialView.findViewById(R.id.coursedetails_handinpaper1_questiontype);
                coursedetails_handinpaper1_questiontype.setText("材料题");
            }
            GridLayout coursedetails_handinpaper1_questionnumber = materialView.findViewById(R.id.coursedetails_handinpaper1_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper2, null);
            coursedetails_handinpaper1_questionnumber.addView(view);
            TextView questionbank_handin2_select = view.findViewById(R.id.questionbank_handin2_select);
            questionbank_handin2_select.setText("" + (count + 1));
//                if (tf_marked != null){
//                    if (tf_marked.equals("1")){//标记此题
//                        ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
//                        questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
//                    questionbank_handin2_select.setTextColor(view.getResources().getColor(R.color.white));
//                    questionbank_handin2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
//                }
            int finalCount = count;
            questionbank_handin2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeHandInShow();
            });
        }
        count++;
        //}
        // cursor.close();
    }

    public void QuestionBankDetailsQuestionModeShow() {
        if (mCurrentAnswerMode.equals("test")) {
            QuestionBankDetailsQuestionModeTestShow();
        } else if (mCurrentAnswerMode.equals("exam")) {
            QuestionBankDetailsQuestionModeExamShow();
        } else if (mCurrentAnswerMode.equals("handin")) {
            QuestionBankDetailsQuestionModeHandInShow();
        } else if (mCurrentAnswerMode.equals("wrong")) {
            QuestionBankDetailsQuestionModeWrongQuestionShow();
        } else if (mCurrentAnswerMode.equals("collection")) {
            QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
        } else if (mCurrentAnswerMode.equals("requestionrecord")) {
            QuestionBankDetailsQuestionModeQuestionRecordShow();
        }
    }

    //答题-练习模式界面展示
    private void QuestionBankDetailsQuestionModeTestShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "test";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankAnswerPaperView == null) {
             //练习模式分析内容
            mModelQuestionBankAnswerPaperView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankAnswerPaperView);
        //此题所属章节名称（分析内容标题）
        TextView questionbank_answerpaper_questiontitle = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questiontitle);
        questionbank_answerpaper_questiontitle.setText(mCurrentChapterName + "当前选中节的名称");
        //倒计时时间
        TextView questionbank_answerpaper_countdowntimetext = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_countdowntimetext);
        //点击标记
        ImageView questionbank_answerpaper_sign = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_sign);
        questionbank_answerpaper_sign.setOnClickListener(v -> {
            if (mIsSign) {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign));
                mIsSign = false;
//                getMyQuestionBankflag();
                //网络请求取消标记
                Toast.makeText(mControlMainActivity, "取消标记", Toast.LENGTH_SHORT).show();
            } else {
                //网络请求进行标记
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign_blue));
                Toast.makeText(mControlMainActivity, "标记此题", Toast.LENGTH_SHORT).show();
                mIsSign = true;
//                getMyQuestionBankflag();
            }
        });
        //查询试卷表中生成的临时题
//        String question_id_group = "";
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("question_id_group");
//            question_id_group = cursor.getString(countIndex);
//            break;
//        }
//        cursor.close();
//        //添加题,//默认显示第一题
//        QuestionViewAdd(question_id_group);
        //点击交卷
        LinearLayout questionbank_answerpaper_commit = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_commit);
//        String finalQuestion_id_group1 = question_id_group;
        questionbank_answerpaper_commit.setOnClickListener(v -> {
            //判断啊当前是否删除
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            tip.setText("交卷");
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("确认交卷吗？");
            TextView button_cancel = view1.findViewById(R.id.button_cancel);
            button_cancel.setText("再检查一下");
            button_cancel.setOnClickListener(View -> {
                mMyDialog.cancel();
            });
            //点击   网络请求
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("交卷");
            button_sure.setOnClickListener(View -> {
                //暂停计时器
                if (mTimer2 != null) {
                    mTimer2.cancel();
                }
                if (mTask2 != null) {
                    mTask2.cancel();
                }
                //显示交卷界面
                //QuestionBankDetailsHandInPaperShow(finalQuestion_id_group1);
                //文件暂停和继续做题
                mMyDialog.cancel();
            });
        });
        //点击暂停   网络请求暂停
        ImageView questionbank_answerpaper_pause = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_pause);
        questionbank_answerpaper_pause.setOnClickListener(v -> {
            //判断是否删除选择内容
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(false);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            tip.setText("暂停");
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("哎呦，休息时间到啦");
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("继续做题");

            button_sure.setOnClickListener(View -> {    //点击继续做题
                mMyDialog.cancel();
                //重新打开计时器
                mTimer2 = new Timer();
                mTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        mTime = mTime + 1;
                        runOnUiThread(() ->
                                questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
                    }
                };
                mTimer2.schedule(mTask2, 0, 1000);
            });
            //暂停计时器
            if (mTimer2 != null) {
                mTimer2.cancel();
            }
            if (mTask2 != null) {
                mTask2.cancel();
            }
        });
        //        上一题
        // String finalQuestion_id_group = question_id_group;   截取字符串
        //  String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        LinearLayout button_questionbank_beforquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_beforquestion);
        button_questionbank_beforquestion.setOnClickListener(v -> {
            TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
            if (questionbank_answerpaper_questioncount.getText().toString().equals("1") || questionbank_answerpaper_questioncount.getText().toString().equals("0")) {
                Toast.makeText(mControlMainActivity, "前面没有题啦", Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
                //  QuestionViewAdd(finalQuestion_id_group);
                //打开解析按钮
                TextView coursedetails_answerpaper_analysisbutton = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysisbutton);
                LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
                lLayoutParams.height = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_37);
                lLayoutParams.topMargin = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_70);
                coursedetails_answerpaper_analysisbutton.setLayoutParams(lLayoutParams);
                LinearLayout coursedetails_answerpaper_analysis = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysis);
                coursedetails_answerpaper_analysis.removeAllViews();
            }
        });
        //         下一题
        LinearLayout button_questionbank_nextquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_nextquestion);
        button_questionbank_nextquestion.setOnClickListener(V -> {
            if ("question_id_groupS" != null) {
                TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
                if (questionbank_answerpaper_questioncount.getText().toString().equals("" + "question_id_groupS.length")) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
                    QuestionViewAdd("finalQuestion_id_group");
                    //打开解析按钮
                    TextView coursedetails_answerpaper_analysisbutton = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysisbutton);
                    LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
                    lLayoutParams.height = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_37);
                    lLayoutParams.topMargin = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_70);
                    coursedetails_answerpaper_analysisbutton.setLayoutParams(lLayoutParams);
                    LinearLayout coursedetails_answerpaper_analysis = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysis);
                    coursedetails_answerpaper_analysis.removeAllViews();
                }
            }
        });
        //答题卡
        LinearLayout button_questionbank_answerquestioncard = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_answerquestioncard);
        //添加答题卡
        button_questionbank_answerquestioncard.setOnClickListener(v ->
                AnswerQuestionCardViewAdd("finalQuestion_id_group"));
        //点击字号
        ImageView questionbank_answerpaper_fontsize = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_fontsize);
        questionbank_answerpaper_fontsize.setOnClickListener(v -> {
            ShowPopFontSize("finalQuestion_id_group", questionbank_answerpaper_fontsize);
        });
        //计时器
        if (mTimer2 != null) {
            mTimer2.cancel();
            mTimer2 = null;
        }
        if (mTask2 != null) {
            mTask2.cancel();
            mTask2 = null;
        }
        mTime = 0;
        mTimer2 = new Timer();
        mTask2 = new TimerTask() {
            @Override
            public void run() {
                mTime = mTime + 1;   //显示倒计时的时间
                runOnUiThread(() -> questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
            }
        };
        mTimer2.schedule(mTask2, 0, 1000);
        //查看解析
        TextView coursedetails_answerpaper_analysisbutton = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysisbutton);
        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
        lLayoutParams.height = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_37);
        lLayoutParams.topMargin = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_70);
        coursedetails_answerpaper_analysisbutton.setLayoutParams(lLayoutParams);
        //解析下面的内容
        LinearLayout coursedetails_answerpaper_analysis = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysis);
        coursedetails_answerpaper_analysis.removeAllViews();
        coursedetails_answerpaper_analysisbutton.setOnClickListener(v -> {
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
            ll.height = 0;
            ll.topMargin = 0;
            coursedetails_answerpaper_analysisbutton.setLayoutParams(ll);
            //项目单选判断
//            while (cursor1.moveToNext()) {
//                int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
//                int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//                int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
//                String optionanswer = cursor1.getString(optionanswerIndex);
//                String question_type = cursor1.getString(question_typeIndex);
//                String question_analysis = cursor1.getString(question_analysisIndex);
//                if (optionanswer == null || question_type == null || question_analysis == null) {
//                    break;
//                }
            //答题卡网络请求  optionanswer  question_type question_analysis

            if ("question_type".equals("1") || "question_type".equals("2")) {//单选题或多选题
                //个人答案
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                coursedetails_answerpaper_analysis.addView(view);
                //修改内容为正确答案
//                    String[] optionanswerS = optionanswer.split(";");
//                    if (optionanswerS == null) {
//                        break;
//                    }
//                    String currentAnswer = "";
//                    for (int i = 0; i < optionanswerS.length; i++) {
//                        String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
//                        if (optionanswerS1.length != 3) {
//                            break;
//                        }
//                        if (optionanswerS1[1].equals("是")) {
//                            currentAnswer = currentAnswer + optionanswerS1[0] + " ";
//                        }
//                    }
//                    if (currentAnswer.equals("")) {
//                        break;
//                    }
                //正确答案
                TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                questionbank_analysis1_rightAnswer.setText("currentAnswer");
//             //修改内容为此题的解析
                TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                questionbank_analysis1_content.setText("question_analysis");
                //个人答案
                TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                questionbank_analysis1_yourAnswer.setText("aaa");
                //字体大小的设置
                if (mFontSize.equals("nomal")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            } else if ("question_type".equals("4")) {//简答题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                coursedetails_answerpaper_analysis.addView(view);
                //修改内容为正确答案
                TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                questionbank_analysis2_rightAnswer.setText("我是正确答案解析");
                //修改内容为此题的解析
                TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                questionbank_analysis2_content.setText("我是此题解析");
                //修改内容为您的答案
                TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("自己的答案解析");
                questionbank_analysis2_yourAnswer.setText("自己的答案");


                if (mFontSize.equals("nomal")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            }
            //}
        });

    }

    //答题-考试模式界面展示
    private void QuestionBankDetailsQuestionModeExamShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "exam";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankAnswerPaperView == null) {
            mModelQuestionBankAnswerPaperView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankAnswerPaperView);
        //此题所属章节名称
        TextView questionbank_answerpaper_questiontitle = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questiontitle);
        questionbank_answerpaper_questiontitle.setText(mCurrentChapterName + "当前选中的章或节的名称");
        //倒计时
        TextView questionbank_answerpaper_countdowntimetext = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_countdowntimetext);

        //点击标记
        ImageView questionbank_answerpaper_sign = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_sign);
        questionbank_answerpaper_sign.setOnClickListener(v -> {
            if (mIsSign) {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign));
                mIsSign = false;
//                getMyQuestionBankflag();
                //取消标记
                Toast.makeText(mControlMainActivity, "取消标记", Toast.LENGTH_SHORT).show();
            } else {
                //标记
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign_blue));
                Toast.makeText(mControlMainActivity, "标记此题", Toast.LENGTH_SHORT).show();
                mIsSign = true;
//               getMyQuestionBankflag();
            }
        });
        //查询试卷表中生成的临时题
//        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
//                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("question_id_group");
//            question_id_group = cursor.getString(countIndex);
//            break;
//        }
        // cursor.close();
        //添加题,//默认显示第一题
        QuestionViewAdd(question_id_group);
        //点击交卷
        LinearLayout questionbank_answerpaper_commit = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_commit);
        String finalQuestion_id_group1 = question_id_group;
        questionbank_answerpaper_commit.setOnClickListener(v -> {
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            tip.setText("交卷");
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("确认交卷吗？");
            TextView button_cancel = view1.findViewById(R.id.button_cancel);
            button_cancel.setText("再检查一下");
            button_cancel.setOnClickListener(View -> {
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("交卷");
            button_sure.setOnClickListener(View -> {
                //暂停计时器
                if (mTimer2 != null) {
                    mTimer2.cancel();
                }
                if (mTask2 != null) {
                    mTask2.cancel();
                }
                //显示交卷界面
                QuestionBankDetailsHandInPaperShow(finalQuestion_id_group1);
                mMyDialog.cancel();
            });
        });
        //点击暂停
        ImageView questionbank_answerpaper_pause = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_pause);
        questionbank_answerpaper_pause.setOnClickListener(v -> {
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(false);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            tip.setText("暂停");
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("哎呦，休息时间到啦");
            TextView button_sure = view1.findViewById(R.id.button_sure);
            //点击继续做题
            button_sure.setText("继续做题");
            button_sure.setOnClickListener(View -> {
                ///getMyQuestionBankGoon();   //判断暂停还是继续做题
                mMyDialog.cancel();
                //重新打开计时器
                mTimer2 = new Timer();
                mTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        mTime = mTime + 1;
                        //定时器  判断当前的题库是否继做题
                        runOnUiThread(() ->
                                //显示倒计时的时间
                                questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
                    }
                };
                mTimer2.schedule(mTask2, 0, 1000);
            });
            //暂停计时器
            if (mTimer2 != null) {
                mTimer2.cancel();
            }
            if (mTask2 != null) {
                mTask2.cancel();
            }
        });


        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        LinearLayout button_questionbank_beforquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_beforquestion);
        button_questionbank_beforquestion.setOnClickListener(v -> {
            TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
            if (questionbank_answerpaper_questioncount.getText().toString().equals("1") || questionbank_answerpaper_questioncount.getText().toString().equals("0")) {
                Toast.makeText(mControlMainActivity, "前面没有题啦", Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
                QuestionViewAdd(finalQuestion_id_group);
            }
        });


        //下一题
        LinearLayout button_questionbank_nextquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_nextquestion);
        button_questionbank_nextquestion.setOnClickListener(V -> {
            if (question_id_groupS != null) {
                TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
                if (questionbank_answerpaper_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
                    QuestionViewAdd(finalQuestion_id_group);
                }
            }
        });


        //答题卡
        LinearLayout button_questionbank_answerquestioncard = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_answerquestioncard);
        button_questionbank_answerquestioncard.setOnClickListener(v ->
                AnswerQuestionCardViewAdd(finalQuestion_id_group));

        //点击字号
        ImageView questionbank_answerpaper_fontsize = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_fontsize);
        questionbank_answerpaper_fontsize.setOnClickListener(v -> {
            ShowPopFontSize(finalQuestion_id_group, questionbank_answerpaper_fontsize);
        });
        //计时器
        if (mTimer2 != null) {
            mTimer2.cancel();
            mTimer2 = null;
        }
        if (mTask2 != null) {
            mTask2.cancel();
            mTask2 = null;
        }
        mTime = 0;
        mTimer2 = new Timer();
        mTask2 = new TimerTask() {
            @Override
            public void run() {
                mTime = mTime + 1;
                runOnUiThread(() -> questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
            }
        };
        mTimer2.schedule(mTask2, 0, 1000);

        TextView coursedetails_answerpaper_analysisbutton = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysisbutton);
        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
        lLayoutParams.height = 0;
        lLayoutParams.topMargin = 0;
        coursedetails_answerpaper_analysisbutton.setLayoutParams(lLayoutParams);
    }

    //答题-交卷解析模式界面展示
    private void QuestionBankDetailsQuestionModeHandInShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "handin";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankHandInAnalysisView == null) {
            mModelQuestionBankHandInAnalysisView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handin_analysis, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankHandInAnalysisView);
        //此题所属章节名称
        TextView questionbank_handin_analysis_questiontitle = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questiontitle);
        questionbank_handin_analysis_questiontitle.setText(mCurrentChapterName + "当前选中的章或节的名称");
        //点击标记
        ImageView questionbank_handin_analysis_collection = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_collection);
        questionbank_handin_analysis_collection.setOnClickListener(v -> {
            if (mIsCollect) {
                questionbank_handin_analysis_collection.setBackground(mModelQuestionBankHandInAnalysisView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
                // getMyQuestionBankCollection();
                //取消收藏网络请求
                Toast.makeText(mControlMainActivity, "取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                questionbank_handin_analysis_collection.setBackground(mModelQuestionBankHandInAnalysisView.getResources().getDrawable(R.drawable.button_collect_enable));
                mIsCollect = true;
                //getMyQuestionBankCollection();
                //收藏列表网络请求
                Toast.makeText(mControlMainActivity, "收藏此题", Toast.LENGTH_SHORT).show();

            }
        });
        //查询试卷表中生成的临时题
    String question_id_group = "";
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("question_id_group");
//            question_id_group = cursor.getString(countIndex);
//            break;

        //添加题
        HandInAnalysisQuestionViewAdd(question_id_group);
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        LinearLayout button_handin_analysis_beforquestion = mModelQuestionBankHandInAnalysisView.findViewById(R.id.button_handin_analysis_beforquestion);
        button_handin_analysis_beforquestion.setOnClickListener(v -> {
            TextView questionbank_handin_analysis_questioncount = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncount);
            if (questionbank_handin_analysis_questioncount.getText().toString().equals("1") || questionbank_handin_analysis_questioncount.getText().toString().equals("0")) {
                Toast.makeText(mControlMainActivity, "前面没有题啦", Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeHandInShow();
            }
        });

        //下一题
        LinearLayout button_handin_analysis_nextquestion = mModelQuestionBankHandInAnalysisView.findViewById(R.id.button_handin_analysis_nextquestion);
        button_handin_analysis_nextquestion.setOnClickListener(V -> {
            if (question_id_groupS != null) {
                TextView questionbank_handin_analysis_questioncount = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncount);
                if (questionbank_handin_analysis_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
//                    HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                    QuestionBankDetailsQuestionModeHandInShow();
                }
            }
        });
        //答题卡
        LinearLayout button_handin_analysis_answerquestioncard = mModelQuestionBankHandInAnalysisView.findViewById(R.id.button_handin_analysis_answerquestioncard);
        button_handin_analysis_answerquestioncard.setOnClickListener(v ->
                QuestionBankDetailsHandInPaperShow(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_handin_analysis_fontsize = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_fontsize);
        questionbank_handin_analysis_fontsize.setOnClickListener(v -> {
            ShowPopFontSize(finalQuestion_id_group, questionbank_handin_analysis_fontsize);
        });
        //添加题的解析（解析题的答案）
        LinearLayout coursedetails_handin_analysis_analysis = mModelQuestionBankHandInAnalysisView.findViewById(R.id.coursedetails_handin_analysis_analysis);
        coursedetails_handin_analysis_analysis.removeAllViews();
        //题的解析网络请求   三个参数      optionanswer  question_type  question_analysis

//        while (cursor1.moveToNext()) {
//            int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
//            int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//            int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
//            String optionanswer = cursor1.getString(optionanswerIndex);
//            String question_type = cursor1.getString(question_typeIndex);
//            String question_analysis = cursor1.getString(question_analysisIndex);
        //判断当前的题目类型
        if ("optionanswer" == null || "question_type" == null || "question_analysis" == null) {

        }
        if ("question_type".equals("1") || "question_type".equals("2")) {//单选题或多选题
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
            coursedetails_handin_analysis_analysis.addView(view);
            //修改内容为正确答案
            String[] optionanswerS = "".split(";");
            if (optionanswerS == null) {

            }
            String currentAnswer = "";
            for (int i = 0; i < optionanswerS.length; i++) {
                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                if (optionanswerS1.length != 3) {
                    break;
                }
                if (optionanswerS1[1].equals("是")) {
                    currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                }
            }
            if (currentAnswer.equals("")) {

            }
            //正确答案内容
            TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
            questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
            TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
            questionbank_analysis1_content.setText("question_analysis");
            //修改内容为您的答案
            TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
            questionbank_analysis1_yourAnswer.setText("我的答案");
            //字体大小的设置
            if (mFontSize.equals("nomal")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
            } else if (mFontSize.equals("small")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
            } else if (mFontSize.equals("big")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
            }
        } else if ("question_type".equals("4")) {//简答题
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
            coursedetails_handin_analysis_analysis.addView(view);
            //修改内容为正确答案
            TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
            questionbank_analysis2_rightAnswer.setText("optionanswer");
            //修改内容为此题的解析
            TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
            questionbank_analysis2_content.setText("question_analysis");
            //修改内容为您的答案
            TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
            if (mFontSize.equals("nomal")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
            } else if (mFontSize.equals("small")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
            } else if (mFontSize.equals("big")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
            }
        }
    }

    //答题-错题模式界面展示
    private void QuestionBankDetailsQuestionModeWrongQuestionShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "wrong";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankWrongQuestionView == null) {
            mModelQuestionBankWrongQuestionView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_wrongquestions, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankWrongQuestionView);
        //此题所属章节名称
        TextView questionbank_wrongquestion_questiontitle = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questiontitle);
        questionbank_wrongquestion_questiontitle.setText(mCurrentChapterName + "当前选中的章或节的名称");
        //点击收藏
        ImageView questionbank_wrongquestion_collection = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_collection);
        questionbank_wrongquestion_collection.setOnClickListener(v -> {
            if (mIsCollect) {
                questionbank_wrongquestion_collection.setBackground(mModelQuestionBankWrongQuestionView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
//                getMyQuestionBankCollection();
                Toast.makeText(mControlMainActivity, "取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                questionbank_wrongquestion_collection.setBackground(mModelQuestionBankWrongQuestionView.getResources().getDrawable(R.drawable.button_collect_enable));
                Toast.makeText(mControlMainActivity, "收藏此题", Toast.LENGTH_SHORT).show();
                mIsCollect = true;
//                getMyQuestionBankCollection();

            }
        });
        //查询试卷表中生成的临时题
//        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
//                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
//        String question_id_group = "";
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("question_id_group");
//            question_id_group = cursor.getString(countIndex);
//            break;
//        }
//        cursor.close();
        //添加题,//默认显示第一题
        WrongQuestionViewAdd("question_id_group");
        //上一题
        String finalQuestion_id_group = "question_id_group";
        String[] question_id_groupS = "question_id_group".substring(1, "question_id_group".length()).split("#");
        LinearLayout button_wrongquestion_beforquestion = mModelQuestionBankWrongQuestionView.findViewById(R.id.button_wrongquestion_beforquestion);
        button_wrongquestion_beforquestion.setOnClickListener(v -> {
            TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
            if (questionbank_wrongquestion_questioncount.getText().toString().equals("1") || questionbank_wrongquestion_questioncount.getText().toString().equals("0")) {
                Toast.makeText(mControlMainActivity, "前面没有题啦", Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeWrongQuestionShow();
            }
        });
        //下一题
        LinearLayout button_wrongquestion_nextquestion = mModelQuestionBankWrongQuestionView.findViewById(R.id.button_wrongquestion_nextquestion);
        button_wrongquestion_nextquestion.setOnClickListener(V -> {
            if (question_id_groupS != null) {
                TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
                if (questionbank_wrongquestion_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
//                    HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                    QuestionBankDetailsQuestionModeWrongQuestionShow();
                }
            }
        });
        //题型
        LinearLayout button_wrongquestion_answerquestioncard = mModelQuestionBankWrongQuestionView.findViewById(R.id.button_wrongquestion_answerquestioncard);
        button_wrongquestion_answerquestioncard.setOnClickListener(v ->
                QuestionBankDetailsQuestionTypeShow(finalQuestion_id_group));
        //交卷
        LinearLayout questionbank_wrongquestion_commit = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_commit);
        questionbank_wrongquestion_commit.setOnClickListener(v -> {
//            if (){  //如果答案不正确，弹出提示框
//                Toast.makeText(mControlMainActivity,"回答错误",Toast.LENGTH_SHORT).show();
//            } else {
            //弹出提示框，回答正确，是否跳转到下一题，如果是，从错题本中移除此题，并跳转到下一道题；如果不是，对话框消失
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            tip.setText("提示");
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("回答正确，是否跳转到下一题？");
            TextView button_cancel = view1.findViewById(R.id.button_cancel);
            button_cancel.setText("否");
            button_cancel.setOnClickListener(View -> {
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("是");
            button_sure.setOnClickListener(View -> {
                if (question_id_groupS != null) {
                    TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
                    if (questionbank_wrongquestion_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                        Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                    } else { //跳到下一道题
                        mCurrentIndex = mCurrentIndex + 1;
//                    HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                        QuestionBankDetailsQuestionModeWrongQuestionShow();
                    }
                }
                mMyDialog.cancel();
            });
//            }
        });
        //点击字号
        ImageView questionbank_wrongquestion_fontsize = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_fontsize);
        questionbank_wrongquestion_fontsize.setOnClickListener(v -> {
            ShowPopFontSize(finalQuestion_id_group, questionbank_wrongquestion_fontsize);
        });
        //点击按钮，显示解析
        TextView coursedetails_wrongquestion_analysisbutton = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_analysisbutton);
        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_wrongquestion_analysisbutton.getLayoutParams();
        lLayoutParams.height = mModelQuestionBankWrongQuestionView.getResources().getDimensionPixelSize(R.dimen.dp_37);
        lLayoutParams.topMargin = mModelQuestionBankWrongQuestionView.getResources().getDimensionPixelSize(R.dimen.dp_70);
        coursedetails_wrongquestion_analysisbutton.setLayoutParams(lLayoutParams);
        LinearLayout coursedetails_wrongquestion_analysis = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_analysis);
        coursedetails_wrongquestion_analysis.removeAllViews();
        coursedetails_wrongquestion_analysisbutton.setOnClickListener(v -> {
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_wrongquestion_analysisbutton.getLayoutParams();
            ll.height = 0;
            ll.topMargin = 0;
            coursedetails_wrongquestion_analysisbutton.setLayoutParams(ll);
//            while (cursor1.moveToNext()) {
//                int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
//                int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//                int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
//                String optionanswer = cursor1.getString(optionanswerIndex);
//                String question_type = cursor1.getString(question_typeIndex);
//                String question_analysis = cursor1.getString(question_analysisIndex);
            if ("optionanswer" == null || "question_type" == null || "question_analysis" == null) {

            }
            if ("question_type".equals("1") || "question_type".equals("2")) {//单选题或多选题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                coursedetails_wrongquestion_analysis.addView(view);
                //修改内容为正确答案
                String[] optionanswerS = "optionanswer".split(";");
                if (optionanswerS == null) {

                }
                String currentAnswer = "";
                for (int i = 0; i < optionanswerS.length; i++) {
                    String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                    if (optionanswerS1.length != 3) {
                        break;
                    }
                    if (optionanswerS1[1].equals("是")) {
                        currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                    }
                }
                if (currentAnswer.equals("")) {

                }
                TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
                TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                questionbank_analysis1_content.setText("question_analysis");
                //修改内容为您的答案
                TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                questionbank_analysis1_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            } else if ("question_type".equals("4")) {//简答题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                coursedetails_wrongquestion_analysis.addView(view);
                //修改内容为正确答案
                TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                questionbank_analysis2_rightAnswer.setText("optionanswer");
                //修改内容为此题的解析
                TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                questionbank_analysis2_content.setText("question_analysis");
                //修改内容为您的答案
                TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            }
        });
    }

    //答题-我收藏的题模式界面展示
    private void QuestionBankDetailsQuestionModeMyCollectionQuestionShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "collection";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankMyCollectionQuestionView == null) {
            mModelQuestionBankMyCollectionQuestionView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_mycollectionquestions, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankMyCollectionQuestionView);
        //此题所属章节名称
        TextView questionbank_mycollextionquestion_questiontitle = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questiontitle);
        questionbank_mycollextionquestion_questiontitle.setText(mCurrentChapterName + "当前选中的章或节的名称");
        //点击收藏
        ImageView questionbank_mycollextionquestion_collection = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_collection);
        questionbank_mycollextionquestion_collection.setOnClickListener(v -> {
            if (mIsCollect) {
                questionbank_mycollextionquestion_collection.setBackground(mModelQuestionBankMyCollectionQuestionView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
//                getMyQuestionBankCollection();
                Toast.makeText(mControlMainActivity, "取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                questionbank_mycollextionquestion_collection.setBackground(mModelQuestionBankMyCollectionQuestionView.getResources().getDrawable(R.drawable.button_collect_enable));
                Toast.makeText(mControlMainActivity, "收藏此题", Toast.LENGTH_SHORT).show();
                mIsCollect = true;
//                getMyQuestionBankCollection();
            }
        });
        //查询试卷表中生成的临时题
      String question_id_group = "";
//        while (cursor.moveToNext()) {
//            int countIndex = cursor.getColumnIndex("question_id_group");
//            question_id_group = cursor.getString(countIndex);
//            break;
//        }
//        cursor.close();        //添加题,//默认显示第一题
        CollectionQuestionViewAdd(question_id_group);
        //上一题    字符串分割
        String finalQuestion_id_group = "question_id_group";
        String[] question_id_groupS =question_id_group.substring(1, question_id_group.length()).split("#");
        LinearLayout button_mycollextionquestion_beforquestion = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.button_mycollextionquestion_beforquestion);
        button_mycollextionquestion_beforquestion.setOnClickListener(v -> {
            TextView questionbank_mycollextionquestion_questioncount = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncount);
            if (questionbank_mycollextionquestion_questioncount.getText().toString().equals("1") || questionbank_mycollextionquestion_questioncount.getText().toString().equals("0")) {
                Toast.makeText(mControlMainActivity, "前面没有题啦", Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
            }
        });
        //下一题
        LinearLayout button_mycollextionquestion_nextquestion = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.button_mycollextionquestion_nextquestion);
        button_mycollextionquestion_nextquestion.setOnClickListener(V -> {
            if (question_id_groupS != null) {
                TextView questionbank_mycollextionquestion_questioncount = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncount);
                if (questionbank_mycollextionquestion_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
//                    HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                    QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
                }
            }
        });
        //题型
        LinearLayout button_mycollextionquestion_answerquestioncard = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.button_mycollextionquestion_answerquestioncard);
        button_mycollextionquestion_answerquestioncard.setOnClickListener(v -> QuestionBankDetailsQuestionTypeShow(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_mycollextionquestion_fontsize = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_fontsize);
        questionbank_mycollextionquestion_fontsize.setOnClickListener(v -> {
            ShowPopFontSize(finalQuestion_id_group, questionbank_mycollextionquestion_fontsize);
        });

        //添加题的解析   我的收藏题
        LinearLayout coursedetails_mycollextionquestion_analysis = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.coursedetails_mycollextionquestion_analysis);
        coursedetails_mycollextionquestion_analysis.removeAllViews();
//        while (cursor1.moveToNext()) {
//            int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
//            int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//            int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
//            String optionanswer = cursor1.getString(optionanswerIndex);
//            String question_type = cursor1.getString(question_typeIndex);
//            String question_analysis = cursor1.getString(question_analysisIndex);
        //我的收藏题网络请求获取的三个参数  optionanswer  question_type question_type
                               getQuestionBankMyFavoriteQuestion();
                               String optionanswer = "";
                               String question_type = "";
                                String question_analysis = "";
        if (optionanswer == null || question_type == null || question_type == null) {

        }
        if (question_type.equals("1") || question_type.equals("2")) {//单选题或多选题
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
            coursedetails_mycollextionquestion_analysis.addView(view);

            //修改内容为正确答案  分割字符串数组
            String[] optionanswerS = optionanswer.split(";");
            if (optionanswerS == null) {
            }
            //字符串截取正确的答案
            String currentAnswer = "";
            for (int i = 0; i < optionanswerS.length; i++) {
                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                if (optionanswerS1.length != 3) {
                    break;
                }
                if (optionanswerS1[1].equals("是")) {
                    currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                }
            }
            if (currentAnswer.equals("")) {

            }

            TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
            questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
            TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
            questionbank_analysis1_content.setText("问题的解析");
            //修改内容为您的答案
            TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
            questionbank_analysis1_yourAnswer.setText("aaa");
            //修改内容的字体大小
            if (mFontSize.equals("nomal")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
            } else if (mFontSize.equals("small")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
            } else if (mFontSize.equals("big")) {
                questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
            }
        } else if ("question_type".equals("4")) {//简答题
                    //简答题的解析
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
            coursedetails_mycollextionquestion_analysis.addView(view);
            //修改内容为正确答案
            TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
            questionbank_analysis2_rightAnswer.setText("optionanswer");
            //修改内容为此题的解析
            TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
            questionbank_analysis2_content.setText("question_analysis");
            //修改内容为您的答案
            TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa")

            if (mFontSize.equals("nomal")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize17));
            } else if (mFontSize.equals("small")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize14));
            } else if (mFontSize.equals("big")) {
                questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.textsize20));
            }
        }
    }

    //答题-做题记录模式界面展示
    private void QuestionBankDetailsQuestionModeQuestionRecordShow() {
        if (mview == null) {
            return;
        }
        mCurrentAnswerMode = "requestionrecord";
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankQuestionRecordView == null) {
            mModelQuestionBankQuestionRecordView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questionrecord, null);
            //章节练习
            TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
            //快速做题
            TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
            //模拟真题
            TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
            questionbank_questionrecords_tab_chapterexercises.setOnClickListener(this);
            questionbank_questionrecords_tab_quicktask.setOnClickListener(this);
            questionbank_questionrecords_tab_simulated.setOnClickListener(this);
            //做题记录的刷新控件
            mSmart_model_questionbank_questionrecord = mModelQuestionBankQuestionRecordView.findViewById(R.id.Smart_model_questionbank_questionrecord);
            mSmart_model_questionbank_questionrecord.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_questionbank_questionrecord.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_questionbank_questionrecord.finishRefresh();
                }
            });

        }
        fragmentquestionbank_main.addView(mModelQuestionBankQuestionRecordView);
        //默认游标位置在章节练习
        ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
        int x = width / 6 - mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        questionbank_questionrecords_cursor1.setX(x);
        //默认选中的为章节练习
        mQuestionRecordLastTabIndex = 1;
        mQuestionRecordCurrentTab = "ChapterExercises";
        //章节练习
        TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
        //快速做题
        TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
        //模拟真题
        TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
        questionbank_questionrecords_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        questionbank_questionrecords_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        questionbank_questionrecords_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));

        LinearLayout questionbank_questionrecords_content = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_content);
        questionbank_questionrecords_content.removeAllViews();
        View questionbank_questionrecords_line1 = null;
    //   while (cursor.moveToNext()) {
        //        int test_paper_idIndex = cursor.getColumnIndex("test_paper_id");
        //     int test_paper_nameIndex = cursor.getColumnIndex("test_paper_name");
        //        int timeIndex = cursor.getColumnIndex("time");
        //        int used_answer_timeIndex = cursor.getColumnIndex("used_answer_time");
        //        int question_numIndex = cursor.getColumnIndex("question_num");
        //       int error_numIndex = cursor.getColumnIndex("error_num");
        //        String test_paper_id = cursor.getString(test_paper_idIndex);
        //        String test_paper_name = cursor.getString(test_paper_nameIndex);
        //        String time = cursor.getString(timeIndex);
        //        String used_answer_time = cursor.getString(used_answer_timeIndex);
        //         String question_num = cursor.getString(question_numIndex);
        //        String error_num = cursor.getString(error_numIndex);
        //试卷名称  测试的名称   网络请求
        String time = "";
        String test_paper_id = "";
        String test_paper_name = "";
        String used_answer_time = "";
        String question_nu = "";
        String question_num = "";
        String error_num = "";

        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questionrecord1, null);
        //试卷名称
        TextView questionbank_questionrecords_testname = view.findViewById(R.id.questionbank_questionrecords_testname);
        questionbank_questionrecords_testname.setText(test_paper_name);
        //试卷时间
        TextView questionbank_questionrecords_detailstime = view.findViewById(R.id.questionbank_questionrecords_detailstime);
        if (time == null) {
            time = "";
        }
        if (used_answer_time == null) {
            used_answer_time = "0";
        }
        if (question_num == null) {
            question_num = "0";
        }
        if (error_num == null) {
            error_num = "";
        }
        time = time.substring(0, time.indexOf(" "));
        questionbank_questionrecords_detailstime.setText(time);
        questionbank_questionrecords_content.addView(view);
        //试卷的时间
        TextView questionbank_questionrecords_detailsduring = view.findViewById(R.id.questionbank_questionrecords_detailsduring);
        questionbank_questionrecords_detailsduring.setText(getStringTime(Integer.valueOf(used_answer_time)));
        //正确的数量
        TextView questionbank_questionrecords_detailsrightnum = view.findViewById(R.id.questionbank_questionrecords_detailsrightnum);
        int errornum = 0;
        if (!error_num.equals("")) {
            errornum = error_num.split(";").length;
        }
        questionbank_questionrecords_detailsrightnum.setText((Integer.valueOf(question_num) - errornum) + "");
        //错误的数量
        TextView questionbank_questionrecords_detailserrornum = view.findViewById(R.id.questionbank_questionrecords_detailserrornum);
        questionbank_questionrecords_detailserrornum.setText(errornum + "");
        questionbank_questionrecords_line1 = view.findViewById(R.id.questionbank_questionrecords_line1);
        String finalUsed_answer_time = used_answer_time;
        view.setOnClickListener(v -> {
            //弹出提示框，回答正确，是否跳转到下一题，如果是，从错题本中移除此题，并跳转到下一道题；如果不是，对话框消失
            View view1 = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
            ControllerCenterDialog mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view1, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view1.findViewById(R.id.tip);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) tip.getLayoutParams();
            rl.height = 0;
            tip.setLayoutParams(rl);
            TextView dialog_content = view1.findViewById(R.id.dialog_content);
            dialog_content.setText("该试卷已完成，耗时" + getStringTime(Integer.valueOf(finalUsed_answer_time)));
            TextView button_cancel = view1.findViewById(R.id.button_cancel);
            button_cancel.setText("查看解析");
            button_cancel.setOnClickListener(View -> {
                //跳转到解析界面
                Toast.makeText(mControlMainActivity, "查看解析内容", Toast.LENGTH_SHORT).show();
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("再做一遍");
            button_sure.setOnClickListener(View -> {//将试卷重新调出来，做题
                Toast.makeText(mControlMainActivity, "试卷需要重新做", Toast.LENGTH_SHORT).show();

                mMyDialog.cancel();
            });
        });
        // }
        if (questionbank_questionrecords_line1 != null) {
            questionbank_questionrecords_line1.setVisibility(View.INVISIBLE);
        }
    }

    //添加问题界面
    private void QuestionViewAdd(String question_id_group) {
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_answerpaper_details = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_details);
        coursedetails_answerpaper_details.removeAllViews();
        coursedetails_answerpaper_details.addView(view2);

        //字符串分割
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
            //总体的数量
            TextView questionbank_answerpaper_questioncountsum = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncountsum);
            questionbank_answerpaper_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                //单选题
                //案列分析的解析
                TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
                questionbank_answerpaper_questioncount.setText(String.valueOf(mCurrentIndex + 1));

                //  while (cursor.moveToNext()) {
                //  int question_idIndex = cursor.getColumnIndex("question_id");
                //  int question_nameIndex = cursor.getColumnIndex("question_name");
                // int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                //  int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                // int question_analysisIndex = cursor.getColumnIndex("question_analysis");
                //String question_id = cursor.getString(question_idIndex);
                //String question_name = cursor.getString(question_nameIndex);
                //String optionanswer = cursor.getString(optionanswerIndex);
//                    String question_type = cursor.getString(question_typeIndex);
//                    String question_analysis = cursor.getString(question_analysisIndex);
                TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                if (mFontSize.equals("nomal")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
                questionbank_answerpaper_single_title.setText("question_name");
                questionbank_answerpaper_single_title.setHint("question_id");
                //判断当前选择题的类型
                TextView questionbank_answerpaper_questiontype = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questiontype);
                if ("question_type".equals("1")) {
                    questionbank_answerpaper_questiontype.setText("[单选题]");
                } else if ("question_type".equals("2")) {
                    questionbank_answerpaper_questiontype.setText("[多选题]");
                } else if ("question_type".equals("4")) {
                    questionbank_answerpaper_questiontype.setText("[简答题]");
                } else if ("question_type".equals("7")) {
                    questionbank_answerpaper_questiontype.setText("[材料题]");
                }
                LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                questionbank_answerpaper_content.removeAllViews();
                if ("question_type".equals("1") || "question_type".equals("2")) { //如果是单选题或多选题添加选项布局
                    String[] optionanswerS = "optionanswer".split(";");
                    if (optionanswerS != null) {
                        for (int i = 0; i < optionanswerS.length; i++) {
                            View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                            String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                            if (optionanswerS1.length != 3) {//question_analysisS1的结构应为#A#是#选择A
                                continue;
                            }
                            TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                            questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                            questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                            questionbank_answerpaper_option_name.setOnClickListener(v -> {
                                questionbank_answerpaper_option_name.setBackground(view3.getResources().getDrawable(R.drawable.textview_style_circle_blue649cf0));
                                questionbank_answerpaper_option_name.setTextColor(view3.getResources().getColor(R.color.blue649cf0));
                                if ("question_type".equals("1")) { //如果是单选题，将其他选项置为false
                                    int count = questionbank_answerpaper_content.getChildCount();
                                    for (int num = 0; num < count; num++) {
                                        View view4 = questionbank_answerpaper_content.getChildAt(num);
                                        if (view4 != view3) {
                                            TextView textview = view4.findViewById(R.id.questionbank_answerpaper_option_name);
                                            textview.setBackground(view3.getResources().getDrawable(R.drawable.textview_style_circle_gray8099));
                                            textview.setTextColor(view3.getResources().getColor(R.color.black80999999));
                                        }
                                    }
                                }
                            });
                            TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                            questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                            if (mFontSize.equals("nomal")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                            } else if (mFontSize.equals("small")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                            } else if (mFontSize.equals("big")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                            }
                            questionbank_answerpaper_content.addView(view3);
                        }
                    }
                } else if ("question_type".equals("4")) {//如果是简答题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                } else if ("question_type".equals("4")) {//如果是材料题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                }
            }


        }
    }

    //添加答题卡-问题界面
    private void HandInAnalysisQuestionViewAdd(String question_id_group) {
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_handin_analysis_details = mModelQuestionBankHandInAnalysisView.findViewById(R.id.coursedetails_handin_analysis_details);
        coursedetails_handin_analysis_details.removeAllViews();
        coursedetails_handin_analysis_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
            TextView questionbank_handin_analysis_questioncountsum = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncountsum);
            questionbank_handin_analysis_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_handin_analysis_questioncount = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncount);
                questionbank_handin_analysis_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                //   Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                //         "select question_id,question_name,optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                //               + question_id_groupS[mCurrentIndex] + "';", null);
//                while (cursor.moveToNext()) {
//                    int question_idIndex = cursor.getColumnIndex("question_id");
//                    int question_nameIndex = cursor.getColumnIndex("question_name");
//                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
//                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");
//                    String question_id = cursor.getString(question_idIndex);
//                    String question_name = cursor.getString(question_nameIndex);
//                    String optionanswer = cursor.getString(optionanswerIndex);
//                    String question_type = cursor.getString(question_typeIndex);
//                    String question_analysis = cursor.getString(question_analysisIndex);
                TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                if (mFontSize.equals("nomal")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
                questionbank_answerpaper_single_title.setText("question_name");
                questionbank_answerpaper_single_title.setHint("question_id");
                //单选题或者多选题赋值
                TextView questionbank_handin_analysis_questiontype = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questiontype);
                if ("question_type".equals("1")) {
                    questionbank_handin_analysis_questiontype.setText("[单选题]");
                } else if ("question_type".equals("2")) {
                    questionbank_handin_analysis_questiontype.setText("[多选题]");
                } else if ("question_type".equals("4")) {
                    questionbank_handin_analysis_questiontype.setText("[简答题]");
                } else if ("question_type".equals("7")) {
                    questionbank_handin_analysis_questiontype.setText("[材料题]");
                }

                LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                questionbank_answerpaper_content.removeAllViews();
                if ("question_type".equals("1") || "question_type".equals("2")) { //如果是单选题或多选题添加选项布局
                    String[] optionanswerS = "optionanswer".split(";");
                    if (optionanswerS != null) {
                        for (int i = 0; i < optionanswerS.length; i++) {
                            View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                            String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                            if (optionanswerS1.length != 3) {//question_analysisS1的结构应为#A#是#选择A
                                continue;
                            }
                            TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                            questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                            questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                            TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                            questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                            if (mFontSize.equals("nomal")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                            } else if (mFontSize.equals("small")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                            } else if (mFontSize.equals("big")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                            }
                            questionbank_answerpaper_content.addView(view3);
                        }
                    }
                } else if ("question_type".equals("4")) {//如果是简答题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    //设置不可编辑，交卷以后不能输入
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                    rl.height = 0;
                    rl.topMargin = 0;
                    questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                } else if ("question_type".equals("4")) {//如果是材料题
                    //输入答案界面
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    //设置不可编辑，交卷以后不能输入
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                    rl.height = 0;
                    rl.topMargin = 0;
                    questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                }

            }
        }
    }

    //添加错题本-问题界面
    private void WrongQuestionViewAdd(String question_id_group) {
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_wrongquestion_details = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_details);
        coursedetails_wrongquestion_details.removeAllViews();
        coursedetails_wrongquestion_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
            TextView questionbank_wrongquestion_questioncountsum = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncountsum);
            questionbank_wrongquestion_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                //错题本的数量
                TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
                questionbank_wrongquestion_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                //while (cursor.moveToNext()) {
                //    int question_idIndex = cursor.getColumnIndex("question_id");
                //    int question_nameIndex = cursor.getColumnIndex("question_name");
                //    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                //    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                //    int question_analysisIndex = cursor.getColumnIndex("question_analysis");

                //    String question_id = cursor.getString(question_idIndex);
                //    String question_name = cursor.getString(question_nameIndex);
                //   String optionanswer = cursor.getString(optionanswerIndex);
                //   String question_type = cursor.getString(question_typeIndex);
                //  String question_analysis = cursor.getString(question_analysisIndex);
                String question_id="";
                String question_name="错题本的数量";
                String optionanswer="错题本的问题";
                String question_type="错题本的分类";
                String question_analysis="";
                TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                if (mFontSize.equals("nomal")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
                questionbank_answerpaper_single_title.setText(question_name);
                questionbank_answerpaper_single_title.setHint(question_id);
                TextView questionbank_wrongquestion_questiontype = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questiontype);
                if (question_type.equals("1")) {
                    questionbank_wrongquestion_questiontype.setText("[单选题]");
                } else if (question_type.equals("2")) {
                    questionbank_wrongquestion_questiontype.setText("[多选题]");
                } else if (question_type.equals("4")) {
                    questionbank_wrongquestion_questiontype.setText("[简答题]");
                } else if (question_type.equals("7")) {
                    questionbank_wrongquestion_questiontype.setText("[材料题]");
                }
                LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                questionbank_answerpaper_content.removeAllViews();
                if (question_type.equals("1") || question_type.equals("2")) { //如果是单选题或多选题添加选项布局
                    String[] optionanswerS = optionanswer.split(";");
                    if (optionanswerS != null) {
                        for (int i = 0; i < optionanswerS.length; i++) {
                            View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                            String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                            if (optionanswerS1.length != 3) {//question_analysisS1的结构应为#A#是#选择A
                                continue;
                            }
                            TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                            questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                            questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                            TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                            questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                            if (mFontSize.equals("nomal")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                            } else if (mFontSize.equals("small")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                            } else if (mFontSize.equals("big")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                            }
                            questionbank_answerpaper_content.addView(view3);
                            questionbank_answerpaper_option_name.setOnClickListener(v -> {
                                questionbank_answerpaper_option_name.setBackground(view3.getResources().getDrawable(R.drawable.textview_style_circle_blue649cf0));
                                questionbank_answerpaper_option_name.setTextColor(view3.getResources().getColor(R.color.blue649cf0));
                                if (question_type.equals("1")) { //如果是单选题，将其他选项置为false
                                    int count = questionbank_answerpaper_content.getChildCount();
                                    for (int num = 0; num < count; num++) {
                                        View view4 = questionbank_answerpaper_content.getChildAt(num);
                                        if (view4 != view3) {
                                            TextView textview = view4.findViewById(R.id.questionbank_answerpaper_option_name);
                                            textview.setBackground(view3.getResources().getDrawable(R.drawable.textview_style_circle_gray8099));
                                            textview.setTextColor(view3.getResources().getColor(R.color.black80999999));
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else if (question_type.equals("4")) {//如果是简答题

                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    //请输入你的答案
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                } else if (question_type.equals("4")) {
                    //如果是材料题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    //请输入你的答案
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                }
            }
        }
    }

    //题型展示
    private void QuestionBankDetailsQuestionTypeShow(String question_id_group) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankQuestionTypeView == null) {
            mModelQuestionBankQuestionTypeView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questiontype, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankQuestionTypeView);
        LinearLayout questionbank_questiontype_singlebutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_singlebutton);
        questionbank_questiontype_singlebutton.setOnClickListener(v -> { //直接跳到单选题

        });
        LinearLayout questionbank_questiontype_mutilbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_mutilbutton);
        questionbank_questiontype_mutilbutton.setOnClickListener(v -> { //直接跳到多选题

        });
        LinearLayout questionbank_questiontype_shortanswerbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_shortanswerbutton);
        questionbank_questiontype_shortanswerbutton.setOnClickListener(v -> { //直接跳到简答题

        });
        LinearLayout questionbank_questiontype_materialbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_materialbutton);
        questionbank_questiontype_materialbutton.setOnClickListener(v -> { //直接跳到材料题
        });
    }

    //添加收藏题本-问题界面
    private void CollectionQuestionViewAdd(String question_id_group) {
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_mycollextionquestion_details = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.coursedetails_mycollextionquestion_details);
        coursedetails_mycollextionquestion_details.removeAllViews();
        coursedetails_mycollextionquestion_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
            TextView questionbank_mycollextionquestion_questioncountsum = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncountsum);
            questionbank_mycollextionquestion_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_mycollextionquestion_questioncount = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncount);
                questionbank_mycollextionquestion_questioncount.setText(String.valueOf(mCurrentIndex + 1));
//                while (cursor.moveToNext()) {
//                    int question_idIndex = cursor.getColumnIndex("question_id");
//                    int question_nameIndex = cursor.getColumnIndex("question_name");
//                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
//                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
//                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");

//                    String question_id = cursor.getString(question_idIndex);
//                    String question_name = cursor.getString(question_nameIndex);
//                    String optionanswer = cursor.getString(optionanswerIndex);
//                    String question_type = cursor.getString(question_typeIndex);
//                    String question_analysis = cursor.getString(question_analysisIndex);
                TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                if (mFontSize.equals("nomal")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")) {
                    questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
                questionbank_answerpaper_single_title.setText("question_name");
                questionbank_answerpaper_single_title.setHint("question_id");
                TextView questionbank_mycollextionquestion_questiontype = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questiontype);
                if ("question_type".equals("1")) {
                    questionbank_mycollextionquestion_questiontype.setText("[单选题]");
                } else if ("question_type".equals("2")) {
                    questionbank_mycollextionquestion_questiontype.setText("[多选题]");
                } else if ("question_type".equals("4")) {
                    questionbank_mycollextionquestion_questiontype.setText("[简答题]");
                } else if ("question_type".equals("7")) {
                    questionbank_mycollextionquestion_questiontype.setText("[材料题]");
                }
                LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                questionbank_answerpaper_content.removeAllViews();
                if ("question_type".equals("1") || "question_type".equals("2")) { //如果是单选题或多选题添加选项布局
                    String[] optionanswerS = "question_type".split(";");
                    if (optionanswerS != null) {
                        for (int i = 0; i < optionanswerS.length; i++) {
                            View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                            String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                            if (optionanswerS1.length != 3) {//question_analysisS1的结构应为#A#是#选择A
                                continue;
                            }
                            TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                            questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                            questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                            TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                            questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                            if (mFontSize.equals("nomal")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                            } else if (mFontSize.equals("small")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                            } else if (mFontSize.equals("big")) {
                                questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                            }
                            questionbank_answerpaper_content.addView(view3);
                        }
                    }
                } else if ("question_type".equals("4")) {//如果是简答题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    //设置不可编辑，交卷以后不能输入
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                    rl.height = 0;
                    rl.topMargin = 0;
                    questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                } else if ("question_type".equals("4")) {//如果是材料题
                    View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                    questionbank_answerpaper_content.addView(view3);
                    EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                    if (mFontSize.equals("nomal")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")) {
                        questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    //设置不可编辑，交卷以后不能输入
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                    rl.height = 0;
                    rl.topMargin = 0;
                    questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                }
            }
        }
    }

    //添加答题卡界面
    private void AnswerQuestionCardViewAdd(String question_id_group) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankAnswerQuestionCard();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        mModelQuestionBankAnswerQuestionCardView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard, null);
        fragmentquestionbank_main.addView(mModelQuestionBankAnswerQuestionCardView);
        LinearLayout coursedetails_answerquestioncard_details = mModelQuestionBankAnswerQuestionCardView.findViewById(R.id.coursedetails_answerquestioncard_details);
        coursedetails_answerquestioncard_details.removeAllViews();
        //字符串分割
        String[] question_id_groupS = question_id_group.substring(1, question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length) { //不在数组范围直接返回
                return;
            }
        }
        View singleView = null;
        View mutilView = null;
        View shortAnswerView = null;
        View materialView = null;
        int count = 0;
//        for (int i = 0; i < question_id_groupS.length ; i ++) {
//        while (cursor.moveToNext()) {
//            int question_typeIndex = cursor.getColumnIndex("question_type");
//            int tf_markedIndex = cursor.getColumnIndex("tf_marked");
//            String question_type = cursor.getString(question_typeIndex);
//            String tf_marked = cursor.getString(tf_markedIndex);
        if ("question_type".equals("1")) {
            if (singleView == null) {
                singleView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard1, null);
                coursedetails_answerquestioncard_details.addView(singleView);
            }
            GridLayout coursedetails_answerquestioncard_questionnumber = singleView.findViewById(R.id.coursedetails_answerquestioncard_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard2, null);
            coursedetails_answerquestioncard_questionnumber.addView(view);
            //题标序号
            TextView questionbank_answerquestioncard2_select = view.findViewById(R.id.questionbank_answerquestioncard2_select);
            questionbank_answerquestioncard2_select.setText("" + (count + 1));
            if ("tf_marked" != null) {
                if ("tf_marked".equals("1")) {//标记此题
                    ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                    questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                }
            }
            if (mCurrentIndex == count) { //此题为当前正在答的题,改变题的颜色
                questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
            }
            int finalCount = count;
            questionbank_answerquestioncard2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeShow();
            });
        } else if ("question_type".equals("2")) {
            if (mutilView == null) {
                mutilView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard1, null);
                coursedetails_answerquestioncard_details.addView(mutilView);
                TextView coursedetails_handinpaper1_questiontype = mutilView.findViewById(R.id.coursedetails_answerquestioncard_questiontype);
                coursedetails_handinpaper1_questiontype.setText("多选题");
            }
            GridLayout coursedetails_answerquestioncard_questionnumber = mutilView.findViewById(R.id.coursedetails_answerquestioncard_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard2, null);
            coursedetails_answerquestioncard_questionnumber.addView(view);
            TextView questionbank_answerquestioncard2_select = view.findViewById(R.id.questionbank_answerquestioncard2_select);
            questionbank_answerquestioncard2_select.setText("" + (count + 1));
            if ("tf_marked" != null) {
                if ("tf_marked".equals("1")) {//标记此题
                    ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                    questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                }
            }
            if (mCurrentIndex == count) { //此题为当前正在答的题,改变题的颜色
                questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
            }
            int finalCount = count;
            questionbank_answerquestioncard2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeShow();
            });
        } else if ("question_type".equals("4")) {
            if (shortAnswerView == null) {
                shortAnswerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard1, null);
                coursedetails_answerquestioncard_details.addView(shortAnswerView);
                TextView coursedetails_handinpaper1_questiontypeshortAnswerView = shortAnswerView.findViewById(R.id.coursedetails_answerquestioncard_questiontype);
                coursedetails_handinpaper1_questiontypeshortAnswerView.setText("简答题");
            }
            GridLayout coursedetails_answerquestioncard_questionnumber = shortAnswerView.findViewById(R.id.coursedetails_answerquestioncard_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard2, null);
            coursedetails_answerquestioncard_questionnumber.addView(view);
            TextView questionbank_answerquestioncard2_select = view.findViewById(R.id.questionbank_answerquestioncard2_select);
            questionbank_answerquestioncard2_select.setText("" + (count + 1));
            if ("tf_marked" != null) {
                if ("tf_marked".equals("1")) {//标记此题
                    ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                    questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                }
            }
            if (mCurrentIndex == count) { //此题为当前正在答的题,改变题的颜色
                questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
            }
            int finalCount = count;
            questionbank_answerquestioncard2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeShow();
            });
        } else if ("question_type".equals("7")) {
            if (materialView == null) {
                materialView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard1, null);
                coursedetails_answerquestioncard_details.addView(materialView);
                TextView coursedetails_handinpaper1_questiontypematerialView = materialView.findViewById(R.id.coursedetails_answerquestioncard_questiontype);
                coursedetails_handinpaper1_questiontypematerialView.setText("材料题");
            }
            GridLayout coursedetails_answerquestioncard_questionnumber = materialView.findViewById(R.id.coursedetails_answerquestioncard_questionnumber);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard2, null);
            coursedetails_answerquestioncard_questionnumber.addView(view);
            TextView questionbank_answerquestioncard2_select = view.findViewById(R.id.questionbank_answerquestioncard2_select);
            questionbank_answerquestioncard2_select.setText("" + (count + 1));
            if ("tf_marked" != null) {
                if ("tf_marked".equals("1")) {//标记此题
                    ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                    questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                }
            }
            if (mCurrentIndex == count) { //此题为当前正在答的题,改变题的颜色
                questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
            }
            int finalCount = count;
            questionbank_answerquestioncard2_select.setOnClickListener(v -> { //点击题号。跳转到指定题
                mCurrentIndex = finalCount;
                QuestionBankDetailsQuestionModeShow();
            });
        }
        count++;
        //点击交卷查看结果
        TextView coursedetails_answerquestioncard_commit = mModelQuestionBankAnswerQuestionCardView.findViewById(R.id.coursedetails_answerquestioncard_commit);
        coursedetails_answerquestioncard_commit.setOnClickListener(v -> {
            //显示交卷界面
            QuestionBankDetailsHandInPaperShow(question_id_group);
        });
    }

    //隐藏所有图层
    private void HideAllLayout() {
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        fragmentquestionbank_main.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //章节练习
            case R.id.questionbank_sub_details_tab_chapterexercises: {
                //章节练习
                if (!mCurrentTab.equals("ChapterExercises")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation((mLastTabIndex - 1) * width / 3, 0, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_sub_details_cursor1.startAnimation(animation);
                    TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
                    TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
                    TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
                    questionbank_sub_details_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    questionbank_sub_details_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_sub_details_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }

                mLastTabIndex = 1;
                mCurrentTab = "ChapterExercises";    //章节练习
                QuestionBankDetailsChapterShow();
                break;
            }
            //快速做题
            case R.id.questionbank_sub_details_tab_quicktask: {
                if (!mCurrentTab.equals("QuickTask")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation((mLastTabIndex - 1) * width / 3, width / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_sub_details_cursor1.startAnimation(animation);
                    TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
                    TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
                    TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
                    questionbank_sub_details_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_sub_details_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    questionbank_sub_details_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                QuestionBankDetailsQuickTaskShow();
                mLastTabIndex = 2;
                //快速做题
                mCurrentTab = "QuickTask";
                break;
            }
            //模拟真题
            case R.id.questionbank_sub_details_tab_simulated: {

                if (!mCurrentTab.equals("Simulated")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation((mLastTabIndex - 1) * width / 3, width * 2 / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_sub_details_cursor1.startAnimation(animation);
                    TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
                    TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
                    TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
                    questionbank_sub_details_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_sub_details_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_sub_details_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                QuestionBankDetailsSimulatedShow();
                mLastTabIndex = 3;
                mCurrentTab = "Simulated";
                break;
            }
            //章节练习
            case R.id.questionbank_questionrecords_tab_chapterexercises: {
                if (!mQuestionRecordCurrentTab.equals("ChapterExercises")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation((mQuestionRecordLastTabIndex - 1) * width / 3, 0, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_questionrecords_cursor1.startAnimation(animation);
                    TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
                    TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
                    TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
                    questionbank_questionrecords_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    questionbank_questionrecords_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_questionrecords_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mQuestionRecordLastTabIndex = 1;
                mQuestionRecordCurrentTab = "ChapterExercises";
                break;
            }
            //快速做题
            case R.id.questionbank_questionrecords_tab_quicktask: {
                if (!mQuestionRecordCurrentTab.equals("QuickTask")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation((mQuestionRecordLastTabIndex - 1) * width / 3, width / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_questionrecords_cursor1.startAnimation(animation);
                    TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
                    TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
                    TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
                    questionbank_questionrecords_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_questionrecords_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    questionbank_questionrecords_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mQuestionRecordLastTabIndex = 2;
                mQuestionRecordCurrentTab = "QuickTask";


                break;
            }
            //模拟真题
            case R.id.questionbank_questionrecords_tab_simulated: {
                if (!mQuestionRecordCurrentTab.equals("Simulated")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation((mQuestionRecordLastTabIndex - 1) * width / 3, width * 2 / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    questionbank_questionrecords_cursor1.startAnimation(animation);
                    TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
                    TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
                    TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
                    questionbank_questionrecords_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_questionrecords_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    questionbank_questionrecords_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mQuestionRecordLastTabIndex = 3;
                mQuestionRecordCurrentTab = "Simulated";

                break;
            }
            //三道杠
            case R.id.questionbank_sub_details_buttonmore: {
                showPop();//popwindow设置
                toggleBright(); //设置动画的时间，长度和距离
                break;
            }
            case R.id.pop_add_mycollect: {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
                break;
            }
            case R.id.pop_add_mywrong: {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeWrongQuestionShow();
                break;
            }
            case R.id.pop_add_myrecord: {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeQuestionRecordShow();
                break;
            }
            default:
                break;
        }
    }

    //显示pop的文件
    private void showPop() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(mControlMainActivity).inflate(R.layout.pop_add, null));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        ImageView questionbank_sub_details_buttonmore = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_buttonmore);
        mPopupWindow.showAsDropDown(questionbank_sub_details_buttonmore, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(() -> toggleBright());

        TextView pop_add_mycollect = mPopupWindow.getContentView().findViewById(R.id.pop_add_mycollect);
        TextView pop_add_mywrong = mPopupWindow.getContentView().findViewById(R.id.pop_add_mywrong);
        TextView pop_add_myrecord = mPopupWindow.getContentView().findViewById(R.id.pop_add_myrecord);

        pop_add_mycollect.setOnClickListener(this);
        pop_add_mywrong.setOnClickListener(this);
        pop_add_myrecord.setOnClickListener(this);
    }

    private void ShowPopFontSize(String question_id_group, ImageView imageView) {
        if (mPointoutPopupWindow == null) {
            mPointoutPopupWindow = new PopupWindow(mControlMainActivity);
            mPointoutAnimUtil = new ModelAnimUtil();
        }
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        mPointoutAnimUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        mPointoutAnimUtil.addUpdateListener(progress -> {
            // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
            bgPointoutAlpha = bPointoutRight ? progress : (START_ALPHA + END_ALPHA - progress);
            backgroundAlpha(bgPointoutAlpha);
        });
        mPointoutAnimUtil.addEndListner(animator -> {
            // 在一次动画结束的时候，翻转状态
            bPointoutRight = !bPointoutRight;
        });
        mPointoutAnimUtil.startAnimator();
        //字号大小的设置
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.pop_pointout, null);
        // 设置布局文件
        mPointoutPopupWindow.setContentView(view);
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPointoutPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPointoutPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPointoutPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPointoutPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPointoutPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPointoutPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPointoutPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPointoutPopupWindow.showAsDropDown(imageView, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPointoutPopupWindow.setOnDismissListener(() -> {
            // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
            mPointoutAnimUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
            mPointoutAnimUtil.addUpdateListener(progress -> {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgPointoutAlpha = bPointoutRight ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgPointoutAlpha);
            });
            mPointoutAnimUtil.addEndListner(animator -> {
                // 在一次动画结束的时候，翻转状态
                bPointoutRight = !bPointoutRight;
            });
            mPointoutAnimUtil.startAnimator();
        });
        //A--字体大小的设置
        TextView fontsizesmall = view.findViewById(R.id.fontsizesmall);
        //A 字体大小的设置
        TextView fontsizenomal = view.findViewById(R.id.fontsizenomal);
        //A++ 变大字体大小的设置
        TextView fontsizebig = view.findViewById(R.id.fontsizebig);
        if (mFontSize.equals("small")) {
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.blue649cf0));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
        } else if (mFontSize.equals("nomal")) {
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.blue649cf0));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
        } else if (mFontSize.equals("big")) {
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.blue649cf0));
        }
        if (mCurrentAnswerMode.equals("handin")) {
            LinearLayout fontsize_main_layout = view.findViewById(R.id.fontsize_main_layout);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) fontsize_main_layout.getLayoutParams();
            ll.rightMargin = view.getResources().getDimensionPixelSize(R.dimen.dp13);
            fontsize_main_layout.setLayoutParams(ll);
            fontsizesmall.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                mFontSize = "small";
//                HandInAnalysisQuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
            fontsizenomal.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                mFontSize = "nomal";
//                HandInAnalysisQuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
            fontsizebig.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                mFontSize = "big";
//                HandInAnalysisQuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
        } else {
            fontsizesmall.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                mFontSize = "small";
//                QuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
            fontsizenomal.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                mFontSize = "nomal";
//                QuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
            fontsizebig.setOnClickListener(v -> {
                fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
                fontsizebig.setTextColor(view.getResources().getColor(R.color.blue649cf0));
                mFontSize = "big";
//                QuestionViewAdd(question_id_group);
                QuestionBankDetailsQuestionModeShow();
            });
        }
    }

    //起始，结束，时间长度
    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(progress -> {
            // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
            bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
            backgroundAlpha(bgAlpha);
        });
        animUtil.addEndListner(animator -> {
            // 在一次动画结束的时候，翻转状态
            bright = !bright;
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mControlMainActivity.getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        mControlMainActivity.getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        mControlMainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onDestroyView() {
        if (mTimer2 != null) {
            mTimer2.cancel();
            mTimer2 = null;
        }
        if (mTask2 != null) {
            mTask2.cancel();
            mTask2 = null;
        }
        super.onDestroyView();
    }

    //题库收藏和取消收藏
    public static class MyQuestionBankCollection {
        /**
         * msg : 修改成功
         * code : 200
         */

        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    //题库标记和取消标记
    public static class MyQuestionBankflag {

    }

    //题库--章节考点
    public static class MyQuestionBankChapterTestBean {
        /**
         * msg : 查询成功
         * code : 200
         * data : [{"chapter_test_point_id":1,"ibs_id":1,"num":39,"jie":[{"chapter_test_point_id":6,"ibs_id":1,"num":39,"name":"节1333","father_id":1,"type":2}],"name":"章1222","father_id":0,"type":1},{"chapter_test_point_id":2,"ibs_id":1,"num":38,"jie":[{"chapter_test_point_id":7,"ibs_id":1,"num":36,"name":"节1","father_id":2,"type":2},{"chapter_test_point_id":12,"ibs_id":1,"num":2,"name":"节1","father_id":2,"type":2}],"name":"章2333","father_id":0,"type":1},{"chapter_test_point_id":3,"ibs_id":1,"num":41,"jie":[{"chapter_test_point_id":8,"ibs_id":1,"num":41,"name":"节1","father_id":3,"type":2}],"name":"章3","father_id":0,"type":1},{"chapter_test_point_id":4,"ibs_id":1,"num":47,"jie":[{"chapter_test_point_id":9,"ibs_id":1,"num":47,"name":"节1","father_id":4,"type":2}],"name":"章4","father_id":0,"type":1}]
         */

        private String msg;
        private int code;
        private List<DataBean> data;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * chapter_test_point_id : 1
             * ibs_id : 1
             * num : 39
             * jie : [{"chapter_test_point_id":6,"ibs_id":1,"num":39,"name":"节1333","father_id":1,"type":2}]
             * name : 章1222
             * father_id : 0
             * type : 1
             */

            private int chapter_test_point_id;
            private int ibs_id;
            private int num;
            private String name;
            private int father_id;
            private int type;
            private List<JieBean> jie;

            public int getChapter_test_point_id() {
                return chapter_test_point_id;
            }

            public void setChapter_test_point_id(int chapter_test_point_id) {
                this.chapter_test_point_id = chapter_test_point_id;
            }

            public int getIbs_id() {
                return ibs_id;
            }

            public void setIbs_id(int ibs_id) {
                this.ibs_id = ibs_id;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getFather_id() {
                return father_id;
            }

            public void setFather_id(int father_id) {
                this.father_id = father_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<JieBean> getJie() {
                return jie;
            }

            public void setJie(List<JieBean> jie) {
                this.jie = jie;
            }

            public static class JieBean {
                /**
                 * chapter_test_point_id : 6
                 * ibs_id : 1
                 * num : 39
                 * name : 节1333
                 * father_id : 1
                 * type : 2
                 */

                private int chapter_test_point_id;
                private int ibs_id;
                private int num;
                private String name;
                private int father_id;
                private int type;

                public int getChapter_test_point_id() {
                    return chapter_test_point_id;
                }

                public void setChapter_test_point_id(int chapter_test_point_id) {
                    this.chapter_test_point_id = chapter_test_point_id;
                }

                public int getIbs_id() {
                    return ibs_id;
                }

                public void setIbs_id(int ibs_id) {
                    this.ibs_id = ibs_id;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getFather_id() {
                    return father_id;
                }

                public void setFather_id(int father_id) {
                    this.father_id = father_id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }


    //我的题库----列表
    public static class MyQuestionBankBean {

    }

    //首页-----查看试卷
    public static class QuestionBankTestPaperBean {

    }

    //做题设置-查询题型错题和未做的题数
    public static class QuestionBankSeeting {

    }

    //首页-----题库列表(包括子题库)
    public static class QuestionBankBean {
        /**
         * msg : 查询成功
         * code : 200
         * data : [{"item_bank_id":1,"item_bank_name":"题库名22","icon":"./assets/images/tiku9.svg","brief_introduction":"这是题库一","sub_library":[{"ibs_id":1,"ibs_name":"name222"},{"ibs_id":4,"ibs_name":"科目四"},{"ibs_id":7,"ibs_name":"name1"},{"ibs_id":8,"ibs_name":"name2"},{"ibs_id":9,"ibs_name":"name3"},{"ibs_id":10,"ibs_name":"name4"},{"ibs_id":11,"ibs_name":"name5"},{"ibs_id":12,"ibs_name":"name6"},{"ibs_id":13,"ibs_name":"name72"},{"ibs_id":17,"ibs_name":"222"},{"ibs_id":18,"ibs_name":"1334"},{"ibs_id":19,"ibs_name":"江山111"},{"ibs_id":20,"ibs_name":"test11"}]},{"item_bank_id":2,"item_bank_name":"题库二","icon":"./assets/images/tiku9.svg","brief_introduction":"这是题库二","sub_library":[{"ibs_id":2,"ibs_name":"科目二"}]},{"item_bank_id":3,"item_bank_name":"题库三","icon":"./assets/images/tiku7.svg","brief_introduction":"这是题库三","sub_library":[{"ibs_id":3,"ibs_name":"科目三"},{"ibs_id":16,"ibs_name":"江山"}]},{"item_bank_id":6,"item_bank_name":"www","icon":"./assets/images/tiku9.svg","brief_introduction":"www","sub_library":[]}]
         */
        private String msg;
        private int code;
        private List<DataBean> data;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * item_bank_id : 1
             * item_bank_name : 题库名22
             * icon : ./assets/images/tiku9.svg
             * brief_introduction : 这是题库一
             * sub_library : [{"ibs_id":1,"ibs_name":"name222"},{"ibs_id":4,"ibs_name":"科目四"},{"ibs_id":7,"ibs_name":"name1"},{"ibs_id":8,"ibs_name":"name2"},{"ibs_id":9,"ibs_name":"name3"},{"ibs_id":10,"ibs_name":"name4"},{"ibs_id":11,"ibs_name":"name5"},{"ibs_id":12,"ibs_name":"name6"},{"ibs_id":13,"ibs_name":"name72"},{"ibs_id":17,"ibs_name":"222"},{"ibs_id":18,"ibs_name":"1334"},{"ibs_id":19,"ibs_name":"江山111"},{"ibs_id":20,"ibs_name":"test11"}]
             */

            private int item_bank_id;
            private String item_bank_name;
            private String icon;
            private String brief_introduction;
            private List<SubLibraryBean> sub_library;

            public int getItem_bank_id() {
                return item_bank_id;
            }

            public void setItem_bank_id(int item_bank_id) {
                this.item_bank_id = item_bank_id;
            }

            public String getItem_bank_name() {
                return item_bank_name;
            }

            public void setItem_bank_name(String item_bank_name) {
                this.item_bank_name = item_bank_name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getBrief_introduction() {
                return brief_introduction;
            }

            public void setBrief_introduction(String brief_introduction) {
                this.brief_introduction = brief_introduction;
            }

            public List<SubLibraryBean> getSub_library() {
                return sub_library;
            }

            public void setSub_library(List<SubLibraryBean> sub_library) {
                this.sub_library = sub_library;
            }

            public static class SubLibraryBean {
                /**
                 * ibs_id : 1
                 * ibs_name : name222
                 */

                private int ibs_id;
                private String ibs_name;

                public int getIbs_id() {
                    return ibs_id;
                }

                public void setIbs_id(int ibs_id) {
                    this.ibs_id = ibs_id;
                }

                public String getIbs_name() {
                    return ibs_name;
                }

                public void setIbs_name(String ibs_name) {
                    this.ibs_name = ibs_name;
                }
            }
        }
    }

    //判断当前任务是否继续
    public static class MyQuestionBankGoonBean {

    }
     //题库-做题记录Bean
    public static class QuestionBankAnswerRecordBean{

     }
     //题库  错题本Bean
    public static class QuestionBankSweeperBean{

     }
     //题库 错题本
     public void getQuestionBankSweeper(){
         Retrofit retrofit = new Retrofit.Builder()
                 .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(ModelObservableInterface.urlHead)
                 .build();
         ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
         Gson gson = new Gson();
         HashMap<String, String> paramsMap = new HashMap<>();
         paramsMap.put("ibs_id", "1");//	答题卡参数1
         paramsMap.put("ibs_id", "2");//	答题卡参数2
         paramsMap.put("ibs_id", "3");//	答题卡参数3
         String strEntity = gson.toJson(paramsMap);
         RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
         //queryQuestionBankSweeper
         queryMyCourseList.queryQuestionBankSweeper(body)
                 .enqueue(new Callback<QuestionBankSweeperBean>() {
                     @Override
                     public void onResponse(Call<QuestionBankSweeperBean> call, Response<QuestionBankSweeperBean> response) {
                         QuestionBankSweeperBean bean = response.body();
                         if (bean!=null){

                         }
                     }

                     @Override
                     public void onFailure(Call<QuestionBankSweeperBean> call, Throwable t) {
                         Log.e(TAG, "onFailure: "+t.getMessage());
                     }
                 });

     }


     //题库-做题记录
    public void  getQuestionBankAnswerRecord(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ibs_id", "1");//做题记录的参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryQuestionBankAnswerRecord(body)
                .enqueue(new Callback<QuestionBankAnswerRecordBean>() {
                    @Override
                    public void onResponse(Call<QuestionBankAnswerRecordBean> call, Response<QuestionBankAnswerRecordBean> response) {
                        QuestionBankAnswerRecordBean recordBean = response.body();
                        if (recordBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionBankAnswerRecordBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    //题库-答题卡Bean
    public static class QuestionBankAnswerSheetBean {

    }

    //题库-答题卡
    public void getQuestionBankAnswerSheet() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ibs_id", "1");//	答题卡参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryQuestionBankAnswerSheet(body)
                .enqueue(new Callback<QuestionBankAnswerSheetBean>() {
                    @Override
                    public void onResponse(Call<QuestionBankAnswerSheetBean> call, Response<QuestionBankAnswerSheetBean> response) {
                        QuestionBankAnswerSheetBean body1 = response.body();
                        if (body1 != null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionBankAnswerSheetBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    //判断当前的任务是否继续
    public void getMyQuestionBankGoon() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ibs_id", "1");//	子题库的id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankGoon(body)
                .enqueue(new Callback<MyQuestionBankGoonBean>() {
                    @Override
                    public void onResponse(Call<MyQuestionBankGoonBean> call, Response<MyQuestionBankGoonBean> response) {
                        MyQuestionBankGoonBean bankGoonBean = response.body();
                        if (bankGoonBean != null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<MyQuestionBankGoonBean> call, Throwable t) {

                    }
                });
    }
    public static class QuestionBankMyFavoriteQuestionBean{

    }


     //题库  我的收藏题
    public void getQuestionBankMyFavoriteQuestion(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ModelObservableInterface.urlHead)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ibs_id", "1");//	子题库的id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        modelObservableInterface.queryQuestionBankMyFavoriteQuestion(body)
                .enqueue(new Callback<QuestionBankMyFavoriteQuestionBean>() {
                    @Override
                    public void onResponse(Call<QuestionBankMyFavoriteQuestionBean> call, Response<QuestionBankMyFavoriteQuestionBean> response) {
                        QuestionBankMyFavoriteQuestionBean body1 = response.body();
                        if (body1!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionBankMyFavoriteQuestionBean> call, Throwable t) {
                        Log.e(TAG, "ModelQuestionBank”s Failure: "+t.getMessage());
                    }
                });
    }
    //queryMyQuestionBankChapterTest 题库章节考点
    public void getMyQuestionBankChapterTest() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ibs_id", "1");//	子题库的id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankChapterTest(body)
                .enqueue(new Callback<MyQuestionBankChapterTestBean>() {
                    //章
                    private int chapter_test_point_id;
                    private int chapter_father_id;
                    private int chapter_ibs_id;
                    private String chapter_name;
                    private int chapter_num;
                    private int chapter_type;
                    //节
                    private int jie_type;
                    private int jie_num;
                    private String jie_name;
                    private int jie_ibs_id;
                    private int jie_father_id;
                    private int jie_test_point_id;

                    @Override
                    public void onResponse(Call<MyQuestionBankChapterTestBean> call, Response<MyQuestionBankChapterTestBean> response) {
                        MyQuestionBankChapterTestBean chapterTestBean = response.body();
                        if (chapterTestBean != null) {
                            int code = chapterTestBean.getCode();
                            if (code == 200) {
                                String msg = chapterTestBean.getMsg();
                                Log.d(TAG, "onResponse: " + msg);
                                List<MyQuestionBankChapterTestBean.DataBean> data = chapterTestBean.getData();
                                for (int i = 0; i < data.size(); i++) {
                                    chapter_test_point_id = data.get(i).getChapter_test_point_id();   //	章id
                                    chapter_father_id = data.get(i).getFather_id();  //父id
                                    chapter_ibs_id = data.get(i).getIbs_id();    //子题库id
                                    chapter_name = data.get(i).getName();   //	章名称
                                    chapter_num = data.get(i).getNum(); //	题数量
                                    chapter_type = data.get(i).getType(); //类型(章 1 节 2 考点 3)
                                    initChater(); //章赋值

                                    List<MyQuestionBankChapterTestBean.DataBean.JieBean> jie = data.get(i).getJie();
                                    for (int j = 0; j < data.size(); j++) {
                                        jie_test_point_id = jie.get(j).getChapter_test_point_id();  //节id
                                        jie_father_id = jie.get(j).getFather_id();//	父id
                                        jie_ibs_id = jie.get(j).getIbs_id(); //	子题库id
                                        jie_name = jie.get(j).getName();//	节名称
                                        jie_num = jie.get(j).getNum(); //题数量
                                        jie_type = jie.get(j).getType();//	类型(章 1 节 2 考点 3)
                                        //节赋值
                                        mCurrentChapterName=jie_name;
                                    }
                                }
                            }
                        }
                    }

                    private void initChater() {
                        //ModelExpandListView展开列表   章节练习网络请求
                        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_chapterexercises, null);
                        //基金法律法规
                        TextView questionbank_sub_details_chapterexercises_name = view.findViewById(R.id.questionbank_sub_details_chapterexercises_name);
                        questionbank_sub_details_chapterexercises_name.setText(chapter_name);  //章名字
                        questionbank_sub_details_chapterexercises_name.setHint(chapter_test_point_id);//章id

                        //默认全部展开
                        ImageView questionbank_sub_details_chapterexercises_arrow_right = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_right);
                        //显示章 展开下面的节或考点
                        ImageView questionbank_sub_details_chapterexercises_arrow_down = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_down);
                        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
                        ll.width = 0;
                        questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll);
                        //更多
                        ModelExpandView questionbank_sub_details_chapterexercises_expandView = view.findViewById(R.id.questionbank_sub_details_chapterexercises_expandView);
                        questionbank_sub_details_chapterexercises_arrow_right.setClickable(true);
                        questionbank_sub_details_chapterexercises_arrow_down.setClickable(true);
                        // //显示章 展开下面的节或考点    节或者考点的id
                        QuestionBankDetailsChapterExerisesShow(view, mIbs_id, String.valueOf(jie_test_point_id));
                        questionbank_sub_details_chapterexercises_arrow_down.setOnClickListener(v -> {
                            // TODO Auto-generated method stub
                            if (questionbank_sub_details_chapterexercises_expandView.isExpand()) {
                                questionbank_sub_details_chapterexercises_expandView.collapse();
                                //收缩隐藏布局
                                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_sub_details_chapterexercises_expandView.getLayoutParams();
                                rl.height = 0;
                                questionbank_sub_details_chapterexercises_expandView.setLayoutParams(rl);
                                questionbank_sub_details_chapterexercises_expandView.setVisibility(View.INVISIBLE);
                                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
                                ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp6);
                                questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll1);
                                ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_down.getLayoutParams();
                                ll1.width = 0;
                                questionbank_sub_details_chapterexercises_arrow_down.setLayoutParams(ll1);
                            } else {
                                //题库的详情
                                QuestionBankDetailsChapterExerisesShow(view, mIbs_id, String.valueOf(chapter_test_point_id));
                            }
                        });
                        questionbank_sub_details_chapterexercises_arrow_right.setOnClickListener(v -> {
                            // TODO Auto-generated method stub
                            if (questionbank_sub_details_chapterexercises_expandView.isExpand()) {
                                questionbank_sub_details_chapterexercises_expandView.collapse();
                                //收缩隐藏布局
                                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_sub_details_chapterexercises_expandView.getLayoutParams();
                                rl.height = 0;
                                questionbank_sub_details_chapterexercises_expandView.setLayoutParams(rl);
                                questionbank_sub_details_chapterexercises_expandView.setVisibility(View.INVISIBLE);
                                LinearLayout.LayoutParams ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
                                ll1.width = view.getResources().getDimensionPixelSize(R.dimen.dp6);
                                questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll1);
                                ll1 = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_down.getLayoutParams();
                                ll1.width = 0;
                                questionbank_sub_details_chapterexercises_arrow_down.setLayoutParams(ll1);
                            } else {
                                QuestionBankDetailsChapterExerisesShow(view, mIbs_id, String.valueOf(chapter_test_point_id));
                            }
                        });
                        //点击章名称，进行章抽题
                        questionbank_sub_details_chapterexercises_name.setClickable(true);
                        questionbank_sub_details_chapterexercises_name.setOnClickListener(v -> {
                            //初始化做题设置界面并展示
                            QuestionBankQuestionSettingShow("chapter", "id");
                        });
                        questionbank_sub_details_content.addView(view);
                    }

                    @Override
                    public void onFailure(Call<MyQuestionBankChapterTestBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }


    //MyQuestionBankflag       题库标记和取消标记
    public void getMyQuestionBankflag() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", "1");//题库参数1
        paramsMap.put("pageSize", "2");//题库参数2
        paramsMap.put("stu_id", "3");//题库参数3
        paramsMap.put("type", "4");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankflag(body)
                .enqueue(new Callback<MyQuestionBankflag>() {
                    @Override
                    public void onResponse(Call<MyQuestionBankflag> call, Response<MyQuestionBankflag> response) {
                        MyQuestionBankflag bankflag = response.body();
                        if (bankflag != null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<MyQuestionBankflag> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }


    //题库收藏和取消收藏
    public void getMyQuestionBankCollection() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("stu_id", "1");//第几页
        paramsMap.put("question_id", "2");//问题id
        paramsMap.put("tf_collection", "3");//是否收藏 (1是,2否)
        paramsMap.put("tf_marked", "4");//是否标记 (1是,2否)
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankCollection(body)
                .enqueue(new Callback<MyQuestionBankCollection>() {
                    @Override
                    public void onResponse(Call<MyQuestionBankCollection> call, Response<MyQuestionBankCollection> response) {
                        MyQuestionBankCollection collection = response.body();
                        if (collection != null) {
                            int code = collection.getCode();
                            String msg = collection.getMsg();
                            if (code == 200) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();


                            } else if (code == 209) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), "code错误" + code, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyQuestionBankCollection> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    //题库 交卷接口
    public static class QuestionBankHandInBean {
        /**
         * msg : 提交记录成功
         * code : 200
         */

        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    //题库 交卷接口
    public void getQuestionBankHandInBean() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("answer_id", "记录的唯一id");//第几页
        paramsMap.put("error_num", "错题记录所有的都传");//每页几条
        paramsMap.put("score", "得分");//学生id
        paramsMap.put("state", "1 完成");
        paramsMap.put("used_answer_time", "用时");
        paramsMap.put("stu_id", "学生ID");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankHandIn(body)
                .enqueue(new Callback<QuestionBankHandInBean>() {
                    @Override
                    public void onResponse(Call<QuestionBankHandInBean> call, Response<QuestionBankHandInBean> response) {
                        QuestionBankHandInBean handInBean = response.body();
                        if (handInBean != null) {
                            int code = handInBean.getCode();
                            if (code == 200) {
                                String msg = handInBean.getMsg();
                            } else if (code == 215) {
                                String msg = handInBean.getMsg();
                            } else {
                                String msg = handInBean.getMsg();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionBankHandInBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }


    public static class QuestionBankstatesBean {

    }


    //题库 暂停或者继续做题
    public void getQuestionBankstates() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", "1");//第几页
        paramsMap.put("pageSize", "2");//每页几条
        paramsMap.put("stu_id", "3");//学生id
        paramsMap.put("type", "4");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankstatus(body)
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Object body1 = response.body();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }

    //题库-出题   是不是练习模式Bean类
    public static class MyQuestionBankExercises {

    }


    //题库-出题   是不是练习模式
    public void getMyQuestionBankExercises() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", "1");//第几页
        paramsMap.put("pageSize", "2");//每页几条
        paramsMap.put("stu_id", "3");//学生id
        paramsMap.put("type", "4");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankExercises(body)
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Object body1 = response.body();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
    }
    //题库  点击继续和取消做题


    //题库----查看试卷
    public void getQuestionBankTestPaper() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", "1");//第几页
        paramsMap.put("pageSize", "2");//每页几条
        paramsMap.put("stu_id", "3");//学生id
        paramsMap.put("type", "4");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        //queryMyQuestionBankTestPaper
        queryMyCourseList.queryMyQuestionBankTestPaper(body)
                .enqueue(new Callback<QuestionBankTestPaperBean>() {
                    @Override
                    public void onResponse(Call<QuestionBankTestPaperBean> call, Response<QuestionBankTestPaperBean> response) {
                        QuestionBankTestPaperBean testPaperBean = response.body();
                        if (testPaperBean != null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionBankTestPaperBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    //我的题库----题库列表   网络请求
    public void MyQuestionBankBeanList() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", "1");//第几页
        paramsMap.put("pageSize", "2");//每页几条
        paramsMap.put("stu_id", "3");//学生id
        paramsMap.put("type", "4");  //我的题库参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionBankList(body)
                .enqueue(new Callback<MyQuestionBankBean>() {
                    @Override
                    public void onResponse(Call<MyQuestionBankBean> call, Response<MyQuestionBankBean> response) {
                        MyQuestionBankBean bankBean = response.body();
                        if (bankBean != null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<MyQuestionBankBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }


    //首页-----题库列表(包括子题库)
    public void getQuestionBankBeanList() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("stu_id", "3");//学生id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryQuestionBankList(body)
                .enqueue(new Callback<QuestionBankBean>() {
                    private String ibs_name;
                    private int ibs_id;
                    private View item_bank_view;
                    private String brief_introduction;
                    private String icon;
                    private String item_bank_name;
                    private int item_bank_id;

                    @Override
                    public void onResponse(Call<QuestionBankBean> call, Response<QuestionBankBean> response) {
                        QuestionBankBean questionBankBean = response.body();
                        if (questionBankBean != null) {
                            String msg = questionBankBean.getMsg();
                            int code = questionBankBean.getCode();
                            if (code == 200) {
                                List<QuestionBankBean.DataBean> beanList = questionBankBean.getData();
                                for (int i = 0; i < beanList.size(); i++) {
                                    item_bank_id = beanList.get(i).getItem_bank_id();      //题库的id
                                    item_bank_name = beanList.get(i).getItem_bank_name();  //题库的name
                                    icon = beanList.get(i).getIcon();  //题库的图片链接
                                    brief_introduction = beanList.get(i).getBrief_introduction();  //	详情描述
                                    //题库界面赋值
                                    initDataList();
                                    //子题库的数据
                                    List<QuestionBankBean.DataBean.SubLibraryBean> sub_library = beanList.get(i).getSub_library();
                                    for (int j = 0; j < sub_library.size(); j++) {
                                        ibs_id = sub_library.get(j).getIbs_id(); //子题库的id
                                        ibs_name = sub_library.get(j).getIbs_name(); //子题库的名字
                                        //子题库的数据
                                        initDataList2();
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "onResponse: " + questionBankBean.getCode());
                        }
                    }

                    //标签列表
                    private void initDataList2() {
                        GridLayout modelquestionbank_mainquestionbank_list = item_bank_view.findViewById(R.id.modelquestionbank_mainquestionbank_list);
                        modelquestionbank_mainquestionbank_list.removeAllViews();
                        View item_bank_view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1_1, null);
                        TextView modelquestionbank_subquestionbank1 = item_bank_view1.findViewById(R.id.modelquestionbank_subquestionbank1);
                        modelquestionbank_subquestionbank1.setHint(ibs_id);
                        modelquestionbank_subquestionbank1.setText(ibs_name);
                        modelquestionbank_mainquestionbank_list.addView(item_bank_view1);
                        //判断题库是否有做题权限，如果没有不可点击颜色变灰
                        if (!ibs_name.equals(item_bank_name)) {
                            modelquestionbank_subquestionbank1.setClickable(true);
                            modelquestionbank_subquestionbank1.setOnClickListener(v -> {
                                //传入相关的id和name
                                QuestionBankDetailsShow(String.valueOf(ibs_id), ibs_name); //详情的显示
                            });
                            modelquestionbank_subquestionbank1.setTextColor(item_bank_view1.getResources().getColor(R.color.collectdefaultcolor));
                        } else {
                            modelquestionbank_subquestionbank1.setTextColor(item_bank_view1.getResources().getColor(R.color.black999999));
                        }
                        questionbank_main_content.addView(item_bank_view1);
                    }

                    private void initDataList() {
                        //题库子条目标签
                        item_bank_view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1, null);
                        //题库子条目id
                        TextView modelquestionbank_mainquestionbank_id = item_bank_view.findViewById(R.id.modelquestionbank_mainquestionbank_id);
                        modelquestionbank_mainquestionbank_id.setText(item_bank_id);
                        //题库子条目的标题
                        TextView modelquestionbank_mainquestionbank_name = item_bank_view.findViewById(R.id.modelquestionbank_mainquestionbank_name);
                        modelquestionbank_mainquestionbank_name.setText(item_bank_name);
                        //题库子条目的描述
                        TextView modelquestionbank_mainquestionbank_describ = item_bank_view.findViewById(R.id.modelquestionbank_mainquestionbank_describ);
                        modelquestionbank_mainquestionbank_describ.setText(brief_introduction);
                        //题库点击更多
                        LinearLayout modelquestionbank_mainquestionbank_more = item_bank_view.findViewById(R.id.modelquestionbank_mainquestionbank_more);
                        modelquestionbank_mainquestionbank_more.setClickable(true);
                        modelquestionbank_mainquestionbank_more.setOnClickListener(v -> {
                            //题库列表详情   将id name
                            QuestionBankMainMoreShow(String.valueOf(item_bank_id), item_bank_name, brief_introduction);
                        });

                    }

                    @Override
                    public void onFailure(Call<QuestionBankBean> call, Throwable t) {
                        Log.e(TAG, "onFail我的错误是+" + t.getMessage());
                    }

                });
    }
}
