package com.litesnap.open;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.litesnap.open.load.LoadAdapter;
import com.litesnap.open.load.MyAdapter;
import com.litesnap.open.open.R;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(String.valueOf(i));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mAdapter = new MyAdapter(this, list);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setLoadView(LayoutInflater.from(this).inflate(R.layout.refresh_down_layout, mRecyclerView, false));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new LoadAdapter.OnLoadingListener() {
            @Override
            public void onLoad() {
                mAdapter.setLoad(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mAdapter.getItemCount() > 20){
                                        mAdapter.setCanLoad(false);
                                        TextView textView = mAdapter.getLoadView().findViewById(R.id.content);
                                        View loadView = mAdapter.getLoadView().findViewById(R.id.load);
                                        loadView.setVisibility(View.GONE);
                                        textView.setText("(*+﹏+*)  No more");
                                    }else {
                                        addData();
                                        mAdapter.setLoad(false);
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void addData(){
        List<Object> l = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            l.add(String.valueOf(i));
        mAdapter.addAll(l);
    }
}
