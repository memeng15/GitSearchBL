package com.app.searchbl;

import com.app.searchbl.model.Detail;
import com.app.searchbl.model.Suggestion;
import com.app.searchbl.model.TopKeywords;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Genflix on 5/31/17.
 */

public interface SOService {

    @GET("top_keywords/index.json")
    Call<TopKeywords> getEmptySuggestion();

    @GET("omniscience/v2.json?user=3e995240926d49ac9ecf60b58ad62167&key=b58bfa45d99e3f1406ff895553eb2d44")
    Call<Suggestion> getWordSuggestion(@Query("word") String word);

    @GET("products.json?per_page=20")
    Call<Detail> getSearchResult(@Query("keywords") String key, @Query("page") int page);
}