package com.dev.smsphishingdetector.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.adapter.OrganizationAdapter;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentHomeBinding;
import com.dev.smsphishingdetector.model.Organisations;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.dev.smsphishingdetector.model.UserData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class homeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseDatabase firebaseDatabase;
    Utils utils;
    List<ReportsModel> list = new ArrayList<>();
    List<ReportsModel> userReported = new ArrayList<>();
    List<Organisations> organisationsList = new ArrayList<>();
    Organisations organisations;
    OrganizationAdapter adapter;
    UserData currentUser;

    public homeFragment() {

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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        utils = new Utils(requireContext());
        organisations = new Organisations();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        binding.tvDate.setText(new SimpleDateFormat("dd MMMM").format(new Date(System.currentTimeMillis())));
        utils.showDialog("Loading Reports");

        firebaseDatabase.getReference(Utils.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UserData userData = snapshot1.getValue(UserData.class);
                        if (userData.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            currentUser = userData;
                            currentUser.setCount(currentUser.getCount() + 1);
                            binding.tvTime.setText(currentUser.getCount() + " ");
                            Utils.currentUser = currentUser;
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        firebaseDatabase.getReference().child(Utils.REPORTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        utils.hideDialog();
                        if (snapshot.exists()) {
                            list.clear();
                            organisationsList.clear();
                            List<Organisations> dbList = new ArrayList<>(organisations.allOrganizations());

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                ReportsModel model = snapshot1.getValue(ReportsModel.class);
                                if (Objects.equals(model.getUserId(), FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    list.add(model);
                                }
                                binding.tvMinutes.setText(list.size() + " ");

                                for (int i = 0; i < dbList.size() - 1; i++) {
                                    if (model.getOrganisationName().toLowerCase(Locale.ROOT)
                                            .equals(dbList.get(i).getTitle().toLowerCase(Locale.ROOT))) {
                                        dbList.get(i).setCount(dbList.get(i).getCount() + 1);
                                        dbList.set(i, dbList.get(i));

                                    }
                                }
                            }
                                dbList.sort(Comparator.comparingInt(Organisations::getCount));
                            Collections.reverse(dbList);
                                organisationsList=dbList.subList(0,2);
                                setBarCharts();
                            binding.rvPhisings.setLayoutManager(new LinearLayoutManager(requireContext()));
                            adapter = new OrganizationAdapter(organisationsList, requireContext());
                            binding.rvPhisings.setAdapter(adapter);

                        }

                        }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        utils.showToast(error.getMessage());
                        utils.hideDialog();
                    }
                });


    }

    private void setBarCharts(){
        BarChart barChart = binding.barCharts;
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, (int) value);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(calendar.getTime());
            }
        });

        List<BarEntry> entries = new ArrayList<>();
        List<Integer> reportsPerDayOfWeek = getReportsPerDayOfWeek();
        for (int i = 0; i < reportsPerDayOfWeek.size(); i++) {
            entries.add(new BarEntry(i + 1, reportsPerDayOfWeek.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Number of Reports");
        dataSet.setColor(Color.BLUE);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        binding.barCharts.setData(barData);
        binding.barCharts.invalidate();
    }
    private List<Integer> getReportsPerDayOfWeek() {
        List<Integer> reportsPerDayOfWeek = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            int numReports = 0;
            for (ReportsModel report : list) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(report.getTime());
                if (calendar.get(Calendar.DAY_OF_WEEK) == i) {
                    numReports++;
                }
            }
            reportsPerDayOfWeek.add(numReports);
        }
        return reportsPerDayOfWeek;
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}