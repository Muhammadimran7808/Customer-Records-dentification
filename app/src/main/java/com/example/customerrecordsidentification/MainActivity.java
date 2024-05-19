package com.example.customerrecordsidentification;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        if (user == null) {
            // User is not logged in, redirect to login activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // User is logged in, retrieve and display user information
            String userEmail = user.getEmail();

            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // No document found for the logged-in user
                                    Toast.makeText(MainActivity.this, "User information not found.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Retrieve and display user information
                                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                    Map<String, Object> data = document.getData();
                                    String name = String.valueOf(data.get("name"));
                                    String email = String.valueOf(data.get("email"));
                                    String phoneNumber = String.valueOf(data.get("phone"));

                                    // Display the extracted data on the screen
                                    String displayText = "Name: " + name + "\n"
                                            + "Email: " + email + "\n"
                                            + "Phone Number: " + phoneNumber;
                                    textView.setText(displayText);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Something went wrong while getting user information.", Toast.LENGTH_SHORT).show();
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