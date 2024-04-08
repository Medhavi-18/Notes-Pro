package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.createaccount_text_view_btn);


        //setting on click listener
        loginBtn.setOnClickListener((v) -> loginUser() );
        //switching page from login to createAccount page
        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }


    //creating function
    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();



        //calling validateData function from createAccount
        boolean isValidated = validateData(email, password);

        //data is not validated
        if(!isValidated){
            return;
        }
        //calling function
        loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);


                if(task.isSuccessful()){
                    //login successfully
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainActivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                    else{
                        Utility.showToast(LoginActivity.this,"Email not verified Please verify your email");
                       // Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //fail to login
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }

            }
        });
    }


    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData (String email, String password) {
        //validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is Invalid");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;

    }

}