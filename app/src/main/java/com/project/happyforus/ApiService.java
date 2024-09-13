package com.project.happyforus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @POST("store_notifications.php")
    Call<Void> sendNotificationToServer(@Body NotificationRequest notificationData);

    @FormUrlEncoded
    @POST("test_db_connection.php")
    Call<ConnectionResponse> testDbConnection(
            @Field("db_server") String dbServer,
            @Field("db_username") String dbUsername,
            @Field("db_password") String dbPassword,
            @Field("db_name") String dbName,
            @Field("db_table") String dbTable
    );
}
