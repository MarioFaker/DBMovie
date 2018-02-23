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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honghui.dbmovie.R;
import com.honghui.dbmovie.adapter.HotMovieAdapter;
import com.honghui.dbmovie.apivisit.ClientApi;
import com.honghui.dbmovie.model.MovieSimple;
import com.honghui.dbmovie.utils.NetWorkUtils;
import com.honghui.dbmovie.widget.LoadTipView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class IndexFirst extends Fragment {
    public static String TAG = "IndexFirst";
    private LoadTipView loadView;
    private PullToRefreshListView pull_lv;
    private List<MovieSimple> dataList;
    private HotMovieAdapter adapter;
    private boolean isRefreshDown = false;
    private int rp_start = 0;
    private int rp_limit = 20;

    //onCreateView 初始化Fragment的布局。加载布局和findViewById的操作通常在此函数内完成
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载自己的layout布局
        View view = inflater.inflate(R.layout.index_first, null);
        findView(view);

        loadView.showLoading();
        getData();

        return view;
    }

    @SuppressWarnings("unchecked")
    private void findView(View view) {
        dataList = new ArrayList<MovieSimple>();
        adapter = new HotMovieAdapter(getActivity(), dataList);

        pull_lv = (PullToRefreshListView) view.findViewById(R.id.pull_lv);
         //设置刷新的模式：常用的有三种
         //PullToRefreshBase.Mode.BOTH  //上下拉刷新都可以
         //PullToRefreshBase.Mode.PULL_FROM_START  //只允许下拉刷新
         //PullToRefreshBase.Mode.PULL_FROM_END   //只允许上拉刷新
        pull_lv.setMode(PullToRefreshBase.Mode.BOTH);
        //设置Adapter
        pull_lv.setAdapter(adapter);
        //设置item的点击事件
        pull_lv.setOnItemClickListener(itemClickListener);
        //设置下拉刷新和上拉加载动作的监听
        pull_lv.setOnRefreshListener(orderOnRefresh);

        loadView = (LoadTipView) view.findViewById(R.id.loadView);

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

    private OnRefreshListener2 orderOnRefresh = new OnRefreshListener2() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            //下拉刷新的回调，你在这里可以刷新数据
            isRefreshDown = true;
            rp_start = 0;
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            // TODO Auto-generated method stub
            //上拉加载更多的回调，你在这里可以获取列表下一页的数据
            isRefreshDown = false;
            rp_start = dataList.size();
            getData();
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            MovieSimple info = dataList.get((int)arg3);
            Intent intent = new Intent(getActivity(), MovieDetail.class);
            intent.putExtra("id", info.getId());
            intent.putExtra("title", info.getTitle());
            intent.putExtra("imgUrl",info.getImgUrl());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        }
    };

    private void getData() {
        // TODO Auto-generated method stub

        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            // 无网络
            getDataFail(getString(R.string.network_not_connected));
            return;
        }

        String visitUrl = ClientApi.Base_Url+ClientApi.url_hotlist;
        RequestParams params = new RequestParams();
        params.put("city", "广州");
        params.put("start", rp_start);
        params.put("count", rp_limit);

        AsyncHttpClient client1 = ClientApi.getClient();
        if(client1!=null) {
            client1.post(visitUrl, params, new AsyncHttpResponseHandler() {

                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                      Throwable paramThrowable) {
                    //获取数据失败
                    getDataFail(getString(R.string.get_data_fail));
                }

                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    //获取数据成功
                    String response = null;
                    if (arg2 != null) {
                        response = new String(arg2);
                    }

                    if (response != null && response.startsWith("\ufeff")) {
                        response = response.substring(1);
                    }

                    if (isRefreshDown) {
                        dataList.clear();
                    }

                    boolean haveNewData = false;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (Exception e) {
                        jsonObject = null;
                    }

                    if(jsonObject!=null) {
                        JSONArray movieJsonArray = jsonObject.optJSONArray("subjects");
                        if (movieJsonArray != null && movieJsonArray.length() > 0) {
                            for (int i = 0; i < movieJsonArray.length(); i++) {
                                MovieSimple model = new MovieSimple();
                                JSONObject Item = movieJsonArray.optJSONObject(i);

                                model.setId(Item.optString("id"));
                                model.setTitle(Item.optString("title"));
                                model.setYear(Item.optInt("year"));

                                //电影图片
                                JSONObject imgObj = Item.optJSONObject("images");
                                if(imgObj!=null) {
                                    model.setImgUrl(imgObj.optString("small"));
                                }

                                //电影类型
                                String genresStr = "";
                                JSONArray genresJsonArray = Item.optJSONArray("genres");
                                if (genresJsonArray != null && genresJsonArray.length() > 0) {
                                    for(int k=0;k<genresJsonArray.length();k++) {
                                        try {
                                            if(!genresStr.equals("")) {
                                                genresStr+="/";
                                            }
                                            genresStr+=genresJsonArray.getString(k);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                                model.setGenres(genresStr);

                                //电影评分
                                JSONObject ratingObj = Item.optJSONObject("rating");
                                if(ratingObj!=null) {
                                    model.setAverage(ratingObj.optDouble("average"));
                                }

                                //导演列表
                                String directorsStr = "";
                                JSONArray directorsJsonArray = Item.optJSONArray("directors");
                                if (directorsJsonArray != null && directorsJsonArray.length() > 0) {
                                    for(int k=0;k<directorsJsonArray.length();k++) {
                                        JSONObject directorItem = directorsJsonArray.optJSONObject(k);
                                        if(!directorsStr.equals("")) {
                                            directorsStr+="/";
                                        }
                                        directorsStr+=directorItem.optString("name");
                                    }
                                }
                                model.setDirectors(directorsStr);

                                //演员列表
                                String castsStr = "";
                                JSONArray castsJsonArray = Item.optJSONArray("casts");
                                if (castsJsonArray != null && castsJsonArray.length() > 0) {
                                    for(int k=0;k<castsJsonArray.length();k++) {
                                        JSONObject directorItem = castsJsonArray.optJSONObject(k);
                                        if(!castsStr.equals("")) {
                                            castsStr+="/";
                                        }
                                        castsStr+=directorItem.optString("name");
                                    }
                                }
                                model.setCasts(castsStr);


                                dataList.add(model);

                                haveNewData = true;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    pull_lv.onRefreshComplete();

                    if(isRefreshDown) {
                        //刷新成功
                        Toast.makeText(getActivity(),R.string.refresh_success,Toast.LENGTH_SHORT).show();
                    } else if(!haveNewData){
                        //没有更多数据了
                        Toast.makeText(getActivity(),R.string.no_more_data,Toast.LENGTH_SHORT).show();
                    }

                    if (dataList.size() == 0) {
                        loadView.showEmpty(getString(R.string.no_data));
                    } else {
                        loadView.hide();
                    }
                }

            });
        } else {
            //获取数据失败
            getDataFail(getString(R.string.get_data_fail));
        }
    }

    //获取数据失败
    public void getDataFail(String failStr) {
        pull_lv.onRefreshComplete();

        if(dataList.size()==0) {
            loadView.showLoadFail(failStr);
        } else {
            loadView.hide();
            Toast.makeText(getActivity(),failStr,Toast.LENGTH_SHORT).show();
        }
    }
}
