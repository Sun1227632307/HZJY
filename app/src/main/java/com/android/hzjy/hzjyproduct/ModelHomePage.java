package com.android.hzjy.hzjyproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hzjy.hzjyproduct.consts.PlayType;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
/**
 * Created by dayuer on 19/7/2.
 * 首页
 */
public class ModelHomePage extends Fragment{
    private static int FragmentPage;
    private ModelImageSlideshow mImageSlideshow;
    private boolean mIsUseImageSlideShow = true;
    private boolean mIsUseFunctionShow = true;
    private View mView = null;

    private static ControlMainActivity mControlMainActivity;
    private TextView mTextView;
    private LayoutInflater inflater;
    private ViewGroup container;

    //点击查看课程详情需要用到的课程对象
    private ModelCourseCover mModelCourseCover = null;

    private class FunctionButtonInfo{
        int mId; // 1：课程包 2：公开课 3：题库 4：问答 5：课程表 6：新闻资讯 7：课程 8：我的
        String mButtonName; //按钮名称
    }
    //要显示的页面
//    private int FragmentPage;
    public  static  Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mControlMainActivity = content;
        ModelHomePage myFragment = new ModelHomePage();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(FragmentPage,container,false);
        this.inflater = inflater;
        this.container = container;
        HomePageShow();
        return mView;
    }

    //隐藏所有图层
    private void HideAllLayout(){
        LinearLayout homepage_layout_main = mView.findViewById(R.id.homepage_layout_main);
        homepage_layout_main.removeAllViews();
    }

    public void HomePageShow(){
        if (mView == null){
            return;
        }
        HideAllLayout();
        LinearLayout homepage_layout_main = mView.findViewById(R.id.homepage_layout_main);
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.homepage_layout1, null);
        homepage_layout_main.addView(view);
        ModelPtrFrameLayout ptrFrameLayout = view.findViewById(R.id.store_house_ptr_frame);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                ptrFrameLayout.refreshComplete();
            }
        });
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        if (mIsUseImageSlideShow) {
            mImageSlideshow = view.findViewById(R.id.is_gallery);
            //设置使用控件的宽高
            LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) mImageSlideshow.getLayoutParams();
            LP.width = LinearLayout.LayoutParams.MATCH_PARENT;
            LP.height = height / 4;
            mImageSlideshow.setLayoutParams(LP);
            // 初始化轮播图数据
            String[] imageUrls = {
//                "http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
//                "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
//                "https://b-ssl.duitang.com/uploads/item/201901/12/20190112192759_lobng.jpg",
//                "https://b-ssl.duitang.com/uploads/item/201902/13/20190213152812_thuwi.gif",
//                "http://p2.qhimgs4.com/t01c133219d80bc10c5.jpg"
            };
            String[] imageSkipUrls = {
//                "http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
//                "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
//                "http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg",
//                "http://pic1.zhimg.com/b6f59c017b43937bb85a81f9269b1ae8.jpg",
//                "http://pic2.zhimg.com/a62f9985cae17fe535a99901db18eba9.jpg"
            };
            initSlideImageData(imageSkipUrls, imageUrls);

            // 为ImageSlideshow设置数据
            mImageSlideshow.setDotSpace(12);
            mImageSlideshow.setDotSize(12);
            mImageSlideshow.setDelay(3000);
            mImageSlideshow.commit();
        }
        if (mIsUseFunctionShow){
            List<FunctionButtonInfo> FunctionButtonInfoList = new ArrayList<>();
            FunctionButtonInfo FunctionButtonInfo1 = new FunctionButtonInfo();
            FunctionButtonInfo1.mId = 1;
            FunctionButtonInfo1.mButtonName = "课程包";
            FunctionButtonInfoList.add(FunctionButtonInfo1);
            FunctionButtonInfo FunctionButtonInfo2 = new FunctionButtonInfo();
            FunctionButtonInfo2.mId = 2;
            FunctionButtonInfo2.mButtonName = "公开课";
            FunctionButtonInfoList.add(FunctionButtonInfo2);
            FunctionButtonInfo FunctionButtonInfo3 = new FunctionButtonInfo();
            FunctionButtonInfo3.mId = 3;
            FunctionButtonInfo3.mButtonName = "题库";
            FunctionButtonInfoList.add(FunctionButtonInfo3);
            FunctionButtonInfo FunctionButtonInfo4 = new FunctionButtonInfo();
            FunctionButtonInfo4.mId = 4;
            FunctionButtonInfo4.mButtonName = "问答";
            FunctionButtonInfoList.add(FunctionButtonInfo4);
            FunctionButtonInfo FunctionButtonInfo5 = new FunctionButtonInfo();
            FunctionButtonInfo5.mId = 5;
            FunctionButtonInfo5.mButtonName = "课程表";
            FunctionButtonInfoList.add(FunctionButtonInfo5);
            FunctionButtonInfo FunctionButtonInfo6 = new FunctionButtonInfo();
            FunctionButtonInfo6.mId = 6;
            FunctionButtonInfo6.mButtonName = "新闻资讯";
            FunctionButtonInfoList.add(FunctionButtonInfo6);
            FunctionButtonInfo FunctionButtonInfo7 = new FunctionButtonInfo();
            FunctionButtonInfo7.mId = 7;
            FunctionButtonInfo7.mButtonName = "课程";
            FunctionButtonInfoList.add(FunctionButtonInfo7);
            FunctionButtonInfo FunctionButtonInfo8 = new FunctionButtonInfo();
            FunctionButtonInfo8.mId = 8;
            FunctionButtonInfo8.mButtonName = "我的";
            FunctionButtonInfoList.add(FunctionButtonInfo8);
            while (FunctionButtonInfoList.size() > 8){ //功能按钮不能多于GridLayout的所创个数
                FunctionButtonInfoList.remove(FunctionButtonInfoList.size() - 1);
            }
            GridLayout mfunctionButton = view.findViewById(R.id.functionButton);
            for (int i = 0; i < FunctionButtonInfoList.size(); i ++){ //循环添加功能按钮
                FunctionButtonInfo functionButtonInfo = FunctionButtonInfoList.get(i);
                View functionbutton = inflater.inflate(R.layout.homepage_layout_functionbutton,container,false);
                LinearLayout mfunctionbuttonLinearLayout = functionbutton.findViewById(R.id.LinearLayout_functionbutton);
                //添加完以后重置按钮布局的大小与间隔
                FrameLayout.LayoutParams mfunctionbuttonLinearLayoutLayoutParams = (FrameLayout.LayoutParams) mfunctionbuttonLinearLayout.getLayoutParams();
                mfunctionbuttonLinearLayoutLayoutParams.width = width / 4;
                mfunctionbuttonLinearLayoutLayoutParams.topMargin = width / 14 / 4;
                mfunctionbuttonLinearLayout.setLayoutParams(mfunctionbuttonLinearLayoutLayoutParams);
                mfunctionButton.addView(functionbutton);
                TextView TextView_functionbutton = functionbutton.findViewById(R.id.TextView_functionbutton);
                //重置按钮名称
                TextView_functionbutton.setText(functionButtonInfo.mButtonName);
                LinearLayout.LayoutParams TextView_functionbuttonLayoutParams = (LinearLayout.LayoutParams) TextView_functionbutton.getLayoutParams();
                TextView_functionbuttonLayoutParams.topMargin = 0;
                TextView_functionbutton.setLayoutParams(TextView_functionbuttonLayoutParams);
                ImageView ImageView_functionbutton = functionbutton.findViewById(R.id.ImageView_functionbutton);
                LinearLayout.LayoutParams ImageView_functionbuttonLayoutParams = (LinearLayout.LayoutParams) ImageView_functionbutton.getLayoutParams();
                ImageView_functionbuttonLayoutParams.width = width / 8;
                ImageView_functionbuttonLayoutParams.height = width / 8;
                ImageView_functionbutton.setLayoutParams(ImageView_functionbuttonLayoutParams);
                //判断按钮的id 来加载不同按钮的图片   1：课程包 2：公开课 3：题库 4：问答 5：课程表 6：新闻资讯 7：课程 8：我的
                if (functionButtonInfo.mId == 1){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_coursepacketbutton));
                } else if (functionButtonInfo.mId == 2){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_openclassbutton));
                } else if (functionButtonInfo.mId == 3){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_questionbankbutton));
                } else if (functionButtonInfo.mId == 4){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_answersbutton));
                } else if (functionButtonInfo.mId == 5){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_classchedulecardbutton));
                } else if (functionButtonInfo.mId == 6){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_newsbutton));
                } else if (functionButtonInfo.mId == 7){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_coursebutton));
                } else if (functionButtonInfo.mId == 8){
                    ImageView_functionbutton.setImageDrawable(getResources().getDrawable(R.drawable.functionbutton_mybutton));
                }
                mfunctionbuttonLinearLayout.setOnClickListener(v -> {
                    //判断点击什么按钮，跳转什么功能界面1：课程包 2：公开课 3：题库 4：问答 5：课程表 6：新闻资讯 7：课程 8：我的
                    if (functionButtonInfo.mId == 1){
                        mControlMainActivity.Page_MoreCoursePacket();
                    } else if (functionButtonInfo.mId == 2){
//                        mControlMainActivity.LoginLiveOrPlayback("391068","dadada","",PlayType.LIVE);
//                        mControlMainActivity.LoginLiveOrPlayback("365061","dadada","799723",PlayType.PLAYBACK);
                        mControlMainActivity.Page_OpenClass();
                    } else if (functionButtonInfo.mId == 3){
                        mControlMainActivity.Page_QuestionBank();
                    } else if (functionButtonInfo.mId == 4){
                        mControlMainActivity.Page_CommunityAnswer();
                    } else if (functionButtonInfo.mId == 5){
                        mControlMainActivity.Page_ClassCheduleCard();
                    } else if (functionButtonInfo.mId == 6){
                        mControlMainActivity.Page_News();
                    } else if (functionButtonInfo.mId == 7){
                        mControlMainActivity.Page_Course();
                    } else if (functionButtonInfo.mId == 8){
                        mControlMainActivity.Page_My();
                    }
                });
            }
        }
        //更多课程
        RelativeLayout morecourseRelativeLayout = view.findViewById(R.id.morecourse);
        LinearLayout.LayoutParams morecourseRelativeLayoutLp = (LinearLayout.LayoutParams) morecourseRelativeLayout.getLayoutParams();
        morecourseRelativeLayoutLp.topMargin = width / 25;
        morecourseRelativeLayoutLp.rightMargin = width / 28;
        morecourseRelativeLayoutLp.leftMargin = width / 28;
        morecourseRelativeLayout.setLayoutParams(morecourseRelativeLayoutLp);
        List<CourseInfo> CourseInfoList = new ArrayList<>();
        {
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
            CourseInfo2.mCourseType = "直播";
            CourseInfo2.mIsReferCourse = "1";
            CourseInfoList.add(CourseInfo2);
        }
        LinearLayout courseModelLinearLayout = view.findViewById(R.id.coursemodel);
        for (int i = 0; i < CourseInfoList.size(); i ++){
            CourseInfo courseInfo = CourseInfoList.get(i);
            if (courseInfo == null){
                continue;
            }
            if (courseInfo.mIsReferCourse.equals("0")){ //非推荐课程在这里不显示
                continue;
            }
            ModelCourseCover modelCourseCover = new ModelCourseCover();
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity,courseInfo);
            modelCourseView.setOnClickListener(v->{ //点击某一课程
                ModelCourseCover modelCourseCover1 = new ModelCourseCover();
                View modelCourseView1 = modelCourseCover1.ModelCourseCover(mControlMainActivity,courseInfo);
                modelCourseCover1.CourseDetailsShow();
                HideAllLayout();
                homepage_layout_main.addView(modelCourseView1);
                mControlMainActivity.onClickCourseDetails();
                mModelCourseCover = modelCourseCover1;
            });
            courseModelLinearLayout.addView(modelCourseView);
        }
        View courseline = view.findViewById(R.id.courseline);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) courseline.getLayoutParams();
        lp.rightMargin = width / 25;
        lp.leftMargin = width / 25;
        courseline.setLayoutParams(lp);
        //更多课程包
        RelativeLayout morecoursepacketRelativeLayout = view.findViewById(R.id.morecoursepacket);
        LinearLayout.LayoutParams morecoursepacketRelativeLayoutLp = (LinearLayout.LayoutParams) morecoursepacketRelativeLayout.getLayoutParams();
        morecoursepacketRelativeLayoutLp.topMargin = width / 25;
        morecoursepacketRelativeLayoutLp.rightMargin = width / 28;
        morecoursepacketRelativeLayoutLp.leftMargin = width / 28;
        morecoursepacketRelativeLayout.setLayoutParams(morecoursepacketRelativeLayoutLp);
        List<CoursePacketInfo> CoursePacketInfoList = new ArrayList<>();
        {
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
            CoursePacketInfoList.add(CoursePacketInfo2);
        }
        LinearLayout coursePacketModelLinearLayout = view.findViewById(R.id.coursepacketmodel);
        for (int i = 0; i < CoursePacketInfoList.size(); i ++){
            CoursePacketInfo CoursePacketInfo = CoursePacketInfoList.get(i);
            if (CoursePacketInfo == null){
                continue;
            }
            if (CoursePacketInfo.mIsReferCoursePacket.equals("0")){ //非推荐课程在这里不显示
                continue;
            }
            ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity,CoursePacketInfo);
            modelCoursePacketView.setOnClickListener(v->{ //点击某一课程包
                ModelCoursePacketCover modelCoursePacketCover1 = new ModelCoursePacketCover();
                View modelCoursePacketView1 = modelCoursePacketCover1.ModelCoursePacketCover(mControlMainActivity,CoursePacketInfo);
                modelCoursePacketCover1.CoursePacketDetailsShow();
                HideAllLayout();
                homepage_layout_main.addView(modelCoursePacketView1);
                mControlMainActivity.onClickCoursePacketDetails();
            });
            coursePacketModelLinearLayout.addView(modelCoursePacketView);
        }

        View line = view.findViewById(R.id.line);
        LinearLayout.LayoutParams lineRelativeLayoutLp = (LinearLayout.LayoutParams) line.getLayoutParams();
        lineRelativeLayoutLp.height = height / 11;
        line.setLayoutParams(lineRelativeLayoutLp);
    }
    /**
     * 初始化数据
     */
    private void initSlideImageData(String[] imageSkipUrls,String[] imageUrls) {
        if (imageUrls == null){
            return;
        }
        //如果有url,使用远程图片
        for (int i = 0; i < imageUrls.length; i ++) {
            if (imageSkipUrls == null){
                mImageSlideshow.addImageUrlAndSkipUrl(null,imageUrls[i]);
                continue;
            }
            if (i >= imageSkipUrls.length){
                mImageSlideshow.addImageUrlAndSkipUrl(null,imageUrls[i]);
                continue;
            }
            mImageSlideshow.addImageUrlAndSkipUrl(imageSkipUrls[i],imageUrls[i]);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        // 释放资源
        if (mImageSlideshow != null) {
            mImageSlideshow.releaseResource();
        }
        super.onDestroy();
    }

    public void ModelCourseCoverQuestionPictureAdd(Intent data){ //添加图片成功后，转到图片界面
        if (mModelCourseCover != null){
            mModelCourseCover.ModelCourseCoverQuestionPictureAdd(data);
        }
    }
}