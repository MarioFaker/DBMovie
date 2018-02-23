package com.honghui.dbmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honghui.dbmovie.R;

/**
 * Created by lyw on 2018/2/18.
 */

public class ClassificationAdapter extends BaseAdapter {
    private Context mContext;
    private String[] data;

    public ClassificationAdapter(Context c, String[] list) {
        this.mContext = c;
        this.data = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
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
                    R.layout.gridview_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tv_title.setText(data[position]);

        return convertView;
    }

    public static class ViewHolder {
        TextView tv_title;
    }

}
