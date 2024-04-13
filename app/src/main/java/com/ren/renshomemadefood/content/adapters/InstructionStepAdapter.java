package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.models.Step;

import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewHolder> {
    private Context context;
    private List<Step> stepList;

    public InstructionStepAdapter(Context context, List<Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public InstructionStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewHolder(LayoutInflater.from(context).inflate(
                R.layout.list_ingredients_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewHolder holder, int position) {
        holder.textStep.setText(String.valueOf(stepList.get(position).number));
        holder.stepTitle.setText(stepList.get(position).step);

        holder.rvIngredients.setHasFixedSize(true);
        holder.rvIngredients.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        InstructionIngredientAdapter ingredientAdapter = new InstructionIngredientAdapter(context,
                stepList.get(position).ingredients);
        holder.rvIngredients.setAdapter(ingredientAdapter);

        holder.rvEquipments.setHasFixedSize(true);
        holder.rvEquipments.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        InstructionEquipmentsAdapter equipmentsAdapter = new InstructionEquipmentsAdapter(context,
                stepList.get(position).equipment);
        holder.rvEquipments.setAdapter(equipmentsAdapter);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }
}

class InstructionStepViewHolder extends RecyclerView.ViewHolder {
    public TextView textStep;
    public TextView stepTitle;
    public RecyclerView rvEquipments;
    public RecyclerView rvIngredients;

    public InstructionStepViewHolder(@NonNull View itemView) {
        super(itemView);
        this.textStep = itemView.findViewById(R.id.textStep);
        this.stepTitle = itemView.findViewById(R.id.stepTitle);
        this.rvEquipments = itemView.findViewById(R.id.rvEquipments);
        this.rvIngredients = itemView.findViewById(R.id.rvIngredients);
    }

}
