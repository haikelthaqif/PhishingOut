package com.dev.smsphishingdetector.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.dev.smsphishingdetector.Utils.currentUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentMessageDetailsBinding;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class MessageDetailsFragment extends Fragment {
    FragmentMessageDetailsBinding binding;

    ReportsModel model;
    FirebaseDatabase firebaseDatabase;

    //edits
    private FirebaseStorage storage;
    StorageReference gsReference;

    public MessageDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (ReportsModel) getArguments().getSerializable("data");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageDetailsBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        if (model != null) {



            //Glide.with(this).load(model.getImageUri()).into(binding.imgMsg);

            gsReference = storage.getReferenceFromUrl(model.getImageUri());
            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    // Use this downloadUrl with Glide to load the image
                    Glide.with(requireContext()).load(downloadUrl.toString()).into(binding.imgMsg);
                }
            });





            binding.tvMessage.setText(model.getMessage());
            binding.tvcounts.setText(model.getCount() + " People have encountered this Sms Phishing Message");

            binding.btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseDatabase.getReference(Utils.REPORTS).child(model.getId())
                            .child("count").setValue(model.getCount() + 1);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("count", currentUser.getCount() + 1);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        firebaseDatabase.getReference(Utils.USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
                    }
                }
            });

            binding.btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController(MessageDetailsFragment.this)
                            .navigate(R.id.action_messageDetailsFragment_to_reportFragment);
                }
            });
        }

    }
}