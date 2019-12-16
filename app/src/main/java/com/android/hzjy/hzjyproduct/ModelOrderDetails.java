package com.android.hzjy.hzjyproduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.hzjy.hzjyproduct.wxapi.WeiXinConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.talkfun.sdk.HtSdk;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.fragment.NewImagePagerDialogFragment;

public class ModelOrderDetails implements View.OnClickListener {
    private View mOrderBuyView,mOrderBankCardView,mOrderResultView,mOrderCouponChooseView;
    private ControlMainActivity mControlMainActivity = null;
    //当前选中的支付方式
    private String mCurrentPayType = "bankcard";
    //当前选中的选择优惠券标签(默认为可用优惠券)
    private String mCurrentmOrderCouponChooseTab = "use";
    private int mCurrentmOrderCouponChooseIndex = 1;
    private ControllerCustomDialog mCustomDialog = null;
    private ControllerCenterDialog mMyCouponDialog = null;
    private String mPage = "";
    private View modelOrderDetailsView = null;
    private int height = 1344;
    private int width = 720;
    private ModelOrderDetailsInterface mModelOrderDetailsInterface = null;
    //选择的优惠券Id
    private String mCouponId = "";
    //支付宝支付回调
    private static final int ALISDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALISDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    ModelAliPayResult payResult = new ModelAliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    public View ModelOrderDetails(ModelOrderDetailsInterface modelOrderDetailsInterface,Context context){
        mControlMainActivity = (ControlMainActivity) context;
        mModelOrderDetailsInterface = modelOrderDetailsInterface;
        DisplayMetrics dm = context.getResources().getDisplayMetrics(); //获取屏幕分辨率
        height = dm.heightPixels;
        width = dm.widthPixels;
        modelOrderDetailsView = LayoutInflater.from(context).inflate(R.layout.modelorderdetails, null);
        CourseDetailsBuyInit();
        return modelOrderDetailsView;
    }

    public void HideAllLayout(){
        LinearLayout modeldetails_main = modelOrderDetailsView.findViewById(R.id.modeldetails_main);
        modeldetails_main.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderpay_paytype_bankcard:{ //支付方式选择银行卡
                ImageView orderpay_paytype_bankcardicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_bankcardicon);
                ImageView orderpay_paytype_alipayicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_alipayicon);
                ImageView orderpay_paytype_wechaticon = mOrderBuyView.findViewById(R.id.orderpay_paytype_wechaticon);
                orderpay_paytype_bankcardicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_bluecircle));
                orderpay_paytype_alipayicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                orderpay_paytype_wechaticon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                mCurrentPayType = "bankcard";
                break;
            }
            case R.id.orderpay_paytype_alipay:{ //支付方式选择支付宝
                ImageView orderpay_paytype_bankcardicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_bankcardicon);
                ImageView orderpay_paytype_alipayicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_alipayicon);
                ImageView orderpay_paytype_wechaticon = mOrderBuyView.findViewById(R.id.orderpay_paytype_wechaticon);
                orderpay_paytype_bankcardicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                orderpay_paytype_alipayicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_bluecircle));
                orderpay_paytype_wechaticon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                mCurrentPayType = "alipay";
                break;
            }
            case R.id.orderpay_paytype_wechat:{ //支付方式选择微信
                ImageView orderpay_paytype_bankcardicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_bankcardicon);
                ImageView orderpay_paytype_alipayicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_alipayicon);
                ImageView orderpay_paytype_wechaticon = mOrderBuyView.findViewById(R.id.orderpay_paytype_wechaticon);
                orderpay_paytype_bankcardicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                orderpay_paytype_alipayicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
                orderpay_paytype_wechaticon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_bluecircle));
                mCurrentPayType = "wechat";
                break;
            }
            case R.id.orderpay_immediatepayment: {//点击订单界面的立即支付按钮
                if (mCurrentPayType.equals("bankcard")){ //银行卡支付
                    mControlMainActivity.Page_OrderDetailsBankCard();
                    CourseDetailsBankCardInit();
                } else if (mCurrentPayType.equals("alipay")){ //支付宝支付
                    final Runnable payRunnable = () -> {
                        PayTask alipay = new PayTask(mControlMainActivity);
                        //支付信息由服务器生成orderInfo  以下只是测试数据
                        String orderInfo = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%221212093452-1516%22%7D&method=alipay.trade.app.pay&app_id=2019052765354481&sign_type=RSA&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=XYDlmjqUb4Q%2B48EuChBJEJOc9XldUWcwLjlcGKly5dG22joBzyfqXVCWIdX9HobK8wZ6tJ%2B4BstSlHaeeMXNiemfVam0a2%2B2jLOJazYDt1oo%2FMhumsClPrbX%2FtEOf5wMNnhgDVv8I%2BV1Hw2zlOmRHvwD1fk4DXv1vqI37JqrpAxzJ2NXBQ%2FBzo%2FOzczn5vaBfETQ1c8UeAlwmvEmpu%2ByaHdx6wzXZpbptwJei3gol2pSHnKX%2FHIYKfQZFvQ%2Bnixn4NwxU0Ty465vWbmu5uPxWoJzGRNuK1sHK5Zou6eMFZc5QmwdA%2BgDYCvVu3e3CE8tzN91IUCTRop6pf2h8vRhGg%3D%3D";
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());

                        Message msg = new Message();
                        msg.what = ALISDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else if (mCurrentPayType.equals("wechat")){ //微信支付
                    WxChatAPP();
                }
                break;
            }
            case R.id.orderpay_preferentialnumber_layout:{ //点击选择优惠券
                mControlMainActivity.Page_OrderDetailsChooseCoupon();
                CourseDetailsOrderCouponChooseInit();
                break;
            }
            case R.id.orderpay_couponchoose_tab_use:{ //订单-选择优惠券-可用优惠券
                if (!mCurrentmOrderCouponChooseTab.equals("use")) {
                    ImageView orderpay_couponchoose_cursor1 = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_cursor1);
                    Animation animation = new TranslateAnimation(( mCurrentmOrderCouponChooseIndex - 1)  * width / 2,0 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    orderpay_couponchoose_cursor1.startAnimation(animation);
                    TextView orderpay_couponchoose_tab_use = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_use);
                    TextView orderpay_couponchoose_tab_unused = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_unused);
                    orderpay_couponchoose_tab_use.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                    orderpay_couponchoose_tab_unused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                }
                mCurrentmOrderCouponChooseIndex = 1;
                mCurrentmOrderCouponChooseTab = "use";
                LinearLayout orderpay_couponchoose_main_content = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_content);
                orderpay_couponchoose_main_content.removeAllViews();
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_couponchoose1, null);
                ImageView orderpay_couponchoose1_immediateuse = view.findViewById(R.id.orderpay_couponchoose1_immediateuse);
                orderpay_couponchoose1_immediateuse.setOnClickListener(V->{
                    mCouponId = "1";
                    CourseDetailsBuyInit();
                    mControlMainActivity.Page_OrderDetailsChooseCouponReturn();
                });
                orderpay_couponchoose_main_content.addView(view);
                break;
            }
            case R.id.orderpay_couponchoose_tab_unused:{ //订单-选择优惠券-不可用优惠券
                if (!mCurrentmOrderCouponChooseTab.equals("unuse")) {
                    ImageView orderpay_couponchoose_cursor1 = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_cursor1);
                    Animation animation = new TranslateAnimation(( mCurrentmOrderCouponChooseIndex - 1)  * width / 2,width / 2 , 0, 0);
                    animation.setFillAfter(true);// True:图片停在动画结束位置
                    animation.setDuration(200);
                    orderpay_couponchoose_cursor1.startAnimation(animation);
                    TextView orderpay_couponchoose_tab_use = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_use);
                    TextView orderpay_couponchoose_tab_unused = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_unused);
                    orderpay_couponchoose_tab_use.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize16));
                    orderpay_couponchoose_tab_unused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize18));
                }
                mCurrentmOrderCouponChooseIndex = 2;
                mCurrentmOrderCouponChooseTab = "unuse";
                LinearLayout orderpay_couponchoose_main_content = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_content);
                orderpay_couponchoose_main_content.removeAllViews();
                View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_couponchoose1, null);
                //将文字颜色置为灰色
                TextView orderpay_couponchoose1_couponpriceicon = view.findViewById(R.id.orderpay_couponchoose1_couponpriceicon);
                TextView orderpay_couponchoose1_couponprice = view.findViewById(R.id.orderpay_couponchoose1_couponprice);
                TextView orderpay_couponchoose1_couponrequire = view.findViewById(R.id.orderpay_couponchoose1_couponrequire);
                orderpay_couponchoose1_couponpriceicon.setTextColor(view.getResources().getColor(R.color.black999999));
                orderpay_couponchoose1_couponprice.setTextColor(view.getResources().getColor(R.color.black999999));
                orderpay_couponchoose1_couponrequire.setTextColor(view.getResources().getColor(R.color.black999999));
                ImageView orderpay_couponchoose1_immediateuse = view.findViewById(R.id.orderpay_couponchoose1_immediateuse);
                orderpay_couponchoose1_immediateuse.setBackground(view.getResources().getDrawable(R.drawable.mycoupon_button_immediateuse_gray));
                orderpay_couponchoose_main_content.addView(view);
                break;
            }
            case R.id.orderpay_couponchoose_main_return_button1:{
                mCouponId = "";
                CourseDetailsBuyInit();
                mControlMainActivity.Page_OrderDetailsChooseCouponReturn();
                break;
            }
            case R.id.orderpay_couponchoose_main_exchange:{
                //点击兑换弹出兑换对话框
                View view = mControlMainActivity.getLayoutInflater().inflate(R.layout.dialog_sure_cancel1, null);
                mMyCouponDialog = new ControllerCenterDialog(mControlMainActivity, 0, 0, view, R.style.DialogTheme);
                mMyCouponDialog.setCancelable(true);
                mMyCouponDialog.show();
                TextView button_cancel = view.findViewById(R.id.button_cancel);
                button_cancel.setOnClickListener(View->{
                    mMyCouponDialog.cancel();
                });
                TextView button_sure = view.findViewById(R.id.button_sure);
                button_sure.setOnClickListener(View->{
                    //开始兑换优惠码
                    mMyCouponDialog.cancel();
                });
                break;
            }
            case R.id.orderpay_bankcard_return_button1:{ //银行卡-返回
                CourseDetailsBuyInit();
                mControlMainActivity.Page_OrderDetailsBankCardReturn();
                break;
            }
            default:
                break;
        }
    }
    private void CourseDetailsBuyInit(){
        mPage = "OrderDetailsBuy";
        HideAllLayout();
        LinearLayout modeldetails_main = modelOrderDetailsView.findViewById(R.id.modeldetails_main);
//        if (mOrderBuyView == null) {
            mOrderBuyView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_main, null);
            LinearLayout orderpay_paytype_bankcard = mOrderBuyView.findViewById(R.id.orderpay_paytype_bankcard);
            LinearLayout orderpay_paytype_alipay = mOrderBuyView.findViewById(R.id.orderpay_paytype_alipay);
            LinearLayout orderpay_paytype_wechat = mOrderBuyView.findViewById(R.id.orderpay_paytype_wechat);
            TextView orderpay_immediatepayment = mOrderBuyView.findViewById(R.id.orderpay_immediatepayment);
            LinearLayout orderpay_preferentialnumber_layout = mOrderBuyView.findViewById(R.id.orderpay_preferentialnumber_layout);
            orderpay_preferentialnumber_layout.setOnClickListener(this);
            orderpay_immediatepayment.setOnClickListener(this);
            orderpay_paytype_bankcard.setOnClickListener(this);
            orderpay_paytype_alipay.setOnClickListener(this);
            orderpay_paytype_wechat.setOnClickListener(this);
//            ImageView course_question_add_layout_return_button1 = mCourseBuyView.findViewById(R.id.course_question_add_layout_return_button1);
//            course_question_add_layout_return_button1.setOnClickListener(this);
//        }
        //默认为银行卡支付
        ImageView orderpay_paytype_bankcardicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_bankcardicon);
        ImageView orderpay_paytype_alipayicon = mOrderBuyView.findViewById(R.id.orderpay_paytype_alipayicon);
        ImageView orderpay_paytype_wechaticon = mOrderBuyView.findViewById(R.id.orderpay_paytype_wechaticon);
        orderpay_paytype_bankcardicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_bluecircle));
        orderpay_paytype_alipayicon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
        orderpay_paytype_wechaticon.setBackground(mOrderBuyView.getResources().getDrawable(R.drawable.radiobutton_graycircle));
        mCurrentPayType = "bankcard";
        TextView orderpay_preferentialnumber = mOrderBuyView.findViewById(R.id.orderpay_preferentialnumber);
        if (!mCouponId.equals("")){
            orderpay_preferentialnumber.setText("已选择1张");
        } else {
            orderpay_preferentialnumber.setText("2张可用");
        }
        modeldetails_main.addView(mOrderBuyView);
    }
    //订单支付-银行卡支付界面
    private void CourseDetailsBankCardInit(){
        mPage = "CourseDetailsBankCard";
        HideAllLayout();
        LinearLayout modeldetails_main = modelOrderDetailsView.findViewById(R.id.modeldetails_main);
//        if (mOrderBankCardView == null) {
            mOrderBankCardView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_bankcard, null);
            ImageView orderpay_bankcard_return_button1 = mOrderBankCardView.findViewById(R.id.orderpay_bankcard_return_button1);
        orderpay_bankcard_return_button1.setOnClickListener(this);
//        }
        modeldetails_main.addView(mOrderBankCardView);
    }
    //支付结果界面
    private void CourseDetailsOrderResultInit(){
        mPage = "CourseDetailsOrderResult";
        HideAllLayout();
        LinearLayout modeldetails_main = modelOrderDetailsView.findViewById(R.id.modeldetails_main);
//        if (mOrderResultView == null) {
            mOrderResultView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_payresult, null);
//        }
        modeldetails_main.addView(mOrderResultView);
    }
    //支付-选择优惠券界面
    private void CourseDetailsOrderCouponChooseInit(){
        mPage = "OrderCouponChoose";
        HideAllLayout();
        LinearLayout modeldetails_main = modelOrderDetailsView.findViewById(R.id.modeldetails_main);
//        if (mOrderCouponChooseView == null) {
            mOrderCouponChooseView = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_couponchoose, null);
            TextView orderpay_couponchoose_tab_use = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_use);
            TextView orderpay_couponchoose_tab_unused = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_unused);
            ImageView orderpay_couponchoose_main_return_button1 = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_return_button1);
            TextView orderpay_couponchoose_main_exchange = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_exchange);
            orderpay_couponchoose_main_exchange.setOnClickListener(this);
            orderpay_couponchoose_main_return_button1.setOnClickListener(this);
            orderpay_couponchoose_tab_unused.setOnClickListener(this);
            orderpay_couponchoose_tab_use.setOnClickListener(this);
            //下拉刷新
            ModelPtrFrameLayout orderpay_couponchoose_main_content_frame = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_content_frame);
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mControlMainActivity);
            orderpay_couponchoose_main_content_frame.addPtrUIHandler(header);
            orderpay_couponchoose_main_content_frame.setHeaderView(header);
            orderpay_couponchoose_main_content_frame.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    // 默认实现，根据实际情况做改动
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //在这里写自己下拉刷新数据的请求
                    //需要结束刷新头
                    orderpay_couponchoose_main_content_frame.refreshComplete();
                }
            });
//        }
        ImageView orderpay_couponchoose_cursor1 = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_cursor1);
        int x = width / 4 - mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.dp18) / 2;
        orderpay_couponchoose_cursor1.setX(x);
        //默认选中的为课程
        mCurrentmOrderCouponChooseIndex = 1;
        mCurrentmOrderCouponChooseTab = "use";
//        TextView orderpay_couponchoose_tab_use = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_use);
//        TextView orderpay_couponchoose_tab_unused = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_tab_unused);
        orderpay_couponchoose_tab_use.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize18));
        orderpay_couponchoose_tab_unused.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mOrderCouponChooseView.getResources().getDimensionPixelSize(R.dimen.textsize16));
        LinearLayout orderpay_couponchoose_main_content = mOrderCouponChooseView.findViewById(R.id.orderpay_couponchoose_main_content);
        orderpay_couponchoose_main_content.removeAllViews();
        View view = LayoutInflater.from(mControlMainActivity).inflate(R.layout.modelorderpay_couponchoose1, null);
        ImageView orderpay_couponchoose1_immediateuse = view.findViewById(R.id.orderpay_couponchoose1_immediateuse);
        orderpay_couponchoose1_immediateuse.setOnClickListener(V->{
            mCouponId = "1";
            CourseDetailsBuyInit();
            mControlMainActivity.Page_OrderDetailsChooseCouponReturn();
        });
        orderpay_couponchoose_main_content.addView(view);
        modeldetails_main.addView(mOrderCouponChooseView);
    }

    public void onClickOrderDetailsReturn(){
        mModelOrderDetailsInterface.onRecive();
    }

    public void onClickOrderDetailsChooseCouponReturn(){
        mCouponId = "";
        CourseDetailsBuyInit();
    }

    public void onClickOrderDetailsBankCardReturn(){
        CourseDetailsBuyInit();
    }

    public void WxChatAPP() {
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(mControlMainActivity, WeiXinConstants.APP_ID,false);
        PayReq req = new PayReq();
//        req.appId = "wx2b458d6f92916ec9";
//        req.partnerId = "1518501111";
//        req.prepayId = "wx121504096461655ec009d35f1278551000";
//        req.packageValue = "Sign=WXPay";
//        req.nonceStr = "fMIfZD1yZoEbUQcg";
//        req.timeStamp = "1576134182";
//        req.sign = sign;
        req.appId = "wxb4ba3c02aa476ea1";
        req.partnerId = "1900006771";
        req.prepayId = "wx12160854164270d0ceff740f1156984432";
        req.packageValue = "Sign=WXPay";
        req.nonceStr = "ab9c53887fa7bcb8afe21d6b7a79c0e6";
        req.timeStamp = "1576138134";
        req.extData = "app data"; // optional
        req.sign = "F538B276557BFD7C02AF2116691FFB9B";
        boolean result = wxapi.sendReq(req);
    }
}
