package com.android.hzjy.hzjyproduct;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dayuer on 2019/11/21.
 */

public class ControllerOkManager {
    private File rootFile;
    private File file;
    private long downLoadSize;
    private final ThreadPoolExecutor executor;
    private boolean isDown = false;
    private String name;
    private String path;
    private RandomAccessFile raf;
    private long totalSize = 0;
    private MyThread thread;
    private Handler handler;
    private ControllerDownLoad.IProgress progress;
    public ControllerOkManager(String path, ControllerDownLoad.IProgress progress) {
        this.path = path;
        this.progress = progress;
        this.handler =  new Handler();
        this.name = path.substring(path.lastIndexOf("/")+1);
        rootFile = getRootFile();
        executor = new ThreadPoolExecutor(5,5,50, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(3000));
        //executor.execute(new MyThread());
    }
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            downLoadFile();
        }

    }
    private void downLoadFile() {
        try {
            if (file == null){
                file = createFile(rootFile.getPath(),name);
                raf = new RandomAccessFile(file,"rwd");
                downLoadSize = file.length();
                raf.seek(downLoadSize);
            }else{
                downLoadSize = file.length();
                if (raf == null){
                    raf = new RandomAccessFile(file,"rwd");
                }
                raf.seek(downLoadSize);
            }
            totalSize = getContentLength(path);
            if (downLoadSize==totalSize){
                //已经下载完成
                return ;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(path).
                    addHeader("Range","bytes="+downLoadSize+"-"+totalSize).build();
            Response response = client.newCall(request).execute();
            InputStream ins = response.body().byteStream();
            int len = 0;
            byte[]by = new byte[1024];
            long endTime = System.currentTimeMillis();
            while ((len =ins.read(by))!=-1 && isDown){
                raf.write(by,0,len);
                downLoadSize += len;
                if (System.currentTimeMillis()-endTime>1000){
                    final double dd = downLoadSize/(totalSize*1.0);
                    DecimalFormat format = new DecimalFormat("#0.00");
                    String value = format.format((dd*100))+"%";
                    Log.i("tag","=================="+value);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.onProgress((int) (dd*100));
                        }
                    });
                }
            }
            response.close();
        }catch (Exception e){
            e.getMessage();
            Log.i("tag","=================="+e.getMessage());
            if (thread!=null){
                isDown = false;
                executor.remove(thread);
                thread = null;
            }
        }

    }

    /**
     * 检测sdcard是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isCheckSDCardWarning() {
        return !isSDCardAvailable();
    }

    public static boolean createDir(String path) {
        if (isCheckSDCardWarning()) {
            return false;
        }

        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return true;
    }

    public static File createFile(String path, String filename) {
        if (!createDir(path)) {
            return null;
        }

        if (TextUtils.isEmpty(filename)) {
            return null;
        }

        File file = null;
        file = new File(path, filename);
        if (file.exists()) {
            return file;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }

        return file;
    }
    //创建一个文件夹，用来存放下载的文件
    public static File getRootFile(){
        File sd = Environment.getExternalStorageDirectory();
        File rootFile = new File(sd,"TEMPFILE");
        if (!rootFile.exists()){
            rootFile.mkdirs();
        }
        return rootFile;
    }
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public void start(){
        if (thread == null){
            thread = new MyThread();
            isDown = true;
            executor.execute(thread);
        }
    }
    public void stop(){
        if (thread!=null){
            isDown = false;
            executor.remove(thread);
            thread = null;
        }
    }
    //通过OkhttpClient获取文件的大小
    public long getContentLength(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        long length = response.body().contentLength();
        response.close();
        return length;
    }

    public interface IProgress{
        void onProgress(int progress);
    }

}
