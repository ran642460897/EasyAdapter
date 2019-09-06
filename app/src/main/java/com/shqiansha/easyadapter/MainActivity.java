package com.shqiansha.easyadapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shqiansha.adapter.EasyAdapter;
import com.shqiansha.adapter.EasyHolder;
import com.shqiansha.adapter.helper.RecyclerViewHelper;
import com.shqiansha.adapter.listener.OnRecyclerViewClickListener;
import com.shqiansha.adapter.listener.OnRecyclerViewRefreshListener;
import com.shqiansha.easyadapter.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EasyAdapter<User> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        findViewById(R.id.main_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.updateData(null);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.list);
        adapter=new EasyAdapter<User>(this,R.layout.item_test) {
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

            }

            @Override
            public void onLoadMore() {

            }
        };

        new RecyclerViewHelper(recyclerView,adapter,listener).init();



        List<User> users=new ArrayList<>();
        users.add(new User("1","男"));
        users.add(new User("2","男"));
        users.add(new User("3","男"));

        adapter.updateData(users);
    }
}
