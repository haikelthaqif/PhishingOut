package com.dev.smsphishingdetector.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.databinding.ActivityRegisterBinding;
import com.dev.smsphishingdetector.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    Utils utils;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference(Utils.USERS);

        utils = new Utils(this);

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showDialog("Creating User");
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                String phoneNumber = binding.etPhone.getText().toString();
                String user = binding.etUser.getText().toString();

                if (email.isEmpty()) {
                    utils.showToast("The Email field is empty");
                    utils.hideDialog();
                    return;
                }
                if (password.isEmpty()) {
                    utils.showToast("The Password field is empty");
                    utils.hideDialog();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    utils.showToast("The Phone Number field is empty");
                    utils.hideDialog();
                    return;
                }
                if (user.isEmpty()) {
                    utils.showToast("The Username field is empty");
                    utils.hideDialog();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            UserData userData = new UserData(id, user, email, phoneNumber);
                            reference.child(id).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    utils.hideDialog();
                                    if (task.isSuccessful()) {
                                        utils.showToast("Account successfully created. Please Login now");
                                        finish();
                                    } else {
                                        utils.showToast(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            utils.hideDialog();
                            utils.showToast(task.getException().getMessage());
                        }
                    }
                });


            }
        });
    }
}