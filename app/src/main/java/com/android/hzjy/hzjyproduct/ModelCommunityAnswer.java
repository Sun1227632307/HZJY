package com.android.hzjy.hzjyproduct;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import net.sqlcipher.Cursor;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.fragment.NewImagePagerDialogFragment;

public class ModelCommunityAnswer extends Fragment{
    private static ControlMainActivity mControlMainActivity;
    private static String mContext="xxxxxxxxxxxxx";
    //要显示的页面
    static private int FragmentPage;
    private View mview,mCommunityAnswerView ,mCommunityAnswerSelectView ,mCommunityAnswerAddView ,mCommunityAnswerChooseSignView;
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
            ModelPtrFrameLayout communityanswer_ptr_frame = mCommunityAnswerView.findViewById(R.id.communityanswer_ptr_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            communityanswer_ptr_frame.addPtrUIHandler(header);
            communityanswer_ptr_frame.setHeaderView(header);
            communityanswer_ptr_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    communityanswer_ptr_frame.refreshComplete();
                }
            });
            //关键词搜索监听
            ImageView communityanswer_searchimage = mCommunityAnswerView.findViewById(R.id.communityanswer_searchimage);
            communityanswer_searchimage.setOnClickListener(v->{
                CommunityAnswerSelectShow();
            });
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

        //测试数据
        {
            View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child, null);
            communityanswer_linearlayout.addView(view);
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
        }
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
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
            }
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
            }
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
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
            }
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
            }
            {
                View respondView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.model_communityanswer_child1, null);
                communityanswer_child_content.addView(respondView);
            }
            //超过三条，显示查看全部
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
        }
    }

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
        //=============图片九宫格=========================
        mPictureAdapter = null;
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
                EditText communityanswer_add_layout_contentedittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentedittext);
                if (communityanswer_add_layout_contentedittext.getText().toString().length() < 10) {
                    //弹出提示，内容不允许少于10个字
                    Toast.makeText(mControlMainActivity, "内容不允许少于10个字", Toast.LENGTH_LONG).show();
                    return;
                }
                //点击下一步选择标签
                CommunityAnswerChooseSign();
            }
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
        EditText communityanswer_add_layout_contentetitledittext = mCommunityAnswerAddView.findViewById(R.id.communityanswer_add_layout_contentetitledittext);
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
        //发表按钮
        TextView communityanswer_choosesign_layout_commit_button1 = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_layout_commit_button1);
        communityanswer_choosesign_layout_commit_button1.setOnClickListener(v->{
            //点击发表问答，先判断是否有选择标签，如果没有选择标签，不做处理
            if (mCommunityAnswerChooseSignList.size() != 0){
                //发表，清空草稿箱中的文字，并返回到问答首页
                ModelSearchRecordSQLiteOpenHelper.getWritableDatabase(mControlMainActivity).
                        execSQL("delete from communityanswerdraftbox");
            }
        });
        //选中的标签个数
        TextView communityanswer_choosesign_choosecount = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_choosecount);
        //添加标签
        ControllerWarpLinearLayout communityanswer_choosesign_warpLinearLayout = mCommunityAnswerChooseSignView.findViewById(R.id.communityanswer_choosesign_warpLinearLayout);
        communityanswer_choosesign_warpLinearLayout.removeAllViews();
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
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
        //测试数据
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("技术基础实务");
            communityanswer_choosesign_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("1");
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaaaa");
            communityanswer_choosesign_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("2");
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("消防考试专业课");
            communityanswer_choosesign_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("3");
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
                System.out.println("我收到了" + string);
            });
            // 5. 设置点击返回按键后的操作（通过回调接口）
            communityanswer_search_view.setOnClickBack(()->{
                mControlMainActivity.Page_CommunityAnswer();
            });
        }
        communityanswer_layout_main.addView(mCommunityAnswerSelectView);
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
        //添加搜索标签
        ControllerWarpLinearLayout communityanswer_select_warpLinearLayout = popupWindowView.findViewById(R.id.communityanswer_select_warpLinearLayout);
        communityanswer_select_warpLinearLayout.removeAllViews();
        mCommunityAnswerSelectTemp = mCommunityAnswerSelect;
        //必须有的标签-全部:默认选中全部
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("技术基础实务");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("1");
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
            if (mCommunityAnswerSelect.equals(communityanswer_selectpop_child_signname.getHint().toString())) {
                communityanswer_selectpop_child_signname.setBackground(view.getResources().getDrawable(R.drawable.textview_style_rect_blue));
                communityanswer_selectpop_child_signname.setTextColor(view.getResources().getColor(R.color.white));
            }
        }
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaaaa");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("2");
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
                        communityanswer_selectpop_child_signname1.setPadding(padding,padding,padding,padding);
                        communityanswer_selectpop_child_signname1.setTextColor(view.getResources().getColor(R.color.white));
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaa");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("3");
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaa");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("4");
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaa");
            communityanswer_select_warpLinearLayout.addView(view);
            communityanswer_selectpop_child_signname.setHint("5");
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
        {
            View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.model_communityanswer_selectpop_child, null);
            TextView communityanswer_selectpop_child_signname = view.findViewById(R.id.communityanswer_selectpop_child_signname);
            communityanswer_selectpop_child_signname.setText("aaaaaaaaaaaaaaaaaaaaaaa");
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
        //点击确定
        TextView communityanswer_select_buttonsure = popupWindowView.findViewById(R.id.communityanswer_select_buttonsure);
        communityanswer_select_buttonsure.setOnClickListener(v->{
            mCommunityAnswerSelect = mCommunityAnswerSelectTemp;
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
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = mControlMainActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mControlMainActivity.getWindow().setAttributes(lp);
    }

    //获取屏幕高度 不包含虚拟按键=
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
}
