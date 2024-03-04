package com.leo.riverguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IdrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<String> idrList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texIdr;
        public ViewHolder(View v) {
            super(v);
            texIdr = v.findViewById(R.id.IdrInput);
        }
    }

    public IdrAdapter(Context context, ArrayList<String> shoulderList) {
        this.context = context;
        this.idrList = shoulderList;

    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.idr_info_layout, parent, false);
        IdrAdapter.ViewHolder vh = new IdrAdapter.ViewHolder(v);
        return vh;    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        IdrAdapter.ViewHolder itemHolder = (IdrAdapter.ViewHolder) holder;
        itemHolder.texIdr.setText(idrList.get(position));
    }

    @Override
    public int getItemCount() {
        int dataCount = idrList.size();
        return dataCount;
    }
}
