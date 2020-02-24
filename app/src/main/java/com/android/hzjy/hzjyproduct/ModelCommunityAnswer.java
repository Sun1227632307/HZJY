package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import net.sqlcipher.Cursor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.fragment.NewImagePagerDialogFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelCommunityAnswer extends Fragment{
    //课程问答
        private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    private static final String TAG = "ModelCommunityAnswer";
    //要显示的页面
    static private int FragmentPage;
    private View mview,mCommunityAnswerView ,mCommunityAnswerSelectView ,mCommunityAnswerAddView ,mCommunityAnswerChooseSignView
            ,mCommunityAnswerDetailsView;
    private int height = 1344;
    private int width = 720;
    //弹出窗口（筛选条件）
    private PopupWindow popupWindow;
    //检索条件-默认全部  mCommunityAnswerSelectTemp 为临时存储，当点击确定时，才替换到mCommunityAnswerSelect
    private String mCommunityAnswerSelect = "-1";
    private String mCommunityAnswerSelectTemp = "-1";
    //添加问答-图片选择器
    private RecyclerView mRecyclerView;
    private ArrayList<ControllerPictureBean> mPictureBeansList;
    private ControllerPictureAdapter mPictureAdapter;
    private ArrayList<String> selPhotosPath = null;//选中的图片路径集合
    private boolean mIsPublish = true;
    private boolean mQuestionPublishImage = false;
    private boolean mQuestionPublishTitle = false;
    private boolean mQuestionPublishContent = false;
    //添加问题标签(存储选中的标签id)。最多添加三个
    private List<String> mCommunityAnswerChooseSignList = new ArrayList<>();
    //草稿箱提示框的dialog
    private ControllerCenterDialog mMyDialog;

    //评论
    private ControllerCustomDialog mCustomDialog = null;
    private DialogInterface.OnKeyListener keylistener = (dialog, keyCode, event) -> {
        Log.i("TAG", "键盘code---" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            return false;
        } else if(keyCode == KeyEvent.KEYCODE_DEL){//删除键
            return false;
        }else{
            return true;
        }
    };
    private SmartRefreshLayout smart_model_communityanswer;
    private LinearLayout communityanswer_datails_linearlayout;
    private SmartRefreshLayout mSmart_model_communityanswer_detalis;
    private View model_communityanswer_child_view1;
    private LinearLayout.LayoutParams ll;
    private EditText communityanswer_add_layout_contentetitledittext;
    private EditText communityanswer_add_layout_contentedittext;

    public  static Fragment newInstance(ControlMainActivity content, String context, int iFragmentPage){
        mContext = context;
        mControlMainActivity = content;
        ModelCommunityAnswer myFragment = new ModelCommunityAnswer();
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
        CommunityAnswerMainShow();
        return mview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void CommunityAnswerMainShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        if (mCommunityAnswerView == null){
            mCommunityAnswerView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer, null);
            //下滑刷新处理
           //Smart_model_communityanswer
            smart_model_communityanswer = mCommunityAnswerView.findViewById(R.id.Smart_model_communityanswer);
            smart_model_communityanswer.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    smart_model_communityanswer.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    smart_model_communityanswer.finishRefresh();
                }
            });
            //关键词搜索监听
            ImageView communityanswer_searchimage = mCommunityAnswerView.findViewById(R.id.communityanswer_searchimage);
            communityanswer_searchimage.setOnClickListener(v->{
                CommunityAnswerSelectShow();
            });
            //搜索框
            TextView communityanswer_hint = mCommunityAnswerView.findViewById(R.id.communityanswer_hint);
            communityanswer_hint.setOnClickListener(v->{
                CommunityAnswerSelectShow();
            });
            //条件查询
            ImageView communityanswer_searchcondition = mCommunityAnswerView.findViewById(R.id.communityanswer_searchcondition);
            communityanswer_searchcondition.setOnClickListener(v->{
                initPopupWindow();
            });
            //点击添加问答
            ImageView communityanswer_add = mCommunityAnswerView.findViewById(R.id.communityanswer_add);
            communityanswer_add.setOnClickListener(v->{
                CommunityAnswerAddInit(true);
            });
        }
        communityanswer_layout_main.addView(mCommunityAnswerView);
        LinearLayout communityanswer_linearlayout = mCommunityAnswerView.findViewById(R.id.communityanswer_linearlayout);
        communityanswer_linearlayout.removeAllViews();
         //社区问答列表
        //测试数据1      getCommunityData（）网络请求

        {
            model_communityanswer_child_view1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
            communityanswer_linearlayout.addView(model_communityanswer_child_view1);
           //浏览人数
            TextView communityanswer_child_look = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_look);
            communityanswer_child_look.setText("浏览人数2000");
            //社区问答列表头像
            ControllerCustomRoundAngleImageView communityanswer_child_headportrait = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_headportrait);
             //社区列表时间
            TextView communityanswer_child_time = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_time);
            communityanswer_child_time.setText("2020/01/14");
            //社区问答标题
            TextView communityanswer_child_title = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_title);
            communityanswer_child_title.setText("我是社区问答标题");
            //社区问答内容
            TextView communityanswer_child_message = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_message);
            communityanswer_child_message.setText("我是社区问答内容");
            //社区问答图片   communityanswer_child_imagelayout
            GridLayout communityanswer_child_imagelayout = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_imagelayout);
            //集合.size
            for (int i = 0; i < 2; i++){
                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
                Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
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
                }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
                communityanswer_child_imagelayout.addView(imageView);
            }
            //社区问答标签  至少一个 最多三个    communityanswer_child_sign1
            TextView communityanswer_child_sign1 = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_sign1);
            communityanswer_child_sign1.setText("我是社区问答标签");
            //添加部分评论，此页最多显示三条
            LinearLayout communityanswer_child_body = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_body);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) communityanswer_child_body.getLayoutParams();
            rl.topMargin = (int) model_communityanswer_child_view1.getResources().getDimension(R.dimen.dp15);
            communityanswer_child_body.setLayoutParams(rl);
            communityanswer_child_body.setPadding(0,0,0, (int) model_communityanswer_child_view1.getResources().getDimension(R.dimen.dp10));
            //社区问答的条目评论显示
            LinearLayout communityanswer_child_content = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_content);

            //点击评论，对其进行回复
            LinearLayout communityanswer_child_function_discuss = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_function_discuss);
            communityanswer_child_function_discuss.setOnClickListener(v->{
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"评论",false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {
                           //获取回复的网络请求 for循环  判断当前的size判断当前的size是否大于3
//                        View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
//                        communityanswer_child_content.addView(respondView);
//                        respondView.setOnClickListener(v->{
                                    Toast.makeText(getActivity(), "我是回复的内容", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < 2; i++){
                                View respondView1 = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                                //回复人的名字
                                TextView communityanswer_child1_name = respondView1.findViewById(R.id.communityanswer_child1_name);
                                communityanswer_child1_name.setText("张三");
                                //被回复人名字
                                TextView communityanswer_child1_name1 = respondView1.findViewById(R.id.communityanswer_child1_name1);
                                communityanswer_child1_name1.setText("李四");
                                //回复内容
                                TextView communityanswer_child1_content = respondView1.findViewById(R.id.communityanswer_child1_name);
                                communityanswer_child1_content.setText("我是回复的内容");
                                communityanswer_child_content.addView(respondView1);
                            }
//                        });
                       //大于3的话显示查看全部评论   判断当前的评论条目
                    }

                    @Override
                    public void image() {
                        Toast.makeText(getActivity(), "我是公共的图片", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            //点击查看评论详情
            LinearLayout communityanswer_child_content1 = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_content1);
            communityanswer_child_content1.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
//            //点击查看全部评论进入评论详情
//            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
//                CommunityAnswerDetailsShow();
//            });
            //       判断当前的评论是否超过三条，如果评论超过三条显示查看全部
            LinearLayout communityanswer_child_lookalldiscuss = model_communityanswer_child_view1.findViewById(R.id.communityanswer_child_lookalldiscuss);
            ll = (LinearLayout.LayoutParams) communityanswer_child_lookalldiscuss.getLayoutParams();
            ll.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ll.topMargin = (int) model_communityanswer_child_view1.getResources().getDimension(R.dimen.dp10);
            communityanswer_child_lookalldiscuss.setLayoutParams(ll);

            //点击查看全部评论进入评论详情
            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
        }
        //测试数据-----去掉顶
        {
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
            //去掉顶
            ImageView communityanswer_child_top = view.findViewById(R.id.communityanswer_child_top);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) communityanswer_child_top.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_top.setLayoutParams(ll);
//            //去掉精
//            ImageView communityanswer_child_fine = view.findViewById(R.id.communityanswer_child_fine);
//            ll = (LinearLayout.LayoutParams) communityanswer_child_fine.getLayoutParams();
//            ll.width = 0;
//            ll.rightMargin = 0;
//            communityanswer_child_fine.setLayoutParams(ll);
            communityanswer_linearlayout.addView(view);
            //添加图片
            GridLayout communityanswer_child_imagelayout = view.findViewById(R.id.communityanswer_child_imagelayout);
            //布局图片的加载
            {
                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
                Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
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
                }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
                communityanswer_child_imagelayout.addView(imageView);
            }
//            {
//                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//                Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                        return false;
//                    }
//                }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//                communityanswer_child_imagelayout.addView(imageView);
//            }
            //点击评论，对其进行回复
            LinearLayout communityanswer_child_function_discuss = view.findViewById(R.id.communityanswer_child_function_discuss);
            communityanswer_child_function_discuss.setOnClickListener(v->{
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"评论",false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {

                    }

                    @Override
                    public void image() {

                    }
                });
            });
            //点击查看评论详情
            LinearLayout communityanswer_child_content1 = view.findViewById(R.id.communityanswer_child_content1);
            communityanswer_child_content1.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
//            //点击查看全部评论进入评论详情
//            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
//                CommunityAnswerDetailsShow();
//            });
        }
        {
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
//            //去掉顶
            ImageView communityanswer_child_top = view.findViewById(R.id.communityanswer_child_top);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) communityanswer_child_top.getLayoutParams();
//            ll.width = 0;
//            ll.rightMargin = 0;
            communityanswer_child_top.setLayoutParams(ll);
            //去掉精
            ImageView communityanswer_child_fine = view.findViewById(R.id.communityanswer_child_fine);
            ll = (LinearLayout.LayoutParams) communityanswer_child_fine.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_fine.setLayoutParams(ll);
            communityanswer_linearlayout.addView(view);
            //添加图片
            GridLayout communityanswer_child_imagelayout = view.findViewById(R.id.communityanswer_child_imagelayout);
            {
                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
                Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
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
                }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
                communityanswer_child_imagelayout.addView(imageView);
            }
//            {
//                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//                Glide.with(mControlMainActivity).
//                        load("").listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                        return false;
//                    }
//                })
//                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//                communityanswer_child_imagelayout.addView(imageView);
//            }
//            {
//                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//                Glide.with(mControlMainActivity).
//                        load("").listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                        return false;
//                    }
//                })
//                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//                communityanswer_child_imagelayout.addView(imageView);
//            }
            //点击评论，对其进行回复
//            LinearLayout communityanswer_child_function_discuss = view.findViewById(R.id.communityanswer_child_function_discuss);
//            communityanswer_child_function_discuss.setOnClickListener(v->{
//                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"评论",false);
//                mCustomDialog.setOnKeyListener(keylistener);
//                mCustomDialog.show();
//                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
//                    @Override
//                    public void publish() {
//
//                    }
//
//                    @Override
//                    public void image() {
//
//                    }
//                });
//            });
            //点击查看评论详情
            LinearLayout communityanswer_child_content1 = view.findViewById(R.id.communityanswer_child_content1);
            communityanswer_child_content1.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
//            //点击查看全部评论进入评论详情
//            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
//                CommunityAnswerDetailsShow();
//            });
        }
        {
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
//            //去掉顶
            ImageView communityanswer_child_top = view.findViewById(R.id.communityanswer_child_top);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) communityanswer_child_top.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_top.setLayoutParams(ll);
            //去掉精
            ImageView communityanswer_child_fine = view.findViewById(R.id.communityanswer_child_fine);
            ll = (LinearLayout.LayoutParams) communityanswer_child_fine.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_fine.setLayoutParams(ll);
            communityanswer_linearlayout.addView(view);
            //添加图片
            GridLayout communityanswer_child_imagelayout = view.findViewById(R.id.communityanswer_child_imagelayout);
//            {
//                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//                Glide.with(mControlMainActivity).
//                        load("").listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                        return false;
//                    }
//                })
//                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//                communityanswer_child_imagelayout.addView(imageView);
//            }
            {
                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
                Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
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
                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
                communityanswer_child_imagelayout.addView(imageView);
            }
            //添加部分评论，此页最多显示三条
            LinearLayout communityanswer_child_body = view.findViewById(R.id.communityanswer_child_body);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) communityanswer_child_body.getLayoutParams();
            rl.topMargin = (int) view.getResources().getDimension(R.dimen.dp15);
            communityanswer_child_body.setLayoutParams(rl);
            communityanswer_child_body.setPadding(0,0,0, (int) view.getResources().getDimension(R.dimen.dp10));
            LinearLayout communityanswer_child_content = view.findViewById(R.id.communityanswer_child_content);
            //社区问答部分评论内容
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
                respondView.setOnClickListener(v->{
                    TextView communityanswer_child1_name = respondView.findViewById(R.id.communityanswer_child1_name);
                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_child1_name.getText().toString(),false);
                    mCustomDialog.setOnKeyListener(keylistener);
                    mCustomDialog.show();
                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                        @Override
                        public void publish() {

                        }

                        @Override
                        public void image() {

                        }
                    });
                });
            }
//            {
//                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
//                communityanswer_child_content.addView(respondView);
//                respondView.setOnClickListener(v->{
//                    TextView communityanswer_child1_name = respondView.findViewById(R.id.communityanswer_child1_name);
//                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_child1_name.getText().toString(),false);
//                    mCustomDialog.setOnKeyListener(keylistener);
//                    mCustomDialog.show();
//                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
//                        @Override
//                        public void publish() {
//
//                        }
//
//                        @Override
//                        public void image() {
//
//                        }
//                    });
//                });
//            }
            //点击评论，对其进行回复
            LinearLayout communityanswer_child_function_discuss = view.findViewById(R.id.communityanswer_child_function_discuss);
            communityanswer_child_function_discuss.setOnClickListener(v->{
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"评论",false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {

                    }

                    @Override
                    public void image() {

                    }
                });
            });
            //超过三条，显示查看全部
            //点击查看评论详情
            LinearLayout communityanswer_child_content1 = view.findViewById(R.id.communityanswer_child_content1);
            communityanswer_child_content1.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
//            //点击查看全部评论进入评论详情
//            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
//                CommunityAnswerDetailsShow();
//            });
        }
        {
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
//            //去掉顶
            ImageView communityanswer_child_top = view.findViewById(R.id.communityanswer_child_top);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) communityanswer_child_top.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_top.setLayoutParams(ll);
            //去掉精
            ImageView communityanswer_child_fine = view.findViewById(R.id.communityanswer_child_fine);
            ll = (LinearLayout.LayoutParams) communityanswer_child_fine.getLayoutParams();
            ll.width = 0;
            ll.rightMargin = 0;
            communityanswer_child_fine.setLayoutParams(ll);
            communityanswer_linearlayout.addView(view);
            //添加图片
            GridLayout communityanswer_child_imagelayout = view.findViewById(R.id.communityanswer_child_imagelayout);
//            {
//                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//                Glide.with(mControlMainActivity).
//                        load("").listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                        return false;
//                    }
//                }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//                communityanswer_child_imagelayout.addView(imageView);
//            }
            {
                View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
                ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
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
                        .error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
                communityanswer_child_imagelayout.addView(imageView);
            }
            //添加部分评论，此页最多显示三条
            LinearLayout communityanswer_child_body = view.findViewById(R.id.communityanswer_child_body);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) communityanswer_child_body.getLayoutParams();
            rl.topMargin = (int) view.getResources().getDimension(R.dimen.dp15);
            communityanswer_child_body.setLayoutParams(rl);
            communityanswer_child_body.setPadding(0,0,0, (int) view.getResources().getDimension(R.dimen.dp10));
            LinearLayout communityanswer_child_content = view.findViewById(R.id.communityanswer_child_content);
//            {
//                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
//                communityanswer_child_content.addView(respondView);
//                respondView.setOnClickListener(v->{
//                    //回复人的名字
//                    TextView communityanswer_child1_name = respondView.findViewById(R.id.communityanswer_child1_name);
//                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_child1_name.getText().toString(),false);
//                    mCustomDialog.setOnKeyListener(keylistener);
//                    mCustomDialog.show();
//                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
//                        @Override
//                        public void publish() {
//
//                        }
//
//                        @Override
//                        public void image() {
//
//                        }
//                    });
//                });
//            }
//            {
//                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
//                communityanswer_child_content.addView(respondView);
//                respondView.setOnClickListener(v->{
//                    //回复人的名字
//                    TextView communityanswer_child1_name = respondView.findViewById(R.id.communityanswer_child1_name);
//                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_child1_name.getText().toString(),false);
//                    mCustomDialog.setOnKeyListener(keylistener);
//                    mCustomDialog.show();
//                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
//                        @Override
//                        public void publish() {
//
//                        }
//
//                        @Override
//                        public void image() {
//
//                        }
//                    });
//                });
//            }
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
                respondView.setOnClickListener(v->{
                    //回复人的名字
                    TextView communityanswer_child1_name = respondView.findViewById(R.id.communityanswer_child1_name);
                    mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_child1_name.getText().toString(),false);
                    mCustomDialog.setOnKeyListener(keylistener);
                    mCustomDialog.show();
                    mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                        @Override
                        public void publish() {

                        }

                        @Override
                        public void image() {

                        }
                    });
                });
            }
            //       判断当前的评论是否超过三条，如果评论超过三条显示查看全部
            LinearLayout communityanswer_child_lookalldiscuss = view.findViewById(R.id.communityanswer_child_lookalldiscuss);
            ll = (LinearLayout.LayoutParams) communityanswer_child_lookalldiscuss.getLayoutParams();
            ll.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ll.topMargin = (int) view.getResources().getDimension(R.dimen.dp10);
            communityanswer_child_lookalldiscuss.setLayoutParams(ll);
            //点击评论，对其进行回复
            LinearLayout communityanswer_child_function_discuss = view.findViewById(R.id.communityanswer_child_function_discuss);
            communityanswer_child_function_discuss.setOnClickListener(v->{
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"评论",false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {

                    }

                    @Override
                    public void image() {

                    }
                });
            });
            //点击查看评论详情
            LinearLayout communityanswer_child_content1 = view.findViewById(R.id.communityanswer_child_content1);
            communityanswer_child_content1.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
            //点击查看全部评论进入评论详情
            communityanswer_child_lookalldiscuss.setOnClickListener(v->{
                CommunityAnswerDetailsShow();
            });
        }
    }




    //添加----社区问答的列表
    public void CommunityAnswerAddInit(boolean m_isInit){
        if (mview == null) {
            return;
        }
        mControlMainActivity.Page_onCommunityAnswerAdd();
        HideAllLayout();
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        if (mCommunityAnswerAddView == null) {
            mCommunityAnswerAddView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_add, null);
        }
        RecyclerView communityanswer_add_layout_image = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_image);
        communityanswer_add_layout_image.setLayoutManager(new GridLayoutManager(mControlMainActivity, 3));
        selPhotosPath = new ArrayList<>();
        //=============图片九宫格=========================//
        mPictureAdapter = null;
        //图片集合
        mPictureBeansList = new ArrayList<>();
        //设置布局管理器
        mRecyclerView = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_image);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mControlMainActivity, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        if(mPictureAdapter == null){
            //设置适配器
            mPictureAdapter = new ControllerPictureAdapter(mControlMainActivity, mPictureBeansList);
            mRecyclerView.setAdapter(mPictureAdapter);
            //添加分割线
            //设置添加删除动画
            //调用ListView的setSelected(!ListView.isSelected())方法，这样就能及时刷新布局
            mRecyclerView.setSelected(true);
        }else{
            mPictureAdapter.notifyDataSetChanged();
        }
        //图片九宫格点击事件
        mPictureAdapter.setOnItemClickLitener(new ControllerPictureAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View v,int position) {
                //打开自定义的图片预览对话框
                List<String> photos = mPictureAdapter.getAllPhotoPaths();
                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                NewImagePagerDialogFragment newImagePagerDialogFragment = NewImagePagerDialogFragment.getInstance(mControlMainActivity,photos,position,screenLocation, v.getWidth(),
                        v.getHeight(),false);
                newImagePagerDialogFragment.show(mControlMainActivity.getSupportFragmentManager(),"preview img");
            }

            @Override
            public void onItemAddClick() {
                PhotoPicker.builder()
                        .setPhotoCount(mPictureAdapter.MAX)
                        .setGridColumnCount(3)
//                        .setSelected(selPhotosPath)
                        .start(mControlMainActivity, ControllerGlobals.CHOOSE_PIC_REQUEST_CODE);

            }

            @Override
            public void onItemDeleteClick(View view, int position){
                mPictureBeansList.remove(position);
                mPictureAdapter.notifyDataSetChanged();
                if (mPictureBeansList.size() == 0) {
                    mQuestionPublishImage = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.blackff333333));
                } else {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.black999999));
                }
            }
        });

        //下一步
        TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
        communityanswer_add_layout_next_button1.setClickable(true);
        communityanswer_add_layout_next_button1.setOnClickListener(v->{
            if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                if (!mQuestionPublishTitle) {
                    //弹出提示，必须添加问题标题
                    Toast.makeText(mControlMainActivity, "您还没有输入问题标题", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!mQuestionPublishContent) {
                    //弹出提示，必须有问题内容
                    Toast.makeText(mControlMainActivity, "您还没有输入问题", Toast.LENGTH_LONG).show();
                    return;
                }
                communityanswer_add_layout_contentedittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentedittext);
                if (communityanswer_add_layout_contentedittext.getText().toString().length() < 10) {
                    //弹出提示，内容不允许少于10个字
                    Toast.makeText(mControlMainActivity, "内容不允许少于10个字", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (!mIsPublish){
                Toast.makeText(mControlMainActivity,"正在发布问题，请稍后！",Toast.LENGTH_LONG).show();
                return;
            }
            mControlMainActivity.setmState("发布问答");
            mIsPublish = false;
            //communityanswer_add_layout_contentetitledittext    communityanswer_add_layout_contentedittext
            String name = communityanswer_add_layout_contentetitledittext.getText().toString();
            String context = communityanswer_add_layout_contentetitledittext.getText().toString();
            //点击发布问题
            if (selPhotosPath.size() == 0 ){ //如果没有图片直接发送内容
                Toast.makeText(mControlMainActivity, "图片集合为空", Toast.LENGTH_SHORT).show();
               //不要图片   加载网络请求
            } else if (selPhotosPath!=null) {//如果有图片先上传图片在加载网络请求
                Toast.makeText(mControlMainActivity, "图片集合不为空", Toast.LENGTH_SHORT).show();
               // upLoadAnswerImage(name,context);
            }
            //点击下一步选择标签
            CommunityAnswerChooseSign();
        });
        EditText communityanswer_add_layout_contentedittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentedittext);
//        //设置标题输入框最先获取焦点 弹出输入法
//        communityanswer_add_layout_contentetitledittext.setFocusable(true);
//        communityanswer_add_layout_contentetitledittext.setFocusableInTouchMode(true);
//        communityanswer_add_layout_contentetitledittext.requestFocus();
//        communityanswer_add_layout_contentetitledittext.setSelection(communityanswer_add_layout_contentetitledittext.getText().toString().length());
        communityanswer_add_layout_contentedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    mQuestionPublishContent = true;
                } else {
                    mQuestionPublishContent = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.blackff333333));
                } else {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.black999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //发布信息的标题
        communityanswer_add_layout_contentetitledittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentetitledittext);
        communityanswer_add_layout_contentetitledittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    mQuestionPublishTitle = true;
                } else {
                    mQuestionPublishTitle = false;
                }
                if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.blackff333333));
                } else {
                    TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
                    communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.black999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        communityanswer_layout_main.addView(mCommunityAnswerAddView);
        if (m_isInit) { //如果是从社区问答主页跳转进来的 需要将所有东西初始化一下
            mQuestionPublishTitle = false;
            mQuestionPublishContent = false;
            mQuestionPublishImage = false;
            mCommunityAnswerChooseSignList.clear();
            if (mPictureBeansList != null) {
                mPictureBeansList.clear();
            }
            if (selPhotosPath != null) {
                selPhotosPath.clear();
                mPictureBeansList.clear();
            }
            //先查询数据库中草稿箱中是否有未完成的问答
            Cursor cursor = ModelSearchRecordSQLiteOpenHelper.getReadableDatabase(mControlMainActivity).rawQuery(
                    "select * from communityanswerdraftbox ", null);
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex("title");
                int contentIndex = cursor.getColumnIndex("content");
                int photospathIndex = cursor.getColumnIndex("photospath");
                int signIndex = cursor.getColumnIndex("sign");
                String title = cursor.getString(titleIndex);
                String content = cursor.getString(contentIndex);
                String photospath = cursor.getString(photospathIndex);
                String sign = cursor.getString(signIndex);
                if (title != null) {
                    communityanswer_add_layout_contentetitledittext.setText(title);
                    if (!title.equals("")) {
                        mQuestionPublishTitle = true;
                    }
                }
                if (content != null) {
                    communityanswer_add_layout_contentedittext.setText(content);
                    if (!content.equals("")) {
                        mQuestionPublishContent = true;
                    }
                }
                if (photospath != null) {
                    String photospathS[] = photospath.split(";");
                    for (int i = 0; i < photospathS.length; i++) {
                        if (photospathS[i].equals("")) {
                            continue;
                        }
                        selPhotosPath.add(photospathS[i]);
                    }
                    for (String path : selPhotosPath) {
                        ControllerPictureBean pictureBean = new ControllerPictureBean();
                        pictureBean.setPicPath(path);
                        pictureBean.setPicName(ControllerGlobals.getFileName(path));
                        //去掉总数目的限制，这里通过增大MAX的数字来实现
                        if (mPictureBeansList.size() < mPictureAdapter.MAX) {
                            mPictureBeansList.add(pictureBean);
                        } else {
                            Toast.makeText(mControlMainActivity, "最多可以选择" + mPictureAdapter.MAX + "张图片", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    mPictureAdapter.notifyDataSetChanged();
                }
                if (sign != null) {
                    String signS[] = sign.split(";");
                    for (int i = 0; i < signS.length; i++) {
                        if (signS[i].equals("")) {
                            continue;
                        }
                        mCommunityAnswerChooseSignList.add(signS[i]);
                    }
                }
                break;
            }
            cursor.close();
        }
    }
    public void CommunityAnswerPictureAdd(Intent data){
        //添加图片，发布按钮改为蓝色
        mQuestionPublishImage = true;
        if (mQuestionPublishImage || mQuestionPublishTitle || mQuestionPublishContent) {
            TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
            communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.blackff333333));
        } else {
            TextView communityanswer_add_layout_next_button1 = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_next_button1);
            communityanswer_add_layout_next_button1.setTextColor(mCommunityAnswerView.getResources().getColor(R.color.black999999));
        }
        if (data != null) {
            selPhotosPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
        }
        if (selPhotosPath != null) {

            //下面的代码主要用于这样一个场景，就是注释了.setSelected(selPhotosPath)之后，还想要保证选择的图片不重复
					/*for(String path : selPhotosPath){
						Log.w(TAG,"path="+path);///storage/emulated/0/tempHxzk/IMG_1498034535796.jpg
						boolean existThisPic = false;
						for(int i=0;i<mPictureBeansList.size();i++){
							if(path.equals(mPictureBeansList.get(i).getPicPath())){
								//如果新选择的图片集合中存在之前选中的图片，那么跳过去
								existThisPic = true;
								break;
							}
						}
						if(! existThisPic){
							PictureBean pictureBean = new PictureBean();
							pictureBean.setPicPath(path);
							pictureBean.setPicName(getFileName(path));
							//去掉总数目的限制，这里通过增大MAX的数字来实现
							if (mPictureBeansList.size() < mPictureAdapter.MAX) {
								mPictureBeansList.add(pictureBean);
							} else {
								Toast.makeText(MainActivity.this, "最多可以选择" + mPictureAdapter.MAX + "张图片", Toast.LENGTH_SHORT).show();
								break;
							}
						}
					}*/

            //是常规操作，和上面的代码不可共存
            for (String path : selPhotosPath) {
                ControllerPictureBean pictureBean = new ControllerPictureBean();
                pictureBean.setPicPath(path);
                pictureBean.setPicName(ControllerGlobals.getFileName(path));
                //去掉总数目的限制，这里通过增大MAX的数字来实现
                if (mPictureBeansList.size() < mPictureAdapter.MAX) {
                    mPictureBeansList.add(pictureBean);
                } else {
                    Toast.makeText(mControlMainActivity, "最多可以选择" + mPictureAdapter.MAX + "张图片", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            mPictureAdapter.notifyDataSetChanged();
        }
    }
    //选择标签----选择标签
    private void CommunityAnswerChooseSign(){
        if (mview == null) {
            return;
        }
        mControlMainActivity.Page_onCommunityAnswerChooseSign();
        HideAllLayout();
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        if (mCommunityAnswerChooseSignView == null) {
            mCommunityAnswerChooseSignView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_choosesign, null);
        }
        communityanswer_layout_main.addView(mCommunityAnswerChooseSignView);
        //                   发表按钮
        TextView communityanswer_choosesign_layout_commit_button1 = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_layout_commit_button1);
        communityanswer_choosesign_layout_commit_button1.setOnClickListener(v->{
            //点击发表问答，先判断是否有选择标签，如果没有选择标签，不做处理
            if (mCommunityAnswerChooseSignList.size() != 0){
                //发表，清空草稿箱中的文字，并返回到问答首页
                //发表标签的网络请求
                ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).execSQL("delete from communityanswerdraftbox");
            }
        });
        //选中的标签个数
        TextView communityanswer_choosesign_choosecount = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_choosecount);
        //添加标签
        ControllerWarpLinearLayout communityanswer_choosesign_warpLinearLayout = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_warpLinearLayout);
        communityanswer_choosesign_warpLinearLayout.removeAllViews();
         //标签赋值和点击标签的状态变化      for循环赋值刷新页面
        //getCommunitylabelData();      //社区问答标签
        //刷新界面
        // CommunityAnswerChooseSign();
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            //选择标签的网络请求
            communityanswer_selectpop_child_signname.setText("啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            communityanswer_selectpop_child_signname.setHint("0");
            communityanswer_choosesign_warpLinearLayout.addView(view);
            view.setOnClickListener(v->{
                //如果已经是选中的标签，再次点击取消选中状态
                for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
                    String chooseSign = mCommunityAnswerChooseSignList.get(i);
                    if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
                        communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.grayff999999));
                        communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5));
                        mCommunityAnswerChooseSignList.remove(i);
                        communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
                        if (mCommunityAnswerChooseSignList.size() == 0){
                            communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.black999999));
                        }
                        return;
                    }
                }
                //点击选中
                if (mCommunityAnswerChooseSignList.size() >= 3){
                    Toast.makeText(mControlMainActivity, "最多选择三个！", Toast.LENGTH_LONG).show();
                    return;
                }
                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
                communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5));
                String hint = communityanswer_selectpop_child_signname.getHint().toString();
                mCommunityAnswerChooseSignList.add(hint);
                //重置发表按钮颜色
                if (mCommunityAnswerChooseSignList.size() != 0){
                    communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.blackff333333));
                }
                //重置选中标签的数量
                communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
            });
            //没有标签默认选中第一个
            if (mCommunityAnswerChooseSignList.size() == 0){
                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
                mCommunityAnswerChooseSignList.add(communityanswer_selectpop_child_signname.getHint().toString());
                communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5),
                        (int) view.getResources().getDimension(R.dimen.dp5));
                communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
                //重置发表按钮颜色
                if (mCommunityAnswerChooseSignList.size() != 0){
                    communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.blackff333333));
                }
            } else {
                for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
                    String chooseSign = mCommunityAnswerChooseSignList.get(i);
                    if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
                        communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
                        communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5),
                                (int) view.getResources().getDimension(R.dimen.dp5));
                        break;
                    }
                }
            }
        }
        //测试数据2
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("技术基础实务");
//            communityanswer_choosesign_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("1");
//            view.setOnClickListener(v->{
//                //如果已经是选中的标签，再次点击取消选中状态
//                for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                    String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                    if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                        communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5));
//                        mCommunityAnswerChooseSignList.remove(i);
//                        communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//                        if (mCommunityAnswerChooseSignList.size() == 0){
//                            communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.black999999));
//                        }
//                        return;
//                    }
//                }
//                //点击选中
//                if (mCommunityAnswerChooseSignList.size() >= 3){
//                    Toast.makeText(mControlMainActivity, "最多选择三个！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5));
//                String hint = communityanswer_selectpop_child_signname.getHint().toString();
//                mCommunityAnswerChooseSignList.add(hint);
//                //重置发表按钮颜色
//                if (mCommunityAnswerChooseSignList.size() != 0){
//                    communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.blackff333333));
//                }
//                //重置选中标签的数量
//                communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//            });
//            for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                    communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                    communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                    communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5));
//                    break;
//                }
//            }
//        }
        //测试数据3
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("aaaaa");
//            communityanswer_choosesign_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("2");
//            view.setOnClickListener(v->{
//                //如果已经是选中的标签，再次点击取消选中状态
//                for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                    String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                    if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                        communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5));
//                        mCommunityAnswerChooseSignList.remove(i);
//                        communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//                        if (mCommunityAnswerChooseSignList.size() == 0){
//                            communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.black999999));
//                        }
//                        return;
//                    }
//                }
//                //点击选中
//                if (mCommunityAnswerChooseSignList.size() >= 3){
//                    Toast.makeText(mControlMainActivity, "最多选择三个！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5));
//                String hint = communityanswer_selectpop_child_signname.getHint().toString();
//                mCommunityAnswerChooseSignList.add(hint);
//                //重置发表按钮颜色
//                if (mCommunityAnswerChooseSignList.size() != 0){
//                    communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.blackff333333));
//                }
//                //重置选中标签的数量
//                communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//            });
//            for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                    communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                    communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                    communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5));
//                    break;
//                }
//            }
//        }
        //测试数据4
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("消防考试专业课");
//            communityanswer_choosesign_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("3");
//            view.setOnClickListener(v->{
//                //如果已经是选中的标签，再次点击取消选中状态
//                for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                    String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                    if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                        communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5),
//                                (int) view.getResources().getDimension(R.dimen.dp5));
//                        mCommunityAnswerChooseSignList.remove(i);
//                        communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//                        if (mCommunityAnswerChooseSignList.size() == 0){
//                            communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.black999999));
//                        }
//                        return;
//                    }
//                }
//                //点击选中
//                if (mCommunityAnswerChooseSignList.size() >= 3){
//                    Toast.makeText(mControlMainActivity, "最多选择三个！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5),
//                        (int) view.getResources().getDimension(R.dimen.dp5));
//                String hint = communityanswer_selectpop_child_signname.getHint().toString();
//                mCommunityAnswerChooseSignList.add(hint);
//                //重置发表按钮颜色
//                if (mCommunityAnswerChooseSignList.size() != 0){
//                    communityanswer_choosesign_layout_commit_button1.setTextColor(view.getResources().getColor(R.color.blackff333333));
//                }
//                //重置选中标签的数量
//                communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
//            });
//            for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
//                String chooseSign = mCommunityAnswerChooseSignList.get(i);
//                if (chooseSign.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                    communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                    communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//                    communityanswer_selectpop_child_signname.setPadding((int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5),
//                            (int) view.getResources().getDimension(R.dimen.dp5));
//                    break;
//                }
//            }
//        }
        communityanswer_choosesign_choosecount.setText(String.valueOf(mCommunityAnswerChooseSignList.size()));
    }

    //文件的搜索
    private void CommunityAnswerSelectShow(){
        if (mview == null) {
            return;
        }
        HideAllLayout();
        mControlMainActivity.Page_onCommunityAnswerSearch();
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        if (mCommunityAnswerSelectView == null) {
            mCommunityAnswerSelectView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_select, null);
            ModelSearchView communityanswer_search_view = mCommunityAnswerSelectView.findViewById(R.id.communityanswer_search_view);
            communityanswer_search_view.init("communityanswersearchrecords");
            // 4. 设置点击搜索按键后的操作（通过回调接口）
            // 参数 = 搜索框输入的内容,,,,,,,,.
            communityanswer_search_view.setOnClickSearch(string ->{
               // System.out.println("我收到了" + string);
                Toast.makeText(mControlMainActivity, "我查询参数是"+string, Toast.LENGTH_SHORT).show();
                //getCommunityKeyWordSearchBeanData();
            });
            // 5. 设置点击返回按键后的操作（通过回调接口）
            communityanswer_search_view.setOnClickBack(()->{
                mControlMainActivity.Page_CommunityAnswer();
            });
        }
        communityanswer_layout_main.addView(mCommunityAnswerSelectView);
    }
    //社区子条目详情
    public void CommunityAnswerDetailsShow() {
        if (mview == null) {
            return;
        }
        HideAllLayout();
        mControlMainActivity.Page_onCommunityAnswerDetails();
        //详情的评论
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        if (mCommunityAnswerDetailsView == null) {
            mCommunityAnswerDetailsView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_details, null);
            //下滑刷新处理
            //Smart_model_communityanswer_detalis
            mSmart_model_communityanswer_detalis = mCommunityAnswerDetailsView.findViewById(R.id.Smart_model_communityanswer_detalis);
            mSmart_model_communityanswer_detalis.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_communityanswer_detalis.finishLoadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mSmart_model_communityanswer_detalis.finishRefresh();
                }
            });
            //点击回复问题
            LinearLayout communityanswer_datails_content1 = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_content1);
            communityanswer_datails_content1.setOnClickListener(v->{
                //学员名字
                TextView communityanswer_datails_name = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_name);
                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_datails_name.getText().toString(),false);
                mCustomDialog.setOnKeyListener(keylistener);
                mCustomDialog.show();
                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                    @Override
                    public void publish() {

                    }

                    @Override
                    public void image() {

                    }
                });
            });
        }
        //评论的主评论的标题赋值
        communityanswer_layout_main.addView(mCommunityAnswerDetailsView);
        //社区问答评论组标题
        initCommunityComment();
        //子布局的总布局带页面的自定义view线
        communityanswer_datails_linearlayout = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_linearlayout);
        communityanswer_datails_linearlayout.removeAllViews();
        //测试数据子条目学员评论条目
        initCommunityItem();

//        {
//            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_details1, null);
//            communityanswer_datails_linearlayout.addView(view);
//            view.setOnClickListener(v->{
//                TextView communityanswer_datails1_name = view.findViewById(R.id.communityanswer_datails1_name);
//                mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_datails1_name.getText().toString(),false);
//                mCustomDialog.setOnKeyListener(keylistener);
//                mCustomDialog.show();
//                mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
//                    @Override
//                    public void publish() {
//
//                    }
//
//                    @Override
//                    public void image() {
//
//                    }
//                });
//            });
//        }
    }

    private void initCommunityComment() {
        //子条目的评论title
        TextView communityanswer_datails_titletext = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_titletext);
        communityanswer_datails_titletext.setText("社区问答评论");
        //学员名字
        TextView communityanswer_datails_name = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_name);
        communityanswer_datails_name.setText("我是学员的名字");
        //学员的头像
        ControllerCustomRoundAngleImageView communityanswer_datails_headportrait = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_headportrait);
        Glide.with(getActivity()).load("").into(communityanswer_datails_headportrait);
        //学员的时间
        TextView communityanswer_datails_time = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_time);
        communityanswer_datails_time.setText("我使学员的时间");
        //学员的标题
        TextView communityanswer_datails_title = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_title);
        communityanswer_datails_title.setText("我是问答标题");
        //学员的内容
        TextView communityanswer_datails_message = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_message);
        communityanswer_datails_message.setText("我是问答学员的内容");
        //学员的标签1    communityanswer_datails_sign1
        TextView communityanswer_datails_sign1 = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_sign1);
        communityanswer_datails_sign1.setText("我是学员标签1");
        //学员的标签2    communityanswer_datails_sign2
        TextView communityanswer_datails_sign2 = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_sign2);
        communityanswer_datails_sign2.setText("我是学员标签2");
        //学员的标签3    communityanswer_datails_sign3
        TextView communityanswer_datails_sign3 = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_sign3);
        communityanswer_datails_sign3.setText("我是学员标签3");
        //图片区
        GridLayout communityanswer_datails_imagelayout = mCommunityAnswerDetailsView.findViewById(R.id.communityanswer_datails_imagelayout);
        communityanswer_datails_imagelayout.removeAllViews();
        //多张图片for循环添加
        {
            View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
            ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
            Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
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
            }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
            communityanswer_datails_imagelayout.addView(imageView);
        }
//        {
//            View imageView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.controllercustomroundangleimageview_layout, null);
//            ControllerCustomRoundAngleImageView CustomRoundAngleImageView = imageView.findViewById(R.id.CustomRoundAngleImageView);
//            Glide.with(mControlMainActivity).load("").listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                    return false;
//                }
//                @Override
//                public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                    return false;
//                }
//            }).error(mControlMainActivity.getResources().getDrawable(R.drawable.modelcoursecover)).into(CustomRoundAngleImageView);
//            communityanswer_datails_imagelayout.addView(imageView);
//        }
    }

    //子评论中的学员回复信息
    private void initCommunityItem() {
        //for循环获取网络数据赋值
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_details1, null);
        //子条目学员的头像
        ControllerCustomRoundAngleImageView mcommunityanswer_datails1_headportrait = view.findViewById(R.id.communityanswer_datails1_headportrait);
        Glide.with(getActivity()).load("").into(mcommunityanswer_datails1_headportrait);
        //学员的姓名
        TextView communityanswer_datails1_name = view.findViewById(R.id.communityanswer_datails1_name);
        communityanswer_datails1_name.setText("我是学员的姓名");
        //时间
        TextView communityanswer_datails1_time = view.findViewById(R.id.communityanswer_datails1_time);
        communityanswer_datails1_time.setText("我是时间");
        //内容
        TextView communityanswer_datails1_message = view.findViewById(R.id.communityanswer_datails1_message);
        communityanswer_datails1_message.setText("我是内容");
        communityanswer_datails_linearlayout.addView(view);

        view.setOnClickListener(v->{
            mCustomDialog = new ControllerCustomDialog(mControlMainActivity, R.style.customdialogstyle,"回复 " + communityanswer_datails1_name.getText().toString(),false);
            mCustomDialog.setOnKeyListener(keylistener);
            mCustomDialog.show();
            mCustomDialog.setOnClickPublishOrImagelistener(new ControllerCustomDialog.OnClickPublishOrImage() {
                @Override
                public void publish() {

                }

                @Override
                public void image() {

                }
            });
        });
    }

    //隐藏所有图层
    private void HideAllLayout(){
        LinearLayout communityanswer_layout_main = mview.findViewById(R.id.communityanswer_layout_main);
        communityanswer_layout_main.removeAllViews();
    }

    //添加问答的返回
    public void CommunityAnswerAddReturn(){
        String title = "";
        String content = "";
        String photosPath = "";
        String sign = "";
        if (mCommunityAnswerAddView != null){ //必须放在界面跳转之前拿到数据
            EditText communityanswer_add_layout_contentetitledittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentetitledittext);
            title = communityanswer_add_layout_contentetitledittext.getText().toString();
            EditText communityanswer_add_layout_contentedittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentedittext);
            content = communityanswer_add_layout_contentedittext.getText().toString();
        }
        CommunityAnswerMainShow();
        //如果有编辑内容的话
        if (mQuestionPublishTitle || mQuestionPublishContent || mQuestionPublishImage){
            //弹出提示，已存入草稿箱
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure, null);
            mMyDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
            TextView tip = view.findViewById(R.id.tip);
            tip.setText("已存入草稿箱");
            TextView dialog_content = view.findViewById(R.id.dialog_content);
            dialog_content.setText("点击提问按钮，可再次编辑");
            TextView button_sure = view.findViewById(R.id.button_sure);
            button_sure.setText("知道了");
            button_sure.setTextColor(view.getResources().getColor(R.color.blue649cf0));
            button_sure.setOnClickListener(View->{
                mMyDialog.cancel();
            });
            //获取添加图片的路径  ；分割
            for (int i = 0; i < mPictureBeansList.size() ; i ++){
                if (i == mPictureBeansList.size() - 1){
                    photosPath = photosPath + mPictureBeansList.get(i).getPicPath();
                } else {
                    photosPath = photosPath + mPictureBeansList.get(i).getPicPath() + ";";
                }
            }
            //获取标签  ；分割
            for (int i = 0; i < mCommunityAnswerChooseSignList.size() ; i ++){
                if (i == mCommunityAnswerChooseSignList.size() - 1){
                    sign = sign + mCommunityAnswerChooseSignList.get(i);
                } else {
                    sign = sign + mCommunityAnswerChooseSignList.get(i) + ";";
                }
            }
            //将数据存储到本地数据库草稿箱  selPhotosPath:图片路径集合；content 内容；title ：标题  mCommunityAnswerChooseSignList：标签
            ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).
                    execSQL("delete from communityanswerdraftbox");
            ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).
                    execSQL("insert into communityanswerdraftbox(title,content,photospath,sign) values('" + title + "','" + content + "','" + photosPath + "','" + sign + "')");
        }
    }
    /**
   * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
   *
   */
    class popupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
    protected void initPopupWindow() {
        View popupWindowView = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop, null);
        int height1 = (int) (getScreenHeight() - mview.getResources().getDimension(R.dimen.dp45) - getStateBar());
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, (int) mview.getResources().getDimension(R.dimen.dp_280), height1, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(mControlMainActivity.getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.RIGHT, 0, 500);
        popupWindow.setBackgroundDrawable(null);
        //设置背景半透明
        backgroundAlpha(0.9f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener((v, event) -> {
            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });


        //添加搜索标签的搜索接口
        ControllerWarpLinearLayout communityanswer_select_warpLinearLayout = popupWindowView.findViewById(R.id.communityanswer_select_warpLinearLayout);
        communityanswer_select_warpLinearLayout.removeAllViews();
        mCommunityAnswerSelectTemp = mCommunityAnswerSelect;
        //必须有的标签-全部:默认选中全部
        //获取网络数据 给搜索标签赋值刷新页面
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("全部");
            communityanswer_selectpop_child_signname.setHint("-1");
            communityanswer_select_warpLinearLayout.addView(view);
            view.setOnClickListener(v->{
                //将其他置为未选中
                String hint = "";
                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
                for (int i = 0; i < childcount ; i ++){
                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
                    if (childView == null){
                        continue;
                    }
                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
                    if (childView == view){
                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                    }
                }
                //将选中项置为当前选中项id
                mCommunityAnswerSelectTemp = hint;
            });
            if (mCommunityAnswerSelect.equals("-1")) {
                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //测试数据
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("技术基础实务");
//            communityanswer_select_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("1");
//            view.setOnClickListener(v->{
//                //将其他置为未选中
//                String hint = "";
//                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
//                for (int i = 0; i < childcount ; i ++){
//                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
//                    if (childView == null){
//                        continue;
//                    }
//                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
//                    if (childView == view){
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
//                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
//                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                    }
//                }
//                //将选中项置为当前选中项id
//                mCommunityAnswerSelectTemp = hint;
//            });
//            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//            }
//        }
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("aaaaa");
//            communityanswer_select_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("2");
//            view.setOnClickListener(v->{
//                //将其他置为未选中
//                String hint = "";
//                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
//                for (int i = 0; i < childcount ; i ++){
//                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
//                    if (childView == null){
//                        continue;
//                    }
//                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
//                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
//                    if (childView == view){
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
//                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
//                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                    }
//                }
//                //将选中项置为当前选中项id
//                mCommunityAnswerSelectTemp = hint;
//            });
//            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//            }
//        }
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("aaa");
//            communityanswer_select_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("3");
//            view.setOnClickListener(v->{
//                //将其他置为未选中
//                String hint = "";
//                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
//                for (int i = 0; i < childcount ; i ++){
//                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
//                    if (childView == null){
//                        continue;
//                    }
//                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
//                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
//                    if (childView == view){
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
//                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                    }
//                }
//                //将选中项置为当前选中项id
//                mCommunityAnswerSelectTemp = hint;
//            });
//            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//            }
//        }
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("aaa");
//            communityanswer_select_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("4");
//            view.setOnClickListener(v->{
//                //将其他置为未选中
//                String hint = "";
//                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
//                for (int i = 0; i < childcount ; i ++){
//                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
//                    if (childView == null){
//                        continue;
//                    }
//                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
//                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
//                    if (childView == view){
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
//                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                    }
//                }
//                //将选中项置为当前选中项id
//                mCommunityAnswerSelectTemp = hint;
//            });
//            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//            }
//        }
//        {
//            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
//            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
//            communityanswer_selectpop_child_signname.setText("aaa");
//            communityanswer_select_warpLinearLayout.addView(view);
//            communityanswer_selectpop_child_signname.setHint("5");
//            view.setOnClickListener(v->{
//                //将其他置为未选中
//                String hint = "";
//                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
//                for (int i = 0; i < childcount ; i ++){
//                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
//                    if (childView == null){
//                        continue;
//                    }
//                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
//                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
//                    if (childView == view){
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
//                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
//                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
//                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
//                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
//                    }
//                }
//                //将选中项置为当前选中项id
//                mCommunityAnswerSelectTemp = hint;
//            });
//            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
//                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
//                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
//            }
//        }
        //传入name和id
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("半步");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("6");
            view.setOnClickListener(v->{
                //将其他置为未选中
                String hint = "";
                int childcount = communityanswer_select_warpLinearLayout.getChildCount();
                for (int i = 0; i < childcount ; i ++){
                    View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
                    if (childView == null){
                        continue;
                    }
                    TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
                    int padding = (int) view.getResources().getDimension(R.dimen.dp5);
                    if (childView == view){
                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
                        hint = communityanswer_selectpop_child_signname1.getHint().toString();
                    } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
                        communityanswer_selectpop_child_signname1.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect));
                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.grayff999999));
                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
                    }
                }
                //将选中项置为当前选中项id
                mCommunityAnswerSelectTemp = hint;
            });
            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        //点击确定加载网络数据刷新页面   重置页面
        TextView communityanswer_select_buttonsure = popupWindowView.findViewById(R.id.communityanswer_select_buttonsure);
        communityanswer_select_buttonsure.setOnClickListener(v->{
            mCommunityAnswerSelect = mCommunityAnswerSelectTemp;
            //请求网络数据关闭页面
            popupWindow.dismiss();
        });
        //点击重置
        TextView communityanswer_select_buttonreset = popupWindowView.findViewById(R.id.communityanswer_select_buttonreset);
        communityanswer_select_buttonreset.setOnClickListener(v->{
            //将其他置为未选中
            int childcount = communityanswer_select_warpLinearLayout.getChildCount();
            for (int i = 0; i < childcount ; i ++){
                View childView = communityanswer_select_warpLinearLayout.getChildAt(i);
                if (childView == null){
                    continue;
                }
                TextView communityanswer_selectpop_child_signname1 = childView.findViewById(R.id.communityanswer_selectpop_child_signname);
                int padding = (int) childView.getResources().getDimension(R.dimen.dp5);
                if (communityanswer_selectpop_child_signname1.getHint().toString().equals("-1")){
                    communityanswer_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                    communityanswer_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.white));
                    communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
                } else if (communityanswer_selectpop_child_signname1.getHint().toString().equals(mCommunityAnswerSelectTemp)){ // 如果上个找到上一个选中的id，将其置为未选状态
                    communityanswer_selectpop_child_signname1.setBackground(childView.getResources().getDrawable(R.drawable.textview_style_rect));
                    communityanswer_selectpop_child_signname1.setTextColor(childView.getResources().getColor(R.color.grayff999999));
                    communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
                }
            }
            mCommunityAnswerSelectTemp = "-1";
            mCommunityAnswerSelect = "-1";
        });
    }
    /**
   * 设置添加屏幕的背景透明度
   * @param bgAlpha
   */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mControlMainActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mControlMainActivity.getWindow().setAttributes(lp);
    }

    //获取屏幕高度 不包含虚拟按键
    public static int getScreenHeight() {
        DisplayMetrics dm = mControlMainActivity.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private int getStateBar(){
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //社区问答—发布
    private static  void getCommunityissue(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("agreement_id", "agreement_id");//社区问答的参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyCommunityissue(body)
                .enqueue(new Callback<CommunityissueBean>() {
                    @Override
                    public void onResponse(Call<CommunityissueBean> call, Response<CommunityissueBean> response) {
                        CommunityissueBean communityissueBean = response.body();
                        if (communityissueBean!=null){
                         //社区问答发布   发布前需要先上传相关的图片
                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityissueBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
        }

    //社区问答的列表
    private static void getCommunityData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        retrofit.create(ModelObservableInterface.class)
                .queryAllCoursePackageCommunity()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
               .subscribe(new Observer<CommunityBean>() {
                   @Override
                   public void onSubscribe(Disposable d) {

                   }
                   @Override
                   public void onNext(CommunityBean communityBean) {
                       //获取网络数据
                       int code = communityBean.getCode();
                       if (code==200){
                           CommunityBean.DataBean beanData = communityBean.getData();
                           if (beanData!=null){
                               int uid = beanData.getUid();
                               String details = beanData.getDetails();
                               String label = beanData.getLabel();
                               String picture = beanData.getPicture();
                               int status = beanData.getStatus();
                               String title = beanData.getTitle();
                              //判断当前的状态
                           }
                       }
                   }
                   @Override
                   public void onError(Throwable e) {
                       Log.e(TAG, "onError: "+e.getMessage() );
                   }

                   @Override
                   public void onComplete() {

                   }
               });
    }
    //社区问答-选择标签      条件搜索（标签）
    public static void getCommunitylabelData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("", "");//社区问答---选择标签的参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryMyCommunitylabel(body)
                .enqueue(new Callback<CommunitylabelBan>() {
                    @Override
                    public void onResponse(Call<CommunitylabelBan> call, Response<CommunitylabelBan> response) {
                        CommunitylabelBan communitylabelBan = response.body();
                        if (communitylabelBan!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<CommunitylabelBan> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }
    //社区问答-----详情
    public static void getCommunityDetilsBeanData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("", "");//社区问答---选择标签的参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryCommunityDetilsBean(body)
                .enqueue(new Callback<CommunityDetilsBean>() {
                    @Override
                    public void onResponse(Call<CommunityDetilsBean> call, Response<CommunityDetilsBean> response) {
                        CommunityDetilsBean communityDetilsBean = response.body();
                        if (communityDetilsBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityDetilsBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }
    //社区问答查询标签
    public static void getCommunityQuerytagsBeanData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("", "");//社区问答---查询标签的参数
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        //queryMyCommunityQuerytags
        queryMyCourseList.queryMyCommunityQuerytags(body)
                .enqueue(new Callback<CommunityQuerytagsBean>() {
                    @Override
                    public void onResponse(Call<CommunityQuerytagsBean> call, Response<CommunityQuerytagsBean> response) {
                        CommunityQuerytagsBean querytagsBean = response.body();
                        if (querytagsBean!=null){
                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityQuerytagsBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
    }


    //社区问答-----回复
    public static void getCommunityDetilsreplyBeanData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("", "");//社区问答---回复
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryCommunityDetilsreplyBean(body)
                .enqueue(new Callback<CommunityreplyBean>() {
                    @Override
                    public void onResponse(Call<CommunityreplyBean> call, Response<CommunityreplyBean> response) {
                        CommunityreplyBean communityreplyBean = response.body();
                        if (communityreplyBean!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityreplyBean> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });

    }



    //社区问答--关键字搜索
    public static void getCommunityKeyWordSearchBeanData(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ModelObservableInterface.urlHead)
                .build();
        ModelObservableInterface queryMyCourseList = retrofit.create(ModelObservableInterface.class);
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("", "");//社区问答---回复
        String strEntity = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        queryMyCourseList.queryCommunityKeyWordSearchBean(body)
                .enqueue(new Callback<CommunityKeyWordSearch>() {
                    @Override
                    public void onResponse(Call<CommunityKeyWordSearch> call, Response<CommunityKeyWordSearch> response) {
                        CommunityKeyWordSearch keyWordSearch = response.body();
                        if (keyWordSearch!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityKeyWordSearch> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    //上传社区问答中的图片
    private void upLoadAnswerImage(String title,String content) {
        if (title.equals("") || content.equals("")){
            mControlMainActivity.setmState("");
            mIsPublish = true;
            Toast.makeText(mControlMainActivity, "问题发布失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    String uu = UUID.randomUUID().toString();
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "multipart/form-data; boundary=" + uu)
                            .build();
                    return chain.proceed(request);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ModelObservableInterface.urlHead)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        ModelObservableInterface modelObservableInterface = retrofit.create(ModelObservableInterface.class);
        Map<String, RequestBody> params=new HashMap<>() ;
        for (int i = 0; i < selPhotosPath.size(); i ++){
            File file = new File(selPhotosPath.get(i));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            params.put("file\"; filename=\""+ i + "#" + file.getName(), requestBody);
        }
        retrofit2.Call call = modelObservableInterface.upLoadImage(params);
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(retrofit2.Call call, retrofit2.Response response) {
                String imgs = "";
                if (imgs.isEmpty()){
                    //加载网络请求
                   // getCommunityissue();
                }
            }
            //图片上传失败
            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                Log.d("Tag",t.getMessage().toString());
                mControlMainActivity.setmState("");
                mIsPublish = true;
                Toast.makeText(mControlMainActivity, "问题发布时上传图像失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }





    //社区问答---关键字搜索
    public static  class CommunityKeyWordSearch{

    }

    //社区问答-选择标签      条件搜索（标签）
    public static  class CommunitylabelBan{

    }
    //社区问答-查询标签
    public static class CommunityQuerytagsBean{

    }
    //社区问答-回复
    public static class CommunityreplyBean{

    }
    //社区问答---发布
    public static class  CommunityissueBean{

    }
    //社区问答列表详情
    public static class CommunityDetilsBean{

    }


     //社区问答列表
    public static class CommunityBean{
        /**
         * code : 200
         * data : {"uid":1,"status":1,"title":"问答标题","details":"这个老师特别好，知识讲解很详细","picture":"","label":"消防安全实务"}
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
             * uid : 1
             * status : 1
             * title : 问答标题
             * details : 这个老师特别好，知识讲解很详细
             * picture :
             * label : 消防安全实务
             */

            private int uid;
            private int status;
            private String title;
            private String details;
            private String picture;
            private String label;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }
    }
}
