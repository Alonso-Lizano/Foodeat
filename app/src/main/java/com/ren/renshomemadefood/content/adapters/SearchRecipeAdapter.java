package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.screens.components.RecipeScreen;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private IRecipeClick recipeClick;
    public SearchRecipeAdapter(Context context, List<Recipe> recipeList, IRecipeClick recipeClick) {
        this.context = context;
        this.recipeList = recipeList;
        this.recipeClick = recipeClick;
    }

    @NonNull
    @Override
    public SearchRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.search_rv,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecipeViewHolder holder, int position) {
        holder.searchName.setText(capitalizeWords(recipeList.get(position).title));
        holder.searchName.setSelected(true);
        Picasso.get().load(recipeList.get(position).image).into(holder.imgSearch);
        holder.itemView.setOnClickListener(v ->
                recipeClick.onRecipeClicked(String.valueOf(recipeList.get(holder
                .getAdapterPosition()).id)));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    private String capitalizeWords(String text) {
        StringBuilder result = new StringBuilder(text.length());
        String[] words = text.split("\\s");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1).toLowerCase());
                result.append(" ");
            }
        }
        return result.toString().trim();
    }
}

class SearchRecipeViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgSearch;
    public TextView searchName;

    public SearchRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imgSearch = itemView.findViewById(R.id.imgSearch);
        this.searchName = itemView.findViewById(R.id.searchName);
    }
}