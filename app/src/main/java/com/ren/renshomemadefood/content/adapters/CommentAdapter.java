package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapterViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.row_comment,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapterViewHolder holder, int position) {
        holder.getCommentUsername().setText(comments.get(position).getUserName());
        holder.getCommentContent().setText(comments.get(position).getComment());
        holder.getCommentDate().setText(getTimePost((Long) comments.get(position).getTimestamp()));
        String userImg = comments.get(position).getUserImg();
        if (userImg != null) {
            Picasso.get().load(userImg).into(holder.getCommentUserImg());
        } else {
            Picasso.get().load(R.drawable.user_img).into(holder.getCommentUserImg());
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private String getTimePost(long time) {
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.setTimeInMillis(time);
        return DateFormat.format("hh:mm", calendar).toString();
    }
}

class CommentAdapterViewHolder extends RecyclerView.ViewHolder {

    private ImageView commentUserImg;
    private TextView commentUsername;
    private TextView commentContent;
    private TextView commentDate;
    private ImageView likeButton;
    private TextView likeCount;

    public CommentAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        this.commentUserImg = itemView.findViewById(R.id.comment_user_img);
        this.commentUsername = itemView.findViewById(R.id.comment_username);
        this.commentContent = itemView.findViewById(R.id.comment_content);
        this.commentDate = itemView.findViewById(R.id.comment_date);
        this.likeButton = itemView.findViewById(R.id.comment_like_button);
        this.likeCount = itemView.findViewById(R.id.comment_like_count);
    }

    public ImageView getCommentUserImg() {
        return commentUserImg;
    }

    public TextView getCommentUsername() {
        return commentUsername;
    }

    public TextView getCommentContent() {
        return commentContent;
    }

    public TextView getCommentDate() {
        return commentDate;
    }

    public ImageView getLikeButton() {
        return likeButton;
    }

    public TextView getLikeCount() {
        return likeCount;
    }

    private void onClickLikeBtn() {
        this.likeButton.setOnClickListener(v -> {
            int currentLikes = Integer.parseInt(likeCount.getText().toString());
            likeCount.setText(String.valueOf(currentLikes + 1));
        });
    }
}
