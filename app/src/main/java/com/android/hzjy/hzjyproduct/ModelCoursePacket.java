package com.android.hzjy.hzjyproduct;
import android.app.Fragment;
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

import static com.android.hzjy.hzjyproduct.R.layout.modelcoursepacketlist_layout;

public class ModelCoursePacket extends Fragment implements ModelCoursePacketCover.ModelCoursePacketCoverOnClickListener{
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mView;
    private int height = 1344;
    private int width = 720;
    private ModelSearchView searchView = null;

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelCoursePacket myFragment = new ModelCoursePacket();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(FragmentPage,container,false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        CoursePacketMainInit();
        CoursePacketSearchInit();
        if (mContext.equals("课程包:")) {
            CoursePacketMainShow(1);
        } else {
            CoursePacketMainShow(0);
        }
        // 3. 绑定组件
        searchView = mView.findViewById(R.id.search_view);
        searchView.init("coursepacketsearchrecords");
        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容,,,,,,,,.
        searchView.setOnClickSearch((string) ->{
                System.out.println("我收到了" + string);
            });
        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(()->{
            mControlMainActivity.Page_MoreCoursePacket();
        });
        ModelDropDownMenu dropDownMenu = mView.findViewById(R.id.dropDownMenu);
        ModelSoftRadioGroup radiogroup =  mView.findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener((group, checkedId, orientation) -> {
            switch (checkedId){
                case R.id.softradiobutton0:
                    if (orientation){
                        dropDownMenu.open();
                    }
                    break;
                default:
                    break;
            }
        });
        TextView softradiobutton0 =  mView.findViewById(R.id.softradiobutton0);
        softradiobutton0.setOnClickListener(v ->{dropDownMenu.open();});
        ModelPtrFrameLayout coursepacket_store_house_ptr_frame = mView.findViewById(R.id.coursepacket_store_house_ptr_frame);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) coursepacket_store_house_ptr_frame.getLayoutParams();
        lp.topMargin = width / 10;
        coursepacket_store_house_ptr_frame.setLayoutParams(lp);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        coursepacket_store_house_ptr_frame.addPtrUIHandler(header);
        coursepacket_store_house_ptr_frame.setHeaderView(header);
        coursepacket_store_house_ptr_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                coursepacket_store_house_ptr_frame.refreshComplete();
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
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        HideAllLayout();
        RelativeLayout coursepacket_mainLayout = mView.findViewById(R.id.coursepacket_mainLayout);
        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) coursepacket_mainLayout.getLayoutParams();
        LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
        LP.height = LinearLayout.LayoutParams.MATCH_PARENT;
        coursepacket_mainLayout.setLayoutParams(LP);
        coursepacket_mainLayout.setVisibility(View.VISIBLE);
        RelativeLayout coursepacket_titleRelativeLayout = mView.findViewById(R.id.coursepacket_titleRelativeLayout);
        RelativeLayout coursepacket_titleRelativeLayout1 = mView.findViewById(R.id.coursepacket_titleRelativeLayout1);
        if (returnString == 0){
            mContext = "课程包:首页";
            coursepacket_titleRelativeLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout.getLayoutParams();
            lp.topMargin = leftMargin;
            lp.leftMargin = leftMargin;
            lp.rightMargin = leftMargin;
//            lp.bottomMargin = bottomMargin;
            lp.height = layoutheight;
            coursepacket_titleRelativeLayout.setLayoutParams(lp);
//            coursepacket_titleRelativeLayout1.setVisibility(View.INVISIBLE);
            lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout1.getLayoutParams();
            lp.topMargin = 0;
//            lp.leftMargin = 0;
//            lp.rightMargin = 0;
//            lp.bottomMargin = 0;
//            lp.height = 0;
            coursepacket_titleRelativeLayout1.setLayoutParams(lp);
        } else {
            mContext = "课程包:";
//            coursepacket_titleRelativeLayout1.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout1.getLayoutParams();
            lp.topMargin = leftMargin;
//            lp.leftMargin = leftMargin;
//            lp.rightMargin = leftMargin;
//            lp.bottomMargin = bottomMargin;
//            lp.height = layoutheight;
            coursepacket_titleRelativeLayout1.setLayoutParams(lp);
            coursepacket_titleRelativeLayout.setVisibility(View.INVISIBLE);
            lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout.getLayoutParams();
            lp.topMargin = 0;
            lp.leftMargin = 0;
            lp.rightMargin = 0;
            lp.bottomMargin = 0;
            lp.height = 0;
            coursepacket_titleRelativeLayout.setLayoutParams(lp);
        }
        ScrollView coursepacket_block_menu_scroll_view = mView.findViewById(R.id.coursepacket_block_menu_scroll_view);
        coursepacket_block_menu_scroll_view.scrollTo(0,0);
        //课程包列表
        List<CoursePacketInfo> CoursePacketInfoList = new ArrayList<>();
        CoursePacketInfo CoursePacketInfo1 = new CoursePacketInfo();
        CoursePacketInfo1.mCoursePacketCover = "http://image.yunduoketang.com/course/34270/20190829/394d6b0a-6c3a-4121-b12c-ef53afff866d.png";
        CoursePacketInfo1.mCoursePacketLearnPersonNum = "1000";
        CoursePacketInfo1.mCoursePacketName = "皇家内训班";
        CoursePacketInfo1.mCoursePacketPrice = "免费";
        CoursePacketInfo1.mCoursePacketPriceOld = "1000";
        CoursePacketInfo1.mCoursePacketStageNum = "3";
        CoursePacketInfo1.mCoursePacketCourseNum = "6";
        CoursePacketInfo1.mIsReferCoursePacket = "1";
        CoursePacketInfo1.mCoursePacketMessage = "皇家内训";
        CoursePacketInfo1.mCoursePacketDetails = "<p>&nbsp;&nbsp;随着2017年一级注册消防工程师考试的报名结束，沉寂一段时间的二消又立刻成为了考证大军们新一轮关注的焦点。二消的报考条件比一级宽松，学历门槛较低，考试难度相对较小，而且一二级证书之间互不约束，所以除了吸引更多自身符合报考资格的意向考友外，其中还不乏一些已经参加一级考试的考生踊跃参与，这些都使得二级证书的前景更加明朗，竞争也更加激烈。</p>\n" +
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
        StageCourseInfo StageCourseInfo1 = new StageCourseInfo();
        StageCourseInfo1.mStageCourseId = "0";
        StageCourseInfo1.mStageCourseName = "课程包阶段1";
        StageCourseInfo1.mStageCourseDescribe = "可厉害";
        StageCourseInfo1.mStageCourseIsSale = "0";
//        StageCourseInfo1.mStageCourseOrder = "1";
        CourseInfo CourseInfo1 = new CourseInfo();
        CourseInfo1.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
        CourseInfo1.mCourseLearnPersonNum = "1000";
        CourseInfo1.mCourseName = "开学典礼";
        CourseInfo1.mCoursePrice = "免费";
        CourseInfo1.mCourseType = "直播";
        CourseInfo1.mIsReferCourse = "1";
        CourseInfo1.mCoursePriceOld = "5000.00";
//        CourseInfo1.mCourseOrder = "1";
        StageCourseInfo1.mCourseInfoList.add(CourseInfo1);

        CourseInfo CourseInfo2 = new CourseInfo();
        CourseInfo2.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
        CourseInfo2.mCourseLearnPersonNum = "1000";
        CourseInfo2.mCourseName = "开学典礼";
        CourseInfo2.mCoursePrice = "免费";
        CourseInfo2.mCourseType = "直播";
        CourseInfo2.mIsReferCourse = "1";
        CourseInfo2.mCoursePriceOld = "5000.00";
//        CourseInfo1.mCourseOrder = "1";
//        StageCourseInfo1.mCourseInfoList.add(CourseInfo2);
        StageCourseInfo StageCourseInfo2 = new StageCourseInfo();
        StageCourseInfo2.mStageCourseId = "0";
        StageCourseInfo2.mStageCourseName = "课程包阶段2";
        StageCourseInfo2.mStageCourseDescribe = "可厉害";
        StageCourseInfo2.mStageCourseIsSale = "0";
        StageCourseInfo2.mCourseInfoList.add(CourseInfo2);
        CoursePacketInfo1.mStageCourseInfoList.add(StageCourseInfo1);
        CoursePacketInfo1.mStageCourseInfoList.add(StageCourseInfo2);
        TeacherInfo TeacherInfo1 = new TeacherInfo();
        TeacherInfo1.mTeacherName = "张哲";
        TeacherInfo1.mTeacherMessage = "更高";
        TeacherInfo1.mTeacherCover = "";
        TeacherInfo TeacherInfo2 = new TeacherInfo();
        TeacherInfo2.mTeacherName = "江山";
        TeacherInfo2.mTeacherMessage = "更帅";
        TeacherInfo2.mTeacherCover = "";
        TeacherInfo TeacherInfo3 = new TeacherInfo();
        TeacherInfo3.mTeacherName = "徐华洲";
        TeacherInfo3.mTeacherMessage = "更强";
        TeacherInfo3.mTeacherCover = "";
        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo1);
        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo2);
        CoursePacketInfo1.mTeacherInfoList.add(TeacherInfo3);
        CoursePacketInfoList.add(CoursePacketInfo1);
        CoursePacketInfo CoursePacketInfo2 = new CoursePacketInfo();
        CoursePacketInfo2.mCoursePacketCover = "";
        CoursePacketInfo2.mCoursePacketLearnPersonNum = "1000";
        CoursePacketInfo2.mCoursePacketPriceOld = "1000";
        CoursePacketInfo2.mCoursePacketName = "课程包呀";
        CoursePacketInfo2.mCoursePacketPrice = "900000";
        CoursePacketInfo2.mCoursePacketStageNum = "3";
        CoursePacketInfo2.mCoursePacketCourseNum = "6";
        CoursePacketInfo2.mIsReferCoursePacket = "1";
        CoursePacketInfo2.mCoursePacketMessage = "课程包呀  这个昂贵";
        CoursePacketInfo2.mCoursePacketDetails = "";
        CoursePacketInfo2.mStageCourseInfoList.add(StageCourseInfo1);
        CoursePacketInfoList.add(CoursePacketInfo2);
        CoursePacketInfo CoursePacketInfo3 = new CoursePacketInfo();
        CoursePacketInfo3.mCoursePacketCover = "";
        CoursePacketInfo3.mCoursePacketLearnPersonNum = "1000";
        CoursePacketInfo3.mCoursePacketPriceOld = "90000";
        CoursePacketInfo3.mCoursePacketName = "课程包呀";
        CoursePacketInfo3.mCoursePacketPrice = "100";
        CoursePacketInfo3.mCoursePacketStageNum = "3";
        CoursePacketInfo3.mCoursePacketCourseNum = "6";
        CoursePacketInfo3.mIsReferCoursePacket = "0";
        CoursePacketInfo3.mCoursePacketMessage = "课程包呀  这个便宜";
        CoursePacketInfo3.mCoursePacketDetails = "";
        CoursePacketInfoList.add(CoursePacketInfo3);
        LinearLayout coursepacket_linearlayout = mView.findViewById(R.id.coursepacket_linearlayout);
        coursepacket_linearlayout.removeAllViews();
        for (int i = 0; i < CoursePacketInfoList.size(); i ++){
            CoursePacketInfo CoursePacketInfo = CoursePacketInfoList.get(i);
            if (CoursePacketInfo == null){
                continue;
            }
            ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
            modelCoursePacketCover.ModelCoursePacketCoverOnClickListenerSet(this);
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity,CoursePacketInfo);
            coursepacket_linearlayout.addView(modelCoursePacketView);
            if (i == CoursePacketInfoList.size() - 1){
                View view = new View(mControlMainActivity);
                coursepacket_linearlayout.addView(view);
                LinearLayout.LayoutParams viewLp = (LinearLayout.LayoutParams) view.getLayoutParams();
                viewLp.height = height / 11;
                view.setLayoutParams(viewLp);
            }
        }
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

    //隐藏所有图层
    private void HideAllLayout(){
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

    //初始化课程包主界面
    public void CoursePacketMainInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
//        int rightMargin = width / 40;
        int bottomMargin = width / 35;
        RelativeLayout coursepacket_titleRelativeLayout = mView.findViewById(R.id.coursepacket_titleRelativeLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        coursepacket_titleRelativeLayout.setLayoutParams(lp);
        RelativeLayout coursepacket_titleRelativeLayout1 = mView.findViewById(R.id.coursepacket_titleRelativeLayout1);
        lp = (RelativeLayout.LayoutParams) coursepacket_titleRelativeLayout1.getLayoutParams();
        lp.topMargin = leftMargin;
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.bottomMargin = bottomMargin;
        lp.height = layoutheight;
        coursepacket_titleRelativeLayout1.setLayoutParams(lp);
        //返回
        ImageView coursepacket_title_return = mView.findViewById(R.id.coursepacket_title_return);
        lp = (RelativeLayout.LayoutParams) coursepacket_title_return.getLayoutParams();
        lp.height = width / 15;
        lp.width = width / 15;
        coursepacket_title_return.setLayoutParams(lp);
        //查询
//        ImageView coursepacket_title_search = mview.findViewById(R.id.coursepacket_title_search);
//        lp = (RelativeLayout.LayoutParams) coursepacket_title_search.getLayoutParams();
//        lp.height = width / 15;
//        lp.width = width / 15;
//        coursepacket_title_search.setLayoutParams(lp);
        ImageView coursepacket_title_search1 = mView.findViewById(R.id.coursepacket_title_search1);
        lp = (RelativeLayout.LayoutParams) coursepacket_title_search1.getLayoutParams();
        lp.height = width / 15;
        lp.width = width / 15;
        lp.leftMargin = width / 50;
        lp.rightMargin = leftMargin;
        coursepacket_title_search1.setLayoutParams(lp);
        //功能列表
        ModelSoftRadioGroup radiogroup = mView.findViewById(R.id.radiogroup);
        lp = (RelativeLayout.LayoutParams) radiogroup.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.rightMargin = leftMargin;
        lp.height = layoutheight;
        radiogroup.setLayoutParams(lp);
        //项目
        TextView softradiobutton0 = mView.findViewById(R.id.softradiobutton0);
        LinearLayout.LayoutParams LinearLayoutLp = (LinearLayout.LayoutParams) softradiobutton0.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        softradiobutton0.setLayoutParams(LinearLayoutLp);
        //时间排序
        ModelSoftRadioButton softradiobutton1 = mView.findViewById(R.id.softradiobutton1);
        LinearLayoutLp = (LinearLayout.LayoutParams) softradiobutton1.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        softradiobutton1.setLayoutParams(LinearLayoutLp);
        //热度排序
        ModelSoftRadioButton softradiobutton2 = mView.findViewById(R.id.softradiobutton2);
        LinearLayoutLp = (LinearLayout.LayoutParams) softradiobutton2.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        softradiobutton2.setLayoutParams(LinearLayoutLp);
        //重置搜索条件
        TextView softradiobutton3 = mView.findViewById(R.id.softradiobutton3);
        LinearLayoutLp = (LinearLayout.LayoutParams) softradiobutton3.getLayoutParams();
        LinearLayoutLp.width = (width - leftMargin * 2) / 4;
        softradiobutton3.setLayoutParams(LinearLayoutLp);
    }

    //初始化课程包-搜索界面
    public void CoursePacketSearchInit(){
        //主要参数
        int layoutheight = width / 10;
        int leftMargin = width / 25;
        int rightMargin = width / 40;
        int bottomMargin = width / 35;
//        ModelSearchView search_view = mview.findViewById(R.id.search_view);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) search_view.getLayoutParams();
//        lp.topMargin = leftMargin;
//        lp.leftMargin = leftMargin;
//        lp.rightMargin = leftMargin;
//        lp.bottomMargin = bottomMargin;
//        lp.height = layoutheight;
//        search_view.setLayoutParams(lp);
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
}
