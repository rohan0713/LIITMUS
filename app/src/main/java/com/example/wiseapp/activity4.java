package com.example.wiseapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class activity4 extends AppCompatActivity {


   static StringBuilder stringBuilder1 = new StringBuilder();
    static StringBuilder stringBuilder2 = new StringBuilder();
    Dialog dialog;
    Button btn;
    TextInputLayout text;
    TextView textView, textView2, textView4, textView3, textView1, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity4);


        textView = findViewById(R.id.cal1);
        textView2 = findViewById(R.id.bac);
        textView4 = findViewById(R.id.consume);
        textView3 = findViewById(R.id.textView14);
        textView1 = findViewById(R.id.more);
        textView5 = findViewById(R.id.con);


        dialog = new Dialog(this);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opendialog();
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog1();
            }
        });

        textView1.setPaintFlags(textView1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView5.setPaintFlags(textView5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        String str = getIntent().getStringExtra("drink");

        double st = getIntent().getDoubleExtra("count", 0);
        double sti = getIntent().getDoubleExtra("alcohol", 0);
        String s = String.valueOf(st);
        String sp = String.valueOf(sti);
        if (textView.getText() != null || textView2.getText() != null || textView4.getText() != null) {

            textView.setText(null);
            textView2.setText(null);
            textView4.setText(null);
            if (s.length() > 3) {
                textView.setText(s.substring(0, 4));

            } else {
                textView.setText(String.valueOf(Math.round(st)));
            }
            textView2.setText(str + "%");

            if(sp.length()>3){
                textView4.setText(sp.substring(0 , 4) + "ml");
            }else {
                textView4.setText(String.valueOf(sti) + "ml");
            }

            textView3.setText(String.valueOf(Math.round(st)) + " hrs");
        } else {
            if (s.length() > 3) {
                textView.setText(s.substring(0, 4));

            } else {
                textView.setText(String.valueOf(Math.round(st)));
            }
            textView2.setText(str + "%");

            if(sp.length()>3){
                textView4.setText(sp.substring(0 , 4) + "ml");
            }else {
                textView4.setText(String.valueOf(sti) + "ml");
            }
        }
        if (Double.parseDouble(str) > 0.15) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    opendialog1();

                }
            }, 4000);
        }
    }

    private void opendialog() {

        dialog.setContentView(R.layout.popup);
        dialog.show();
    }

    private void opendialog1() {

        dialog.setContentView(R.layout.custom);
        dialog.show();

        btn = dialog.findViewById(R.id.cab);
        text = dialog.findViewById(R.id.dest);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(activity4.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    String st = text.getEditText().getText().toString().trim();
                    if (st.isEmpty()) {
                        text.setError("Field can't be empty");
                        Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        text.setError(null);
                        text.setErrorEnabled(false);

                        getaddress(st, activity4.this);

                        StringBuilder str = new StringBuilder("https://m.uber.com/ul/?client_id=<CLIENT_ID>&action=setPickup&my_location&");
                        str.append("dropoff[latitude]=").append(stringBuilder2.toString());
                        str.append("&dropoff[longitude]=").append(stringBuilder1.toString());
                        str.append("&dropoff[nickname]=").append(st).append("&product_id=a1111c8c-c720-46c3-8534-2fcdd730040d&link_text=View%20team%20roster&partner_deeplink=partner%3A%2F%2Fteam%2F9383");
                        Uri uri = Uri.parse(str.toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                }else{

                    ActivityCompat.requestPermissions(activity4.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }
    public static void getaddress(final String locationaddress, final Context context) {


                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;

                try {
                    List addresslist = geocoder.getFromLocationName(locationaddress, 1);
                    if (addresslist != null && addresslist.size() > 0) {

                        Address address = (Address) addresslist.get(0);

                        stringBuilder1.delete(0 , stringBuilder1.length());
                        stringBuilder2.delete(0 , stringBuilder2.length());
                        stringBuilder1.append(address.getLongitude());
                        stringBuilder2.append(address.getLatitude());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

}
