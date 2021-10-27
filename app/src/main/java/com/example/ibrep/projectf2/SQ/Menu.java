package com.example.ibrep.projectf2.SQ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibrep.projectf2.Login;
import com.example.ibrep.projectf2.R;
import com.example.ibrep.projectf2.SelectPage;

import static com.example.ibrep.projectf2.SelectPage.MY_PREFS_NAME;

public class Menu  extends AppCompatActivity {

    Button sair, changename, apagar;
    AlertDialog yourName, clean, askclean, askclose, managerdialog;
    TextView textname, textemail, manager;
    public static final String INHISTORIC = "MyPrefsFile";

    @Override
    public void onBackPressed()
    {
        Intent intent1 = new Intent(Menu.this, SelectPage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent1, 0);
        overridePendingTransition(0,0);
        Menu.this.finish();
    }

    SharedPreferences Admin;
    SharedPreferences CCC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Admin = getSharedPreferences("Admin", MODE_PRIVATE);
        CCC = getSharedPreferences("CCC", MODE_PRIVATE);

        manager = findViewById(R.id.manager);
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                //define o titulo
                builder.setTitle("Código de acesso");
                builder.setMessage("Insira apenas se autorizado");

                final EditText user = new EditText(Menu.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                user.setLayoutParams(lp);
                builder.setView(user);

                builder.setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (user.getText().toString().equals("ADM@AC10")) {
                            SharedPreferences.Editor editor = Admin.edit();
                            editor.putBoolean("Admin", true);
                            editor.apply();
                            Toast.makeText(Menu.this, "Acesso administrativo permitido", Toast.LENGTH_LONG).show();
                        } else if (user.getText().toString().equals("#CCC$")) {
                            Toast.makeText(Menu.this, "Cadastro de cartão de crédito permitido", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = CCC.edit();
                            editor.putBoolean("CCC", true);
                            editor.apply();
                        } else if (user.getText().toString().equals("0")) {
                            Toast.makeText(Menu.this, "Revogado acesso especial", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = CCC.edit();
                            editor.putBoolean("CCC", false);
                            editor.apply();
                            SharedPreferences.Editor editor1 = Admin.edit();
                            editor1.putBoolean("Admin", false);
                            editor1.apply();
                        } else {
                            Toast.makeText(Menu.this, "Acesso negado", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                managerdialog = builder.create();
                //Exibe
                managerdialog.show();
            }
        });

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        textname = findViewById(R.id.textname);
        textname.setText(prefs.getString("name", "No name defined"));

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        textemail = findViewById(R.id.textemail);
        textemail.setText(settings.getString("String", null));


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_recents);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
//                        Toast.makeText(Menu.this, "Menu", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SelectPage.this, SelectPage.class);
//                        startActivity(intent);
                        break;
                    case R.id.action_favorites:
//                        Toast.makeText(Menu.this, "Home", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SelectPage.this, SelectPage.class);
//                        startActivity(intent);
                        Intent intent = new Intent(Menu.this, SelectPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(0,0);
                        Menu.this.finish();
                        break;
                    case R.id.action_nearby:
//                        Toast.makeText(Menu.this, "Histórico", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SelectPage.this, MainActivity.class);
//                        startActivity(intent);
                        Intent intent1 = new Intent(Menu.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent1, 0);
                        overridePendingTransition(0,0);
                        Menu.this.finish();
                        break;

                }
                return true;
            }
        });

        sair = findViewById(R.id.sair);
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                //define o titulo
                builder.setTitle("Sair");
                //define a mensagem
                builder.setMessage("Deseja sair de sua conta?");


                builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        /*
                        //Cria o gerador do AlertDialog
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                        //define o titulo
                        builder.setTitle("Sair");
                        //define a mensagem
                        builder.setMessage("Deseja manter o hitórico de prestação de contas do dispositivo?");

                        builder.setNegativeButton("Manter histórico", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {*/
                                SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0); // 0 - for private mode
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("hasLoggedIn", false);
                                editor.putString("String", "No email defined");
                                editor.apply();

                                SharedPreferences settings2 = getSharedPreferences(MY_PREFS_NAME, 0); // 0 - for private mode
                                SharedPreferences.Editor editor2 = settings2.edit();
                                editor2.putString("name", "No name defined");
                                editor2.apply();

                                Intent intent = new Intent(Menu.this, Login.class);
                                startActivity(intent);
                                finish();
                            /*}
                        });

                        builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences ISHISTORIC = getSharedPreferences(INHISTORIC, MODE_PRIVATE);
                                if (ISHISTORIC.getBoolean("haveAnything", false)) {
                                    DBAdapter.openDB();
                                    deleteAll();
                                    SharedPreferences.Editor editor = ISHISTORIC.edit();
                                    editor.putBoolean("haveAnything", false);
                                    editor.apply();
                                } else {
                                    Toast.makeText(Menu.this, "Histórico vazio", Toast.LENGTH_SHORT).show();
                                }
                                SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0); // 0 - for private mode
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("hasLoggedIn", false);
                                editor.putString("String", "No email defined");
                                editor.apply();

                                SharedPreferences settings2 = getSharedPreferences(MY_PREFS_NAME, 0); // 0 - for private mode
                                SharedPreferences.Editor editor2 = settings2.edit();
                                editor2.putString("name", "No name defined");
                                editor2.apply();

                                Intent intent = new Intent(Menu.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        askclean = builder.create();
                        askclean.show();*/
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                //cria o AlertDialog
                askclose = builder.create();
                //Exibe
                askclose.show();
            }
        });

        /*apagar = findViewById(R.id.apagar);
        apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria o gerador do AlertDialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                //define o titulo
                builder.setTitle("Apagar o histórico do dipositivo");
                //define a mensagem
                builder.setMessage("Essa ação irá apagar todo o hitórico de pretações de contas de seu dispositivo");

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("Prosseguir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences ISHISTORIC = getSharedPreferences(INHISTORIC, MODE_PRIVATE);
                        if (ISHISTORIC.getBoolean("haveAnything", false)) {
                            DBAdapter.openDB();
                            deleteAll();
                            SharedPreferences.Editor editor = ISHISTORIC.edit();
                            editor.putBoolean("haveAnything", false);
                            editor.apply();
                        } else {
                            Toast.makeText(Menu.this, "Histórico vazio", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                clean = builder.create();
                clean.show();
            }
        });*/


        changename = findViewById(R.id.changename);
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria o gerador do AlertDialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                //define o titulo
                builder.setTitle("Alterar nome");
                //define a mensagem
                builder.setMessage("Insira seu nome completo no campo abaixo");

                final EditText globalname = new EditText(Menu.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                globalname.setLayoutParams(lp);
                builder.setView(globalname);

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (TextUtils.isEmpty(globalname.getText().toString())) {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("name", "No name defined");
                            editor.apply();
                            textname.setText(prefs.getString("name", "No name defined"));
                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("name", globalname.getText().toString());
                            editor.apply();
                            textname.setText(prefs.getString("name", "No name defined"));
                        }
                    }
                });
                yourName = builder.create();
                yourName.show();
            }
        });
    }

    public void deleteAll()
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        DBAdapter.db.execSQL("delete from "+ Constants.TB_NAME);
        DBAdapter.db.close();
    }

}

