package com.android.hzjy.hzjyproduct;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ModelClassAdapter extends ModelGroupRecyclerAdapter<String, ClassBean> {


    private RequestManager mLoader;
    private Context mContext ;

    public ModelClassAdapter(Context context) {
        super(context);
        mContext = context;
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<ClassBean>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("2019年11月11日;今日课次：1", create(""));
        map.put("2019年11月12日;今日课次：1111", create(""));
        map.put("2019年11月13日;今日课次：1", create(""));
        map.put("2019年11月14日;今日课次：1", create(""));
        titles.add("2019年11月11日;今日课次：1");
        titles.add("2019年11月12日;今日课次：1111");
        titles.add("2019年11月13日;今日课次：1");
        titles.add("2019年11月14日;今日课次：1");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ClassViewHolder(mInflater.inflate(R.layout.fragment_classchedulecard2, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, ClassBean item, int position) {
        ClassViewHolder h = (ClassViewHolder) holder;
        h.mclasschedulecard2_coursename.setText(item.getClassName());
        h.mclasschedulecard2_time.setText(item.getClassTime());
//        Glide.with(mContext.getApplicationContext()).
//                load(mContext.getApplicationContext().getResources().getDrawable(R.drawable.background_course_live)).into(h.mclasschedulecard2_background);

//        Glide.with(mContext.getApplicationContext()).
//                load("").listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Log.d("Warn","加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
//                return false;
//            }
//            @Override
//            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                Log.d("Warn","成功  Drawable Name:" + resource.getClass().getCanonicalName());
//                return false;
//            }
//        })
//                .error(mContext.getApplicationContext().getResources().getDrawable(R.drawable.background_course_live)).into(h.mclasschedulecard2_background);

//        mLoader.load(mContext.getApplicationContext().getResources().getDrawable(R.drawable.background_course_live))
//                .into(h.mclasschedulecard2_background);
    }

    private static class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView mclasschedulecard2_coursename,
                mclasschedulecard2_time,mclasschedulecard2_mainteacher;
        private ControllerCustomRoundAngleImageView mclasschedulecard2_teachers_headportrait,mclasschedulecard2_background;

        private ClassViewHolder(View itemView) {
            super(itemView);
            mclasschedulecard2_coursename = itemView.findViewById(R.id.classchedulecard2_coursename);
            mclasschedulecard2_time = itemView.findViewById(R.id.classchedulecard2_time);
            mclasschedulecard2_teachers_headportrait = itemView.findViewById(R.id.classchedulecard2_teachers_headportrait);
            mclasschedulecard2_mainteacher = itemView.findViewById(R.id.classchedulecard2_mainteacher);
//            mclasschedulecard2_background = itemView.findViewById(R.id.classchedulecard2_background);
        }
    }


    private static ClassBean create(String className, String classTime, String teacherName, String teacherImg) {
        ClassBean classBean = new ClassBean();
        classBean.setClassName(className);
        classBean.setClassTime(classTime);
        classBean.setClassTeacher(teacherName);
        classBean.setClassTeacherImg(teacherImg);
        return classBean;
    }

    private static List<ClassBean> create(String data) {
        List<ClassBean> list = new ArrayList<>();
        list.add(create("技术失误呀", "7:00-8:00",
                "http://cms-bucket.nosdn.127.net/catchpic/2/27/27e2ce7fd02e6c096e21b1689a8a3fe9.jpg?imageView&thumbnail=550x0","大玉儿"));
        list.add(create("技术失误呀1", "7:00-8:00",
                "http://cms-bucket.nosdn.127.net/catchpic/2/27/27e2ce7fd02e6c096e21b1689a8a3fe9.jpg?imageView&thumbnail=550x0","大玉儿1"));
        list.add(create("技术失误呀2", "7:00-8:00",
                "http://cms-bucket.nosdn.127.net/catchpic/2/27/27e2ce7fd02e6c096e21b1689a8a3fe9.jpg?imageView&thumbnail=550x0","大玉儿2"));
        list.add(create("技术失误呀3", "7:00-8:00",
                "http://cms-bucket.nosdn.127.net/catchpic/2/27/27e2ce7fd02e6c096e21b1689a8a3fe9.jpg?imageView&thumbnail=550x0","大玉儿3"));
        return list;
    }
}
