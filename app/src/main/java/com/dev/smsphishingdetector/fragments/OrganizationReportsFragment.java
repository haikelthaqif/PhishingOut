package com.dev.smsphishingdetector.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.adapter.ReportsAdapter;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentOrganizationReportsBinding;
import com.dev.smsphishingdetector.model.OnitemClick;
import com.dev.smsphishingdetector.model.Organisations;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrganizationReportsFragment extends Fragment {
    FragmentOrganizationReportsBinding binding;
    Organisations organisations;
    FirebaseDatabase firebaseDatabase;
    List<ReportsModel> list = new ArrayList<>();
    Utils utils;
    ReportsAdapter adapter;

    public OrganizationReportsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            organisations = (Organisations) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrganizationReportsBinding.inflate(getLayoutInflater(), container, false);
        utils = new Utils(requireContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (organisations != null) {
            Glide.with(requireContext()).load(organisations.getImageUri()).into(binding.imgView);
            binding.tvTitle.setText(organisations.getTitle());

            utils.showDialog("Fetching Reports");
            firebaseDatabase.getReference(Utils.REPORTS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            utils.hideDialog();
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    ReportsModel model = snapshot1.getValue(ReportsModel.class);
                                    if (model.getOrganisationName().toLowerCase(Locale.ROOT).
                                            equals(organisations.getTitle().toLowerCase(Locale.ROOT))) {
                                        list.add(model);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                binding.rvMessages.setVisibility(View.VISIBLE);
                                binding.tvNoMessage.setVisibility(View.GONE);
                            } else {
                                binding.rvMessages.setVisibility(View.GONE);
                                binding.tvNoMessage.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController(OrganizationReportsFragment.this)
                            .navigate(R.id.action_organizationReportsFragment_to_reportFragment);
                }
            });

            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });

            binding.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish();
                }
            });

            // set RecyclerView to display reports using LinearLayoutManager and ReportsAdapter
            binding.rvMessages.setLayoutManager(new LinearLayoutManager(requireActivity()));
            adapter = new ReportsAdapter(list, requireContext(), position -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", list.get(position));
                findNavController(OrganizationReportsFragment.this)
                        .navigate(R.id.action_organizationReportsFragment_to_messageDetailsFragment, bundle);
            });
            binding.rvMessages.setAdapter(adapter);

        }
    }

    public void search(String text) {
        List<ReportsModel> list1 = new ArrayList<>();
        for (ReportsModel model : list) {
            if (text.toLowerCase(Locale.ROOT).contains(model.getMessage().toLowerCase(Locale.ROOT))) {
                list1.add(model);
            }
        }
        adapter.setList(list1);
    }
}