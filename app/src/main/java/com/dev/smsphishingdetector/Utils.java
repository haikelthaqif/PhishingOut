package com.dev.smsphishingdetector;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.dev.smsphishingdetector.model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    Context context;
    ProgressDialog progressDialog;
    public static String USERS="users";
    public static String REPORTS="reports";
    public static UserData currentUser;
    public Utils(Context context){
        this.context=context;
        progressDialog=new ProgressDialog(context);

    }

    public void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public void showDialog(String message){
        progressDialog.setMessage(message);
        progressDialog.setIcon(R.drawable.iv_happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public  List<String> extractLinks(String text) {
        List<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String link = matcher.group();
            links.add(link);
        }
        return links;
    }
}
