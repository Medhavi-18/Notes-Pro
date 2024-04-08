package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.ConditionVariable;
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
import com.google.firebase.ktx.Firebase;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);


        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> finish());

        }


        void createAccount() {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();


            //calling validateData function from createAccount
            boolean isValidated = validateData(email, password, confirmPassword);

            //data is not validated
            if(!isValidated){
                return;
            }

            //data is validated so, addit in firebase
            createAccountInFirebase(email,password);

        }


        //FOR DISPLAYING PROGRESSBAR
        void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
            }
        }

        void createAccountInFirebase(String email,String password){
            changeInProgress(true);

            //TO CREATE ACCOUNT IN FIREBASE
            FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    changeInProgress(false);
                    if(task.isSuccessful()){
                        //creating account is done
                       // Toast.makeText(CreateAccountActivity.this, "Successfully Created Account", Toast.LENGTH_SHORT).show();
                        Utility.showToast(CreateAccountActivity.this, "Successfully Created Account");

                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    }
                    else{
                        //fail to create an account
                        Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());
                        //Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    boolean validateData (String email, String password, String confirmPassword) {
        //validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is Invalid");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password length is invalid");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password not match");
            return false;
        }

        return true;

    }

    }