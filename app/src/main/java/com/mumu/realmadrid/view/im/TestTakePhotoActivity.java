package com.mumu.realmadrid.view.im;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.utils.ToastUtil;
import lib.widget.FrameProgressLayout;

public class TestTakePhotoActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datas = new ArrayList<>(Arrays.asList("Java", "JavaScript", "C++", "Ruby", "Json", "Html", "Java", "JavaScript", "C++", "Ruby", "Json", "Html", "Java", "JavaScript", "C++"));
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    private ImageView imgAnim;
    private FrameProgressLayout frameProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_take_photo);

//        frameProgressLayout = (FrameProgressLayout) findViewById(R.id.frame_progress);
//        frameProgressLayout.initContainView();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtil.show("onRefresh");
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        });
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
//        imgAnim = (ImageView) findViewById(R.id.img_anim);
//        imgAnim.setImageResource(R.drawable.anim_bear);
//        AnimationDrawable anim = (AnimationDrawable) imgAnim.getDrawable();
//        anim.stop();
//        anim.start();
    }


}
