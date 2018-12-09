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

public interface RegisterUSer {
    @FormUrlEncoded
    @POST("/api/register")
    public void RegisterUser(
            @Field("firstName") String fname,
            @Field("lastName") String lname,
            @Field("userName") String uname,
            @Field("password") String pwd,
            @Field("confirmpassword") String cpwd,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("dob") Date dob,



            Callback<Response> callback);
}
