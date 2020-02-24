package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelNews extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext = "xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview;
    private int height = 1344;
    private int width = 720;
    private static final String TAG = "ModelNews";
    private LinearLayout news_content;
    public View view;
    private View line;
    private SmartRefreshLayout mSmart_new_layout;
    private String news_cover;
    private int tf_comment;
    private String create_time;
    private int news_id;
    private String news_title;
    private  int visit_num;
    private View view1;

    //测试数据

    String mDetails = "<p>&nbsp;&nbsp;随着2017年一级注册消防工程师考试的报名结束，沉寂一段时间的二消又立刻成为了考证大军们新一轮关注的焦点。二消的报考条件比一级宽松，学历门槛较低，考试难度相对较小，而且一二级证书之间互不约束，所以除了吸引更多自身符合报考资格的意向考友外，其中还不乏一些已经参加一级考试的考生踊跃参与，这些都使得二级证书的前景更加明朗，竞争也更加激烈。</p>\n" +
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
    private String format1;
    private SimpleDateFormat simpleDateFormat;
    private String create_time1;
    private String news_content1;
    private int news_id1;
    private String news_title1;
    private int visit_num1;
    private static ModelNews myFragment;


    public static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage) {
        mContext = context;
        mControlMainActivity = content;
        myFragment = new ModelNews();
        FragmentPage = iFragmentPage;
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(FragmentPage, container, false);
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        NewsMainShow();
        initSmartRefresh();
        return mview;
    }
    private void initSmartRefresh() {
        mSmart_new_layout = mview.findViewById(R.id.Smart_new_layout);
        mSmart_new_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmart_new_layout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmart_new_layout.finishRefresh();
            }
        });
    }

    public void NewsMainShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        news_content = mview.findViewById(R.id.news_content);
        getModelNews();
    }

    //隐藏所有图层
    private void HideAllLayout() {
        LinearLayout news_content = mview.findViewById(R.id.news_content);
        news_content.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: {
                break;
            }
        }
    }


    //新闻咨讯详情
    public void getModelNewsDetils() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("news_id", 1);//第几页
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        modelObservableInterface.queryCoursePackageModelNewsDetils(body)
                .enqueue(new Callback<ModelNewsDetilsBean>() {
                    @Override
                    public void onResponse(Call<ModelNewsDetilsBean> call, Response<ModelNewsDetilsBean> response) {
                        ModelNewsDetilsBean detilsBean = response.body();
                        if (detilsBean != null) {
                            int code = detilsBean.getCode();
                            if (code == 200) {
                                ModelNewsDetilsBean.DataBean data = detilsBean.getData();
                                create_time1 = data.getCreate_time();    //创建时间
                                news_content1 = data.getNews_content(); //内容
                                news_id1 = data.getNews_id();  //新闻资讯id
                                news_title1 = data.getNews_title();  //标题
                                visit_num1 = data.getVisit_num();//浏览人数
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelNewsDetilsBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }


    //新闻资讯
    public void getModelNews() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", 1);//第几页
        paramsMap.put("pageSize", 5);//	每页的条目数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        modelObservableInterface.queryCoursePackageModelNews(body)
                .enqueue(new Callback<ModelNewsBean>() {
                    @Override
                    public void onResponse(Call<ModelNewsBean> call, Response<ModelNewsBean> response) {
                        ModelNewsBean newsBean = response.body();
                        if (newsBean != null) {
                            int code = newsBean.getCode();
                            if (code == 200) {
                                ModelNewsBean.DataBean data = newsBean.getData();
                                List<ModelNewsBean.DataBean.ListBean> list = data.getList();
                                for (int i = 0; i < list.size(); i++) {
                                    news_cover = list.get(i).getNews_cover();//新闻封面
                                    tf_comment = list.get(i).getTf_comment(); //是否推荐（1是2否）
                                    create_time = list.get(i).getCreate_time(); //创建时间
                                    news_id = list.get(i).getNews_id();     //	新闻资讯id
                                    news_title = list.get(i).getNews_title();  //新闻标题
                                    visit_num = list.get(i).getVisit_num();//浏览人数
                                    if (tf_comment == 1) {   //推荐1
                                        initNewsData2();
                                    } else if (tf_comment == 2) { //推荐2
                                        initNewsData1();
                                    } else {
                                        Log.e(TAG, "onResponse: " + "信息错误");
                                    }
                                }
                                mSmart_new_layout.finishRefresh();
                            } else {
                                Toast.makeText(getActivity(), "code错误" + code, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelNewsBean> call, Throwable t) {
                        Log.e(TAG, "onFailure:错误信息是 " + t.getMessage());
                    }
                });
    }

    public void initNewsData1() {
        view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout1, null);
        news_content.addView(view);
        View line = null; //最后一个模块的线要隐藏
        //加载新闻封面
        ImageView news1_cover = view.findViewById(R.id.news1_cover);
        Glide.with(mControlMainActivity).load(news_cover).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Warn", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Warn", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                return false;
            }
        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(news1_cover);
        //新闻名称
        TextView news1_classname = view.findViewById(R.id.news1_classname);
        news1_classname.setText(news_title);
        //新闻发布时间
        TextView news1_data = view.findViewById(R.id.news1_data);
        //时间格式转码
        DateFormat df = null;
        Date date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            try {
                date = df.parse(create_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                Date date1 = null;
                try {
                    date1 = df1.parse(date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null) {
                    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                    create_time = df2.format(date1);
                }
            }
        }
        news1_data.setText(create_time);
        TextView news_looknum = view.findViewById(R.id.news1_looknum);
        news_looknum.setText(String.valueOf(visit_num));
        //分割线
        line = view.findViewById(R.id.news1_line1);
        //为每个新闻设置监听详情
        initDataDetils();
        line.setVisibility(View.VISIBLE);
    }

    private void initDataDetils() {
        view.setOnClickListener(v -> {
            HideAllLayout();
            mControlMainActivity.Page_NewsDetails();
            View newsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout2, null);
            getModelNewsDetils();
            //新闻标题
            TextView news2_newstitle = newsView.findViewById(R.id.news2_newstitle);
            news2_newstitle.setText(news_title1);
            //新闻详情的内容  HTML格式
            TextView news2_news = newsView.findViewById(R.id.news2_news);
            news2_news.setText(news_content1);
            //new ModelHtmlUtils(mControlMainActivity, news2_news).setHtmlWithPic(news_content1);
            news_content.addView(newsView);
        });
    }

    public void initNewsData2() {
        view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout1, null);
        news_content.addView(view1);
        //加载新闻封面
        getModelNewsDetils();
        ImageView news1_cover1 = view1.findViewById(R.id.news1_cover);
        Glide.with(mControlMainActivity).load(news_cover).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("Warn", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("Warn", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                return false;
            }
        }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(news1_cover1);
        //新闻名称
        TextView news1_classname1 = view1.findViewById(R.id.news1_classname);
        news1_classname1.setText(news_title);
        //新闻发布时间
        TextView news1_data1 = view1.findViewById(R.id.news1_data);
        //时间格式转码
        DateFormat df = null;
        Date date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            try {
                date = df.parse(create_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                Date date1 = null;
                try {
                    date1 = df1.parse(date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null) {
                    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                    create_time = df2.format(date1);
                }
            }
        }
        news1_data1.setText(create_time);
        TextView news1_looknum = view1.findViewById(R.id.news1_looknum);
        news1_looknum.setText(String.valueOf(visit_num));
        //非推荐新闻
        TextView news1_state = view1.findViewById(R.id.news1_state);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) news1_state.getLayoutParams();
        ll.rightMargin = 0;
        news1_state.setLayoutParams(ll);
        news1_state.setText("推荐");
        //分割线
        line = view1.findViewById(R.id.news1_line1);
        //为每个新闻设置监听
        initDataDetils2();
        line.setVisibility(View.INVISIBLE);
    }
     //页面详情2
    private void initDataDetils2() {
        view1.setOnClickListener(v -> {
            HideAllLayout();
            mControlMainActivity.Page_NewsDetails();
            View newsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout2, null);
           //获取详情的网络数据
            getModelNewsDetils();
            //新闻标题
            TextView news2_newstitle = newsView.findViewById(R.id.news2_newstitle);
            news2_newstitle.setText(news_title1);
            //新闻详情的内容  HTML格式
            TextView news2_news = newsView.findViewById(R.id.news2_news);
            news2_news.setText(news_content1);
           // new ModelHtmlUtils(mControlMainActivity, news2_news).setHtmlWithPic(news_content1);
            news_content.addView(newsView);
        });
    }

    public static class ModelNewsBean {
        /**
         * code : 200
         * data : {"total":3,"list":[{"news_cover":"插入封面2","tf_comment":1,"create_time":"2019-10-29T09:30:30.000+0800","news_title":"测试插入标题2","visit_num":10,"news_id":4},{"news_cover":"E:/upload111/logo1575509930210.png","tf_comment":1,"create_time":"2019-12-05T09:38:58.000+0800","news_title":"新闻标题0101","visit_num":0,"news_id":11},{"news_cover":"E:/upload111/u=1653307308,2646823946&fm=26&gp=01575621264686.jpg","tf_comment":1,"create_time":"2019-12-06T14:03:25.000+0800","news_title":"新闻222","visit_num":0,"news_id":16}],"pageNum":1,"pageSize":3,"size":3,"startRow":1,"endRow":3,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1}
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
             * list : [{"news_cover":"插入封面2","tf_comment":1,"create_time":"2019-10-29T09:30:30.000+0800","news_title":"测试插入标题2","visit_num":10,"news_id":4},{"news_cover":"E:/upload111/logo1575509930210.png","tf_comment":1,"create_time":"2019-12-05T09:38:58.000+0800","news_title":"新闻标题0101","visit_num":0,"news_id":11},{"news_cover":"E:/upload111/u=1653307308,2646823946&fm=26&gp=01575621264686.jpg","tf_comment":1,"create_time":"2019-12-06T14:03:25.000+0800","news_title":"新闻222","visit_num":0,"news_id":16}]
             * pageNum : 1
             * pageSize : 3
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
                 * news_cover : 插入封面2
                 * tf_comment : 1
                 * create_time : 2019-10-29T09:30:30.000+0800
                 * news_title : 测试插入标题2
                 * visit_num : 10
                 * news_id : 4
                 */

                private String news_cover;
                private int tf_comment;
                private String create_time;
                private String news_title;
                private int visit_num;
                private int news_id;

                public String getNews_cover() {
                    return news_cover;
                }

                public void setNews_cover(String news_cover) {
                    this.news_cover = news_cover;
                }

                public int getTf_comment() {
                    return tf_comment;
                }

                public void setTf_comment(int tf_comment) {
                    this.tf_comment = tf_comment;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public String getNews_title() {
                    return news_title;
                }

                public void setNews_title(String news_title) {
                    this.news_title = news_title;
                }

                public int getVisit_num() {
                    return visit_num;
                }

                public void setVisit_num(int visit_num) {
                    this.visit_num = visit_num;
                }

                public int getNews_id() {
                    return news_id;
                }

                public void setNews_id(int news_id) {
                    this.news_id = news_id;
                }
            }
        }
    }
    //新闻详情
    public static class ModelNewsDetilsBean {
        /**
         * code : 200
         * data : {"create_time":"2019-10-29T09:30:30.000+0800","news_content":"插入内容3","news_title":"测试插入标题2","visit_num":13,"news_id":4}
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
             * create_time : 2019-10-29T09:30:30.000+0800
             * news_content : 插入内容3
             * news_title : 测试插入标题2
             * visit_num : 13
             * news_id : 4
             */

            private String create_time;
            private String news_content;
            private String news_title;
            private int visit_num;
            private int news_id;

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getNews_content() {
                return news_content;
            }

            public void setNews_content(String news_content) {
                this.news_content = news_content;
            }

            public String getNews_title() {
                return news_title;
            }

            public void setNews_title(String news_title) {
                this.news_title = news_title;
            }

            public int getVisit_num() {
                return visit_num;
            }

            public void setVisit_num(int visit_num) {
                this.visit_num = visit_num;
            }

            public int getNews_id() {
                return news_id;
            }

            public void setNews_id(int news_id) {
                this.news_id = news_id;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
