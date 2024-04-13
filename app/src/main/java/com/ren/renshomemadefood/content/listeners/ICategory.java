package com.ren.renshomemadefood.content.listeners;

import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.models.Root;

public interface ICategory {
    void didFetch(Root recipe, String msg);
    void didError(String msg);
}
