package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
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
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.ren.renshomemadefood.content.models.Root;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context context;
    private List<Recipe> recipeDetailsList;
    private IRecipeClick recipeClick;

    public CategoryAdapter(Context context, List<Recipe> recipeList, IRecipeClick recipeClick) {
        this.context = context;
        this.recipeDetailsList = recipeList;
        this.recipeClick = recipeClick;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_rv,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.title.setText(recipeDetailsList.get(position).title);
        holder.title.setSelected(true);
        Picasso.get().load(recipeDetailsList.get(position).image).into(holder.image);
        holder.forward.setOnClickListener(v ->
                recipeClick.onRecipeClicked(String.valueOf(recipeDetailsList.get(holder
                        .getAdapterPosition()).id)));
    }

    @Override
    public int getItemCount() {
        return recipeDetailsList.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView image;
    public ImageView forward;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.title);
        this.image = itemView.findViewById(R.id.image);
        this.forward = itemView.findViewById(R.id.forward);
    }
}