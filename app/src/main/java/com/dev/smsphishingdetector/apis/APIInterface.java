package com.dev.smsphishingdetector.apis;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    //Get information data of the weblink
    @GET("v1.0/whois")
    Call<Response> getWebLinkInformation(@Query("whois") String whois, @Query("domainName") String lon, @Query("apiKey") String apiKey);


}
