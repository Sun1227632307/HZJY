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
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelMy extends Fragment {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext = "xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview, mMyView, mMyClassView, mMyClassPacketView, mMyCollectView, mMyCacheView, mMyCacheManagementCacheView, mMyOrderView, mMyOrderDetailsView, mMyCouponView, mMyMessageView, mMyMessageView0, mMyAnswerView, mMyAnswerDetailsView;
    private int width = 720;
    private int mMyCollectLastTabIndex = 1;
    private String mMyCollectCurrentTab = "course";
    private int mMyOrderLastTabIndex = 1;
    private String mMyOrderCurrentTab = "all";
    private int mMyCouponLastTabIndex = 1;
    private String mMyCouponCurrentTab = "notused";
    private int mMyAnswerLastTabIndex = 1;
    private String mMyAnswerCurrentTab = "course";
    private ControllerCenterDialog mMyDialog, mMyCouponDialog;
    private ControllerMyMessage1Adapter adapter;
    private static final String TAG = "ModelMy";
    boolean m_isFind = false;  //判断当前已读和未读的状态
    private int page=0;
    private int pagesize=0;
    private int Collection_page=0;
    private int Collection_pagesize=0;
    //我的消息的集合
    List<ControllerMyMessage1Adapter.MyMessageInfo> list = new ArrayList<>();
    //个人信息返回数据
    private PersonalInfoBean.PersonalInfoDataBean mPersonalInfoDataBean;
    //测试数据
    private CourseInfo CourseInfo1 = new CourseInfo();
    private Map<String, CourseRecordPlayDownloadInfo> mCourseRecordPlayDownloadInfoMap = new HashMap<>();
    private SmartRefreshLayout mSmart_model_my_myclass;
    private View view;   //我的课程view
    private View MyOrderShow_MyOrder_view1;
    private View MyOrderShow_MyOrder_view2;
    private View MyOrderShow_MyOrder_view3;
    private View view3;
    private View view4;
    private SmartRefreshLayout mSmart_model_my_myclasspacket;
    private int MyCoupon_pageNum=1;
    private int MyCoupon_pageSize=1;
    private String pageSize_type="";
    private SmartRefreshLayout mSmart_model_my_myorder;
//    private String product_name;
//    private String order_status;
//    private String order_num;
//    private int product_price;
    private MyOrderlistBean.DataBean.ListBean mMyOrderListBean;
    private SmartRefreshLayout mSmart_model_my_mycollect;
    private SmartRefreshLayout mSmart_model_my_myorderdetails;
    private ModelOrderDetailsInterface ModelOrderDetailsInterface;
    private SmartRefreshLayout mSmart_model_my_mymessage;
    private SmartRefreshLayout mSmart_model_my_myanswer;
    private LinearLayout modelmy_myanswer_main_content;
    private SmartRefreshLayout mSmart_model_my_myanswerdetails;
    private SmartRefreshLayout mSmart_model_my_mycache;
    private View myclass_view;
    private View MyOrderShow_MyOrder_view4;
    private View my_mymessage2_view;
    private SmartRefreshLayout mSmart_model_my_mycoupon;
    private View lessenfull_view1;
    private View withstandcoupon_view2;
    private View discountcoupon_view3;


    public static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage) {
        mContext = context;
        mControlMainActivity = content;
        ModelMy myFragment = new ModelMy();
        FragmentPage = iFragmentPage;
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage, container, false);
        //测试数据
        CourseInfo1.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
        CourseInfo1.mCourseLearnPersonNum = "1000";
        CourseInfo1.mCourseName = "开学典礼";
        CourseInfo1.mCoursePrice = "免费";
        CourseInfo1.mCourseType = "直播";
        CourseInfo1.mCourseIsHave = "1";
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
        for (int i = 0; i < CourseInfo1.mCourseChaptersInfoList.size(); i++) {
            CourseChaptersInfo courseChaptersInfo = CourseInfo1.mCourseChaptersInfoList.get(i);
            if (courseChaptersInfo == null) {
                continue;
            }
            for (int num = 0; num < courseChaptersInfo.mCourseSectionsInfoList.size(); num++) {
                CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(num);
                if (courseSectionsInfo == null) {
                    continue;
                }
                //___________________________________________________________________测试进度
                if (count == 0) {
                    CourseRecordPlayDownloadInfo courseRecordPlayDownloadInfo = new CourseRecordPlayDownloadInfo();
                    courseRecordPlayDownloadInfo.mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;
                    courseRecordPlayDownloadInfo.mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;
                    courseRecordPlayDownloadInfo.mCourseSectionsId = courseSectionsInfo.mCourseSectionsId;
                    courseRecordPlayDownloadInfo.mCourseSectionsName = courseSectionsInfo.mCourseSectionsName;
                    courseRecordPlayDownloadInfo.mCourseSectionsDownloadSize = "2048";
                    courseRecordPlayDownloadInfo.mCourseSectionsSize = courseSectionsInfo.mCourseSectionsSize;
                    if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo);
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
                    if (mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId) != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mCourseRecordPlayDownloadInfoMap.replace(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo);
                        }
                    } else {
                        mCourseRecordPlayDownloadInfoMap.put(courseSectionsInfo.mCourseSectionsId, courseRecordPlayDownloadInfo);
                    }
                }
                count++;
            }
        }
       
        getPersonalInfoDatas(); // 查询个人信息详情（我的界面）
         ModelMyInit();    //加载用户头像

      
        return mview;
    }

    //隐藏所有图层
    private void HideAllLayout() {
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        my_layout_main.removeAllViews();
    }

    public void ModelMyInit() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyView == null) {
            mMyView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.my_layout_main, null);
            TextView username = mMyView.findViewById(R.id.username);
            username.setOnClickListener(v -> {
                if (mPersonalInfoDataBean == null) {
                    mControlMainActivity.onClickImmediatelyLogin("login");
                } else {
                    mControlMainActivity.onClickImmediatelyLogin("personinfo");
                }
            });
        }
        my_layout_main.addView(mMyView);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        width = dm.widthPixels;
        //加载用户头像
        ControllerCustomRoundAngleImageView headportraitImageView = mMyView.findViewById(R.id.headportrait);
        if (mPersonalInfoDataBean == null) {
            Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                    return false;
                }

                @Override
                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                    return false;
                }
            }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelmy_myheaddefault)).into(headportraitImageView);
            TextView username = mMyView.findViewById(R.id.username);
            username.setText(mMyView.getResources().getString(R.string.title_loginimmediately));
            TextView userinfo = mMyView.findViewById(R.id.userinfo);
            userinfo.setText(mMyView.getResources().getString(R.string.title_personalstatement));
        } else {
            if (mPersonalInfoDataBean.stu_name != null) {  //显示用户昵称
                TextView username = mMyView.findViewById(R.id.username);
                username.setText(mPersonalInfoDataBean.stu_name);
            }
            if (mPersonalInfoDataBean.autograph != null) {  //显示用户个性签名
                TextView userinfo = mMyView.findViewById(R.id.userinfo);
                userinfo.setText(mPersonalInfoDataBean.autograph);
            }
            Glide.with(mControlMainActivity).load(mPersonalInfoDataBean.head).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                    return false;
                }

                @Override
                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                    return false;
                }
            }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelmy_myheaddefault)).into(headportraitImageView);
        }
    }

    //展示我的课程界面
    public void MyClassShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        //创建布局添加刷新控件
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyClassView == null) {
            mMyClassView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass, null);
            //Smart_model_my_myclass     布局刷新控件

            mSmart_model_my_myclass = mMyClassView.findViewById(R.id.Smart_model_my_myclass);
            mSmart_model_my_myclass.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    //数据的加载
                    mSmart_model_my_myclass.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    //页面的刷新
                    mSmart_model_my_myclass.finishRefresh();
                }
            });
        }
        my_layout_main.addView(mMyClassView);
        //我的课程界面  
        LinearLayout modelmy_myclass_main_content = mMyClassView.findViewById(R.id.modelmy_myclass_main_content);
        TextView modelmy_myclass_main_titletext = mMyClassView.findViewById(R.id.modelmy_myclass_main_titletext);
        modelmy_myclass_main_titletext.setText("我的课程");
        modelmy_myclass_main_content.removeAllViews();
        view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass1, null);
        //加载我的课程数据
        //getMyCourseList();
        modelmy_myclass_main_content.addView(view);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v -> {
            //测试数据
            CourseInfo CourseInfo1 = new CourseInfo();
            CourseInfo1.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
            CourseInfo1.mCourseLearnPersonNum = "1000";
            CourseInfo1.mCourseName = "开学典礼";
            CourseInfo1.mCoursePrice = "免费";
            CourseInfo1.mCourseType = "直播";
            CourseInfo1.mCourseIsHave = "1";
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
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity, CourseInfo1);
            modelCourseCover.CourseDetailsShow();
            HideAllLayout();
            my_layout_main.addView(modelCourseView);
            mControlMainActivity.onClickCourseDetails();
        });
        //我的课程协议
        TextView modelmy_myclass1_agreement = view.findViewById(R.id.modelmy_myclass1_agreement);
        modelmy_myclass1_agreement.setOnClickListener(v -> { //点击查看协议
            HideAllLayout();
            mControlMainActivity.onClickMyAgreement();
            myclass_view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_agreement, null);
            //我的课程网络请求
           // getModelMyMeent();
            TextView tv1 = myclass_view.findViewById(R.id.model_agreement_content);
            tv1.setText("我是协议内容");
            my_layout_main.addView(myclass_view);
        });
        //view线
        View modelmy_myclass1_line1 = view.findViewById(R.id.modelmy_myclass1_line1);
        modelmy_myclass1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的课程包界面
    public void MyClassPacketShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyClassPacketView == null) {
            mMyClassPacketView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket, null);
            //Smart_model_my_myclasspacket  刷新
            mSmart_model_my_myclasspacket = mMyClassPacketView.findViewById(R.id.Smart_model_my_myclasspacket);
            mSmart_model_my_myclasspacket.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myclasspacket.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myclasspacket.finishRefresh();
                }
            });
        }
        my_layout_main.addView(mMyClassPacketView);
        //课程包列表布局
        LinearLayout modelmy_myclasspacket_main_content = mMyClassPacketView.findViewById(R.id.modelmy_myclasspacket_main_content);
        modelmy_myclasspacket_main_content.removeAllViews();
        //子条目的布局
        MyOrderShow_MyOrder_view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket1, null);
        modelmy_myclasspacket_main_content.addView(MyOrderShow_MyOrder_view2);
        //网络请求并且赋值
       // getMyPacketList();
        //点击查看协议
        TextView classPacket_agreement = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_agreement);
        classPacket_agreement.setOnClickListener(v -> {
            HideAllLayout();
            mControlMainActivity.onClickMyAgreement();
            myclass_view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_agreement, null);
            //getModelMyMeent();   协议的网络请求
            TextView tv1 = myclass_view.findViewById(R.id.model_agreement_content);
            tv1.setText("我是协议内容");
            my_layout_main.addView(myclass_view);
            Toast.makeText(mControlMainActivity, "我是协议", Toast.LENGTH_SHORT).show();
        });
        //添加每个课程的监听，点击跳转到课程详情
        MyOrderShow_MyOrder_view2.setOnClickListener(v -> {
            //测试数据
            CoursePacketInfo CoursePacketInfo1 = new CoursePacketInfo();
            CoursePacketInfo1.mCoursePacketCover = "http://image.yunduoketang.com/course/34270/20190829/394d6b0a-6c3a-4121-b12c-ef53afff866d.png";
            CoursePacketInfo1.mCoursePacketLearnPersonNum = "1000";
            CoursePacketInfo1.mCoursePacketName = "皇家内训班";
            CoursePacketInfo1.mCoursePacketPrice = "免费";
            CoursePacketInfo1.mCoursePacketPriceOld = "1000";
            CoursePacketInfo1.mCoursePacketStageNum = "3";
            CoursePacketInfo1.mCoursePacketCourseNum = "6";
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
            CourseInfo1.mCoursePriceOld = "5000.00";
//        CourseInfo1.mCourseOrder = "1";
            StageCourseInfo1.mCourseInfoList.add(CourseInfo1);

            CourseInfo CourseInfo2 = new CourseInfo();
            CourseInfo2.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
            CourseInfo2.mCourseLearnPersonNum = "1000";
            CourseInfo2.mCourseName = "开学典礼";
            CourseInfo2.mCoursePrice = "免费";
            CourseInfo2.mCourseType = "直播";
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
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity, CoursePacketInfo1);
            modelCoursePacketCover.CoursePacketDetailsShow();
            HideAllLayout();
            my_layout_main.addView(modelCoursePacketView);
            mControlMainActivity.onClickCoursePacketDetails();
        });
       //view线
        View modelmy_myclasspacket1_line1 = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_line1);
        modelmy_myclasspacket1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的收藏界面
    public void MyCollectShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCollectView == null) {
            mMyCollectView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycollect, null);
            //Smart_model_my_mycollect
            mSmart_model_my_mycollect = mMyCollectView.findViewById(R.id.Smart_model_my_mycollect);
            mSmart_model_my_mycollect.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    Collection_page++;
                    Collection_pagesize++;
                    //加载网络数据  刷新页面
                     //  MyCollectShow();
                    mSmart_model_my_mycollect.finishLoadMore();
                }
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_mycollect.finishRefresh();
                }
            });
          //我的课程控件  
            TextView modelmy_mycollect_tab_course = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_course);
            modelmy_mycollect_tab_course.setOnClickListener(v -> {
                if (!mMyCollectCurrentTab.equals("course")) {
                    ImageView modelmy_mycollect_cursor1 = mMyCollectView.findViewById(R.id.modelmy_mycollect_cursor1);
                    Animation animation = new TranslateAnimation((mMyCollectLastTabIndex - 1) * width / 2, 0, 0, 0);
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
            //课程包
            TextView modelmy_mycollect_tab_coursepacket = mMyCollectView.findViewById(R.id.modelmy_mycollect_tab_coursepacket);
            modelmy_mycollect_tab_coursepacket.setOnClickListener(v -> {
                if (!mMyCollectCurrentTab.equals("coursepacket")) {
                    ImageView modelmy_mycollect_cursor1 = mMyCollectView.findViewById(R.id.modelmy_mycollect_cursor1);
                    Animation animation = new TranslateAnimation((mMyCollectLastTabIndex - 1) * width / 2, width / 2, 0, 0);
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
    private void MyCollectShow_MyCourse(LinearLayout modelmy_mycollect_main_content) {
        //测试数据我的课程子条目
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclass1, null);
        modelmy_mycollect_main_content.addView(view);
       //加载网络数据  我的课程
//        getModelMyClassCollection();
       //刷新页面
       // MyCollectShow_MyCourse(modelmy_mycollect_main_content);
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v -> {
            //跳转课程详情
            ModelCourseCover modelCourseCover = new ModelCourseCover();
            View modelCourseView = modelCourseCover.ModelCourseCover(mControlMainActivity, CourseInfo1);
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
    private void MyCollectShow_MyCoursePacket(LinearLayout modelmy_mycollect_main_content) {
        //modelmy_myclasspacket1_line1
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myclasspacket1, null);
        modelmy_mycollect_main_content.addView(view);
        //我的课程包
        //getgetModelMyClassPacketCollection();
        //刷新我的界面
        //添加每个课程的监听，点击跳转到课程详情
        view.setOnClickListener(v -> {
            //测试数据
            CoursePacketInfo CoursePacketInfo1 = new CoursePacketInfo();
            CoursePacketInfo1.mCoursePacketCover = "http://image.yunduoketang.com/course/34270/20190829/394d6b0a-6c3a-4121-b12c-ef53afff866d.png";
            CoursePacketInfo1.mCoursePacketLearnPersonNum = "1000";
            CoursePacketInfo1.mCoursePacketName = "皇家内训班";
            CoursePacketInfo1.mCoursePacketPrice = "免费";
            CoursePacketInfo1.mCoursePacketPriceOld = "1000";
            CoursePacketInfo1.mCoursePacketStageNum = "3";
            CoursePacketInfo1.mCoursePacketCourseNum = "6";
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
            CourseInfo1.mCoursePriceOld = "5000.00";
//        CourseInfo1.mCourseOrder = "1";
            StageCourseInfo1.mCourseInfoList.add(CourseInfo1);

            CourseInfo CourseInfo2 = new CourseInfo();
            CourseInfo2.mCourseCover = "http://image.yunduoketang.com/course/34270/20190829/2d04e2f9-1a4c-4cd6-8a4e-839ae8f653c2.png";
            CourseInfo2.mCourseLearnPersonNum = "1000";
            CourseInfo2.mCourseName = "开学典礼";
            CourseInfo2.mCoursePrice = "免费";
            CourseInfo2.mCourseType = "直播";
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
            View modelCoursePacketView = modelCoursePacketCover.ModelCoursePacketCover(mControlMainActivity, CoursePacketInfo1);
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
        //布局控件的线
        View modelmy_myclasspacket1_line1 = view.findViewById(R.id.modelmy_myclasspacket1_line1);
        modelmy_myclasspacket1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的缓存界面
    public void MyCacheShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCacheView == null) {
            mMyCacheView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycache, null);
            //Smart_model_my_mycache  我的缓存刷新
            mSmart_model_my_mycache = mMyCacheView.findViewById(R.id.Smart_model_my_mycache);
            mSmart_model_my_mycache.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_mycache.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_mycache.finishRefresh();
                }
            });
        }
        my_layout_main.addView(mMyCacheView);
        //我的缓存子条目布局
        LinearLayout modelmy_mycache_main_content = mMyCacheView.findViewById(R.id.modelmy_mycache_main_content);
        modelmy_mycache_main_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycache1, null);
        modelmy_mycache_main_content.addView(view);
        ControllerCustomRoundAngleImageView modelmy_mycache1_cover = view.findViewById(R.id.modelmy_mycache1_cover);
        Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                return false;
            }
        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_mycache1_cover);
        
        //监听，如果点击此课程，查看此课程下缓存的详细信息
        LinearLayout modelmy_mycache1_classname_layout = view.findViewById(R.id.modelmy_mycache1_classname_layout);
        modelmy_mycache1_classname_layout.setOnClickListener(v -> {
            MyCache_ManagementCacheShow();
        });
        //布局控件的线
        View modelmy_mycache1_line1 = view.findViewById(R.id.modelmy_mycache1_line1);
        modelmy_mycache1_line1.setVisibility(View.INVISIBLE);
    }

    //展示我的缓存-管理缓存界面
    public void MyCache_ManagementCacheShow() {
        if (mview == null) {
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
        for (int i = 0; i < CourseInfo1.mCourseChaptersInfoList.size(); i++) {
            CourseChaptersInfo courseChaptersInfo = CourseInfo1.mCourseChaptersInfoList.get(i);
            if (courseChaptersInfo == null) {
                continue;
            }
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager_child, null);
            TextView course_downloadmanager_child_titletext = view.findViewById(R.id.course_downloadmanager_child_titletext);
            course_downloadmanager_child_titletext.setText(courseChaptersInfo.mCourseChaptersName);
            course_downloadmanager_child_titletext.setHint(courseChaptersInfo.mCourseChaptersId);
            if (courseChaptersInfo.mCourseSectionsInfoList.size() == 0) {
                View course_downloadmanager_child_line1 = view.findViewById(R.id.course_downloadmanager_child_line1);
                course_downloadmanager_child_line1.setVisibility(View.INVISIBLE);
            }
            course_downloadmanager_layout_content.addView(view);
            LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
            course_downloadmanager_child_content.removeAllViews();
            for (int num = 0; num < courseChaptersInfo.mCourseSectionsInfoList.size(); num++) {
                CourseSectionsInfo courseSectionsInfo = courseChaptersInfo.mCourseSectionsInfoList.get(num);
                if (courseSectionsInfo == null) {
                    continue;
                }
                CourseRecordPlayDownloadInfo info = mCourseRecordPlayDownloadInfoMap.get(courseSectionsInfo.mCourseSectionsId);
                if (info == null) { //没有添加下载的不做处理
                    continue;
                }
                View MyOrderShow_MyOrder_view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelcoursedetails_download_manager_child1, null);
                TextView course_downloadmanager_child1_name = MyOrderShow_MyOrder_view2.findViewById(R.id.course_downloadmanager_child1_name);
                course_downloadmanager_child1_name.setText(courseSectionsInfo.mCourseSectionsName);
                course_downloadmanager_child1_name.setHint(courseSectionsInfo.mCourseSectionsId);
                ProgressBar progress_bar_healthy = MyOrderShow_MyOrder_view2.findViewById(R.id.progress_bar_healthy);
                int progress = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress = Math.toIntExact(Long.valueOf(info.mCourseSectionsDownloadSize)
                            / Long.valueOf(info.mCourseSectionsSize));
                }
                progress_bar_healthy.setProgress(progress);
                ImageView course_downloadmanager_child1_function = MyOrderShow_MyOrder_view2.findViewById(R.id.course_downloadmanager_child1_function);
                TextView course_downloadmanager_child_state = MyOrderShow_MyOrder_view2.findViewById(R.id.course_downloadmanager_child_state);
                course_downloadmanager_child1_function.setOnClickListener(v -> {
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_pause_blue) {
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_play_blue);
                        course_downloadmanager_child_state.setText("已暂停");
                        progress_bar_healthy.setProgressDrawable(MyOrderShow_MyOrder_view2.getResources().getDrawable(R.drawable.progressbar_bg1));
                    } else if (id == R.drawable.button_play_blue) {
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_pause_blue);
                        course_downloadmanager_child_state.setText("下载中");
                        progress_bar_healthy.setProgressDrawable(MyOrderShow_MyOrder_view2.getResources().getDrawable(R.drawable.progressbar_bg));
                    }
                });
                course_downloadmanager_child_content.addView(MyOrderShow_MyOrder_view2);
                count++;
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
        course_downloadmanager_layout_return_button1.setOnClickListener(v -> { //管理缓存界面的返回
            MyCacheShow();
        });
        TextView course_downloadmanager_all = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_all);
        course_downloadmanager_all.setOnClickListener(v -> { //点击全部暂停
            int num = course_downloadmanager_layout_content.getChildCount();
            for (int i = 0; i < num; i++) {
                View view = course_downloadmanager_layout_content.getChildAt(i);
                LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                int childCount = course_downloadmanager_child_content.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childView = course_downloadmanager_child_content.getChildAt(j);
                    ImageView course_downloadmanager_child1_function = childView.findViewById(R.id.course_downloadmanager_child1_function);
                    TextView course_downloadmanager_child_state = childView.findViewById(R.id.course_downloadmanager_child_state);
                    ProgressBar progress_bar_healthy = childView.findViewById(R.id.progress_bar_healthy);
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_pause_blue) {
                        course_downloadmanager_child1_function.setBackgroundResource(R.drawable.button_play_blue);
                        course_downloadmanager_child_state.setText("已暂停");
                        progress_bar_healthy.setProgressDrawable(childView.getResources().getDrawable(R.drawable.progressbar_bg1));
                    }
                }
            }

        });
        TextView course_downloadmanager_startall = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_startall);
        course_downloadmanager_startall.setOnClickListener(v -> { //点击全部开始
            int num = course_downloadmanager_layout_content.getChildCount();
            for (int i = 0; i < num; i++) {
                View view = course_downloadmanager_layout_content.getChildAt(i);
                LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                int childCount = course_downloadmanager_child_content.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childView = course_downloadmanager_child_content.getChildAt(j);
                    ImageView course_downloadmanager_child1_function = childView.findViewById(R.id.course_downloadmanager_child1_function);
                    TextView course_downloadmanager_child_state = childView.findViewById(R.id.course_downloadmanager_child_state);
                    ProgressBar progress_bar_healthy = childView.findViewById(R.id.progress_bar_healthy);
                    int id = getV7ImageResourceId(course_downloadmanager_child1_function);
                    if (id == R.drawable.button_play_blue) {
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
        course_downloadmanager_layout_edit.setOnClickListener(v -> {
            if (course_downloadmanager_layout_edit.getText().toString().equals("编辑")) { //跳转到编辑界面
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) course_downloadmanager_function.getLayoutParams();
                llp.height = 0;
                course_downloadmanager_function.setLayoutParams(llp);
                llp = (LinearLayout.LayoutParams) course_downloadmanager_function1.getLayoutParams();
                llp.height = mMyCacheManagementCacheView.getResources().getDimensionPixelSize(R.dimen.dp40);
                course_downloadmanager_function1.setLayoutParams(llp);
                course_downloadmanager_layout_edit.setText("完成");
                int num = course_downloadmanager_layout_content.getChildCount();
                for (int i = 0; i < num; i++) {
                    View view = course_downloadmanager_layout_content.getChildAt(i);
                    LinearLayout course_downloadmanager_child_content = view.findViewById(R.id.course_downloadmanager_child_content);
                    int childCount = course_downloadmanager_child_content.getChildCount();
                    for (int j = 0; j < childCount; j++) {
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
            } else if (course_downloadmanager_layout_edit.getText().toString().equals("完成")) {  //点击完成，跳转界面
                MyCache_ManagementCacheShow();
            }
        });
        //全部选择
        TextView course_downloadmanager_allselect = mMyCacheManagementCacheView.findViewById(R.id.course_downloadmanager_allselect);
        course_downloadmanager_allselect.setOnClickListener(v -> {
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
        course_downloadmanager_delete.setOnClickListener(v -> {
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
            button_cancel.setOnClickListener(View -> {
                mMyDialog.cancel();
            });
            TextView button_sure = view.findViewById(R.id.button_sure);
            button_sure.setText("确定");
            button_sure.setOnClickListener(View -> {
                int num = course_downloadmanager_layout_content.getChildCount();
                for (int i = 0; i < num; i++) {
                    View childView = course_downloadmanager_layout_content.getChildAt(i);
                    LinearLayout course_downloadmanager_child_content = childView.findViewById(R.id.course_downloadmanager_child_content);
                    int childCount = course_downloadmanager_child_content.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        View childMyOrderShow_MyOrder_view2 = course_downloadmanager_child_content.getChildAt(j);
                        ImageView course_downloadmanager_child1_select = childMyOrderShow_MyOrder_view2.findViewById(R.id.course_downloadmanager_child1_select);
                        int id = getV7ImageResourceId(course_downloadmanager_child1_select);
                        if (id == R.drawable.button_select_red) {//将选中的项目缓存全部清除
                            TextView course_downloadmanager_child1_name = childMyOrderShow_MyOrder_view2.findViewById(R.id.course_downloadmanager_child1_name);
                            if (this.mCourseRecordPlayDownloadInfoMap.get(course_downloadmanager_child1_name.getHint().toString()) != null) {
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

    private long getAvailaleSize() {

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024 / 1024;

        //(availableBlocks * blockSize)/1024      KIB 单位

        //(availableBlocks * blockSize)/1024 /1024  MIB单位
    }

    private static int getV7ImageResourceId(ImageView imageView) {
        int imgid = 0;
        Field[] fields = imageView.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().equals("mBackgroundTintHelper")) {
                f.setAccessible(true);
                try {
                    Object obj = f.get(imageView);
                    Field[] fields2 = obj.getClass().getDeclaredFields();
                    for (Field f2 : fields2) {
                        if (f2.getName().equals("mBackgroundResId")) {
                            f2.setAccessible(true);
                            imgid = f2.getInt(obj);
                            android.util.Log.d("1111", "Image ResourceId:" + imgid);
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
    public void MyOrderShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyOrderView == null) {
            mMyOrderView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder, null);
            //Smart_model_my_myorder   我的订单
            mSmart_model_my_myorder = mMyOrderView.findViewById(R.id.Smart_model_my_myorder);
            mSmart_model_my_myorder.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    //分页加载
//                    page++;
//                    pagesize++;
//                    getModelMyOrderList();
                    //分页加载数据
                    mSmart_model_my_myorder.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myorder.finishRefresh();
                }
            });
            //全部
            TextView modelmy_myorder_tab_all = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_all);
            modelmy_myorder_tab_all.setOnClickListener(v -> {
                if (!mMyOrderCurrentTab.equals("all")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation((mMyOrderLastTabIndex - 1) * width / 3, 0, 0, 0);
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
            //已完成
            TextView modelmy_myorder_tab_finished = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_finished);
            modelmy_myorder_tab_finished.setOnClickListener(v -> {
                if (!mMyOrderCurrentTab.equals("finished")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation((mMyOrderLastTabIndex - 1) * width / 3, width / 3, 0, 0);
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
            //未完成
            TextView modelmy_myorder_tab_unfinish = mMyOrderView.findViewById(R.id.modelmy_myorder_tab_unfinish);
            modelmy_myorder_tab_unfinish.setOnClickListener(v -> {
                if (!mMyOrderCurrentTab.equals("unfinish")) {
                    ImageView modelmy_myorder_cursor1 = mMyOrderView.findViewById(R.id.modelmy_myorder_cursor1);
                    Animation animation = new TranslateAnimation((mMyOrderLastTabIndex - 1) * width / 3, width * 2 / 3, 0, 0);
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

    //测试的数据
    private void MyOrderShow_MyOrder(LinearLayout modelmy_myorder_main_content) {
       //待支付
        MyOrderShow_MyOrder_view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        //未完成
        MyOrderShow_MyOrder_view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        //已完成
        MyOrderShow_MyOrder_view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorder1, null);
        //MyOrderShow_MyOrder_view3支付需要控制订单是否取消和是否重新支付
        if (mMyOrderCurrentTab.equals("all")) {
            //全部订单的网络数据
        // getModelMyOrderList();  判断当前的状态  添加MyOrderShow_MyOrder_view2就完事了
            //当前的支付状态
            TextView modelmy_myorder1_ordername1 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername1.setText("111");
            TextView modelmy_myorder1_orderstate1 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate1.setText("待支付");
            modelmy_myorder1_orderstate1.setTextColor(MyOrderShow_MyOrder_view1.getResources().getColor(R.color.text_orange1));
            TextView modelmy_myorder1_ordernumber1 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber1.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney1 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney1.setText("9000");

            modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view1); //待支付
            modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view2); //未完成
            modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view3);////已完成
        } else if (mMyOrderCurrentTab.equals("finished")) {
            // getModelMyOrderList();
            //已经完成
          // modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view1);
            TextView modelmy_myorder1_ordername = MyOrderShow_MyOrder_view3.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername.setText("技术精讲班1");
            TextView modelmy_myorder1_orderstate = MyOrderShow_MyOrder_view3.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate.setText("支付成功");
            modelmy_myorder1_orderstate.setTextColor(MyOrderShow_MyOrder_view3.getResources().getColor(R.color.holo_red_dark));
            TextView modelmy_myorder1_ordernumber = MyOrderShow_MyOrder_view3.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney = MyOrderShow_MyOrder_view3.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney.setText("9000");
            modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view3); //已完成
        } else if (mMyOrderCurrentTab.equals("unfinish")) {
            // getModelMyOrderList();
            //未完成
            TextView modelmy_myorder1_ordername = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordername);
            modelmy_myorder1_ordername.setText("技术精讲班1");
            TextView modelmy_myorder1_orderstate = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate.setText("支付失败");
            modelmy_myorder1_orderstate.setTextColor(MyOrderShow_MyOrder_view2.getResources().getColor(R.color.holo_red_dark));
            TextView modelmy_myorder1_ordernumber = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordernumber);
            modelmy_myorder1_ordernumber.setText("6523478915463225");
            TextView modelmy_myorder1_ordermoney = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordermoney);
            modelmy_myorder1_ordermoney.setText("9000");
            modelmy_myorder_main_content.addView(MyOrderShow_MyOrder_view2);
        }
//        //待支付
//        MyOrderShow_MyOrder_view1.setOnClickListener(v -> {
//            MyOrderShow_OrderDetails("success");
//        });
        //未完成
        MyOrderShow_MyOrder_view2.setOnClickListener(v -> {
            MyOrderShow_OrderDetails("fail");
        });
         //已完成
        MyOrderShow_MyOrder_view3.setOnClickListener(v -> {
            MyOrderShow_OrderDetails("success");
        });


        RelativeLayout modelmy_myorder1_orderfunction = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_orderfunction);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) modelmy_myorder1_orderfunction.getLayoutParams();
        rl.height = 0;
        modelmy_myorder1_orderfunction.setLayoutParams(rl);

        modelmy_myorder1_orderfunction = MyOrderShow_MyOrder_view3.findViewById(R.id.modelmy_myorder1_orderfunction);
        rl = (RelativeLayout.LayoutParams) modelmy_myorder1_orderfunction.getLayoutParams();
        rl.height = 0;
        modelmy_myorder1_orderfunction.setLayoutParams(rl);
        //待支付的订单取消和重新支付
        ImageView modelmy_myorder1_cancel = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_cancel);
        ImageView modelmy_myorder1_retrypay = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_retrypay);
        modelmy_myorder1_cancel.setOnClickListener(v -> {
            //取消支付，将此订单设置为失效
            TextView modelmy_myorder1_orderstate1 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_orderstate);
            modelmy_myorder1_orderstate1.setText("支付失败");
            String s = modelmy_myorder1_orderstate1.getText().toString();
            if (s.equals("支付失败")){
                RelativeLayout modelmy_myorder1_orderfunction2 = MyOrderShow_MyOrder_view1.findViewById(R.id.modelmy_myorder1_orderfunction);
                RelativeLayout.LayoutParams r2 = (RelativeLayout.LayoutParams) modelmy_myorder1_orderfunction2.getLayoutParams();
                r2.height = 0;
                modelmy_myorder1_orderfunction2.setLayoutParams(r2);
            }
        });
        modelmy_myorder1_retrypay.setOnClickListener(v -> {
            //重新支付，进入支付界面OrderDtils判断是否支付成功
            HideAllLayout();
            LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
            View view = mControlMainActivity.Page_OrderDetails(() -> {
                //订单详情页返回刷新订单列表
                MyOrderShow();
            }, null, null,mMyOrderListBean);
            my_layout_main.addView(view);
            Toast.makeText(mControlMainActivity, "重新支付", Toast.LENGTH_SHORT).show();
        });
    }
       //订单待支付
    private void initOrderAll() {
        TextView modelmy_myorder1_ordername = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordername);
        modelmy_myorder1_ordername.setText(mMyOrderListBean.product_name);
        TextView modelmy_myorder1_orderstate = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_orderstate);
        modelmy_myorder1_orderstate.setText(mMyOrderListBean.order_status);
        modelmy_myorder1_orderstate.setTextColor(MyOrderShow_MyOrder_view2.getResources().getColor(R.color.holo_red_dark));
        TextView modelmy_myorder1_ordernumber = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordernumber);
        modelmy_myorder1_ordernumber.setText(mMyOrderListBean.order_num);
        TextView modelmy_myorder1_ordermoney = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordermoney);
        modelmy_myorder1_ordermoney.setText(mMyOrderListBean.product_price+"");
    }
         //订单支付成功
    private void initOrderFinish() {
        TextView modelmy_myorder1_ordername = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordername);
        modelmy_myorder1_ordername.setText(mMyOrderListBean.product_name);
        TextView modelmy_myorder1_orderstate = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_orderstate);
        modelmy_myorder1_orderstate.setText(mMyOrderListBean.order_status);
        modelmy_myorder1_orderstate.setTextColor(MyOrderShow_MyOrder_view2.getResources().getColor(R.color.holo_red_dark));
        TextView modelmy_myorder1_ordernumber = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordernumber);
        modelmy_myorder1_ordernumber.setText(mMyOrderListBean.order_num);
        TextView modelmy_myorder1_ordermoney = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordermoney);
        modelmy_myorder1_ordermoney.setText(mMyOrderListBean.product_price+"");
    }
    //订单支付不成功
        private void initOrderUnFinish() {
        TextView modelmy_myorder1_ordername = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordername);
        modelmy_myorder1_ordername.setText(mMyOrderListBean.product_name);
        TextView modelmy_myorder1_orderstate = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_orderstate);
        modelmy_myorder1_orderstate.setText(mMyOrderListBean.order_status);
        modelmy_myorder1_orderstate.setTextColor(MyOrderShow_MyOrder_view2.getResources().getColor(R.color.holo_red_dark));
        TextView modelmy_myorder1_ordernumber = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordernumber);
        modelmy_myorder1_ordernumber.setText(mMyOrderListBean.order_num);
        TextView modelmy_myorder1_ordermoney = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myorder1_ordermoney);
        modelmy_myorder1_ordermoney.setText(mMyOrderListBean.product_price+"");
    }
    //订单详情
    private void MyOrderShow_OrderDetails(String state) {
        if (mview == null) {
            return;
        }
        mControlMainActivity.onClickMyOrderDetails();
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyOrderDetailsView == null) {
            //订单详情的刷新控件
            mMyOrderDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myorderdetails, null);
            //Smart_model_my_myorderdetails  刷新控件
            mSmart_model_my_myorderdetails = mMyOrderDetailsView.findViewById(R.id.Smart_model_my_myorderdetails);
            mSmart_model_my_myorderdetails.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myorderdetails.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myorderdetails.finishRefresh();
                }
            });
            //点击复制订单号
            ImageView modelmy_myorderdetails_ordernumbercopy = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_ordernumbercopy);
            modelmy_myorderdetails_ordernumbercopy.setOnClickListener(v -> {
                //订单编号
                TextView modelmy_myorderdetails_ordernumber = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_ordernumber);
                String modelmy_myorderdetails_ordernumbertext = modelmy_myorderdetails_ordernumber.getText().toString();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mControlMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", modelmy_myorderdetails_ordernumbertext);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(mControlMainActivity, "已将订单号复制到剪贴板", Toast.LENGTH_SHORT).show();
            });
        }
        my_layout_main.addView(mMyOrderDetailsView);
        //文件订单详情   订单判断的时间
        ImageView modelmy_myorderdetails_icon = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_icon);
        ImageView modelmy_myorderdetails_background_fail = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_background_fail);
        TextView modelmy_myorderdetails_invalid = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_invalid);
        ImageView modelmy_myorderdetails_invalidicon = mMyOrderDetailsView.findViewById(R.id.modelmy_myorderdetails_invalidicon);
        if (state.equals("success")) { //成功的详情背景
            modelmy_myorderdetails_invalid.setText("支付成功");
            modelmy_myorderdetails_background_fail.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_background_success));
            modelmy_myorderdetails_icon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_success));
            modelmy_myorderdetails_invalidicon.setBackground(mMyOrderDetailsView.getResources().getDrawable(R.drawable.orderdetails_icon_paysuccess));
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) modelmy_myorderdetails_invalidicon.getLayoutParams();
            rl.width = mMyOrderDetailsView.getResources().getDimensionPixelSize(R.dimen.dp_77);
            modelmy_myorderdetails_invalidicon.setLayoutParams(rl);
        } else { //反之失败的详情背景
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
    public void MyCouponShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyCouponView == null) {
            mMyCouponView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon, null);
            //Smart_model_my_mycoupon   我的优惠券列表
            mSmart_model_my_mycoupon = mMyCouponView.findViewById(R.id.Smart_model_my_mycoupon);
            mSmart_model_my_mycoupon.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    MyCoupon_pageNum++;
                    MyCoupon_pageSize++;
                    //获取网络数据
                    //getMyCouponList();
                    mSmart_model_my_mycoupon.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_mycoupon.finishRefresh();
                }
            });
            TextView modelmy_mycoupon_tab_notused = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_notused);
            modelmy_mycoupon_tab_notused.setOnClickListener(v -> {
                if (!mMyCouponCurrentTab.equals("notused")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation((mMyCouponLastTabIndex - 1) * width / 3, 0, 0, 0);
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
                mMyCouponCurrentTab = "notused";        //未使用的优惠券
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            TextView modelmy_mycoupon_tab_alreadyused = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_alreadyused);
            modelmy_mycoupon_tab_alreadyused.setOnClickListener(v -> {
                if (!mMyCouponCurrentTab.equals("alreadyused")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation((mMyCouponLastTabIndex - 1) * width / 3, width / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycoupon_cursor1.startAnimation(animation);
                    TextView modelmy_mycoupon_tab_expired = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_expired);
                    modelmy_mycoupon_tab_expired.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_notused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_alreadyused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                //已使用
                mMyCouponLastTabIndex = 2;
                mMyCouponCurrentTab = "alreadyused";
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            TextView modelmy_mycoupon_tab_expired = mMyCouponView.findViewById(R.id.modelmy_mycoupon_tab_expired);
            modelmy_mycoupon_tab_expired.setOnClickListener(v -> {
                if (!mMyOrderCurrentTab.equals("expired")) {
                    ImageView modelmy_mycoupon_cursor1 = mMyCouponView.findViewById(R.id.modelmy_mycoupon_cursor1);
                    Animation animation = new TranslateAnimation((mMyCouponLastTabIndex - 1) * width / 3, width * 2 / 3, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_mycoupon_cursor1.startAnimation(animation);
                    modelmy_mycoupon_tab_expired.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_mycoupon_tab_notused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    modelmy_mycoupon_tab_alreadyused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyCouponView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                //已过期
                mMyCouponLastTabIndex = 3;
                mMyCouponCurrentTab = "expired";
                LinearLayout modelmy_mycoupon_main_content = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_content);
                modelmy_mycoupon_main_content.removeAllViews();
                //可循环调用以下方法，添加多个优惠券
                MyCouponShow_MyCoupon(modelmy_mycoupon_main_content);
            });
            //点击兑换
            TextView modelmy_mycoupon_main_exchange = mMyCouponView.findViewById(R.id.modelmy_mycoupon_main_exchange);
            modelmy_mycoupon_main_exchange.setOnClickListener(v -> {
                //点击兑换弹出兑换对话框
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel1, null);
                mMyCouponDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
                mMyCouponDialog.setCancelable(true);
                mMyCouponDialog.show();
                TextView button_cancel = view.findViewById(R.id.button_cancel);
                button_cancel.setOnClickListener(View -> {
                    mMyCouponDialog.cancel();
                });
                TextView button_sure = view.findViewById(R.id.button_sure);
                button_sure.setOnClickListener(View -> {
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

    private void MyCouponShow_MyCoupon(LinearLayout modelmy_myorder_main_content) {
       //减满
        lessenfull_view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
        //打折券
        withstandcoupon_view2 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
        // 抵现券
        discountcoupon_view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
        //未使用列表
        if (mMyCouponCurrentTab.equals("notused")) {
            //三种优惠券    网络加载数据的话直接for循环添加
         //   getMyCouponList();
            {
                //添加满减卷
                TextView modelmy_mycoupon1_couponprice = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponprice);//价格
                modelmy_mycoupon1_couponprice.setText("200");
                TextView modelmy_mycoupon1_couponfullreduction = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);//优惠券
                modelmy_mycoupon1_couponfullreduction.setText("满减券");
                TextView modelmy_mycoupon1_termofvaliditydata = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);//有效期
                modelmy_mycoupon1_termofvaliditydata.setText("2020.01.13");
                TextView modelmy_mycoupon1_couponrequire = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);//检满价格
                modelmy_mycoupon1_couponrequire.setText("满10元就可以使用");
                TextView modelmy_mycoupon1_areaofapplication = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_areaofapplication);//使用范围
                modelmy_mycoupon1_areaofapplication.setText("all");
                modelmy_myorder_main_content.addView(lessenfull_view1);
            }
//            {
//                //添加打折卷
//                modelmy_myorder_main_content.addView(withstandcoupon_view2);
//                TextView modelmy_mycoupon1_couponrequire = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponpriceicon = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
//                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
//                ll.width = 0;
//                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponprice = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
//                TextView modelmy_mycoupon1_couponfullreduction = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//            }
//            {
//                //抵现卷
//                modelmy_myorder_main_content.addView(discountcoupon_view3);
//                TextView modelmy_mycoupon1_couponrequire = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponprice = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("600");
//                TextView modelmy_mycoupon1_couponfullreduction = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//            }
        } else if (mMyCouponCurrentTab.equals("alreadyused")) {
            //已经使用
            {
                //添加满减卷
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                //设置背景 字体大小  字体颜色
                modelmy_myorder_main_content.addView(lessenfull_view1);
                TextView modelmy_mycoupon1_couponpriceicon = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponprice = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponrequire = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                modelmy_mycoupon1_couponrequire.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(lessenfull_view1.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
            }
//            {
//                //添加打折卷
//                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
//                modelmy_myorder_main_content.addView(withstandcoupon_view2);
//                //显示或者隐藏布局中需要掩饰的控件
//                TextView modelmy_mycoupon1_couponrequire = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponpriceicon = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
//                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
//                ll.width = 0;
//                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
//                //网络请求
//                TextView modelmy_mycoupon1_couponprice = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
//                TextView modelmy_mycoupon1_couponfullreduction = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//
//                modelmy_mycoupon1_couponpriceicon.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponprice.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponrequire.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                ImageView modelmy_mycoupon1_couponstate = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponstate);
//                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
//                modelmy_mycoupon1_couponstate.setBackground(withstandcoupon_view2.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
//            }
//            {
//                //抵现卷的布局样式
//                View MyOrderShow_MyOrder_view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
//                modelmy_myorder_main_content.addView(discountcoupon_view3);
//                TextView modelmy_mycoupon1_couponrequire = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponprice = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("600");
//                TextView modelmy_mycoupon1_couponfullreduction = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//
//                TextView modelmy_mycoupon1_couponpriceicon = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
//                modelmy_mycoupon1_couponpriceicon.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponprice.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponrequire.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                ImageView modelmy_mycoupon1_couponstate = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponstate);
//                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
//                modelmy_mycoupon1_couponstate.setBackground(discountcoupon_view3.getResources().getDrawable(R.drawable.mycoupon_icon_alreadyused));
//            }
        } else if (mMyCouponCurrentTab.equals("expired")) {
            //已经过期列表    网络请求获取相应的数据
            {
                //添加满减卷
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
                modelmy_myorder_main_content.addView(lessenfull_view1);
                TextView modelmy_mycoupon1_couponpriceicon = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
                modelmy_mycoupon1_couponpriceicon.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponprice = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponprice);
                modelmy_mycoupon1_couponprice.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                TextView modelmy_mycoupon1_couponrequire = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);
                modelmy_mycoupon1_couponrequire.setTextColor(lessenfull_view1.getResources().getColor(R.color.grayccbab9b9));
                ImageView modelmy_mycoupon1_couponstate = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponstate);
                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
                modelmy_mycoupon1_couponstate.setBackground(lessenfull_view1.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
            }
//            {
//                //添加打折卷
//                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
//                modelmy_myorder_main_content.addView(withstandcoupon_view2);
//                TextView modelmy_mycoupon1_couponrequire = view.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponpriceicon = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
//                ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponpriceicon.getLayoutParams();
//                ll.width = 0;
//                modelmy_mycoupon1_couponpriceicon.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponprice = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("0.01" + "折");
//                TextView modelmy_mycoupon1_couponfullreduction = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("打折卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//
//                modelmy_mycoupon1_couponpriceicon.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponprice.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponrequire.setTextColor(withstandcoupon_view2.getResources().getColor(R.color.grayccbab9b9));
//                ImageView modelmy_mycoupon1_couponstate = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponstate);
//                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
//                modelmy_mycoupon1_couponstate.setBackground(withstandcoupon_view2.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
//            }
//            {
//                //抵现卷
//                View MyOrderShow_MyOrder_view3 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mycoupon1, null);
//                modelmy_myorder_main_content.addView(discountcoupon_view3);
//                TextView modelmy_mycoupon1_couponrequire = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponrequire);
//                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) modelmy_mycoupon1_couponrequire.getLayoutParams();
//                ll.height = 0;
//                modelmy_mycoupon1_couponrequire.setLayoutParams(ll);
//                TextView modelmy_mycoupon1_couponprice = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponprice);
//                modelmy_mycoupon1_couponprice.setText("600");
//                TextView modelmy_mycoupon1_couponfullreduction = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);
//                modelmy_mycoupon1_couponfullreduction.setText("抵现卷");
//                TextView modelmy_mycoupon1_termofvaliditydata = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);
//                modelmy_mycoupon1_termofvaliditydata.setText("2019-12-04");
//                TextView modelmy_mycoupon1_areaofapplication = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_areaofapplication);
//                modelmy_mycoupon1_areaofapplication.setText("可适用于所有课程包");
//
//                TextView modelmy_mycoupon1_couponpriceicon = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponpriceicon);
//                modelmy_mycoupon1_couponpriceicon.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponprice.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                modelmy_mycoupon1_couponrequire.setTextColor(discountcoupon_view3.getResources().getColor(R.color.grayccbab9b9));
//                ImageView modelmy_mycoupon1_couponstate = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponstate);
//                modelmy_mycoupon1_couponstate.setVisibility(View.VISIBLE);
//                modelmy_mycoupon1_couponstate.setBackground(discountcoupon_view3.getResources().getDrawable(R.drawable.mycoupon_icon_outofdata));
//            }
        }
    }

    //展示我的消息界面
    public void MyMessageShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyMessageView == null) {
            mMyMessageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mymessage, null);
            //Smart_model_my_mymessage   我的消息刷新控件
            mSmart_model_my_mymessage = mMyMessageView.findViewById(R.id.Smart_model_my_mymessage);
            mSmart_model_my_mymessage.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_mymessage.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if (list!=null){
                        list.clear();
                    }
                    adapter.notifyDataSetChanged();
                    mSmart_model_my_mymessage.finishRefresh();
                }
            });

            ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
            //点击全部已读
            modelmy_mymessage_clear.setOnClickListener(v -> {
               //modelmy_mymessage_noticemessagecount
                TextView modelmy_mymessage_noticemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_noticemessagecount);
                TextView modelmy_mymessage_advertisemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_advertisemessagecount);
                if (modelmy_mymessage_noticemessagecount.getVisibility() == View.VISIBLE || modelmy_mymessage_advertisemessagecount.getVisibility() == View.VISIBLE) {  //有未读消息
                    m_isFind = true;
                } else {
                    ControllerListViewForScrollView listView = mMyMessageView0.findViewById(R.id.modelmy_mymessage_main_contentlistview);
                     //获取到相应的子条目的消息数量
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
                if (!m_isFind) {
                    Toast.makeText(mControlMainActivity, "暂无未读消息", Toast.LENGTH_LONG).show();
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
        //点击清空消息
        ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
        modelmy_mymessage_clear.setVisibility(View.VISIBLE);
        my_layout_main.addView(mMyMessageView);
        //我的消息列表
        LinearLayout modelmy_mymessage_main_content = mMyMessageView.findViewById(R.id.modelmy_mymessage_main_content);
        modelmy_mymessage_main_content.removeAllViews();
        if (mMyMessageView0 == null) {
            mMyMessageView0 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_mymessage0, null);
        }
        //系统通知
        RelativeLayout modelmy_mymessage_notice = mMyMessageView0.findViewById(R.id.modelmy_mymessage_notice);
        modelmy_mymessage_notice.setOnClickListener(v -> {
            m_isFind=false;
            TextView modelmy_mymessage_noticemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_noticemessagecount);
            //修改系统通知的已读状态
            modelmy_mymessage_noticemessagecount.setVisibility(android.view.View.INVISIBLE);
             //系统的列表
            MyMessageShow_MessageDatails(modelmy_mymessage_main_content, "notice");
        });
        //广告
        RelativeLayout modelmy_mymessage_advertise = mMyMessageView0.findViewById(R.id.modelmy_mymessage_advertise);
        modelmy_mymessage_advertise.setOnClickListener(v -> {
            m_isFind=true;
            //修改广告通知的已读状态
            TextView modelmy_mymessage_advertisemessagecount = mMyMessageView0.findViewById(R.id.modelmy_mymessage_advertisemessagecount);
            modelmy_mymessage_advertisemessagecount.setVisibility(android.view.View.INVISIBLE);
            //广告的列表
            MyMessageShow_MessageDatails(modelmy_mymessage_main_content, "advertise");
        });
        modelmy_mymessage_main_content.addView(mMyMessageView0);
        //子条目回复问题列表
        ControllerListViewForScrollView listView = mMyMessageView0.findViewById(R.id.modelmy_mymessage_main_contentlistview);

        //加网络请求放到网络请求中赋值 刷新适配器
       // getModelMyMessageList();
        for (int i = 0; i < 10; i++) {
            ControllerMyMessage1Adapter.MyMessageInfo message = new ControllerMyMessage1Adapter.MyMessageInfo();
            message.modelmy_mymessage1_coverurl = "";
            message.modelmy_mymessage1_name = i + "回复您的问题";  //我的消息列表标题
            message.modelmy_mymessage1_messagecount = i + "";     //我的消息未读数量
            message.modelmy_mymessage1_message = "你的问题都很有趣";//我的消息message
            message.modelmy_mymessage1_time = "1-12 22:11";//我的消息时间
            list.add(message);
        }
        adapter = new ControllerMyMessage1Adapter(mControlMainActivity, list);
        listView.setAdapter(adapter);
    }

    //展示我的消息-系统消息界面
    public void MyMessageShow_MessageDatails(LinearLayout modelmy_mymessage_main_content, String type) {
        mControlMainActivity.onClickMyMessageDetails();
        if (mview == null || mMyMessageView == null) {
            return;
        }
        ImageView modelmy_mymessage_clear = mMyMessageView.findViewById(R.id.modelmy_mymessage_clear);
        modelmy_mymessage_clear.setVisibility(View.INVISIBLE);
        modelmy_mymessage_main_content.removeAllViews();
        if (type.equals("notice")) {
       //我的系统消息的网络请求
            {
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                //系统消息的logo
                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
                Glide.with(getActivity()).load("").error(R.drawable.img_error).into(modelmy_mymessage2_cover);
                //系统消息的时间
                TextView modelmy_mymessage2_time = view.findViewById(R.id.modelmy_mymessage2_time);
                modelmy_mymessage2_time.setText("我是系统消息的时间");
                //系统消息的message
                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
                modelmy_mymessage2_message.setText("我是系统消息的message");
                modelmy_mymessage_main_content.addView(view);


            }
//            {
//                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
//                modelmy_mymessage_main_content.addView(view);
//            }
//            {
//                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
//                modelmy_mymessage_main_content.addView(view);
//                //如果是已读，将文字颜色置为灰色    已读状态判断
//                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
//                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
//            }
        } else {
            //我的广告信息网络请求
            {
                my_mymessage2_view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
                //切换广告logo
                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = my_mymessage2_view.findViewById(R.id.modelmy_mymessage2_cover);
                modelmy_mymessage2_cover.setBackground(my_mymessage2_view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
                //加载广告的logo
                Glide.with(getActivity()).load("").error(R.drawable.img_error).into(modelmy_mymessage2_cover);
                //系统消息的时间
                TextView modelmy_mymessage2_time = my_mymessage2_view.findViewById(R.id.modelmy_mymessage2_time);
                modelmy_mymessage2_time.setText("我是广告消息的时间");
                //系统消息的message
                TextView modelmy_mymessage2_message = my_mymessage2_view.findViewById(R.id.modelmy_mymessage2_message);
                modelmy_mymessage2_message.setText("我是广告消息的message");
                modelmy_mymessage_main_content.addView(my_mymessage2_view);
            }
//            {
//                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
//                //切换广告logo
//                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
//                modelmy_mymessage2_cover.setBackground(view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
//                modelmy_mymessage_main_content.addView(view);
//                //如果是已读，将文字颜色置为灰色
//                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
//                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
//            }
//            {
//                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_mymessage2, null);
//                //切换广告logo
//                ControllerCustomRoundAngleImageView modelmy_mymessage2_cover = view.findViewById(R.id.modelmy_mymessage2_cover);
//                modelmy_mymessage2_cover.setBackground(view.getResources().getDrawable(R.drawable.img_mymessage_advertisement));
//                modelmy_mymessage_main_content.addView(view);
//                //如果是已读，将文字颜色置为灰色
//                TextView modelmy_mymessage2_message = view.findViewById(R.id.modelmy_mymessage2_message);
//                modelmy_mymessage2_message.setTextColor(view.getResources().getColor(R.color.black999999));
//            }
        }
    }

    //展示我的问答界面
    public void MyAnswerShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyAnswerView == null) {
            mMyAnswerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_my_myanswer, null);
            //我的问答刷新控件
            mSmart_model_my_myanswer = mMyAnswerView.findViewById(R.id.Smart_model_my_myanswer);
            mSmart_model_my_myanswer.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myanswer.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myanswer.finishRefresh();
                }
            });
            TextView modelmy_myanswer_tab_question = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_question);
            modelmy_myanswer_tab_question.setOnClickListener(v -> {
                if (!mMyAnswerCurrentTab.equals("question")) {
                    ImageView modelmy_myanswer_cursor1 = mMyAnswerView.findViewById(R.id.modelmy_myanswer_cursor1);
                    Animation animation = new TranslateAnimation((mMyAnswerLastTabIndex - 1) * width / 2, 0, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    modelmy_myanswer_cursor1.startAnimation(animation);
                    TextView modelmy_myanswer_tab_answer = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_answer);
                    modelmy_myanswer_tab_question.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    modelmy_myanswer_tab_answer.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMyAnswerView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mMyAnswerLastTabIndex = 1;
                mMyAnswerCurrentTab = "question";
                modelmy_myanswer_main_content = mMyAnswerView.findViewById(R.id.modelmy_myanswer_main_content);
                modelmy_myanswer_main_content.removeAllViews();
                //测试数据  网络请求赋值刷新页面
                //getModelMyQuestionList();
                //initMyQuestion();
                //我的提问  Question   我的回答 answer   测试数据
                for (int i = 0; i < 5; i++) {
                    View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
                    TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
                    modelmy_myanswer1_title.setText("技术基础实务" + i);
                    TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
                    modelmy_myanswer1_content.setText("消防器材的正确使用方式是什么啊");
                    TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
                    modelmy_myanswer1_time.setText("5分钟前");
                    modelmy_myanswer_main_content.addView(view);
                    view.setOnClickListener(V -> {
                        MyAnswerShow_Details();
                    });
                }
            });
            TextView modelmy_myanswer_tab_answer = mMyAnswerView.findViewById(R.id.modelmy_myanswer_tab_answer);
            modelmy_myanswer_tab_answer.setOnClickListener(v -> {
                if (!mMyAnswerCurrentTab.equals("answer")) {
                    ImageView modelmy_myanswer_cursor1 = mMyAnswerView.findViewById(R.id.modelmy_myanswer_cursor1);
                    Animation animation = new TranslateAnimation((mMyAnswerLastTabIndex - 1) * width / 2, width / 2, 0, 0);
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
               // getModelMyAnswerList();
               // initMyAnswer();
                //测试数据  刷新页面
                for (int i = 0; i < 3; i++) {
                    View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
                    TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
                    modelmy_myanswer1_title.setText("我是回答的技术实务" + i);
                    TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
                    modelmy_myanswer1_content.setText("我是回答的消防器材的正确使用方式是什么啊");
                    TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
                    modelmy_myanswer1_time.setText("我是回答的5分钟前");
                    modelmy_myanswer_main_content.addView(view);
                    view.setOnClickListener(V -> {
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
        for (int i = 0; i < 5; i++) {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
            TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
            modelmy_myanswer1_title.setText("技术基础实务" + i);
            TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
            modelmy_myanswer1_content.setText("消防器材的正确使用方式是什么啊");
            TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
            modelmy_myanswer1_time.setText("5分钟前");
            modelmy_myanswer_main_content.addView(view);
            view.setOnClickListener(V -> {
                MyAnswerShow_Details();
            });
        }

//        //测试数据
//        initMyAnswer();
//        for (int i = 0; i < 5; i++) {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
//            TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
//            modelmy_myanswer1_title.setText("我是question的技术基础实务" + i);
//            TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
//            modelmy_myanswer1_content.setText("我是question的消防器材的正确使用方式是什么啊");
//            TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
//            modelmy_myanswer1_time.setText("我是question的5分钟前");
//            modelmy_myanswer_main_content.addView(view);
//            view.setOnClickListener(V -> {
//                MyAnswerShow_Details();
//            });
//        }
    }
    //回答
    public void initMyAnswer() {

//        for (int i = 0; i < 5; i++) {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswer1, null);
//            TextView modelmy_myanswer1_title = view.findViewById(R.id.modelmy_myanswer1_title);
//            modelmy_myanswer1_title.setText("我是question的技术基础实务" + i);
//            TextView modelmy_myanswer1_content = view.findViewById(R.id.modelmy_myanswer1_content);
//            modelmy_myanswer1_content.setText("我是question的消防器材的正确使用方式是什么啊");
//            TextView modelmy_myanswer1_time = view.findViewById(R.id.modelmy_myanswer1_time);
//            modelmy_myanswer1_time.setText("我是question的5分钟前");
//            modelmy_myanswer_main_content.addView(view);
//            view.setOnClickListener(V -> {
//                MyAnswerShow_Details();
//            });
//        }
    }

    public void initMyQuestion() {

    }
     //我的问答详情
    private void MyAnswerShow_Details() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout my_layout_main = mview.findViewById(R.id.my_layout_main);
        if (mMyAnswerDetailsView == null) {
            mMyAnswerDetailsView = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_my_myanswerdetails, null);
            //Smart_model_my_myanswerdetails  我的问答详情
            mSmart_model_my_myanswerdetails = mMyAnswerDetailsView.findViewById(R.id.Smart_model_my_myanswerdetails);
            mSmart_model_my_myanswerdetails.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_my_myanswerdetails.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    //控件的刷新方法
                    mSmart_model_my_myanswerdetails.finishRefresh();
                }
            });
            //点击删除本条问答
            ImageView modelmy_myanswerdetails_delete = mMyAnswerDetailsView.findViewById(R.id.modelmy_myanswerdetails_delete);
            modelmy_myanswerdetails_delete.setOnClickListener(v -> {
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
                button_cancel.setOnClickListener(View -> {
                    mMyDialog.cancel();
                });
                TextView button_sure = view.findViewById(R.id.button_sure);
                button_sure.setText("确定");
                button_sure.setOnClickListener(View -> {
                    mMyDialog.cancel();
                    //点击确定删除本条数据
                    mControlMainActivity.onClickMyAnswerReturn(View);
                    //关闭本页面 将当前的数据id重集合中删除   刷新页面


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
        //我的问答学员评论列表请求
        //测试数据
        for (int i = 0; i < 3; i++) {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.modelanswerdetails_child, null);
            //学员的头像
            ControllerCustomRoundAngleImageView answerdetails_child_headportrait = view.findViewById(R.id.answerdetails_child_headportrait);
            Glide.with(getActivity()).load("").into(answerdetails_child_headportrait);
            //学员名称
            TextView answerdetails_child_name = view.findViewById(R.id.answerdetails_child_name);
            answerdetails_child_name.setText("学员" + i);
            //学员的时间
            TextView manswerdetails_child_time = view.findViewById(R.id.answerdetails_child_time);
            manswerdetails_child_time.setText("我的学员时间");
             //学员的消息
            TextView answerdetails_child_message = view.findViewById(R.id.answerdetails_child_message);
            answerdetails_child_message.setText("学员的message消息");
            answerdetails_content.addView(view);
            line = view.findViewById(R.id.answerdetails_child_line);
        }
        if (line != null) {
            line.setVisibility(View.INVISIBLE);
        }
        mControlMainActivity.Page_AnswerDetails();
    }

    // 查询个人信息详情（我的界面）
    public void getPersonalInfoDatas() {
        if (mControlMainActivity.mStuId.equals("")){
            mPersonalInfoDataBean = null;
            //重置我的界面
            ModelMyInit();
//            Toast.makeText(mControlMainActivity, "获取个人信息失败", Toast.LENGTH_LONG).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ModelObservableInterface.urlHead)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);

        Gson gson = new Gson();

        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("stu_id", Integer.valueOf(mControlMainActivity.mStuId));
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        Call<PersonalInfoBean> call = modelObservableInterface.queryModelMyPersonInfo(body);
        call.enqueue(new Callback<PersonalInfoBean>() {
            @Override
            public void onResponse(Call<PersonalInfoBean> call, Response<PersonalInfoBean> response) {
                PersonalInfoBean personalInfoBean = response.body();
                if (personalInfoBean == null) {
                    Toast.makeText(mControlMainActivity, "获取个人信息失败", Toast.LENGTH_LONG).show();
                    return;
                }
                //网络请求数据成功
                mPersonalInfoDataBean = personalInfoBean.getData();
                //重置我的界面
                ModelMyInit();
            }

            @Override
            public void onFailure(Call<PersonalInfoBean> call, Throwable t) {
                Toast.makeText(mControlMainActivity, "获取个人信息失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    //个人信息详情（我的界面）
    public static class PersonalInfoBean {
        private PersonalInfoDataBean data;
        private int code;
        private String msg;

        public PersonalInfoDataBean getData() {
            return data;
        }

        public void setData(PersonalInfoDataBean data) {
            this.data = data;
        }

        public int getErrorCode() {
            return code;
        }

        public void setErrorCode(int code) {
            this.code = code;
        }

        public String getErrorMsg() {
            return msg;
        }

        public void setErrorMsg(String msg) {
            this.msg = msg;
        }

        public static class PersonalInfoDataBean {
            private String autograph;       //个人签名
            private String nickname;        //用户昵称
            private String stu_name;            //姓名
            private String head;          //用户头像
            private String tel;           //手机号码
            private String login_number;    //账号
            private String ID_number;       //身份证号码
            private String email;           //邮箱
        }
    }

    //我的课程列表
    public void getMyCourseList() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("news_id", 1);//第几页
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        //queryMyCourseList
        queryMyCourseList.queryMyCourseList(body)
                .enqueue(new Callback<queryMyCourseListBean>() {
                    private String cover;
                    private int course_id;
                    private Object agreement_id;
                    private String course_type;
                    private int buying_base_number;
                    private int stuNum;
                    private String course_name;
                    @Override
                    public void onResponse(Call<queryMyCourseListBean> call, Response<queryMyCourseListBean> response) {
                        queryMyCourseListBean listBean = response.body();
                        int code = listBean.getCode();
                        if (code == 200) {
                            queryMyCourseListBean.DataBean listBeanData = listBean.getData();
                            List<queryMyCourseListBean.DataBean.ListBean> list = listBeanData.getList();
                            for (int i = 0; i < list.size(); i++) {
                                cover = list.get(i).getCover();//课程封面
                                course_id = list.get(i).getCourse_id(); //课程id
                                agreement_id = list.get(i).getAgreement_id(); //协议id
                                course_type = list.get(i).getCourse_type();  //课程类型
                                buying_base_number = list.get(i).getBuying_base_number();  //购买基数（用不到）
                                stuNum = list.get(i).getStuNum();//这个课程下面的学生数量
                                course_name = list.get(i).getCourse_name();   //课程名称
                                initData();
                            }
                        }
                    }
                    private void initData() {
                        //数据的赋值
                        ControllerCustomRoundAngleImageView modelmy_myclass1_cover = view.findViewById(R.id.modelmy_myclass1_cover);
                        Glide.with(mControlMainActivity).load(cover).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                                return false;
                            }
                        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_myclass1_cover);
                        //title标签
                        TextView modelmy_myclass1_classname = view.findViewById(R.id.modelmy_myclass1_classname);
                        modelmy_myclass1_classname.setText(course_name);
                        //人数
                        TextView modelmy_myclass1_state = view.findViewById(R.id.modelmy_myclass1_state);
                        modelmy_myclass1_state.setText(stuNum + "");
                        //直播的类型
                        TextView iv_imagetype = view.findViewById(R.id.iv_imagetype);
                        iv_imagetype.setText(course_type);
                    }

                    @Override
                    public void onFailure(Call<queryMyCourseListBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage() + "");
                    }
                });
    }

    //queryCourseModelNewsPacket 我的课程包列表请求数据
    public void getMyPacketList() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("stu_id", 1);//学生id
        paramsMap.put("pageNum", 1);//第几页
        paramsMap.put("pageSize", 1);//每页几条
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryCourseModelNewsPacket(body)
                .enqueue(new Callback<MyclassPacketList>() {
             private TextView classPacket_agreement;
             private int agreement_id;
             private double rateOfLearning;
             private int buying_base_number;
             private int courseNum;
             private int stageNum;
             private String cp_name;
             private String cover;
                    @Override
                    public void onResponse(Call<MyclassPacketList> call, Response<MyclassPacketList> response) {
                        MyclassPacketList myclassPacketList = response.body();
                        if (myclassPacketList!=null){
                            if (myclassPacketList.getCode() == 200) {
                                MyclassPacketList.DataBean listData = myclassPacketList.getData();
                                if (listData != null) {
                                    List<MyclassPacketList.DataBean.ListBean> listBeans = listData.getList();
                                    for (int i = 0; i < listBeans.size(); i++) {
                                        cover = listBeans.get(i).getCover();    //封面
                                        stageNum = listBeans.get(i).getStageNum();//阶段数量
                                        int course_package_id = listBeans.get(i).getCourse_package_id();//课程包id
                                        agreement_id = listBeans.get(i).getAgreement_id();//协议id
                                        int stuNum = listBeans.get(i).getStuNum();//学生数量
                                        courseNum = listBeans.get(i).getCourseNum(); //课程数量
                                        cp_name = listBeans.get(i).getCp_name();   //课程包名字
                                        rateOfLearning = listBeans.get(i).getRateOfLearning();//学习进度
                                        buying_base_number = listBeans.get(i).getBuying_base_number();    //购买基数
                                            //数据赋值
                                            initDataMyClassPacket();
                                            //协议判断
//                                            if (String.valueOf(agreement_id).isEmpty()){
//                                                //modelmy_myclasspacket1_agreement     查看协议
//                                                classPacket_agreement = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_agreement);
//                                                classPacket_agreement.setOnClickListener(v -> {
//
//                                                    //点击查看协议
//                                                    Toast.makeText(mControlMainActivity, "我是协议", Toast.LENGTH_SHORT).show();
//                                                });
//                                            }else {
//                                                classPacket_agreement.setVisibility(View.GONE);
//                                            }

                                    }
                                }
                            }
                        }else {
                            Toast.makeText(getActivity(), "code错误", Toast.LENGTH_SHORT).show();
                        }

                    }

                    private void initDataMyClassPacket() {
                        ControllerCustomRoundAngleImageView modelmy_myclasspacket1_cover = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_cover);
                        Glide.with(mControlMainActivity).load(cover).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                                return false;
                            }
                        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(modelmy_myclasspacket1_cover);
                        //modelmy_myclasspacket1_classname   cp_name	string	课程包名字
                        TextView ClassPacket_Nametitle = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_classname);
                        ClassPacket_Nametitle.setText(cp_name);
                        //modelmy_myclasspacket1_stagecount    阶段数量
                        TextView ClassPacket_StageNum = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_stagecount);
                        ClassPacket_StageNum.setText(stageNum+"");
                        //modelmy_myclasspacket1_coursecount  课程数量
                        TextView ClassPacket_CourseNum = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_coursecount);
                        ClassPacket_CourseNum.setText(courseNum+"");
                        //modelmy_myclasspacket1_learnpersoncount  购买基数buying_base_number
                        TextView ClassPacket_buying_base_number = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_learnpersoncount);
                        ClassPacket_buying_base_number.setText(buying_base_number+"");
                        //modelmy_myclasspacket1_learnprogresscount 学习进度rateOfLearning
                        TextView ClassPacket_rateOfLearning = MyOrderShow_MyOrder_view2.findViewById(R.id.modelmy_myclasspacket1_learnprogresscount);
                        ClassPacket_rateOfLearning.setText(rateOfLearning+"");
                    }

                    @Override
                    public void onFailure(Call<MyclassPacketList> call, Throwable t) {
                        Log.e(TAG, "onFailure:我的错误是" + t.getMessage());
                    }
                });
    }
    //我的收藏列表（课程包）
    public void getgetModelMyClassPacketCollection(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("pageNum",Collection_page);//第几页
        paramsMap.put("pageSize",Collection_pagesize);//每页几条
        paramsMap.put("stu_id", 1);//学生id
        paramsMap.put("type", Integer.valueOf("coursepacket"));//课程/课程包
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyCollectionPacketList(body)
                .enqueue(new Callback<queryMyCollectionPacketListBean>() {

                    private int courseNum;
                    private int stuNum;
                    private int stageNum;
                    private String cp_name;
                    private String cover;

                    @Override
                    public void onResponse(Call<queryMyCollectionPacketListBean> call, Response<queryMyCollectionPacketListBean> response) {
                        queryMyCollectionPacketListBean body1 = response.body();
                        int code = body1.getCode();
                        if (code==200){
                            queryMyCollectionPacketListBean.DataBean data = body1.getData();
                            List<queryMyCollectionPacketListBean.DataBean.ListBean> list = data.getList();
                            for (int i = 0; i < list.size(); i++){
                                cover = list.get(i).getCover();//封面
                                int buying_base_number = list.get(i).getBuying_base_number();//购买基数
                                courseNum = list.get(i).getCourseNum();//课程数量
                                int course_package_id = list.get(i).getCourse_package_id();//课程包id
                                cp_name = list.get(i).getCp_name();//课程包名字
                                stageNum = list.get(i).getStageNum();//阶段数量
                                stuNum = list.get(i).getStuNum();    //学生数量
                               initDataPacket();
                            }
                        }
                    }

                    private void initDataPacket() {
                        ControllerCustomRoundAngleImageView modelmy_myclasspacket1_cover = view.findViewById(R.id.modelmy_myclasspacket1_cover);
                        Glide.with(mControlMainActivity).load(cover).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                                return false;
                            }
                        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursepacketcover)).into(modelmy_myclasspacket1_cover);
                         //标题
                        TextView modelmy_myclasspacket1_classname = view.findViewById(R.id.modelmy_myclasspacket1_classname);
                        modelmy_myclasspacket1_classname.setText("");
                        //阶段数量
                        TextView modelmy_myclasspacket1_stagecount = view.findViewById(R.id.modelmy_myclasspacket1_stagecount);
                        modelmy_myclasspacket1_stagecount.setText("");
                        //学习人数
                        TextView modelmy_myclasspacket1_learnpersoncount = view.findViewById(R.id.modelmy_myclasspacket1_learnpersoncount);
                        modelmy_myclasspacket1_learnpersoncount.setText("");
                    }


                    @Override
                    public void onFailure(Call<queryMyCollectionPacketListBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }
    //我的收藏列表（课程）
    public void getModelMyClassCollection(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", Collection_page);//第几页
        paramsMap.put("pageSize",Collection_pagesize);//每页几条
        paramsMap.put("stu_id", 1);//学生id
        paramsMap.put("type", Integer.valueOf("course"));//课程/课程包
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        //queryMyCollectionList
        queryMyCourseList.queryMyCollectionList(body)
                .enqueue(new Callback<queryMyCollectionListBean>() {

                    private String course_type;
                    private int stuNum;
                    private String course_name;
                    private String cover;

                    @Override
                    public void onResponse(Call<queryMyCollectionListBean> call, Response<queryMyCollectionListBean> response) {
                        queryMyCollectionListBean listBean = response.body();
                        int code = listBean.getCode();
                        if (code==200){
                            queryMyCollectionListBean.DataBean data = listBean.getData();
                            if (data!=null){
                                List<queryMyCollectionListBean.DataBean.ListBean> list = data.getList();
                                if (list!=null){
                                    for (int i = 0; i < list.size(); i++){
                                        cover = list.get(i).getCover(); //封面
                                        int course_id = list.get(i).getCourse_id();//课程id
                                        course_type = list.get(i).getCourse_type(); //课程类型
                                        stuNum = list.get(i).getStuNum();   //学生数量
                                        int buying_base_number = list.get(i).getBuying_base_number();//购买基数（没用）
                                        course_name = list.get(i).getCourse_name();//	课程名字
                                        initData();
                                    }
                                }
                            }

                        }else {
                            Toast.makeText(getActivity(), "code码错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void initData() {
                        //加载网络图片封面
                        ControllerCustomRoundAngleImageView modelmy_myclass1_cover = view.findViewById(R.id.modelmy_myclass1_cover);
                        Glide.with(mControlMainActivity).load(cover).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Wain", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Wain", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                                return false;
                            }
                        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(modelmy_myclass1_cover);
                         //封面
                        TextView modelmy_myclass1_classname = view.findViewById(R.id.modelmy_myclass1_classname);
                        modelmy_myclass1_classname.setText(course_name);
                        //stuNum
                        TextView modelmy_myclass1_state = view.findViewById(R.id.modelmy_myclass1_state);
                        modelmy_myclass1_state.setText(stuNum+"");
                        //类型
                        ImageView iv_imagetype = view.findViewById(R.id.iv_imagetype);

                    }

                    @Override
                    public void onFailure(Call<queryMyCollectionListBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage()+"错误是" );
                    }
                });
    }
    //我的消息列表--------删除
    public void getModelMyMessageListDelect(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id","id");//id值
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
       //queryMyMessageDelectList
        queryMyCourseList.queryMyMessageDelectList(body)
                .enqueue(new Callback<MymessageDelectBean>() {
                    @Override
                    public void onResponse(Call<MymessageDelectBean> call, Response<MymessageDelectBean> response) {
                        MymessageDelectBean delectBean = response.body();
                        if (delectBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<MymessageDelectBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }



    //我的消息列表
    public void getModelMyMessageList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();

        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", mControlMainActivity.mToken);//token值
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyMessageList(body)
               .enqueue(new Callback<MymessageBean>() {
                   @Override
                   public void onResponse(Call<MymessageBean> call, Response<MymessageBean> response) {
                       MymessageBean body1 = response.body();
                       if (body1!=null){
                           int code = body1.getCode();
                           if (code==0){
                         //集合的话循环获取值2039-2046
                           }else {
                               Toast.makeText(getActivity(), "code值错误", Toast.LENGTH_SHORT).show();
                           }
                       }else {
                           Toast.makeText(getActivity(), "获取参数体错误", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<MymessageBean> call, Throwable t) {
                   //网络请求失败
                       Log.e(TAG, "onFailure: "+t.getMessage()+"");
                   }
               });
    }
    //我的问答列表(提问)---------删除
    public void getModelMyDelectQuestionList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", "1");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionDelectList(body)
                .enqueue(new Callback<MyQuestionsDelectBean>() {
                    @Override
                    public void onResponse(Call<MyQuestionsDelectBean> call, Response<MyQuestionsDelectBean> response) {
                        MyQuestionsDelectBean delectBean = response.body();
                        if (delectBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<MyQuestionsDelectBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }
    //我的问答列表(回答)---------删除
    public void getModelMyQuestionDelectList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", "3123123");
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyAnswerDelectList(body)
                .enqueue(new Callback<MyAnswerDelectBean>() {
                    @Override
                    public void onResponse(Call<MyAnswerDelectBean> call, Response<MyAnswerDelectBean> response) {
                        MyAnswerDelectBean answerDelectBean = response.body();
                        if (answerDelectBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<MyAnswerDelectBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }

    //我的问答的提问列表
    public void getModelMyQuestionList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();

        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", mControlMainActivity.mToken);//token值
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyQuestionList(body)
               .enqueue(new Callback<MyQuestionsBean>() {
                   @Override
                   public void onResponse(Call<MyQuestionsBean> call, Response<MyQuestionsBean> response) {
                       MyQuestionsBean myQuestionsBean = response.body();
                       //获取网络数据集合for循环
                   }

                   @Override
                   public void onFailure(Call<MyQuestionsBean> call, Throwable t) {
                       Log.e(TAG, "onFailure: "+t.getMessage());
                   }
               });
    }

    //我的问答的回答列表
    public void getModelMyAnswerList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();

        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", mControlMainActivity.mToken);//token值
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyAnswerList(body)
                .enqueue(new Callback<MyAnswerBean>() {
                    @Override
                    public void onResponse(Call<MyAnswerBean> call, Response<MyAnswerBean> response) {
                        MyAnswerBean myAnswerBean = response.body();

                    }

                    @Override
                    public void onFailure(Call<MyAnswerBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: 我的错误是"+t.getMessage());
                    }
                });
    }


    //我的问答（我的提问）
    public static class MyQuestionsBean{

    }
    //我的问答（我的提问）--删除
    public static class MyQuestionsDelectBean{

    }
    //我的问答（我的回答）
    public static class MyAnswerBean{

    }
    //我的消息 单个已读
    public static class  MyASingleBean{

    }
    //我的消息   全部已读
    public static class  MyAllSingleBean{

    }
    //我的问答（我的回答）-删除
    public static class MyAnswerDelectBean{

    }
    //我的数据列表 删除
    public static class MymessageDelectBean{

    }

    //我的消息列表Bean
    public static class MymessageBean{
        private int code;

        public MymessageBean(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "MymessageBean{" +
                    "code=" + code +
                    '}';
        }
    }
    // 我的消息----全部已读
    public void getMyAllSingle(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id","消息的id");//消息的id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyAllSingleList(body)
                .enqueue(new Callback<MymessageBean>() {
                    @Override
                    public void onResponse(Call<MymessageBean> call, Response<MymessageBean> response) {
                        MymessageBean mymessageBean = response.body();
                        if (mymessageBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<MymessageBean> call, Throwable t) {

                    }
                });
    }
    //我的消息---单个已读
    public void getMyASingle(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlPacketHead)
                .build();

        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id","消息的id");//消息的id
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyASingleList(body)
                .enqueue(new Callback<MyASingleBean>() {
                    @Override
                    public void onResponse(Call<MyASingleBean> call, Response<MyASingleBean> response) {
                        MyASingleBean myASingleBean = response.body();
                    }

                    @Override
                    public void onFailure(Call<MyASingleBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }



    //我的协议
    public void getModelMyMeent(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("agreement_id", "agreement_id");//第几页
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryCourseModelNewsAgreeMent(body)
                .enqueue(new Callback<MyClassMeent>() {
                    private String agreement_content;
                    @Override
                    public void onResponse(Call<MyClassMeent> call, Response<MyClassMeent> response) {
                        MyClassMeent classMeent = response.body();
                        if (classMeent.getCode()==200){
                            MyClassMeent.DataBean data = classMeent.getData();
                            agreement_content = data.getAgreement_content();
                            int agreement_id = data.getAgreement_id();
                            initData();
                        }
                    }

                    private void initData() {
                        TextView tv1 = myclass_view.findViewById(R.id.model_agreement_content);
                        tv1.setText(agreement_content);
                    }

                    @Override
                    public void onFailure(Call<MyClassMeent> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });

    }
    //我的订单--取消订单的支付状态
    public void getCancelOrder(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status", "status");//第几页
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyCancelOrderStates(body)
                .enqueue(new Callback<CancelOrder>() {
                    @Override
                    public void onResponse(Call<CancelOrder> call, Response<CancelOrder> response) {
                        CancelOrder cancelOrder = response.body();

                    }

                    @Override
                    public void onFailure(Call<CancelOrder> call, Throwable t) {

                    }
                });

    }
    //订单列表请求网络数据          queryMyPackageOrderList
    public void getModelMyOrderList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", page);//第几页
        paramsMap.put("pageSize",pagesize);//每页几条
        paramsMap.put("stu_id", 1);//学生id
        paramsMap.put("type", 2);//全部/已完成/未完成
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyPackageOrderList(body)
                .enqueue(new Callback<MyOrderlistBean>() {
                    @Override
                    public void onResponse(Call<MyOrderlistBean> call, Response<MyOrderlistBean> response) {
                        MyOrderlistBean orderlistBean = response.body();
                        int code = orderlistBean.getCode();
                        if (code==200){
                            MyOrderlistBean.DataBean data = orderlistBean.getData();
                            List<MyOrderlistBean.DataBean.ListBean> list = data.getList();
                            for (int i = 0; i < list.size(); i++) {
                                mMyOrderListBean = list.get(i);
//                                int order_id = list.get(i).getOrder_id();//	订单id
//                                order_num = list.get(i).getOrder_num();  //订单号
                                String order_status = list.get(i).getOrder_status(); //	订单状态
//                                String order_time = list.get(i).getOrder_time();//订单时间
//                                product_name = list.get(i).getProduct_name(); //产品名称
//                                product_price = list.get(i).getProduct_price(); //产品价格
                                String product_type = list.get(i).getProduct_type();//	产品类型
                                 //判断当前的订单状态
                                if (order_status.isEmpty()){
                                    if (order_status.isEmpty()&&order_status.equals("支付成功")){
                                        initOrderFinish();
                                    }else if (order_status.isEmpty()&&order_status.equals("支付失败")){
                                        initOrderUnFinish();
                                    }else {
                                         initOrderAll();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "订单状态错误", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<MyOrderlistBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    //我的收藏列表(课程)
   public static class queryMyCollectionListBean{
        /**
         * code : 200
         * data : {"total":3,"list":[{"cover":"http://image.yunduoketang.com/course/public/fce05edc-b263-416d-8f59-0836f750cb23.jpg","course_id":2,"course_type":"直播,录播","stuNum":201,"course_name":"测试课程002","buying_base_number":199},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","course_id":4,"course_type":"直播","stuNum":12,"course_name":"江山体验","buying_base_number":11},{"cover":"E:/upload111/1576150503144.png","course_id":5,"course_type":"直播,录播","stuNum":24,"course_name":"1","buying_base_number":23}],"pageNum":1,"pageSize":10,"size":3,"startRow":1,"endRow":3,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 3
             * list : [{"cover":"http://image.yunduoketang.com/course/public/fce05edc-b263-416d-8f59-0836f750cb23.jpg","course_id":2,"course_type":"直播,录播","stuNum":201,"course_name":"测试课程002","buying_base_number":199},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","course_id":4,"course_type":"直播","stuNum":12,"course_name":"江山体验","buying_base_number":11},{"cover":"E:/upload111/1576150503144.png","course_id":5,"course_type":"直播,录播","stuNum":24,"course_name":"1","buying_base_number":23}]
             * pageNum : 1
             * pageSize : 10
             * size : 3
             * startRow : 1
             * endRow : 3
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * lastPage : 1
             * firstPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int lastPage;
            private int firstPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * cover : http://image.yunduoketang.com/course/public/fce05edc-b263-416d-8f59-0836f750cb23.jpg
                 * course_id : 2
                 * course_type : 直播,录播
                 * stuNum : 201
                 * course_name : 测试课程002
                 * buying_base_number : 199
                 */

                private String cover;
                private int course_id;
                private String course_type;
                private int stuNum;
                private String course_name;
                private int buying_base_number;

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getCourse_id() {
                    return course_id;
                }

                public void setCourse_id(int course_id) {
                    this.course_id = course_id;
                }

                public String getCourse_type() {
                    return course_type;
                }

                public void setCourse_type(String course_type) {
                    this.course_type = course_type;
                }

                public int getStuNum() {
                    return stuNum;
                }

                public void setStuNum(int stuNum) {
                    this.stuNum = stuNum;
                }

                public String getCourse_name() {
                    return course_name;
                }

                public void setCourse_name(String course_name) {
                    this.course_name = course_name;
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
    //我的收藏列表(课程包)
    public static class queryMyCollectionPacketListBean{
        /**
         * code : 200
         * data : {"total":1,"list":[{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":0,"course_package_id":14,"stuNum":52,"courseNum":0,"cp_name":"江山11","buying_base_number":50}],"pageNum":1,"pageSize":10,"size":1,"startRow":1,"endRow":1,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 1
             * list : [{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":0,"course_package_id":14,"stuNum":52,"courseNum":0,"cp_name":"江山11","buying_base_number":50}]
             * pageNum : 1
             * pageSize : 10
             * size : 1
             * startRow : 1
             * endRow : 1
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * lastPage : 1
             * firstPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int lastPage;
            private int firstPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * cover : http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png
                 * stageNum : 0
                 * course_package_id : 14
                 * stuNum : 52
                 * courseNum : 0
                 * cp_name : 江山11
                 * buying_base_number : 50
                 */

                private String cover;
                private int stageNum;
                private int course_package_id;
                private int stuNum;
                private int courseNum;
                private String cp_name;
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

                public int getStuNum() {
                    return stuNum;
                }

                public void setStuNum(int stuNum) {
                    this.stuNum = stuNum;
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

                public int getBuying_base_number() {
                    return buying_base_number;
                }

                public void setBuying_base_number(int buying_base_number) {
                    this.buying_base_number = buying_base_number;
                }
            }
        }
    }
    //我的课程列表
    public static class queryMyCourseListBean {
        /**
         * code : 200
         * data : {"total":4,"list":[{"cover":"封面88899","course_id":1,"agreement_id":null,"course_type":"直播","stuNum":53,"course_name":"测试课程","buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/public/fce05edc-b263-416d-8f59-0836f750cb23.jpg","course_id":2,"agreement_id":2,"course_type":"直播,录播","stuNum":201,"course_name":"测试课程002","buying_base_number":199},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","course_id":4,"agreement_id":null,"course_type":"直播","stuNum":12,"course_name":"江山体验","buying_base_number":11},{"cover":"E:/upload111/1576150503144.png","course_id":5,"agreement_id":null,"course_type":"直播,录播","stuNum":24,"course_name":"1","buying_base_number":23}],"pageNum":1,"pageSize":4,"size":4,"startRow":1,"endRow":4,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 4
             * list : [{"cover":"封面88899","course_id":1,"agreement_id":null,"course_type":"直播","stuNum":53,"course_name":"测试课程","buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/public/fce05edc-b263-416d-8f59-0836f750cb23.jpg","course_id":2,"agreement_id":2,"course_type":"直播,录播","stuNum":201,"course_name":"测试课程002","buying_base_number":199},{"cover":"http://image.yunduoketang.com/course/34270/20190829/11c506e7-d6f2-47c6-831c-9d19fa0b5c13.png","course_id":4,"agreement_id":null,"course_type":"直播","stuNum":12,"course_name":"江山体验","buying_base_number":11},{"cover":"E:/upload111/1576150503144.png","course_id":5,"agreement_id":null,"course_type":"直播,录播","stuNum":24,"course_name":"1","buying_base_number":23}]
             * pageNum : 1
             * pageSize : 4
             * size : 4
             * startRow : 1
             * endRow : 4
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * firstPage : 1
             * lastPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int firstPage;
            private int lastPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * cover : 封面88899
                 * course_id : 1
                 * agreement_id : null
                 * course_type : 直播
                 * stuNum : 53
                 * course_name : 测试课程
                 * buying_base_number : 50
                 */

                private String cover;
                private int course_id;
                private Object agreement_id;
                private String course_type;
                private int stuNum;
                private String course_name;
                private int buying_base_number;

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getCourse_id() {
                    return course_id;
                }

                public void setCourse_id(int course_id) {
                    this.course_id = course_id;
                }

                public Object getAgreement_id() {
                    return agreement_id;
                }

                public void setAgreement_id(Object agreement_id) {
                    this.agreement_id = agreement_id;
                }

                public String getCourse_type() {
                    return course_type;
                }

                public void setCourse_type(String course_type) {
                    this.course_type = course_type;
                }

                public int getStuNum() {
                    return stuNum;
                }

                public void setStuNum(int stuNum) {
                    this.stuNum = stuNum;
                }

                public String getCourse_name() {
                    return course_name;
                }

                public void setCourse_name(String course_name) {
                    this.course_name = course_name;
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

    //我的课程包列表
    public static class MyclassPacketList {
        /**
         * code : 200
         * data : {"total":2,"list":[{"cover":"E:/upload111/1576155322131.png","stageNum":10,"course_package_id":1,"agreement_id":4,"stuNum":52,"courseNum":6,"cp_name":"课程包001","rateOfLearning":0.4521,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":1,"course_package_id":14,"agreement_id":null,"stuNum":52,"courseNum":3,"cp_name":"江山11","rateOfLearning":0.4533,"buying_base_number":50}],"pageNum":1,"pageSize":10,"size":2,"startRow":1,"endRow":2,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 2
             * list : [{"cover":"E:/upload111/1576155322131.png","stageNum":10,"course_package_id":1,"agreement_id":4,"stuNum":52,"courseNum":6,"cp_name":"课程包001","rateOfLearning":0.4521,"buying_base_number":50},{"cover":"http://image.yunduoketang.com/course/34270/20190313/ff89e692-6e38-425b-ab0e-1aa88f7ce5d6.png","stageNum":1,"course_package_id":14,"agreement_id":null,"stuNum":52,"courseNum":3,"cp_name":"江山11","rateOfLearning":0.4533,"buying_base_number":50}]
             * pageNum : 1
             * pageSize : 10
             * size : 2
             * startRow : 1
             * endRow : 2
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * lastPage : 1
             * firstPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int lastPage;
            private int firstPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * cover : E:/upload111/1576155322131.png
                 * stageNum : 10
                 * course_package_id : 1
                 * agreement_id : 4
                 * stuNum : 52
                 * courseNum : 6
                 * cp_name : 课程包001
                 * rateOfLearning : 0.4521
                 * buying_base_number : 50
                 */

                private String cover;
                private int stageNum;
                private int course_package_id;
                private int agreement_id;
                private int stuNum;
                private int courseNum;
                private String cp_name;
                private double rateOfLearning;
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

                public int getAgreement_id() {
                    return agreement_id;
                }

                public void setAgreement_id(int agreement_id) {
                    this.agreement_id = agreement_id;
                }

                public int getStuNum() {
                    return stuNum;
                }

                public void setStuNum(int stuNum) {
                    this.stuNum = stuNum;
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

                public double getRateOfLearning() {
                    return rateOfLearning;
                }

                public void setRateOfLearning(double rateOfLearning) {
                    this.rateOfLearning = rateOfLearning;
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
    //我的课程协议和我的课程包协议     queryCourseModelNewsAgreeMent
    public static  class MyClassMeent{
        /**
         * code : 200
         * data : {"agreement_id":1,"agreement_content":"\r\n学员\r\n \r\n课程\r\n \r\n教学\r\n \r\n运营\r\n \r\n题库\r\n \r\n资源\r\n \r\n财务\r\n \r\n数据\r\n \r\n系统\r\n \r\n帮助\r\n \r\n默认分校 秦羽\r\n \r\n退出\r\n资源\r\n学科\r\n \r\n校区\r\n \r\n教室\r\n \r\n资源库\r\n \r\n课程协议\r\n \r\n教师课时\r\n \r\n课程单元\r\n查看详情\r\n账号\r\n商品名称\r\n火种教育一级注册消防工程师考试【定金班】协议（20191015版）\r\n签署终端\r\n签署时间\r\n一级注册消防工程师考试学习服务协议【定金班】\r\n一级注册消防工程师考试学习服务协议【定金班】\r\n\r\n\u2014\u2014帮助您在火种教育更好地完成学业和收获能力\r\n\r\n亲爱的学员，您好！\r\n\r\n火种教育全体同仁欢迎您选择在这里完成学业并考取证书。\r\n\r\n在您享受课程的过程中，以下守则的遵守和共识的达成，对我们的服务是否及时和有效至关重要。\r\n\r\n请您仔细阅读并签字确认，感谢您的理解和配合。\r\n\r\n一、班级化管理准则\r\n\r\n关于校区：学校以网络直播的授课方式为主，可以随时听课，请大家自我约束并主动学习，请勿自误。直播课堂的学习需要在学员的电脑里安装专用软件，由校方教学运营专员协助学员安装。当您完成报名、培训学习费用缴纳后、学校将向学员发放直播课的会员权限。该权限只允许学员一人使用，不得转借或转让他人。一经发现，学校有权取消该学员听课资格，不承担任何违约责任。\r\n\r\n校方联系方式:     咨询服务微信：[huozhongbanzhuren    ]\r\n\r\n咨询服务邮箱：huozhongedu    @126.com\r\n\r\n                   联系电话：\r\n\r\n                                01060703671 服务时间：09:00-12:00,  13:00-18:00\r\n                                01081705189 服务时间：13:00-18:00,  19:00-22:00\r\n\r\n二、学校提供的产品和服务\r\n\r\n1、在线直播主课程：全年滚动上课\r\n\r\n2、网课：直播结束后上传，可供随时观看\r\n\r\n3、在线答疑（客服类问题）：微信解决学员操作类疑问\r\n\r\n4、1对1私人助教（学习类问题）：教研老师在规定时间内解决\r\n\r\n5、跟踪管理：跟踪学员学习情况，考试情况的定期电话/微信/QQ回访\r\n\r\n6、模拟考试：题库系统模考答题及解析\r\n\r\n7、通关宝典：历年考试重难点浓缩精华\r\n\r\n8、考前答疑：考试前夕名师直播解答学员疑问\r\n\r\n9、模拟卷在学员按规定支付培训费用后由学校邮寄给学员，邮费由学校承担。\r\n\r\n10、学校为学员提供的服务期限从报名之日起至2020年一级注册消防工程师考试结束当天24点止。若学员在服务期限内未通过考试，并符合重修条件的按本协议约定执行。\r\n\r\n三、课程管理准则\r\n\r\n1．开课：学员所报课程在学校安排的第一次上课时间即为开课日期，插班学员款到日期即为开课日期，插班学员可自行补足开课前的课程；在无特殊情况下我们会严格依照原定开课计划履行课程服务。由于各种意外情况或不可抗力导致的无法按时开课的情况，我们将以微信等方式通知学员，后期会安排补课，具体开课时间另行通知。\r\n\r\n2．确认课程安排：学员应及时主动接收微信并查看班主任通知的课表，上课时间及地点，按通知的信息上课。学员因事、病等自身原因不能前来上课，需至少提前6小时请假，请假的学员自行从学校网站观看课程。每个学员在培训期间请假的次数不能超过当期课程的30%。\r\n\r\n3．上课纪律：上课时请遵守课堂纪律。如有疑问，可课下咨询班主任或老师。如学员严重干扰课堂纪律，影响他人上课，班主任有权终止该学员继续听课。该情形达到3次，学校有权取消学员听课资格，学校不承担任何违约责任。\r\n\r\n4．课程有效性：学员在报名时选择相应的考期，考期结束后，学校会关停学员学习账号。若学员通过报名表中报名科目，则本协议终止。若学员未通过此次考试，则学员需向学校申请重修。\r\n\r\n四、缴费标准及方式\r\n\r\n学员应在本协议签订前支付培训课程定金费用，剩余培训费用学员应在收到甲方的协议和定金班课程后2日内付完全款，定金一经缴纳，无论是否补费均不予退还。缴费方式参考报名表。\r\n\r\n五、关于不过退费与延时重修\r\n\r\n延时重修：报名本协议约定班型的学员可免费享受两次延时重修的机会，即学员 2020年参加考试未通过，2021年延时重修（在线课程、网上题库免费，其他资料费用学校另行收取），2021年参加考试仍未通过，2022年还可以延时重修（在线课程、网上题库免费，其他资料费用学校另行收取）。若2022年考试仍未通过，学员想继续学习的，则该学员应重新缴纳足额的课程费用方可继续学习。\r\n\r\n延时重修是学校赠予的课程服务，如果学员因自身原因或不可抗力导致不能重修的，学校不承担任何违约责任，也不予退还任何费用。若学员在重修期间通过考试的，学员不得要求学校继续提供相关的课程服务。因延时重修不等同于重新报名，故延时重修期间不计入前述的服务期限。\r\n\r\n延时重修仅适用于以下班型：基础精讲班、一次取证班、签约取证班。\r\n\r\n不过退费：本协议生效后，学员经过服务期内一次考试及延时重修期间两次考试，共计三次考试均未缺考一级注册消防工程师考试，且每次考试三科成绩均大于0分且均低于合格分，则培训费用全额退还。\r\n\r\n不过退费条款将于2022年一级注册消防工程师考试结果公布后生效，学员将三年考试成绩发送给班主任向学校申请退费。经学校审核学员满足退费条件的，学校按退费约定退还学员费用。\r\n\r\n不过退费仅适用于以下班型：一次取证班、签约取证班。\r\n\r\n不同班型的不过退费金额不一致，具体金额根据补费班型确定。\r\n\r\n退费时，如已开具发票，必须退回完整发票。（一旦奖区联被撕毁或丢失，根据税务局相关规定将扣除相应税费）\r\n\r\n六、考过培训费用全额奖励\r\n\r\n考过培训费用全额奖励：报名本协议约定班型的学员可享受考过培训费用全额奖励，即学员参加2020年一级注册消防工程师考试，3科成绩均高于合格分，可申请培训费用全额奖励。学员须将考试成绩发给学校审核，学校审核确认后按约定退还学员培训费用作为奖励。\r\n\r\n考过培训费用全额奖励仅适用于以下班型：一次取证班、签约取证班。\r\n\r\n七、关于转班\r\n\r\n学员开课后7天内（含第七天），可以免费换为同等价格的其他课程；超过7天到不超过30天的，需要缴纳新课程的费用的10%作为手续费；超过30天但不超过60天的，需要缴纳新课程的费用的20%作为手续费；超过60天但不超过90天的，需要缴纳新课程的费用的30%作为手续费；超过90天但不超过120天的，需要缴纳新课程的费用的40%作为手续费；超过120天后学员不可以更换课程。更换课程不影响课程的有效期，即更换新课程后服务期仍从初始班名班型开始计算服务期。\r\n\r\n八、关于冻结课程\r\n\r\n学员每次选定考期后，所选当期考期考试第一天（算作第一天）向前推30天（含第30天）之内不允许冻结课程，31天向前可以冻结，冻结期限最长为3个月，拥有一次冻结机会。\r\n\r\n九、关于升班\r\n\r\n学员报名后随时可以进行班型升级，升级时需要补齐新班型与老班型之前的差价，服务期从新班型差价补齐开始计算；升级班型后将签订新的协议，本协议自动作废。\r\n\r\n十、信息变更准则\r\n\r\n如有任何授课信息的变动，校方会以QQ/微信等形式通知各位学员，重要通知我们也会通过电话等方式提前6小时告知学员。请学员保证预留的联系方式准确无误。如学员的联系方式变更，请及时致电班主任进行修改确认。否则由该学员承担未及时通知的后果。\r\n\r\n十一、注意事项\r\n\r\n1、为防止内部讲课视频流出和密训题泄露，严禁学员私下建立QQ和微信群，一经发现取消听课资格，有任何事情随时联系学员的班主任，班主任会一对一为学员解答。\r\n\r\n2、学校向学员发出的任何通知均可通过学员预留的任一联系方式发出，学员应及时收悉并回复确认，未及时确认的，通知发出后3小时视为送达。\r\n\r\n3、学员应对学校的商业信息（包括但不限于内部视频讲课、密训题、上课内容、教材等）负有保密义务。未经学校书面同意学员不得泄露或用于其他用途，否则学校有权要求学员支付违约金10000元。\r\n\r\n4、因学员自身原因被取消听课资格或学员自己提出终止听课的，学校不退还学员任何费用。\r\n\r\n5、因不可抗拒（如自然灾害、政府行为、征收、征用、罢工、骚乱等）等特殊情形导致无法完成课程，出现学员退费情形的，学校按学员剩余课时退还学员报名费用。\r\n\r\n6、学员不得私自与班主任达成协议，以金钱或其他方式要求班主任为其上课或私自提供课程资料。一经发现，学校有权取消该学员听课资格并不予退还报名费。\r\n\r\n7、任何一方对本协议的内容或履行有争议的，应协商解决。协商不成任何一方向学校所在地法院起诉。\r\n\r\n8、本协议自学校盖章、学员签字后生效。本协议一式两份，学校、学员各持一份。\r\n\r\n"}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * agreement_id : 1
             * agreement_content :
             学员

             课程

             教学

             运营

             题库

             资源

             财务

             数据

             系统

             帮助

             默认分校 秦羽

             退出
             资源
             学科

             校区

             教室

             资源库

             课程协议

             教师课时

             课程单元
             查看详情
             账号
             商品名称
             火种教育一级注册消防工程师考试【定金班】协议（20191015版）
             签署终端
             签署时间
             一级注册消防工程师考试学习服务协议【定金班】
             一级注册消防工程师考试学习服务协议【定金班】

             ——帮助您在火种教育更好地完成学业和收获能力

             亲爱的学员，您好！

             火种教育全体同仁欢迎您选择在这里完成学业并考取证书。

             在您享受课程的过程中，以下守则的遵守和共识的达成，对我们的服务是否及时和有效至关重要。

             请您仔细阅读并签字确认，感谢您的理解和配合。

             一、班级化管理准则

             关于校区：学校以网络直播的授课方式为主，可以随时听课，请大家自我约束并主动学习，请勿自误。直播课堂的学习需要在学员的电脑里安装专用软件，由校方教学运营专员协助学员安装。当您完成报名、培训学习费用缴纳后、学校将向学员发放直播课的会员权限。该权限只允许学员一人使用，不得转借或转让他人。一经发现，学校有权取消该学员听课资格，不承担任何违约责任。

             校方联系方式:     咨询服务微信：[huozhongbanzhuren    ]

             咨询服务邮箱：huozhongedu    @126.com

             联系电话：

             01060703671 服务时间：09:00-12:00,  13:00-18:00
             01081705189 服务时间：13:00-18:00,  19:00-22:00

             二、学校提供的产品和服务

             1、在线直播主课程：全年滚动上课

             2、网课：直播结束后上传，可供随时观看

             3、在线答疑（客服类问题）：微信解决学员操作类疑问

             4、1对1私人助教（学习类问题）：教研老师在规定时间内解决

             5、跟踪管理：跟踪学员学习情况，考试情况的定期电话/微信/QQ回访

             6、模拟考试：题库系统模考答题及解析

             7、通关宝典：历年考试重难点浓缩精华

             8、考前答疑：考试前夕名师直播解答学员疑问

             9、模拟卷在学员按规定支付培训费用后由学校邮寄给学员，邮费由学校承担。

             10、学校为学员提供的服务期限从报名之日起至2020年一级注册消防工程师考试结束当天24点止。若学员在服务期限内未通过考试，并符合重修条件的按本协议约定执行。

             三、课程管理准则

             1．开课：学员所报课程在学校安排的第一次上课时间即为开课日期，插班学员款到日期即为开课日期，插班学员可自行补足开课前的课程；在无特殊情况下我们会严格依照原定开课计划履行课程服务。由于各种意外情况或不可抗力导致的无法按时开课的情况，我们将以微信等方式通知学员，后期会安排补课，具体开课时间另行通知。

             2．确认课程安排：学员应及时主动接收微信并查看班主任通知的课表，上课时间及地点，按通知的信息上课。学员因事、病等自身原因不能前来上课，需至少提前6小时请假，请假的学员自行从学校网站观看课程。每个学员在培训期间请假的次数不能超过当期课程的30%。

             3．上课纪律：上课时请遵守课堂纪律。如有疑问，可课下咨询班主任或老师。如学员严重干扰课堂纪律，影响他人上课，班主任有权终止该学员继续听课。该情形达到3次，学校有权取消学员听课资格，学校不承担任何违约责任。

             4．课程有效性：学员在报名时选择相应的考期，考期结束后，学校会关停学员学习账号。若学员通过报名表中报名科目，则本协议终止。若学员未通过此次考试，则学员需向学校申请重修。

             四、缴费标准及方式

             学员应在本协议签订前支付培训课程定金费用，剩余培训费用学员应在收到甲方的协议和定金班课程后2日内付完全款，定金一经缴纳，无论是否补费均不予退还。缴费方式参考报名表。

             五、关于不过退费与延时重修

             延时重修：报名本协议约定班型的学员可免费享受两次延时重修的机会，即学员 2020年参加考试未通过，2021年延时重修（在线课程、网上题库免费，其他资料费用学校另行收取），2021年参加考试仍未通过，2022年还可以延时重修（在线课程、网上题库免费，其他资料费用学校另行收取）。若2022年考试仍未通过，学员想继续学习的，则该学员应重新缴纳足额的课程费用方可继续学习。

             延时重修是学校赠予的课程服务，如果学员因自身原因或不可抗力导致不能重修的，学校不承担任何违约责任，也不予退还任何费用。若学员在重修期间通过考试的，学员不得要求学校继续提供相关的课程服务。因延时重修不等同于重新报名，故延时重修期间不计入前述的服务期限。

             延时重修仅适用于以下班型：基础精讲班、一次取证班、签约取证班。

             不过退费：本协议生效后，学员经过服务期内一次考试及延时重修期间两次考试，共计三次考试均未缺考一级注册消防工程师考试，且每次考试三科成绩均大于0分且均低于合格分，则培训费用全额退还。

             不过退费条款将于2022年一级注册消防工程师考试结果公布后生效，学员将三年考试成绩发送给班主任向学校申请退费。经学校审核学员满足退费条件的，学校按退费约定退还学员费用。

             不过退费仅适用于以下班型：一次取证班、签约取证班。

             不同班型的不过退费金额不一致，具体金额根据补费班型确定。

             退费时，如已开具发票，必须退回完整发票。（一旦奖区联被撕毁或丢失，根据税务局相关规定将扣除相应税费）

             六、考过培训费用全额奖励

             考过培训费用全额奖励：报名本协议约定班型的学员可享受考过培训费用全额奖励，即学员参加2020年一级注册消防工程师考试，3科成绩均高于合格分，可申请培训费用全额奖励。学员须将考试成绩发给学校审核，学校审核确认后按约定退还学员培训费用作为奖励。

             考过培训费用全额奖励仅适用于以下班型：一次取证班、签约取证班。

             七、关于转班

             学员开课后7天内（含第七天），可以免费换为同等价格的其他课程；超过7天到不超过30天的，需要缴纳新课程的费用的10%作为手续费；超过30天但不超过60天的，需要缴纳新课程的费用的20%作为手续费；超过60天但不超过90天的，需要缴纳新课程的费用的30%作为手续费；超过90天但不超过120天的，需要缴纳新课程的费用的40%作为手续费；超过120天后学员不可以更换课程。更换课程不影响课程的有效期，即更换新课程后服务期仍从初始班名班型开始计算服务期。

             八、关于冻结课程

             学员每次选定考期后，所选当期考期考试第一天（算作第一天）向前推30天（含第30天）之内不允许冻结课程，31天向前可以冻结，冻结期限最长为3个月，拥有一次冻结机会。

             九、关于升班

             学员报名后随时可以进行班型升级，升级时需要补齐新班型与老班型之前的差价，服务期从新班型差价补齐开始计算；升级班型后将签订新的协议，本协议自动作废。

             十、信息变更准则

             如有任何授课信息的变动，校方会以QQ/微信等形式通知各位学员，重要通知我们也会通过电话等方式提前6小时告知学员。请学员保证预留的联系方式准确无误。如学员的联系方式变更，请及时致电班主任进行修改确认。否则由该学员承担未及时通知的后果。

             十一、注意事项

             1、为防止内部讲课视频流出和密训题泄露，严禁学员私下建立QQ和微信群，一经发现取消听课资格，有任何事情随时联系学员的班主任，班主任会一对一为学员解答。

             2、学校向学员发出的任何通知均可通过学员预留的任一联系方式发出，学员应及时收悉并回复确认，未及时确认的，通知发出后3小时视为送达。

             3、学员应对学校的商业信息（包括但不限于内部视频讲课、密训题、上课内容、教材等）负有保密义务。未经学校书面同意学员不得泄露或用于其他用途，否则学校有权要求学员支付违约金10000元。

             4、因学员自身原因被取消听课资格或学员自己提出终止听课的，学校不退还学员任何费用。

             5、因不可抗拒（如自然灾害、政府行为、征收、征用、罢工、骚乱等）等特殊情形导致无法完成课程，出现学员退费情形的，学校按学员剩余课时退还学员报名费用。

             6、学员不得私自与班主任达成协议，以金钱或其他方式要求班主任为其上课或私自提供课程资料。一经发现，学校有权取消该学员听课资格并不予退还报名费。

             7、任何一方对本协议的内容或履行有争议的，应协商解决。协商不成任何一方向学校所在地法院起诉。

             8、本协议自学校盖章、学员签字后生效。本协议一式两份，学校、学员各持一份。


             */

            private int agreement_id;
            private String agreement_content;

            public int getAgreement_id() {
                return agreement_id;
            }

            public void setAgreement_id(int agreement_id) {
                this.agreement_id = agreement_id;
            }

            public String getAgreement_content() {
                return agreement_content;
            }

            public void setAgreement_content(String agreement_content) {
                this.agreement_content = agreement_content;
            }
        }
    }
    //我的订单--取消订单
    public static class CancelOrder{

    }


    //我的订单列表
    public static class MyOrderlistBean{
        /**
         * code : 200
         * data : {"total":6,"list":[{"order_status":"支付成功","product_type":"课程包","order_num":"20190920105823","order_time":"2019-09-20T10:58:40.000+0800","product_price":1,"order_id":1,"product_name":"课程包001"},{"order_status":"支付成功","product_type":"课程包","order_num":"20190920111237","order_time":"2019-09-20T11:12:37.000+0800","product_price":1,"order_id":2,"product_name":"课程包002"},{"order_status":"支付成功","product_type":"课程","order_num":"1573527488150","order_time":"2019-11-12T10:58:08.000+0800","product_price":66,"order_id":51,"product_name":"测试课程11"},{"order_status":"未支付","product_type":"课程包","order_num":"1576232505724","order_time":"2019-12-13T18:21:45.000+0800","product_price":111,"order_id":56,"product_name":"江山11"},{"order_status":"支付成功","product_type":"课程","order_num":"S1C1T1577445735963","order_time":"2019-12-27T19:22:15.000+0800","product_price":66,"order_id":63,"product_name":"测试课程"},{"order_status":"支付成功","product_type":"课程","order_num":"S1C1T1577448266676","order_time":"2019-12-27T20:04:26.000+0800","product_price":66,"order_id":64,"product_name":"测试课程"}],"pageNum":1,"pageSize":10,"size":6,"startRow":1,"endRow":6,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 6
             * list : [{"order_status":"支付成功","product_type":"课程包","order_num":"20190920105823","order_time":"2019-09-20T10:58:40.000+0800","product_price":1,"order_id":1,"product_name":"课程包001"},{"order_status":"支付成功","product_type":"课程包","order_num":"20190920111237","order_time":"2019-09-20T11:12:37.000+0800","product_price":1,"order_id":2,"product_name":"课程包002"},{"order_status":"支付成功","product_type":"课程","order_num":"1573527488150","order_time":"2019-11-12T10:58:08.000+0800","product_price":66,"order_id":51,"product_name":"测试课程11"},{"order_status":"未支付","product_type":"课程包","order_num":"1576232505724","order_time":"2019-12-13T18:21:45.000+0800","product_price":111,"order_id":56,"product_name":"江山11"},{"order_status":"支付成功","product_type":"课程","order_num":"S1C1T1577445735963","order_time":"2019-12-27T19:22:15.000+0800","product_price":66,"order_id":63,"product_name":"测试课程"},{"order_status":"支付成功","product_type":"课程","order_num":"S1C1T1577448266676","order_time":"2019-12-27T20:04:26.000+0800","product_price":66,"order_id":64,"product_name":"测试课程"}]
             * pageNum : 1
             * pageSize : 10
             * size : 6
             * startRow : 1
             * endRow : 6
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * firstPage : 1
             * lastPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int firstPage;
            private int lastPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * order_status : 支付成功
                 * product_type : 课程包
                 * order_num : 20190920105823
                 * order_time : 2019-09-20T10:58:40.000+0800
                 * product_price : 1
                 * order_id : 1
                 * product_name : 课程包001
                 */

                private String order_status;
                private String product_type;
                private String order_num;
                private String order_time;
                private int product_price;
                private int order_id;
                private String product_name;

                public String getOrder_status() {
                    return order_status;
                }

                public void setOrder_status(String order_status) {
                    this.order_status = order_status;
                }

                public String getProduct_type() {
                    return product_type;
                }

                public void setProduct_type(String product_type) {
                    this.product_type = product_type;
                }

                public String getOrder_num() {
                    return order_num;
                }

                public void setOrder_num(String order_num) {
                    this.order_num = order_num;
                }

                public String getOrder_time() {
                    return order_time;
                }

                public void setOrder_time(String order_time) {
                    this.order_time = order_time;
                }

                public int getProduct_price() {
                    return product_price;
                }

                public void setProduct_price(int product_price) {
                    this.product_price = product_price;
                }

                public int getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(int order_id) {
                    this.order_id = order_id;
                }

                public String getProduct_name() {
                    return product_name;
                }

                public void setProduct_name(String product_name) {
                    this.product_name = product_name;
                }
            }
        }

    }
    //我的优惠券列表网络请求
    public void getMyCouponList(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", String.valueOf(MyCoupon_pageNum));//第几页
        paramsMap.put("pageSize", String.valueOf(MyCoupon_pageSize));//每页几条
        paramsMap.put("stu_id", String.valueOf(1));//学生id
        paramsMap.put("type", pageSize_type);//未使用/已使用/已过期
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.getMyCouponListMessage(body)
                .enqueue(new Callback<MyCoupon>() {
                    @Override
                    public void onResponse(Call<MyCoupon> call, Response<MyCoupon> response) {
                        MyCoupon myCoupon = response.body();
                        int code = myCoupon.getCode();
                        if (code==200){
                            MyCoupon.DataBean data = myCoupon.getData();
                            List<MyCoupon.DataBean.ListBean> list = data.getList();
                            for (int i = 0; i < list.size(); i++){
                                Object subject_id = list.get(i).getSubject_id();//科目id
                                String dc_denomination = list.get(i).getDc_denomination();//优惠大小
                                int course_id = list.get(i).getCourse_id();//课程ID
                                String service_life_end_time = list.get(i).getService_life_end_time();//优惠券结束时间
                                String service_life_start_time = list.get(i).getService_life_start_time();//优惠券开始时间
                                String preferential_way = list.get(i).getPreferential_way();//优惠类型
                                String small_discount_status = list.get(i).getSmall_discount_status();//优惠券状态   已领取
                                String product_type = list.get(i).getProduct_type();//优惠券类型
                                Object course_package_id = list.get(i).getCourse_package_id();//课程包id
                                Object project_id = list.get(i).getProject_id();//项目id
                                int small_discount_id = list.get(i).getSmall_discount_id();//这个优惠券的id
                                String preferential_scope = list.get(i).getPreferential_scope();//优惠范围
                              if (preferential_way.equals("打折")){
                                  discountcoupon();
                              }else if (product_type.equals("抵现")){
                                  withstandcoupon();
                              }else {
                                  //减少满券
                                  lessenfull();
                              }

                            }
                        }else {
                            Log.d(TAG, "onResponse:MissCode is "+code);
                        }
                    }
                    //减满券
                    private void lessenfull() {
                        TextView modelmy_mycoupon1_couponprice = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponprice);//价格
                        modelmy_mycoupon1_couponprice.setText("200");//优惠券的价格
                        TextView modelmy_mycoupon1_couponfullreduction = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);//优惠券
                        modelmy_mycoupon1_couponfullreduction.setText("满减券");//优惠券的类型
                        TextView modelmy_mycoupon1_termofvaliditydata = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);//有效期
                        modelmy_mycoupon1_termofvaliditydata.setText("2020.01.13");//时间
                        TextView modelmy_mycoupon1_couponrequire = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_couponrequire);//检满价格
                        modelmy_mycoupon1_couponrequire.setText("满10元就可以使用");//满价格优惠
                        TextView modelmy_mycoupon1_areaofapplication = lessenfull_view1.findViewById(R.id.modelmy_mycoupon1_areaofapplication);//使用范围
                        modelmy_mycoupon1_areaofapplication.setText("all");//使用的范围
                    }
                     //抵现券
                    private void withstandcoupon() {
                        TextView modelmy_mycoupon1_couponprice = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponprice);//价格
                        modelmy_mycoupon1_couponprice.setText("200");
                        TextView modelmy_mycoupon1_couponfullreduction = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);//优惠券
                        modelmy_mycoupon1_couponfullreduction.setText("抵现券");
                        TextView modelmy_mycoupon1_termofvaliditydata = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);//有效期
                        modelmy_mycoupon1_termofvaliditydata.setText("2020.01.13");
                        TextView modelmy_mycoupon1_couponrequire = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_couponrequire);//检满价格
                        modelmy_mycoupon1_couponrequire.setText("满20元就可以使用");
                        TextView modelmy_mycoupon1_areaofapplication = withstandcoupon_view2.findViewById(R.id.modelmy_mycoupon1_areaofapplication);//使用范围
                        modelmy_mycoupon1_areaofapplication.setText("all");

                    }

                    //打折券赋值
                    private void discountcoupon() {
                        TextView modelmy_mycoupon1_couponprice = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponprice);//价格
                        modelmy_mycoupon1_couponprice.setText("200");
                        TextView modelmy_mycoupon1_couponfullreduction = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponfullreduction);//优惠券
                        modelmy_mycoupon1_couponfullreduction.setText("打折券");
                        TextView modelmy_mycoupon1_termofvaliditydata = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_termofvaliditydata);//有效期
                        modelmy_mycoupon1_termofvaliditydata.setText("2020.01.13");
                        TextView modelmy_mycoupon1_couponrequire = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_couponrequire);//检满价格
                        modelmy_mycoupon1_couponrequire.setText("满30元就可以使用");
                        TextView modelmy_mycoupon1_areaofapplication = discountcoupon_view3.findViewById(R.id.modelmy_mycoupon1_areaofapplication);//使用范围
                        modelmy_mycoupon1_areaofapplication.setText("all");
                    }

                    @Override
                    public void onFailure(Call<MyCoupon> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage()+"" );
                    }
                });
    }


    //我的优惠券列表
    public static class MyCoupon{
        /**
         * code : 200
         * data : {"total":1,"list":[{"subject_id":null,"dc_denomination":"0.02","course_id":0,"service_life_start_time":"2019-10-30","preferential_way":"抵现","stu_id":1,"small_discount_status":"已领取","使用范围":"可用于所有课程","product_type":"课程","course_package_id":null,"project_id":null,"service_life_end_time":"2019-11-01","small_discount_id":9,"preferential_scope":"全部课程"}],"pageNum":1,"pageSize":10,"size":1,"startRow":1,"endRow":1,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1}
         */

        private int code;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * total : 1
             * list : [{"subject_id":null,"dc_denomination":"0.02","course_id":0,"service_life_start_time":"2019-10-30","preferential_way":"抵现","stu_id":1,"small_discount_status":"已领取","使用范围":"可用于所有课程","product_type":"课程","course_package_id":null,"project_id":null,"service_life_end_time":"2019-11-01","small_discount_id":9,"preferential_scope":"全部课程"}]
             * pageNum : 1
             * pageSize : 10
             * size : 1
             * startRow : 1
             * endRow : 1
             * pages : 1
             * prePage : 0
             * nextPage : 0
             * isFirstPage : true
             * isLastPage : true
             * hasPreviousPage : false
             * hasNextPage : false
             * navigatePages : 8
             * navigatepageNums : [1]
             * navigateFirstPage : 1
             * navigateLastPage : 1
             * firstPage : 1
             * lastPage : 1
             */

            private int total;
            private int pageNum;
            private int pageSize;
            private int size;
            private int startRow;
            private int endRow;
            private int pages;
            private int prePage;
            private int nextPage;
            private boolean isFirstPage;
            private boolean isLastPage;
            private boolean hasPreviousPage;
            private boolean hasNextPage;
            private int navigatePages;
            private int navigateFirstPage;
            private int navigateLastPage;
            private int firstPage;
            private int lastPage;
            private List<ListBean> list;
            private List<Integer> navigatepageNums;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public int getEndRow() {
                return endRow;
            }

            public void setEndRow(int endRow) {
                this.endRow = endRow;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getPrePage() {
                return prePage;
            }

            public void setPrePage(int prePage) {
                this.prePage = prePage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public boolean isIsFirstPage() {
                return isFirstPage;
            }

            public void setIsFirstPage(boolean isFirstPage) {
                this.isFirstPage = isFirstPage;
            }

            public boolean isIsLastPage() {
                return isLastPage;
            }

            public void setIsLastPage(boolean isLastPage) {
                this.isLastPage = isLastPage;
            }

            public boolean isHasPreviousPage() {
                return hasPreviousPage;
            }

            public void setHasPreviousPage(boolean hasPreviousPage) {
                this.hasPreviousPage = hasPreviousPage;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNavigatePages() {
                return navigatePages;
            }

            public void setNavigatePages(int navigatePages) {
                this.navigatePages = navigatePages;
            }

            public int getNavigateFirstPage() {
                return navigateFirstPage;
            }

            public void setNavigateFirstPage(int navigateFirstPage) {
                this.navigateFirstPage = navigateFirstPage;
            }

            public int getNavigateLastPage() {
                return navigateLastPage;
            }

            public void setNavigateLastPage(int navigateLastPage) {
                this.navigateLastPage = navigateLastPage;
            }

            public int getFirstPage() {
                return firstPage;
            }

            public void setFirstPage(int firstPage) {
                this.firstPage = firstPage;
            }

            public int getLastPage() {
                return lastPage;
            }

            public void setLastPage(int lastPage) {
                this.lastPage = lastPage;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<Integer> getNavigatepageNums() {
                return navigatepageNums;
            }

            public void setNavigatepageNums(List<Integer> navigatepageNums) {
                this.navigatepageNums = navigatepageNums;
            }

            public static class ListBean {
                /**
                 * subject_id : null
                 * dc_denomination : 0.02
                 * course_id : 0
                 * service_life_start_time : 2019-10-30
                 * preferential_way : 抵现
                 * stu_id : 1
                 * small_discount_status : 已领取
                 * 使用范围 : 可用于所有课程
                 * product_type : 课程
                 * course_package_id : null
                 * project_id : null
                 * service_life_end_time : 2019-11-01
                 * small_discount_id : 9
                 * preferential_scope : 全部课程
                 */

                private Object subject_id;
                private String dc_denomination;
                private int course_id;
                private String service_life_start_time;
                private String preferential_way;
                private int stu_id;
                private String small_discount_status;
                private String 使用范围;
                private String product_type;
                private Object course_package_id;
                private Object project_id;
                private String service_life_end_time;
                private int small_discount_id;
                private String preferential_scope;

                public Object getSubject_id() {
                    return subject_id;
                }

                public void setSubject_id(Object subject_id) {
                    this.subject_id = subject_id;
                }

                public String getDc_denomination() {
                    return dc_denomination;
                }

                public void setDc_denomination(String dc_denomination) {
                    this.dc_denomination = dc_denomination;
                }

                public int getCourse_id() {
                    return course_id;
                }

                public void setCourse_id(int course_id) {
                    this.course_id = course_id;
                }

                public String getService_life_start_time() {
                    return service_life_start_time;
                }

                public void setService_life_start_time(String service_life_start_time) {
                    this.service_life_start_time = service_life_start_time;
                }

                public String getPreferential_way() {
                    return preferential_way;
                }

                public void setPreferential_way(String preferential_way) {
                    this.preferential_way = preferential_way;
                }

                public int getStu_id() {
                    return stu_id;
                }

                public void setStu_id(int stu_id) {
                    this.stu_id = stu_id;
                }

                public String getSmall_discount_status() {
                    return small_discount_status;
                }

                public void setSmall_discount_status(String small_discount_status) {
                    this.small_discount_status = small_discount_status;
                }

                public String get使用范围() {
                    return 使用范围;
                }

                public void set使用范围(String 使用范围) {
                    this.使用范围 = 使用范围;
                }

                public String getProduct_type() {
                    return product_type;
                }

                public void setProduct_type(String product_type) {
                    this.product_type = product_type;
                }

                public Object getCourse_package_id() {
                    return course_package_id;
                }

                public void setCourse_package_id(Object course_package_id) {
                    this.course_package_id = course_package_id;
                }

                public Object getProject_id() {
                    return project_id;
                }

                public void setProject_id(Object project_id) {
                    this.project_id = project_id;
                }

                public String getService_life_end_time() {
                    return service_life_end_time;
                }

                public void setService_life_end_time(String service_life_end_time) {
                    this.service_life_end_time = service_life_end_time;
                }

                public int getSmall_discount_id() {
                    return small_discount_id;
                }

                public void setSmall_discount_id(int small_discount_id) {
                    this.small_discount_id = small_discount_id;
                }

                public String getPreferential_scope() {
                    return preferential_scope;
                }

                public void setPreferential_scope(String preferential_scope) {
                    this.preferential_scope = preferential_scope;
                }
            }
        }
    }
}
