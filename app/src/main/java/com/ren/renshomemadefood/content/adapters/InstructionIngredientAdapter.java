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
import com.ren.renshomemadefood.content.models.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionIngredientAdapter extends RecyclerView.Adapter<InstructionIngredientViewHolder> {
    private Context context;
    private List<Ingredient> ingredientList;

    public InstructionIngredientAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public InstructionIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ingredients_items,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientViewHolder holder, int position) {
        holder.nameItem.setText(ingredientList.get(position).name);
        holder.nameItem.setSelected(true);
        Picasso.get().load("https://img.spoonacular.com/ingredients_100x100/" + ingredientList
                .get(position).image).into(holder.ingItem);

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}

class InstructionIngredientViewHolder extends RecyclerView.ViewHolder {

    public ImageView ingItem;
    public TextView nameItem;

    public InstructionIngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        this.ingItem = itemView.findViewById(R.id.ingItem);
        this.nameItem = itemView.findViewById(R.id.nameItem);
    }
}