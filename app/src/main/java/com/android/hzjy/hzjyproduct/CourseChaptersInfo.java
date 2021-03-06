package com.android.hzjy.hzjyproduct;

import java.util.ArrayList;
import java.util.List;

public class CourseChaptersInfo {
    String mCourseChaptersId = "";//课程-章id
    String mCourseChaptersName = "";//课程-章名称
    String mCourseChaptersOrder = "";//课程-章排序
    List<CourseSectionsInfo> mCourseSectionsInfoList = new ArrayList<>();//课程-章-节
    public CourseChaptersInfo(){

    }
    public CourseChaptersInfo(CourseChaptersInfo courseChaptersInfo){
        mCourseChaptersId = courseChaptersInfo.mCourseChaptersId;//课程-章id
        mCourseChaptersName = courseChaptersInfo.mCourseChaptersName;//课程-章名称
        mCourseChaptersOrder = courseChaptersInfo.mCourseChaptersOrder;//课程-章排序
       mCourseSectionsInfoList.addAll(courseChaptersInfo.mCourseSectionsInfoList);//课程-章-节
    }
}
