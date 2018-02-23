package com.honghui.dbmovie.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honghui.dbmovie.R;
import com.honghui.dbmovie.apivisit.ClientApi;
import com.honghui.dbmovie.utils.NetWorkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by lyw on 2018/2/18.
 */

public class MovieDetail extends Activity {
    private String id = "";
    private String title = "";
    private String imgUrl = "";

    private TextView tv_load_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        imgUrl = getIntent().getStringExtra("imgUrl");

        initView();
        getData();
    }
    private void initView() {
        TextView tv_tital = (TextView)findViewById(R.id.tv_tital);
        tv_tital.setText(title);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.imgload2)// 加载中显示的图片
                .showImageOnFail(R.drawable.imgload2)// 加载失败显示的图片
                .cacheInMemory(true)// 缓存保存在内存中
                .cacheOnDisk(true)// 缓存保存在硬盘中
                .build();

        ImageView iv_img = (ImageView)findViewById(R.id.iv_img);
        ImageLoader.getInstance().displayImage(imgUrl, iv_img, options);

        ImageView iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        tv_load_info = (TextView)findViewById(R.id.tv_load_info);

    }

    public void getData() {
        if (!NetWorkUtils.isNetworkConnected(MovieDetail.this)) {
            // 无网络
            getDataFail(getString(R.string.network_not_connected));
            return;
        }

        String visitUrl = ClientApi.Base_Url+ClientApi.url_details+"/"+id;
        RequestParams params = new RequestParams();

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

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (Exception e) {
                        jsonObject = null;
                    }

                    if(jsonObject!=null) {
                        TextView tv_movie_title = (TextView)findViewById(R.id.tv_movie_title);
                        tv_movie_title.setText(title);

                        String oTitle = jsonObject.optString("original_title");
                        int year = jsonObject.optInt("year");

                        //电影评分
                        double average = 0;
                        JSONObject ratingObj = jsonObject.optJSONObject("rating");
                        if(ratingObj!=null) {
                            average = ratingObj.optDouble("average");
                        }
                        TextView tv_pingfeng = (TextView)findViewById(R.id.tv_pingfeng);
                        tv_pingfeng.setText(""+average);

                        setStars(average);

                        //评分人数
                        int ratings_count = jsonObject.optInt("ratings_count");
                        TextView tv_ratings_count = (TextView)findViewById(R.id.tv_ratings_count);
                        tv_ratings_count.setText(""+ratings_count+"人");


                        //简介
                        String summary = jsonObject.optString("summary");
                        TextView tv_jianji = (TextView)findViewById(R.id.tv_jianji);
                        tv_jianji.setText("      "+summary);

                        //制片地区
                        String countries = "";
                        JSONArray countriesJsonArray = jsonObject.optJSONArray("countries");
                        if (countriesJsonArray != null && countriesJsonArray.length() > 0) {
                            for(int k=0;k<countriesJsonArray.length();k++) {
                                try {
                                    if(!countries.equals("")) {
                                        countries+="/";
                                    }
                                    countries+=countriesJsonArray.getString(k);
                                } catch (Exception e) {
                                }
                            }
                        }

                        //电影类型
                        String genresStr = "";
                        JSONArray genresJsonArray = jsonObject.optJSONArray("genres");
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

                        String infoStr = "";
                        if(oTitle!=null && oTitle.length()>0) {
                            //电影原名
                            infoStr+= getString(R.string.o_title)+oTitle;
                        }
                        //年份+ 制片地区
                        infoStr+= "\n" + year + "/" + countries;
                        //电影类型
                        infoStr+= "\n" + genresStr;

                        TextView tv_info1 = (TextView)findViewById(R.id.tv_info1);
                        tv_info1.setText(infoStr);


                        LinearLayout ll_yangyuang = (LinearLayout)findViewById(R.id.ll_yangyuang);

                        //导演列表
                        JSONArray directorsJsonArray = jsonObject.optJSONArray("directors");
                        if (directorsJsonArray != null && directorsJsonArray.length() > 0) {
                            for(int k=0;k<directorsJsonArray.length();k++) {
                                JSONObject directorItem = directorsJsonArray.optJSONObject(k);
                                String acName = directorItem.optString("name");

                                String acimageUrl = "";
                                JSONObject directorImg = directorItem.optJSONObject("avatars");
                                if(directorImg!=null) {
                                    acimageUrl =directorImg.optString("small");
                                }

                                View acView = getActorView(acimageUrl,acName,getString(R.string.daoyan2));
                                ll_yangyuang.addView(acView);
                            }
                        }

                        //演员列表
                        JSONArray castsJsonArray = jsonObject.optJSONArray("casts");
                        if (castsJsonArray != null && castsJsonArray.length() > 0) {
                            for(int k=0;k<castsJsonArray.length();k++) {
                                JSONObject directorItem = castsJsonArray.optJSONObject(k);
                                String acName = directorItem.optString("name");

                                String acimageUrl = "";
                                JSONObject directorImg = directorItem.optJSONObject("avatars");
                                if(directorImg!=null) {
                                    acimageUrl =directorImg.optString("small");
                                }

                                View acView = getActorView(acimageUrl,acName,getString(R.string.yanyuan2));
                                ll_yangyuang.addView(acView);
                            }
                        }

                        tv_load_info.setVisibility(View.GONE);
                        RelativeLayout rl_detail_info = (RelativeLayout)findViewById(R.id.rl_detail_info);
                        rl_detail_info.setVisibility(View.VISIBLE);

                    } else {
                        //获取数据失败
                        getDataFail(getString(R.string.get_data_fail));
                    }
                }

            });
        } else {
            //获取数据失败
            getDataFail(getString(R.string.get_data_fail));
        }
    }

    //获取数据失败
    private void getDataFail(String failStr) {
        Toast.makeText(MovieDetail.this,failStr,Toast.LENGTH_SHORT).show();
        tv_load_info.setVisibility(View.VISIBLE);
        tv_load_info.setText(failStr);
    }

    //获取演员item view
    private View getActorView(String actorImgUrl,String name,String type) {
        View actorView = LayoutInflater.from(MovieDetail.this).inflate(
                R.layout.actor_item, null);
        ImageView iv_img = (ImageView)actorView.findViewById(R.id.iv_img);
        TextView tv_name = (TextView)actorView.findViewById(R.id.tv_name);
        TextView tv_type = (TextView)actorView.findViewById(R.id.tv_type);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.imgload1)// 加载中显示的图片
                .showImageOnFail(R.drawable.imgload1)// 加载失败显示的图片
                .cacheInMemory(true)// 缓存保存在内存中
                .cacheOnDisk(true)// 缓存保存在硬盘中
                .build();
        ImageLoader.getInstance().displayImage(actorImgUrl, iv_img, options);

        tv_name.setText(name);
        tv_type.setText(type);
        return actorView;
    }

    //设置星级
    private void setStars(double dAverge) {
        int averge = new BigDecimal(dAverge).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        ImageView iv_start1 = (ImageView) findViewById(R.id.iv_start1);
        ImageView iv_start2 = (ImageView) findViewById(R.id.iv_start2);
        ImageView iv_start3 = (ImageView) findViewById(R.id.iv_start3);
        ImageView iv_start4 = (ImageView) findViewById(R.id.iv_start4);
        ImageView iv_start5 = (ImageView) findViewById(R.id.iv_start5);
        switch (averge) {
            case 1:
                iv_start1.setImageResource(R.drawable.start_half);
                break;
            case 2:
                iv_start1.setImageResource(R.drawable.start1);
                break;
            case 3:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start_half);
                break;
            case 4:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                break;
            case 5:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start_half);
                break;
            case 6:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start1);
                break;
            case 7:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start1);
                iv_start4.setImageResource(R.drawable.start_half);
                break;
            case 8:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start1);
                iv_start4.setImageResource(R.drawable.start1);
                break;
            case 9:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start1);
                iv_start4.setImageResource(R.drawable.start1);
                iv_start5.setImageResource(R.drawable.start_half);
                break;
            case 10:
                iv_start1.setImageResource(R.drawable.start1);
                iv_start2.setImageResource(R.drawable.start1);
                iv_start3.setImageResource(R.drawable.start1);
                iv_start4.setImageResource(R.drawable.start1);
                iv_start5.setImageResource(R.drawable.start1);
                break;
        }

    }

    public void finishActivity(){
        this.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
