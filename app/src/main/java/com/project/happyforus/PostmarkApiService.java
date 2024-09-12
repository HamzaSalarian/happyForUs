package com.project.happyforus;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PostmarkApiService {
    @POST("/email")
    Call<JsonObject> sendEmail(@Body JsonObject emailBody, @Header("X-Postmark-Server-Token") String apiKey);
}
