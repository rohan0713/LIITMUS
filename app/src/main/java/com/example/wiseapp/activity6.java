package com.example.wiseapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class activity6  extends BaseActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private TextInputLayout mStatusTextView;
    private TextInputLayout mDetailTextView;
     private static Button btn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity6);

        // Views
        mStatusTextView = findViewById(R.id.emailADD);
        mDetailTextView = findViewById(R.id.password);
        btn = findViewById(R.id.login);


            findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity6.this, activity3.class);
                    startActivity(intent);
                    finish();
                }
            });
            findViewById(R.id.forgot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity6.this , updation.class);
                    startActivity(intent);
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!validateEmail() | !validatePassword()) {
                        return;
                    } else {
                        isUser();
                    }
                }
            });

    }

        private void isUser () {

          final String username = mStatusTextView.getEditText().getText().toString().trim();
          final String password = mDetailTextView.getEditText().getText().toString().trim();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query check = reference.orderByChild("username").equalTo(username);

            check.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        mStatusTextView.setError(null);
                        mStatusTextView.setErrorEnabled(false);
                        String passwordfromDB = dataSnapshot.child(username).child("password").getValue(String.class);

                        if (passwordfromDB.equalsIgnoreCase(password)) {


                            mDetailTextView.setError(null);
                            mDetailTextView.setErrorEnabled(false);
                            Intent i = new Intent(activity6.this , menulayout.class);
                            i.putExtra("confirm" , false);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);


                        } else {

                            mDetailTextView.setError("wrong password");
                            mDetailTextView.requestFocus();
                        }
                    } else {

                        mStatusTextView.setError("no such user exists");
                        mStatusTextView.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private boolean validatePassword () {
            String passwordInput = mDetailTextView.getEditText().getText().toString().trim();

            if (passwordInput.isEmpty()) {
                mDetailTextView.setError("Field can't be empty");
                Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

                return false;
            } else {
                mDetailTextView.setError(null);
                mDetailTextView.setErrorEnabled(false);

                return true;
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private boolean validateEmail () {
            String emailInput = mStatusTextView.getEditText().getText().toString().trim();

            if (emailInput.isEmpty()) {
                mStatusTextView.setError("Field can't be empty");
                Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

                return false;
            } else {
                mStatusTextView.setError(null);
                mStatusTextView.setErrorEnabled(false);
                return true;
            }
        }



}