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
import com.ren.renshomemadefood.content.models.ExtendedIngredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {

    private Context context;
    private List<ExtendedIngredient> ingredientList;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ingredients,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.nameIngredient.setText(ingredientList.get(position).name);
        holder.nameIngredient.setSelected(true);
        holder.amountIngredient.setText(ingredientList.get(position).original);
        holder.amountIngredient.setSelected(true);
        Picasso.get().load("https://img.spoonacular.com/ingredients_100x100/" +
                ingredientList.get(position).image).into(holder.imgIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}

class IngredientsViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgIngredient;
    public TextView nameIngredient;
    public TextView amountIngredient;

    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        imgIngredient = itemView.findViewById(R.id.imgIngredient);
        nameIngredient = itemView.findViewById(R.id.nameIngredient);
        amountIngredient = itemView.findViewById(R.id.amountIngredient);
    }
}
