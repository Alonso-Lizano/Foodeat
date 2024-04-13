package com.ren.renshomemadefood.content.listeners;

import com.ren.renshomemadefood.content.models.Root;


public interface IRecipeSearch {

    void didFetch(Root recipe, String msg);
    void didError(String msg);

}
