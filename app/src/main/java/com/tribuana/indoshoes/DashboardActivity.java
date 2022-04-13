package com.tribuana.indoshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class DashboardActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private TextView textName;
    private Button logout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        textName = findViewById(R.id.name);
        logout = findViewById(R.id.btnLogout);

        logout.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser.getDisplayName()!=null){
            textName.setText(firebaseUser.getDisplayName());
        } else {
            textName.setText("Login Gagal!");
        }


    }
}