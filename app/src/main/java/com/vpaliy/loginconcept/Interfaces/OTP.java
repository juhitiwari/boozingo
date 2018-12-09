package com.vpaliy.loginconcept.Interfaces;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by slytherin on 20/9/18.
 */

public interface OTP {
    @FormUrlEncoded
    @POST("/api/generate_otp")
    public void generateOTP(
            @Field("new_user") String bool,
            @Field("mobile_no") String mobile,


            Callback<Response> callback);
}
