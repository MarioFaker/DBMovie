package com.honghui.dbmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honghui.dbmovie.R;
import com.honghui.dbmovie.model.MovieSimple;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lyw on 2018/2/17.
 */

public class HotMovieAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieSimple> data;
    private DisplayImageOptions options;

    public HotMovieAdapter(Context c, List<MovieSimple> list) {
        this.mContext = c;
        this.data = list;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.imgload1)// 加载中显示的图片
                .showImageOnFail(R.drawable.imgload1)// 加载失败显示的图片
                .cacheInMemory(true)// 缓存保存在内存中
                .cacheOnDisk(true)// 缓存保存在硬盘中
                .build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.hot_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mViewHolder.tv_average = (TextView) convertView.findViewById(R.id.tv_average);
            mViewHolder.tv_daoyan = (TextView) convertView.findViewById(R.id.tv_daoyan);
            mViewHolder.tv_yanyuang = (TextView) convertView.findViewById(R.id.tv_yanyuang);
            mViewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            mViewHolder.iv_start1 = (ImageView) convertView.findViewById(R.id.iv_start1);
            mViewHolder.iv_start2 = (ImageView) convertView.findViewById(R.id.iv_start2);
            mViewHolder.iv_start3 = (ImageView) convertView.findViewById(R.id.iv_start3);
            mViewHolder.iv_start4 = (ImageView) convertView.findViewById(R.id.iv_start4);
            mViewHolder.iv_start5 = (ImageView) convertView.findViewById(R.id.iv_start5);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        MovieSimple model = data.get(position);
        setViewText(mViewHolder.tv_title,model.getTitle(), "--");
        mViewHolder.tv_average.setText(""+model.getAverage());
        mViewHolder.tv_daoyan.setText(mContext.getResources().getString(R.string.daoyan)+model.getDirectors());
        mViewHolder.tv_yanyuang.setText(mContext.getResources().getString(R.string.yanyuan)+model.getCasts());

        ImageLoader.getInstance().displayImage(model.getImgUrl(), mViewHolder.iv_img, options);

        int averge = new BigDecimal(model.getAverage()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        switch (averge) {
            case 1:
                mViewHolder.iv_start1.setImageResource(R.drawable.start_half);
                break;
            case 2:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                break;
            case 3:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start_half);
                break;
            case 4:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                break;
            case 5:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start_half);
                break;
            case 6:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start1);
                break;
            case 7:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start1);
                mViewHolder.iv_start4.setImageResource(R.drawable.start_half);
                break;
            case 8:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start1);
                mViewHolder.iv_start4.setImageResource(R.drawable.start1);
                break;
            case 9:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start1);
                mViewHolder.iv_start4.setImageResource(R.drawable.start1);
                mViewHolder.iv_start5.setImageResource(R.drawable.start_half);
                break;
            case 10:
                mViewHolder.iv_start1.setImageResource(R.drawable.start1);
                mViewHolder.iv_start2.setImageResource(R.drawable.start1);
                mViewHolder.iv_start3.setImageResource(R.drawable.start1);
                mViewHolder.iv_start4.setImageResource(R.drawable.start1);
                mViewHolder.iv_start5.setImageResource(R.drawable.start1);
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView tv_title,tv_average,tv_daoyan,tv_yanyuang;
        ImageView iv_img,iv_start1,iv_start2,iv_start3,iv_start4,iv_start5;
    }

    /**
     * 设置文本控件内容，如果内容为空，则显示defaultStr
     * */
    public void setViewText(TextView tv, String str, String defaultStr) {
        if (str == null || str.equals("null") || str.length() == 0) {
            tv.setText(defaultStr);
        } else {
            tv.setText(str);
        }
    }
}

