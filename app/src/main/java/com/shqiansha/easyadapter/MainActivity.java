package com.shqiansha.easyadapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shqiansha.adapter.BaseEasyAdapter;
import com.shqiansha.adapter.EasyHolder;
import com.shqiansha.adapter.helper.RecyclerViewHelper;
import com.shqiansha.adapter.listener.OnRecyclerViewClickListener;
import com.shqiansha.adapter.listener.OnRecyclerViewRefreshListener;
import com.shqiansha.easyadapter.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BaseEasyAdapter<User> adapter;
    private int page=1,size=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
//        findViewById(R.id.main_empty).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.notifyLoadingSucceeded(null);
//            }
//        });
//        findViewById(R.id.main_loading).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.notifyLoading();
//            }
//        });


        RecyclerView recyclerView=findViewById(R.id.list);
        adapter=new BaseEasyAdapter<User>(this,R.layout.item_test) {
            @Override
            public void onBind(@NonNull EasyHolder holder, int position) {
                ((TextView)holder.getView(R.id.item_test_text)).setText(adapter.getItem(position).getName());
            }
        };
        adapter.addOnRecyclerViewClickListener(R.id.item_test_text, new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getApplicationContext(),adapter.getItem(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
        OnRecyclerViewRefreshListener listener=new OnRecyclerViewRefreshListener() {
            @Override
            public void onPullDown() {
                page=1;
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        };

        new RecyclerViewHelper(recyclerView,adapter,listener).bindRefreshLayout((SwipeRefreshLayout) findViewById(R.id.refresh)).init().firstRefresh(false);

    }
    private void getData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<User> users=new ArrayList<>();
                for (int i=0;i<15;i++) {
                    users.add(new User(i+"", "男"));
                }
                adapter.notifyLoadingSucceeded(users,page==1,page==3);
                page++;
            }
        },5000);
    }
}
