package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.hzjy.hzjyproduct.consts.PlayType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelOpenClass extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int height = 1344;
    private int width = 720;
    private int mLastTabIndex = 1;
    private String mCurrentTab = "all";
    private String CurrentTime;
    private static final String TAG = "ModelOpenClass";
    private SimpleDateFormat simpleDateFormat;
    private LinearLayout openclass_content;
    private SmartRefreshLayout mSmart_openclass_layout;
    private ModelOpenclassBean openclassBean;
    private String OPENCLASS_TYPE="";

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelOpenClass myFragment = new ModelOpenClass();
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

        //控件的刷新效果
        mSmart_openclass_layout = mview.findViewById(R.id.Smart_openclass_layout);
        mSmart_openclass_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmart_openclass_layout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (openclassBean!=null){
                    ModelOpenclassBean.DataBean data = openclassBean.getData();
                    List<ModelOpenclassBean.DataBean.ListBean> list = data.getList();
                    if (list!=null){
                        list.clear();
                    }
                }
                mSmart_openclass_layout.finishRefresh();
            }
        });
        TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
        TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
        TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
        TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
        openclass_tab_all.setOnClickListener(this);
        openclass_tab_haveinhand.setOnClickListener(this);
        openclass_tab_begininaminute.setOnClickListener(this);
        openclass_tab_over.setOnClickListener(this);
        CourseMainShow();
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void CourseMainShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        //默认游标位置在全部
        ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
        int x = width / 8 - mview.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        openclass_cursor1.setX(x);
        //默认选中的为全部      默认选中为全部
        mLastTabIndex = 1;
        mCurrentTab = "all";
        TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
        TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
        TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
        TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
        openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
        openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
        openclass_content = mview.findViewById(R.id.openclass_content);
        openclass_content.removeAllViews();
        if (OPENCLASS_TYPE.isEmpty()){
            OPENCLASS_TYPE="全部";
        } else {
            OPENCLASS_TYPE="";
        }

        //getOpenClassBean();
    }

    //隐藏所有图层
    private void HideAllLayout(){
//        RelativeLayout course_mainLayout = mview.findViewById(R.id.course_mainLayout);
//        LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) course_mainLayout.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_mainLayout.setLayoutParams(LP);
//        course_mainLayout.setVisibility(View.INVISIBLE);
//        RelativeLayout course_searchlayout = mview.findViewById(R.id.course_searchlayout);
//        LP = (LinearLayout.LayoutParams) course_searchlayout.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_searchlayout.setLayoutParams(LP);
//        course_searchlayout.setVisibility(View.INVISIBLE);
//        RelativeLayout course_details1 = mview.findViewById(R.id.course_details1);
//        LP = (LinearLayout.LayoutParams) course_details1.getLayoutParams();
//        LP.width = 0;
//        LP.height = 0;
//        course_details1.setLayoutParams(LP);
//        course_details1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openclass_tab_all:{
                if (!mCurrentTab.equals("all")) {
                    //全部
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    if (OPENCLASS_TYPE.isEmpty()){
                        OPENCLASS_TYPE="全部";
                    } else {
                        OPENCLASS_TYPE="";
                    }

                   //getOpenClassBean();//文件加载全部数据
                }
                mLastTabIndex = 1;
                mCurrentTab = "all";
                break;
            }
            //进行中
            case R.id.openclass_tab_haveinhand:{
                if (!mCurrentTab.equals("haveinhand")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width / 4 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    if (OPENCLASS_TYPE.isEmpty()){
                        OPENCLASS_TYPE="已开始";
                    } else {
                        OPENCLASS_TYPE="";
                    }
                    //getOpenClassBean();
                }
                mLastTabIndex = 2;
                mCurrentTab = "haveinhand";
                break;
            }
            case R.id.openclass_tab_begininaminute:{
                if (!mCurrentTab.equals("begininaminute")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width * 2 / 4, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    if (OPENCLASS_TYPE.isEmpty()){
                        OPENCLASS_TYPE="未开始";
                    } else {
                        OPENCLASS_TYPE="";
                    }
                    //getOpenClassBean();
                }
                mLastTabIndex = 3;
                mCurrentTab = "begininaminute";
                break;
            }
            case R.id.openclass_tab_over:{
                if (!mCurrentTab.equals("over")) {
                    ImageView openclass_cursor1 = mview.findViewById(R.id.openclass_cursor1);
                    Animation animation = new TranslateAnimation(( mLastTabIndex - 1)  * width / 4,width * 3 / 4, 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    openclass_cursor1.startAnimation(animation);
                    TextView openclass_tab_all = mview.findViewById(R.id.openclass_tab_all);
                    TextView openclass_tab_haveinhand = mview.findViewById(R.id.openclass_tab_haveinhand);
                    TextView openclass_tab_begininaminute = mview.findViewById(R.id.openclass_tab_begininaminute);
                    TextView openclass_tab_over = mview.findViewById(R.id.openclass_tab_over);
                    openclass_tab_all.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_haveinhand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_begininaminute.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    openclass_tab_over.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mview.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    if (OPENCLASS_TYPE.isEmpty()){
                        OPENCLASS_TYPE="已结束";
                    } else {
                        OPENCLASS_TYPE="";
                    }
                    //getOpenClassBean();
                }
                mLastTabIndex = 4;
                mCurrentTab = "over";
                break;
            }
            default:{
                break;
            }
        }
    }
    //获取openclass的数据
   public void getOpenClassBean(){
       Retrofit retrofit = new Retrofit.Builder()
               .addConverterFactory(GsonConverterFactory.create())
               .baseUrl(ModelObservableInterface.urlHead)
               .build();
       ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
       Gson gson = new Gson();
       HashMap<String, String> paramsMap = new HashMap<>();
       paramsMap.put("pageNum", String.valueOf(1));//第几页
       paramsMap.put("pageSize", String.valueOf(2));//	每页的条目数
       paramsMap.put("type",OPENCLASS_TYPE);//	未开始/进行中/已结束/全部

       String strEntity = gson.toJson(paramsMap);
       RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
    modelObservableInterface.queryCoursePackageOpenclass(body)
            .enqueue(new Callback<ModelOpenclassBean>() {

                private String status;
                private int public_class_id;
                private String pc_name;
                private String pc_cover;
                private String end_time;
                private String begin_time;

                @Override
                public void onResponse(Call<ModelOpenclassBean> call, Response<ModelOpenclassBean> response) {
                    openclassBean = response.body();
                    if (openclassBean.getCode()==200){
                        ModelOpenclassBean.DataBean data = openclassBean.getData();
                        List<ModelOpenclassBean.DataBean.ListBean> list = data.getList();
                        for (int i = 0; i < list.size(); i++){
                            begin_time = list.get(i).getBegin_time();   //开始时间
                            end_time = list.get(i).getEnd_time();    //结束时间
                            pc_cover = list.get(i).getPc_cover(); //封面
                            pc_name = list.get(i).getPc_name();//公开课名字
                            public_class_id = list.get(i).getPublic_class_id();  //公开课id
                            status = list.get(i).getStatus(); //公开课状态
                            initAll();
                        }
                    }else {
                        Toast.makeText(getActivity(), "错误码"+ openclassBean.getCode(), Toast.LENGTH_SHORT).show();
                    }
                }

                private void initAll() {
                    View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.openclass_layout1, null);
                    openclass_content.addView(view);
                    //加载公开课封面
                    ImageView openclass_cover = view.findViewById(R.id.openclass_cover);
                    Glide.with(mControlMainActivity).load(pc_cover).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
                            return false;
                        }
                    }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(openclass_cover);
                    //公开课名称
                    TextView openclass_classname = view.findViewById(R.id.openclass_classname);
                    openclass_classname.setText(pc_name);
                    //公开课的开始和结束时间
                    TextView openclass1_time = view.findViewById(R.id.openclass1_time);
                    openclass1_time.setText(begin_time+end_time);
                    //公开课的状态
                    TextView openclass1_state = view.findViewById(R.id.openclass1_state);
                    openclass1_state.setText(status);
                    //公开课当前logo
                    ImageView openclass1_logo = view.findViewById(R.id.openclass1_logo);
                    String s = openclass1_state.getText().toString();
                    if (s.equals("即将开始")){
                        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_start));
                        openclass1_state.setTextColor(view.getResources().getColor(R.color.holo_red_dark));
                    }if (s.equals("已结束")){
                        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_over));
                        openclass1_state.setTextColor(view.getResources().getColor(R.color.color_69));
                    }
//        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_start));
//        openclass1_state.setTextColor(view.getResources().getColor(R.color.holo_red_dark));
//        openclass1_state.setText("即将开始");

//        openclass1_logo.setBackground(view.getResources().getDrawable(R.drawable.button_openclass_over));
//        openclass1_state.setTextColor(view.getResources().getColor(R.color.color_69));
//        openclass1_state.setText("已结束");
                    //为每个公开课设置监听
                    view.setOnClickListener(v->{
//            mControlMainActivity.LoginLiveOrPlayback("391068","dadada","",PlayType.LIVE);
                        mControlMainActivity.LoginLiveOrPlayback("365061","dadada","799723", PlayType.PLAYBACK);
                    });
                }

                @Override
                public void onFailure(Call<ModelOpenclassBean> call, Throwable t) {
                    Log.e(TAG, "onFailure:数据是 "+t.getMessage()+"" );
                }
            });
   }




    //公开课的数据类
    public static class ModelOpenclassBean{
        /**
         * code : 200
         * data : {"total":4,"list":[{"pc_name":"江山","end_time":"2019-12-11T00:00:00.000+0800","begin_time":"2019-12-11T00:00:00.000+0800","pc_cover":"E:/upload111/1576151514113.png","public_class_id":21,"status":"已结束"},{"pc_name":"公开课11","end_time":"2019-12-09T11:29:00.000+0800","begin_time":"2019-12-09T10:29:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":18,"status":"已结束"},{"pc_name":"公开课22","end_time":"2019-11-07T10:29:00.000+0800","begin_time":"2019-11-07T10:29:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":17,"status":"已结束"},{"pc_name":"公开课33","end_time":"2017-02-02T00:00:00.000+0800","begin_time":"2017-02-02T00:00:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":12,"status":"已结束"}],"pageNum":1,"pageSize":4,"size":4,"startRow":0,"endRow":3,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1}
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
             * list : [{"pc_name":"江山","end_time":"2019-12-11T00:00:00.000+0800","begin_time":"2019-12-11T00:00:00.000+0800","pc_cover":"E:/upload111/1576151514113.png","public_class_id":21,"status":"已结束"},{"pc_name":"公开课11","end_time":"2019-12-09T11:29:00.000+0800","begin_time":"2019-12-09T10:29:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":18,"status":"已结束"},{"pc_name":"公开课22","end_time":"2019-11-07T10:29:00.000+0800","begin_time":"2019-11-07T10:29:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":17,"status":"已结束"},{"pc_name":"公开课33","end_time":"2017-02-02T00:00:00.000+0800","begin_time":"2017-02-02T00:00:00.000+0800","pc_cover":"E:/upload111/1573110163820.jpg","public_class_id":12,"status":"已结束"}]
             * pageNum : 1
             * pageSize : 4
             * size : 4
             * startRow : 0
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
                 * pc_name : 江山
                 * end_time : 2019-12-11T00:00:00.000+0800
                 * begin_time : 2019-12-11T00:00:00.000+0800
                 * pc_cover : E:/upload111/1576151514113.png
                 * public_class_id : 21
                 * status : 已结束
                 */

                private String pc_name;
                private String end_time;
                private String begin_time;
                private String pc_cover;
                private int public_class_id;
                private String status;

                public String getPc_name() {
                    return pc_name;
                }

                public void setPc_name(String pc_name) {
                    this.pc_name = pc_name;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getBegin_time() {
                    return begin_time;
                }

                public void setBegin_time(String begin_time) {
                    this.begin_time = begin_time;
                }

                public String getPc_cover() {
                    return pc_cover;
                }

                public void setPc_cover(String pc_cover) {
                    this.pc_cover = pc_cover;
                }

                public int getPublic_class_id() {
                    return public_class_id;
                }

                public void setPublic_class_id(int public_class_id) {
                    this.public_class_id = public_class_id;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }
}
