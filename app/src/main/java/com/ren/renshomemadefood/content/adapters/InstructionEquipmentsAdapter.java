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
import com.ren.renshomemadefood.content.models.Equipment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionEquipmentsAdapter extends RecyclerView.Adapter<InstructionEquipmentsViewHolder> {

    private Context context;
    private List<Equipment> equipmentList;

    public InstructionEquipmentsAdapter(Context context, List<Equipment> equipmentList) {
        this.context = context;
        this.equipmentList = equipmentList;
    }

    @NonNull
    @Override
    public InstructionEquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionEquipmentsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ingredients_items,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionEquipmentsViewHolder holder, int position) {
        holder.nameItem.setText(equipmentList.get(position).name);
        holder.nameItem.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/equipment_100x100/" + equipmentList
                .get(position).image).into(holder.ingItem);
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }
}

class InstructionEquipmentsViewHolder extends RecyclerView.ViewHolder {
    public ImageView ingItem;
    public TextView nameItem;

    public InstructionEquipmentsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.ingItem = itemView.findViewById(R.id.ingItem);
        this.nameItem = itemView.findViewById(R.id.nameItem);
    }
}
