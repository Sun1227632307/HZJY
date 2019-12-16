package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.hzjy.hzjyproduct.base.BaseDatabindingAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayuer on 19/7/2.
 * 课程表
 */
public class ModelClassCheduleCard extends Fragment implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {
    private static int FragmentPage;
    private static String mContext;
    //    private List<WeekDay> mWeekDayList ;
    private View mView = null,mClasschedulecardView = null;
    private static ControlMainActivity mControlMainActivity;
    //要显示的页面
//    private int FragmentPage;
    public  static  Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelClassCheduleCard myFragment = new ModelClassCheduleCard();
        FragmentPage = iFragmentPage;
        return  myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(FragmentPage,container,false);
        ClassCheduleCardMainInit(mContext);
        return mView;
    }

    //隐藏所有图层
    private void HideAllLayout(){
        LinearLayout classchedulecard_content = mView.findViewById(R.id.classchedulecard_content);
        classchedulecard_content.removeAllViews();
    }
    public void ClassCheduleCardMainInit(String context){
        if (mView == null){
            return;
        }
        HideAllLayout();
        if (mClasschedulecardView == null) {
            mClasschedulecardView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.fragment_classchedulecard1, null);
            CalendarView calendarView = mClasschedulecardView.findViewById(R.id.calendarView);
//        calendarView.setThemeColor(Color.YELLOW,Color.YELLOW);
            calendarView.setOnDateSelectedListener(this);
            calendarView.setOnYearChangeListener(this);
        }
        if (context.equals("首页")){
            LinearLayout classchedulecard_title_layout = mView.findViewById(R.id.classchedulecard_title_layout);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) classchedulecard_title_layout.getLayoutParams();
            rl.height = 0;
            classchedulecard_title_layout.setLayoutParams(rl);
        } else {
            LinearLayout classchedulecard_title_layout = mView.findViewById(R.id.classchedulecard_title_layout);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) classchedulecard_title_layout.getLayoutParams();
            rl.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            classchedulecard_title_layout.setLayoutParams(rl);
        }
        LinearLayout classchedulecard_content = mView.findViewById(R.id.classchedulecard_content);
        classchedulecard_content.addView(mClasschedulecardView);
        CalendarView calendarView = mClasschedulecardView.findViewById(R.id.calendarView);
        ModelGroupRecyclerView recyclerView = mClasschedulecardView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mControlMainActivity));
        ModelGroupItemDecoration modelGroupItemDecoration = new ModelGroupItemDecoration<String,ClassBean>();
        modelGroupItemDecoration.setTextColor(getResources().getColor(R.color.black999999));
        modelGroupItemDecoration.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize12));
        recyclerView.addItemDecoration(modelGroupItemDecoration);
        ModelClassAdapter modelClassAdapter =  new ModelClassAdapter(mControlMainActivity);
        modelClassAdapter.setOnItemClickListener((position, itemId) -> {
            //点击每个课程信息的回调
        });
        recyclerView.setAdapter( modelClassAdapter);
        recyclerView.notifyDataSetChanged();
        initData(calendarView);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {

    }

    protected void initData(CalendarView calendarView) {

        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();

        List<Calendar> CalendarList = new ArrayList<>();
        CalendarList.add(getSchemeCalendar(year, month, 18, getResources().getColor(R.color.blue649cf0), "假"));
        CalendarList.add(getSchemeCalendar(year, month, 21, getResources().getColor(R.color.blue649cf0), "假"));
        CalendarList.add(getSchemeCalendar(year, month, 24, getResources().getColor(R.color.blue649cf0), "假"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView.setSchemeDate(CalendarList);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }
}