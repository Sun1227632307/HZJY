package com.android.hzjy.hzjyproduct;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ControllerPopDialog extends Dialog {
    private ControlMainActivity mControlMainActivity;
    private int mLayout = 0;
//    public ControllerPopDialog(@NonNull Context context) {
//        this(context, 0);
//    }

    public ControllerPopDialog(@NonNull Context context, int themeResId,int layout) {
        super(context, themeResId);
        mControlMainActivity = (ControlMainActivity) context;
        mLayout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayout);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
