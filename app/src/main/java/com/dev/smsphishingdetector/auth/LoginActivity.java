package com.dev.smsphishingdetector.auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dev.smsphishingdetector.MainActivity;
import com.dev.smsphishingdetector.R;
import com.dev.smsphishingdetector.Utils;
import com.dev.smsphishingdetector.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Utils utils;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utils = new Utils(this);
        firebaseAuth = FirebaseAuth.getInstance();


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showDialog("Signing in");
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                if (email.isEmpty()) {
                    utils.showToast("Email field is empty. Please enter Email");
                    utils.hideDialog();
                    return;
                }
                if (password.isEmpty()) {
                    utils.showToast("Password field is Empty. Please enter Password");
                    utils.hideDialog();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        utils.hideDialog();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            utils.showToast("Email or Password is invalid. Please try again");
                        }
                    }
                });

            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        binding.tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForget();
            }
        });
    }

    private void showForget(){
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_forget,null,false);
        AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.setView(view);
        EditText etEmail=view.findViewById(R.id.etEmail);
        MaterialButton btnSend=view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString();
                if (email.isEmpty()){
                    utils.showDialog("Email is empty");
                    return;
                }
                utils.showDialog("Sending link");
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        utils.hideDialog();
                        if (task.isSuccessful()){
                            utils.showToast("Reset Link sent to your email");
                        }else {
                            utils.showToast("Email does not existing in our database");
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}