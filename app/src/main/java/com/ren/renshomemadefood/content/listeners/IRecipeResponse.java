package com.ren.renshomemadefood.content.listeners;

import com.ren.renshomemadefood.content.models.RandomRecipes;

import java.util.List;

public interface IRecipeResponse {

    void didFetch(RandomRecipes recipes, String msg);
    void didError(String msg);

}
