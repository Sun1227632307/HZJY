package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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

public class ModelMy extends Fragment{
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ,mMyView,mMyClassView,mMyClassPacketView,mMyCollectView,mMyCacheView,mMyCacheManagementCacheView
            ,mMyOrderView,mMyOrderDetailsView,mMyCouponView,mMyMessageView,mMyMessageView0,mMyAnswerView,mMyAnswerDetailsView;
    static private UserInfo mUserInfo = new UserInfo();
    private int height = 1344;
    private int width = 720;
    private int mMyCollectLastTabIndex = 1;
    private String mMyCollectCurrentTab = "course";
    private int mMyOrderLastTabIndex = 1;
    private String mMyOrderCurrentTab = "all";
    private int mMyCouponLastTabIndex = 1;
    private String mMyCouponCurrentTab = "notused";
    private int mMyAnswerLastTabIndex = 1;
    private String mMyAnswerCurrentTab = "course";
    private ControllerCenterDialog mMyDialog,mMyCouponDialog,mMyMessageDeleteDialog;
    private ControllerMyMessage1Adapter adapter;

    //测试数据
    private CourseInfo CourseInfo1 = new CourseInfo();
    private Map<String,CourseRecordPlayDownloadInfo> mCourseRecordPlayDownloadInfoMap = new HashMap<>();


    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage,UserInfo userinfo){
        mContext = context;
        if (userinfo != null) {
            mUserInfo.mUserHeadUrl = userinfo.mUserHeadUrl;
            mUserInfo.mUserName = userinfo.mUserName;
            mUserInfo.mUserLoginState = userinfo.mUserLoginState;
            mUserInfo.mUserIntroduce = userinfo.mUserIntroduce;
        }
        mControlMainActivity = content;
        ModelMy myFragment = new ModelMy();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage,container,false);
        //测试数据
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
        int count = 0;
        for (int i = 0; i < CourseInfo1.mCourseChaptersInfoList.size() ; i ++) {
            CourseChaptersInfo courseChaptersInfo = CourseInfo1.mCourseChaptersInfoList.get(i);
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
        ModelMyInit();
        return mview;
    }

    //隐藏所有图层
    private void HideAllLayout(){
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        my_layout_main.removeAllViews();
    }

    public void ModelMyUserInfoSet(UserInfo userInfo){
        if (userInfo != null) {
            mUserInfo.mUserHeadUrl = userInfo.mUserHeadUrl;
            mUserInfo.mUserName = userInfo.mUserName;
            mUserInfo.mUserLoginState = userInfo.mUserLoginState;
            mUserInfo.mUserIntroduce = userInfo.mUserIntroduce;
        }
        ModelMyInit();
    }

    private void ModelMyInit(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyView == null) {
            mMyView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.my_layout_main, null);
        }
        my_layout_main.addView(mMyView);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        //重置名片背景图片的大小及位置
        ControllerCustomRoundAngleImageView businesscardbackground = mMyView.findViewById(R.id.businesscardbackground);
        businesscardbackground.setImageDrawable(mControlMainActivity.getResources().getDrawable(R.drawable.businesscardbackground));//如果没有url，加载默认图片
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) businesscardbackground.getLayoutParams();
        lp.leftMargin = width / 50;
        lp.rightMargin = width / 50;
        lp.topMargin = width / 50;
        lp.height = height / 4;
        businesscardbackground.setLayoutParams(lp);
        //加载用户头像
        ControllerCustomRoundAngleImageView headportraitImageView = mMyView.findViewById(R.id.headportrait);
        Glide.with(mControlMainActivity).
                load(mUserInfo.mUserHeadUrl).listener(new RequestListener<Drawable>() {
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
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelmy_myheaddefault)).into(headportraitImageView);
        lp = (RelativeLayout.LayoutParams) headportraitImageView.getLayoutParams();
        lp.leftMargin = width / 15;
        lp.rightMargin = 0;
        lp.topMargin = (width / 50 + height / 4 - width / 4) / 2;
        lp.height = width / 4;
        lp.width = width / 4;
        headportraitImageView.setLayoutParams(lp);
        TextView settingTextView = mMyView.findViewById(R.id.setting); //设置
        lp = (RelativeLayout.LayoutParams) settingTextView.getLayoutParams();
        lp.rightMargin = width / 15;
        lp.topMargin = width / 20;
        settingTextView.setLayoutParams(lp);
        TextView usernameTextView = mMyView.findViewById(R.id.username); //用户登录
        lp = (RelativeLayout.LayoutParams) usernameTextView.getLayoutParams();
        lp.topMargin = (width / 50 + height / 4 - width / 20) / 2;
        lp.leftMargin = width / 25;
        usernameTextView.setLayoutParams(lp);
        TextView userinfoTextView = mMyView.findViewById(R.id.userinfo); //用户的个人说明
        lp = (RelativeLayout.LayoutParams) userinfoTextView.getLayoutParams();
        lp.topMargin = width / 25;
        lp.leftMargin = width / 25;
        userinfoTextView.setLayoutParams(lp);
        //根据用户信息填写资料
        if (mUserInfo.mUserLoginState.equals("1")){
            usernameTextView.setText(mUserInfo.mUserName);
            userinfoTextView.setText(mUserInfo.mUserIntroduce);
        }
        //功能列表布局重置
        //我的课程
        ImageView mycourse_imageview = mMyView.findViewById(R.id.mycourse_imageview);
        RelativeLayout.LayoutParams mycourse_imageviewlp = (RelativeLayout.LayoutParams) mycourse_imageview.getLayoutParams();
        mycourse_imageviewlp.topMargin = width / 25;
        mycourse_imageviewlp.leftMargin = width / 25;
        mycourse_imageviewlp.rightMargin = width / 25;
        mycourse_imageviewlp.bottomMargin = width / 35;
        mycourse_imageviewlp.width = width / 15;
        mycourse_imageviewlp.height = width / 15;
        mycourse_imageview.setLayoutParams(mycourse_imageviewlp);
        TextView mycourse_textview = mMyView.findViewById(R.id.mycourse_textview);
        RelativeLayout.LayoutParams mycourse_textviewlp = (RelativeLayout.LayoutParams) mycourse_textview.getLayoutParams();
        mycourse_textviewlp.topMargin = width / 25;
        mycourse_textviewlp.bottomMargin = width / 35;
        mycourse_textviewlp.height = width / 15;
        mycourse_textview.setLayoutParams(mycourse_textviewlp);
        ImageView mycourse_go = mMyView.findViewById(R.id.mycourse_go);
        RelativeLayout.LayoutParams mycourse_golp = (RelativeLayout.LayoutParams) mycourse_go.getLayoutParams();
        mycourse_golp.topMargin = (width / 35 + width / 15) / 2;
        mycourse_golp.rightMargin = width / 25;
        mycourse_golp.height = width / 25;
        mycourse_golp.width = width / 15;
        mycourse_go.setLayoutParams(mycourse_golp);
        //我的课程包
        ImageView mycoursepacket_imageview = mMyView.findViewById(R.id.mycoursepacket_imageview);
        RelativeLayout.LayoutParams mycoursepacket_imageviewlp = (RelativeLayout.LayoutParams) mycoursepacket_imageview.getLayoutParams();
        mycoursepacket_imageviewlp.topMargin = width / 25;
        mycoursepacket_imageviewlp.leftMargin = width / 25;
        mycoursepacket_imageviewlp.rightMargin = width / 25;
        mycoursepacket_imageviewlp.bottomMargin = width / 25;
        mycoursepacket_imageviewlp.width = width / 15;
        mycoursepacket_imageviewlp.height = width / 15;
        mycoursepacket_imageview.setLayoutParams(mycoursepacket_imageviewlp);
        TextView mycoursepacket_textview = mMyView.findViewById(R.id.mycoursepacket_textview);
        RelativeLayout.LayoutParams mycoursepacket_textviewlp = (RelativeLayout.LayoutParams) mycoursepacket_textview.getLayoutParams();
        mycoursepacket_textviewlp.topMargin = width / 25;
        mycoursepacket_textviewlp.height = width / 15;
        mycoursepacket_textviewlp.bottomMargin = width / 25;
        mycoursepacket_textview.setLayoutParams(mycoursepacket_textviewlp);
        ImageView mycoursepacket_go = mMyView.findViewById(R.id.mycoursepacket_go);
        RelativeLayout.LayoutParams mycoursepacket_golp = (RelativeLayout.LayoutParams) mycoursepacket_go.getLayoutParams();
        mycoursepacket_golp.topMargin = (width / 25 + width / 15) / 2;
        mycoursepacket_golp.rightMargin = width / 25;
        mycoursepacket_golp.height = width / 25;
        mycoursepacket_golp.width = width / 15;
        mycoursepacket_go.setLayoutParams(mycoursepacket_golp);
        //我的收藏
        ImageView mycollect_imageview = mMyView.findViewById(R.id.mycollect_imageview);
        RelativeLayout.LayoutParams mycollect_imageviewlp = (RelativeLayout.LayoutParams) mycollect_imageview.getLayoutParams();
        mycollect_imageviewlp.topMargin = width / 25;
        mycollect_imageviewlp.width = width / 15;
        mycollect_imageviewlp.height = width / 15;
        mycollect_imageviewlp.leftMargin = width / 25;
        mycollect_imageviewlp.rightMargin = width / 25;
        mycollect_imageviewlp.bottomMargin = width / 25;
        mycollect_imageview.setLayoutParams(mycollect_imageviewlp);
        TextView mycollect_textview = mMyView.findViewById(R.id.mycollect_textview);
        RelativeLayout.LayoutParams mycollect_textviewlp = (RelativeLayout.LayoutParams) mycollect_textview.getLayoutParams();
        mycollect_textviewlp.topMargin = width / 25;
        mycollect_textviewlp.height = width / 15;
        mycollect_textviewlp.bottomMargin = width / 25;
        mycollect_textview.setLayoutParams(mycollect_textviewlp);
        ImageView mycollect_go = mMyView.findViewById(R.id.mycollect_go);
        RelativeLayout.LayoutParams mycollect_golp = (RelativeLayout.LayoutParams) mycollect_go.getLayoutParams();
        mycollect_golp.topMargin = (width / 25 + width / 15) / 2;
        mycollect_golp.rightMargin = width / 25;
        mycollect_golp.height = width / 25;
        mycollect_golp.width = width / 15;
        mycollect_go.setLayoutParams(mycollect_golp);
        //我的题库
        ImageView myquestion_imageview = mMyView.findViewById(R.id.myquestion_imageview);
        RelativeLayout.LayoutParams myquestion_imageviewlp = (RelativeLayout.LayoutParams) myquestion_imageview.getLayoutParams();
        myquestion_imageviewlp.topMargin = width / 25;
        myquestion_imageviewlp.width = width / 15;
        myquestion_imageviewlp.height = width / 15;
        myquestion_imageviewlp.leftMargin = width / 25;
        myquestion_imageviewlp.rightMargin = width / 25;
        myquestion_imageviewlp.bottomMargin = width / 25;
        myquestion_imageview.setLayoutParams(myquestion_imageviewlp);
        TextView myquestion_textview = mMyView.findViewById(R.id.myquestion_textview);
        RelativeLayout.LayoutParams myquestion_textviewlp = (RelativeLayout.LayoutParams) myquestion_textview.getLayoutParams();
        myquestion_textviewlp.topMargin = width / 25;
        myquestion_textviewlp.height = width / 15;
        myquestion_textviewlp.bottomMargin = width / 25;
        myquestion_textview.setLayoutParams(myquestion_textviewlp);
        ImageView myquestion_go = mMyView.findViewById(R.id.myquestion_go);
        RelativeLayout.LayoutParams myquestion_golp = (RelativeLayout.LayoutParams) myquestion_go.getLayoutParams();
        myquestion_golp.topMargin = (width / 25 + width / 15) / 2;
        myquestion_golp.rightMargin = width / 25;
        myquestion_golp.height = width / 25;
        myquestion_golp.width = width / 15;
        myquestion_go.setLayoutParams(myquestion_golp);
        //我的订单
        ImageView myorder_imageview = mMyView.findViewById(R.id.myorder_imageview);
        RelativeLayout.LayoutParams myorder_imageviewlp = (RelativeLayout.LayoutParams) myorder_imageview.getLayoutParams();
        myorder_imageviewlp.topMargin = width / 25;
        myorder_imageviewlp.width = width / 15;
        myorder_imageviewlp.height = width / 15;
        myorder_imageviewlp.leftMargin = width / 25;
        myorder_imageviewlp.rightMargin = width / 25;
        myorder_imageviewlp.bottomMargin = width / 25;
        myorder_imageview.setLayoutParams(myorder_imageviewlp);
        TextView myorder_textview = mMyView.findViewById(R.id.myorder_textview);
        RelativeLayout.LayoutParams myorder_textviewlp = (RelativeLayout.LayoutParams) myorder_textview.getLayoutParams();
        myorder_textviewlp.topMargin = width / 25;
        myorder_textviewlp.height = width / 15;
        myorder_textviewlp.bottomMargin = width / 25;
        myorder_textview.setLayoutParams(myorder_textviewlp);
        ImageView myorder_go = mMyView.findViewById(R.id.myorder_go);
        RelativeLayout.LayoutParams myorder_golp = (RelativeLayout.LayoutParams) myorder_go.getLayoutParams();
        myorder_golp.topMargin = (width / 25 + width / 15) / 2;
        myorder_golp.rightMargin = width / 25;
        myorder_golp.height = width / 25;
        myorder_golp.width = width / 15;
        myorder_go.setLayoutParams(myorder_golp);
        //我的消息
        ImageView mymessage_imageview = mMyView.findViewById(R.id.mymessage_imageview);
        RelativeLayout.LayoutParams mymessage_imageviewlp = (RelativeLayout.LayoutParams) mymessage_imageview.getLayoutParams();
        mymessage_imageviewlp.topMargin = width / 25;
        mymessage_imageviewlp.width = width / 15;
        mymessage_imageviewlp.height = width / 15;
        mymessage_imageviewlp.leftMargin = width / 25;
        mymessage_imageviewlp.rightMargin = width / 25;
        mymessage_imageviewlp.bottomMargin = width / 25;
        mymessage_imageview.setLayoutParams(mymessage_imageviewlp);
        TextView mymessage_textview = mMyView.findViewById(R.id.mymessage_textview);
        RelativeLayout.LayoutParams mymessage_textviewlp = (RelativeLayout.LayoutParams) mymessage_textview.getLayoutParams();
        mymessage_textviewlp.topMargin = width / 25;
        mymessage_textviewlp.height = width / 15;
        mymessage_textviewlp.bottomMargin = width / 25;
        mymessage_textview.setLayoutParams(mymessage_textviewlp);
        ImageView mymessage_go = mMyView.findViewById(R.id.mymessage_go);
        RelativeLayout.LayoutParams mymessage_golp = (RelativeLayout.LayoutParams) mymessage_go.getLayoutParams();
        mymessage_golp.topMargin = (width / 25 + width / 15) / 2;
        mymessage_golp.rightMargin = width / 25;
        mymessage_golp.height = width / 25;
        mymessage_golp.width = width / 15;
        mymessage_go.setLayoutParams(mymessage_golp);
        //我的问答
        ImageView myanswer_imageview = mMyView.findViewById(R.id.myanswer_imageview);
        RelativeLayout.LayoutParams myanswer_imageviewlp = (RelativeLayout.LayoutParams) myanswer_imageview.getLayoutParams();
        myanswer_imageviewlp.topMargin = width / 25;
        myanswer_imageviewlp.width = width / 15;
        myanswer_imageviewlp.height = width / 15;
        myanswer_imageviewlp.leftMargin = width / 25;
        myanswer_imageviewlp.rightMargin = width / 25;
        myanswer_imageviewlp.bottomMargin = width / 25;
        myanswer_imageview.setLayoutParams(myanswer_imageviewlp);
        TextView myanswer_textview = mMyView.findViewById(R.id.myanswer_textview);
        RelativeLayout.LayoutParams myanswer_textviewlp = (RelativeLayout.LayoutParams) myanswer_textview.getLayoutParams();
        myanswer_textviewlp.topMargin = width / 25;
        myanswer_textviewlp.height = width / 15;
        myanswer_textviewlp.bottomMargin = width / 25;
        myanswer_textview.setLayoutParams(myanswer_textviewlp);
        ImageView myanswer_go = mMyView.findViewById(R.id.myanswer_go);
        RelativeLayout.LayoutParams myanswer_golp = (RelativeLayout.LayoutParams) myanswer_go.getLayoutParams();
        myanswer_golp.topMargin = (width / 25 + width / 15) / 2;
        myanswer_golp.rightMargin = width / 25;
        myanswer_golp.height = width / 25;
        myanswer_golp.width = width / 15;
        myanswer_go.setLayoutParams(myanswer_golp);
        //我的缓存
        ImageView mycache_imageview = mMyView.findViewById(R.id.mycache_imageview);
        RelativeLayout.LayoutParams mycache_imageviewlp = (RelativeLayout.LayoutParams) mycache_imageview.getLayoutParams();
        mycache_imageviewlp.topMargin = width / 25;
        mycache_imageviewlp.width = width / 15;
        mycache_imageviewlp.height = width / 15;
        mycache_imageviewlp.leftMargin = width / 25;
        mycache_imageviewlp.rightMargin = width / 25;
        mycache_imageviewlp.bottomMargin = width / 25;
        mycache_imageview.setLayoutParams(mycache_imageviewlp);
        TextView mycache_textview = mMyView.findViewById(R.id.mycache_textview);
        RelativeLayout.LayoutParams mycache_textviewlp = (RelativeLayout.LayoutParams) mycache_textview.getLayoutParams();
        mycache_textviewlp.topMargin = width / 25;
        mycache_textviewlp.height = width / 15;
        mycache_textviewlp.bottomMargin = width / 25;
        mycache_textview.setLayoutParams(mycache_textviewlp);
        ImageView mycache_go = mMyView.findViewById(R.id.mycache_go);
        RelativeLayout.LayoutParams mycache_golp = (RelativeLayout.LayoutParams) mycache_go.getLayoutParams();
        mycache_golp.topMargin = (width / 25 + width / 15) / 2;
        mycache_golp.rightMargin = width / 25;
        mycache_golp.height = width / 25;
        mycache_golp.width = width / 15;
        mycache_go.setLayoutParams(mycache_golp);
        //我的优惠券
        ImageView mycoupon_imageview = mMyView.findViewById(R.id.mycoupon_imageview);
        RelativeLayout.LayoutParams mycoupon_imageviewlp = (RelativeLayout.LayoutParams) mycoupon_imageview.getLayoutParams();
        mycoupon_imageviewlp.topMargin = width / 25;
        mycoupon_imageviewlp.width = width / 15;
        mycoupon_imageviewlp.height = width / 15;
        mycoupon_imageviewlp.leftMargin = width / 25;
        mycoupon_imageviewlp.rightMargin = width / 25;
        mycoupon_imageviewlp.bottomMargin = width / 25;
        mycoupon_imageview.setLayoutParams(mycoupon_imageviewlp);
        TextView mycoupon_textview = mMyView.findViewById(R.id.mycoupon_textview);
        RelativeLayout.LayoutParams mycoupon_textviewlp = (RelativeLayout.LayoutParams) mycoupon_textview.getLayoutParams();
        mycoupon_textviewlp.topMargin = width / 25;
        mycoupon_textviewlp.height = width / 15;
        mycoupon_textviewlp.bottomMargin = width / 25;
        mycoupon_textview.setLayoutParams(mycoupon_textviewlp);
        ImageView mycoupon_go = mMyView.findViewById(R.id.mycoupon_go);
        RelativeLayout.LayoutParams mycoupon_golp = (RelativeLayout.LayoutParams) mycoupon_go.getLayoutParams();
        mycoupon_golp.topMargin = (width / 25 + width / 15) / 2;
        mycoupon_golp.rightMargin = width / 25;
        mycoupon_golp.height = width / 25;
        mycoupon_golp.width = width / 15;
        mycoupon_go.setLayoutParams(mycoupon_golp);
        //我的余额
        ImageView mybalance_imageview = mMyView.findViewById(R.id.mybalance_imageview);
        RelativeLayout.LayoutParams mybalance_imageviewlp = (RelativeLayout.LayoutParams) mybalance_imageview.getLayoutParams();
        mybalance_imageviewlp.topMargin = width / 25;
        mybalance_imageviewlp.width = width / 15;
        mybalance_imageviewlp.height = width / 15;
        mybalance_imageviewlp.leftMargin = width / 25;
        mybalance_imageviewlp.rightMargin = width / 25;
        mybalance_imageviewlp.bottomMargin = width / 25;
        mybalance_imageview.setLayoutParams(mybalance_imageviewlp);
        TextView mybalance_textview = mMyView.findViewById(R.id.mybalance_textview);
        RelativeLayout.LayoutParams mybalance_textviewlp = (RelativeLayout.LayoutParams) mybalance_textview.getLayoutParams();
        mybalance_textviewlp.topMargin = width / 25;
        mybalance_textviewlp.height = width / 15;
        mybalance_textviewlp.bottomMargin = width / 25;
        mybalance_textview.setLayoutParams(mybalance_textviewlp);
        ImageView mybalance_go = mMyView.findViewById(R.id.mybalance_go);
        RelativeLayout.LayoutParams mybalance_golp = (RelativeLayout.LayoutParams) mybalance_go.getLayoutParams();
        mybalance_golp.topMargin = (width / 25 + width / 15) / 2;
        mybalance_golp.rightMargin = width / 25;
        mybalance_golp.height = width / 25;
        mybalance_golp.width = width / 15;
        mybalance_go.setLayoutParams(mybalance_golp);
        View line = mMyView.findViewById(R.id.modelmy_line);
        LinearLayout.LayoutParams lineRelativeLayoutLp = (LinearLayout.LayoutParams) line.getLayoutParams();
        lineRelativeLayoutLp.height = height / 11;
        line.setLayoutParams(lineRelativeLayoutLp);
    }

    //展示我的课程界面
    public void MyClassShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyClassView == null) {
            mMyClassView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass, null);
            ModelPtrFrameLayout modelmy_myclass_main_content_frame = mMyClassView.findViewById(R.id.modelmy_myclass_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myclass_main_content_frame.addPtrUIHandler(header);
            modelmy_myclass_main_content_frame.setHeaderView(header);
            modelmy_myclass_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myclass_main_content_frame.refreshComplete();
                }
            });
        }
        my_layout_main.addView(mMyClassView);
        LinearLayout modelmy_myclass_main_content = mMyClassView.findViewById(R.id.modelmy_myclass_main_content);
        modelmy_myclass_main_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass1, null);
        modelmy_myclass_main_content.addView(view);
        ControllerCustomRoundAngleImageView modelmy_myclass1_cover = view.findViewById(R.id.modelmy_myclass1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_myclass1_cover);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v->{
            //测试数据
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
            //跳转课程详情
            ModelCourseCover modelCourseCover = new ModelCourseCover();
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity,CourseInfo1);
            modelCourseCover.CourseDetailsShow();
            HideAllLayout();
            my_layout_main.addView(modelCourseView);
            mControlMainActivity.onClickCourseDetails();
        });
        TextView modelmy_myclass1_agreement = view.findViewById(R.id.modelmy_myclass1_agreement);
        modelmy_myclass1_agreement.setOnClickListener(v->{ //点击查看协议

        });
        View modelmy_myclass1_line1 = view.findViewById(R.id.modelmy_myclass1_line1);
        modelmy_myclass1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的课程包界面
    public void MyClassPacketShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyClassPacketView == null) {
            mMyClassPacketView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket, null);
            ModelPtrFrameLayout modelmy_myclasspacket_main_content_frame = mMyClassPacketView.findViewById(R.id.modelmy_myclasspacket_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myclasspacket_main_content_frame.addPtrUIHandler(header);
            modelmy_myclasspacket_main_content_frame.setHeaderView(header);
            modelmy_myclasspacket_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myclasspacket_main_content_frame.refreshComplete();
                }
            });
        }
        my_layout_main.addView(mMyClassPacketView);
        LinearLayout modelmy_myclasspacket_main_content = mMyClassPacketView.findViewById(R.id.modelmy_myclasspacket_main_content);
        modelmy_myclasspacket_main_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket1, null);
        modelmy_myclasspacket_main_content.addView(view);

        ControllerCustomRoundAngleImageView modelmy_myclasspacket1_cover = view.findViewById(R.id.modelmy_myclasspacket1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(modelmy_myclasspacket1_cover);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v->{
            //测试数据
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
            //跳转课程包详情
            ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity,CoursePacketInfo1);
            modelCoursePacketCover.CoursePacketDetailsShow();
            HideAllLayout();
            my_layout_main.addView(modelCoursePacketView);
            mControlMainActivity.onClickCoursePacketDetails();
        });
        TextView modelmy_myclasspacket1_agreement = view.findViewById(R.id.modelmy_myclasspacket1_agreement);
        modelmy_myclasspacket1_agreement.setOnClickListener(v->{ //点击查看协议

        });
        View modelmy_myclasspacket1_line1 = view.findViewById(R.id.modelmy_myclasspacket1_line1);
        modelmy_myclasspacket1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的收藏界面
    public void MyCollectShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCollectView == null) {
            mMyCollectView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycollect, null);
            ModelPtrFrameLayout modelmy_mycollect_main_content_frame = mMyCollectView.findViewById(R.id.modelmy_mycollect_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_mycollect_main_content_frame.addPtrUIHandler(header);
            modelmy_mycollect_main_content_frame.setHeaderView(header);
            modelmy_mycollect_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_mycollect_main_content_frame.refreshComplete();
                }
            });
            TextView modelmy_mycollect_tab_course = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_course);
            modelmy_mycollect_tab_course.setOnClickListener(v->{
                if (!mMyCollectCurrentTab.equals("course")) {
                    ImageView modelmy_mycollect_cursor1 = mMyCollectView.findViewById(R.id.modelmy_mycollect_cursor1);
                    Animation animation = new TranslateAnimation(( mMyCollectLastTabIndex - 1)  * width / 2,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycollect_cursor1.startAnimation(animation);
                    TextView modelmy_mycollect_tab_coursepacket = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_coursepacket);
                    modelmy_mycollect_tab_course.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_mycollect_tab_coursepacket.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyCollectLastTabIndex = 1;
                mMyCollectCurrentTab = "course";
                LinearLayout modelmy_mycollect_main_content = mMyCollectView.findViewById(R.id.modelmy_mycollect_main_content);
                modelmy_mycollect_main_content.removeAllViews();
                //可循环调用以下方法，添加多个课程
                MyCollectShow_MyCourse(modelmy_mycollect_main_content);
            });
            TextView modelmy_mycollect_tab_coursepacket = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_coursepacket);
            modelmy_mycollect_tab_coursepacket.setOnClickListener(v->{
                if (!mMyCollectCurrentTab.equals("coursepacket")) {
                    ImageView modelmy_mycollect_cursor1 = mMyCollectView.findViewById(R.id.modelmy_mycollect_cursor1);
                    Animation animation = new TranslateAnimation(( mMyCollectLastTabIndex - 1)  * width / 2,width / 2 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycollect_cursor1.startAnimation(animation);
                    modelmy_mycollect_tab_course.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycollect_tab_coursepacket.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mMyCollectLastTabIndex = 2;
                mMyCollectCurrentTab = "coursepacket";
                LinearLayout modelmy_mycollect_main_content = mMyCollectView.findViewById(R.id.modelmy_mycollect_main_content);
                modelmy_mycollect_main_content.removeAllViews();
                //可循环调用以下方法，添加多个课程包
                MyCollectShow_MyCoursePacket(modelmy_mycollect_main_content);
            });
        }
        my_layout_main.addView(mMyCollectView);
        LinearLayout modelmy_mycollect_main_content = mMyCollectView.findViewById(R.id.modelmy_mycollect_main_content);
        modelmy_mycollect_main_content.removeAllViews();
        ImageView modelmy_mycollect_cursor1 = mMyCollectView.findViewById(R.id.modelmy_mycollect_cursor1);
        int x = width / 4 - mMyCollectView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        modelmy_mycollect_cursor1.setX(x);
        //默认选中的为课程
        mMyCollectLastTabIndex = 1;
        mMyCollectCurrentTab = "course";
        TextView modelmy_mycollect_tab_course = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_course);
        TextView modelmy_mycollect_tab_coursepacket = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_coursepacket);
        modelmy_mycollect_tab_course.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        modelmy_mycollect_tab_coursepacket.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCollectView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        MyCollectShow_MyCourse(modelmy_mycollect_main_content);
    }

    //展示我的收藏界面-课程
    private void MyCollectShow_MyCourse(LinearLayout modelmy_mycollect_main_content){
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass1, null);
        modelmy_mycollect_main_content.addView(view);
        ControllerCustomRoundAngleImageView modelmy_myclass1_cover = view.findViewById(R.id.modelmy_myclass1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_myclass1_cover);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v->{
            //跳转课程详情
            ModelCourseCover modelCourseCover = new ModelCourseCover();
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity,CourseInfo1);
            modelCourseCover.CourseDetailsShow();
            HideAllLayout();
            LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
            my_layout_main.addView(modelCourseView);
            mControlMainActivity.onClickCourseDetails();
        });
        //我的收藏不需要显示课程协议，因为没有购买的课程也可以被收藏
        TextView modelmy_myclass1_agreement = view.findViewById(R.id.modelmy_myclass1_agreement);
        modelmy_myclass1_agreement.setVisibility(View.INVISIBLE);
        View modelmy_myclass1_line1 = view.findViewById(R.id.modelmy_myclass1_line1);
        modelmy_myclass1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的收藏界面-课程包
    private void MyCollectShow_MyCoursePacket(LinearLayout modelmy_mycollect_main_content){
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket1, null);
        modelmy_mycollect_main_content.addView(view);

        ControllerCustomRoundAngleImageView modelmy_myclasspacket1_cover = view.findViewById(R.id.modelmy_myclasspacket1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(modelmy_myclasspacket1_cover);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v->{
            //测试数据
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
            //跳转课程包详情
            ModelCoursePacketCover modelCoursePacketCover = new ModelCoursePacketCover();
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity,CoursePacketInfo1);
            modelCoursePacketCover.CoursePacketDetailsShow();
            HideAllLayout();
            LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
            my_layout_main.addView(modelCoursePacketView);
            mControlMainActivity.onClickCoursePacketDetails();
        });
        //我的收藏不需要显示课程协议，因为没有购买的课程也可以被收藏
        TextView modelmy_myclasspacket1_agreement = view.findViewById(R.id.modelmy_myclasspacket1_agreement);
        modelmy_myclasspacket1_agreement.setVisibility(View.INVISIBLE);
        //我的收藏不需要显示学习进度，因为没有购买的课程也可以被收藏
        LinearLayout modelmy_myclasspacket1_learnprogresslayout = view.findViewById(R.id.modelmy_myclasspacket1_learnprogresslayout);
        modelmy_myclasspacket1_learnprogresslayout.setVisibility(View.INVISIBLE);
        View modelmy_myclasspacket1_line1 = view.findViewById(R.id.modelmy_myclasspacket1_line1);
        modelmy_myclasspacket1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的缓存界面
    public void MyCacheShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCacheView == null) {
            mMyCacheView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycache, null);
            ModelPtrFrameLayout modelmy_mycache_main_content_frame = mMyCacheView.findViewById(R.id.modelmy_mycache_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_mycache_main_content_frame.addPtrUIHandler(header);
            modelmy_mycache_main_content_frame.setHeaderView(header);
            modelmy_mycache_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_mycache_main_content_frame.refreshComplete();
                }
            });
        }
        my_layout_main.addView(mMyCacheView);
        LinearLayout modelmy_mycache_main_content = mMyCacheView.findViewById(R.id.modelmy_mycache_main_content);
        modelmy_mycache_main_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycache1, null);
        modelmy_mycache_main_content.addView(view);

        ControllerCustomRoundAngleImageView modelmy_mycache1_cover = view.findViewById(R.id.modelmy_mycache1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                return false;
            }
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_mycache1_cover);
        //监听，如果点击此课程，查看此课程下缓存的详细信息
        LinearLayout modelmy_mycache1_classname_layout = view.findViewById(R.id.modelmy_mycache1_classname_layout);
        modelmy_mycache1_classname_layout.setOnClickListener(v->{
            MyCache_ManagementCacheShow();
        });
        View modelmy_mycache1_line1 = view.findViewById(R.id.modelmy_mycache1_line1);
        modelmy_mycache1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的缓存-管理缓存界面
    public void MyCache_ManagementCacheShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCacheManagementCacheView == null) {
            mMyCacheManagementCacheView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager, null);
        }
        my_layout_main.addView(mMyCacheManagementCacheView);
        TextView course_downloadmanager_layout_titletext = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_layout_titletext);
        course_downloadmanager_layout_titletext.setText("管理缓存");
        int count = 0;
        LinearLayout course_downloadmanager_layout_content = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_layout_content);
        course_downloadmanager_layout_content.removeAllViews();
        for (int i = 0; i < CourseInfo1.mCourseChaptersInfoList.size() ; i ++) {
            CourseChaptersInfo courseChaptersInfo = CourseInfo1.mCourseChaptersInfoList.get(i);
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
        TextView course_downloadmanager_num = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_num);
        course_downloadmanager_num.setText("0");
        TextView course_downloadmanager_sumnum = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_sumnum);
        course_downloadmanager_sumnum.setText("/" + count);
        //获取手机剩余存储空间
        TextView course_downloadmanager_availalesize = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_availalesize);
        long size = getAvailaleSize();
        course_downloadmanager_availalesize.setText("剩余空间：" + size + "G");
        ImageView course_downloadmanager_layout_return_button1 = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_layout_return_button1);
        course_downloadmanager_layout_return_button1.setOnClickListener(v->{ //管理缓存界面的返回
            MyCacheShow();
        });
        TextView course_downloadmanager_all = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_all);
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
        TextView course_downloadmanager_startall = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_startall);
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
        TextView course_downloadmanager_layout_edit = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_layout_edit);
        LinearLayout course_downloadmanager_function = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_function);
        LinearLayout course_downloadmanager_function1 = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_function1);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_downloadmanager_function.getLayoutParams();
        ll.height = mMyCacheManagementCacheView.getResources().getDimensionPixelSize(R.dimen.dp40);
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
                llp.height = mMyCacheManagementCacheView.getResources().getDimensionPixelSize(R.dimen.dp40);
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
            } else if (course_downloadmanager_layout_edit.getText().toString().equals("完成")){  //点击完成，跳转界面
                MyCache_ManagementCacheShow();
            }
        });
        //全部选择
        TextView course_downloadmanager_allselect = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_allselect);
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
        TextView course_downloadmanager_delete = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_delete);
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
                MyCache_ManagementCacheShow();
            });
        });
    }

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

    //展示我的订单界面
    public void MyOrderShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyOrderView == null) {
            mMyOrderView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder, null);
            ModelPtrFrameLayout modelmy_myorder_main_content_frame = mMyOrderView.findViewById(R.id.modelmy_myorder_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myorder_main_content_frame.addPtrUIHandler(header);
            modelmy_myorder_main_content_frame.setHeaderView(header);
            modelmy_myorder_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myorder_main_content_frame.refreshComplete();
                }
            });
            TextView modelmy_myorder_tab_all = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_all);
            modelmy_myorder_tab_all.setOnClickListener(v->{
                if (!mMyOrderCurrentTab.equals("all")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation(( mMyOrderLastTabIndex - 1)  * width / 3,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myorder_cursor1.startAnimation(animation);
                    TextView modelmy_myorder_tab_finished = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_finished);
                    TextView modelmy_myorder_tab_unfinish = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_unfinish);
                    modelmy_myorder_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_myorder_tab_finished.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_myorder_tab_unfinish.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyOrderLastTabIndex = 1;
                mMyOrderCurrentTab = "all";
                LinearLayout modelmy_myorder_main_content = mMyOrderView.findViewById(R.id.modelmy_myorder_main_content);
                modelmy_myorder_main_content.removeAllViews();
                //可循环调用以下方法，添加多个订单
                MyOrderShow_MyOrder(modelmy_myorder_main_content);
            });
            TextView modelmy_myorder_tab_finished = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_finished);
            modelmy_myorder_tab_finished.setOnClickListener(v->{
                if (!mMyOrderCurrentTab.equals("finished")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation(( mMyOrderLastTabIndex - 1)  * width / 3,width / 3 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myorder_cursor1.startAnimation(animation);
                    TextView modelmy_myorder_tab_unfinish = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_unfinish);
                    modelmy_myorder_tab_unfinish.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_myorder_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_myorder_tab_finished.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mMyOrderLastTabIndex = 2;
                mMyOrderCurrentTab = "finished";
                LinearLayout modelmy_myorder_main_content = mMyOrderView.findViewById(R.id.modelmy_myorder_main_content);
                modelmy_myorder_main_content.removeAllViews();
                //可循环调用以下方法，添加多个订单
                MyOrderShow_MyOrder(modelmy_myorder_main_content);
            });
            TextView modelmy_myorder_tab_unfinish = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_unfinish);
            modelmy_myorder_tab_unfinish.setOnClickListener(v->{
                if (!mMyOrderCurrentTab.equals("unfinish")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation(( mMyOrderLastTabIndex - 1)  * width / 3,width * 2 / 3 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myorder_cursor1.startAnimation(animation);
                    modelmy_myorder_tab_unfinish.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_myorder_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_myorder_tab_finished.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyOrderView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyOrderLastTabIndex = 3;
                mMyOrderCurrentTab = "unfinish";
                LinearLayout modelmy_myorder_main_content = mMyOrderView.findViewById(R.id.modelmy_myorder_main_content);
                modelmy_myorder_main_content.removeAllViews();
                //可循环调用以下方法，添加多个订单
                MyOrderShow_MyOrder(modelmy_myorder_main_content);
            });
        }
        ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
        int x = width / 6 - mMyOrderView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        modelmy_myorder_cursor1.setX(x);
        //默认选中的为所有
        mMyOrderLastTabIndex = 1;
        mMyOrderCurrentTab = "all";
        my_layout_main.addView(mMyOrderView);
        LinearLayout modelmy_myorder_main_content = mMyOrderView.findViewById(R.id.modelmy_myorder_main_content);
        modelmy_myorder_main_content.removeAllViews();
        //可循环调用以下方法，添加多个订单
        MyOrderShow_MyOrder(modelmy_myorder_main_content);
    }

    private void MyOrderShow_MyOrder(LinearLayout modelmy_myorder_main_content){
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        if (mMyOrderCurrentTab.equals("all")){
            TextView modelmy_myorder1_ordername = view1.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername.setText("技术精讲班1");
            TextView modelmy_myorder1_orderstate = view1.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate.setText("支付失败");
            modelmy_myorder1_orderstate.setTextColor(view1.getResources().getColor(R.color.holo_red_dark));
            TextView modelmy_myorder1_ordernumber = view1.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney = view1.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney.setText("9000");
            modelmy_myorder_main_content.addView(view1);
            modelmy_myorder_main_content.addView(view);
            TextView modelmy_myorder1_ordername1 = view2.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername1.setText("技术精讲班1");
            TextView modelmy_myorder1_orderstate1 = view2.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate1.setText("待支付");
            modelmy_myorder1_orderstate1.setTextColor(view2.getResources().getColor(R.color.text_orange1));
            TextView modelmy_myorder1_ordernumber1 = view2.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber1.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney1 = view2.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney1.setText("9000");
            modelmy_myorder_main_content.addView(view2);
        } else if (mMyOrderCurrentTab.equals("finished")){
            modelmy_myorder_main_content.addView(view);
        } else if (mMyOrderCurrentTab.equals("unfinish")){
            TextView modelmy_myorder1_ordername = view1.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername.setText("技术精讲班1");
            TextView modelmy_myorder1_orderstate = view1.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate.setText("支付失败");
            modelmy_myorder1_orderstate.setTextColor(view1.getResources().getColor(R.color.holo_red_dark));
            TextView modelmy_myorder1_ordernumber = view1.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney = view1.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney.setText("9000");
            modelmy_myorder_main_content.addView(view1);
        }
        view.setOnClickListener(v->{
            MyOrderShow_OrderDetails("success");
        });
        view1.setOnClickListener(v->{
            MyOrderShow_OrderDetails("fail");
        });
        RelativeLayout modelmy_myorder1_orderfunction = view.findViewById(R.id.modelmy_myorder1_orderfunction);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) modelmy_myorder1_orderfunction.getLayoutParams();
        rl.height = 0;
        modelmy_myorder1_orderfunction.setLayoutParams(rl);

        modelmy_myorder1_orderfunction = view1.findViewById(R.id.modelmy_myorder1_orderfunction);
        rl = (RelativeLayout.LayoutParams) modelmy_myorder1_orderfunction.getLayoutParams();
        rl.height = 0;
        modelmy_myorder1_orderfunction.setLayoutParams(rl);

        ImageView modelmy_myorder1_cancel = view2.findViewById(R.id.modelmy_myorder1_cancel);
        ImageView modelmy_myorder1_retrypay = view2.findViewById(R.id.modelmy_myorder1_retrypay);
        modelmy_myorder1_cancel.setOnClickListener(v->{
            //取消支付，将此订单设置为失效
        });
        modelmy_myorder1_retrypay.setOnClickListener(v->{
            //重新支付，进入支付界面
        });
    }

    private void MyOrderShow_OrderDetails(String state){
        if (mview == null){
            return;
        }
        mControlMainActivity.onClickMyOrderDetails();
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyOrderDetailsView == null) {
            mMyOrderDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorderdetails, null);
            ModelPtrFrameLayout modelmy_myorderdetails_main_content_frame = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myorderdetails_main_content_frame.addPtrUIHandler(header);
            modelmy_myorderdetails_main_content_frame.setHeaderView(header);
            modelmy_myorderdetails_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myorderdetails_main_content_frame.refreshComplete();
                }
            });
            //点击复制订单号
            ImageView modelmy_myorderdetails_ordernumbercopy = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_ordernumbercopy);
            modelmy_myorderdetails_ordernumbercopy.setOnClickListener(v->{
                TextView modelmy_myorderdetails_ordernumber = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_ordernumber);
                String modelmy_myorderdetails_ordernumbertext = modelmy_myorderdetails_ordernumber.getText().toString();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mControlMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", modelmy_myorderdetails_ordernumbertext);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(mControlMainActivity,"已将订单号复制到剪贴板",Toast.LENGTH_SHORT).show();
            });
        }
        my_layout_main.addView(mMyOrderDetailsView);
        ImageView modelmy_myorderdetails_icon = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_icon);
        ImageView modelmy_myorderdetails_background_fail = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_background_fail);
        TextView modelmy_myorderdetails_invalid = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_invalid);
        ImageView modelmy_myorderdetails_invalidicon = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_invalidicon);
        if (state.equals("success")){ //成功
            modelmy_myorderdetails_invalid.setText("支付成功");
            modelmy_myorderdetails_background_fail.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_background_success));
            modelmy_myorderdetails_icon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_success));
            modelmy_myorderdetails_invalidicon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_paysuccess));
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) modelmy_myorderdetails_invalidicon.getLayoutParams();
            rl.width = mMyOrderDetailsView.getResources().getDimensionPixelSize(R.dimen.dp_77);
            modelmy_myorderdetails_invalidicon.setLayoutParams(rl);
        } else { //反之失败
            modelmy_myorderdetails_invalid.setText("订单已失效");
            modelmy_myorderdetails_background_fail.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_background_fail));
            modelmy_myorderdetails_icon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_fail));
            modelmy_myorderdetails_invalidicon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_invalid));
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) modelmy_myorderdetails_invalidicon.getLayoutParams();
            rl.width = mMyOrderDetailsView.getResources().getDimensionPixelSize(R.dimen.dp_58);
            modelmy_myorderdetails_invalidicon.setLayoutParams(rl);
        }
        //文字栅格化
        TextView modelmy_myorderdetails_coursediscountprice = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_coursediscountprice);
        modelmy_myorderdetails_coursediscountprice.setPaintFlags(modelmy_myorderdetails_coursediscountprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    //展示我的优惠券界面
    public void MyCouponShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCouponView == null) {
            mMyCouponView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon, null);
            ModelPtrFrameLayout modelmy_mycoupon_main_content_frame = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_mycoupon_main_content_frame.addPtrUIHandler(header);
            modelmy_mycoupon_main_content_frame.setHeaderView(header);
            modelmy_mycoupon_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_mycoupon_main_content_frame.refreshComplete();
                }
            });
            TextView modelmy_mycoupon_tab_notused = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_notused);
            modelmy_mycoupon_tab_notused.setOnClickListener(v->{
                if (!mMyCouponCurrentTab.equals("notused")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation(( mMyCouponLastTabIndex - 1)  * width / 3,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycoupon_cursor1.startAnimation(animation);
                    TextView modelmy_mycoupon_tab_alreadyused = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_alreadyused);
                    TextView modelmy_mycoupon_tab_expired = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_expired);
                    modelmy_mycoupon_tab_notused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_mycoupon_tab_alreadyused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_expired.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyCouponLastTabIndex = 1;
                mMyCouponCurrentTab = "notused";
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            TextView modelmy_mycoupon_tab_alreadyused = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_alreadyused);
            modelmy_mycoupon_tab_alreadyused.setOnClickListener(v->{
                if (!mMyCouponCurrentTab.equals("alreadyused")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation(( mMyCouponLastTabIndex - 1)  * width / 3,width / 3 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycoupon_cursor1.startAnimation(animation);
                    TextView modelmy_mycoupon_tab_expired = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_expired);
                    modelmy_mycoupon_tab_expired.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_notused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_alreadyused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mMyCouponLastTabIndex = 2;
                mMyCouponCurrentTab = "alreadyused";
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            TextView modelmy_mycoupon_tab_expired = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_expired);
            modelmy_mycoupon_tab_expired.setOnClickListener(v->{
                if (!mMyOrderCurrentTab.equals("expired")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation(( mMyCouponLastTabIndex - 1)  * width / 3,width * 2 / 3 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycoupon_cursor1.startAnimation(animation);
                    modelmy_mycoupon_tab_expired.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_mycoupon_tab_notused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_alreadyused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyCouponLastTabIndex = 3;
                mMyCouponCurrentTab = "expired";
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            //点击兑换
            TextView modelmy_mycoupon_main_exchange = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_exchange);
            modelmy_mycoupon_main_exchange.setOnClickListener(v->{
                //点击兑换弹出兑换对话框
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel1, null);
                mMyCouponDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
                mMyCouponDialog.setCancelable(true);
                mMyCouponDialog.show();
                TextView button_cancel = view.findViewById(R.id.button_cancel);
                button_cancel.setOnClickListener(View->{
                    mMyCouponDialog.cancel();
                });
                TextView button_sure = view.findViewById(R.id.button_sure);
                button_sure.setOnClickListener(View->{
                    //开始兑换优惠码
                    mMyCouponDialog.cancel();
                });
            });
        }
        ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
        int x = width / 6 - mMyCouponView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        modelmy_mycoupon_cursor1.setX(x);
        //默认选中的为所有
        mMyCouponLastTabIndex = 1;
        mMyCouponCurrentTab = "notused";
        my_layout_main.addView(mMyCouponView);
        LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
        modelmy_mycoupon_main_content.removeAllViews();
        //可循环调用以下方法，添加多个优惠券
        MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
    }

    private void MyCouponShow_MyCoupon(LinearLayout modelmy_myorder_main_content){
        if (mMyCouponCurrentTab.equals("notused")){
            {
                //添加满减卷
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view);
            }
            {
                //添加打折卷
                View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view1);
                TextView modelmy_mycoupon1_couponrequire = view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponpriceicon = view1.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
                ll.width = 0;
                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view1.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
                TextView modelmy_mycoupon1_couponfullreduction = view1.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view1.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view1.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
            }
            {
                //抵现卷
                View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view2);
                TextView modelmy_mycoupon1_couponrequire = view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("600");
                TextView modelmy_mycoupon1_couponfullreduction = view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
            }
        } else if (mMyCouponCurrentTab.equals("alreadyused")){
            {
                //添加满减卷
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view);
                TextView modelmy_mycoupon1_couponpriceicon = view.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponprice = view.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponrequire = view.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                modelmy_mycoupon1_couponrequire.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
            }
            {
                //添加打折卷
                View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view1);
                TextView modelmy_mycoupon1_couponrequire = view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponpriceicon = view1.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
                ll.width = 0;
                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view1.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
                TextView modelmy_mycoupon1_couponfullreduction = view1.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view1.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view1.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");

                modelmy_mycoupon1_couponpriceicon.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponprice.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponrequire.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view1.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view1.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
            }
            {
                //抵现卷
                View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view2);
                TextView modelmy_mycoupon1_couponrequire = view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("600");
                TextView modelmy_mycoupon1_couponfullreduction = view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");

                TextView modelmy_mycoupon1_couponpriceicon = view2.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponprice.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponrequire.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view2.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view2.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
            }
        } else if (mMyCouponCurrentTab.equals("expired")){
            {
                //添加满减卷
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view);
                TextView modelmy_mycoupon1_couponpriceicon = view.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponprice = view.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponrequire = view.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                modelmy_mycoupon1_couponrequire.setTextColor(view.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
            }
            {
                //添加打折卷
                View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view1);
                TextView modelmy_mycoupon1_couponrequire = view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponpriceicon = view1.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
                ll.width = 0;
                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view1.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
                TextView modelmy_mycoupon1_couponfullreduction = view1.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view1.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view1.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");

                modelmy_mycoupon1_couponpriceicon.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponprice.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponrequire.setTextColor(view1.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view1.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view1.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
            }
            {
                //抵现卷
                View view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(view2);
                TextView modelmy_mycoupon1_couponrequire = view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
                ll.height = 0;
                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
                TextView modelmy_mycoupon1_couponprice = view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setText("600");
                TextView modelmy_mycoupon1_couponfullreduction = view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
                TextView modelmy_mycoupon1_termofvaliditydata = view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
                TextView modelmy_mycoupon1_areaofapplication = view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");

                TextView modelmy_mycoupon1_couponpriceicon = view2.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponprice.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                modelmy_mycoupon1_couponrequire.setTextColor(view2.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = view2.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(view2.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
            }
        }
    }

    //展示我的消息界面
    public void MyMessageShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyMessageView == null) {
            mMyMessageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mymessage, null);
            ModelPtrFrameLayout modelmy_mymessage_content_frame = mMyMessageView.findViewById(R.id.modelmy_mymessage_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_mymessage_content_frame.addPtrUIHandler(header);
            modelmy_mymessage_content_frame.setHeaderView(header);
            modelmy_mymessage_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_mymessage_content_frame.refreshComplete();
                }
            });
            ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
            //点击全部已读
            modelmy_mymessage_clear.setOnClickListener(v->{
                boolean m_isFind = false;
                TextView modelmy_mymessage_noticemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_noticemessagecount);
                TextView modelmy_mymessage_advertisemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_advertisemessagecount);
                if (modelmy_mymessage_noticemessagecount.getVisibility() == View.VISIBLE || modelmy_mymessage_advertisemessagecount.getVisibility() == View.VISIBLE) {  //有未读消息
                    m_isFind = true;
                } else {
                    ControllerListViewForScrollView listView = mMyMessageView0.findViewById(R.id.modelmy_mymessage_main_contentlistview);
                    int childCount = listView.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View view = listView.getChildAt(i);
                        if (view == null) {
                            continue;
                        }
                        TextView modelmy_mymessage1_messagecount = view.findViewById(R.id.modelmy_mymessage1_messagecount);
                        if (modelmy_mymessage1_messagecount == null) {
                            continue;
                        }
                        if (modelmy_mymessage1_messagecount.getVisibility() == View.VISIBLE) {  //有未读消息
                            m_isFind = true;
                            break;
                        }
                    }
                }
                //如果没有未读消息弹出toast提示
                if (!m_isFind){
                    Toast.makeText(mControlMainActivity,"暂无未读消息",Toast.LENGTH_LONG).show();
                } else {
                    //弹出提示，是否将未读消息置为全部已读
                    View dialogView = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_notip_sure_cancel, null);
                    mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, dialogView, R.style.DialogTheme);
                    mMyDialog.setCancelable(true);
                    mMyDialog.show();
                    TextView dialog_content = dialogView.findViewById(R.id.dialog_content);
                    dialog_content.setText("是否将未读消息置为全部已读？");
                    TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
                    button_cancel.setText("取消");
                    button_cancel.setOnClickListener(View -> {
                        mMyDialog.cancel();
                    });
                    TextView button_sure = dialogView.findViewById(R.id.button_sure);
                    button_sure.setText("确定");
                    button_sure.setOnClickListener(View -> {
                        //全部已读
                        modelmy_mymessage_noticemessagecount.setVisibility(android.view.View.INVISIBLE);
                        modelmy_mymessage_advertisemessagecount.setVisibility(android.view.View.INVISIBLE);
                        ControllerListViewForScrollView listView = mMyMessageView0.findViewById(R.id.modelmy_mymessage_main_contentlistview);
                        int childCount = listView.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View view = listView.getChildAt(i);
                            if (view == null) {
                                continue;
                            }
                            TextView modelmy_mymessage1_messagecount = view.findViewById(R.id.modelmy_mymessage1_messagecount);
                            if (modelmy_mymessage1_messagecount == null) {
                                continue;
                            }
                            modelmy_mymessage1_messagecount.setVisibility(android.view.View.INVISIBLE);
                        }
                        mMyDialog.cancel();
                    });
                }
            });
        }
        ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
        modelmy_mymessage_clear.setVisibility(View.VISIBLE);
        my_layout_main.addView(mMyMessageView);
        LinearLayout modelmy_mymessage_main_content = mMyMessageView.findViewById(R.id.modelmy_mymessage_main_content);
        modelmy_mymessage_main_content.removeAllViews();
        if (mMyMessageView0 == null) {
            mMyMessageView0 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mymessage0, null);
        }
        RelativeLayout modelmy_mymessage_notice = mMyMessageView0.findViewById(R.id.modelmy_mymessage_notice);
        modelmy_mymessage_notice.setOnClickListener(v->{
            MyMessageShow_MessageDatails(modelmy_mymessage_main_content,"notice");
        });
        RelativeLayout modelmy_mymessage_advertise = mMyMessageView0.findViewById(R.id.modelmy_mymessage_advertise);
        modelmy_mymessage_advertise.setOnClickListener(v->{
            MyMessageShow_MessageDatails(modelmy_mymessage_main_content,"advertise");
        });
        modelmy_mymessage_main_content.addView(mMyMessageView0);
        ControllerListViewForScrollView listView = mMyMessageView0.findViewById(R.id.modelmy_mymessage_main_contentlistview);
        List<ControllerMyMessage1Adapter.MyMessageInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ControllerMyMessage1Adapter.MyMessageInfo message = new ControllerMyMessage1Adapter.MyMessageInfo();
            message.modelmy_mymessage1_coverurl = "";
            message.modelmy_mymessage1_name = i + "回复您的问题";
            message.modelmy_mymessage1_messagecount = i + "";
            message.modelmy_mymessage1_message = "你的问题都很有趣";
            message.modelmy_mymessage1_time = "1-12 22:11";
            list.add(message);
        }
        adapter = new ControllerMyMessage1Adapter(mControlMainActivity, list);
        listView.setAdapter(adapter);
    }

    //展示我的消息-系统消息界面
    public void MyMessageShow_MessageDatails(LinearLayout modelmy_mymessage_main_content,String type){
        mControlMainActivity.onClickMyMessageDetails();
        if (mview == null || mMyMessageView == null){
            return;
        }
        ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
        modelmy_mymessage_clear.setVisibility(View.INVISIBLE);
        modelmy_mymessage_main_content.removeAllViews();
        if (type.equals("notice")){
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                modelmy_mymessage_main_content.addView(view);
            }
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                modelmy_mymessage_main_content.addView(view);
            }
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                modelmy_mymessage_main_content.addView(view);
                //如果是已读，将文字颜色置为灰色
                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
            }
        } else {
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                //切换广告logo
                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
                modelmy_mymessage2_cover.setBackground(view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
                modelmy_mymessage_main_content.addView(view);
            }
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                //切换广告logo
                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
                modelmy_mymessage2_cover.setBackground(view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
                modelmy_mymessage_main_content.addView(view);
                //如果是已读，将文字颜色置为灰色
                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
            }
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                //切换广告logo
                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
                modelmy_mymessage2_cover.setBackground(view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
                modelmy_mymessage_main_content.addView(view);
                //如果是已读，将文字颜色置为灰色
                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
            }
        }
    }

    //展示我的问答界面
    public void MyAnswerShow(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyAnswerView == null) {
            mMyAnswerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myanswer, null);
            ModelPtrFrameLayout modelmy_myanswer_main_content_frame = mMyAnswerView.findViewById(R.id.modelmy_myanswer_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myanswer_main_content_frame.addPtrUIHandler(header);
            modelmy_myanswer_main_content_frame.setHeaderView(header);
            modelmy_myanswer_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myanswer_main_content_frame.refreshComplete();
                }
            });
            TextView modelmy_myanswer_tab_question = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_question);
            modelmy_myanswer_tab_question.setOnClickListener(v->{
                if (!mMyAnswerCurrentTab.equals("question")) {
                    ImageView modelmy_myanswer_cursor1 = mMyAnswerView.findViewById(R.id.modelmy_myanswer_cursor1);
                    Animation animation = new TranslateAnimation(( mMyAnswerLastTabIndex - 1)  * width / 2,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myanswer_cursor1.startAnimation(animation);
                    TextView modelmy_myanswer_tab_answer = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_answer);
                    modelmy_myanswer_tab_question.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_myanswer_tab_answer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyAnswerLastTabIndex = 1;
                mMyAnswerCurrentTab = "question";
                LinearLayout modelmy_myanswer_main_content = mMyAnswerView.findViewById(R.id.modelmy_myanswer_main_content);
                modelmy_myanswer_main_content.removeAllViews();
                //测试数据
                for (int i = 0; i < 5 ; i ++) {
                    View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
                    TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
                    modelmy_myanswer1_title.setText("技术基础实务" +i);
                    TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
                    modelmy_myanswer1_content.setText("消防器材的正确使用方式是什么啊");
                    TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
                    modelmy_myanswer1_time.setText("5分钟前");
                    modelmy_myanswer_main_content.addView(view);
                    view.setOnClickListener(V->{
                        MyAnswerShow_Details();
                    });
                }
            });
            TextView modelmy_myanswer_tab_answer = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_answer);
            modelmy_myanswer_tab_answer.setOnClickListener(v->{
                if (!mMyAnswerCurrentTab.equals("answer")) {
                    ImageView modelmy_myanswer_cursor1 = mMyAnswerView.findViewById(R.id.modelmy_myanswer_cursor1);
                    Animation animation = new TranslateAnimation(( mMyAnswerLastTabIndex - 1)  * width / 2,width / 2 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myanswer_cursor1.startAnimation(animation);
                    modelmy_myanswer_tab_question.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_myanswer_tab_answer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mMyAnswerLastTabIndex = 2;
                mMyAnswerCurrentTab = "answer";
                LinearLayout modelmy_myanswer_main_content = mMyAnswerView.findViewById(R.id.modelmy_myanswer_main_content);
                modelmy_myanswer_main_content.removeAllViews();
                //测试数据
                for (int i = 0; i < 3 ; i ++) {
                    View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
                    TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
                    modelmy_myanswer1_title.setText("技术基础实务" +i);
                    TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
                    modelmy_myanswer1_content.setText("消防器材的正确使用方式是什么啊");
                    TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
                    modelmy_myanswer1_time.setText("5分钟前");
                    modelmy_myanswer_main_content.addView(view);
                    view.setOnClickListener(V->{
                        MyAnswerShow_Details();
                    });
                }
            });
        }
        my_layout_main.addView(mMyAnswerView);
        ImageView modelmy_myanswer_cursor1 = mMyAnswerView.findViewById(R.id.modelmy_myanswer_cursor1);
        int x = width / 4 - mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        modelmy_myanswer_cursor1.setX(x);
        //默认选中的为我的提问
        mMyAnswerLastTabIndex = 1;
        mMyAnswerCurrentTab = "question";
        TextView modelmy_myanswer_tab_question = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_question);
        TextView modelmy_myanswer_tab_answer = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_answer);
        modelmy_myanswer_tab_question.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        modelmy_myanswer_tab_answer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        //移除标签下的所有内容，重新加载
        LinearLayout modelmy_myanswer_main_content = mMyAnswerView.findViewById(R.id.modelmy_myanswer_main_content);
        modelmy_myanswer_main_content.removeAllViews();
        //测试数据
        for (int i = 0; i < 5 ; i ++) {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
            TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
            modelmy_myanswer1_title.setText("技术基础实务" +i);
            TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
            modelmy_myanswer1_content.setText("消防器材的正确使用方式是什么啊");
            TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
            modelmy_myanswer1_time.setText("5分钟前");
            modelmy_myanswer_main_content.addView(view);
            view.setOnClickListener(V->{
                MyAnswerShow_Details();
            });
        }
    }

    private void MyAnswerShow_Details(){
        if (mview == null){
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyAnswerDetailsView == null){
            mMyAnswerDetailsView = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswerdetails, null);
            ModelPtrFrameLayout modelmy_myanswerdetails_main_content_frame = mMyAnswerDetailsView.findViewById(R.id.modelmy_myanswerdetails_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            modelmy_myanswerdetails_main_content_frame.addPtrUIHandler(header);
            modelmy_myanswerdetails_main_content_frame.setHeaderView(header);
            modelmy_myanswerdetails_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    modelmy_myanswerdetails_main_content_frame.refreshComplete();
                }
            });
            ImageView modelmy_myanswerdetails_delete = mMyAnswerDetailsView.findViewById(R.id.modelmy_myanswerdetails_delete);
            modelmy_myanswerdetails_delete.setOnClickListener(v->{
                //点击弹出删除问答提示框
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel, null);
                mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
                mMyDialog.setCancelable(true);
                mMyDialog.show();
                TextView tip = view.findViewById(R.id.tip);
                tip.setText("删除问答");
                TextView dialog_content = view.findViewById(R.id.dialog_content);
                dialog_content.setText("确定删除此条问答吗？");
                TextView button_cancel = view.findViewById(R.id.button_cancel);
                button_cancel.setText("取消");
                button_cancel.setOnClickListener(View->{
                    mMyDialog.cancel();
                });
                TextView button_sure = view.findViewById(R.id.button_sure);
                button_sure.setText("确定");
                button_sure.setOnClickListener(View->{
                    mMyDialog.cancel();
                    mControlMainActivity.onClickMyAnswerReturn(View);
                });
            });
        }
        my_layout_main.addView(mMyAnswerDetailsView);
        LinearLayout modelmy_myanswerdetails_main_content = mMyAnswerDetailsView.findViewById(R.id.modelmy_myanswerdetails_main_content);
        modelmy_myanswerdetails_main_content.removeAllViews();
        View answerdetailsview = mControlMainActivity.getLayoutInflater().inflate(R.layout.modelanswerdetails, null);
        modelmy_myanswerdetails_main_content.addView(answerdetailsview);
        LinearLayout answerdetails_content = answerdetailsview.findViewById(R.id.answerdetails_content);
        View line = null;
        //测试数据
        for (int i = 0; i < 3; i ++){
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.modelanswerdetails_child, null);
            TextView answerdetails_child_name = view.findViewById(R.id.answerdetails_child_name);
            answerdetails_child_name.setText("学员" + i);
            answerdetails_content.addView(view);
            line = view.findViewById(R.id.answerdetails_child_line);
        }
        if (line != null){
            line.setVisibility(View.INVISIBLE);
        }
        mControlMainActivity.Page_AnswerDetails();
    }
}
