package com.example.chillapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("api/phrases")
    Call<List<CustomItem>> getPhrases();

    @GET("api/version")
    Call<VersionResp> getVersion();
}
