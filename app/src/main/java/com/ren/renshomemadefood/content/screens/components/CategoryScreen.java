package com.ren.renshomemadefood.content.screens.components;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.ICategory;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.models.Root;
import com.ren.renshomemadefood.content.adapters.CategoryAdapter;
import com.ren.renshomemadefood.content.util.RequestManager;

import java.util.ArrayList;

public class CategoryScreen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestManager manager;
    private CategoryAdapter categoryAdapter;
    private TextView title;
    private ImageView back;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_screen);
        recyclerView = findViewById(R.id.categoryRv);
        title = findViewById(R.id.titleType);
        back = findViewById(R.id.ivReturn);
        manager = new RequestManager(this);
        categoryName = getIntent().getStringExtra("categoryName");
        title.setText(categoryName);
        manager.getRecipesByType(searchByCategory, categoryName);

        back.setOnClickListener(v -> finish());
    }

    private final ICategory searchByCategory = new ICategory() {
        @Override
        public void didFetch(Root recipe, String msg) {
            recyclerView.setLayoutManager(new LinearLayoutManager(CategoryScreen.this));
            categoryAdapter = new CategoryAdapter(CategoryScreen.this, recipe.results, recipeClick);
            recyclerView.setAdapter(categoryAdapter);
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(CategoryScreen.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private final IRecipeClick recipeClick = id -> {
        Intent intent = new Intent(this, RecipeScreen.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };
}