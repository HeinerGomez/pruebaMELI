package com.avility.pruebameli.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET(Constants.ENDPOINT_SEARCH)
    Observable<Response<JsonObject>> searchProductsAPI(
            @Query("q") String search,
            @Query("offset") int offset
    );
}
