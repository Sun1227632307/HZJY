package com.android.hzjy.hzjyproduct;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ControllerCustomDialog extends Dialog {
    private EditText et_input;
    private ImageView course_question_respond_layout_commit_button ;
    private ControlMainActivity mControlMainActivity;
    private String mContent = ""; //框中显示内容
    private boolean mIsUseImage = false;
    private OnClickPublishOrImage mOnClickPublishOrImage = null;
    public ControllerCustomDialog(@NonNull Context context) {
        this(context, 0,"",false);
    }

    public ControllerCustomDialog(@NonNull Context context, int themeResId,String content,boolean isUseImage) {
        super(context, themeResId);
        mControlMainActivity = (ControlMainActivity) context;
        if (content != null){
            mContent = content;
        }
        mIsUseImage = isUseImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelcoursedetails_question_respond);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        et_input = findViewById(R.id.course_question_respond_layout_edittext);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView course_question_respond_layout_commit_button = findViewById(R.id.course_question_respond_layout_commit_button);
                if (!s.toString().equals("")){
                    course_question_respond_layout_commit_button.setBackgroundResource(R.drawable.button_publish_blue);
                } else {
                    course_question_respond_layout_commit_button.setBackgroundResource(R.drawable.button_publish_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_input.setHint(mContent);
        openKeyBoard(et_input);
        //点击发布按钮
        course_question_respond_layout_commit_button = findViewById(R.id.course_question_respond_layout_commit_button);
        course_question_respond_layout_commit_button.setOnClickListener(v->{
            if (mOnClickPublishOrImage != null && !et_input.getText().toString().equals("")){{ //输入框必须有内容才能发布
                mOnClickPublishOrImage.publish();
            }}
        });
        //是否启用图片功能
        if (mIsUseImage){
            ImageView course_question_respond_layout_image_button = findViewById(R.id.course_question_respond_layout_image_button);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_question_respond_layout_image_button.getLayoutParams();
            ll.width = (int) this.getContext().getResources().getDimension(R.dimen.dp25);
            ll.rightMargin = (int) this.getContext().getResources().getDimension(R.dimen.dp13);
            course_question_respond_layout_image_button.setLayoutParams(ll);
            course_question_respond_layout_image_button.setOnClickListener(v->{
                if (mOnClickPublishOrImage != null){
                    mOnClickPublishOrImage.image();
                }
            });
        }
    }

    @Override
    public void dismiss() {
        hideInput();
        super.dismiss();
    }

    /**
     * 弹起软键盘
     * @param editText
     */
    public void openKeyBoard(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                                mControlMainActivity.getApplication()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,0);
                editText.setSelection(editText.getText().length());
            }
        },200);
    }
    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) mControlMainActivity.getSystemService(INPUT_METHOD_SERVICE);
        View v = mControlMainActivity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    public void setOnClickPublishOrImagelistener(OnClickPublishOrImage onClickPublishOrImage){
        mOnClickPublishOrImage = onClickPublishOrImage;
    }

    interface OnClickPublishOrImage{
        void publish();
        void image();
    }

    public void setImage(Drawable drawable, float width, float height){
        ImageView course_question_respond_layout_image_button = findViewById(R.id.course_question_respond_layout_image_button);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) course_question_respond_layout_image_button.getLayoutParams();
        if (width >= 0) {
            ll.width = (int) width;
        }
        if (height >= 0) {
            ll.height = (int) height;
        }
        course_question_respond_layout_image_button.setLayoutParams(ll);
        course_question_respond_layout_image_button.setBackground(drawable);
    }
}
