package com.android.hzjy.hzjyproduct;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ControllerListViewForScrollView extends ListView {

    public ControllerListViewForScrollView(Context context) {
        super(context);
    }

    public ControllerListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControllerListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 只需要重写这个方法即可
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
