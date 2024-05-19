package com.example.customerrecordsidentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class SearchCustomer extends AppCompatActivity {

    EditText editTextSearch;
    Button searchBtn;
    TextView textView;
    ProgressBar progressBar;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        editTextSearch = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        textView = findViewById(R.id.searchResult);
        progressBar = findViewById(R.id.progressBar);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("");
                String searchNumber;
                searchNumber = String.valueOf(editTextSearch.getText());
                if (TextUtils.isEmpty(searchNumber)) {
                    Toast.makeText(SearchCustomer.this, "Enter Number", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("users")
                            .whereEqualTo("phone", searchNumber)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            // No document found for the logged-in user
                                            Toast.makeText(SearchCustomer.this, "No User found.", Toast.LENGTH_SHORT).show();
                                            textView.setText("No User found");
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
                                        Toast.makeText(SearchCustomer.this, "Something went wrong while getting user information.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}