package com.dev.smsphishingdetector.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.auth.LoginActivity;
import com.dev.smsphishingdetector.databinding.FragmentReportBinding;
import com.dev.smsphishingdetector.model.Organisations;
import com.dev.smsphishingdetector.model.ReportsModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    FragmentReportBinding binding;
    Utils utils;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String imageUri;


    public ReportFragment() {
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
        binding = FragmentReportBinding.inflate(getLayoutInflater(), container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        utils = new Utils(requireContext());
        ref = firebaseDatabase.getReference(Utils.REPORTS);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ReportFragment.this)
                        .crop()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addReport();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            binding.imgMessage.setImageURI(data.getData());
            binding.imgMessage.setVisibility(View.VISIBLE);
            binding.imgUpload.setVisibility(View.GONE);
            binding.tvUpload.setVisibility(View.GONE);
            imageUri = data.getData().toString();
        } else {
            utils.showToast("Canceled");
        }
    }

    private void addReport() throws FileNotFoundException {
        String name = binding.etName.getText().toString();
        String message = binding.etMessage.getText().toString();
        String number = binding.etNumber.getText().toString();
        Long date = System.currentTimeMillis();


        if (name.isEmpty()) {
            utils.showToast("Please Enter Name");
            return;
        }
        if (message.isEmpty()) {
            utils.showToast("Please Enter Organisation name");
            return;
        }
        if (number.isEmpty()) {
            utils.showToast("Please Enter Caller ID Number");
            return;
        }
        utils.showDialog("Reporting Message");

        String id = ref.push().getKey();


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference profileRef = storageReference.child("images" + id + ".jpg");

        InputStream inputStream = requireActivity().getContentResolver().openInputStream(Uri.parse(imageUri));
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        //Upload Image request to firebase Storage
        UploadTask uploadTask = profileRef.putBytes(bytes);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                utils.hideDialog();
                throw task.getException();
            }
            return profileRef.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    //Uploaded Successfully now proceed to add new Data
                    ReportsModel model = new ReportsModel(id, name, number, message, imageUri, FirebaseAuth.getInstance().getCurrentUser().getUid(), date);
                    ref.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            utils.hideDialog();
                            if (task.isSuccessful()) {

                                Toast.makeText(requireContext(), "Report Added Successfully", Toast.LENGTH_SHORT).show();
                                findNavController(ReportFragment.this).navigateUp();
                            } else {
                                Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    utils.hideDialog();
                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}