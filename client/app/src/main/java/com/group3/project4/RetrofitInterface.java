package com.group3.project4;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/api/users")
    Call<LoginResult> login(@Body HashMap<String, String> data);

    @POST("/signup")
    Call<Void> signup(@Body HashMap<String, String> data);
}
