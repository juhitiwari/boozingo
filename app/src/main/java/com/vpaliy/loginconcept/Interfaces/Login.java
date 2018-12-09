package com.vpaliy.loginconcept.Interfaces;

import java.util.Date;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by slytherin on 11/10/18.
 */

public interface Login {
    @FormUrlEncoded
    @POST("/api/login")
    public void login(
            @Field("userName") String uname,
            @Field("password") String pwd,




            Callback<Response> callback);
}
