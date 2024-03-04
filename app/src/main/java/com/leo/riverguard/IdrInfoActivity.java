package com.leo.riverguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class IdrInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView_idr;
    IdrAdapter idrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_idr);

        recyclerView_idr = findViewById(R.id.recyclerView_idr);


        recyclerView_idr.setLayoutManager(new LinearLayoutManager(IdrInfoActivity.this));
        idrAdapter = new IdrAdapter(IdrInfoActivity.this,UserProfileActivity.connectedThread.idrList);
        recyclerView_idr.setAdapter(idrAdapter);
        recyclerView_idr.setItemAnimator(new DefaultItemAnimator());

    }
}