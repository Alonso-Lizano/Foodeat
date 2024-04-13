package com.ren.renshomemadefood.content.screens.components;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.listeners.IRecipeSearch;
import com.ren.renshomemadefood.content.models.Root;
import com.ren.renshomemadefood.content.util.RequestManager;
import com.ren.renshomemadefood.content.adapters.SearchRecipeAdapter;

public class SearchScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private RequestManager manager;
    private SearchRecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        recyclerView = findViewById(R.id.searchRv);
        searchView = findViewById(R.id.searchView);

        manager = new RequestManager(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                manager.getSearchRecipe(searchRecipeResponse, newText);
                return true;
            }
        });
    }

    private final IRecipeSearch searchRecipeResponse = new IRecipeSearch() {

        @Override
        public void didFetch(Root recipe, String msg) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchScreen.this));
            recipeAdapter = new SearchRecipeAdapter(SearchScreen.this, recipe.results, recipeClick);
            recyclerView.setAdapter(recipeAdapter);
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(SearchScreen.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private final IRecipeClick recipeClick = id -> {
        Intent intent = new Intent(this, RecipeScreen.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };
}