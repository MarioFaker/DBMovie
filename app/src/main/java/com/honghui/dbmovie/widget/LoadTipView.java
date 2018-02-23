package com.honghui.dbmovie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honghui.dbmovie.R;

/**
 * Created by lyw on 2018/2/18.
 */

public class LoadTipView extends RelativeLayout {
    private Context mContext;
    private TextView tv_tip;
    private ProgressBar iv_loading;

    public LoadTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.loading_layout, this,
                true);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        iv_loading = (ProgressBar)findViewById(R.id.iv_loading);
    }

    public boolean isVisible() {
        if (this.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

    public void showLoading() {
        this.setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.VISIBLE);
        tv_tip.setText(mContext.getString(R.string.loading));
    }

    public void showLoadFail(String str) {
        this.setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.GONE);
        tv_tip.setText(str);
    }

    public void showEmpty(String str) {
        this.setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.GONE);
        tv_tip.setText(str);
    }
}
