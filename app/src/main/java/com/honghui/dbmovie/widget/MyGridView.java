package com.honghui.dbmovie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

/**
 * Created by lyw on 2018/2/18.
 */

public class MyGridView extends GridView {
    public boolean useOnMeasure = true;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        Log.d("gv","loadImg:onMeasure:widthMeasureSpec="+widthMeasureSpec+" heightMeasureSpec="+heightMeasureSpec+" expandSpec="+expandSpec);
        useOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
