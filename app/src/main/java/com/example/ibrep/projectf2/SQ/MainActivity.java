package com.example.ibrep.projectf2.SQ;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibrep.projectf2.BuildConfig;
import com.example.ibrep.projectf2.Email.Config;
import com.example.ibrep.projectf2.Email.SendMail;
import com.example.ibrep.projectf2.Login;
import com.example.ibrep.projectf2.SelectPage;
import com.example.ibrep.projectf2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import static com.example.ibrep.projectf2.SQ.Menu.INHISTORIC;

public class MainActivity extends AppCompatActivity {

    private String editTextEmail;
    private String editTextSubject;
    public ListView lv;
    EditText nameEditText;
    ArrayList<Spacecraft> spacecrafts = new ArrayList<>();
    static CustomAdapter adapter;
    final Boolean forUpdate = true;
    AlertDialog Excluir, EnviarNovamente;
    String hist;
    SwipeRefreshLayout mSwipeRefreshLayout;

    String assuntocrd;
    String messagecrd;
    //CRD e CCC assunto

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(MainActivity.this, SelectPage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent1, 0);
        overridePendingTransition(0, 0);
        MainActivity.this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Transition fade = new Fade();
//        fade.excludeTarget(R.id.bottom_navigation, true);
//        getWindow().setExitTransition(fade);
//        getWindow().setEnterTransition(fade);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_nearby);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
//                        Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(MainActivity.this, SelectPage.class);
//                        startActivity(intent1);
                        Intent intent = new Intent(MainActivity.this, Menu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(0, 0);
                        MainActivity.this.finish();
                        break;
                    case R.id.action_favorites:
//                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(MainActivity.this, SelectPage.class);
//                        startActivity(intent1);
                        Intent intent1 = new Intent(MainActivity.this, SelectPage.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent1, 0);
                        overridePendingTransition(0, 0);
                        MainActivity.this.finish();
                        break;
                    case R.id.action_nearby:
//                        Toast.makeText(MainActivity.this, "Histórico", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intent);
                        break;

                }
                return true;
            }
        });


        lv = (ListView) findViewById(R.id.lv);
        adapter = new CustomAdapter(this, spacecrafts);

        this.getSpacecrafts();
        Collections.reverse(spacecrafts);
        // lv.setAdapter(adapter);

        editTextEmail = Config.EMAILTO;
        editTextSubject = "CÓPIA"; //Assunto
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //recreate();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }
//    public void displayDialog(Boolean forUpdate)
//    {
//        Dialog d=new Dialog(this);
//        d.setTitle("SQLITE DATA");
//        d.setContentView(R.layout.dialog_layout);
//
//        nameEditText= (EditText) d.findViewById(R.id.nameEditTxt);
//        saveBtn= (Button) d.findViewById(R.id.saveBtn);
//        retrieveBtn= (Button) d.findViewById(R.id.retrieveBtn);
//
//        if(!forUpdate)
//        {
//            saveBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    save(nameEditText.getText().toString());
//                }
//            });
//            retrieveBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getSpacecrafts();
//                }
//            });
//        } else {
//
//            SET SELECTED TEXT
//            nameEditText.setText(adapter.getSelectedItemName());
//
//            saveBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                     update(nameEditText.getText().toString());
//                }
//            });
//            retrieveBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                     getSpacecrafts();
//                }
//            });
//        }
//
//        d.show();
//
//    }

    //SAVE
    private void save(String name, String assunto) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        boolean saved = db.add(name, assunto);

        if(saved)
        {
            Toast.makeText(this,"Salvo no dispositivo",Toast.LENGTH_SHORT).show();
            SharedPreferences ISHISTORIC = getSharedPreferences(INHISTORIC, MODE_PRIVATE);
            SharedPreferences.Editor editor = ISHISTORIC.edit();
            editor.putBoolean("haveAnything", true);
            editor.apply();

//            nameEditText.setText("");
//            getSpacecrafts();
        }else {
            Toast.makeText(this,"Não foi possível salvar no dispositivo",Toast.LENGTH_SHORT).show();
        }
    }

    //RETRIEVE OR GETSPACECRAFTS
    private void getSpacecrafts() {
        spacecrafts.clear();
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        Cursor c = db.retrieve();
        Spacecraft spacecraft = null;

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);

            spacecraft = new Spacecraft();
            spacecraft.setId(id);
            spacecraft.setName(name);

            spacecrafts.add(spacecraft);
        }

        db.closeDB();
        lv.setAdapter(adapter);
    }

    //UPDATE OR EDIT
    private void update(String newName) {
        //GET ID OF SPACECRAFT
        int id = adapter.getSelectedItemID();

        //UPDATE IN DB
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        boolean updated = db.update(newName, id);
        db.closeDB();

        if (updated) {
            nameEditText.setText(newName);
            getSpacecrafts();
        } else {
            Toast.makeText(this, "Unable To Update", Toast.LENGTH_SHORT).show();
        }

    }

    private void delete() {
        //GET ID
        int id = adapter.getSelectedItemID();

        //DELETE FROM DB
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        boolean deleted = db.delete(id);
        db.closeDB();

        if (deleted) {
            getSpacecrafts();
            Toast.makeText(this, "Excluido", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Unable To Delete", Toast.LENGTH_SHORT).show();
        }
    }

    public void copiar() {
        //GET ID
//        int id=adapter.getSelectedItemID();
        String str = adapter.getSelectedItemName();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(str, str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copiado", Toast.LENGTH_SHORT).show();
    }

    private void sendEmail() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);


        Random random = new Random();

        int versionCode = BuildConfig.VERSION_CODE;

        String numero1 = Integer.toString(random.nextInt(10));
        String numero2 = Integer.toString(random.nextInt(10));

        //10 00 19 04 10 35 60 04

        String protocolo = numero1 + numero2 + data_completa.substring(0, 2) + data_completa.substring(3, 5) + data_completa.substring(11, 13) + data_completa.substring(14, 16)+ data_completa.substring(17, 19) + "0" + versionCode;


        String str = adapter.getSelectedItemName();
        String WH = str.substring(0, 3);
        switch (WH) {
            case "KMP":
                String assuntokmp = "KMP-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                String datekmp = str.substring(str.indexOf(";")+10, str.indexOf(";", str.indexOf(";") + 1));
                String namekmp = str.substring(str.indexOf(";", str.indexOf(";") + 1)+9,   str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1));
                String emailkmp = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1)+10, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1));
                String KMkmp = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1)+7, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1));
                String email2 = editTextEmail.trim();
                String subject = assuntokmp.trim();
                String message = "<!DOCTYPE html><html><body>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Data: </strong>" + datekmp + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">KM particular: </strong>" + KMkmp + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + namekmp + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + emailkmp + "</p>"
                        ;

                SendMail sm = new SendMail(this, email2, subject, message);

                sm.execute();
                hist =
                        "\n" + " Data: " + datekmp.trim() + ";" + "\n" +
                                " Nome: " + namekmp.trim() + ";" + "\n" +
                                " Email: " + emailkmp.trim() + ";" + "\n" +
                                " KM: " + KMkmp.trim() + ";";
                save(hist, assuntokmp);
                break;
            case "CCC":
            case "CRD":
                String datecrd = str.substring(str.indexOf(";")+18, str.indexOf(";", str.indexOf(";") + 1));
                String nomealunocrd = str.substring(str.indexOf(";", str.indexOf(";") + 1)+18,   str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1));
                String numeromatriculacrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1)+24, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1));
                String valorcrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1)+13, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1));
                String nomecrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1));
                String emailcrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)+10, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));
                String notacrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1));


                if (WH.equals("CRD")) {
                    assuntocrd = "CRD-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                    messagecrd = "<!DOCTYPE html><html><body>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data do depósito: </strong>" + datecrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor: </strong>" + valorcrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + nomealunocrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">N° Matricula: </strong>" + numeromatriculacrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + nomecrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + emailcrd.trim() + "</p>"
                            + "<img src=\"" + notacrd + "\" width=\"400\" />"
                            + "<br />"
                            + "<a href=\"" + notacrd + "\">Imagem da nota</a>"
                    ;
                } else if (WH.equals("CCC")) {
                    assuntocrd = "CCC-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                    messagecrd = "<!DOCTYPE html><html><body>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data do cartão: </strong>" + datecrd.trim()  + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor: </strong>" + valorcrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + nomealunocrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">N° Matricula: </strong>" + numeromatriculacrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + nomecrd.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + emailcrd.trim() + "</p>"
                            + "<img src=\"" + notacrd + "\" width=\"400\" />"
                            + "<br/>"
                            + "<a href=\"" + notacrd + "\">Imagem do comprovante</a>"
                    ;
                }

                String emailtocrd = editTextEmail.trim();
                String subjectcrd = assuntocrd.trim();


                //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

                SendMail sm2 = new SendMail(this, emailtocrd, subjectcrd, messagecrd);

                sm2.execute();
                hist =
                        "\n" + " Data da nota: " + datecrd.trim() + ";" + "\n" +
                                " Nome do aluno: " + nomealunocrd.trim() + ";" + "\n" +
                                " Número da matrícula: " + numeromatriculacrd.trim() + ";" + "\n" +
                                " Valor: R$ " + valorcrd.trim() + ";" + "\n" +
                                " Nome: " + nomecrd.trim() + ";" + "\n" +
                                " Email: " + emailcrd.trim() + ";" + "\n" +
                                " Nota: " + notacrd + ";";
                save(hist, assuntocrd);

                break;
            case "CAB":
                String assuntocab = "CAB-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                String nomecab = str.substring(str.indexOf(";")+10, str.indexOf(";", str.indexOf(";") + 1));
                String emailcab = str.substring(str.indexOf(";", str.indexOf(";") + 1)+10,   str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1));
                String valor = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1)+18,   str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1));
                String datacab = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1)+9,   str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1));
                String valhod = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1)+14,   str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1));
                String notacab = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));
                String hodometrocab = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+20, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1));

                String emailtocab = editTextEmail.trim();
                String subjectcab = assuntocab.trim();
                String messagecab = "<!DOCTYPE html><html><body>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + nomecab.trim() + "</small></p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + emailcab.trim() + "</small></p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Valor da nota: </strong>" + valor + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Data da nota: </strong>" + datacab + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Hodômetro: </strong>" + valhod + " KM</p>"
                        + "<img src=\"" + notacab + "\" width=\"320\" />"
                        + "<img src=\"" + hodometrocab + "\" width=\"320\" />"
                        + "<br />"
                        + "<a href=\"" + notacab + "\">Imagem da nota</a>"
                        + "<br />"
                        + "<a href=\"" + hodometrocab + "\">Imagem hodômetro</a>"
                        ;

                //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

                SendMail sm3 = new SendMail(this, emailtocab, subjectcab, messagecab);

                sm3.execute();
                hist =
                        "\n" + " Nome: " + nomecab.trim() + ";" + "\n" +
                                " Email: " + emailcab.trim() + ";" + "\n" +
                                " Valor da Nota: " + valor + ";" + "\n" +
                                " Data: " + datacab + ";" + "\n" +
                                " Hodômetro: " + valhod + ";" + "\n" +
                                " Nota: " + notacab + ";" + "\n" +
                                " Imagem Hodômetro: " + hodometrocab + ";";
                save(hist, assuntocab);

                break;
            case "RCC":
                String assuntorcc = "RCC-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                String datercc = str.substring(str.indexOf(";")+18, str.indexOf(";", str.indexOf(";") + 1));
                String NFrcc = str.substring(str.indexOf(";", str.indexOf(";") + 1)+10,   str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1));
                String nomercc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1));
                String emailrcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1)+10, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1));
                String reemcartrcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1)+21, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1));
                String centrodecustorcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)+20, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));
                String categoriarcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+14, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                String subcategoriarcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1)+17, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                String valorrcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)+13, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                String jutificativarcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)+18, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                String notarcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));

                if (str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) != str.lastIndexOf( ';' )) {
                    String autorizacaorcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)+16, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                    String emailtorcc = editTextEmail.trim();
                    String subjectrcc = assuntorcc.trim();
                    String messagercc = "<!DOCTYPE html><html><body>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data da nota: </strong>" + datercc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">NF/CF: </strong>" + NFrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor da nota: </strong>R$ " + valorrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + nomercc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + emailrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Reembolso/Cartão: </strong>" + reemcartrcc + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Centro de custo: </strong>" + centrodecustorcc + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Categoria: </strong>" + categoriarcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Subcategoria: </strong>" + subcategoriarcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Justificativa: </strong>" + jutificativarcc.trim() + "</p>"
                            //+ "<img src=\"" + stringUriNota + "\" />"
                            //+ Teste1
                            + "<img src=\"" + notarcc + "\" width=\"270\" />"
                            + "<img src=\"" + autorizacaorcc + "\" width=\"270\" />"
                            + "<br />"
                            + "<a href=\"" + notarcc + "\">Imagem da nota</a>"
                            + "<br />"
                            + "<a href=\"" + autorizacaorcc + "\">Imagem autorização</a>"
                            //+ "<img src=\"data:image/png;base64, " + img_str.trim() + "\" />"
                            //+ "<img src=\"data:image/png;base64, " + img_str2.trim() + "\" />"
                            ;

                    //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

                    SendMail sm4 = new SendMail(this, emailtorcc, subjectrcc, messagercc);

                    sm4.execute();
                    hist =
                            "\n" + " Data da nota: " + datercc.trim() + ";" + "\n" +
                                    " NF/CF: " + NFrcc.trim() + ";" + "\n" +
                                    " Nome: " + nomercc.trim() + ";" + "\n" +
                                    " Email: " + emailrcc.trim() + ";" + "\n" +
                                    " Reembolso/Cartão: " + reemcartrcc + ";" + "\n" +
                                    " Centro de custo: " + centrodecustorcc + ";" + "\n" +
                                    " Categoria: " + categoriarcc.trim() + ";" + "\n" +
                                    " Subcategoria: " + subcategoriarcc.trim() + ";" + "\n" +
                                    " Valor: R$ " + valorrcc.trim() + ";" + "\n" +
                                    " Justificativa: " + jutificativarcc.trim() + ";" + "\n" +
                                    " Nota: " + notarcc + ";" + "\n" +
                                    " Autorização: " + autorizacaorcc + ";";
                } else {
                    String emailtorcc = editTextEmail.trim();
                    String subjectrcc = assuntorcc.trim();
                    String messagercc = "<!DOCTYPE html><html><body>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data da nota: </strong>" + datercc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">NF/CF: </strong>" + NFrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor da nota: </strong>R$ " + valorrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + nomercc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + emailrcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Reembolso/Cartão: </strong>" + reemcartrcc + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Centro de custo: </strong>" + centrodecustorcc + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Categoria: </strong>" + categoriarcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Subcategoria: </strong>" + subcategoriarcc.trim() + "</p>"
                            + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Justificativa: </strong>" + jutificativarcc.trim() + "</p>"
                            //+ "<img src=\"" + stringUriNota + "\" />"
                            //+ Teste1
                            + "<img src=\"" + notarcc + "\" width=\"270\" />"
//                            + "<img src=\"" + autorizacaorcc + "\" width=\"420\" />"
                            + "<br />"
                            + "<a href=\"" + notarcc + "\">Imagem da nota</a>"
//                            + "<br />"
//                            + "<a href=\"" + autorizacaorcc + "\">Imagem autorização</a>"
                            //+ "<img src=\"data:image/png;base64, " + img_str.trim() + "\" />"
                            //+ "<img src=\"data:image/png;base64, " + img_str2.trim() + "\" />"
                            ;

                    //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

                    SendMail sm4 = new SendMail(this, emailtorcc, subjectrcc, messagercc);

                    sm4.execute();
                    hist =
                            "\n" + " Data da nota: " + datercc.trim() + ";" + "\n" +
                                    " NF/CF: " + NFrcc.trim() + ";" + "\n" +
                                    " Nome: " + nomercc.trim() + ";" + "\n" +
                                    " Email: " + emailrcc.trim() + ";" + "\n" +
                                    " Reembolso/Cartão: " + reemcartrcc + ";" + "\n" +
                                    " Centro de custo: " + centrodecustorcc + ";" + "\n" +
                                    " Categoria: " + categoriarcc.trim() + ";" + "\n" +
                                    " Subcategoria: " + subcategoriarcc.trim() + ";" + "\n" +
                                    " Valor: R$ " + valorrcc.trim() + ";" + "\n" +
                                    " Justificativa: " + jutificativarcc.trim() + ";" + "\n" +
                                    " Nota: " + notarcc + ";";
                }
                save(hist, assuntorcc);

                break;
            case "CRC":
                String assuntocrc = "CRC-(" + str.substring(0, str.indexOf(";")) + ")" + protocolo;
                String nomealunocrc = str.substring(str.indexOf(";")+19, str.indexOf(";", str.indexOf(";") + 1));
                String numeromatriculacrc = str.substring(str.indexOf(";", str.indexOf(";") + 1)+24,   str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1));
                String quantidacrc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1)+26, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1));
                String nomecrc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1));
                String emailcrc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1)+10, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1));
                String chequecrc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)+11, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));

                String emailtorcc = editTextEmail.trim();
                String subjectrcc = assuntocrc.trim();
                String messagercc = "<!DOCTYPE html><html><body>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + nomealunocrc.trim() + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">N° Matrícula: </strong>" + numeromatriculacrc.trim() + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Quantidade de cheques: </strong>" + quantidacrc.trim() + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + nomecrc.trim() + "</p>"
                        + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + emailcrc.trim() + "</p>"
                        + "<img src=\"" + chequecrc + "\" width=\"400\" />"
                        + "<br />"
                        + "<a href=\"" + chequecrc + "\">Cheques</a>";

                //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

                SendMail sm5 = new SendMail(this, emailtorcc, subjectrcc, messagercc);

                sm5.execute();
                hist =
                        "\n" + " Nome do aluno: " + nomealunocrc.trim() + ";" + "\n" +
                                " Número da matrícula: " + numeromatriculacrc.trim() + ";" + "\n" +
                                " Quantidade de cheques: " + quantidacrc.trim() + ";" + "\n" +
                                " Nome: " + nomecrc.trim() + ";" + "\n" +
                                " Email: " + emailcrc.trim() + ";" + "\n" +
                                " Cheque: " + chequecrc + ";";
                save(hist, assuntocrc);
                break;
                default:
                    Toast.makeText(MainActivity.this, "Erro, não foi possível reenviar", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CharSequence title = item.getTitle();
        if (title == "Reenviar") {

            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Reenviar");
            //define a mensagem
            builder.setMessage("Essa ação irá enviar uma cópia desses dados");


            builder.setPositiveButton("Prosseguir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    if (isNetworkAvailable()) {
                        sendEmail();
                    } else {
                        Toast.makeText(getApplicationContext(),"Sem conexão com a internet", Toast.LENGTH_LONG).show();
                    }
                }
            });


            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            //cria o AlertDialog
            EnviarNovamente = builder.create();
            //Exibe
            EnviarNovamente.show();

        } else if (title == "Copiar") {
            copiar();
        } else if (title == "Excluir") {

            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Excluir");
            //define a mensagem
            builder.setMessage("Essa ação irá apenas excluir o dado de seu dispositivo");


            builder.setPositiveButton("Prosseguir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    delete();
                }
            });


            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            //cria o AlertDialog
            Excluir = builder.create();
            //Exibe
            Excluir.show();
        }

        return super.onContextItemSelected(item);
    }
}
















