package com.example.wiseapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class menulayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon, male, female, reset;
    Menu menu;
    TextInputLayout alc, alc1, alc2, amount, pints, wine, weight, hr, mn;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;

    Button button;
    static int gender = 0;
    static double SD_beer = 0.0;
    static double SD_whisky = 0.0;
    static double SD_wine = 0.0;
    static double dp = 0.0;
    static boolean perm = true;
    static double perc = 0.0;
    static double perc1 = 0.0;
    static double perc2 = 0.0;
    static boolean para = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menulayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menubutton);
        menu = navigationView.getMenu();
        final RelativeLayout relativeLayout = findViewById(R.id.relative);
        final RelativeLayout relativeLayout1 = findViewById(R.id.relative1);
        alc = findViewById(R.id.textInputLayout2);
        pints = findViewById(R.id.textInputLayout1);
        alc1 = findViewById(R.id.textInputLayout6);
        alc2 = findViewById(R.id.textInputLayout8);
        amount = findViewById(R.id.textInputLayout5);
        wine = findViewById(R.id.textInputLayout9);
        weight = findViewById(R.id.weightlayout);
        male = findViewById(R.id.maleImage);
        reset = findViewById(R.id.reset);
        female = findViewById(R.id.femaleimage);
        button = findViewById(R.id.calculate);
        hr = findViewById(R.id.Time1);
        mAuth = FirebaseAuth.getInstance();

        mn = findViewById(R.id.Time2);

        para = getIntent().getBooleanExtra("confirm", true);
        if (!para) {
            menu.findItem(R.id.out).setVisible(true);
            menu.findItem(R.id.log).setVisible(false);
            mAuth.updateCurrentUser(null);

        } else {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);

        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pints.getEditText().setText(null);
                alc.getEditText().setText(null);
                amount.getEditText().setText(null);
                alc1.getEditText().setText(null);
                wine.getEditText().setText(null);
                alc2.getEditText().setText(null);
                weight.getEditText().setText(null);
                hr.getEditText().setText(null);
                mn.getEditText().setText(null);
                relativeLayout.setBackgroundResource(R.drawable.card1);
                relativeLayout1.setBackgroundResource(R.drawable.card1);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String number = pints.getEditText().getText().toString().trim();
                String beer = alc.getEditText().getText().toString().trim();
                String peg = amount.getEditText().getText().toString().trim();
                String whisky = alc1.getEditText().getText().toString().trim();
                String glass = wine.getEditText().getText().toString().trim();
                String wi = alc2.getEditText().getText().toString().trim();
                String wt = weight.getEditText().getText().toString().trim();
                String hour = hr.getEditText().getText().toString().trim();
                String minutes = mn.getEditText().getText().toString().trim();

                if (!number.isEmpty() && !beer.isEmpty() && !wt.isEmpty()) {

                    double volume = Double.parseDouble(number) * 330;
                    perc = (volume * Double.parseDouble(beer)) / 100;
                    SD_beer = (perc / 12.7);

                    if (!peg.isEmpty() && !whisky.isEmpty()) {

                        double volume1 = Double.parseDouble(peg);
                        perc1 = (volume1 * Double.parseDouble(whisky)) / 100;
                        SD_whisky = (perc1 / 12.7);
                    }
                    if (!hour.isEmpty()) {

                        if (!minutes.isEmpty()) {

                            minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                            dp = Double.parseDouble(hour) + Double.parseDouble(minutes);
                            perm = true;
                        } else {
                            dp = Double.parseDouble(hour);
                            perm = true;
                        }
                    } else if (!minutes.isEmpty()) {

                        minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                        dp = Double.parseDouble(minutes);
                        perm = true;

                    } else {
                        perm = false;
                    }

                    if (!glass.isEmpty() && !wi.isEmpty()) {

                        double volume2 = Double.parseDouble(glass);
                        perc2 = (volume2 * Double.parseDouble(wi)) / 100;
                        SD_wine = (perc2 / 12.7);
                    }

                    double Total = SD_beer + SD_wine + SD_whisky;
                    double consume = perc + perc2 + perc1;
                    double a = 0.806 * Total * 1.2;
                    double bw, mr;
                    if (gender == 0) {
                        bw = 0.58;
                        mr = 0.015;
                    } else {
                        bw = 0.49;
                        mr = 0.017;
                    }
                    double b = bw * Double.parseDouble(wt);
                    double c = mr * dp;
                    double BAC = ((a / b) - c);
                    String str = String.valueOf(BAC);

                    if (!perm) {
                        Toast.makeText(menulayout.this, "Enter the fields appropriately.", Toast.LENGTH_SHORT).show();
                        Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

                    } else {

                        SD_beer = 0;
                        SD_whisky = 0;
                        SD_wine = 0;
                        perc1 = 0;
                        perc = 0;
                        perc = 0;
                        dp = 0;
                        Intent intent = new Intent(menulayout.this, activity4.class);
                        intent.putExtra("alcohol", consume);
                        intent.putExtra("count", Total);
                        if(str.length()>=6) {
                            intent.putExtra("drink", str.substring(0, 6));
                        }else{
                            intent.putExtra("drink", str);

                        }
                        startActivity(intent);
                    }

                } else if (!peg.isEmpty() && !whisky.isEmpty() && !wt.isEmpty()) {

                    double volume1 = Double.parseDouble(peg);
                    perc1 = (volume1 * Double.parseDouble(whisky)) / 100;
                    SD_whisky = (perc1 / 12.7);

                    if (!hour.isEmpty()) {

                        if (!minutes.isEmpty()) {

                            minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                            dp = Double.parseDouble(hour) + Double.parseDouble(minutes);
                            perm = true;
                        } else {
                            dp = Double.parseDouble(hour);
                            perm = true;
                        }
                    } else if (!minutes.isEmpty()) {

                        minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                        dp = Double.parseDouble(minutes);
                        perm = true;

                    } else {
                        perm = false;
                    }


                    if (!glass.isEmpty() && !wi.isEmpty()) {

                        double volume2 = Double.parseDouble(glass);
                        perc2 = (volume2 * Double.parseDouble(wi)) / 100;
                        SD_wine = (perc2 / 12.7);
                    }
                    double Total = SD_beer + SD_wine + SD_whisky;
                    double consume = perc + perc2 + perc1;
                    double a = 0.806 * Total * 1.2;
                    double bw, mr;
                    if (gender == 0) {
                        bw = 0.58;
                        mr = 0.015;
                    } else {
                        bw = 0.49;
                        mr = 0.017;
                    }
                    double b = bw * Double.parseDouble(wt);
                    double c = mr * dp;
                    double BAC = ((a / b) - c);
                    String str = String.valueOf(BAC);

                    if (!perm) {
                        Toast.makeText(menulayout.this, "Enter the fields appropriately.", Toast.LENGTH_SHORT).show();
                        Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

                    } else {

                        SD_wine = 0;
                        SD_whisky = 0;
                        perc1 = 0;
                        perc = 0;
                        perc = 0;
                        dp = 0;
                        Intent intent = new Intent(menulayout.this, activity4.class);
                        intent.putExtra("alcohol", consume);
                        intent.putExtra("count", Total);
                        if(str.length()>=6) {
                            intent.putExtra("drink", str.substring(0, 6));
                        }else{
                            intent.putExtra("drink", str);
                        }
                        startActivity(intent);
                    }


                } else if (!glass.isEmpty() && !wi.isEmpty() && !wt.isEmpty()) {

                    double volume2 = Double.parseDouble(glass);
                    perc2 = (volume2 * Double.parseDouble(wi)) / 100;
                    SD_wine = (perc2 / 12.7);

                    if (!hour.isEmpty()) {

                        if (!minutes.isEmpty()) {

                            minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                            dp = Double.parseDouble(hour) + Double.parseDouble(minutes);
                            perm = true;
                        } else {
                            dp = Double.parseDouble(hour);
                            perm = true;
                        }
                    } else if (!minutes.isEmpty()) {

                        minutes = String.valueOf(Double.parseDouble(minutes) / 60);
                        dp = Double.parseDouble(minutes);
                        perm = true;

                    } else {
                        perm = false;
                    }


                    double Total = SD_beer + SD_wine + SD_whisky;
                    double consume = perc + perc2 + perc1;
                    double a = 0.806 * Total * 1.2;
                    double bw, mr;
                    if (gender == 0) {
                        bw = 0.58;
                        mr = 0.015;
                    } else {
                        bw = 0.49;
                        mr = 0.017;
                    }
                    double b = bw * Double.parseDouble(wt);
                    double c = mr * dp;
                    double BAC = ((a / b) - c);
                    String str = String.valueOf(BAC);

                    if (!perm) {
                        Toast.makeText(menulayout.this, "Enter the fields appropriately.", Toast.LENGTH_SHORT).show();
                        Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));


                    } else {

                        SD_wine = 0;
                        perc1 = 0;
                        perc = 0;
                        perc = 0;
                        dp = 0;
                        Intent intent = new Intent(menulayout.this, activity4.class);
                        intent.putExtra("alcohol", consume);
                        intent.putExtra("count", Total);
                        if(str.length()>=6) {
                            intent.putExtra("drink", str.substring(0, 6));
                        }else{
                            intent.putExtra("drink", str);
                        }
                        startActivity(intent);
                    }
                } else {

                    Toast.makeText(menulayout.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vv.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

                }

            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                gender = 1;
                relativeLayout.setBackgroundResource(R.drawable.card1);
                relativeLayout1.setBackgroundResource(R.drawable.card2);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                gender = 0;
                relativeLayout1.setBackgroundResource(R.drawable.card1);
                relativeLayout.setBackgroundResource(R.drawable.card2);
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner1);
        spinner3 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.whisky, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.wine, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter1);
        spinner3.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(this);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = parent.getItemAtPosition(position).toString();
                if (text.equalsIgnoreCase("Jack Daniels")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("Johnny Walker")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(37));
                } else if (text.equalsIgnoreCase("Cutty Sark")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("VAT 69")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(39));
                } else if (text.equalsIgnoreCase("Chivas Regal 12")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("Jameson")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(35));
                } else if (text.equalsIgnoreCase("Teachers Highland Cream")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(45));
                } else if (text.equalsIgnoreCase("100 Pipers")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(43));
                } else if (text.equalsIgnoreCase("Ballantines")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(45));
                } else if (text.equalsIgnoreCase("Monkey Shoulder Triple Malt")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("The Glenlivet")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(37));
                } else if (text.equalsIgnoreCase("Glenfiddich 12")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("Black Dog")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(39));
                } else if (text.equalsIgnoreCase("Blenders Pride")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(40));
                } else if (text.equalsIgnoreCase("Amrut")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(35));
                } else if (text.equalsIgnoreCase("Royal Stag")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(35));
                } else if (text.equalsIgnoreCase("others")) {
                    alc1.getEditText().setText(null);
                    alc1.getEditText().setText(String.valueOf(35));
                } else {
                    alc1.getEditText().setText(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = parent.getItemAtPosition(position).toString();
                if (text.equalsIgnoreCase("Fratelli")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(12.50));
                } else if (text.equalsIgnoreCase("Sula")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(13));
                } else if (text.equalsIgnoreCase("Myra Misfit")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(13));
                } else if (text.equalsIgnoreCase("Grover La Reserve")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(13));
                } else if (text.equalsIgnoreCase("York Arrows")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(14.30));
                } else if (text.equalsIgnoreCase("KRSMA")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(13.60));
                } else if (text.equalsIgnoreCase("Les Cordeliers")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(11));
                } else if (text.equalsIgnoreCase("Zampa Soriee")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(12));
                } else if (text.equalsIgnoreCase("Riesling")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(12.20));
                } else if (text.equalsIgnoreCase("Others")) {
                    alc2.getEditText().setText(null);
                    alc2.getEditText().setText(String.valueOf(12.20));
                } else {
                    alc2.getEditText().setText(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                break;

            case R.id.log:
               Intent intent = new Intent(menulayout.this, activity6.class);
                startActivity(intent);
                break;
            case R.id.out:
                // Firebase sign out
                mAuth.signOut();

//                // Google sign out
//                mGoogleSignInClient.signOut().addOnCompleteListener(this,
//                        new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                updateUI(null);
//                            }
//                        });
                menu.findItem(R.id.out).setVisible(false);
                menu.findItem(R.id.log).setVisible(true);
                break;
            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.contact:
                String num = "tel:+917827305903";
                Uri uri = Uri.parse(num);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equalsIgnoreCase("kingfisher ultra")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(5));
        } else if (text.equalsIgnoreCase("kingfisher premium")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.8));
        } else if (text.equalsIgnoreCase("kingfisher strong")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(7.8));
        } else if (text.equalsIgnoreCase("Mahou classic")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.5));
        } else if (text.equalsIgnoreCase("Mahou strong")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(7.8));
        } else if (text.equalsIgnoreCase("Brocode(10)")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(10));
        } else if (text.equalsIgnoreCase("Brocode(15)")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(15));
        } else if (text.equalsIgnoreCase("Budweiser")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(5));
        } else if (text.equalsIgnoreCase("Miller lite")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.2));
        } else if (text.equalsIgnoreCase("Tuborg gold")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(6.5));
        } else if (text.equalsIgnoreCase("Tuborg classic")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(6.7));
        } else if (text.equalsIgnoreCase("Carlsberg")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(5));
        } else if (text.equalsIgnoreCase("Heineken")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(5));
        } else if (text.equalsIgnoreCase("Corona")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.6));
        } else if (text.equalsIgnoreCase("Bira 91")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(7));
        } else if (text.equalsIgnoreCase("Hoegaarden")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.9));
        } else if (text.equalsIgnoreCase("Others")) {
            alc.getEditText().setText(null);
            alc.getEditText().setText(String.valueOf(4.5));
        } else {
            alc.getEditText().setText(null);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void updateUI(FirebaseUser user) {

        if (user != null) {

            menu.findItem(R.id.out).setVisible(true);
            menu.findItem(R.id.log).setVisible(false);


        } else {
            menu.findItem(R.id.log).setVisible(true);
            menu.findItem(R.id.out).setVisible(false);

     }
    }

}