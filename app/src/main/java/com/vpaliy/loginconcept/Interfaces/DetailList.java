package com.vpaliy.loginconcept.Interfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by slytherin on 22/10/18.
 */

public interface DetailList {
    @FormUrlEncoded
    @POST("/api/get_details")
    public void details(
            @Field("bar_id") Integer id,


            Callback<Response> callback);
}
