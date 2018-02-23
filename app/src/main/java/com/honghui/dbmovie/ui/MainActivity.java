package com.honghui.dbmovie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honghui.dbmovie.R;

public class MainActivity extends FragmentActivity {

    //导航栏相关控件
    private LinearLayout view_index_first, view_index_third,view_index_second;
    private TextView tv_index_first, tv_index_third, tv_index_second;
    private ImageView iv_index_first, iv_index_third, iv_index_second;

    private FragmentManager fragmentManager;//用于管理Activity中的FragmentManager
    private Fragment curFragment;// 记录当前显示的Fragment
    private IndexFirst firstFragment = null;//热映Fragment
    private IndexSecond secondFragment = null;//排行Fragment
    private IndexThird thirdFragment = null;//分类Fragment

    public static String curTag = "";// 当前导航标签

    private long exitTime = 0;// 计算短时间内点击返回按钮的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        curTag = "";
        toFirstFragment();
    }

    private void initView() {
        view_index_first = (LinearLayout) findViewById(R.id.view_index_first);
        view_index_second = (LinearLayout) findViewById(R.id.view_index_second);
        view_index_third = (LinearLayout) findViewById(R.id.view_index_third);

        tv_index_first = (TextView) findViewById(R.id.tv_index_first);
        tv_index_third = (TextView) findViewById(R.id.tv_index_third);
        tv_index_second = (TextView) findViewById(R.id.tv_index_second);

        iv_index_first = (ImageView) findViewById(R.id.iv_index_first);
        iv_index_third = (ImageView) findViewById(R.id.iv_index_third);
        iv_index_second = (ImageView) findViewById(R.id.iv_index_second);

        view_index_first.setOnClickListener(viewClickListener);
        view_index_third.setOnClickListener(viewClickListener);
        view_index_second.setOnClickListener(viewClickListener);

        //获取FragmentManager
        fragmentManager = getSupportFragmentManager();
    }

    //导航栏的点击事件
    private View.OnClickListener viewClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_index_first:
                    toFirstFragment();//切换到“热映”Fragment
                    break;
                case R.id.view_index_second:
                    toSecondFragment();//切换到“排行”Fragment
                    break;
                case R.id.view_index_third:
                    toThirdFragment();//切换到“分类”Fragment
                    break;
                default:
                    break;
            }
        }
    };

    // 显示tab标签对应的数据
    private void showTabData(String tag) {
        curTag = tag;
        //在fragmentManager中，通过指定的tag，找到对应的Fragment
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        //开始一个事务
        FragmentTransaction transation = fragmentManager.beginTransaction();

        //如果没有找到 对应的Fragment，就创建一个
        if (fragment == null) {
            if (IndexFirst.TAG.equals(tag)) {
                firstFragment = new IndexFirst();
                fragment = firstFragment;
            } else if (IndexSecond.TAG.equals(tag)) {
                secondFragment = new IndexSecond();
                fragment = secondFragment;
            } else {
                thirdFragment = new IndexThird();
                fragment = thirdFragment;
            }
            //向activity中指定的位置（第一个参数），添加一个Fragment（第二个参数），这个Fragment对应的tag指定为（第三个参数）
            transation.add(R.id.container, fragment, tag);
        }

        if (curFragment != null) {
            //隐藏当前的Fragment
            transation.hide(curFragment);
        }
        //显示指定的Fragment
        transation.show(fragment);
        //提交一个事务
        transation.commitAllowingStateLoss();
        //记录当前显示的Fragment
        curFragment = fragment;
    }

    /**
     * 切换到 热映 界面
     * */
    private void toFirstFragment() {
        int breakColor = getResources().getColor(R.color.bar_txt_default);
        int blueColor = getResources().getColor(R.color.bar_txt_selected);
        if (!curTag.equals(IndexFirst.TAG)) {
            //显示指定的Fragment
            showTabData(IndexFirst.TAG);

            //更新导航栏控件
            tv_index_first.setTextColor(blueColor);
            tv_index_second.setTextColor(breakColor);
            tv_index_third.setTextColor(breakColor);

            iv_index_first.setImageResource(R.drawable.m1_selected);
            iv_index_second.setImageResource(R.drawable.m2_defult);
            iv_index_third.setImageResource(R.drawable.m3_defult);
        }
    }

    /**
     * 切换到 排行 界面
     * */
    public void toSecondFragment() {
        int breakColor = getResources().getColor(R.color.bar_txt_default);
        int blueColor = getResources().getColor(R.color.bar_txt_selected);
        if (!curTag.equals(IndexSecond.TAG)) {
            //显示指定的Fragment
            showTabData(IndexSecond.TAG);

            //更新导航栏控件
            tv_index_first.setTextColor(breakColor);
            tv_index_second.setTextColor(blueColor);
            tv_index_third.setTextColor(breakColor);

            iv_index_first.setImageResource(R.drawable.m1_defult);
            iv_index_second.setImageResource(R.drawable.m2_selected);
            iv_index_third.setImageResource(R.drawable.m3_defult);
        }
    }

    /**
     * 切换到 分类 界面
     * */
    public void toThirdFragment() {
        int breakColor = getResources().getColor(R.color.bar_txt_default);
        int blueColor = getResources().getColor(R.color.bar_txt_selected);
        if (!curTag.equals(IndexThird.TAG)) {
            //显示指定的Fragment
            showTabData(IndexThird.TAG);

            //更新导航栏控件
            tv_index_first.setTextColor(breakColor);
            tv_index_second.setTextColor(breakColor);
            tv_index_third.setTextColor(blueColor);

            iv_index_first.setImageResource(R.drawable.m1_defult);
            iv_index_second.setImageResource(R.drawable.m2_defult);
            iv_index_third.setImageResource(R.drawable.m3_selected);
        }
    }

    /** 点击返回按钮2次，退出应用程序 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.exit,
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
