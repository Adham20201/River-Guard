package com.leo.riverguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UltrasonicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<String> ultrasonicList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        public ViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.UltrasonicInput);
        }
    }

    public UltrasonicListAdapter(Context context, ArrayList<String> ultrasonicList) {
        this.context = context;
        this.ultrasonicList = ultrasonicList;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ultrasonic_info_layout, parent, false);
        UltrasonicListAdapter.ViewHolder vh = new UltrasonicListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UltrasonicListAdapter.ViewHolder itemHolder = (UltrasonicListAdapter.ViewHolder) holder;
        itemHolder.textName.setText(ultrasonicList.get(position));

    }

    @Override
    public int getItemCount() {
        int dataCount = ultrasonicList.size();
        return dataCount;
    }

}
