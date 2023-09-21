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

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.adapter.OrganizationAdapter;
import com.dev.smsphishingdetector.adapter.SelectOrganization;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentLibraryBinding;
import com.dev.smsphishingdetector.model.OnitemClick;
import com.dev.smsphishingdetector.model.Organisations;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LibraryFragment extends Fragment {
    FragmentLibraryBinding binding;
    Organisations organisations;
    SelectOrganization adapter;
    List<Organisations> list;


    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(getLayoutInflater(), container, false);
        organisations = new Organisations();
        list = organisations.allOrganizations();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(LibraryFragment.this).navigate(R.id.action_libraryFragment_to_reportFragment);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    search(newText);
                }else {
                    adapter.setList(list);
                }
                return true;
            }
        });

        adapter = new SelectOrganization(list, requireContext(), new OnitemClick() {
            @Override
            public void clickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", list.get(position));
                findNavController(LibraryFragment.this).navigate(R.id.action_libraryFragment_to_organizationReportsFragment, bundle);
            }
        });

        binding.rvOrganisations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrganisations.setAdapter(adapter);

        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });

    }

    public void search(String text) {
        List<Organisations> list1 = new ArrayList<>();
        for (Organisations model : list) {
            if (model.getTitle().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                list1.add(model);
            }
        }
        adapter.setList(list1);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = organisations.allOrganizations();
        adapter.notifyDataSetChanged();
    }
}