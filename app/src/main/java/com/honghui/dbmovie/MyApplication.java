package com.honghui.dbmovie;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * Created by lyw on 2018/2/17.
 */

public class MyApplication extends Application {
    private static MyApplication instance = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        initImageLoader(getApplicationContext());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void initImageLoader(Context context) {
        //缓存目录
        File cacheDir = new File(AppConst.cacheDir);
        //配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置线程的优先级
                .denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .diskCache(new UnlimitedDiscCache(cacheDir))//设置本地缓存目录
                .diskCacheSize(50 * 1024 * 1024)//本地缓存大小
                .tasksProcessingOrder(QueueProcessingType.FIFO)//设置图片下载和显示的工作队列排序
                .build();

        //通过配置初始化ImageLoader
        ImageLoader.getInstance().init(config);
    }
}

