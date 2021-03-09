package com.example.wiseapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class updation extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}");               //at least 4 characters
    private static TextInputLayout user , newpass , old;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updation);

        user = findViewById(R.id.text_input_email);
        newpass = findViewById(R.id.text_input_username);
        old = findViewById(R.id.text_input_password);
        btn = findViewById(R.id.confirm);

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (!validateuser() | !validatenew() | !validateold()) {
                    return;
                } else if (newpass.getEditText().getText().toString().equalsIgnoreCase(old.getEditText().getText().toString())) {

                    isuser();
                } else {

                    Toast.makeText(updation.this, "Can't update password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateold() {

        String emailInput = old.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            old.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else if (!PASSWORD_PATTERN.matcher(emailInput).matches()) {
            old.setError("Password must contain at least 4 character and 1 special character");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else {
            old.setError(null);
            old.setErrorEnabled(false);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validatenew() {

        String emailInput =  newpass.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            newpass.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        }else if (!PASSWORD_PATTERN.matcher(emailInput).matches()) {
            newpass.setError("Password must contain at least 4 character and 1 special character");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        }  else {
            newpass.setError(null);
            newpass.setErrorEnabled(false);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateuser() {

        String emailInput = user.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            user.setError("Field can't be empty");
            Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            return false;
        } else {
            user.setError(null);
            user.setErrorEnabled(false);
            return true;
        }
    }

    private void isuser() {

        final String username = user.getEditText().getText().toString().trim();
        final String password = newpass.getEditText().getText().toString().trim();
        final String upassword = old.getEditText().getText().toString().trim();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query check = reference.orderByChild("username").equalTo(username);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    reference.child(username).child("password").setValue(upassword);
                    Intent intent = new Intent(updation.this, activity6.class);
                    startActivity(intent);
                    finish();
                } else {

                    user.setError("no such user exists");
                    user.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
