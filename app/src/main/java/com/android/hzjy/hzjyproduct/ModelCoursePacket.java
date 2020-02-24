package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hzjy.hzjyproduct.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.ActionN;


public class ModelCoursePacket extends Fragment implements ModelCoursePacketCover.ModelCoursePacketCoverOnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext = "xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mView;
    private int height = 1344;
    private int width = 720;

    private ModelSearchView searchView = null;
    //弹出窗口（筛选条件）
    private PopupWindow popupWindow;
    //一级搜索
    private String mCoursePacketSelectTemp = "-1";
    private String mCoursePacketSelect = "-1";
    //二级搜索
    private String mCoursePacketSelectTemp1 = "-1";
    private String mCoursePacketSelect1 = "-1";
    //排序方式搜索
    private String mCoursePacketSelectSortTemp = "-1";
    private String mCoursePacketSelectSort = "-1";
    private static final String TAG = "ModelCoursePacket";

    private List<CoursePacketBean.DataBean> dataBeans;
    private List<CoursePacketBean.DataBean> coursePacketBeanData;
    private SmartRefreshLayout mSmart_fragment_coursepacket;

    public static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage) {
        mContext = context;
        mControlMainActivity = content;
        ModelCoursePacket myFragment = new ModelCoursePacket();
        FragmentPage = iFragmentPage;
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(FragmentPage, container, false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        if (mContext.equals("课程包:")) {
            CoursePacketMainShow(1);
        } else {
            CoursePacketMainShow(0);
        }
        // 3. 绑定组件
        searchView = mView.findViewById(R.id.search_view);
        searchView.init("coursepacketsearchrecords");
        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容,.
        searchView.setOnClickSearch((string) -> {
            System.out.println("我收到了" + string);
            //搜索的接口数据
            //getModelSreachViewData();

        });
        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(() -> {
            mControlMainActivity.Page_MoreCoursePacket();
        });

//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) coursepacket_store_house_ptr_frame.getLayoutParams();
//        lp.topMargin = width / 10;
//        coursepacket_store_house_ptr_frame.setLayoutParams(lp);
        mSmart_fragment_coursepacket = mView.findViewById(R.id.Smart_fragment_coursepacket);
        mSmart_fragment_coursepacket.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmart_fragment_coursepacket.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (dataBeans!=null){
                    dataBeans.clear();
                }
                mSmart_fragment_coursepacket.finishRefresh();
//                getModelCoursePacketDatas();
//                 getConditionQuery();
            }
        });
        return mView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void CoursePacketMainShow(int returnString) { // returnString:  0:显示返回按钮
        if (mView == null) {
            return;
        }
        //主要参数
        HideAllLayout();
        RelativeLayout coursepacket_mainLayout = mView.findViewById(R.id.coursepacket_mainLayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_mainLayout.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        coursepacket_mainLayout.setLayoutParams(LP);
        coursepacket_mainLayout.setVisibility(View.VISIBLE);
        //关键字的查询
        RelativeLayout coursepacket_titleRelativeLayout = mView.findViewById(R.id.coursepacket_titleRelativeLayout);
        //二级列表查询
        RelativeLayout coursepacket_titleRelativeLayout1 = mView.findViewById(R.id.coursepacket_titleRelativeLayout1);
        if (returnString == 0) {
            mContext = "课程包:首页";
            coursepacket_titleRelativeLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout.getLayoutParams();
            lp.height = (int) mView.getResources().getDimension(R.dimen.dp44);
            coursepacket_titleRelativeLayout.setLayoutParams(lp);
            lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout1.getLayoutParams();
            lp.topMargin = 0;
            coursepacket_titleRelativeLayout1.setLayoutParams(lp);
        } else {
            mContext = "课程包:";
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout1.getLayoutParams();
            lp.height = (int) mView.getResources().getDimension(R.dimen.dp44);
            coursepacket_titleRelativeLayout1.setLayoutParams(lp);
            coursepacket_titleRelativeLayout.setVisibility(View.INVISIBLE);
            lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout.getLayoutParams();
            lp.height = 0;
            coursepacket_titleRelativeLayout.setLayoutParams(lp);
        }
        ScrollView coursepacket_block_menu_scroll_view = mView.findViewById(R.id.coursepacket_block_menu_scroll_view);
        coursepacket_block_menu_scroll_view.scrollTo(0, 0);
        //课程包列表
        //List<CoursePacketInfo> CoursePacketInfoList = new ArrayList<>();
//        CoursePacketInfo CoursePacketInfo1 = new CoursePacketInfo();
//        CoursePacketInfo1.mCoursePacketCover = "http://image.yunduoketang.com/course/34270/20190829/394d6b0a-6c3a-4121-b12c-ef53afff866d.png";
//        CoursePacketInfo1.mCoursePacketLearnPersonNum = "1000";
//        CoursePacketInfo1.mCoursePacketName = "皇家内训班";
//        CoursePacketInfo1.mCoursePacketPrice = "免费";
//        CoursePacketInfo1.mCoursePacketPriceOld = "1000";
//        CoursePacketInfo1.mCoursePacketStageNum = "3";
//        CoursePacketInfo1.mCoursePacketCourseNum = "6";
//        CoursePacketInfo1.mCoursePacketMessage = "皇家内训";
//        CoursePacketInfo1.mCoursePacketDetails = "<p>&nbsp;&nbsp;随着2017年一级注册消防工程师考试的报名结束，沉寂一段时间的二消又立刻成为了考证大军们新一轮关注的焦点。二消的报考条件比一级宽松，学历门槛较低，考试难度相对较小，而且一二级证书之间互不约束，所以除了吸引更多自身符合报考资格的意向考友外，其中还不乏一些已经参加一级考试的考生踊跃参与，这些都使得二级证书的前景更加明朗，竞争也更加激烈。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;但是今年的二消在业内预估的时间内仍是迟迟未开考，这同样令一部分已提前准备的考友加剧了忧思，准备了这么久，能等到二级的开考吗？据官方内部消息透露，今年时机不对，年底前二级已不会有开考可能，但是2018年将是二级正式走入执业证书市场的最佳时机。内部分析如下，供参考。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;首先，关于执业资格证的&ldquo;三年一大变&rdquo;规律可知，参考同类型的注册建造师是从一级考试开考的第三年之后，就开始大举改革教材，而2017年恰好是一级消防工程师的第3次开考，本着相似的改革规律，2016年消防工程师考试已经更新过教材，那么2018年能实施的重要举措极有可能就是二消的开考了。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;其次，公安部现已发布了两个重要文件。其中，14年公安令129号令中的《社会消防技术服务管理规定》明确表示消防相关从业单位，消防重点单位，消防安装、维保检测单位必须具备相应条件才可以继续升级资质，具体要求见下表。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
////                "<div style=\"text-align:center;\"><img src=\"http://image.yunduoketang.com/editor/14972/20170918/924ca0ac-3837-48f4-889d-a6649ac20245.png\" alt=\"\" /></div>"+
//                "<p style=\"text-align: center;\"><img alt=\"\" src=\"http://image.yunduoketang.com/editor/14972/20170918/924ca0ac-3837-48f4-889d-a6649ac20245.png\"  />&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;上表分析可知，6人以上的要求中，一级需3人以上，那么剩下人数的必然就是二级的了。但由相关数据整理可知，开考两年后，一级消防工程师全国现通过人数仅约12566人，远达不到目前国内消防企业或单位所需的人才匹配量，因此，将部分人才压力的缓解寄希望于二消也是业内的大势所趋，那么尽快开放二消考试，保障人数的供需不平衡就是18年的重中之重了。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;而且今年3月底发布的公安令143号文件也明确说明，《注册消防工程师管理规定》从2017年10月1日就要开始正式实施了。其中第三十三条规定还强调，注册消防工程师不得同时在两个以上消防技术服务机构，或者消防安全重点单位执业。这一点，无疑又加剧了市场的落差。但目前的行业内情况就是粥少僧多，而国家又要在今年如期推行该规定，在一消证书不足的情况，二消证书的重要性可想而知，已是迫在眉睫。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;最后，在129令后，全国的很多省份为了弥补二级消防证书的空缺，以及减少消防技术服务机构等的资质审核排队情况，推出了&ldquo;临时二级证书&rdquo;的政策，只要一级的成绩达到相应的分数，或者参加消防技术服务综合考试合格者均可，但该证书肯定会一定的时效，如辽宁和安徽省，有效时间只截止到2018年的12月31号，那么18年后必然要有正规的证书加以衔接，这也是18年推举二消考试的最佳时机。所以说临时二级证书只是一种过渡性的举措，正是因为试点该证书的受面广泛，才让业内对明年的二消考试有了更大的期许。</p>\n" +
//                "\n" +
//                "<p>&nbsp;</p>\n" +
//                "\n" +
//                "<p>&nbsp; &nbsp;根据今年公安部消防局的各大消防安全检查通知和防范救援的投入可知，国家高层现在非常重视消防安全和防范救援，因此今年10月份&ldquo;十九大&rdquo;的召开，也极有可能会为明年二消的开考添一把火，据业内消息预估开考时间为明年的上半年。故在综合形势下，北京火种教育提醒大家，借鉴一消在考前三月突然公开报名的先例和以上的内部主流分析，明年有意二消考试的同学也需要提前做好准备，打有备之战。</p>\n";
//        StageCourseInfo StageCourseInfo1 = new StageCourseInfo();
//        StageCourseInfo1.mStageCourseId = "0";
//        StageCourseInfo1.mStageCourseName = "课程包阶段1";
//        StageCourseInfo1.mStageCourseDescribe = "可厉害";
//        StageCourseInfo1.mStageCourseIsSale = "0";
////        StageCourseInfo1.mStageCourseOrder = "1";
//        CourseInfo CourseInfo1 = new CourseInfo();
//        CourseInfo1.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
//        CourseInfo1.mCourseLearnPersonNum = "1000";
//        CourseInfo1.mCourseName = "开学典礼";
//        CourseInfo1.mCoursePrice = "免费";
//        CourseInfo1.mCourseType = "直播";
//        CourseInfo1.mCoursePriceOld = "5000.00";
////        CourseInfo1.mCourseOrder = "1";
//        StageCourseInfo1.mCourseInfoList.add(CourseInfo1);
//
//        CourseInfo CourseInfo2 = new CourseInfo();
//        CourseInfo2.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
//        CourseInfo2.mCourseLearnPersonNum = "1000";
//        CourseInfo2.mCourseName = "开学典礼";
//        CourseInfo2.mCoursePrice = "免费";
//        CourseInfo2.mCourseType = "直播";
//        CourseInfo2.mCoursePriceOld = "5000.00";
////        CourseInfo1.mCourseOrder = "1";
////        StageCourseInfo1.mCourseInfoList.add(CourseInfo2);
//        StageCourseInfo StageCourseInfo2 = new StageCourseInfo();
//        StageCourseInfo2.mStageCourseId = "0";
//        StageCourseInfo2.mStageCourseName = "课程包阶段2";
//        StageCourseInfo2.mStageCourseDescribe = "可厉害";
//        StageCourseInfo2.mStageCourseIsSale = "0";
//        StageCourseInfo2.mCourseInfoList.add(CourseInfo2);
//        CoursePacketInfo1.mStageCourseInfoList.add(StageCourseInfo1);
//        CoursePacketInfo1.mStageCourseInfoList.add(StageCourseInfo2);
//        TeacherInfo TeacherInfo1 = new TeacherInfo();
//        TeacherInfo1.mTeacherName = "张哲";
//        TeacherInfo1.mTeacherMessage = "更高";
//        TeacherInfo1.mTeacherCover = "";
//        TeacherInfo TeacherInfo2 = new TeacherInfo();
//        TeacherInfo2.mTeacherName = "江山";
//        TeacherInfo2.mTeacherMessage = "更帅";
//        TeacherInfo2.mTeacherCover = "";
//        TeacherInfo TeacherInfo3 = new TeacherInfo();
//        TeacherInfo3.mTeacherName = "徐华洲";
//        TeacherInfo3.mTeacherMessage = "更强";
//        TeacherInfo3.mTeacherCover = "";
//        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo1);
//        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo2);
//        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo3);
//        CoursePacketInfoList.add(CoursePacketInfo1);
//        CoursePacketInfo CoursePacketInfo2 = new CoursePacketInfo();
//        CoursePacketInfo2.mCoursePacketCover = "";
//        CoursePacketInfo2.mCoursePacketLearnPersonNum = "1000";
//        CoursePacketInfo2.mCoursePacketPriceOld = "1000";
//        CoursePacketInfo2.mCoursePacketName = "课程包呀";
//        CoursePacketInfo2.mCoursePacketPrice = "900000";
//        CoursePacketInfo2.mCoursePacketStageNum = "3";
//        CoursePacketInfo2.mCoursePacketCourseNum = "6";
//        CoursePacketInfo2.mCoursePacketMessage = "课程包呀  这个昂贵";
//        CoursePacketInfo2.mCoursePacketDetails = "";
//        CoursePacketInfo2.mStageCourseInfoList.add(StageCourseInfo1);
//        CoursePacketInfoList.add(CoursePacketInfo2);
//        CoursePacketInfo CoursePacketInfo3 = new CoursePacketInfo();
//        CoursePacketInfo3.mCoursePacketCover = "";
//        CoursePacketInfo3.mCoursePacketLearnPersonNum = "1000";
//        CoursePacketInfo3.mCoursePacketPriceOld = "90000";
//        CoursePacketInfo3.mCoursePacketName = "课程包呀";
//        CoursePacketInfo3.mCoursePacketPrice = "100";
//        CoursePacketInfo3.mCoursePacketStageNum = "3";
//        CoursePacketInfo3.mCoursePacketCourseNum = "6";
//        CoursePacketInfo3.mCoursePacketMessage = "课程包呀  这个便宜";
//        CoursePacketInfo3.mCoursePacketDetails = "";
//        CoursePacketInfoList.add(CoursePacketInfo3);

        LinearLayout coursepacket_linearlayout = mView.findViewById(R.id.coursepacket_linearlayout);
        coursepacket_linearlayout.removeAllViews();
        //网络数据的集合dataBean
        getModelCoursePacketDatas();
    }

    public void CoursePacketMainSearchShow() {
        if (mView == null) {
            return;
        }
        HideAllLayout();
        RelativeLayout coursepacket_searchlayout = mView.findViewById(R.id.coursepacket_searchlayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_searchlayout.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        coursepacket_searchlayout.setLayoutParams(LP);
        coursepacket_searchlayout.setVisibility(View.VISIBLE);
    }

    public void CoursePacketMainSearchConditionShow() {
        initPopupWindow();
    }

    //隐藏所有图层
    private void HideAllLayout() {
        RelativeLayout coursepacket_mainLayout = mView.findViewById(R.id.coursepacket_mainLayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_mainLayout.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        coursepacket_mainLayout.setLayoutParams(LP);
        coursepacket_mainLayout.setVisibility(View.INVISIBLE);
        RelativeLayout coursepacket_searchlayout = mView.findViewById(R.id.coursepacket_searchlayout);
        LP = (LinearLayout.LayoutParams) coursepacket_searchlayout.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        coursepacket_searchlayout.setLayoutParams(LP);
        coursepacket_searchlayout.setVisibility(View.INVISIBLE);
        RelativeLayout coursepacket_details1 = mView.findViewById(R.id.coursepacket_details1);
        LP = (LinearLayout.LayoutParams) coursepacket_details1.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        coursepacket_details1.setLayoutParams(LP);
        coursepacket_details1.setVisibility(View.INVISIBLE);
    }

    /**
     *   * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *   *
     *   
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    protected void initPopupWindow() {
        View popupWindowView = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_coursepacket_selectpop, null);
        int height1 = (int) (getScreenHeight() - mView.getResources().getDimension(R.dimen.dp45) - getStateBar());
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, (int) mView.getResources().getDimension(R.dimen.dp_280), height1, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(mControlMainActivity.getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.RIGHT, 0, 500);
        popupWindow.setBackgroundDrawable(null);
        //设置背景半透明
        backgroundAlpha(0.9f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener((v, event) -> {
            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });
        //添加一级搜索标签
        ControllerWarpLinearLayout coursepacket_select_warpLinearLayout1 = popupWindowView.findViewById(R.id.coursepacket_select_warpLinearLayout1);
        coursepacket_select_warpLinearLayout1.removeAllViews();
        //添加二级搜索标签
        ControllerWarpLinearLayout coursepacket_select_warpLinearLayout2 = popupWindowView.findViewById(R.id.coursepacket_select_warpLinearLayout2);
        coursepacket_select_warpLinearLayout2.removeAllViews();
        mCoursePacketSelectTemp1 = mCoursePacketSelect1;   //二级
        mCoursePacketSelectTemp = mCoursePacketSelect;     //一级
        //必须有的标签-全部:默认选中全部   1级列表标签
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_coursepacket_selectpop_child, null);
            TextView coursepacket_selectpop_child_signname = view.findViewById(R.id.coursepacket_selectpop_child_signname);
            coursepacket_selectpop_child_signname.setText("全部");
            coursepacket_selectpop_child_signname.setHint("-1");
            coursepacket_select_warpLinearLayout1.addView(view);
            view.setOnClickListener(v -> {
                //将其他置为未选中
                String hint = "";
                int childcount = coursepacket_select_warpLinearLayout1.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View childView = coursepacket_select_warpLinearLayout1.getChildAt(i);
                    if (childView == null) {
                        continue;
                    }
                    TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                    if (childView == view) {
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        hint = coursepacket_selectpop_child_signname1.getHint().toString();
                    } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                    }
                }
                //将选中项置为当前选中项id
                mCoursePacketSelectTemp = hint;
            });
            if (mCoursePacketSelect.equals("-1")) {
                coursepacket_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                coursepacket_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //测试数据
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_coursepacket_selectpop_child, null);
            TextView coursepacket_selectpop_child_signname = view.findViewById(R.id.coursepacket_selectpop_child_signname);
            coursepacket_select_warpLinearLayout1.addView(view);
            coursepacket_selectpop_child_signname.setHint("2");
            coursepacket_selectpop_child_signname.setText("我是科二基础事务");
            view.setOnClickListener(v -> {
                //将其他置为未选中
                String hint = "";
                int childcount = coursepacket_select_warpLinearLayout1.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View childView = coursepacket_select_warpLinearLayout1.getChildAt(i);
                    if (childView == null) {
                        continue;
                    }
                    TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
                    if (childView == view) {
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                        hint = coursepacket_selectpop_child_signname1.getHint().toString();
                    } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                        coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                    }
                }
                //将选中项置为当前选中项id
                mCoursePacketSelectTemp = hint;
            });
            if (mCoursePacketSelect.equals(coursepacket_selectpop_child_signname.getHint().toString())) {
                coursepacket_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                coursepacket_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //添加排序方式搜索标签
        ControllerWarpLinearLayout coursepacket_select_warpLinearLayout3 = popupWindowView.findViewById(R.id.coursepacket_select_warpLinearLayout3);
        coursepacket_select_warpLinearLayout3.removeAllViews();
        mCoursePacketSelectSortTemp = mCoursePacketSelectSort;
        //必须有的标签-综合:默认选中综合
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_coursepacket_selectpop_child, null);
            TextView coursepacket_selectpop_child_signname = view.findViewById(R.id.coursepacket_selectpop_child_signname);
            coursepacket_selectpop_child_signname.setText("综合");
            coursepacket_selectpop_child_signname.setHint("-1");
            coursepacket_select_warpLinearLayout3.addView(view);
            view.setOnClickListener(v -> {
                //将其他置为未选中
                String hint = "";
                int childcount = coursepacket_select_warpLinearLayout3.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View childView = coursepacket_select_warpLinearLayout3.getChildAt(i);
                    if (childView == null) {
                        continue;
                    }
                    TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                    if (childView == view) {
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        hint = coursepacket_selectpop_child_signname1.getHint().toString();
                    } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectSortTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                    }
                }
                //将选中项置为当前选中项id
                mCoursePacketSelectSortTemp = hint;
            });
            if (mCoursePacketSelectSort.equals("-1")) {
                coursepacket_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                coursepacket_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //必须有的标签-按热度
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_coursepacket_selectpop_child, null);
            TextView coursepacket_selectpop_child_signname = view.findViewById(R.id.coursepacket_selectpop_child_signname);
            coursepacket_selectpop_child_signname.setText("按热度");
            coursepacket_selectpop_child_signname.setHint("0");
            coursepacket_select_warpLinearLayout3.addView(view);
            view.setOnClickListener(v -> {
                //将其他置为未选中
                String hint = "";
                int childcount = coursepacket_select_warpLinearLayout3.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View childView = coursepacket_select_warpLinearLayout3.getChildAt(i);
                    if (childView == null) {
                        continue;
                    }
                    TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                    if (childView == view) {
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        hint = coursepacket_selectpop_child_signname1.getHint().toString();
                    } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectSortTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                        coursepacket_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        coursepacket_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                    }
                }
                //将选中项置为当前选中项id
                mCoursePacketSelectSortTemp = hint;
            });
            if (mCoursePacketSelectSort.equals("0")) {
                coursepacket_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                coursepacket_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //点击确定
        TextView communityanswer_select_buttonsure = popupWindowView.findViewById(R.id.coursepacket_select_buttonsure);
        communityanswer_select_buttonsure.setOnClickListener(v -> {
            mCoursePacketSelect = mCoursePacketSelectTemp;
            mCoursePacketSelect1 = mCoursePacketSelectTemp1;
            mCoursePacketSelectSort = mCoursePacketSelectSortTemp;
            //条件筛选缺一级分类和二级分类
            //网络加载数据传相应的参数 刷新页面
           getConditionQuery();
            //返回数据
            popupWindow.dismiss();

        });
        //点击重置
        TextView communityanswer_select_buttonreset = popupWindowView.findViewById(R.id.coursepacket_select_buttonreset);
        communityanswer_select_buttonreset.setOnClickListener(v -> {
            //将其他置为未选中
            int childcount = coursepacket_select_warpLinearLayout1.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View childView = coursepacket_select_warpLinearLayout1.getChildAt(i);
                if (childView == null) {
                    continue;
                }
                TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                int padding = (int) childView.getResources().getDimension(R.dimen.dp5);
                if (coursepacket_selectpop_child_signname1.getHint().toString().equals("-1")) {
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.white));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.grayff999999));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                }
            }
            mCoursePacketSelectTemp = "-1";
            mCoursePacketSelect = "-1";
            childcount = coursepacket_select_warpLinearLayout3.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View childView = coursepacket_select_warpLinearLayout3.getChildAt(i);
                if (childView == null) {
                    continue;
                }
                TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                int padding = (int) childView.getResources().getDimension(R.dimen.dp5);
                if (coursepacket_selectpop_child_signname1.getHint().toString().equals("-1")) {
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.white));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectSortTemp)) { // 如果上个找到上一个选中的id，将其置为未选状态
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.grayff999999));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                }
            }
            mCoursePacketSelectSortTemp = "-1";
            mCoursePacketSelectSort = "-1";
            childcount = coursepacket_select_warpLinearLayout2.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View childView = coursepacket_select_warpLinearLayout2.getChildAt(i);
                if (childView == null) {
                    continue;
                }
                TextView coursepacket_selectpop_child_signname1 = childView.findViewById(R.id.coursepacket_selectpop_child_signname);
                int padding = (int) childView.getResources().getDimension(R.dimen.dp5);
                if (coursepacket_selectpop_child_signname1.getHint().toString().equals("-1")) {
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.white));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                } else if (coursepacket_selectpop_child_signname1.getHint().toString().equals(mCoursePacketSelectTemp1)) { // 如果上个找到上一个选中的id，将其置为未选状态
                    coursepacket_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect));
                    coursepacket_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.grayff999999));
                    coursepacket_selectpop_child_signname1.setPadding(padding, padding, padding, padding);
                }
            }
            mCoursePacketSelectTemp1 = "-1";
            mCoursePacketSelect1 = "-1";
        });
    }

    /**
     *   * 设置添加屏幕的背景透明度
     *   * @param bgAlpha
     *   
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mControlMainActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mControlMainActivity.getWindow().setAttributes(lp);
    }

    //获取屏幕高度 不包含虚拟按键=
    public static int getScreenHeight() {
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void OnClickListener(View view) {
        HideAllLayout();
        RelativeLayout coursepacket_details1 = mView.findViewById(R.id.coursepacket_details1);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_details1.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        coursepacket_details1.setLayoutParams(LP);
        coursepacket_details1.setVisibility(View.VISIBLE);
        coursepacket_details1.removeAllViews();
        LinearLayout coursepacket_linearlayout = mView.findViewById(R.id.coursepacket_linearlayout);
        coursepacket_linearlayout.removeAllViews();
        coursepacket_details1.addView(view);
        mControlMainActivity.onClickCoursePacketDetails();
    }

    //数据的条件筛选
    private void getConditionQuery() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
        HashMap<String, Integer> hashMap = new HashMap<>();
        //对应的参数  项目id  按照时间hour  热度fever  一级分类stair   二级分类secondlevel
        hashMap.put("project_id", Integer.valueOf(mCoursePacketSelect));
        hashMap.put("hour", Integer.valueOf(mCoursePacketSelect1));
        hashMap.put("fever", Integer.valueOf(mCoursePacketSelectSort));
        hashMap.put("stair", Integer.valueOf("1"));
        hashMap.put("secondlevel", Integer.valueOf("2"));

        modelObservableInterface.queryAllcoursePackageSearchBox(hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoursePacketBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CoursePacketBean coursePacketBean) {
                        int code = coursePacketBean.getCode();
                        if (code == 200) {
                            //获取网络数据
                            coursePacketBeanData = coursePacketBean.getData();
                            if (coursePacketBeanData != null) {
                                View line = null;
                                LinearLayout coursepacket_linearlayout = mView.findViewById(R.id.coursepacket_linearlayout);
                                coursepacket_linearlayout.removeAllViews();
                                //获取网络数据的方法
                                for (int i = 0; i < dataBeans.size(); i++) {
                                    CoursePacketBean.DataBean dataBean = dataBeans.get(i);
                                    if (dataBean == null) {
                                        continue;
                                    }
                                    ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
                                    modelCoursePacketCover.ModelCoursePacketCoverOnClickListenerSet(ModelCoursePacket.this);
                                    //new 一个相关的实体类直接一个一个赋值
                                    //            private int total_price;          //总价格        private int courseNum;    //课程数量
                                    //            private String cp_name;           //	课程包名字     private int favorable_price;    //优惠价格
                                    //            private int buying_base_number;  //优惠价格
                                    CoursePacketInfo coursePacketInfo = new CoursePacketInfo();
                                    coursePacketInfo.mCoursePacketId = String.valueOf(dataBean.course_package_id);//课程
                                    coursePacketInfo.mCoursePacketCover = dataBean.cover;
                                    coursePacketInfo.mCoursePacketStageNum = String.valueOf(dataBean.stageNum);
                                    coursePacketInfo.mCoursePacketName = dataBean.cp_name;   //包名
                                    coursePacketInfo.mCoursePacketPrice = String.valueOf(dataBean.favorable_price);//总价格
                                    coursePacketInfo.mCoursePacketCourseNum = String.valueOf(dataBean.courseNum);
                                    coursePacketInfo.mCoursePacketPriceOld = String.valueOf(dataBean.total_price);//数据原来的价格
                                    View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity, coursePacketInfo);
                                    coursepacket_linearlayout.addView(modelCoursePacketView);
                                    line = modelCoursePacketView.findViewById(R.id.coursepacket_line1);
                                }
                                if (line != null) {
                                    line.setVisibility(View.INVISIBLE);
                                }
                            }
                            mSmart_fragment_coursepacket.finishRefresh();
                        }else {
                            Log.d(TAG, "onNext: "+code);
                            ToastUtil.show(getActivity(),"code"+code);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "错误是"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
     //课程包的关键字搜索
    private void getModelSreachViewData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
         //	课程包名字
        modelObservableInterface.queryAllCoursePackageSelectName("课程包001")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoursePacketSearchView>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CoursePacketSearchView coursePacketSearchView) {
                     if (coursePacketSearchView.getCode()==200){
                         List<CoursePacketSearchView.DataBean> searchViewData = coursePacketSearchView.getData();
                         if (searchViewData!=null){
                             for (int i = 0; i < searchViewData.size(); i++){
                                 CoursePacketSearchView.DataBean dataBean = searchViewData.get(i);
                                 String cover = dataBean.getCover();//封面
                                 String cp_name = dataBean.getCp_name();//课程包名字
                                 double total_price = dataBean.getTotal_price();//总价格
                                 int buying_base_number = dataBean.getBuying_base_number();//优惠价格
                                 int course_package_id = dataBean.getCourse_package_id();//	课程包id
                                 int courseNum = dataBean.getCourseNum();//课程数量
                                 double favorable_price = dataBean.getFavorable_price();//优惠价格
                                 int stageNum = dataBean.getStageNum();//阶段数量
                                 //数据赋值 刷新页面
                                 CoursePacketInfo coursePacketInfo = new CoursePacketInfo();
                                 coursePacketInfo.mCoursePacketCover=cover;
                                 coursePacketInfo.mCoursePacketName=cp_name;
                                 coursePacketInfo.mCoursePacketStageNum= String.valueOf(stageNum);
                                 coursePacketInfo.mCoursePacketId= String.valueOf(course_package_id);
                                 coursePacketInfo.mCoursePacketCourseNum= String.valueOf(courseNum);
                                 coursePacketInfo.mCoursePacketPriceOld= String.valueOf(favorable_price);
                                 coursePacketInfo.mCoursePacketLearnPersonNum= String.valueOf(buying_base_number);
                             }
                         }
                     }
                        mSmart_fragment_coursepacket.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"错误的数据是"+e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //课程包列表请求
    private void getModelCoursePacketDatas() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();
        ModelObservableInterface observableInterface = retrofit.create(ModelObservableInterface.class);
        observableInterface.queryAllCoursePackageInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoursePacketBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CoursePacketBean coursePacketBean) {
                        int code = coursePacketBean.getCode();
                        //判断当前的code值是否为200
                        if (code == 200) {
                            dataBeans = coursePacketBean.getData();
                            if (dataBeans!= null) {
                                View line = null;
                                LinearLayout coursepacket_linearlayout = mView.findViewById(R.id.coursepacket_linearlayout);
                                coursepacket_linearlayout.removeAllViews();
                                //获取网络数据的方法
                                for (int i = 0; i < dataBeans.size(); i++) {
                                    CoursePacketBean.DataBean dataBean = dataBeans.get(i);
                                    if (dataBean == null) {
                                        continue;
                                    }
                                    ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
                                    modelCoursePacketCover.ModelCoursePacketCoverOnClickListenerSet(ModelCoursePacket.this);
                                    //new 一个相关的实体类直接一个一个赋值
                                    //            private int total_price;          //总价格        private int courseNum;    //课程数量
                                    //            private String cp_name;           //	课程包名字     private int favorable_price;    //优惠价格
                                    //            private int buying_base_number;  //优惠价格
                                    CoursePacketInfo coursePacketInfo = new CoursePacketInfo();
                                    coursePacketInfo.mCoursePacketId = String.valueOf(dataBean.course_package_id);//课程
                                    coursePacketInfo.mCoursePacketCover = dataBean.cover;
                                    coursePacketInfo.mCoursePacketStageNum = String.valueOf(dataBean.stageNum);
                                    coursePacketInfo.mCoursePacketName = dataBean.cp_name;   //包名
                                    coursePacketInfo.mCoursePacketPrice = String.valueOf(dataBean.favorable_price);//总价格
                                    coursePacketInfo.mCoursePacketCourseNum = String.valueOf(dataBean.courseNum);
                                    coursePacketInfo.mCoursePacketPriceOld = String.valueOf(dataBean.total_price);//数据原来的价格
                                    coursePacketInfo.mCoursePacketLearnPersonNum= String.valueOf(dataBean.buying_base_number);//购买人数
                                    View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity, coursePacketInfo);
                                    coursepacket_linearlayout.addView(modelCoursePacketView);
                                    line = modelCoursePacketView.findViewById(R.id.coursepacket_line1);
                                }
                                if (line != null) {
                                    line.setVisibility(View.INVISIBLE);
                                }
                            }
                            mSmart_fragment_coursepacket.finishRefresh();
                        }
                        Log.d(TAG, "onNext: " + "数据返回值" + dataBeans.size());
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //课程包的列表数据结构
    public static class CoursePacketBean {
        /**
         * code : 200
         * data : [{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":7,"course_package_id":1,"total_price":8888,"courseNum":1,"cp_name":"课程包001","favorable_price":1,"buying_base_number":52},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":2,"course_package_id":3,"total_price":8888,"courseNum":null,"cp_name":"课程包003","favorable_price":1,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":4,"total_price":8888,"courseNum":null,"cp_name":"课程包004","favorable_price":1,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":6,"total_price":8888,"courseNum":null,"cp_name":"课程包001","favorable_price":1,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":7,"total_price":8888,"courseNum":null,"cp_name":"课程包001","favorable_price":1,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":8,"total_price":8888,"courseNum":null,"cp_name":"江山","favorable_price":111,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":0,"course_package_id":9,"total_price":8888,"courseNum":null,"cp_name":"江山","favorable_price":111,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":0,"course_package_id":14,"total_price":8888,"courseNum":null,"cp_name":"江山11","favorable_price":111,"buying_base_number":52}]
         */

        private int code;
        private List<DataBean> data;

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
             * cover : http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png
             * stageNum : 7
             * course_package_id : 1
             * total_price : 8888
             * courseNum : 1
             * cp_name : 课程包001
             * favorable_price : 1
             * buying_base_number : 52
             */

            private String cover;   //数据包的图片
            private int stageNum;    //阶段数量
            private int course_package_id;  //课程包id
            private int total_price;    //总价格
            private int courseNum;    //课程数量
            private String cp_name;     //	课程包名字
            private int favorable_price;    //优惠价格
            private int buying_base_number;  //优惠价格

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getStageNum() {
                return stageNum;
            }

            public void setStageNum(int stageNum) {
                this.stageNum = stageNum;
            }

            public int getCourse_package_id() {
                return course_package_id;
            }

            public void setCourse_package_id(int course_package_id) {
                this.course_package_id = course_package_id;
            }

            public int getTotal_price() {
                return total_price;
            }

            public void setTotal_price(int total_price) {
                this.total_price = total_price;
            }

            public int getCourseNum() {
                return courseNum;
            }

            public void setCourseNum(int courseNum) {
                this.courseNum = courseNum;
            }

            public String getCp_name() {
                return cp_name;
            }

            public void setCp_name(String cp_name) {
                this.cp_name = cp_name;
            }

            public int getFavorable_price() {
                return favorable_price;
            }

            public void setFavorable_price(int favorable_price) {
                this.favorable_price = favorable_price;
            }

            public int getBuying_base_number() {
                return buying_base_number;
            }

            public void setBuying_base_number(int buying_base_number) {
                this.buying_base_number = buying_base_number;
            }
        }
    }
    //搜索地实体类
    public static class CoursePacketSearchView{
        /**
         * code : 200
         * data : [{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":7,"course_package_id":1,"total_price":8888,"courseNum":1,"cp_name":"课程包001","favorable_price":1,"buying_base_number":52},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":6,"total_price":8888,"courseNum":null,"cp_name":"课程包001","favorable_price":1,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","stageNum":0,"course_package_id":7,"total_price":8888,"courseNum":null,"cp_name":"课程包001","favorable_price":1,"buying_base_number":50}]
         */
        private int code;
        private List<DataBean> data;

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
             * cover : http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png
             * stageNum : 7
             * course_package_id : 1
             * total_price : 8888.0
             * courseNum : 1
             * cp_name : 课程包001
             * favorable_price : 1.0
             * buying_base_number : 52
             */

            private String cover;
            private int stageNum;
            private int course_package_id;
            private double total_price;
            private int courseNum;
            private String cp_name;
            private double favorable_price;
            private int buying_base_number;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getStageNum() {
                return stageNum;
            }

            public void setStageNum(int stageNum) {
                this.stageNum = stageNum;
            }

            public int getCourse_package_id() {
                return course_package_id;
            }

            public void setCourse_package_id(int course_package_id) {
                this.course_package_id = course_package_id;
            }

            public double getTotal_price() {
                return total_price;
            }

            public void setTotal_price(double total_price) {
                this.total_price = total_price;
            }

            public int getCourseNum() {
                return courseNum;
            }

            public void setCourseNum(int courseNum) {
                this.courseNum = courseNum;
            }

            public String getCp_name() {
                return cp_name;
            }

            public void setCp_name(String cp_name) {
                this.cp_name = cp_name;
            }

            public double getFavorable_price() {
                return favorable_price;
            }

            public void setFavorable_price(double favorable_price) {
                this.favorable_price = favorable_price;
            }

            public int getBuying_base_number() {
                return buying_base_number;
            }

            public void setBuying_base_number(int buying_base_number) {
                this.buying_base_number = buying_base_number;
            }
        }
    }


}

