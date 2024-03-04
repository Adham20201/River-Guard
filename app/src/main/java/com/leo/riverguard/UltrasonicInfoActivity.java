package com.leo.riverguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class UltrasonicInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView_ultrasonicList;
    UltrasonicListAdapter ultrasonicListListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ultrasonic);

        recyclerView_ultrasonicList = findViewById(R.id.recyclerView_ultrasonic);
        recyclerView_ultrasonicList.setLayoutManager(new LinearLayoutManager(UltrasonicInfoActivity.this));
        ultrasonicListListAdapter = new UltrasonicListAdapter(UltrasonicInfoActivity.this,UserProfileActivity.connectedThread.ultrasonicList);
        recyclerView_ultrasonicList.setAdapter(ultrasonicListListAdapter);
        recyclerView_ultrasonicList.setItemAnimator(new DefaultItemAnimator());

    }
}