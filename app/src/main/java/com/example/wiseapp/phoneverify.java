package com.example.wiseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class phoneverify extends AppCompatActivity {

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private  String verificationCodeBySystem;
    private TextInputLayout verify;
    private  Button btn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverify);
        mAuth = FirebaseAuth.getInstance();
        verify = findViewById(R.id.code);
        btn = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        String phoneNUM = getIntent().getStringExtra("phone");

        sendVerificationCodeToUser(phoneNUM);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = verify.getEditText().getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    verify.getEditText().setError("Wrong OTP...");
                    verify.getEditText().requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);


            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNUM) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNUM,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    //Get the code in global variable
                    verificationCodeBySystem = s;
                }
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(phoneverify.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            };

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            rootnode = FirebaseDatabase.getInstance();
                            reference = rootnode.getReference("users");
                            Toast.makeText(phoneverify.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();
                            String username = getIntent().getStringExtra("username");
                            String pass = getIntent().getStringExtra("password");
                            String email = getIntent().getStringExtra("email");
                            String phoneNUM = getIntent().getStringExtra("phone");


                            UserhelperClass u = new UserhelperClass(username , email , pass , phoneNUM);

                            reference.child(email).setValue(u);
                            //Perform Your required action here to either let the user sign In or do something required
//                            Intent intent = new Intent(phoneverify.this, menulayout.class);
//                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(phoneverify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
