package com.dev.smsphishingdetector.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.adapter.SelectionAdapter;
import com.dev.smsphishingdetector.apis.APIInterface;
import com.dev.smsphishingdetector.apis.ApiClient;
import com.dev.smsphishingdetector.apis.Constants;
import com.dev.smsphishingdetector.apis.Response;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentLinkCheckerBinding;
import com.dev.smsphishingdetector.model.OnitemClick;
import com.dev.smsphishingdetector.model.Organisations;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class LinkCheckerFragment extends Fragment {
    FragmentLinkCheckerBinding binding;
    Utils utils;
    APIInterface apiInterface;
    SelectionAdapter adapter;
    Organisations organisations;
    FirebaseDatabase database;
    Boolean isAvailable = null;
    int progress = 0;
    List<Organisations> list=new ArrayList<>();

    String ValidWebsiteLink;


    public LinkCheckerFragment() {
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
        binding = FragmentLinkCheckerBinding.inflate(getLayoutInflater(), container, false);
        database = FirebaseDatabase.getInstance();

        return binding.getRoot();
    }



    private void validationProcess(){

        String userInputLink = binding.SMSMessageTextbox.getText().toString();


        if(userInputLink.contains(ValidWebsiteLink)){
            Toast.makeText(getActivity(),"Valid: "+ ValidWebsiteLink + " and Input Link matches",Toast.LENGTH_SHORT).show();
            String similar = "Link Similar";
            binding.tvSimilar.setText(similar);
            binding.tvSimilar.setTextColor(Color.GREEN);
            progress += 100;
            binding.simpleRatingBar.setRating(100f);
        }

        else{
            Toast.makeText(getActivity(),"Invalid: "+ ValidWebsiteLink + " and Input Link no match",Toast.LENGTH_SHORT).show();
            String Dissimilar = "Link Not Similar";
            binding.tvSimilar.setText(Dissimilar);
            binding.tvSimilar.setTextColor(Color.RED);
            progress += 0;
            binding.simpleRatingBar.setRating(0f);

        }


    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utils = new Utils(requireContext());
        organisations = new Organisations();
        list=organisations.allOrganizations();

        binding.tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.cardView5.getVisibility() == View.GONE) {
                    binding.cardView5.setVisibility(View.VISIBLE);
                } else {
                    binding.cardView5.setVisibility(View.GONE);
                }
            }
        });

        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(LinkCheckerFragment.this).navigate(R.id.action_linkCheckerFragment_to_reportFragment);
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

        adapter = new SelectionAdapter(list, requireContext(), new OnitemClick() {
            @Override
            public void clickListener(int position) {
                binding.tvLink.setText(list.get(position).getTitle());
                ValidWebsiteLink = list.get(position).getUrl();
                Toast.makeText(getActivity(),list.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                binding.cardView5.setVisibility(View.GONE);

            }
        });

        binding.rvOrganizations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrganizations.setAdapter(adapter);
        apiInterface = ApiClient.getClient().create(APIInterface.class);

        binding.btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showDialog("Loading");
                String message = binding.SMSMessageTextbox.getText().toString();



                if (message.isEmpty()) {
                    utils.showToast("No Message Detected");
                    utils.hideDialog();
                    return;
                }

                List<String> link = utils.extractLinks(message);
                if (link.isEmpty()) {
                    utils.showToast("No Links present");
                    utils.hideDialog();
                    return;
                } else {
                    String links = link.get(0);
                    Call<Response> call = apiInterface.getWebLinkInformation("live", links, Constants.APIKEY);
                    call.enqueue(new Callback<Response>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            utils.hideDialog();
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Response responseFromAPI = response.body();



                                    binding.tvOwner.setText(responseFromAPI.getRegistrantContact().getRegistrantCompany());
                                   binding.tvDate.setText(responseFromAPI.getCreate_date());
                                   validationProcess();

                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                    // Parse the birth date
                                    LocalDate birthDate = LocalDate.parse(responseFromAPI.getCreate_date(), formatter);

                                    // Calculate the age in days
                                    long ageInDays = ChronoUnit.DAYS.between(birthDate, LocalDate.now());

                                    // Display the age
                                    binding.tvAge.setText("" + ageInDays + " days old");


                                    binding.location.setText(responseFromAPI.getRegistrantContact().getRegistrantCountryName());
                                    isAvailable = true;


                                }
                            } else {
                                utils.showToast("Link is not available");
                                binding.tvAge.setText("N/A");
                                binding.tvDate.setText("N/A");
                                binding.tvOwner.setText("N/A");
                                binding.location.setText("N/A");
                                isAvailable = false;
                                binding.simpleRatingBar.setRating(0f);

                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            utils.showToast(t.getMessage());
                            utils.hideDialog();
                        }
                    });

                    database.getReference(Utils.REPORTS).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    ReportsModel model = snapshot1.getValue(ReportsModel.class);
                                    if (model.getMessage().contains(message)) {
                                        progress += 20;
                                        binding.tvExists.setText("Yes");
                                    } else {
                                        progress += 0;
                                        binding.tvExists.setText("No");

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                 //   binding.simpleRatingBar.setRating(progress);
                }
            }
        });
    }
}