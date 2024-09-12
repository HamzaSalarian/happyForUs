package com.project.happyforus;

import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailSender {

    private static final String TAG = "EmailSender";
    private static final String POSTMARK_SERVER_TOKEN = "Api key goes here"; // Replace with your Postmark API key

    public void sendEmail() {
        // Initialize the API service
        PostmarkApiService apiService = RetrofitClient.getClient("https://api.postmarkapp.com/").create(PostmarkApiService.class);

        // Create the email body
        JsonObject emailBody = new JsonObject();
        emailBody.addProperty("From", "i200405@nu.edu.pk");
        emailBody.addProperty("To", "i200405@nu.edu.pk");
        emailBody.addProperty("Subject", "Service Stopped");
        emailBody.addProperty("TextBody", "The notification listener service has stopped unexpectedly." );

        // Send the email with the API key as a header
        Call<JsonObject> call = apiService.sendEmail(emailBody, POSTMARK_SERVER_TOKEN);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Email sent successfully");
                } else {
                    Log.e(TAG, "Failed to send email: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error sending email", t);
            }
        });
    }
}
