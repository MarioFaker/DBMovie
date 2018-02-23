package com.honghui.dbmovie;

import android.os.Environment;

/**
 * Created by lyw on 2018/2/17.
 */

public class AppConst {
    public static String AppTAG = "DefaulterFrame";

    public static final String SDCARD = Environment.getExternalStorageDirectory().toString();// sd卡位置
    public static final String appDirName = "DBMovie";// app目录名
    public static final String cacheDir = SDCARD + "/" + appDirName + "/" + "universal_cache_dir";// 缓存目录
}
