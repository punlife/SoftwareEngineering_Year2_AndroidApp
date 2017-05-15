package com.com2027.group03;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by victor on 15.05.2017.
 */

public interface ScoreInterface {

    @POST("login_registration/")
    Call<ScoreResponse> operation(@Body ScoreRequest request);
}
