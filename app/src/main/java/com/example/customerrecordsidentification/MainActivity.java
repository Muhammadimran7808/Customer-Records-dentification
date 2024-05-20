package com.example.customerrecordsidentification;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    Button logoutBtn, gotoSearch;
    TextView textView;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logoutBtn = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        gotoSearch = findViewById(R.id.goto_search);
        user = auth.getCurrentUser();
        imageView = findViewById(R.id.imageView);

        if (user == null) {
            // User is not logged in, redirect to login activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }  else {
            // User is logged in, retrieve and display user information
            String userEmail = user.getEmail();

            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                Map<String, Object> data = document.getData();
                                String name = String.valueOf(data.get("name"));
                                String email = String.valueOf(data.get("email"));
                                String phoneNumber = String.valueOf(data.get("phone"));
                                String imageUrl = String.valueOf(data.get("imageUrl"));

                                // Display the extracted data on the screen
                                String displayText = "Name: " + name + "\n"
                                        + "Email: " + email + "\n"
                                        + "Phone Number: " + phoneNumber;
                                textView.setText(displayText);

                                // Load the image into the ImageView using Glide
                                Glide.with(MainActivity.this)
                                        .load(imageUrl)
                                        .into(imageView);
                            } else {
                                Toast.makeText(MainActivity.this, "User information not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        gotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchCustomer.class);
                startActivity(intent);
                finish();
            }
        });
    }
}