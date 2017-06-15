package com.grampower.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class DeleteNotification extends AppCompatActivity {

    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    NotificationAdapter adapter;
    ImageButton mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notification);
        Bundle bundle = getIntent().getExtras();
        ArrayList<NotificationWrapper> listOfNotification = bundle.getParcelableArrayList("list");
        mRecyclerView=(RecyclerView)findViewById(R.id.notifyRecyckerView);
        mToolbar=(Toolbar)findViewById(R.id.toolbar_delete_notification);
        mBack=(ImageButton)findViewById(R.id.back_delete_notification);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter(listOfNotification);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteNotification.this,MainActivity.class));
                finish();
            }
        });
    }

    void setAdapter(ArrayList<NotificationWrapper> list){
         adapter=new NotificationAdapter(DeleteNotification.this,list,this);
        mRecyclerView.setAdapter(adapter);
    }


}
