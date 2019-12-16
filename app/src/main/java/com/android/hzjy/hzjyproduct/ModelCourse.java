package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ModelCourse extends Fragment implements ModelCourseCover.ModelCourseCoverOnClickListener{
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int height = 1344;
    private int width = 720;
    private ModelSearchView searchView = null;

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelCourse myFragment = new ModelCourse();
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
        CourseMainInit();
        CourseSearchInit();
//        if (mContext.equals("课程:")) {
//            CoursePacketMainShow(1);
//        } else {
//            CoursePacketMainShow(0);
//        }
        CourseMainShow(0);
        // 3. 绑定组件
        searchView = mview.findViewById(R.id.course_search_view);
        searchView.init("coursesearchrecords");
        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容,,,,,,,,.
        searchView.setOnClickSearch(string ->{
                System.out.println("我收到了" + string);
            });
        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(()->{
//            if (mContext.equals("课程:")) {
//                CourseMainShow(1);
//            } else {
                CourseMainShow(0);
//            }
            });
        ModelDropDownMenu course_dropDownMenu = mview.findViewById(R.id.course_dropDownMenu);
        ModelSoftRadioGroup course_radiogroup =  mview.findViewById(R.id.course_radiogroup);
        course_radiogroup.setOnCheckedChangeListener((group, checkedId, orientation) -> {
            switch (checkedId){
                case R.id.course_softradiobutton0:
                    if (orientation){
                        course_dropDownMenu.open();
                    }
                    break;
                default:
                    break;
            }
        });
        TextView course_softradiobutton0 =  mview.findViewById(R.id.course_softradiobutton0);
        course_softradiobutton0.setOnClickListener(v ->{course_dropDownMenu.open();});
        ModelPtrFrameLayout course_store_house_ptr_frame = mview.findViewById(R.id.course_store_house_ptr_frame);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) course_store_house_ptr_frame.getLayoutParams();
        lp.topMargin = width / 10;
        course_store_house_ptr_frame.setLayoutParams(lp);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        course_store_house_ptr_frame.addPtrUIHandler(header);
        course_store_house_ptr_frame.setHeaderView(header);
        course_store_house_ptr_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                course_store_house_ptr_frame.refreshComplete();
            }
        });
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void CourseMainShow(int returnString) { // returnString:  0:显示返回按钮
        if (mview == null) {
            return;
        }
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        HideAllLayout();
        RelativeLayout course_mainLayout = mview.findViewById(R.id.course_mainLayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_mainLayout.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        course_mainLayout.setLayoutParams(LP);
        course_mainLayout.setVisibility(View.VISIBLE);
        RelativeLayout course_titleRelativeLayout = mview.findViewById(R.id.course_titleRelativeLayout);
        RelativeLayout course_titleRelativeLayout1 = mview.findViewById(R.id.course_titleRelativeLayout1);
        if (returnString == 0){
            mContext = "课程:首页";
            course_titleRelativeLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout.getLayoutParams();
            lp.topMargin = leftMargin;
            lp.leftMargin = leftMargin;
            lp.rightMargin = leftMargin;
            lp.height = layoutheight;
            course_titleRelativeLayout.setLayoutParams(lp);
            lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout1.getLayoutParams();
            lp.topMargin = 0;
            course_titleRelativeLayout1.setLayoutParams(lp);
        } else {
            mContext = "课程:";
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout1.getLayoutParams();
            lp.topMargin = leftMargin;
            course_titleRelativeLayout1.setLayoutParams(lp);
            course_titleRelativeLayout.setVisibility(View.INVISIBLE);
            lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout.getLayoutParams();
            lp.topMargin = 0;
            lp.leftMargin = 0;
            lp.rightMargin = 0;
            lp.bottomMargin = 0;
            lp.height = 0;
            course_titleRelativeLayout.setLayoutParams(lp);
        }
        ScrollView course_block_menu_scroll_view = mview.findViewById(R.id.course_block_menu_scroll_view);
        course_block_menu_scroll_view.scrollTo(0,0);
        //课程列表
        List<CourseInfo> CourseInfoList = new ArrayList<>();
        CourseInfo CourseInfo1 = new CourseInfo();
        CourseInfo1.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
        CourseInfo1.mCourseLearnPersonNum = "1000";
        CourseInfo1.mCourseName = "开学典礼";
        CourseInfo1.mCoursePrice = "免费";
        CourseInfo1.mCourseType = "直播";
        CourseInfo1.mCourseIsHave = "1";
        CourseInfo1.mIsReferCourse = "1";
        CourseInfo1.mCoursePriceOld = "5000.00";
        CourseInfo1.mCourseMessage = "这个课是基础课";
        CourseInfo1.mCourseValidityPeriod = "三个月";
        CourseInfo1.mCourseDetails = "<p>&nbsp;&nbsp;随着2017年一级注册消防工程师考试的报名结束，沉寂一段时间的二消又立刻成为了考证大军们新一轮关注的焦点。二消的报考条件比一级宽松，学历门槛较低，考试难度相对较小，而且一二级证书之间互不约束，所以除了吸引更多自身符合报考资格的意向考友外，其中还不乏一些已经参加一级考试的考生踊跃参与，这些都使得二级证书的前景更加明朗，竞争也更加激烈。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;但是今年的二消在业内预估的时间内仍是迟迟未开考，这同样令一部分已提前准备的考友加剧了忧思，准备了这么久，能等到二级的开考吗？据官方内部消息透露，今年时机不对，年底前二级已不会有开考可能，但是2018年将是二级正式走入执业证书市场的最佳时机。内部分析如下，供参考。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;首先，关于执业资格证的&ldquo;三年一大变&rdquo;规律可知，参考同类型的注册建造师是从一级考试开考的第三年之后，就开始大举改革教材，而2017年恰好是一级消防工程师的第3次开考，本着相似的改革规律，2016年消防工程师考试已经更新过教材，那么2018年能实施的重要举措极有可能就是二消的开考了。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;其次，公安部现已发布了两个重要文件。其中，14年公安令129号令中的《社会消防技术服务管理规定》明确表示消防相关从业单位，消防重点单位，消防安装、维保检测单位必须具备相应条件才可以继续升级资质，具体要求见下表。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
//                "<div style=\"text-align:center;\"><img src=\"http://image.yunduoketang.com/editor/14972/20170918/924ca0ac-3837-48f4-889d-a6649ac20245.png\" alt=\"\" /></div>"+
                "<p style=\"text-align: center;\"><img alt=\"\" src=\"http://image.yunduoketang.com/editor/14972/20170918/924ca0ac-3837-48f4-889d-a6649ac20245.png\"  />&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;上表分析可知，6人以上的要求中，一级需3人以上，那么剩下人数的必然就是二级的了。但由相关数据整理可知，开考两年后，一级消防工程师全国现通过人数仅约12566人，远达不到目前国内消防企业或单位所需的人才匹配量，因此，将部分人才压力的缓解寄希望于二消也是业内的大势所趋，那么尽快开放二消考试，保障人数的供需不平衡就是18年的重中之重了。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;而且今年3月底发布的公安令143号文件也明确说明，《注册消防工程师管理规定》从2017年10月1日就要开始正式实施了。其中第三十三条规定还强调，注册消防工程师不得同时在两个以上消防技术服务机构，或者消防安全重点单位执业。这一点，无疑又加剧了市场的落差。但目前的行业内情况就是粥少僧多，而国家又要在今年如期推行该规定，在一消证书不足的情况，二消证书的重要性可想而知，已是迫在眉睫。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;最后，在129令后，全国的很多省份为了弥补二级消防证书的空缺，以及减少消防技术服务机构等的资质审核排队情况，推出了&ldquo;临时二级证书&rdquo;的政策，只要一级的成绩达到相应的分数，或者参加消防技术服务综合考试合格者均可，但该证书肯定会一定的时效，如辽宁和安徽省，有效时间只截止到2018年的12月31号，那么18年后必然要有正规的证书加以衔接，这也是18年推举二消考试的最佳时机。所以说临时二级证书只是一种过渡性的举措，正是因为试点该证书的受面广泛，才让业内对明年的二消考试有了更大的期许。</p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<p>&nbsp; &nbsp;根据今年公安部消防局的各大消防安全检查通知和防范救援的投入可知，国家高层现在非常重视消防安全和防范救援，因此今年10月份&ldquo;十九大&rdquo;的召开，也极有可能会为明年二消的开考添一把火，据业内消息预估开考时间为明年的上半年。故在综合形势下，北京火种教育提醒大家，借鉴一消在考前三月突然公开报名的先例和以上的内部主流分析，明年有意二消考试的同学也需要提前做好准备，打有备之战。</p>\n";
        CourseChaptersInfo CourseChaptersInfo1 = new CourseChaptersInfo();
        CourseChaptersInfo1.mCourseChaptersId = "1";
        CourseChaptersInfo1.mCourseChaptersName = "第一章";
        CourseSectionsInfo CourseSectionsInfo1 = new CourseSectionsInfo();
        CourseSectionsInfo1.mCourseSectionsId = "1";
        CourseSectionsInfo1.mCourseSectionsName = "开学第一讲";
        CourseSectionsInfo1.mCourseSectionsLearnProgress = "未学习";
        CourseSectionsInfo1.mCourseSectionsPrice = "免费";
        CourseSectionsInfo1.mCourseSectionsTime = "00:30:21";
        CourseSectionsInfo1.mCourseSectionsSize = "2048";
        CourseChaptersInfo1.mCourseSectionsInfoList.add(CourseSectionsInfo1);
        CourseSectionsInfo CourseSectionsInfo2 = new CourseSectionsInfo();
        CourseSectionsInfo2.mCourseSectionsId = "2";
        CourseSectionsInfo2.mCourseSectionsName = "开学第二讲";
        CourseSectionsInfo2.mCourseSectionsLearnProgress = "未学习";
        CourseSectionsInfo2.mCourseSectionsPrice = "试听";
        CourseSectionsInfo2.mCourseSectionsTime = "00:20:21";
        CourseSectionsInfo2.mCourseSectionsSize = "2048";
        CourseChaptersInfo1.mCourseSectionsInfoList.add(CourseSectionsInfo2);
        CourseChaptersInfo CourseChaptersInfo2 = new CourseChaptersInfo();
        CourseChaptersInfo2.mCourseChaptersId = "2";
        CourseChaptersInfo2.mCourseChaptersName = "第二章";
        CourseSectionsInfo CourseSectionsInfo3 = new CourseSectionsInfo();
        CourseSectionsInfo3.mCourseSectionsId = "3";
        CourseSectionsInfo3.mCourseSectionsName = "哎呀呀";
        CourseSectionsInfo3.mCourseSectionsLearnProgress = "已学习";
        CourseSectionsInfo3.mCourseSectionsPrice = "试听";
        CourseSectionsInfo3.mCourseSectionsTime = "00:35:21";
        CourseSectionsInfo3.mCourseSectionsSize = "2048";
        CourseChaptersInfo2.mCourseSectionsInfoList.add(CourseSectionsInfo3);
        CourseSectionsInfo CourseSectionsInfo4 = new CourseSectionsInfo();
        CourseSectionsInfo4.mCourseSectionsId = "4";
        CourseSectionsInfo4.mCourseSectionsName = "哎呀呀";
        CourseSectionsInfo4.mCourseSectionsLearnProgress = "已学习";
        CourseSectionsInfo4.mCourseSectionsPrice = "试听";
        CourseSectionsInfo4.mCourseSectionsTime = "00:35:21";
        CourseSectionsInfo4.mCourseSectionsSize = "2048";
        CourseChaptersInfo2.mCourseSectionsInfoList.add(CourseSectionsInfo4);
        CourseSectionsInfo CourseSectionsInfo5 = new CourseSectionsInfo();
        CourseSectionsInfo5.mCourseSectionsId = "5";
        CourseSectionsInfo5.mCourseSectionsName = "哎呀呀";
        CourseSectionsInfo5.mCourseSectionsLearnProgress = "已学习";
        CourseSectionsInfo5.mCourseSectionsPrice = "试听";
        CourseSectionsInfo5.mCourseSectionsTime = "00:35:21";
        CourseSectionsInfo5.mCourseSectionsSize = "2048";
        CourseChaptersInfo2.mCourseSectionsInfoList.add(CourseSectionsInfo5);
        CourseSectionsInfo CourseSectionsInfo6 = new CourseSectionsInfo();
        CourseSectionsInfo6.mCourseSectionsId = "6";
        CourseSectionsInfo6.mCourseSectionsName = "哎呀呀";
        CourseSectionsInfo6.mCourseSectionsLearnProgress = "已学习";
        CourseSectionsInfo6.mCourseSectionsPrice = "试听";
        CourseSectionsInfo6.mCourseSectionsTime = "00:35:21";
        CourseSectionsInfo6.mCourseSectionsSize = "2048";
        CourseChaptersInfo2.mCourseSectionsInfoList.add(CourseSectionsInfo6);
        CourseChaptersInfo CourseChaptersInfo3 = new CourseChaptersInfo();
        CourseChaptersInfo3.mCourseChaptersId = "3";
        CourseChaptersInfo3.mCourseChaptersName = "第san章";
        CourseInfo1.mCourseChaptersInfoList.add(CourseChaptersInfo1);
        CourseInfo1.mCourseChaptersInfoList.add(CourseChaptersInfo2);
        CourseInfo1.mCourseChaptersInfoList.add(CourseChaptersInfo3);
        CourseQuestionInfo CourseQuestionInfo1 = new CourseQuestionInfo();
        CourseQuestionInfo1.mCourseQuestionId = "1";
        CourseQuestionInfo1.mCourseAnswerId = "0";
        CourseQuestionInfo1.mCourseQuestionCommentId1 = "0";//提问者id(为0的时候：一级提问)
        CourseQuestionInfo1.mCourseQuestionCommentName1 = "";//提问者名字
        CourseQuestionInfo1.mCourseQuestionCommentHead1 = "";//提问者头像
        CourseQuestionInfo1.mCourseQuestionCommentId2 = "1";//回答者id
        CourseQuestionInfo1.mCourseQuestionCommentName2 = "徐华洲";//回答者名字
        CourseQuestionInfo1.mCourseQuestionCommentHead2 = "";//回答者头像
        CourseQuestionInfo1.mCourseQuestionTime = "2019-10-12 10:22:33";//课程问答时间
        CourseQuestionInfo1.mCourseQuestionContent = "张哲王八蛋";//课程问答内容
        CourseQuestionInfo1.mCourseQuestionImage = "";//课程问答图片（中间用，分割）
        CourseQuestionInfo1.mCourseQuestionLookNum = "10";//课程问答浏览人数
        CourseInfo1.mCourseQuestionInfoList.add(CourseQuestionInfo1);
        CourseQuestionInfo CourseQuestionInfo2 = new CourseQuestionInfo();
        CourseQuestionInfo2.mCourseQuestionId = "2";
        CourseQuestionInfo2.mCourseAnswerId = "1";
        CourseQuestionInfo2.mCourseQuestionCommentId1 = "1";//提问者id(为0的时候：一级提问)
        CourseQuestionInfo2.mCourseQuestionCommentName1 = "徐华洲";//提问者名字
        CourseQuestionInfo2.mCourseQuestionCommentHead1 = "";//提问者头像
        CourseQuestionInfo2.mCourseQuestionCommentId2 = "2";//回答者id
        CourseQuestionInfo2.mCourseQuestionCommentName2 = "江山";//回答者名字
        CourseQuestionInfo2.mCourseQuestionCommentHead2 = "";//回答者头像
        CourseQuestionInfo2.mCourseQuestionTime = "2019-10-12 10:33:33";//课程问答时间
        CourseQuestionInfo2.mCourseQuestionContent = "华洲说的对";//课程问答内容
        CourseQuestionInfo2.mCourseQuestionImage = "";//课程问答图片（中间用，分割）
        CourseQuestionInfo2.mCourseQuestionLookNum = "10";//课程问答浏览人数
        CourseInfo1.mCourseQuestionInfoList.add(CourseQuestionInfo2);
        CourseQuestionInfo CourseQuestionInfo3 = new CourseQuestionInfo();
        CourseQuestionInfo3.mCourseQuestionId = "3";
        CourseQuestionInfo3.mCourseAnswerId = "1";
        CourseQuestionInfo3.mCourseQuestionCommentId1 = "1";//提问者id(为0的时候：一级提问)
        CourseQuestionInfo3.mCourseQuestionCommentName1 = "徐华洲";//提问者名字
        CourseQuestionInfo3.mCourseQuestionCommentHead1 = "";//提问者头像
        CourseQuestionInfo3.mCourseQuestionCommentId2 = "2";//回答者id
        CourseQuestionInfo3.mCourseQuestionCommentName2 = "江山";//回答者名字
        CourseQuestionInfo3.mCourseQuestionCommentHead2 = "";//回答者头像
        CourseQuestionInfo3.mCourseQuestionTime = "2019-10-12 10:33:33";//课程问答时间
        CourseQuestionInfo3.mCourseQuestionContent = "华洲说的对";//课程问答内容
        CourseQuestionInfo3.mCourseQuestionImage = "";//课程问答图片（中间用，分割）
        CourseQuestionInfo3.mCourseQuestionLookNum = "10";//课程问答浏览人数
        CourseInfo1.mCourseQuestionInfoList.add(CourseQuestionInfo3);
        CourseQuestionInfo CourseQuestionInfo5 = new CourseQuestionInfo();
        CourseQuestionInfo5.mCourseQuestionId = "5";
        CourseQuestionInfo5.mCourseAnswerId = "1";
        CourseQuestionInfo5.mCourseQuestionCommentId1 = "1";//提问者id(为0的时候：一级提问)
        CourseQuestionInfo5.mCourseQuestionCommentName1 = "徐华洲";//提问者名字
        CourseQuestionInfo5.mCourseQuestionCommentHead1 = "";//提问者头像
        CourseQuestionInfo5.mCourseQuestionCommentId2 = "2";//回答者id
        CourseQuestionInfo5.mCourseQuestionCommentName2 = "江山";//回答者名字
        CourseQuestionInfo5.mCourseQuestionCommentHead2 = "";//回答者头像
        CourseQuestionInfo5.mCourseQuestionTime = "2019-10-12 10:33:33";//课程问答时间
        CourseQuestionInfo5.mCourseQuestionContent = "华洲说的对";//课程问答内容
        CourseQuestionInfo5.mCourseQuestionImage = "";//课程问答图片（中间用，分割）
        CourseQuestionInfo5.mCourseQuestionLookNum = "10";//课程问答浏览人数
        CourseInfo1.mCourseQuestionInfoList.add(CourseQuestionInfo5);
        CourseQuestionInfo CourseQuestionInfo4 = new CourseQuestionInfo();
        CourseQuestionInfo4.mCourseQuestionId = "4";
        CourseQuestionInfo4.mCourseAnswerId = "0";
        CourseQuestionInfo4.mCourseQuestionCommentId1 = "0";//提问者id(为0的时候：一级提问)
        CourseQuestionInfo4.mCourseQuestionCommentName1 = "";//提问者名字
        CourseQuestionInfo4.mCourseQuestionCommentHead1 = "";//提问者头像
        CourseQuestionInfo4.mCourseQuestionCommentId2 = "3";//回答者id
        CourseQuestionInfo4.mCourseQuestionCommentName2 = "张哲";//回答者名字
        CourseQuestionInfo4.mCourseQuestionCommentHead2 = "";//回答者头像
        CourseQuestionInfo4.mCourseQuestionTime = "2019-10-12 11:22:33";//课程问答时间
        CourseQuestionInfo4.mCourseQuestionContent = "我是王八蛋";//课程问答内容
        CourseQuestionInfo4.mCourseQuestionImage = "";//课程问答图片（中间用，分割）
        CourseQuestionInfo4.mCourseQuestionLookNum = "12";//课程问答浏览人数
        CourseInfo1.mCourseQuestionInfoList.add(CourseQuestionInfo4);
        CourseInfoList.add(CourseInfo1);
        CourseInfo CourseInfo2 = new CourseInfo();
        CourseInfo2.mCourseCover = "";
        CourseInfo2.mCourseLearnPersonNum = "100";
        CourseInfo2.mCourseName = "开学典礼";
        CourseInfo2.mCoursePrice = "1920.00";
        CourseInfo2.mCoursePriceOld = "5000.00";
        CourseInfo2.mCourseIsHave = "0";
        CourseInfo2.mCourseType = "直播";
        CourseInfo2.mIsReferCourse = "0";
        CourseInfoList.add(CourseInfo2);
        LinearLayout course_linearlayout = mview.findViewById(R.id.course_linearlayout);
        course_linearlayout.removeAllViews();
        for (int i = 0; i < CourseInfoList.size(); i ++){
            CourseInfo courseInfo = CourseInfoList.get(i);
            if (courseInfo == null){
                continue;
            }
            ModelCourseCover modelCourseCover = new ModelCourseCover();
            modelCourseCover.ModelCourseCoverOnClickListenerSet(this);
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity,courseInfo);
            course_linearlayout.addView(modelCourseView);
            if (i == CourseInfoList.size() - 1){
                View view = new View(mControlMainActivity);
                course_linearlayout.addView(view);
                LinearLayout.LayoutParams viewLp = (LinearLayout.LayoutParams) view.getLayoutParams();
                viewLp.height = height / 11;
                view.setLayoutParams(viewLp);
            }
        }
    }

    public void CourseMainSearchShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        RelativeLayout course_searchlayout = mview.findViewById(R.id.course_searchlayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_searchlayout.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        course_searchlayout.setLayoutParams(LP);
        course_searchlayout.setVisibility(View.VISIBLE);
    }

    //隐藏所有图层
    private void HideAllLayout(){
        RelativeLayout course_mainLayout = mview.findViewById(R.id.course_mainLayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_mainLayout.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        course_mainLayout.setLayoutParams(LP);
        course_mainLayout.setVisibility(View.INVISIBLE);
        RelativeLayout course_searchlayout = mview.findViewById(R.id.course_searchlayout);
        LP = (LinearLayout.LayoutParams) course_searchlayout.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        course_searchlayout.setLayoutParams(LP);
        course_searchlayout.setVisibility(View.INVISIBLE);
        RelativeLayout course_details1 = mview.findViewById(R.id.course_details1);
        LP = (LinearLayout.LayoutParams) course_details1.getLayoutParams();
        LP.width = 0;
        LP.height = 0;
        course_details1.setLayoutParams(LP);
        course_details1.setVisibility(View.INVISIBLE);
    }

    //初始化课程主界面
    public void CourseMainInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
//        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout course_titleRelativeLayout = mview.findViewById(R.id.course_titleRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        course_titleRelativeLayout.setLayoutParams(lp);
        RelativeLayout course_titleRelativeLayout1 = mview.findViewById(R.id.course_titleRelativeLayout1);
        lp = (RelativeLayout.LayoutParams) course_titleRelativeLayout1.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        course_titleRelativeLayout1.setLayoutParams(lp);
        //返回
        ImageView course_title_return = mview.findViewById(R.id.course_title_return);
        lp = (RelativeLayout.LayoutParams) course_title_return.getLayoutParams();
        lp.height = width / 15;
        lp.width = width / 15;
        course_title_return.setLayoutParams(lp);
//        TextView course_title = mview.findViewById(R.id.course_title);
        //查询
//        ImageView course_title_search = mview.findViewById(R.id.course_title_search);
//        lp = (RelativeLayout.LayoutParams) course_title_search.getLayoutParams();
//        lp.height = width / 15;
//        lp.width = width / 15;
//        course_title_search.setLayoutParams(lp);
        ImageView course_title_search1 = mview.findViewById(R.id.course_title_search1);
        lp = (RelativeLayout.LayoutParams) course_title_search1.getLayoutParams();
        lp.height = width / 15;
        lp.width = width / 15;
        lp.leftMargin = width / 50;
        lp.rightMargin = leftMargin;
        course_title_search1.setLayoutParams(lp);
        //功能列表
        ModelSoftRadioGroup course_radiogroup = mview.findViewById(R.id.course_radiogroup);
        lp = (RelativeLayout.LayoutParams) course_radiogroup.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.height = layoutheight;
        course_radiogroup.setLayoutParams(lp);
        //项目
        TextView course_softradiobutton0 = mview.findViewById(R.id.course_softradiobutton0);
        LinearLayout.LayoutParams LinearLayoutLp = (LinearLayout.LayoutParams) course_softradiobutton0.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        course_softradiobutton0.setLayoutParams(LinearLayoutLp);
        //时间排序
        ModelSoftRadioButton course_softradiobutton1 = mview.findViewById(R.id.course_softradiobutton1);
        LinearLayoutLp = (LinearLayout.LayoutParams) course_softradiobutton1.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        course_softradiobutton1.setLayoutParams(LinearLayoutLp);
        //热度排序
        ModelSoftRadioButton course_softradiobutton2 = mview.findViewById(R.id.course_softradiobutton2);
        LinearLayoutLp = (LinearLayout.LayoutParams) course_softradiobutton2.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        course_softradiobutton2.setLayoutParams(LinearLayoutLp);
        //重置搜索条件
        TextView course_softradiobutton3 = mview.findViewById(R.id.course_softradiobutton3);
        LinearLayoutLp = (LinearLayout.LayoutParams) course_softradiobutton3.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        course_softradiobutton3.setLayoutParams(LinearLayoutLp);
    }

    //初始化课程包-搜索界面
    public void CourseSearchInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
//        ModelSearchView course_search_view = mview.findViewById(R.id.course_search_view);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) course_search_view.getLayoutParams();
//        lp.topMargin = leftMargin;
//        lp.leftMargin = leftMargin;
//        lp.rightMargin = leftMargin;
//        lp.bottomMargin = bottomMargin;
//        lp.height = layoutheight;
//        course_search_view.setLayoutParams(lp);
    }

    private ModelCourseCover mModelCourseCover = null;
    @Override
    public void OnClickListener(View view,ModelCourseCover modelCourseCover) {
        HideAllLayout();
        RelativeLayout course_details1 = mview.findViewById(R.id.course_details1);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_details1.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        course_details1.setLayoutParams(LP);
        course_details1.setVisibility(View.VISIBLE);
        course_details1.removeAllViews();
        LinearLayout course_linearlayout = mview.findViewById(R.id.course_linearlayout);
        course_linearlayout.removeAllViews();
        course_details1.addView(view);
        mControlMainActivity.onClickCourseDetails();
        mModelCourseCover = modelCourseCover;
    }

    public void ModelCourseCoverQuestionPictureAdd(Intent data){
        if (mModelCourseCover != null){
            mModelCourseCover.ModelCourseCoverQuestionPictureAdd(data);
        }
    }
}
