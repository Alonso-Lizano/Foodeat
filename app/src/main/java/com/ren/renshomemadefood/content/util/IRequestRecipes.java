package com.ren.renshomemadefood.content.util;

import com.ren.renshomemadefood.content.models.InstructionsResponse;
import com.ren.renshomemadefood.content.models.RandomRecipes;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.models.Root;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestRecipes {
    @GET("recipes/random")
    Call<RandomRecipes> RECIPES_CALL(
            @Query("apiKey") String apiKey,
            @Query("number") String number
    );

    @GET("recipes/complexSearch")
    Call<Root> SEARCH_RECIPE_CALL(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("number") String number
    );

    @GET("recipes/complexSearch")
    Call<Root> CATEGORY_RECIPE_CALL(
            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("type") String type
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetails> RECIPE_DETAILS_CALL(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/analyzedInstructions")
    Call<List<InstructionsResponse>> INSTRUCTIONS_CALL(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );
}
