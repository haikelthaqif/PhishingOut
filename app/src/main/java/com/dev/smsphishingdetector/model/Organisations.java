package com.dev.smsphishingdetector.model;

import com.dev.smsphishingdetector.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Organisations implements Serializable {
    String id,title,url;
    int progress,imageUri;

    int count=0;
    public Organisations(String id, int imageUri, String title,String url) {
        this.id = id;
        this.imageUri = imageUri;
        this.title = title;
        this.url=url;
    }

    public Organisations(String name){
        this.title=name;
    }

    public Organisations(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public List<Organisations> allOrganizations(){
        List<Organisations> list=new ArrayList<>();
        list.add(new Organisations("0", R.drawable.sc,"Standard Chartered","sc.com"));
        list.add(new Organisations("1", R.drawable.dhl,"DHL","dhl.com"));
        list.add(new Organisations("2", R.drawable.ocbc,"OCBC","ocbc.com"));
        list.add(new Organisations("3", R.drawable.spost,"SingPost","spost.com"));
        list.add(new Organisations("4", R.drawable.lta,"Land Transport Authority (LTA)","lta.gov.sg"));
        list.add(new Organisations("5", R.drawable.dbs,"DBS","dbs.com.sg"));
        list.add(new Organisations("6", R.drawable.nhs,"NHS","nhs.uk"));
        list.add(new Organisations("7", R.drawable.grab,"Grab","grab.com"));


        return list;
    }

}
