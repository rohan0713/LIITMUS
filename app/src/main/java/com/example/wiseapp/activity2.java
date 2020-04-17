package com.example.wiseapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class activity2 extends AppCompatActivity {

    public static final String TAG = "mfa";
    EditText browse , val1 , val2;
    Button add;
    TextView tvresult;
    ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        val1 = findViewById(R.id.editText2);
        val2 = findViewById(R.id.editText3);
        add = findViewById(R.id.button);
        browse = findViewById(R.id.editText4);
        search = findViewById(R.id.imageButton);
        tvresult = findViewById(R.id.textView3);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int val3 , val4 , val5;
//
//                if(val1!=null && val2!= null) {
//                    val3 = Integer.valueOf(val1.getText().toString());
//                    val4 = Integer.valueOf(val2.getText().toString());
//
//                    val5 = val3 + val4;
//
//                    tvresult.setText(String.valueOf(val5));
//                }
//        }
//        });


//        search.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View v) {
//
//                String url = browse.getText().toString();
//                Uri uri = Uri.parse(url);
//                Intent ij = new Intent(Intent.ACTION_VIEW , uri);
//                try {
//
//
//                    startActivity(ij);
//                }
//                catch (ActivityNotFoundException anfe){
//
//
//                    Log.e(TAG, "onClick: there is an error", anfe );
//                    Toast.makeText(activity2.this,
//                            "could not find any app for this link",
//                            Toast.LENGTH_SHORT).show();
//                    Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
//
//
//                }
//            }
//        });

    }
}


