package com.ren.renshomemadefood.content.screens.components;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.adapters.CommentAdapter;
import com.ren.renshomemadefood.content.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailScreen extends AppCompatActivity {

    private static final String COMMENT_KEY = "Comment";
    private ImageView imgPost;
    private ImageView userImage;
    private ImageView currentUserImage;
    private TextView textPostDesc;
    private TextView textPostDateName;
    private TextView textPostTitle;
    private EditText textComment;
    private Button addCommentBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String id;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail_screen);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        imgPost = findViewById(R.id.post_detail_img);
        userImage = findViewById(R.id.post_detail_user_img);
        currentUserImage = findViewById(R.id.post_detail_currentuser_img);
        textPostDesc = findViewById(R.id.post_detail_desc);
        textPostDateName = findViewById(R.id.post_detail_date_name);
        textPostTitle = findViewById(R.id.post_detail_title);
        textComment = findViewById(R.id.post_detail_comment);
        addCommentBtn = findViewById(R.id.post_detail_add_comment_btn);
        recyclerView = findViewById(R.id.rv_comment);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        addCommentBtn.setOnClickListener(v -> {
            addCommentBtn.setVisibility(View.INVISIBLE);
            DatabaseReference reference = database.getReference(COMMENT_KEY).child(id).push();
            String commentContent = textComment.getText().toString();
            String userId = currentUser.getUid();
            String userName = currentUser.getDisplayName();
            String userImg;
            if (currentUser.getPhotoUrl() != null) {
                userImg = currentUser.getPhotoUrl().toString();
            } else {
                userImg = "android.resource://"+ getPackageName() + "/drawable/user_img";
            }

            Comment comment = new Comment(commentContent, userId, userImg, userName);

            reference.setValue(comment).addOnSuccessListener(unused -> {
                showMessage("Comment added");
                textComment.setText("");
                addCommentBtn.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> showMessage("Fail to add comment: " + e.getMessage()));
        });

        String postImg = getIntent().getStringExtra("postImg");
        Picasso.get().load(postImg).into(imgPost);
        String title = getIntent().getStringExtra("title");
        textPostTitle.setText(title);
        String description = getIntent().getStringExtra("description");
        textPostDesc.setText(description);
        String userImg = getIntent().getStringExtra("userImg");
        if (userImg != null) {
            Picasso.get().load(userImg).into(userImage);
        } else {
            Picasso.get().load(R.drawable.user_img).into(userImage);
        }

        if (currentUser.getPhotoUrl() != null) {
            Picasso.get().load(currentUser.getPhotoUrl()).into(currentUserImage);
        } else {
            Picasso.get().load(R.drawable.user_img).into(currentUserImage);
        }

        id = getIntent().getStringExtra("id");

        String postDate = getTimePost(getIntent().getLongExtra("postDate", 0L));
        textPostDateName.setText(postDate);

        initCommentRecyclerView();

    }

    private void initCommentRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        DatabaseReference commentRef = database.getReference(COMMENT_KEY).child(id);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    commentList.add(comment);

                }
                commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
                recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getTimePost(long time) {
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

}