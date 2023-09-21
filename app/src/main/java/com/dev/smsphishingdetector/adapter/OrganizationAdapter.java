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
import com.dev.smsphishingdetector.model.Organisations;

import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.Vh> {
    List<Organisations> list;
    Context context;

    public OrganizationAdapter(List<Organisations> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_organisations, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        Organisations organisations = list.get(position);
        Glide.with(context)
                .load(organisations.getImageUri())
                .into(holder.binding.imgView);
        holder.binding.tvTitle.setText(organisations.getTitle());
        holder.binding.tvDesc.setText(organisations.getCount()+" sms in library");
        holder.binding.progress.setMax(5);
        holder.binding.progress.setProgress(organisations.getCount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Vh extends RecyclerView.ViewHolder {
        ListOrganisationsBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);
            binding = ListOrganisationsBinding.bind(itemView);
        }
    }
}
