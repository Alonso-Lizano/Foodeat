package com.ren.renshomemadefood.content.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.listeners.IRecipeResponse;
import com.ren.renshomemadefood.content.models.RandomRecipes;
import com.ren.renshomemadefood.content.screens.components.CategoryScreen;
import com.ren.renshomemadefood.content.screens.components.RecipeScreen;
import com.ren.renshomemadefood.content.screens.drawermenu.CommunityScreen;
import com.ren.renshomemadefood.content.screens.drawermenu.FavoriteScreen;
import com.ren.renshomemadefood.content.screens.drawermenu.LoginScreen;
import com.ren.renshomemadefood.content.screens.drawermenu.ProfileScreen;
import com.ren.renshomemadefood.content.screens.components.SearchScreen;
import com.ren.renshomemadefood.content.adapters.PopularRecipesAdapter;
import com.ren.renshomemadefood.content.screens.drawermenu.SettingsScreen;
import com.ren.renshomemadefood.content.util.RequestManager;
import com.squareup.picasso.Picasso;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RequestManager manager;
    private PopularRecipesAdapter recipesAdapter;
    private RecyclerView recyclerView;
    private EditText searchViewFrame;
    private ImageButton mainCourse;
    private ImageButton sideDishBtn;
    private ImageButton dessert;
    private ImageButton appetizer;
    private ImageButton salad;
    private ImageButton bread;
    private ImageButton breakfast;
    private ImageButton soup;
    private ImageButton beverage;
    private ImageButton sauce;
    private ImageButton marinade;
    private ImageButton fingerfood;
    private ImageButton snack;
    private ImageButton drink;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private float userRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        searchViewFrame = findViewById(R.id.searchViewFrame);
        recyclerView = findViewById(R.id.rv);
        mainCourse = findViewById(R.id.mainCourse);
        sideDishBtn = findViewById(R.id.sideDishBtn);
        dessert = findViewById(R.id.dessert);
        appetizer = findViewById(R.id.appetizer);
        salad = findViewById(R.id.salad);
        bread = findViewById(R.id.bread);
        breakfast = findViewById(R.id.breakfast);
        soup = findViewById(R.id.soup);
        beverage = findViewById(R.id.beverage);
        sauce = findViewById(R.id.sauce);
        marinade = findViewById(R.id.marinade);
        fingerfood = findViewById(R.id.fingerfood);
        snack = findViewById(R.id.snack);
        drink = findViewById(R.id.drink);

        //toolbar
        setSupportActionBar(toolbar);

        //init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            System.out.println("Actionbar is null");
        }

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //Setup back button
        setupOnBackPressed();

        searchViewFrame.setOnClickListener(v -> {
            Intent searchIntent = new Intent(MainScreen.this, SearchScreen.class);
            startActivity(searchIntent);
        });

        manager = new RequestManager(this);
        manager.getRandomRecipes(recipeResponse);

        mainCourse.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Main Course");
            startActivity(intent);
        });
        sideDishBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Side Dish");
            startActivity(intent);
        });
        dessert.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Dessert");
            startActivity(intent);
        });
        appetizer.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Appetizer");
            startActivity(intent);
        });
        salad.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Salad");
            startActivity(intent);
        });
        bread.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Bread");
            startActivity(intent);
        });
        breakfast.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Breakfast");
            startActivity(intent);
        });
        soup.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Soup");
            startActivity(intent);
        });
        beverage.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Beverage");
            startActivity(intent);
        });
        sauce.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Sauce");
            startActivity(intent);
        });
        marinade.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Marinade");
            startActivity(intent);
        });
        fingerfood.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Fingerfood");
            startActivity(intent);
        });
        snack.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Snack");
            startActivity(intent);
        });
        drink.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, CategoryScreen.class);
            intent.putExtra("categoryName", "Drink");
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawerNavigation();
        checkIfEmailVerified(currentUser);
    }

    private void checkIfEmailVerified(FirebaseUser currentUser) {
        if (currentUser != null && !currentUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email " +
                "verification next time.");

        builder.setPositiveButton("Continue", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private final IRecipeResponse recipeResponse = new IRecipeResponse() {
        @Override
        public void didFetch(RandomRecipes recipes, String msg) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainScreen.this, LinearLayoutManager.HORIZONTAL, false));
            recipesAdapter = new PopularRecipesAdapter(MainScreen.this, recipes.recipes, recipeClick);
            recyclerView.setAdapter(recipesAdapter);
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(MainScreen.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

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
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    dispatcher.onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.navHome) {

        } else if (menuItem.getItemId() == R.id.navProfile) {
            Intent intent = new Intent(this, ProfileScreen.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.navCommunity) {
            Intent intent = new Intent(this, CommunityScreen.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.navFavorite) {
            Intent intent = new Intent(this, FavoriteScreen.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.navSettings) {
            Intent intent = new Intent(this, SettingsScreen.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.navLogin) {
            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.navLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (menuItem.getItemId() == R.id.navRate) {
            onPressRateBtn();
        } else if (menuItem.getItemId() == R.id.navShare) {
            onPressShareBtn();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateDrawerNavigation() {
        View header = navigationView.getHeaderView(0);
        ImageView userImg = header.findViewById(R.id.userImg);
        TextView userName = header.findViewById(R.id.userName);
        TextView userEmail = header.findViewById(R.id.userEmail);

        if (currentUser != null) {
            userName.setText(currentUser.getDisplayName());
            userEmail.setText(currentUser.getEmail());

            if (currentUser.getPhotoUrl() != null) {
                Picasso.get().load(currentUser.getPhotoUrl()).into(userImg);
            } else {
                Picasso.get().load(R.drawable.user_img).into(userImg);
            }

            navigationView.getMenu().findItem(R.id.navLogin).setVisible(false);
            navigationView.getMenu().findItem(R.id.navLogout).setVisible(true);
        } else {
            userName.setText("Guest");
            userEmail.setText("guest@example.com");
            userImg.setImageResource(R.drawable.user_img);

            navigationView.getMenu().findItem(R.id.navLogin).setVisible(true);
            navigationView.getMenu().findItem(R.id.navLogout).setVisible(false);

            navigationView.getMenu().findItem(R.id.navProfile).setVisible(false);
            navigationView.getMenu().findItem(R.id.navCommunity).setVisible(false);
            navigationView.getMenu().findItem(R.id.navFavorite).setVisible(false);
            navigationView.getMenu().findItem(R.id.navSettings).setVisible(false);
            navigationView.getMenu().findItem(R.id.navRate).setVisible(false);
            navigationView.getMenu().findItem(R.id.navShare).setVisible(false);

        }
    }

    private void onPressRateBtn() {
        final Dialog rateDialog = new Dialog(this, android.R.style.Theme_Light);
        rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        rateDialog.setContentView(R.layout.rate_dialog);
        Button rateNowBtn = rateDialog.findViewById(R.id.rateNowBtn);
        Button laterBtn = rateDialog.findViewById(R.id.laterBtn);
        RatingBar ratingBar = rateDialog.findViewById(R.id.ratingBar);
        ImageView imageRate = rateDialog.findViewById(R.id.imgRate);

        rateNowBtn.setOnClickListener(v -> {
            Toast.makeText(MainScreen.this, "Thank you", Toast.LENGTH_SHORT).show();
            rateDialog.dismiss();
        });
        laterBtn.setOnClickListener(v -> rateDialog.dismiss());

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (rating <= 1) {
                imageRate.setImageResource(R.drawable.user_img);
            } else if (rating <= 2) {
                imageRate.setImageResource(R.drawable.arrow_down_icon);
            } else if (rating <= 3) {
                imageRate.setImageResource(R.drawable.user_img);
            } else if (rating <= 4) {
                imageRate.setImageResource(R.drawable.arrow_down_icon);
            } else if (rating <= 5) {
                imageRate.setImageResource(R.drawable.user_img);
            }
            animateImage(imageRate);
            userRate = rating;
        });
        rateDialog.show();
    }

    private void animateImage(ImageView imageView) {
        ScaleAnimation animation = new ScaleAnimation(0, 1F, 0, 1F, Animation.RELATIVE_TO_SELF,
                0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        animation.setFillAfter(true);
        animation.setDuration(200);
        imageView.startAnimation(animation);
    }

    private void onPressShareBtn() {
        String url = "www.google.es";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent, "Share app"));
    }
}