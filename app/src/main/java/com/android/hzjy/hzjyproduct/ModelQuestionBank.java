package com.android.hzjy.hzjyproduct;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
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

import com.android.hzjy.hzjyproduct.view.FullScreenInputBarView;

import net.sqlcipher.Cursor;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.talkfun.utils.HandlerUtil.runOnUiThread;

public class ModelQuestionBank extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int height = 1344;
    private int width = 720;
    private String mCurrentTab = "ChapterExercises",mQuestionRecordCurrentTab = "ChapterExercises";
    private int mLastTabIndex = 1,mQuestionRecordLastTabIndex = 1;
    private View mModelQuestionBankView = null,mModelQuestionBankDetailsView = null,mModelQuestionBankSettingView = null,
            mModelQuestionBankAnswerPaperView = null,mModelQuestionBankAnswerQuestionCardView = null,mModelQuestionBankHandInView = null
            ,mModelQuestionBankHandInAnalysisView = null,mModelQuestionBankWrongQuestionView = null,mModelQuestionBankMyCollectionQuestionView = null
            ,mModelQuestionBankQuestionTypeView = null,mModelQuestionBankQuestionRecordView = null;
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
    private PopupWindow mPopupWindow,mPointoutPopupWindow;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
    private ModelAnimUtil animUtil,mPointoutAnimUtil;
    private float bgAlpha = 1f,bgPointoutAlpha = 1f;
    private boolean bright = false,bPointoutRight = false;
    private boolean mIsSign = false;  //此题是否被标记
    private String mFontSize = "nomal"; //当前界面的字号大小
    private String mCurrentChapterName = "";//当前选中的章或节的名称
    private int mCurrentIndex = 0;//当前显示的题索引
    private String mCurrentAnswerMode = "test";//当前做题模式
    private boolean mIsCollect = false;  //此题是否被收藏
    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }
    private Timer mTimer2 = null;
    private TimerTask mTask2 = null;
    private int mTime = 0;

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelQuestionBank myFragment = new ModelQuestionBank();
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
        QuestionBankMainShow(mContext);
        ModelPtrFrameLayout questionbank_main_content_frame = mview.findViewById(R.id.questionbank_main_content_frame);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        questionbank_main_content_frame.addPtrUIHandler(header);
        questionbank_main_content_frame.setHeaderView(header);
        questionbank_main_content_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                questionbank_main_content_frame.refreshComplete();
            }
        });
        //让布局向上移来显示软键盘
        mControlMainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
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
        TextView questionbank_main_titletext = mModelQuestionBankView.findViewById(R.id.questionbank_main_titletext);
        if (context.contains("我的题库")){//如果是我的题库，显示的题库只有我购买所有课程所包含的项目和科目
            questionbank_main_titletext.setText("我的题库");
        } else {
            questionbank_main_titletext.setText("题库");
        }
        mIbs_id = "";
        mCurrentTab = "ChapterExercises";
        //题库列表
        ScrollView questionbank_main_content_scroll_view = mModelQuestionBankView.findViewById(R.id.questionbank_main_content_scroll_view);
        questionbank_main_content_scroll_view.scrollTo(0,0);
//        填写假数据
        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("REPLACE INTO `answer_edu` \n" +
                "(`answer_id`,`test_paper_id`,`student_id`,`time`,`type`,`question_num`,`score`,`error_num`,`state`,`tf_delete`,`used_answer_time`)\n" +
                "VALUES \n" +
                "('1', '1', '1', '2019-11-06 00:00:00', '1', '50', '50', '#14#ABC;', '1', '2', null),\n" +
                "('2', '2', '1', '2019-11-06 11:33:49', '2', '60', '60', '#14#ABC;', '2', '2', '19'),\n" +
                "('3', '3', '2', '2019-11-06 11:35:26', '3', '15', '75', '#14#ABC;', '1', '2', null),\n" +
                "('4', '1', '1', '2019-11-06 00:00:00', '1', '50', '50', '#14#ABC;', '1', '2', '30'),\n" +
                "('5', '2', '1', '2019-11-06 00:00:00', '1', '50', '50', '#14#ABC;', '1', '2', '100'),\n" +
                "('6', '2', '1', '2019-11-06 11:33:49', '2', '60', '60', '#14#ABC;', '2', '2', '19'),\n" +
                "('7', '3', '2', '2019-11-06 11:35:26', '3', '15', '75', '#14#ABC;', '1', '2', '100');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `chapter_test_point_edu` \n" +
//                "(`ibs_id`,`name`,`tf_delete`,`creation_time`,`operator_id`,`type`,`father_id`)\n" +
//                "VALUES \n" +
//                "( '1', '章1', '2', '2019-11-06 10:57:52', '1', '1', null),\n" +
//                "( '1', '章2', '2', '2019-11-06 10:58:29', '1', '1', null),\n" +
//                "( '1', '章3', '2', '2019-11-06 10:59:14', '1', '1', null),\n" +
//                "( '1', '节1', '2', '2019-11-06 11:00:16', '1', '2', '1'),\n" +
//                "( '1', '节2', '2', '2019-11-06 11:00:22', '1', '2', '5'),\n" +
//                "( '1', '节3', '2', '2019-11-06 11:00:24', '1', '2', '6'),\n" +
//                "( '1', '考点1', '2', '2019-11-06 11:01:46', '1', '3', '7'),\n" +
//                "( '1', '考点2', '2', '2019-11-06 11:01:49', '1', '3', '8'),\n" +
//                "( '1', '考点3', '2', '2019-11-06 11:01:51', '1', '3', '9'),\n" +
//                "( '1', '考点4', '2', '2019-11-06 11:02:52', '1', '3', '7'),\n" +
//                "( '5', '章1', '2', '2019-11-06 10:57:52', '1', '1', null),\n" +
//                "( '5', '章2', '2', '2019-11-06 10:58:29', '1', '1', null),\n" +
//                "( '7', '章3', '2', '2019-11-06 10:59:14', '1', '1', null),\n" +
//                "( '5', '节1', '2', '2019-11-06 11:00:16', '1', '2', '11'),\n" +
//                "( '5', '节2', '2', '2019-11-06 11:00:22', '1', '2', '11'),\n" +
//                "( '7', '节3', '2', '2019-11-06 11:00:24', '1', '2', '12'),\n" +
//                "( '7', '考点1', '2', '2019-11-06 11:01:46', '1', '3', '14'),\n" +
//                "( '5', '考点2', '2', '2019-11-06 11:01:49', '1', '3', '14');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `item_bank_edu` \n" +
//                "(`item_bank_name`,`brief_introduction`,`tf_enable`,`tf_delete`,`icon`,`project_id`,`subject_id`,`creation_time`,`founder_id`)\n" +
//                "VALUES \n" +
//                "('题库一', '这是题库一', '2', '2', '/1/1.jpg', '1', '1', '2019-11-06 10:50:46', '1'),\n" +
//                "('题库二', '这是题库二', '1', '2', '/2/2.jpg', '1', '1', '2019-11-06 10:51:38', '1'),\n" +
//                "('题库三', '这是题库三', '1', '1', '/3/3.jpg', '1', '1', '2019-11-06 10:52:34', '1'),\n" +
//                "('题库四', '这是题库四', '1', '2', '/1/1.jpg', '1', '1', '2019-11-06 10:50:46', '1'),\n" +
//                "('题库五', '这是题库五', '1', '2', '/1/1.jpg', '1', '1', '2019-11-06 10:50:46', '1'),\n" +
//                "('题库六', '这是题库六', '1', '2', '/1/1.jpg', '1', '1', '2019-11-06 10:50:46', '1');");
////        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `stu_un_answer_edu` \n" +
////                "(`student_id`,`question_id`,`student_answers`,`tf_delete`,`time`)\n" +
////                "VALUES \n" +
////                "('1', '1', '#A', '2', '2019-11-06 11:37:21'),\n" +
////                "('1', '2', '#A#B', '2', '2019-11-06 11:38:38'),\n" +
////                "('1', '3', 'qweqeqweqe', '2', '2019-11-06 11:38:40');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `student_question_edu` \n" +
//                "(`student_id`,`question_id`,`time`,`wrong_answer`,`tf_delete`,`tf_collection`,`tf_wrong`,`tf_marked`)\n" +
//                "VALUES \n" +
//                "('1', '1', '2019-11-06 11:27:06', '#C', '2', '2', '1', '2'),\n" +
//                "('1', '2', '2019-11-06 11:29:06', '#A#B#C#D', '2', '1', '1', '1'),\n" +
//                "('1', '3', '2019-11-06 11:30:08', '简答题错题', '2', '2', '2', '1'),\n" +
//                "('1', '4', '2019-11-06 11:30:55', '#1#2#4', '2', '1', '1', '2');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `sub_library_edu` \n" +
//                "(`ibs_name`,`tf_delete`,`item_bank_id`,`creation_time`,`operator_id`)\n" +
//                "VALUES \n" +
//                "( '科目一', '2', '1', '2019-11-06 10:54:23', '1'),\n" +
//                "( '科目二', '2', '2', '2019-11-06 10:54:26', '1'),\n" +
//                "( '科目三', '1', '3', '2019-11-06 10:54:28', '1'),\n" +
//                "( '科目四', '1', '1', '2019-11-06 10:54:30', '1'),\n" +
//                "( '科目五', '2', '2', '2019-11-06 10:54:26', '1'),\n" +
//                "( '科目六', '1', '3', '2019-11-06 10:54:28', '1'),\n" +
//                "( '科目七', '2', '5', '2019-11-06 10:54:30', '1'),\n" +
//                "( '科目八', '2', '2', '2019-11-06 10:54:26', '1'),\n" +
//                "( '科目九', '2', '2', '2019-11-06 10:54:26', '1');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO `test_paper_edu` \n" +
//                "(`test_paper_name`,`test_paper_type`,`answer_time`,`total_score`,`area`,`question_type_score`,\n" +
//                "`state`,`ibs_id`,`creation_time`,`founder_id`,`auditor`,`tf_delete`,`audit_time`,`question_id_group`,`tf_temporary`)\n" +
//                "VALUES \n" +
//                "( '试卷一', '1', '120', '100.00', '北京', '#单选题#5.00;#多选题#10.00;#简答题#30.00;材料题#30.00;',\n" +
//                " '1', '1', '2019-11-06 11:23:42', '1', '2', '2', '2019-11-06 11:24:22', '#1#2#3#4', '2'),\n" +
//                "( '试卷二', '2', '90', '100.00', '上海', '#单选题#5.00;#多选题#10.00;#简答题#30.00;材料题#30.00;', \n" +
//                "'2', '1', '2019-11-06 11:23:45', '1', '2', '2', '2019-11-06 11:24:24', '#2#1#3#4', '1'),\n" +
//                "( '试卷三', '3', '150', '100.00', '广州', '#单选题#5.00;#多选题#10.00;#简答题#30.00;材料题#30.00;',\n" +
//                " '3', '1', '2019-11-06 11:23:46', '1', '2', '2', '2019-11-06 11:24:26', '#1#2#3#4', '2'),\n" +
//                "( '试卷四', '4', '220', '100.00', '深圳', '#单选题#5.00;#多选题#10.00;#简答题#30.00;材料题#30.00;',\n" +
//                " '4', '1', '2019-11-06 11:23:49', '1', '2', '2', '2019-11-06 11:24:29', '#2#1#3#4', '1'),\n" +
//                "( '试卷五', '4', '220', '100.00', '深圳', '#单选题#5.00;#多选题#10.00;#简答题#30.00;材料题#30.00;',\n" +
//                " '4', '5', '2019-11-06 11:23:49', '1', '2', '2', '2019-11-06 11:24:29', '#2#3#4#5#7#9#11#13', '1');");
//        ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("INSERT INTO \n" +
//                "`test_questions_edu` (`question_name`,`optionanswer`,`question_type`,`question_analysis`,\n" +
//                "`audio_analysis`,`video_analysis`,`question_sub_id`,`state`,`creation_time`,`founder_id`,`auditor_id`,`tf_delete`,`difficulty`,\n" +
//                "`chapter_id`,`section_id`,`examination_site_id`,`ibs_id`)\n" +
//                "VALUES \n" +
//                "( '单选题', '#A#是#选择A;#B#否#选择B;#C#否#选择C;', '1', '这题选A', '/1/1.wav', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:01', '1', '2', '2', '1', '1', null, null, '1'),\n" +
//                "( '单选题1', '#A#是#选择A;#B#否#选择B;#C#否#选择C;', '1', '这题选A', '/1/1.wav', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:01', '1', '2', '2', '1', '11', null, null, '5'),\n" +
//                "( '单选题11', '#A#是#选择A;#B#否#选择B;#C#否#选择C;', '1', '这题选A', '/1/1.wav', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:01', '1', '2', '2', '1', '11', null, null, '5'),\n" +
//                "( '单选题111', '#A#是#选择A;#B#否#选择B;#C#否#选择C;', '1', '这题选A', '/1/1.wav', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:01', '1', '2', '2', '1', '15', '11', null, '5'),\n" +
//                "( '单选题1111', '#A#是#选择A;#B#否#选择B;#C#否#选择C;', '1', '这题选A', '/1/1.wav', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:01', '1', '2', '2', '1', '15', '11', null, '5'),\n" +
//                "( '多选题', '#A#是#选择A;#B#是#选择B;#C#否#选择C;', '2', '这题选AB', '/2/2.mp3', '/2/4.fl', null,\n" +
//                " '3', '2019-11-06 11:15:03', '1', '2', '2', '2', '1', '2', null, '1'),\n" +
//                "( '多选题', '#A#是#选择A;#B#是#选择B;#C#否#选择C;', '2', '这题选AB', '/2/2.mp3', '/2/4.fl', null,\n" +
//                " '3', '2019-11-06 11:15:03', '1', '2', '2', '2', '11', '2', null, '5'),\n" +
//                "( '简答题', '简答 题直接写答案', '4', '解答文本', '/1/3.avi', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:05', '1', '2', '2', '3', '1', '2', '3', '1'),\n" +
//                "( '简答题111111', '简答 题直接写答案', '4', '解答文本', '/1/3.avi', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:05', '1', '2', '2', '3', '11', null, '3', '5'),\n" +
//                "( '简答题111', '简答 题直接写答案', '4', '解答文本', '/1/3.avi', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:05', '1', '2', '2', '3', '11', null, '3', '5'),\n" +
//                "( '简答题1', '简答 题直接写答案', '4', '解答文本', '/1/3.avi', '/2/2.mp4', null, \n" +
//                "'3', '2019-11-06 11:15:05', '1', '2', '2', '3', '11', '15', null, '5'),\n" +
//                "( '材料题', null, '7', null, null, null, '#1#2#3', \n" +
//                "'3', '2019-11-06 11:15:08', '1', '2', '2', '1', null, null, null, '1'),\n" +
//                "( '材料题', null, '7', null, null, null, '#1#2#3', \n" +
//                "'3', '2019-11-06 11:15:08', '1', '2', '2', '1', '11', null, null, '5');");
        LinearLayout questionbank_main_content = mModelQuestionBankView.findViewById(R.id.questionbank_main_content);
        questionbank_main_content.removeAllViews();
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(  //查可用且没有被删除的数据库
                "select item_bank_id as _id,item_bank_name,brief_introduction from item_bank_edu where tf_enable=1 and tf_delete=2", null);
        boolean misEmpty = true;
        while (cursor.moveToNext()) {
            misEmpty = false;
            int item_bank_nameIndex = cursor.getColumnIndex("item_bank_name");
            int _idIndex = cursor.getColumnIndex("_id");
            int brief_introductionIndex = cursor.getColumnIndex("brief_introduction");
            String item_bank_name = cursor.getString(item_bank_nameIndex);
            String _id = cursor.getString(_idIndex);
            String brief_introduction = cursor.getString(brief_introductionIndex);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1, null);
            TextView modelquestionbank_mainquestionbank_id = view.findViewById(R.id.modelquestionbank_mainquestionbank_id);
            TextView modelquestionbank_mainquestionbank_name = view.findViewById(R.id.modelquestionbank_mainquestionbank_name);
            TextView modelquestionbank_mainquestionbank_describ = view.findViewById(R.id.modelquestionbank_mainquestionbank_describ);
            modelquestionbank_mainquestionbank_id.setText(_id);
            modelquestionbank_mainquestionbank_name.setText(item_bank_name);
            modelquestionbank_mainquestionbank_describ.setText(brief_introduction);
            LinearLayout modelquestionbank_mainquestionbank_more = view.findViewById(R.id.modelquestionbank_mainquestionbank_more);
            modelquestionbank_mainquestionbank_more.setClickable(true);
            modelquestionbank_mainquestionbank_more.setOnClickListener(v -> {
                QuestionBankMainMoreShow(_id,item_bank_name,brief_introduction);
            });
            GridLayout modelquestionbank_mainquestionbank_list = view.findViewById(R.id.modelquestionbank_mainquestionbank_list);
            modelquestionbank_mainquestionbank_list.removeAllViews();
            //查询子题库的名称
            Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery( //查没被删除的子题库，且只查两条
                    "select ibs_id,ibs_name from sub_library_edu where tf_delete=2 and item_bank_id=" + _id +" LIMIT 2", null);
            while (cursor1.moveToNext()) {
                int ibs_idIndex = cursor1.getColumnIndex("ibs_id");
                int ibs_nameIndex = cursor1.getColumnIndex("ibs_name");
                String ibs_id = cursor1.getString(ibs_idIndex);
                String ibs_name = cursor1.getString(ibs_nameIndex);
                View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1_1, null);
                TextView modelquestionbank_subquestionbank1 = view1.findViewById(R.id.modelquestionbank_subquestionbank1);
                modelquestionbank_subquestionbank1.setHint(ibs_id);
                modelquestionbank_subquestionbank1.setText(ibs_name);
                modelquestionbank_mainquestionbank_list.addView(view1);
                //判断题库是否有做题权限，如果没有不可点击颜色变灰
                if (!ibs_name.equals("科目二")) {
                    modelquestionbank_subquestionbank1.setClickable(true);
                    modelquestionbank_subquestionbank1.setOnClickListener(v->{
                        QuestionBankDetailsShow(ibs_id,ibs_name);
                    });
                    modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.collectdefaultcolor));
                } else {
                    modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.black999999));
                }
            }
            cursor1.close();
            questionbank_main_content.addView(view);
        }
        cursor.close();
        //如果没有数据，显示空界面
        LinearLayout questionbank_main_nodata = mModelQuestionBankView.findViewById(R.id.questionbank_main_nodata);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_main_nodata.getLayoutParams();
        rl.height = 0;
        questionbank_main_nodata.setLayoutParams(rl);
        if (misEmpty){
            questionbank_main_nodata.removeAllViews();
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_pointout, null);
            questionbank_main_nodata.addView(view);
            rl.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            questionbank_main_nodata.setLayoutParams(rl);
        }
    }

    public void QuestionBankMainMoreShow(String questionBankId,String item_bank_name,String brief_introduction) {
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
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1, null);
        TextView modelquestionbank_mainquestionbank_id = view.findViewById(R.id.modelquestionbank_mainquestionbank_id);
        TextView modelquestionbank_mainquestionbank_name = view.findViewById(R.id.modelquestionbank_mainquestionbank_name);
        TextView modelquestionbank_mainquestionbank_describ = view.findViewById(R.id.modelquestionbank_mainquestionbank_describ);
        modelquestionbank_mainquestionbank_id.setText(questionBankId);
        modelquestionbank_mainquestionbank_name.setText(item_bank_name);
        modelquestionbank_mainquestionbank_describ.setText(brief_introduction);
        LinearLayout modelquestionbank_mainquestionbank_more = view.findViewById(R.id.modelquestionbank_mainquestionbank_more);
        modelquestionbank_mainquestionbank_more.setVisibility(View.INVISIBLE);
        GridLayout modelquestionbank_mainquestionbank_list = view.findViewById(R.id.modelquestionbank_mainquestionbank_list);
        //查询子题库的名称
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery( //查没被删除的子题库
                "select ibs_id,ibs_name from sub_library_edu where tf_delete=2 and item_bank_id=" + questionBankId, null);
        while (cursor.moveToNext()) {
            int ibs_idIndex = cursor.getColumnIndex("ibs_id");
            int ibs_nameIndex = cursor.getColumnIndex("ibs_name");
            String ibs_id = cursor.getString(ibs_idIndex);
            String ibs_name = cursor.getString(ibs_nameIndex);
            View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_1_1, null);
            TextView modelquestionbank_subquestionbank1 = view1.findViewById(R.id.modelquestionbank_subquestionbank1);
            modelquestionbank_subquestionbank1.setHint(ibs_id);
            modelquestionbank_subquestionbank1.setText(ibs_name);
            modelquestionbank_mainquestionbank_list.addView(view1);
            //判断题库是否有做题权限，如果没有不可点击颜色变灰
            if (!ibs_name.equals("科目二")) {
                modelquestionbank_subquestionbank1.setClickable(true);
                modelquestionbank_subquestionbank1.setOnClickListener(v->{
                    QuestionBankDetailsShow(ibs_id,ibs_name);
                });
                modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.collectdefaultcolor));
            } else {
                modelquestionbank_subquestionbank1.setTextColor(view1.getResources().getColor(R.color.black999999));
            }
        }
        cursor.close();
        questionbank_main_content.addView(view);
    }

    public void QuestionBankDetailsShow(String ibs_id,String ibs_name) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankDetails();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankDetailsView == null) {
            mModelQuestionBankDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials, null);
            TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
            TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
            TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
            ImageView questionbank_sub_details_buttonmore = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_buttonmore);
            questionbank_sub_details_buttonmore.setClickable(true);
            questionbank_sub_details_buttonmore.setOnClickListener(this);
            questionbank_sub_details_tab_chapterexercises.setOnClickListener(this);
            questionbank_sub_details_tab_quicktask.setOnClickListener(this);
            questionbank_sub_details_tab_simulated.setOnClickListener(this);
            mPopupWindow = new PopupWindow(mControlMainActivity);
            animUtil = new ModelAnimUtil();
            ModelPtrFrameLayout questionbank_sub_details_content_frame = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            questionbank_sub_details_content_frame.addPtrUIHandler(header);
            questionbank_sub_details_content_frame.setHeaderView(header);
            questionbank_sub_details_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    questionbank_sub_details_content_frame.refreshComplete();
                }
            });
        }
        fragmentquestionbank_main.addView(mModelQuestionBankDetailsView);
        TextView questionbank_sub_details_titletext = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_titletext);
        questionbank_sub_details_titletext.setText(ibs_name);
        //默认游标位置在章节练习
        ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
        int x = width / 6 - mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        questionbank_sub_details_cursor1.setX(x);
        //默认选中的为章节练习
        mLastTabIndex = 1;
        mCurrentTab = "ChapterExercises";
        TextView questionbank_sub_details_tab_chapterexercises = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_chapterexercises);
        TextView questionbank_sub_details_tab_quicktask = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_quicktask);
        TextView questionbank_sub_details_tab_simulated = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_tab_simulated);
        questionbank_sub_details_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        questionbank_sub_details_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        questionbank_sub_details_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankDetailsView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        mIbs_id = ibs_id;
        //如果没有此子题库的做题记录，请将底部功能按钮层隐藏（继续做题）
        LinearLayout questionbank_sub_details_bottomfunction = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_bottomfunction);
        questionbank_sub_details_bottomfunction.setVisibility(View.INVISIBLE);
        QuestionBankDetailsChapterShow();
    }

    //题库详情-章节练习
    private void QuestionBankDetailsChapterShow(){
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("自由选择章节知识点各个突破");
        LinearLayout questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery( //查没被删除的章type=1，id= ibs_id的
                "select chapter_test_point_id,`name` from chapter_test_point_edu where tf_delete=2 and ibs_id=" + mIbs_id + " and type=1", null);
        while (cursor.moveToNext()) {
            int chapter_test_point_idIndex = cursor.getColumnIndex("chapter_test_point_id");
            int nameIndex = cursor.getColumnIndex("name");
            String chapter_test_point_id = cursor.getString(chapter_test_point_idIndex);
            mCurrentChapterName = cursor.getString(nameIndex);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_chapterexercises, null);
            TextView questionbank_sub_details_chapterexercises_name = view.findViewById(R.id.questionbank_sub_details_chapterexercises_name);
            questionbank_sub_details_chapterexercises_name.setText(mCurrentChapterName);
            questionbank_sub_details_chapterexercises_name.setHint(chapter_test_point_id);
            //默认全部展开
            ImageView questionbank_sub_details_chapterexercises_arrow_right = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_right);
            ImageView questionbank_sub_details_chapterexercises_arrow_down = view.findViewById(R.id.questionbank_sub_details_chapterexercises_arrow_down);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) questionbank_sub_details_chapterexercises_arrow_right.getLayoutParams();
            ll.width = 0;
            questionbank_sub_details_chapterexercises_arrow_right.setLayoutParams(ll);
            ModelExpandView questionbank_sub_details_chapterexercises_expandView = view.findViewById(R.id.questionbank_sub_details_chapterexercises_expandView);
            questionbank_sub_details_chapterexercises_arrow_right.setClickable(true);
            questionbank_sub_details_chapterexercises_arrow_down.setClickable(true);
            QuestionBankDetailsChapterExerisesShow(view,mIbs_id,chapter_test_point_id);
            questionbank_sub_details_chapterexercises_arrow_down.setOnClickListener(v-> {
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
                    QuestionBankDetailsChapterExerisesShow(view,mIbs_id,chapter_test_point_id);
                }
            });
            questionbank_sub_details_chapterexercises_arrow_right.setOnClickListener(v-> {
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
                    QuestionBankDetailsChapterExerisesShow(view,mIbs_id,chapter_test_point_id);
                }
            });
            //点击章名称，进行章抽题
            questionbank_sub_details_chapterexercises_name.setClickable(true);
            questionbank_sub_details_chapterexercises_name.setOnClickListener(v->{
                QuestionBankQuestionSettingShow("chapter",chapter_test_point_id);
            });
            questionbank_sub_details_content.addView(view);
        }
        cursor.close();
    }

    //题库详情-快速做题
    private void QuestionBankDetailsQuickTaskShow() {
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("随机抽取一定量的试题 碎片化学习更方便");
        LinearLayout questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_quicktask, null);
        ImageView questionbank_sub_details_quicktask_start = view.findViewById(R.id.questionbank_sub_details_quicktask_start);
        questionbank_sub_details_quicktask_start.setClickable(true);
        questionbank_sub_details_quicktask_start.setOnClickListener(v->{

        });
        questionbank_sub_details_content.addView(view);
    }

    //题库详情-快速做题
    private void QuestionBankDetailsSimulatedShow() {
        TextView questionbank_sub_details_brief = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_brief);
        questionbank_sub_details_brief.setText("模拟真实考试场景知识点综合评测");
        LinearLayout questionbank_sub_details_content = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_content);
        questionbank_sub_details_content.removeAllViews();
        //查数据库，ibs_id下有几套试卷
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(  //查发布成功且没有被删除的真题
                "SELECT test_paper_id,test_paper_name,answer_time,total_score from test_paper_edu where ibs_id=" + mIbs_id + " and test_paper_type='1' " +
                        "and state='3' and tf_delete='2';", null);
        boolean misEmpty = true;
        while (cursor.moveToNext()) {
            misEmpty = false;
            int test_paper_idIndex = cursor.getColumnIndex("test_paper_id");
            int test_paper_nameIndex = cursor.getColumnIndex("test_paper_name");
            int answer_timeIndex = cursor.getColumnIndex("answer_time");
            int total_scoreIndex = cursor.getColumnIndex("total_score");
            String test_paper_id = cursor.getString(test_paper_idIndex);
            String test_paper_name = cursor.getString(test_paper_nameIndex);
            String answer_time = cursor.getString(answer_timeIndex);
            String total_score = cursor.getString(total_scoreIndex);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_simulate, null);
            TextView questionbank_simulated_name = view.findViewById(R.id.questionbank_simulated_name);
            questionbank_simulated_name.setText(test_paper_name);
            questionbank_simulated_name.setHint(test_paper_id);
            TextView questionbank_simulated_time = view.findViewById(R.id.questionbank_simulated_time);
            questionbank_simulated_time.setText(answer_time + "分钟");
            TextView questionbank_simulated_score = view.findViewById(R.id.questionbank_simulated_score);
            questionbank_simulated_score.setText(total_score + "分");
            TextView questionbank_simulated_go = view.findViewById(R.id.questionbank_simulated_go);
            questionbank_simulated_go.setClickable(true);
            questionbank_simulated_go.setOnClickListener(v->{
                //开始真题做题
            });
            ImageView questionbank_simulated_goimage = view.findViewById(R.id.questionbank_simulated_goimage);
            questionbank_simulated_goimage.setClickable(true);
            questionbank_simulated_goimage.setOnClickListener(v->{
                //开始真题做题
            });
            questionbank_sub_details_content.addView(view);
        }
        cursor.close();
        if (misEmpty){  //没数据展示空提示界面
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_pointout, null);
            questionbank_sub_details_content.addView(view);
        }
    }

    //显示章 展开下面的节或考点
    private void QuestionBankDetailsChapterExerisesShow(View view,String ibs_id,String chapter_test_point_id) {
        Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery( //查没被删除的章下面的节和考点，id= ibs_id的
                "select chapter_test_point_id,`name` from chapter_test_point_edu where tf_delete=2 and ibs_id=" + ibs_id + " and father_id=" + chapter_test_point_id, null);
        boolean isFind = false;
        LinearLayout questionbank_sub_details_chapterexercises_content = view.findViewById(R.id.questionbank_sub_details_chapterexercises_content);
        questionbank_sub_details_chapterexercises_content.removeAllViews();
        View questionbank_sub_details_chapterexercises1_line1 = null;
        while (cursor1.moveToNext()) {
            isFind = true;
            int chapter_test_point_idIndex1 = cursor1.getColumnIndex("chapter_test_point_id");
            int nameIndex1 = cursor1.getColumnIndex("name");
            String chapter_test_point_id1 = cursor1.getString(chapter_test_point_idIndex1);
            mCurrentChapterName = cursor1.getString(nameIndex1);
            View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_sub_detials_chapterexercises1, null);
            TextView questionbank_sub_details_chapterexercises1_name = view1.findViewById(R.id.questionbank_sub_details_chapterexercises1_name);
            questionbank_sub_details_chapterexercises1_name.setHint(chapter_test_point_id1);
            questionbank_sub_details_chapterexercises1_name.setText(mCurrentChapterName);
            questionbank_sub_details_chapterexercises1_line1 = view1.findViewById(R.id.questionbank_sub_details_chapterexercises1_line1);
            questionbank_sub_details_chapterexercises1_name.setClickable(true);
            //点击节或考点名称，进行节或考点抽题
            questionbank_sub_details_chapterexercises1_name.setOnClickListener(v->{
                QuestionBankQuestionSettingShow("exercises",chapter_test_point_id1);
            });
            questionbank_sub_details_chapterexercises_content.addView(view1);
        }
        cursor1.close();
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
        if (!isFind){
            Toast.makeText(mControlMainActivity,"本章下面没有节或考点",Toast.LENGTH_SHORT);
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
    public void QuestionBankQuestionSettingShow(String type,String id) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickQuestionBankSetting();
        HideAllLayout();
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        if (mModelQuestionBankSettingView == null) {
            mModelQuestionBankSettingView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questionsetting, null);
            LinearLayout questionbank_questionsetting_questionmode_test = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questionmode_test);
            LinearLayout questionbank_questionsetting_questionmode_exam = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questionmode_exam);
            questionbank_questionsetting_questionmode_test.setClickable(true);
            questionbank_questionsetting_questionmode_test.setOnClickListener(v->{
                mCurrentIndex = 0;
                QuestionBankDetailsQuestionModeTestShow();
            });
            questionbank_questionsetting_questionmode_exam.setOnClickListener(v->{
                mCurrentIndex = 0;
                QuestionBankDetailsQuestionModeExamShow();
            });
        }
        fragmentquestionbank_main.addView(mModelQuestionBankSettingView);
        String keyString = "";
        if ("chapter".equals(type)){
            keyString = "chapter_id='" + id + "'";
        } else {
            keyString = "section_id='" + id + "'";
        }
        TextView questionbank_questionsetting_singlechoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_singlechoice);
        TextView questionbank_questionsetting_multichoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_multichoice);
        TextView questionbank_questionsetting_shortanswerchoice = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_shortanswerchoice);
        TextView questionbank_questionsetting_materialquestion = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_materialquestion);
        //查询此章节下的题-单选题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery( //state:1正在编辑2等待审核3发布成功4审核失败;tf_delete是否删除_1是_2否
                "select count(*) as count from test_questions_edu where question_type='1' and state='3' and tf_delete='2'and " + keyString, null);
        int count = 0;
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("count");
            count = cursor.getInt(countIndex);
        }
        cursor.close();
        if (count == 0){ //没有单选题
            mSingleChoiceState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mSingleChoiceState = "select";
            questionbank_questionsetting_singlechoice.setText("单选题(" + count + ")");
        }
        //查询此章节下的题-多选题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select count(*) as count from test_questions_edu where question_type='2' and state='3' and tf_delete='2'and " + keyString, null);
        count = 0;
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("count");
            count = cursor.getInt(countIndex);
        }
        cursor.close();
        if (count == 0){ //没有多选题
            mMultiChoiceState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mMultiChoiceState = "select";
            questionbank_questionsetting_multichoice.setText("多选题(" + count + ")");
        }
        //查询此章节下的题-简答题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select count(*) as count from test_questions_edu where question_type='4' and state='3' and tf_delete='2'and " + keyString, null);
        count = 0;
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("count");
            count = cursor.getInt(countIndex);
        }
        cursor.close();
        if (count == 0){ //没有简答题
            mShortAnswerState = "disable";
        } else {
            //默认将其可选的改为全选
            questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
            questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
            mShortAnswerState = "select";
            questionbank_questionsetting_shortanswerchoice.setText("简答题(" + count + ")");
        }
        //查询此章节下的题-材料题数量（：1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___(填空题、判断题、不定项__不要了_)）
        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select count(*) as count from test_questions_edu where question_type='7' and state='3' and tf_delete='2'and " + keyString, null);
        count = 0;
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("count");
            count = cursor.getInt(countIndex);
        }
        cursor.close();
        if (count == 0){ //没有材料题
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
        questionbank_questionsetting_singlechoice.setOnClickListener(v->{
            if (mSingleChoiceState.equals("select")){
                //全选改为未选
                questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mSingleChoiceState = "unselect";
            } else if (mSingleChoiceState.equals("unselect")){
                //未选改为全选
                questionbank_questionsetting_singlechoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_singlechoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mSingleChoiceState = "select";
            } else if (mSingleChoiceState.equals("disable")){
                //不可选
                mSingleChoiceState = "disable";
            }
        });
        questionbank_questionsetting_multichoice.setOnClickListener(v->{
            if (mMultiChoiceState.equals("select")){
                //全选改为未选
                questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mMultiChoiceState = "unselect";
            } else if (mMultiChoiceState.equals("unselect")){
                //未选改为全选
                questionbank_questionsetting_multichoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_multichoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mMultiChoiceState = "select";
            } else if (mMultiChoiceState.equals("disable")){
                //不可选
                mMultiChoiceState = "disable";
            }
        });
        questionbank_questionsetting_shortanswerchoice.setOnClickListener(v->{
            if (mShortAnswerState.equals("select")){
                //全选改为未选
                questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mShortAnswerState = "unselect";
            } else if (mShortAnswerState.equals("unselect")){
                //未选改为全选
                questionbank_questionsetting_shortanswerchoice.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_shortanswerchoice.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mShortAnswerState = "select";
            } else if (mShortAnswerState.equals("disable")){
                //不可选
                mShortAnswerState = "disable";
            }
        });
        questionbank_questionsetting_materialquestion.setOnClickListener(v->{
            if (mMaterialQuestionState.equals("select")){
                //全选改为未选
                questionbank_questionsetting_materialquestion.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                questionbank_questionsetting_materialquestion.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                mMaterialQuestionState = "unselect";
            } else if (mMaterialQuestionState.equals("unselect")){
                //未选改为全选
                questionbank_questionsetting_materialquestion.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                questionbank_questionsetting_materialquestion.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.white));
                mMaterialQuestionState = "select";
            } else if (mMaterialQuestionState.equals("disable")){
                //不可选
                mMaterialQuestionState = "disable";
            }
        });
        //判断分类、如果此章/节/考点 有全部题/未做题/错题,将其状态置为可选
        TextView questionbank_questionsetting_all = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_all);
        TextView questionbank_questionsetting_notdone = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_notdone);
        TextView questionbank_questionsetting_wrong = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_wrong);
        //查询此章节下的全部题
        cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select count(*) as count from test_questions_edu where state='3' and tf_delete='2'and " + keyString, null);
        count = 0;
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("count");
            count = cursor.getInt(countIndex);
        }
        cursor.close();
        if (count == 0){ //没有题
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
        questionbank_questionsetting_all.setOnClickListener(v->{
            if (mAllQuestionState.equals("enable")) {
                mQuestionType = "AllQuestion";
                questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mNotDoneQuestionState.equals("disable")) {
                    questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mWrongQuestionState.equals("disable")){
                    questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mAllQuestionState.equals("disable")){
                //不可选
            }
        });
        questionbank_questionsetting_notdone.setOnClickListener(v->{
            if (mNotDoneQuestionState.equals("enable")) {
                mQuestionType = "NotDoneQuestion";
                questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mAllQuestionState.equals("disable")) {
                    questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mWrongQuestionState.equals("disable")){
                    questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mNotDoneQuestionState.equals("disable")){
                //不可选
            }
        });
        questionbank_questionsetting_wrong.setOnClickListener(v->{
            if (mWrongQuestionState.equals("enable")) {
                mQuestionType = "WrongQuestion";
                questionbank_questionsetting_wrong.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_wrong.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mAllQuestionState.equals("disable")) {
                    questionbank_questionsetting_all.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_all.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mNotDoneQuestionState.equals("disable")){
                    questionbank_questionsetting_notdone.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_notdone.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mWrongQuestionState.equals("disable")){
                //不可选
            }
        });
        //判断题量、如果此章/节/考点 有10道题/20道题/100道题,将其状态置为可选
        TextView questionbank_questionsetting_questioncount1 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount1);
        TextView questionbank_questionsetting_questioncount2 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount2);
        TextView questionbank_questionsetting_questioncount3 = mModelQuestionBankSettingView.findViewById(R.id.questionbank_questionsetting_questioncount3);
        if (count > 0 ) {
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
        if (count > 20){
            questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
            questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
            mHundredQuestionState = "enable";
        }
        //设置点击事件
        questionbank_questionsetting_questioncount1.setClickable(true);
        questionbank_questionsetting_questioncount2.setClickable(true);
        questionbank_questionsetting_wrong.setClickable(true);
        questionbank_questionsetting_questioncount1.setOnClickListener(v->{
            if (mTenQuestionState.equals("enable")) {
                mQuestionCount = "TenQuestion";
                questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTwentyQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mHundredQuestionState.equals("disable")){
                    questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mTenQuestionState.equals("disable")){
                //不可选
            }
        });
        questionbank_questionsetting_questioncount2.setOnClickListener(v->{
            if (mTwentyQuestionState.equals("enable")) {
                mQuestionCount = "TwentyQuestion";
                questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTenQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mHundredQuestionState.equals("disable")){
                    questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mTwentyQuestionState.equals("disable")){
                //不可选
            }
        });
        questionbank_questionsetting_questioncount3.setOnClickListener(v->{
            if (mHundredQuestionState.equals("enable")) {
                mQuestionCount = "HundredQuestion";
                questionbank_questionsetting_questioncount3.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_blue1));
                questionbank_questionsetting_questioncount3.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.blue669ef0));
                if (!mTenQuestionState.equals("disable")) {
                    questionbank_questionsetting_questioncount1.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount1.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
                if (!mTwentyQuestionState.equals("disable")){
                    questionbank_questionsetting_questioncount2.setBackground(mModelQuestionBankSettingView.getResources().getDrawable(R.drawable.textview_style_rect_black));
                    questionbank_questionsetting_questioncount2.setTextColor(mModelQuestionBankSettingView.getResources().getColor(R.color.collectdefaultcolor3));
                }
            } else if (mHundredQuestionState.equals("disable")){
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
        mModelQuestionBankHandInView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper, null);
        fragmentquestionbank_main.addView(mModelQuestionBankHandInView);
        ControllerRoundProgressBar coursedetails_handinpaper_accuracyrateprogress = mModelQuestionBankHandInView.findViewById(R.id.coursedetails_handinpaper_accuracyrateprogress);
        coursedetails_handinpaper_accuracyrateprogress.setProgress(30);
        coursedetails_handinpaper_accuracyrateprogress.setMax(100);
        TextView questionbank_handinpaper__main_titletext = mModelQuestionBankHandInView.findViewById(R.id.questionbank_handinpaper__main_titletext);
        questionbank_handinpaper__main_titletext.setText(mCurrentChapterName);
        LinearLayout coursedetails_handinpaper_details = mModelQuestionBankHandInView.findViewById(R.id.coursedetails_handinpaper_details);
        //查找题型
        coursedetails_handinpaper_details.removeAllViews();
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
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
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(//question_type 1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                "SELECT question_type,tf_collection from test_questions_edu " +
                        "LEFT JOIN student_question_edu on test_questions_edu.question_id=student_question_edu.question_id " +
                        "where test_questions_edu.question_id in (" + question_id_group.substring(1,question_id_group.length()).replace("#",",") +
                        ") and test_questions_edu.tf_delete='2' ;", null);
        while (cursor.moveToNext()) {
            int question_typeIndex = cursor.getColumnIndex("question_type");
//            int tf_collectionIndex = cursor.getColumnIndex("tf_collection");
            String question_type = cursor.getString(question_typeIndex);
//            String tf_collection = cursor.getString(tf_collectionIndex);
            if (question_type.equals("1")){
                if (singleView == null){
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
                questionbank_handin2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                    mCurrentIndex = finalCount;
                    QuestionBankDetailsQuestionModeHandInShow();
                });
            } else if (question_type.equals("2")){
                if (mutilView == null){
                    mutilView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_handinpaper1, null);
                    coursedetails_handinpaper_details.addView(mutilView);
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
                questionbank_handin2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                    mCurrentIndex = finalCount;
                    QuestionBankDetailsQuestionModeHandInShow();
                });
            } else if (question_type.equals("4")){
                if (shortAnswerView == null){
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
                questionbank_handin2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                    mCurrentIndex = finalCount;
                    QuestionBankDetailsQuestionModeHandInShow();
                });
            } else if (question_type.equals("7")){
                if (materialView == null){
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
                questionbank_handin2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                    mCurrentIndex = finalCount;
                    QuestionBankDetailsQuestionModeHandInShow();
                });
            }
            count ++;
        }
        cursor.close();
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
        } else if (mCurrentAnswerMode.equals("requestionrecord")){
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
            mModelQuestionBankAnswerPaperView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper, null);
        }
        fragmentquestionbank_main.addView(mModelQuestionBankAnswerPaperView);
        //此题所属章节名称
        TextView questionbank_answerpaper_questiontitle = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questiontitle);
        questionbank_answerpaper_questiontitle.setText(mCurrentChapterName);
        TextView questionbank_answerpaper_countdowntimetext = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_countdowntimetext);
        //点击标记
        ImageView questionbank_answerpaper_sign = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_sign);
        questionbank_answerpaper_sign.setOnClickListener(v->{
            if (mIsSign) {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign));
                mIsSign = false;
                Toast.makeText(mControlMainActivity,"取消标记",Toast.LENGTH_SHORT).show();
            } else {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign_blue));
                Toast.makeText(mControlMainActivity,"标记此题",Toast.LENGTH_SHORT).show();
                mIsSign = true;
            }
        });
        //查询试卷表中生成的临时题
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("question_id_group");
            question_id_group = cursor.getString(countIndex);
            break;
        }
        cursor.close();
        //添加题,//默认显示第一题
        QuestionViewAdd(question_id_group);
        //点击交卷
        LinearLayout questionbank_answerpaper_commit = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_commit);
        String finalQuestion_id_group1 = question_id_group;
        questionbank_answerpaper_commit.setOnClickListener(v->{
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
            button_cancel.setOnClickListener(View-> {
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("交卷");
            button_sure.setOnClickListener(View->{
                //暂停计时器
                if (mTimer2 != null){
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
        questionbank_answerpaper_pause.setOnClickListener(v->{
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
            button_sure.setOnClickListener(View->{
                mMyDialog.cancel();
                //重新打开计时器
                mTimer2 = new Timer();
                mTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        mTime = mTime + 1;
                        runOnUiThread(() -> questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
                    }
                };
                mTimer2.schedule(mTask2, 0, 1000);
            });
            //暂停计时器
            if (mTimer2 != null){
                mTimer2.cancel();
            }
            if (mTask2 != null) {
                mTask2.cancel();
            }
        });
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        LinearLayout button_questionbank_beforquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_beforquestion);
        button_questionbank_beforquestion.setOnClickListener(v->{
            TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
            if (questionbank_answerpaper_questioncount.getText().toString().equals("1") ||questionbank_answerpaper_questioncount.getText().toString().equals("0")){
                Toast.makeText(mControlMainActivity,"前面没有题啦",Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
                QuestionViewAdd(finalQuestion_id_group);
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
        //下一题
        LinearLayout button_questionbank_nextquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_nextquestion);
        button_questionbank_nextquestion.setOnClickListener(V->{
            if (question_id_groupS != null) {
                TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
                if (questionbank_answerpaper_questioncount.getText().toString().equals("" + question_id_groupS.length)) {
                    Toast.makeText(mControlMainActivity, "此题已经是最后一道题啦", Toast.LENGTH_SHORT).show();
                } else { //跳到下一道题
                    mCurrentIndex = mCurrentIndex + 1;
                    QuestionViewAdd(finalQuestion_id_group);
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
        button_questionbank_answerquestioncard.setOnClickListener(v-> AnswerQuestionCardViewAdd(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_answerpaper_fontsize = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_fontsize);
        questionbank_answerpaper_fontsize.setOnClickListener(v->{
            ShowPopFontSize(finalQuestion_id_group,questionbank_answerpaper_fontsize);
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
        lLayoutParams.height = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_37);
        lLayoutParams.topMargin = mModelQuestionBankAnswerPaperView.getResources().getDimensionPixelSize(R.dimen.dp_70);
        coursedetails_answerpaper_analysisbutton.setLayoutParams(lLayoutParams);
        LinearLayout coursedetails_answerpaper_analysis = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_analysis);
        coursedetails_answerpaper_analysis.removeAllViews();
        coursedetails_answerpaper_analysisbutton.setOnClickListener(v->{
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_answerpaper_analysisbutton.getLayoutParams();
            ll.height = 0;
            ll.topMargin = 0;
            coursedetails_answerpaper_analysisbutton.setLayoutParams(ll);
            Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                            "select optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                                    + question_id_groupS[mCurrentIndex] + "';", null);
            while (cursor1.moveToNext()) {
                int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
                int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
                String optionanswer = cursor1.getString(optionanswerIndex);
                String question_type = cursor1.getString(question_typeIndex);
                String question_analysis = cursor1.getString(question_analysisIndex);
                if (optionanswer == null || question_type == null || question_analysis == null){
                    break;
                }
                if (question_type.equals("1") || question_type.equals("2")){//单选题或多选题
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                    coursedetails_answerpaper_analysis.addView(view);
                    //修改内容为正确答案
                    String[] optionanswerS = optionanswer.split(";");
                    if (optionanswerS == null){
                        break;
                    }
                    String currentAnswer = "";
                    for (int  i = 0 ; i < optionanswerS.length ; i ++){
                        String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                        if (optionanswerS1.length != 3){
                            break;
                        }
                        if (optionanswerS1[1].equals("是")){
                            currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                        }
                    }
                    if (currentAnswer.equals("")){
                        break;
                    }
                    TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                    questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
                    TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                    questionbank_analysis1_content.setText(question_analysis);
                    //修改内容为您的答案
                    TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                    questionbank_analysis1_yourAnswer.setText("aaa");
                    if (mFontSize.equals("nomal")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                } else if (question_type.equals("4")){//简答题
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                    coursedetails_answerpaper_analysis.addView(view);
                    //修改内容为正确答案
                    TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                    questionbank_analysis2_rightAnswer.setText(optionanswer);
                    //修改内容为此题的解析
                    TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                    questionbank_analysis2_content.setText(question_analysis);
                    //修改内容为您的答案
                    TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
                    if (mFontSize.equals("nomal")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                }
                break;
            }
            cursor1.close();

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
        questionbank_answerpaper_questiontitle.setText(mCurrentChapterName);
        TextView questionbank_answerpaper_countdowntimetext = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_countdowntimetext);
        //点击标记
        ImageView questionbank_answerpaper_sign = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_sign);
        questionbank_answerpaper_sign.setOnClickListener(v->{
            if (mIsSign) {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign));
                mIsSign = false;
                Toast.makeText(mControlMainActivity,"取消标记",Toast.LENGTH_SHORT).show();
            } else {
                questionbank_answerpaper_sign.setBackground(mModelQuestionBankAnswerPaperView.getResources().getDrawable(R.drawable.button_questionbank_sign_blue));
                Toast.makeText(mControlMainActivity,"标记此题",Toast.LENGTH_SHORT).show();
                mIsSign = true;
            }
        });
        //查询试卷表中生成的临时题
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("question_id_group");
            question_id_group = cursor.getString(countIndex);
            break;
        }
        cursor.close();
        //添加题,//默认显示第一题
        QuestionViewAdd(question_id_group);
        //点击交卷
        LinearLayout questionbank_answerpaper_commit = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_commit);
        String finalQuestion_id_group1 = question_id_group;
        questionbank_answerpaper_commit.setOnClickListener(v->{
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
            button_cancel.setOnClickListener(View->{
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("交卷");
            button_sure.setOnClickListener(View->{
                //暂停计时器
                if (mTimer2 != null){
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
        questionbank_answerpaper_pause.setOnClickListener(v->{
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
            button_sure.setOnClickListener(View->{
                mMyDialog.cancel();
                //重新打开计时器
                mTimer2 = new Timer();
                mTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        mTime = mTime + 1;
                        runOnUiThread(() -> questionbank_answerpaper_countdowntimetext.setText(getStringTime(mTime)));
                    }
                };
                mTimer2.schedule(mTask2, 0, 1000);
            });
            //暂停计时器
            if (mTimer2 != null){
                mTimer2.cancel();
            }
            if (mTask2 != null) {
                mTask2.cancel();
            }
        });
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        LinearLayout button_questionbank_beforquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_beforquestion);
        button_questionbank_beforquestion.setOnClickListener(v->{
            TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
            if (questionbank_answerpaper_questioncount.getText().toString().equals("1") ||questionbank_answerpaper_questioncount.getText().toString().equals("0")){
                Toast.makeText(mControlMainActivity,"前面没有题啦",Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
                QuestionViewAdd(finalQuestion_id_group);
            }
        });
        //下一题
        LinearLayout button_questionbank_nextquestion = mModelQuestionBankAnswerPaperView.findViewById(R.id.button_questionbank_nextquestion);
        button_questionbank_nextquestion.setOnClickListener(V->{
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
        button_questionbank_answerquestioncard.setOnClickListener(v-> AnswerQuestionCardViewAdd(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_answerpaper_fontsize = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_fontsize);
        questionbank_answerpaper_fontsize.setOnClickListener(v->{
            ShowPopFontSize(finalQuestion_id_group,questionbank_answerpaper_fontsize);
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
        questionbank_handin_analysis_questiontitle.setText(mCurrentChapterName);
        //点击标记
        ImageView questionbank_handin_analysis_collection = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_collection);
        questionbank_handin_analysis_collection.setOnClickListener(v->{
            if (mIsCollect) {
                questionbank_handin_analysis_collection.setBackground(mModelQuestionBankHandInAnalysisView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
                Toast.makeText(mControlMainActivity,"取消收藏",Toast.LENGTH_SHORT).show();
            } else {
                questionbank_handin_analysis_collection.setBackground(mModelQuestionBankHandInAnalysisView.getResources().getDrawable(R.drawable.button_collect_enable));
                Toast.makeText(mControlMainActivity,"收藏此题",Toast.LENGTH_SHORT).show();
                mIsCollect = true;
            }
        });
        //查询试卷表中生成的临时题
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("question_id_group");
            question_id_group = cursor.getString(countIndex);
            break;
        }
        cursor.close();
        //添加题,//默认显示第一题
        HandInAnalysisQuestionViewAdd(question_id_group);
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        LinearLayout button_handin_analysis_beforquestion = mModelQuestionBankHandInAnalysisView.findViewById(R.id.button_handin_analysis_beforquestion);
        button_handin_analysis_beforquestion.setOnClickListener(v->{
            TextView questionbank_handin_analysis_questioncount = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncount);
            if (questionbank_handin_analysis_questioncount.getText().toString().equals("1") ||questionbank_handin_analysis_questioncount.getText().toString().equals("0")){
                Toast.makeText(mControlMainActivity,"前面没有题啦",Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeHandInShow();
            }
        });
        //下一题
        LinearLayout button_handin_analysis_nextquestion = mModelQuestionBankHandInAnalysisView.findViewById(R.id.button_handin_analysis_nextquestion);
        button_handin_analysis_nextquestion.setOnClickListener(V->{
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
        button_handin_analysis_answerquestioncard.setOnClickListener(v-> QuestionBankDetailsHandInPaperShow(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_handin_analysis_fontsize = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_fontsize);
        questionbank_handin_analysis_fontsize.setOnClickListener(v->{
            ShowPopFontSize(finalQuestion_id_group,questionbank_handin_analysis_fontsize);
        });

        //添加题的解析
        LinearLayout coursedetails_handin_analysis_analysis = mModelQuestionBankHandInAnalysisView.findViewById(R.id.coursedetails_handin_analysis_analysis);
        coursedetails_handin_analysis_analysis.removeAllViews();
        Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                        + question_id_groupS[mCurrentIndex] + "';", null);
        while (cursor1.moveToNext()) {
            int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
            int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
            int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
            String optionanswer = cursor1.getString(optionanswerIndex);
            String question_type = cursor1.getString(question_typeIndex);
            String question_analysis = cursor1.getString(question_analysisIndex);
            if (optionanswer == null || question_type == null || question_analysis == null){
                break;
            }
            if (question_type.equals("1") || question_type.equals("2")){//单选题或多选题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                coursedetails_handin_analysis_analysis.addView(view);
                //修改内容为正确答案
                String[] optionanswerS = optionanswer.split(";");
                if (optionanswerS == null){
                    break;
                }
                String currentAnswer = "";
                for (int  i = 0 ; i < optionanswerS.length ; i ++){
                    String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                    if (optionanswerS1.length != 3){
                        break;
                    }
                    if (optionanswerS1[1].equals("是")){
                        currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                    }
                }
                if (currentAnswer.equals("")){
                    break;
                }
                TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
                TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                questionbank_analysis1_content.setText(question_analysis);
                //修改内容为您的答案
                TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                questionbank_analysis1_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            } else if (question_type.equals("4")){//简答题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                coursedetails_handin_analysis_analysis.addView(view);
                //修改内容为正确答案
                TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                questionbank_analysis2_rightAnswer.setText(optionanswer);
                //修改内容为此题的解析
                TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                questionbank_analysis2_content.setText(question_analysis);
                //修改内容为您的答案
                TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            }
            break;
        }
        cursor1.close();
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
        questionbank_wrongquestion_questiontitle.setText(mCurrentChapterName);
        //点击收藏
        ImageView questionbank_wrongquestion_collection = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_collection);
        questionbank_wrongquestion_collection.setOnClickListener(v->{
            if (mIsCollect) {
                questionbank_wrongquestion_collection.setBackground(mModelQuestionBankWrongQuestionView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
                Toast.makeText(mControlMainActivity,"取消收藏",Toast.LENGTH_SHORT).show();
            } else {
                questionbank_wrongquestion_collection.setBackground(mModelQuestionBankWrongQuestionView.getResources().getDrawable(R.drawable.button_collect_enable));
                Toast.makeText(mControlMainActivity,"收藏此题",Toast.LENGTH_SHORT).show();
                mIsCollect = true;
            }
        });
        //查询试卷表中生成的临时题
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("question_id_group");
            question_id_group = cursor.getString(countIndex);
            break;
        }
        cursor.close();
        //添加题,//默认显示第一题
        WrongQuestionViewAdd(question_id_group);
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        LinearLayout button_wrongquestion_beforquestion = mModelQuestionBankWrongQuestionView.findViewById(R.id.button_wrongquestion_beforquestion);
        button_wrongquestion_beforquestion.setOnClickListener(v->{
            TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
            if (questionbank_wrongquestion_questioncount.getText().toString().equals("1") ||questionbank_wrongquestion_questioncount.getText().toString().equals("0")){
                Toast.makeText(mControlMainActivity,"前面没有题啦",Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeWrongQuestionShow();
            }
        });
        //下一题
        LinearLayout button_wrongquestion_nextquestion = mModelQuestionBankWrongQuestionView.findViewById(R.id.button_wrongquestion_nextquestion);
        button_wrongquestion_nextquestion.setOnClickListener(V->{
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
        button_wrongquestion_answerquestioncard.setOnClickListener(v-> QuestionBankDetailsQuestionTypeShow(finalQuestion_id_group));
        //交卷
        LinearLayout questionbank_wrongquestion_commit = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_commit);
        questionbank_wrongquestion_commit.setOnClickListener(v->{
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
            button_cancel.setOnClickListener(View->{
                mMyDialog.cancel();
            });
            TextView button_sure = view1.findViewById(R.id.button_sure);
            button_sure.setText("是");
            button_sure.setOnClickListener(View->{
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
        questionbank_wrongquestion_fontsize.setOnClickListener(v->{
            ShowPopFontSize(finalQuestion_id_group,questionbank_wrongquestion_fontsize);
        });
        //点击按钮，显示解析
        TextView coursedetails_wrongquestion_analysisbutton = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_analysisbutton);
        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) coursedetails_wrongquestion_analysisbutton.getLayoutParams();
        lLayoutParams.height = mModelQuestionBankWrongQuestionView.getResources().getDimensionPixelSize(R.dimen.dp_37);
        lLayoutParams.topMargin = mModelQuestionBankWrongQuestionView.getResources().getDimensionPixelSize(R.dimen.dp_70);
        coursedetails_wrongquestion_analysisbutton.setLayoutParams(lLayoutParams);
        LinearLayout coursedetails_wrongquestion_analysis = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_analysis);
        coursedetails_wrongquestion_analysis.removeAllViews();
        coursedetails_wrongquestion_analysisbutton.setOnClickListener(v->{
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) coursedetails_wrongquestion_analysisbutton.getLayoutParams();
            ll.height = 0;
            ll.topMargin = 0;
            coursedetails_wrongquestion_analysisbutton.setLayoutParams(ll);
            Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                    "select optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                            + question_id_groupS[mCurrentIndex] + "';", null);
            while (cursor1.moveToNext()) {
                int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
                int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
                String optionanswer = cursor1.getString(optionanswerIndex);
                String question_type = cursor1.getString(question_typeIndex);
                String question_analysis = cursor1.getString(question_analysisIndex);
                if (optionanswer == null || question_type == null || question_analysis == null){
                    break;
                }
                if (question_type.equals("1") || question_type.equals("2")){//单选题或多选题
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                    coursedetails_wrongquestion_analysis.addView(view);
                    //修改内容为正确答案
                    String[] optionanswerS = optionanswer.split(";");
                    if (optionanswerS == null){
                        break;
                    }
                    String currentAnswer = "";
                    for (int  i = 0 ; i < optionanswerS.length ; i ++){
                        String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                        if (optionanswerS1.length != 3){
                            break;
                        }
                        if (optionanswerS1[1].equals("是")){
                            currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                        }
                    }
                    if (currentAnswer.equals("")){
                        break;
                    }
                    TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                    questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
                    TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                    questionbank_analysis1_content.setText(question_analysis);
                    //修改内容为您的答案
                    TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                    questionbank_analysis1_yourAnswer.setText("aaa");
                    if (mFontSize.equals("nomal")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                } else if (question_type.equals("4")){//简答题
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                    coursedetails_wrongquestion_analysis.addView(view);
                    //修改内容为正确答案
                    TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                    questionbank_analysis2_rightAnswer.setText(optionanswer);
                    //修改内容为此题的解析
                    TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                    questionbank_analysis2_content.setText(question_analysis);
                    //修改内容为您的答案
                    TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
                    if (mFontSize.equals("nomal")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                }
                break;
            }
            cursor1.close();

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
        questionbank_mycollextionquestion_questiontitle.setText(mCurrentChapterName);
        //点击收藏
        ImageView questionbank_mycollextionquestion_collection = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_collection);
        questionbank_mycollextionquestion_collection.setOnClickListener(v->{
            if (mIsCollect) {
                questionbank_mycollextionquestion_collection.setBackground(mModelQuestionBankMyCollectionQuestionView.getResources().getDrawable(R.drawable.button_collect_disable_black));
                mIsCollect = false;
                Toast.makeText(mControlMainActivity,"取消收藏",Toast.LENGTH_SHORT).show();
            } else {
                questionbank_mycollextionquestion_collection.setBackground(mModelQuestionBankMyCollectionQuestionView.getResources().getDrawable(R.drawable.button_collect_enable));
                Toast.makeText(mControlMainActivity,"收藏此题",Toast.LENGTH_SHORT).show();
                mIsCollect = true;
            }
        });
        //查询试卷表中生成的临时题
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select question_id_group from test_paper_edu WHERE tf_temporary='1' and tf_delete='2' and ibs_id='" + mIbs_id + "';", null);
        String question_id_group = "";
        while (cursor.moveToNext()) {
            int countIndex = cursor.getColumnIndex("question_id_group");
            question_id_group = cursor.getString(countIndex);
            break;
        }
        cursor.close();
        //添加题,//默认显示第一题
        CollectionQuestionViewAdd(question_id_group);
        //上一题
        String finalQuestion_id_group = question_id_group;
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        LinearLayout button_mycollextionquestion_beforquestion = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.button_mycollextionquestion_beforquestion);
        button_mycollextionquestion_beforquestion.setOnClickListener(v->{
            TextView questionbank_mycollextionquestion_questioncount = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncount);
            if (questionbank_mycollextionquestion_questioncount.getText().toString().equals("1") ||questionbank_mycollextionquestion_questioncount.getText().toString().equals("0")){
                Toast.makeText(mControlMainActivity,"前面没有题啦",Toast.LENGTH_SHORT).show();
            } else { //跳到上一道题
                mCurrentIndex = mCurrentIndex - 1;
//                HandInAnalysisQuestionViewAdd(finalQuestion_id_group);
                QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
            }
        });
        //下一题
        LinearLayout button_mycollextionquestion_nextquestion = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.button_mycollextionquestion_nextquestion);
        button_mycollextionquestion_nextquestion.setOnClickListener(V->{
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
        button_mycollextionquestion_answerquestioncard.setOnClickListener(v-> QuestionBankDetailsQuestionTypeShow(finalQuestion_id_group));
        //点击字号
        ImageView questionbank_mycollextionquestion_fontsize = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_fontsize);
        questionbank_mycollextionquestion_fontsize.setOnClickListener(v->{
            ShowPopFontSize(finalQuestion_id_group,questionbank_mycollextionquestion_fontsize);
        });

        //添加题的解析
        LinearLayout coursedetails_mycollextionquestion_analysis = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.coursedetails_mycollextionquestion_analysis);
        coursedetails_mycollextionquestion_analysis.removeAllViews();
        Cursor cursor1 = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                        + question_id_groupS[mCurrentIndex] + "';", null);
        while (cursor1.moveToNext()) {
            int optionanswerIndex = cursor1.getColumnIndex("optionanswer");
            int question_typeIndex = cursor1.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
            int question_analysisIndex = cursor1.getColumnIndex("question_analysis");
            String optionanswer = cursor1.getString(optionanswerIndex);
            String question_type = cursor1.getString(question_typeIndex);
            String question_analysis = cursor1.getString(question_analysisIndex);
            if (optionanswer == null || question_type == null || question_analysis == null){
                break;
            }
            if (question_type.equals("1") || question_type.equals("2")){//单选题或多选题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis1, null);
                coursedetails_mycollextionquestion_analysis.addView(view);
                //修改内容为正确答案
                String[] optionanswerS = optionanswer.split(";");
                if (optionanswerS == null){
                    break;
                }
                String currentAnswer = "";
                for (int  i = 0 ; i < optionanswerS.length ; i ++){
                    String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                    if (optionanswerS1.length != 3){
                        break;
                    }
                    if (optionanswerS1[1].equals("是")){
                        currentAnswer = currentAnswer + optionanswerS1[0] + " ";
                    }
                }
                if (currentAnswer.equals("")){
                    break;
                }
                TextView questionbank_analysis1_rightAnswer = view.findViewById(R.id.questionbank_analysis1_rightAnswer);
                questionbank_analysis1_rightAnswer.setText(currentAnswer);
//                    /修改内容为此题的解析
                TextView questionbank_analysis1_content = view.findViewById(R.id.questionbank_analysis1_content);
                questionbank_analysis1_content.setText(question_analysis);
                //修改内容为您的答案
                TextView questionbank_analysis1_yourAnswer = view.findViewById(R.id.questionbank_analysis1_yourAnswer);
                questionbank_analysis1_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")){
                    questionbank_analysis1_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis1_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            } else if (question_type.equals("4")){//简答题
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_analysis2, null);
                coursedetails_mycollextionquestion_analysis.addView(view);
                //修改内容为正确答案
                TextView questionbank_analysis2_rightAnswer = view.findViewById(R.id.questionbank_analysis2_rightAnswer);
                questionbank_analysis2_rightAnswer.setText(optionanswer);
                //修改内容为此题的解析
                TextView questionbank_analysis2_content = view.findViewById(R.id.questionbank_analysis2_content);
                questionbank_analysis2_content.setText(question_analysis);
                //修改内容为您的答案
                TextView questionbank_analysis2_yourAnswer = view.findViewById(R.id.questionbank_analysis2_yourAnswer);
//                    questionbank_analysis2_yourAnswer.setText("aaa");
                if (mFontSize.equals("nomal")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize17));
                } else if (mFontSize.equals("small")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize14));
                } else if (mFontSize.equals("big")){
                    questionbank_analysis2_rightAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_content.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    questionbank_analysis2_yourAnswer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimensionPixelSize(R.dimen.textsize20));
                }
            }
            break;
        }
        cursor1.close();
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
            TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
            TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
            TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
            questionbank_questionrecords_tab_chapterexercises.setOnClickListener(this);
            questionbank_questionrecords_tab_quicktask.setOnClickListener(this);
            questionbank_questionrecords_tab_simulated.setOnClickListener(this);
            ModelPtrFrameLayout questionbank_questionrecords_content_frame = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            questionbank_questionrecords_content_frame.addPtrUIHandler(header);
            questionbank_questionrecords_content_frame.setHeaderView(header);
            questionbank_questionrecords_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    questionbank_questionrecords_content_frame.refreshComplete();
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
        TextView questionbank_questionrecords_tab_chapterexercises = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_chapterexercises);
        TextView questionbank_questionrecords_tab_quicktask = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_quicktask);
        TextView questionbank_questionrecords_tab_simulated = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_tab_simulated);
        questionbank_questionrecords_tab_chapterexercises.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        questionbank_questionrecords_tab_quicktask.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        questionbank_questionrecords_tab_simulated.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mModelQuestionBankQuestionRecordView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        LinearLayout questionbank_questionrecords_content = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_content);
        questionbank_questionrecords_content.removeAllViews();
        View questionbank_questionrecords_line1 = null;
        Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                "select answer_edu.test_paper_id as test_paper_id,test_paper_name,time,used_answer_time,question_num,error_num from test_paper_edu \n" +
                        "left join answer_edu on answer_edu.test_paper_id = test_paper_edu.test_paper_id WHERE type='1';", null);
        while (cursor.moveToNext()) {
            int test_paper_idIndex = cursor.getColumnIndex("test_paper_id");
            int test_paper_nameIndex = cursor.getColumnIndex("test_paper_name");
            int timeIndex = cursor.getColumnIndex("time");
            int used_answer_timeIndex = cursor.getColumnIndex("used_answer_time");
            int question_numIndex = cursor.getColumnIndex("question_num");
            int error_numIndex = cursor.getColumnIndex("error_num");
            String test_paper_id = cursor.getString(test_paper_idIndex);
            String test_paper_name = cursor.getString(test_paper_nameIndex);
            String time = cursor.getString(timeIndex);
            String used_answer_time = cursor.getString(used_answer_timeIndex);
            String question_num = cursor.getString(question_numIndex);
            String error_num = cursor.getString(error_numIndex);
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_questionrecord1, null);
            TextView questionbank_questionrecords_testname = view.findViewById(R.id.questionbank_questionrecords_testname);
            questionbank_questionrecords_testname.setText(test_paper_name);
            TextView questionbank_questionrecords_detailstime = view.findViewById(R.id.questionbank_questionrecords_detailstime);
            if (time == null){
                time = "";
            }
            if (used_answer_time == null){
                used_answer_time = "0";
            }
            if (question_num == null){
                question_num = "0";
            }
            if (error_num == null){
                error_num = "";
            }
            time = time.substring(0,time.indexOf(" "));
            questionbank_questionrecords_detailstime.setText(time);
            questionbank_questionrecords_content.addView(view);
            TextView questionbank_questionrecords_detailsduring = view.findViewById(R.id.questionbank_questionrecords_detailsduring);
            questionbank_questionrecords_detailsduring.setText(getStringTime(Integer.valueOf(used_answer_time)));
            TextView questionbank_questionrecords_detailsrightnum = view.findViewById(R.id.questionbank_questionrecords_detailsrightnum);
            int errornum = 0;
            if (!error_num.equals("")) {
                errornum = error_num.split(";").length;
            }
            questionbank_questionrecords_detailsrightnum.setText((Integer.valueOf(question_num) - errornum) + "");
            TextView questionbank_questionrecords_detailserrornum = view.findViewById(R.id.questionbank_questionrecords_detailserrornum);
            questionbank_questionrecords_detailserrornum.setText(errornum + "");
            questionbank_questionrecords_line1 = view.findViewById(R.id.questionbank_questionrecords_line1);
            String finalUsed_answer_time = used_answer_time;
            view.setOnClickListener(v->{
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
                button_cancel.setOnClickListener(View->{
                    mMyDialog.cancel();
                });
                TextView button_sure = view1.findViewById(R.id.button_sure);
                button_sure.setText("再做一遍");
                button_sure.setOnClickListener(View->{//将试卷重新调出来，做题
                    mMyDialog.cancel();
                });
            });
        }
        if (questionbank_questionrecords_line1 != null){
            questionbank_questionrecords_line1.setVisibility(View.INVISIBLE);
        }
    }

    //添加问题界面
    private void QuestionViewAdd(String question_id_group){
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_answerpaper_details = mModelQuestionBankAnswerPaperView.findViewById(R.id.coursedetails_answerpaper_details);
        coursedetails_answerpaper_details.removeAllViews();
        coursedetails_answerpaper_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length){ //不在数组范围直接返回
                return;
            }
            TextView questionbank_answerpaper_questioncountsum = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncountsum);
            questionbank_answerpaper_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_answerpaper_questioncount = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questioncount);
                questionbank_answerpaper_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                        "select question_id,question_name,optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                                + question_id_groupS[mCurrentIndex] + "';", null);
                while (cursor.moveToNext()) {
                    int question_idIndex = cursor.getColumnIndex("question_id");
                    int question_nameIndex = cursor.getColumnIndex("question_name");
                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");
                    String question_id = cursor.getString(question_idIndex);
                    String question_name = cursor.getString(question_nameIndex);
                    String optionanswer = cursor.getString(optionanswerIndex);
                    String question_type = cursor.getString(question_typeIndex);
                    String question_analysis = cursor.getString(question_analysisIndex);
                    TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                    if (mFontSize.equals("nomal")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    questionbank_answerpaper_single_title.setText(question_name);
                    questionbank_answerpaper_single_title.setHint(question_id);
                    TextView questionbank_answerpaper_questiontype = mModelQuestionBankAnswerPaperView.findViewById(R.id.questionbank_answerpaper_questiontype);
                    if (question_type.equals("1")){
                        questionbank_answerpaper_questiontype.setText("[单选题]");
                    } else if (question_type.equals("2")){
                        questionbank_answerpaper_questiontype.setText("[多选题]");
                    } else if (question_type.equals("4")){
                        questionbank_answerpaper_questiontype.setText("[简答题]");
                    } else if (question_type.equals("7")){
                        questionbank_answerpaper_questiontype.setText("[材料题]");
                    }
                    LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                    questionbank_answerpaper_content.removeAllViews();
                    if (question_type.equals("1") || question_type.equals("2")) { //如果是单选题或多选题添加选项布局
                        String[] optionanswerS = optionanswer.split(";");
                        if (optionanswerS != null) {
                            for (int i = 0; i < optionanswerS.length; i ++) {
                                View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                                if (optionanswerS1.length != 3){//question_analysisS1的结构应为#A#是#选择A
                                    continue;
                                }
                                TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                                questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                                questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                                questionbank_answerpaper_option_name.setOnClickListener(v->{
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
                                TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                                questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                                if (mFontSize.equals("nomal")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                                } else if (mFontSize.equals("small")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                                } else if (mFontSize.equals("big")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                                }
                                questionbank_answerpaper_content.addView(view3);
                            }
                        }
                    } else if (question_type.equals("4")){//如果是简答题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                    } else if (question_type.equals("4")){//如果是材料题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                    }
                }
                cursor.close();
            }
        }
    }

    //添加答题卡-问题界面
    private void HandInAnalysisQuestionViewAdd(String question_id_group){
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_handin_analysis_details = mModelQuestionBankHandInAnalysisView.findViewById(R.id.coursedetails_handin_analysis_details);
        coursedetails_handin_analysis_details.removeAllViews();
        coursedetails_handin_analysis_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length){ //不在数组范围直接返回
                return;
            }
            TextView questionbank_handin_analysis_questioncountsum = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncountsum);
            questionbank_handin_analysis_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_handin_analysis_questioncount = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questioncount);
                questionbank_handin_analysis_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                        "select question_id,question_name,optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                                + question_id_groupS[mCurrentIndex] + "';", null);
                while (cursor.moveToNext()) {
                    int question_idIndex = cursor.getColumnIndex("question_id");
                    int question_nameIndex = cursor.getColumnIndex("question_name");
                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");
                    String question_id = cursor.getString(question_idIndex);
                    String question_name = cursor.getString(question_nameIndex);
                    String optionanswer = cursor.getString(optionanswerIndex);
                    String question_type = cursor.getString(question_typeIndex);
                    String question_analysis = cursor.getString(question_analysisIndex);
                    TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                    if (mFontSize.equals("nomal")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    questionbank_answerpaper_single_title.setText(question_name);
                    questionbank_answerpaper_single_title.setHint(question_id);
                    TextView questionbank_handin_analysis_questiontype = mModelQuestionBankHandInAnalysisView.findViewById(R.id.questionbank_handin_analysis_questiontype);
                    if (question_type.equals("1")){
                        questionbank_handin_analysis_questiontype.setText("[单选题]");
                    } else if (question_type.equals("2")){
                        questionbank_handin_analysis_questiontype.setText("[多选题]");
                    } else if (question_type.equals("4")){
                        questionbank_handin_analysis_questiontype.setText("[简答题]");
                    } else if (question_type.equals("7")){
                        questionbank_handin_analysis_questiontype.setText("[材料题]");
                    }
                    LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                    questionbank_answerpaper_content.removeAllViews();
                    if (question_type.equals("1") || question_type.equals("2")) { //如果是单选题或多选题添加选项布局
                        String[] optionanswerS = optionanswer.split(";");
                        if (optionanswerS != null) {
                            for (int i = 0; i < optionanswerS.length; i ++) {
                                View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                                if (optionanswerS1.length != 3){//question_analysisS1的结构应为#A#是#选择A
                                    continue;
                                }
                                TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                                questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                                questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                                TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                                questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                                if (mFontSize.equals("nomal")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                                } else if (mFontSize.equals("small")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                                } else if (mFontSize.equals("big")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                                }
                                questionbank_answerpaper_content.addView(view3);
                            }
                        }
                    } else if (question_type.equals("4")){//如果是简答题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                        //设置不可编辑，交卷以后不能输入
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                        rl.height = 0;
                        rl.topMargin = 0;
                        questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                    } else if (question_type.equals("4")){//如果是材料题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                        //设置不可编辑，交卷以后不能输入
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                        rl.height = 0;
                        rl.topMargin = 0;
                        questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                    }
                }
                cursor.close();
            }
        }
    }

    //添加错题本-问题界面
    private void WrongQuestionViewAdd(String question_id_group){
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_wrongquestion_details = mModelQuestionBankWrongQuestionView.findViewById(R.id.coursedetails_wrongquestion_details);
        coursedetails_wrongquestion_details.removeAllViews();
        coursedetails_wrongquestion_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length){ //不在数组范围直接返回
                return;
            }
            TextView questionbank_wrongquestion_questioncountsum = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncountsum);
            questionbank_wrongquestion_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_wrongquestion_questioncount = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questioncount);
                questionbank_wrongquestion_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                        "select question_id,question_name,optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                                + question_id_groupS[mCurrentIndex] + "';", null);
                while (cursor.moveToNext()) {
                    int question_idIndex = cursor.getColumnIndex("question_id");
                    int question_nameIndex = cursor.getColumnIndex("question_name");
                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");
                    String question_id = cursor.getString(question_idIndex);
                    String question_name = cursor.getString(question_nameIndex);
                    String optionanswer = cursor.getString(optionanswerIndex);
                    String question_type = cursor.getString(question_typeIndex);
                    String question_analysis = cursor.getString(question_analysisIndex);
                    TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                    if (mFontSize.equals("nomal")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    questionbank_answerpaper_single_title.setText(question_name);
                    questionbank_answerpaper_single_title.setHint(question_id);
                    TextView questionbank_wrongquestion_questiontype = mModelQuestionBankWrongQuestionView.findViewById(R.id.questionbank_wrongquestion_questiontype);
                    if (question_type.equals("1")){
                        questionbank_wrongquestion_questiontype.setText("[单选题]");
                    } else if (question_type.equals("2")){
                        questionbank_wrongquestion_questiontype.setText("[多选题]");
                    } else if (question_type.equals("4")){
                        questionbank_wrongquestion_questiontype.setText("[简答题]");
                    } else if (question_type.equals("7")){
                        questionbank_wrongquestion_questiontype.setText("[材料题]");
                    }
                    LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                    questionbank_answerpaper_content.removeAllViews();
                    if (question_type.equals("1") || question_type.equals("2")) { //如果是单选题或多选题添加选项布局
                        String[] optionanswerS = optionanswer.split(";");
                        if (optionanswerS != null) {
                            for (int i = 0; i < optionanswerS.length; i ++) {
                                View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                                if (optionanswerS1.length != 3){//question_analysisS1的结构应为#A#是#选择A
                                    continue;
                                }
                                TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                                questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                                questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                                TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                                questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                                if (mFontSize.equals("nomal")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                                } else if (mFontSize.equals("small")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                                } else if (mFontSize.equals("big")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                                }
                                questionbank_answerpaper_content.addView(view3);
                                questionbank_answerpaper_option_name.setOnClickListener(v->{
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
                    } else if (question_type.equals("4")){//如果是简答题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                    } else if (question_type.equals("4")){//如果是材料题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                    }
                }
                cursor.close();
            }
        }
    }

    //题型展示
    private void QuestionBankDetailsQuestionTypeShow(String question_id_group){
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
        questionbank_questiontype_singlebutton.setOnClickListener(v->{ //直接跳到单选题

        });
        LinearLayout questionbank_questiontype_mutilbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_mutilbutton);
        questionbank_questiontype_mutilbutton.setOnClickListener(v->{ //直接跳到多选题

        });
        LinearLayout questionbank_questiontype_shortanswerbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_shortanswerbutton);
        questionbank_questiontype_shortanswerbutton.setOnClickListener(v->{ //直接跳到简答题

        });
        LinearLayout questionbank_questiontype_materialbutton = mModelQuestionBankQuestionTypeView.findViewById(R.id.questionbank_questiontype_materialbutton);
        questionbank_questiontype_materialbutton.setOnClickListener(v->{ //直接跳到材料题

        });
    }
    //添加收藏题本-问题界面
    private void CollectionQuestionViewAdd(String question_id_group){
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_single, null);
        LinearLayout coursedetails_mycollextionquestion_details = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.coursedetails_mycollextionquestion_details);
        coursedetails_mycollextionquestion_details.removeAllViews();
        coursedetails_mycollextionquestion_details.addView(view2);
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
        if (question_id_groupS != null) {
            if (mCurrentIndex < 0 || mCurrentIndex >= question_id_groupS.length){ //不在数组范围直接返回
                return;
            }
            TextView questionbank_mycollextionquestion_questioncountsum = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncountsum);
            questionbank_mycollextionquestion_questioncountsum.setText("/" + question_id_groupS.length);
            if (question_id_groupS.length > 0) {
                TextView questionbank_mycollextionquestion_questioncount = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questioncount);
                questionbank_mycollextionquestion_questioncount.setText(String.valueOf(mCurrentIndex + 1));
                Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                        "select question_id,question_name,optionanswer,question_type,question_analysis from test_questions_edu WHERE question_id='"
                                + question_id_groupS[mCurrentIndex] + "';", null);
                while (cursor.moveToNext()) {
                    int question_idIndex = cursor.getColumnIndex("question_id");
                    int question_nameIndex = cursor.getColumnIndex("question_name");
                    int optionanswerIndex = cursor.getColumnIndex("optionanswer");
                    int question_typeIndex = cursor.getColumnIndex("question_type");//1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                    int question_analysisIndex = cursor.getColumnIndex("question_analysis");
                    String question_id = cursor.getString(question_idIndex);
                    String question_name = cursor.getString(question_nameIndex);
                    String optionanswer = cursor.getString(optionanswerIndex);
                    String question_type = cursor.getString(question_typeIndex);
                    String question_analysis = cursor.getString(question_analysisIndex);
                    TextView questionbank_answerpaper_single_title = view2.findViewById(R.id.questionbank_answerpaper_single_title);
                    if (mFontSize.equals("nomal")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                    } else if (mFontSize.equals("small")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                    } else if (mFontSize.equals("big")){
                        questionbank_answerpaper_single_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                    }
                    questionbank_answerpaper_single_title.setText(question_name);
                    questionbank_answerpaper_single_title.setHint(question_id);
                    TextView questionbank_mycollextionquestion_questiontype = mModelQuestionBankMyCollectionQuestionView.findViewById(R.id.questionbank_mycollextionquestion_questiontype);
                    if (question_type.equals("1")){
                        questionbank_mycollextionquestion_questiontype.setText("[单选题]");
                    } else if (question_type.equals("2")){
                        questionbank_mycollextionquestion_questiontype.setText("[多选题]");
                    } else if (question_type.equals("4")){
                        questionbank_mycollextionquestion_questiontype.setText("[简答题]");
                    } else if (question_type.equals("7")){
                        questionbank_mycollextionquestion_questiontype.setText("[材料题]");
                    }
                    LinearLayout questionbank_answerpaper_content = view2.findViewById(R.id.questionbank_answerpaper_content);
                    questionbank_answerpaper_content.removeAllViews();
                    if (question_type.equals("1") || question_type.equals("2")) { //如果是单选题或多选题添加选项布局
                        String[] optionanswerS = optionanswer.split(";");
                        if (optionanswerS != null) {
                            for (int i = 0; i < optionanswerS.length; i ++) {
                                View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_option, null);
                                String[] optionanswerS1 = optionanswerS[i].substring(1, optionanswerS[i].length()).split("#");
                                if (optionanswerS1.length != 3){//question_analysisS1的结构应为#A#是#选择A
                                    continue;
                                }
                                TextView questionbank_answerpaper_option_name = view3.findViewById(R.id.questionbank_answerpaper_option_name);
                                questionbank_answerpaper_option_name.setText(optionanswerS1[0]);
                                questionbank_answerpaper_option_name.setHint(optionanswerS1[1]);
                                TextView questionbank_answerpaper_option_title = view3.findViewById(R.id.questionbank_answerpaper_option_title);
                                questionbank_answerpaper_option_title.setText(optionanswerS1[2]);
                                if (mFontSize.equals("nomal")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                                } else if (mFontSize.equals("small")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                                } else if (mFontSize.equals("big")){
                                    questionbank_answerpaper_option_title.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                                }
                                questionbank_answerpaper_content.addView(view3);
                            }
                        }
                    } else if (question_type.equals("4")){//如果是简答题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                        //设置不可编辑，交卷以后不能输入
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                        rl.height = 0;
                        rl.topMargin = 0;
                        questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                    } else if (question_type.equals("4")){//如果是材料题
                        View view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerpaper_shortanswer, null);
                        questionbank_answerpaper_content.addView(view3);
                        EditText questionbank_answerpaper_shortansweredittext = view3.findViewById(R.id.questionbank_answerpaper_shortansweredittext);
                        if (mFontSize.equals("nomal")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize17));
                        } else if (mFontSize.equals("small")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize14));
                        } else if (mFontSize.equals("big")){
                            questionbank_answerpaper_shortansweredittext.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,view2.getResources().getDimensionPixelSize(R.dimen.textsize20));
                        }
                        //设置不可编辑，交卷以后不能输入
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) questionbank_answerpaper_shortansweredittext.getLayoutParams();
                        rl.height = 0;
                        rl.topMargin = 0;
                        questionbank_answerpaper_shortansweredittext.setLayoutParams(rl);
                    }
                }
                cursor.close();
            }
        }
    }

    //添加答题卡界面
    private void AnswerQuestionCardViewAdd(String question_id_group){
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
        String[] question_id_groupS = question_id_group.substring(1,question_id_group.length()).split("#");
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
            Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(//question_type 1单选题2多选题_3判断题_4简答题_5不定项_6填空题_7材料题___
                    "SELECT question_type,tf_marked from test_questions_edu " +
                            "LEFT JOIN student_question_edu on test_questions_edu.question_id=student_question_edu.question_id " +
                            "where test_questions_edu.question_id in (" + question_id_group.substring(1,question_id_group.length()).replace("#",",") +
                            ") and test_questions_edu.tf_delete='2' ;", null);
            while (cursor.moveToNext()) {
                int question_typeIndex = cursor.getColumnIndex("question_type");
                int tf_markedIndex = cursor.getColumnIndex("tf_marked");
                String question_type = cursor.getString(question_typeIndex);
                String tf_marked = cursor.getString(tf_markedIndex);
                if (question_type.equals("1")){
                    if (singleView == null){
                        singleView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard1, null);
                        coursedetails_answerquestioncard_details.addView(singleView);
                    }
                    GridLayout coursedetails_answerquestioncard_questionnumber = singleView.findViewById(R.id.coursedetails_answerquestioncard_questionnumber);
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_questionbank_answerquestioncard2, null);
                    coursedetails_answerquestioncard_questionnumber.addView(view);
                    TextView questionbank_answerquestioncard2_select = view.findViewById(R.id.questionbank_answerquestioncard2_select);
                    questionbank_answerquestioncard2_select.setText("" + (count + 1));
                    if (tf_marked != null){
                        if (tf_marked.equals("1")){//标记此题
                            ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                            questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
                        questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                        questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
                    }
                    int finalCount = count;
                    questionbank_answerquestioncard2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                        mCurrentIndex = finalCount;
                        QuestionBankDetailsQuestionModeShow();
                    });
                } else if (question_type.equals("2")){
                    if (mutilView == null){
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
                    if (tf_marked != null){
                        if (tf_marked.equals("1")){//标记此题
                            ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                            questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
                        questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                        questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
                    }
                    int finalCount = count;
                    questionbank_answerquestioncard2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                        mCurrentIndex = finalCount;
                        QuestionBankDetailsQuestionModeShow();
                    });
                } else if (question_type.equals("4")){
                    if (shortAnswerView == null){
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
                    if (tf_marked != null){
                        if (tf_marked.equals("1")){//标记此题
                            ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                            questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
                        questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                        questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
                    }
                    int finalCount = count;
                    questionbank_answerquestioncard2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                        mCurrentIndex = finalCount;
                        QuestionBankDetailsQuestionModeShow();
                    });
                } else if (question_type.equals("7")){
                    if (materialView == null){
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
                    if (tf_marked != null){
                        if (tf_marked.equals("1")){//标记此题
                            ImageView questionbank_answerquestioncard2_sign = view.findViewById(R.id.questionbank_answerquestioncard2_sign);
                            questionbank_answerquestioncard2_sign.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mCurrentIndex == count){ //此题为当前正在答的题,改变题的颜色
                        questionbank_answerquestioncard2_select.setTextColor(view.getResources().getColor(R.color.white));
                        questionbank_answerquestioncard2_select.setBackground(view.getResources().getDrawable(R.drawable.textview_style_circle_green));
                    }
                    int finalCount = count;
                    questionbank_answerquestioncard2_select.setOnClickListener(v->{ //点击题号。跳转到指定题
                        mCurrentIndex = finalCount;
                        QuestionBankDetailsQuestionModeShow();
                    });
                }
                count ++;
            }
            cursor.close();
//        }
        TextView coursedetails_answerquestioncard_commit = mModelQuestionBankAnswerQuestionCardView.findViewById(R.id.coursedetails_answerquestioncard_commit);
        coursedetails_answerquestioncard_commit.setOnClickListener(v->{
            //显示交卷界面
            QuestionBankDetailsHandInPaperShow(question_id_group);
        });
    }

    //隐藏所有图层
    private void HideAllLayout(){
        RelativeLayout fragmentquestionbank_main = mview.findViewById(R.id.fragmentquestionbank_main);
        fragmentquestionbank_main.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.questionbank_sub_details_tab_chapterexercises: {
                if (!mCurrentTab.equals("ChapterExercises")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 3,0 , 0, 0);
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
                QuestionBankDetailsChapterShow();
                mLastTabIndex = 1;
                mCurrentTab = "ChapterExercises";
                break;
            }
            case R.id.questionbank_sub_details_tab_quicktask: {
                if (!mCurrentTab.equals("QuickTask")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 3,width / 3 , 0, 0);
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
                mCurrentTab = "QuickTask";
                break;
            }
            case R.id.questionbank_sub_details_tab_simulated: {
                if (!mCurrentTab.equals("Simulated")) {
                    ImageView questionbank_sub_details_cursor1 = mModelQuestionBankDetailsView.findViewById(R.id.questionbank_sub_details_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 3,width* 2 / 3, 0, 0);
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
            case R.id.questionbank_questionrecords_tab_chapterexercises: {
                if (!mQuestionRecordCurrentTab.equals("ChapterExercises")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation(( mQuestionRecordLastTabIndex - 1)  * width / 3,0 , 0, 0);
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
            case R.id.questionbank_questionrecords_tab_quicktask: {
                if (!mQuestionRecordCurrentTab.equals("QuickTask")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation(( mQuestionRecordLastTabIndex - 1)  * width / 3,width / 3 , 0, 0);
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
            case R.id.questionbank_questionrecords_tab_simulated: {
                if (!mQuestionRecordCurrentTab.equals("Simulated")) {
                    ImageView questionbank_questionrecords_cursor1 = mModelQuestionBankQuestionRecordView.findViewById(R.id.questionbank_questionrecords_cursor1);
                    Animation animation = new TranslateAnimation(( mQuestionRecordLastTabIndex - 1)  * width / 3,width* 2 / 3, 0, 0);
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
            case R.id.questionbank_sub_details_buttonmore:{
                showPop();
                toggleBright();
                break;
            }
            case R.id.pop_add_mycollect:{
                if (mPopupWindow != null){
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeMyCollectionQuestionShow();
                break;
            }
            case R.id.pop_add_mywrong:{
                if (mPopupWindow != null){
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeWrongQuestionShow();
                break;
            }
            case R.id.pop_add_myrecord:{
                if (mPopupWindow != null){
                    mPopupWindow.dismiss();
                }
                QuestionBankDetailsQuestionModeQuestionRecordShow();
                break;
            }
            default:
                break;
        }
    }

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

    private void ShowPopFontSize(String question_id_group,ImageView imageView) {
        if (mPointoutPopupWindow == null) {
            mPointoutPopupWindow = new PopupWindow(mControlMainActivity);
            mPointoutAnimUtil = new ModelAnimUtil();
        }
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        mPointoutAnimUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        mPointoutAnimUtil.addUpdateListener(progress->{
            // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
            bgPointoutAlpha = bPointoutRight ? progress : (START_ALPHA + END_ALPHA - progress);
            backgroundAlpha(bgPointoutAlpha);
        });
        mPointoutAnimUtil.addEndListner(animator-> {
            // 在一次动画结束的时候，翻转状态
            bPointoutRight = !bPointoutRight;
        });
        mPointoutAnimUtil.startAnimator();

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
            mPointoutAnimUtil.addUpdateListener(progress->{
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgPointoutAlpha = bPointoutRight ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgPointoutAlpha);
            });
            mPointoutAnimUtil.addEndListner(animator-> {
                // 在一次动画结束的时候，翻转状态
                bPointoutRight = !bPointoutRight;
            });
            mPointoutAnimUtil.startAnimator();
        });
        TextView fontsizesmall = view.findViewById(R.id.fontsizesmall);
        TextView fontsizenomal = view.findViewById(R.id.fontsizenomal);
        TextView fontsizebig = view.findViewById(R.id.fontsizebig);
        if (mFontSize.equals("small")){
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.blue649cf0));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
        } else if (mFontSize.equals("nomal")){
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.blue649cf0));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
        } else if (mFontSize.equals("big")){
            fontsizesmall.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizenomal.setTextColor(view.getResources().getColor(R.color.collectdefaultcolor));
            fontsizebig.setTextColor(view.getResources().getColor(R.color.blue649cf0));
        }
        if (mCurrentAnswerMode.equals("handin")){
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

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(progress->{
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            });
        animUtil.addEndListner(animator-> {
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
}
