package com.android.hzjy.hzjyproduct;

import java.util.ArrayList;
import java.util.List;

//课程信息结构体
public class CoursePacketInfo {
    String mCoursePacketCover = "";//课程包封面
    String mCoursePacketName = "";//课程包名称
    String mCoursePacketStageNum = "";//课程包阶段数量
    String mCoursePacketCourseNum = "";//课程包课程数量
    String mCoursePacketLearnPersonNum = "";//课程包学习人数
    String mCoursePacketPrice = "";//课程包价格
    String mCoursePacketPriceOld = "";//课程包原价格
    String mIsReferCoursePacket = "0";//是否是推荐课程包 0:不推荐 1:推荐
    String mCoursePacketMessage = "";//课程包描述
    String mCoursePacketDetails = "";//课程包详情
    List<StageCourseInfo> mStageCourseInfoList = new ArrayList<>();//课程包包含的课程阶段
    List<TeacherInfo> mTeacherInfoList = new ArrayList<>();//课程包包含的师资
    public CoursePacketInfo(){

    }
    public CoursePacketInfo(CoursePacketInfo coursePacketInfo){
        mCoursePacketCover = coursePacketInfo.mCoursePacketCover;
        mCoursePacketName = coursePacketInfo.mCoursePacketName;
        mCoursePacketStageNum = coursePacketInfo.mCoursePacketStageNum;//课程包阶段数量
        mCoursePacketCourseNum = coursePacketInfo.mCoursePacketCourseNum;//课程包课程数量
        mCoursePacketLearnPersonNum = coursePacketInfo.mCoursePacketLearnPersonNum;//课程包学习人数
        mCoursePacketPrice = coursePacketInfo.mCoursePacketPrice;//课程包价格
        mCoursePacketPriceOld = coursePacketInfo.mCoursePacketPriceOld;//课程包原价格
        mIsReferCoursePacket = coursePacketInfo.mIsReferCoursePacket;//是否是推荐课程包 0:不推荐 1:推荐
        mCoursePacketMessage = coursePacketInfo.mCoursePacketMessage;//课程包描述
        mCoursePacketDetails = coursePacketInfo.mCoursePacketDetails;//课程包详情
        mStageCourseInfoList.addAll(coursePacketInfo.mStageCourseInfoList);//课程包包含的课程阶段
        mTeacherInfoList.addAll(coursePacketInfo.mTeacherInfoList);
    }
}
