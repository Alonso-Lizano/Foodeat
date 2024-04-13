package com.ren.renshomemadefood.content.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.listeners.IRecipeClick;
import com.ren.renshomemadefood.content.models.Recipe;
import com.ren.renshomemadefood.content.models.RecipeDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private static final String FAVORITE_KEY = "Favorites";
    private static final String USERS_KEY = "Users";
    private Context context;
    private List<RecipeDetails> recipes;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private IRecipeClick recipeClick;

    public FavoriteAdapter(Context context, List<RecipeDetails> recipes, FirebaseDatabase database,
                           FirebaseUser currentUser, IRecipeClick recipeClick) {
        this.context = context;
        this.recipes = recipes;
        this.database = database;
        this.currentUser = currentUser;
        this.recipeClick = recipeClick;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.favorite_rv,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        RecipeDetails recipe = recipes.get(position);

        holder.getTitle().setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.getImage());
        holder.getTime().setText(String.format(context.getString(R.string.ready_in_minutes), recipe.readyInMinutes));
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove from favorites")
                    .setMessage("Are you sure you want to remove this recipe from your favorites?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        DatabaseReference recipeRef = database.getReference(USERS_KEY)
                                .child(currentUser.getUid())
                                .child(FAVORITE_KEY)
                                .child(String.valueOf(recipe.id));
                        recipeRef.removeValue();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        });

        holder.getForward().setOnClickListener(v -> recipeClick.onRecipeClicked(String.
                valueOf(recipes.get(holder.getAdapterPosition()).id)));
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
}

class FavoriteViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView title;
    private TextView time;
    private ImageView forward;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.image = itemView.findViewById(R.id.image);
        this.title = itemView.findViewById(R.id.title);
        this.time = itemView.findViewById(R.id.time);
        this.forward = itemView.findViewById(R.id.forward);
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTime() {
        return time;
    }

    public ImageView getForward() {
        return forward;
    }
}
