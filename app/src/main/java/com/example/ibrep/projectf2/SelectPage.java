package com.example.ibrep.projectf2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ibrep.projectf2.Pages.CAB;
import com.example.ibrep.projectf2.Pages.CRC;
import com.example.ibrep.projectf2.Pages.CRD;
import com.example.ibrep.projectf2.Pages.KMP;
import com.example.ibrep.projectf2.Pages.RCC;
import com.example.ibrep.projectf2.SQ.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SelectPage extends AppCompatActivity {
    ImageView callRCC;
    ImageView callCRD;
    ImageView callCAB;
    ImageView callKMP;
    ImageView callCRC;
    ImageView callOraculo;
    ImageView callDrive;
    AlertDialog yourName;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    //private TextView versionStatus;

    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    private void ShowKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    //private static final String version_Control = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        FirebaseApp.initializeApp(this);
//        Transition fade = new Fade();
//        fade.excludeTarget(R.id.bottom_navigation, true);
//        getWindow().setExitTransition(fade);
//        getWindow().setEnterTransition(fade);

        //versionStatus = findViewById(R.id.versionStatus);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        /*mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if ("1.0".equals(mFirebaseRemoteConfig.getString(version_Control))){
                            versionStatus.setText("Atualizado");
                        } else {
                            versionStatus.setText("Desatualizado");
                        }
                        Toast.makeText(SelectPage.this, mFirebaseRemoteConfig.getString(version_Control),Toast.LENGTH_LONG).show();
                    }
                });*/



        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


        if (prefs.getString("name", "No name defined").equals("No name defined")) {
            //Cria o gerador do AlertDialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Bem vindo!");
            //define a mensagem
            builder.setMessage("Insira seu nome completo no campo abaixo");

            final EditText globalname = new EditText(SelectPage.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            globalname.setLayoutParams(lp);
            builder.setView(globalname);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    if (TextUtils.isEmpty(globalname.getText().toString())) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("name", "No name defined");
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("name", globalname.getText().toString());
                        editor.apply();
                    }
                }
            });
            yourName = builder.create();
            yourName.setCancelable(false);
            yourName.show();
            ShowKeyboard();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_favorites);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
//                        Toast.makeText(SelectPage.this, "Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SelectPage.this, com.example.ibrep.projectf2.SQ.Menu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(0, 0);
                        SelectPage.this.finish();
                        break;
                    case R.id.action_favorites:
//                        Toast.makeText(SelectPage.this, "Home", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SelectPage.this, SelectPage.class);
//                        startActivity(intent);
                        break;
                    case R.id.action_nearby:
//                        Toast.makeText(SelectPage.this, "Histórico", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SelectPage.this, MainActivity.class);
//                        startActivity(intent);
                        Intent intent1 = new Intent(SelectPage.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent1, 0);
                        overridePendingTransition(0, 0);
                        SelectPage.this.finish();
                        break;
                }
                return true;
            }
        });

        callCRC = findViewById(R.id.card_viewCRC);
        callCRC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, CRC.class);
                startActivity(intent);
            }
        });

        callRCC = findViewById(R.id.card_viewRCC);
        callRCC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, RCC.class);
                startActivity(intent);
            }
        });

        callCRD = findViewById(R.id.card_viewCRD);
        callCRD.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, CRD.class);
                startActivity(intent);
            }
        });

        callCAB = findViewById(R.id.card_viewCAB);
        callCAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, CAB.class);
                startActivity(intent);
            }
        });

        callKMP = findViewById(R.id.card_viewKMP);
        callKMP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, KMP.class);
                startActivity(intent);
            }
        });

        callDrive = findViewById(R.id.card_viewDrive);
        callDrive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, Drive.class);
                startActivity(intent);
            }
        });

        callOraculo = findViewById(R.id.card_viewOraculo);
        callOraculo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPage.this, Oraculo.class);
                startActivity(intent);
            }
        });
    }
}