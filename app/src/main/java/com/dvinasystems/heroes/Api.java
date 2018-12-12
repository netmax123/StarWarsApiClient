/*
Define endpoint and GET method for Retrofit
Created by Maxim G. on December 12, 2018
*/


package com.dvinasystems.heroes;

import com.dvinasystems.heroes.json.ResultsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

    String BASE_URL = "https://swapi.co/api/";

    @GET
    Call<ResultsList> getAllResults(@Url String url);

}
