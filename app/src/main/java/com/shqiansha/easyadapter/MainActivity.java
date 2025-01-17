package com.shqiansha.easyadapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shqiansha.adapter.BaseEasyAdapter;
import com.shqiansha.adapter.EasyHolder;
import com.shqiansha.adapter.helper.RecyclerViewHelper;
import com.shqiansha.adapter.listener.OnItemChildClickListener;
import com.shqiansha.adapter.listener.OnItemChildLongClickListener;
import com.shqiansha.adapter.listener.OnItemClickListener;
import com.shqiansha.adapter.listener.OnItemLongClickListener;
import com.shqiansha.adapter.listener.OnRefreshListener;
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
        SwipeRefreshLayout refreshLayout=findViewById(R.id.refresh);
        adapter=new BaseEasyAdapter<User>(this,R.layout.item_test) {
            @Override
            public void onBind(@NonNull EasyHolder holder, int position) {
                ((TextView)holder.getView(R.id.item_test_text)).setText(adapter.getItem(position).getName());
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getApplicationContext(),"item",Toast.LENGTH_LONG).show();
            }
        });
        adapter.addOnItemChildClickListener(R.id.item_test_text, new OnItemChildClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getApplicationContext(),"child",Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getApplicationContext(),"item long",Toast.LENGTH_LONG).show();
            }
        });
        adapter.addOnItemChildLongClickListener(R.id.item_test_text, new OnItemChildLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getApplicationContext(),"child long",Toast.LENGTH_LONG).show();
            }
        });


        OnRefreshListener listener=new OnRefreshListener() {
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
        new RecyclerViewHelper.Builder().setRecyclerView(recyclerView).setEasyAdapter(adapter).setRefreshLayout(refreshLayout).setRefreshListener(listener).build().firstRefresh(true);
    }
    private void getData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<User> users=new ArrayList<>();
                for (int i=0;i<15;i++) {
                    users.add(new User(i+"", "男"));
                }
                adapter.notifyLoadingCompleted(users,page==1,page==3);
                page++;
            }
        },5000);
    }
}
