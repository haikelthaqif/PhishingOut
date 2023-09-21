package com.dev.smsphishingdetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.databinding.ListReportsBinding;
import com.dev.smsphishingdetector.model.OnitemClick;
import com.dev.smsphishingdetector.model.ReportsModel;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.Vh> {
    List<ReportsModel> list;
    Context context;
    OnitemClick onitemClick;

    public ReportsAdapter(List<ReportsModel> list, Context context, OnitemClick onitemClick) {
        this.list = list;
        this.context = context;
        this.onitemClick = onitemClick;
    }

    public void setList(List<ReportsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_reports, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        ReportsModel reportsModel = list.get(position);
        holder.binding.tvMessage.setText(reportsModel.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClick.clickListener(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Vh extends RecyclerView.ViewHolder {
        ListReportsBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);
            binding = ListReportsBinding.bind(itemView);
        }
    }
}

