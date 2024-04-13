package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.models.Post;
import com.ren.renshomemadefood.content.screens.components.PostDetailScreen;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapterViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.row_post_item,
                parent, false), context, postList);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterViewHolder holder, int position) {
        holder.title.setText(postList.get(position).getTitle());
        Picasso.get().load(postList.get(position).getImage()).into(holder.postImage);
        String userImg = postList.get(position).getUserImg();

        if (userImg != null) {
            /*StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("User Images/" + userImg);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Picasso.get().load(R.drawable.user_img).into(holder.userImage);
                }
            });*/
            Picasso.get().load(userImg).into(holder.userImage);
        } else {
            Picasso.get().load(R.drawable.user_img).into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Context getContext() {
        return context;
    }

}

class PostAdapterViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private List<Post> postList;
    public TextView title;
    public ImageView postImage;
    public ImageView userImage;
    public ImageButton optionsMenu;

    public PostAdapterViewHolder(@NonNull View itemView, Context context, List<Post> postList) {
        super(itemView);
        this.context = context;
        this.postList = postList;
        this.title = itemView.findViewById(R.id.titlePost);
        this.postImage = itemView.findViewById(R.id.imgPostCommunity);
        this.userImage = itemView.findViewById(R.id.userImage);
        this.optionsMenu = itemView.findViewById(R.id.optionMenu);

        onClickView(itemView);
        onClickOptionMenu();
    }

    private void onClickView(View itemView) {
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailScreen.class);
            int pos = getAdapterPosition();

            intent.putExtra("title", postList.get(pos).getTitle());
            intent.putExtra("postImg", postList.get(pos).getImage());
            intent.putExtra("description", postList.get(pos).getDescription());
            intent.putExtra("id", postList.get(pos).getId());
            intent.putExtra("userImg", postList.get(pos).getUserImg());
            //intent.putExtra("userName", postList.get(pos).getUserName());
            long timestamp = (long) postList.get(pos).getTimeStamp();
            intent.putExtra("postDate", timestamp);
            context.startActivity(intent);
        });
    }

    private void onClickOptionMenu() {
        this.optionsMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, optionsMenu);
            popupMenu.getMenuInflater().inflate(R.menu.report_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {

                if (item.getItemId() == R.id.report_option) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String postId = postList.get(position).getId();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        reportPost(postId, userId);
                    }
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    private void reportPost(String postId, String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report post");

        final String[] reportOptions = {"Spam", "Nudes", "Hate speech", "Violence", "Irrelevant content"};

        builder.setItems(reportOptions, (dialog, which) -> {
            String reportReason = reportOptions[which];

            sendReportToServer(postId, userId, reportReason);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void sendReportToServer(String postId, String userId, String reportReason) {
        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("Report").child(postId);

        reportRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    reportRef.child(userId).child("Reason").setValue(reportReason);

                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
                    postRef.child("Report Count").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer reportCount = mutableData.getValue(Integer.class);
                            if (reportCount == null) {
                                mutableData.setValue(1);
                            } else {
                                mutableData.setValue(reportCount + 1);

                                if (reportCount + 1 >= 5) {
                                    deletePost(postId);
                                }
                            }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                            if (error != null) {
                                Log.e("ReportPost", "Error incrementing report counter", error.toException());
                            } else {
                                Toast.makeText(context, "The post has been reported", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "You have already reported this post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ReportPost", "Error when verifying if the user has reported the post", error.toException());
            }
        });
    }

    private void deletePost(String postId) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        postRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "The post has been deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("DeletePost", "Error deleting post", e);
                });
    }

}
