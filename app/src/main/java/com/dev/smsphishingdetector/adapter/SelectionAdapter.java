package com.dev.smsphishingdetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.databinding.ListOrganisationsBinding;
import com.dev.smsphishingdetector.databinding.ListSelectBinding;
import com.dev.smsphishingdetector.model.OnitemClick;
import com.dev.smsphishingdetector.model.Organisations;

import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.Vh> {
    List<Organisations> list;
    Context context;
    OnitemClick onitemClick;

    public SelectionAdapter(List<Organisations> list, Context context,OnitemClick onitemClick) {
        this.list = list;
        this.context = context;
        this.onitemClick=onitemClick;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_select, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        Organisations organisations = list.get(position);
        Glide.with(context)
                .load(organisations.getImageUri())
                .into(holder.binding.imgLogo);
        holder.binding.tvTitle.setText(organisations.getTitle()+" ("+organisations.getUrl()+")");

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
        ListSelectBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);
            binding = ListSelectBinding.bind(itemView);
        }
    }
}

