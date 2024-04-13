package com.ren.renshomemadefood.content.util;

import android.content.Context;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.ICategory;
import com.ren.renshomemadefood.content.listeners.IInstruction;
import com.ren.renshomemadefood.content.listeners.IRecipeDetails;
import com.ren.renshomemadefood.content.listeners.IRecipeResponse;
import com.ren.renshomemadefood.content.listeners.IRecipeSearch;
import com.ren.renshomemadefood.content.models.InstructionsResponse;
import com.ren.renshomemadefood.content.models.RandomRecipes;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.models.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {

    private Context context;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create()).build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(IRecipeResponse iRecipeResponse) {
        IRequestRecipes recipes = retrofit.create(IRequestRecipes.class);
        Call<RandomRecipes> call = recipes.RECIPES_CALL(context.getString(R.string.api_key), "20");
        call.enqueue(new Callback<RandomRecipes>() {
            @Override
            public void onResponse(Call<RandomRecipes> call, Response<RandomRecipes> response) {
                if (!response.isSuccessful()) {
                    iRecipeResponse.didError(response.message());
                    return;
                }
                iRecipeResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipes> call, Throwable throwable) {
                iRecipeResponse.didError(throwable.getMessage());
            }
        });
    }

    public void getSearchRecipe(IRecipeSearch iRecipeSearch, String name) {
        IRequestRecipes recipes = retrofit.create(IRequestRecipes.class);
        Call<Root> call = recipes.SEARCH_RECIPE_CALL(context.getString(R.string.api_key), name, "20");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (!response.isSuccessful()) {
                    iRecipeSearch.didError(response.message());
                    return;
                }
                iRecipeSearch.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<Root> call, Throwable throwable) {
                iRecipeSearch.didError(throwable.getMessage());
            }
        });
    }

    public void getRecipesByType(ICategory iCategory, String type) {
        IRequestRecipes recipes = retrofit.create(IRequestRecipes.class);
        Call<Root> call = recipes.CATEGORY_RECIPE_CALL(context.getString(R.string.api_key), "20", type);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    iCategory.didFetch(response.body(), response.message());
                } else {
                    iCategory.didError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable throwable) {
                iCategory.didError(throwable.getMessage());
            }
        });
    }

    public void getRecipeDetails(IRecipeDetails iRecipeDetails, int id) {
        IRequestRecipes recipes = retrofit.create(IRequestRecipes.class);
        Call<RecipeDetails> call = recipes.RECIPE_DETAILS_CALL(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if (!response.isSuccessful()) {
                    iRecipeDetails.didError(response.message());
                    return;
                }
                iRecipeDetails.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable throwable) {
                iRecipeDetails.didError(throwable.getMessage());
            }
        });
    }

    public void getInstruction(IInstruction iInstruction, int id) {
        IRequestRecipes recipes = retrofit.create(IRequestRecipes.class);
        Call<List<InstructionsResponse>> call = recipes.INSTRUCTIONS_CALL(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<InstructionsResponse>> call, Response<List<InstructionsResponse>> response) {
                if (!response.isSuccessful()) {
                    iInstruction.didError(response.message());
                    return;
                }
                iInstruction.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<InstructionsResponse>> call, Throwable throwable) {
                iInstruction.didError(throwable.getMessage());
            }
        });
    }

}
