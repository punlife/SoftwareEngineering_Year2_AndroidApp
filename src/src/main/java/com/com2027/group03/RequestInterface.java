package com.com2027.group03;

/**
 * Created by victor on 13.05.2017.
 */

import com.com2027.group03.ServerRequest;
import com.com2027.group03.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("login_registration/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
