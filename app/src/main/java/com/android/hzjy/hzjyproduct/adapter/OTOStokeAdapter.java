package com.android.hzjy.hzjyproduct.adapter;

import android.support.annotation.NonNull;

import com.android.hzjy.hzjyproduct.R;
import com.android.hzjy.hzjyproduct.base.BaseDatabindingAdapter;
import com.android.hzjy.hzjyproduct.databinding.ItemPopStokeBinding;
import java.util.List;

/**
 * Created by ccy on 2019/5/8/20:26
 */
public class OTOStokeAdapter extends BaseDatabindingAdapter<Float> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_pop_stoke;
    }
    @Override
    public boolean isOpenSelectFunction() {
        return true;
    }
    @Override
    protected int getVariableId() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Float> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ItemPopStokeBinding itemPopStokeBinding = (ItemPopStokeBinding) holder.getBinding();
        Float dotPerent = getItem(position);
        if(dotPerent != null){
            itemPopStokeBinding.stokeDv.setDotPerent(dotPerent);
        }
        itemPopStokeBinding.stokeDv.setSelected(position== selectPosition);
    }
}
