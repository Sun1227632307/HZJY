package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ModelNews extends Fragment implements View.OnClickListener {
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview ;
    private int height = 1344;
    private int width = 720;

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

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelNews myFragment = new ModelNews();
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
        ModelPtrFrameLayout news_content_frame = mview.findViewById(R.id.news_content_frame);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
        news_content_frame.addPtrUIHandler(header);
        news_content_frame.setHeaderView(header);
        news_content_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //在这里写自己下拉刷新数据的请求
                //需要结束刷新头
                news_content_frame.refreshComplete();
            }
        });
        NewsMainShow();
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void NewsMainShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout news_content = mview.findViewById(R.id.news_content);
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout1, null);
        news_content.addView(view);
        View line = null; //最后一个模块的线要隐藏
        //加载新闻封面
        ImageView news1_cover = view.findViewById(R.id.news1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
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
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(news1_cover);
        //新闻名称
        TextView news1_classname = view.findViewById(R.id.news1_classname);
        news1_classname.setText("案例分析建筑防火知识yayayay");
        //新闻发布时间
        TextView news1_data = view.findViewById(R.id.news1_data);
        news1_data.setText("2019-11-12");
        //分割线
        line = view.findViewById(R.id.news1_line1);
        //为每个新闻设置监听
        view.setOnClickListener(v->{
            HideAllLayout();
            mControlMainActivity.Page_NewsDetails();
            View newsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout2, null);
            //新闻标题
            TextView news2_newstitle = newsView.findViewById(R.id.news2_newstitle);
            news2_newstitle.setText("案例分析建筑防火知识yayayay");
            //新闻详情的内容  HTML格式
            TextView news2_news = newsView.findViewById(R.id.news2_news);
            new ModelHtmlUtils(mControlMainActivity,news2_news).setHtmlWithPic(mDetails);
            news_content.addView(newsView);
        });

        View view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout1, null);
        news_content.addView(view1);
        //加载新闻封面
        ImageView news1_cover1 = view1.findViewById(R.id.news1_cover);
        Glide.with(mControlMainActivity).
                load("").listener(new RequestListener<Drawable>() {
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
        })
                .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(news1_cover1);
        //新闻名称
        TextView news1_classname1 = view1.findViewById(R.id.news1_classname);
        news1_classname1.setText("案例分析建筑防火知识");
        //新闻发布时间
        TextView news1_data1 = view1.findViewById(R.id.news1_data);
        news1_data1.setText("2019-11-14");
        //非推荐新闻
        TextView news1_state = view1.findViewById(R.id.news1_state);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) news1_state.getLayoutParams();
        ll.rightMargin = 0;
        news1_state.setLayoutParams(ll);
        news1_state.setText("");
        //分割线
        line = view1.findViewById(R.id.news1_line1);
        //为每个新闻设置监听
        view1.setOnClickListener(v->{
            HideAllLayout();
            mControlMainActivity.Page_NewsDetails();
            View newsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.news_layout2, null);
            //新闻标题
            TextView news2_newstitle = newsView.findViewById(R.id.news2_newstitle);
            news2_newstitle.setText("案例分析建筑防火知识yayayay");
            //新闻详情的内容  HTML格式
            TextView news2_news = newsView.findViewById(R.id.news2_news);
            new ModelHtmlUtils(mControlMainActivity,news2_news).setHtmlWithPic(mDetails);
            news_content.addView(newsView);
        });
        line.setVisibility(View.INVISIBLE);
    }
    //隐藏所有图层
    private void HideAllLayout(){
        LinearLayout news_content = mview.findViewById(R.id.news_content);
        news_content.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:{
                break;
            }
        }
    }
}
