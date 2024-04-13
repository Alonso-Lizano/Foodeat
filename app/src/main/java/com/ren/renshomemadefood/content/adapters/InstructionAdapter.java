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
import com.ren.renshomemadefood.content.models.InstructionsResponse;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionViewHolder> {

    private Context context;
    private List<InstructionsResponse> list;

    public InstructionAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.textInstruction.setText(list.get(position).name);
        holder.rvSteps.setHasFixedSize(true);
        holder.rvSteps.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter stepAdapter = new InstructionStepAdapter(context,
                list.get(position).steps);
        holder.rvSteps.setAdapter(stepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionViewHolder extends RecyclerView.ViewHolder {

    public TextView textInstruction;
    public RecyclerView rvSteps;

    public InstructionViewHolder(@NonNull View itemView) {
        super(itemView);
        textInstruction = itemView.findViewById(R.id.textInstruction);
        rvSteps = itemView.findViewById(R.id.rvSteps);
    }
}
