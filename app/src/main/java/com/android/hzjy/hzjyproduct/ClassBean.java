package com.android.hzjy.hzjyproduct;

import java.io.Serializable;

/**
 * 一个简单的bean
 * Created by huanghaibin on 2017/12/4.
 */
@SuppressWarnings("all")
public class ClassBean implements Serializable {
    private int id;
    private String className;
    private String classTime;
    private String classData;
    private String classTeacher;
    private String classTeacherImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassData() {
        return classData;
    }

    public void setClassData(String classData) {
        this.classData = classData;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getClassTeacherImg() {
        return classTeacherImg;
    }

    public void setClassTeacherImg(String classTeacherImg) {
        this.classTeacherImg = classTeacherImg;
    }
}
