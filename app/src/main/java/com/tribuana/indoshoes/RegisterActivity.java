package com.tribuana.indoshoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText inputEmail = findViewById(R.id.emailReg);
        EditText inputUsername = findViewById(R.id.usernameReg);
        EditText inputPassword = findViewById(R.id.passwordReg);
        EditText inputConfirmPassword = findViewById(R.id.confirmPassReg);
        Button buttonRegister = findViewById(R.id.btnRegister);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=inputPassword.getRight()-inputPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=inputPassword.getSelectionEnd();
                        if (passwordVisible){
                            //set image drawable
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_passwordhide,0);
                            //for hide password
                            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;

                        }else{
                            //set image drawable
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_password,0);
                            //for show password
                            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        inputPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        inputConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=inputConfirmPassword.getRight()-inputConfirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=inputPassword.getSelectionEnd();
                        if (passwordVisible){
                            //set image drawable
                            inputConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_passwordhide,0);
                            //for hide password
                            inputConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;

                        }else{
                            //set image drawable
                            inputConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_password,0);
                            //for show password
                            inputConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        inputConfirmPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputEmail.getText().length()>0 && inputUsername.getText().length()>0 && inputPassword.getText().length()>0 && inputConfirmPassword.getText().length()>0){
                    if (inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                        register(inputEmail.getText().toString(), inputUsername.getText().toString(), inputPassword.getText().toString());
                        Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Enter the same password!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please complete all data first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String emailReg, String usernameReg, String passwordReg) {
        mAuth.createUserWithEmailAndPassword(emailReg, passwordReg). addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(usernameReg)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();

                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Register Failed!", Toast.LENGTH_SHORT). show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT). show();
                }
            }
        });

    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}