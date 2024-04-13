package com.ren.renshomemadefood.content.screens.components;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.adapters.InstructionAdapter;
import com.ren.renshomemadefood.content.listeners.IInstruction;
import com.ren.renshomemadefood.content.listeners.IRecipeDetails;
import com.ren.renshomemadefood.content.models.FavoriteRecipe;
import com.ren.renshomemadefood.content.models.InstructionsResponse;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.adapters.IngredientsAdapter;
import com.ren.renshomemadefood.content.screens.drawermenu.FavoriteScreen;
import com.ren.renshomemadefood.content.util.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeScreen extends AppCompatActivity {
    private static final String FAVORITE_KEY = "Favorites";
    private static final String USERS_KEY = "Users";
    private ImageView fullScreenBtn;
    private ImageView imgRecipe;
    private ImageView backRecipe;
    private TextView title;
    private TextView time;
    private boolean fullSizeImg = true;
    private int id;
    private RequestManager manager;
    private ImageView shade;
    private IngredientsAdapter ingredientsAdapter;
    private Button ingredient;
    private Button steps;
    private Button nutrition;
    private RecyclerView ingData;
    private RecyclerView stepsData;
    private InstructionAdapter instructionAdapter;
    private ImageView favoriteBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_screen);

        fullScreenBtn = findViewById(R.id.fullScreenBtn);
        imgRecipe = findViewById(R.id.imgRecipe);
        backRecipe = findViewById(R.id.backRecipe);
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        shade = findViewById(R.id.shade);
        ingData = findViewById(R.id.ingData);
        ingredient = findViewById(R.id.ingredients);
        steps = findViewById(R.id.steps);
        nutrition = findViewById(R.id.nutritional);
        stepsData = findViewById(R.id.stepsData);
        favoriteBtn = findViewById(R.id.favoriteBtn);

        stepsData.setVisibility(View.GONE);

        id = Integer.parseInt(getIntent().getStringExtra("id"));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetails, id);
        manager.getInstruction(instruction, id);

        onPressBackBtn(backRecipe);
        if (currentUser != null) {
            checkIfFavorite();
        }
    }

    private final IInstruction instruction = new IInstruction() {
        @Override
        public void didFetch(List<InstructionsResponse> instructions, String msg) {
            stepsData.setHasFixedSize(true);
            stepsData.setLayoutManager(new LinearLayoutManager(RecipeScreen.this,
                    LinearLayoutManager.VERTICAL, false));
            instructionAdapter = new InstructionAdapter(RecipeScreen.this, instructions);
            stepsData.setAdapter(instructionAdapter);
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(RecipeScreen.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private final IRecipeDetails recipeDetails = new IRecipeDetails() {
        @Override
        public void didFetch(RecipeDetails recipeDetails, String msg) {
            title.setText(recipeDetails.title);
            time.setText(String.format(getString(R.string.ready_in_minutes), recipeDetails.readyInMinutes));
            Picasso.get().load(recipeDetails.image).into(imgRecipe);

            ingData.setHasFixedSize(true);
            ingData.setLayoutManager(new LinearLayoutManager(RecipeScreen.this,
                    LinearLayoutManager.VERTICAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeScreen.this, recipeDetails.extendedIngredients);
            ingData.setAdapter(ingredientsAdapter);

            fullScreenBtn.setOnClickListener(v -> onPressSizeBtn(recipeDetails));

            ingredient.setBackgroundResource(R.drawable.btns_recipe);
            ingredient.setTextColor(getColor(R.color.white));
            steps.setTextColor(getColor(R.color.black));
            steps.setBackground(null);
            nutrition.setTextColor(getColor(R.color.black));
            nutrition.setBackground(null);
            onPressStepsBtn(steps);
            onPressIngredientsBtn(ingredient);
            onPressNutritionBtn(nutrition);
            onClickFavoriteBtn(favoriteBtn, recipeDetails);
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(RecipeScreen.this, msg, Toast.LENGTH_SHORT).show();
        }
    };


    private void onPressBackBtn(ImageView imageView) {
        imageView.setOnClickListener(v -> finish());
    }

    private void onPressSizeBtn(RecipeDetails recipeDetails) {
        if (fullSizeImg) {
            imgRecipe.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.get().load(recipeDetails.image).into(imgRecipe);
            fullScreenBtn.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            shade.setVisibility(View.GONE);
            fullSizeImg = !fullSizeImg;
        } else {
            imgRecipe.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(recipeDetails.image).into(imgRecipe);
            fullScreenBtn.setColorFilter(null);
            shade.setVisibility(View.GONE);
            fullSizeImg = !fullSizeImg;
        }
    }

    private void onPressIngredientsBtn(Button button) {
        button.setOnClickListener(v -> {
            ingredient.setBackgroundResource(R.drawable.btns_recipe);
            ingredient.setTextColor(getColor(R.color.white));
            steps.setTextColor(getColor(R.color.black));
            steps.setBackground(null);
            nutrition.setTextColor(getColor(R.color.black));
            nutrition.setBackground(null);
            stepsData.setVisibility(View.GONE);
            ingData.setVisibility(View.VISIBLE);
        });
    }

    private void onPressStepsBtn(Button button) {
        button.setOnClickListener(v -> {
            steps.setBackgroundResource(R.drawable.btns_recipe);
            steps.setTextColor(getColor(R.color.white));
            ingredient.setTextColor(getColor(R.color.black));
            ingredient.setBackground(null);
            nutrition.setTextColor(getColor(R.color.black));
            nutrition.setBackground(null);
            stepsData.setVisibility(View.VISIBLE);
            ingData.setVisibility(View.GONE);
        });
    }

    private void onPressNutritionBtn(Button button) {
        button.setOnClickListener(v -> {
            final Dialog nutritionDialog = new Dialog(this, android.R.style.Theme_Light);
            nutritionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nutritionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
            nutritionDialog.setContentView(R.layout.nutrition_dialog);
            ImageView imageView = (ImageView) nutritionDialog.findViewById(R.id.imageView_nutrition);
            Picasso.get().load("https://api.spoonacular.com/recipes/" + id + "/nutritionLabel.png?apiKey="
                    + getString(R.string.api_key)).into(imageView);
            nutritionDialog.show();
        });
    }

    private void onClickFavoriteBtn(ImageView imageView, final RecipeDetails recipeDetails) {
        imageView.setOnClickListener(v -> {
            if (currentUser != null) {
                DatabaseReference favoritesRef = database.getReference(USERS_KEY)
                        .child(currentUser.getUid())
                        .child(FAVORITE_KEY)
                        .child(String.valueOf(recipeDetails.id));
                if (isFavorite) {
                    favoritesRef.removeValue();
                    isFavorite = false;
                    showMessage("Recipe removed from favorites");
                } else {
                    Map<String, Object> recipeData = new HashMap<>();
                    recipeData.put("id", recipeDetails.id);
                    recipeData.put("title", recipeDetails.title);
                    recipeData.put("image", recipeDetails.image);
                    recipeData.put("time", recipeDetails.readyInMinutes);

                    favoritesRef.setValue(recipeData)
                            .addOnSuccessListener(unused -> {
                                isFavorite = true;
                                updateFavoriteButtonAppearance();
                                showMessage("Recipe added to favorites");
                            }).addOnFailureListener(e ->
                                    showMessage("Failed to add recipe to favorites: " + e.getMessage()));
                }
            } else {
                showMessage("You need to be logged in to add favorites");
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkIfFavorite() {
        DatabaseReference favoritesRef = database.getReference(USERS_KEY)
                .child(currentUser.getUid())
                .child(FAVORITE_KEY)
                .child(String.valueOf(id));

        favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isFavorite = dataSnapshot.exists();
                updateFavoriteButtonAppearance();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RecipeScreen", "Error checking favorite status: " + databaseError.getMessage());
            }
        });
    }

    private void updateFavoriteButtonAppearance() {
        if (isFavorite) {
            favoriteBtn.setImageResource(R.drawable.favorite_select_icon);
        } else {
            favoriteBtn.setImageResource(R.drawable.favorite_unselect_icon);
        }
    }
}