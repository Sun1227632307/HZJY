package com.android.hzjy.hzjyproduct;

import java.util.ArrayList;
import java.util.List;

//课程信息结构体
public class CourseInfo {
    String mCourseCover = "";//课程封面
    String mCourseName = "";//课程名称
    String mCourseType = "";//课程类型
    String mCourseLearnPersonNum = "";//课程学习人数
    String mCoursePrice = "";//课程价格
    String mCoursePriceOld = "";//课程原价格
    String mIsReferCourse = "0";//是否是推荐课程 0:不推荐 1:推荐
    String mCourseMessage = "";//课程描述
    String mCourseDetails = "";//课程详情
    String mCourseValidityPeriod = "";//课程有效期限
    String mCourseIsHave = "0";//课程是否购买(0:没买 1:买了)
    String mCourseOrder = "";//课程排序
    List<CourseChaptersInfo> mCourseChaptersInfoList = new ArrayList<>();//课程章
    List<CourseUnitInfo> mCourseUnitInfoList = new ArrayList<>();//课程单元
    List<CourseClassTimeInfo> mCourseClassTimeInfoTodayList = new ArrayList<>();//课次(今日)
    List<CourseClassTimeInfo> mCourseClassTimeInfoBeforeList = new ArrayList<>();//课次(历史)
    List<CourseClassTimeInfo> mCourseClassTimeInfoAfterList = new ArrayList<>();//课次(后续)
    List<CourseQuestionInfo> mCourseQuestionInfoList = new ArrayList<>();//课程问答
    public CourseInfo(){

    }
    public CourseInfo(CourseInfo courseInfo){
        mCourseCover = courseInfo.mCourseCover;//课程封面
        mCourseName = courseInfo.mCourseName;//课程名称
        mCourseType = courseInfo.mCourseType;//课程类型
        mCourseLearnPersonNum = courseInfo.mCourseLearnPersonNum;//课程学习人数
        mCoursePrice = courseInfo.mCoursePrice;//课程价格
        mCoursePriceOld = courseInfo.mCoursePriceOld;//课程原价格
        mIsReferCourse = courseInfo.mIsReferCourse;//是否是推荐课程 0:不推荐 1:推荐
        mCourseMessage = courseInfo.mCourseMessage;//课程描述
        mCourseDetails = courseInfo.mCourseDetails;//课程详情
        mCourseValidityPeriod = courseInfo.mCourseValidityPeriod;//课程有效期限
        mCourseOrder = courseInfo.mCourseOrder;//课程排序
        mCourseChaptersInfoList.addAll(courseInfo.mCourseChaptersInfoList);//课程章
        mCourseUnitInfoList.addAll(courseInfo.mCourseUnitInfoList);//课程单元
        mCourseClassTimeInfoTodayList.addAll(courseInfo.mCourseClassTimeInfoTodayList);//课次(今日)
        mCourseClassTimeInfoBeforeList.addAll(courseInfo.mCourseClassTimeInfoBeforeList);//课次(历史)
        mCourseClassTimeInfoAfterList.addAll(courseInfo.mCourseClassTimeInfoAfterList);//课次(后续)
        mCourseQuestionInfoList.addAll(courseInfo.mCourseQuestionInfoList);//课程问答
    }
}
