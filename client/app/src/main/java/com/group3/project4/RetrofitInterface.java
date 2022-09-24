package com.group3.project4;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/api/auth")
    Call<LoginResult> login(@Body HashMap<String, String> data);

    @POST("/api/signup")
    Call<Void> signup(@Body HashMap<String, String> data);
}
