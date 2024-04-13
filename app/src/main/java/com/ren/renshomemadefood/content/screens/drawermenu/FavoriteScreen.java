package com.ren.renshomemadefood.content.screens.drawermenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import com.ren.renshomemadefood.content.adapters.FavoriteAdapter;
import com.ren.renshomemadefood.content.adapters.SearchRecipeAdapter;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.screens.components.CategoryScreen;
import com.ren.renshomemadefood.content.screens.components.RecipeScreen;
import com.ren.renshomemadefood.content.screens.components.SearchScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteScreen extends AppCompatActivity {
    private static final String FAVORITE_KEY = "Favorites";
    private static final String USERS_KEY = "Users";
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<RecipeDetails> recipeDetails;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_screen);

        recyclerView = findViewById(R.id.favorite_food_rv);
        recipeDetails = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        reference = database.getReference(USERS_KEY)
                .child(currentUser.getUid())
                .child(FAVORITE_KEY);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeDetails.clear();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    RecipeDetails details = new RecipeDetails();
                    details.id = Integer.parseInt(recipeSnapshot.getKey());
                    details.title = recipeSnapshot.child("title").getValue(String.class);
                    details.image = recipeSnapshot.child("image").getValue(String.class);
                    details.readyInMinutes = recipeSnapshot.child("time").getValue(Integer.class);

                    recipeDetails.add(details);
                }
                favoriteAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FavoriteScreen", "Error reading data from Firebase: " + databaseError.getMessage());
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(this, recipeDetails, database, currentUser, recipeClick);
        recyclerView.setAdapter(favoriteAdapter);

        setupOnBackPressed();
    }

    private final IRecipeClick recipeClick = id -> {
        Intent intent = new Intent(this, RecipeScreen.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };

    private void setupOnBackPressed() {
        final OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }
}
