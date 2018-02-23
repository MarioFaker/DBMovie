package com.honghui.dbmovie.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
 * Created by lyw on 2018/2/18.
 */

public class SearchActivity extends Activity {
    private LoadTipView loadView;
    private PullToRefreshListView pull_lv;
    private List<MovieSimple> dataList;
    private HotMovieAdapter adapter;
    private boolean isRefreshDown = false;
    private int rp_start = 0;
    private int rp_limit = 20;

    private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ac);

        initView();
    }
    private void initView() {
        ImageView iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        EditText et_search = (EditText)findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if(arg1 == EditorInfo.IME_ACTION_SEARCH) {

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(arg0.getWindowToken(), 0);

                    String str1 = arg0.getText().toString().trim();
                    if(str1.length()>0) {
                        searchStr = str1;

                        dataList.clear();
                        adapter.notifyDataSetChanged();

                        loadView.showLoading();
                        getData();
                    } else {
                        Toast.makeText(SearchActivity.this,R.string.input_tip,Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

        });

        pull_lv = (PullToRefreshListView) findViewById(R.id.pull_lv);
        pull_lv.setMode(PullToRefreshBase.Mode.BOTH);

        dataList = new ArrayList<MovieSimple>();
        adapter = new HotMovieAdapter(SearchActivity.this, dataList);
        pull_lv.setAdapter(adapter);
        pull_lv.setOnItemClickListener(itemClickListener);
        pull_lv.setOnRefreshListener(orderOnRefresh);

        loadView = (LoadTipView) findViewById(R.id.loadView);
    }

    private OnRefreshListener2 orderOnRefresh = new OnRefreshListener2() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            isRefreshDown = true;
            rp_start = 0;
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            // TODO Auto-generated method stub
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
            Intent intent = new Intent(SearchActivity.this, MovieDetail.class);
            intent.putExtra("id", info.getId());
            intent.putExtra("title", info.getTitle());
            intent.putExtra("imgUrl",info.getImgUrl());
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        }
    };

    private void getData() {
        // TODO Auto-generated method stub

        if (!NetWorkUtils.isNetworkConnected(SearchActivity.this)) {
            // 无网络
            getDataFail(getString(R.string.network_not_connected));
            return;
        }

        String visitUrl = ClientApi.Base_Url+ClientApi.url_search;
        RequestParams params = new RequestParams();
        params.put("q", searchStr);
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
                        Toast.makeText(SearchActivity.this,R.string.refresh_success,Toast.LENGTH_SHORT).show();
                    } else if(!haveNewData){
                        //没有更多数据了
                        Toast.makeText(SearchActivity.this,R.string.no_more_data,Toast.LENGTH_SHORT).show();
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

        if (isRefreshDown) {
            dataList.clear();
            adapter.notifyDataSetChanged();
        }

        if(dataList.size()==0) {
            loadView.showLoadFail(failStr);
        } else {
            loadView.hide();
            Toast.makeText(SearchActivity.this,failStr,Toast.LENGTH_SHORT).show();
        }
    }

    public void finishActivity(){
        this.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
