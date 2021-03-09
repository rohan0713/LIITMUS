package com.example.wiseapp;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class activity3 extends BaseActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}");               //at least 4 characters


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout mobilenum;
    Button button2;
    SignInButton sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);

        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        mobilenum = findViewById(R.id.number);
//        sign = findViewById(R.id.signInButton);
//                sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                signIn();
//            }
//        });

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//        // [END config_signin]
//
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//        showProgressBar();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent intent = new Intent(activity6.this , activity7.class);
//                            startActivity(intent);


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                       hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private void updateUI(FirebaseUser user) {
//        hideProgressBar();
        if (user != null) {
            textInputEmail.getEditText().setText(String.valueOf(user.getEmail()));
            textInputUsername.getEditText().setText(String.valueOf(user.getPhoneNumber()));
        } else {
            textInputEmail.getEditText().setText(null);
            textInputUsername.getEditText().setText(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password must contain at least 4 character and 1 special character");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void confirmInput (View v){
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validatenumber()) {
         return;
        }

        String phonenum = mobilenum.getEditText().getText().toString().trim();
        String user = textInputEmail.getEditText().getText().toString().trim();
        String pass = textInputPassword.getEditText().getText().toString().trim();
        String email = textInputUsername.getEditText().getText().toString().trim();
        Intent i = new Intent(activity3.this , phoneverify.class);
        i.putExtra("phone" , phonenum);
        i.putExtra("username" , user);
        i.putExtra("password" , pass);
        i.putExtra("email" , email);
        startActivity(i);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validatenumber() {

        String num = mobilenum.getEditText().getText().toString().trim();
        if (num.isEmpty() || num.length() < 10) {
            mobilenum.setError("Enter a valid number");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            return false;
        } else {
            mobilenum.setError(null);
            return true;

        }
    }


}

