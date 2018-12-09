package com.vpaliy.loginconcept.Interfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by slytherin on 11/10/18.
 */

public interface ConfirmOTP {
    @FormUrlEncoded
    @POST("/api/confirm_otp")
    public void confirmOTP(
            @Field("otp") String otp,
            @Field("mobile_no") String mobile,


            Callback<Response> callback);
}
