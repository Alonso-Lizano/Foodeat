package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.screens.components.RecipeScreen;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularRecipesAdapter extends RecyclerView.Adapter<PopularRecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private IRecipeClick recipeClick;

    public PopularRecipesAdapter(Context context, List<Recipe> recipeList, IRecipeClick recipeClick) {
        this.context = context;
        this.recipeList = recipeList;
        this.recipeClick = recipeClick;
    }

    @NonNull
    @Override
    public PopularRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.popular_recipes,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularRecipeViewHolder holder, int position) {
        holder.recipeName.setText(recipeList.get(position).title);
        holder.recipeName.setSelected(true);
        holder.cookTime.setText(String.format(context.getString(R.string.ready_in_minutes), recipeList.get(position).readyInMinutes));
        Picasso.get().load(recipeList.get(position).image).into(holder.popularFood);
        holder.itemView.setOnClickListener(v ->
                recipeClick.onRecipeClicked(String.valueOf(recipeList.get(
                        holder.getAdapterPosition()).id)));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}


class PopularRecipeViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView recipeName;
    public TextView cookTime;
    public ImageView popularFood;

    public PopularRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.popularCardRecipe);
        this.recipeName = itemView.findViewById(R.id.popularName);
        this.cookTime = itemView.findViewById(R.id.cookTime);
        this.popularFood = itemView.findViewById(R.id.popularImg);
    }
}