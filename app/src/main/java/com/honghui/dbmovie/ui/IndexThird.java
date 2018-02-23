package com.honghui.dbmovie.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.honghui.dbmovie.R;
import com.honghui.dbmovie.adapter.ClassificationAdapter;
import com.honghui.dbmovie.widget.MyGridView;

/**
 * Created by Administrator on 2017/11/3.
 */

public class IndexThird extends Fragment {
    public static String TAG = "IndexThird";
    private String[] xingshiNames={"电影","电视剧","综艺","动画","纪录片","短片"};
    private String[] leixingNames = {"剧情","爱情","喜剧","科幻","动作","悬疑","犯罪","恐怖","青春","励志","战争","文艺","黑色幽默","传记","情色","暴力","音乐","家庭"};
    private String[] diquNames={"大陆","美国","香港","台湾","日本","韩国","英国","法国","德国","意大利","印度","泰国","俄罗斯","伊朗","加拿大","澳大利亚","爱尔兰","瑞典","巴西","丹麦"};

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.index_third, null);
        findView(view);
        return view;
    }

    @SuppressWarnings("unchecked")
    private void findView(View view) {
        MyGridView gv_xingshi = view.findViewById(R.id.gv_xingshi);
        ClassificationAdapter xingshiAdapter = new ClassificationAdapter(getActivity(),xingshiNames);
        gv_xingshi.setAdapter(xingshiAdapter);
        gv_xingshi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String name = xingshiNames[(int)arg3];
                toClassifyAc(name);
            }
        });

        MyGridView gv_leixing = view.findViewById(R.id.gv_leixing);
        ClassificationAdapter leixingAdapter = new ClassificationAdapter(getActivity(),leixingNames);
        gv_leixing.setAdapter(leixingAdapter);
        gv_leixing.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String name = leixingNames[(int)arg3];
                toClassifyAc(name);
            }
        });

        MyGridView gv_diqu = view.findViewById(R.id.gv_diqu);
        ClassificationAdapter diquAdapter = new ClassificationAdapter(getActivity(),diquNames);
        gv_diqu.setAdapter(diquAdapter);
        gv_diqu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String name = diquNames[(int)arg3];
                toClassifyAc(name);
            }
        });

        ImageView iv_search = (ImageView)view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
            }
        });
    }

    private void toClassifyAc(String tag) {
            Intent intent = new Intent(getActivity(), ClassifyActivity.class);
            intent.putExtra("tag", tag);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
    }
}
