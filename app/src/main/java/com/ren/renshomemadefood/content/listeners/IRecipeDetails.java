package com.ren.renshomemadefood.content.listeners;

import com.ren.renshomemadefood.content.models.RecipeDetails;

public interface IRecipeDetails {

    void didFetch(RecipeDetails recipeDetails, String msg);
    void didError(String msg);
}
