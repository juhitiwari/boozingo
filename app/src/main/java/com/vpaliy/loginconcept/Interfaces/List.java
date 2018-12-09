package com.vpaliy.loginconcept.Interfaces;

import com.vpaliy.loginconcept.Places;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit2.Call;

/**
 * Created by slytherin on 21/10/18.
 */

public interface List {
    @FormUrlEncoded
    @POST("/api/list")
    public void list(
            @Field("long") Double longt,
            @Field("lati") Double lat,
            @Field("range") Integer range,
            @Field("sort_by") String sort,

            Callback<Response> callback);
}
