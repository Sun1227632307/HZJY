
package com.android.hzjy.hzjyproduct.event;


import com.android.hzjy.hzjyproduct.entity.ExpressionEntity;

/**
 * Created by asus on 2015/11/24.
 */
public interface OnExpressionListener {
    void OnExpressionSelected(ExpressionEntity entity);
    void OnExpressionRemove();
}
