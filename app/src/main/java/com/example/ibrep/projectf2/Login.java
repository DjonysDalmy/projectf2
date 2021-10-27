package com.example.ibrep.projectf2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText USUARIO, SENHA;
    TextInputLayout USUARIOlay, SENHAlay;
    Button BOTAO;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String email = "MyPrefsFile";




    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);


        if(hasLoggedIn)
        {
            Intent intent = new Intent(Login.this, SelectPage.class);
            startActivity(intent);
            Login.this.finish();
        } else {
            String log = settings.getString("String", null);
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseApp.initializeApp(this);

        USUARIO = findViewById(R.id.USUARIO);
        USUARIOlay = findViewById(R.id.USUARIOlay);
        SENHA = findViewById(R.id.SENHA);
        SENHAlay = findViewById(R.id.SENHAlay);
        BOTAO = findViewById(R.id.BOTAO);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                }

            }
        };

            BOTAO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View view) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    SENHAlay.setError(null);
                    USUARIOlay.setError(null);
                    auth(USUARIO.getText().toString(), SENHA.getText().toString());
                }
            });

        SENHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SENHAlay.setError(null);
            }
        });

        USUARIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USUARIOlay.setError(null);
            }
        });

        SENHA.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    BOTAO.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    public void auth(@NonNull String email, String senha) {
        if (TextUtils.isEmpty(email)) {
            USUARIOlay.setError("Insira seu email");
            USUARIO.requestFocus();
        } else {
        if (TextUtils.isEmpty(senha)) {
            Log.w("AUTH", "Email e/ou senha vazios");
            SENHAlay.setError("Insira sua senha");
            SENHA.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Log.w("AUTH", "Falha ao efetuar o Login: ", task.getException());
                        SENHAlay.setError("Inválido");
                        SENHAlay.requestFocus();
                    } else {
                        Log.d("AUTH", "Login Efetuado com sucesso!!!");
                        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("hasLoggedIn", true);
                        editor.putString("String", USUARIO.getText().toString());
                        editor.apply();
                        Intent intent = new Intent(Login.this, SelectPage.class);
                        startActivity(intent);
                        Login.this.finish();
                    }
                }
            });
        }
        }
    }
}
