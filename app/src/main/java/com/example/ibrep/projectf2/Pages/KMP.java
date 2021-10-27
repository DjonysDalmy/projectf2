package com.example.ibrep.projectf2.Pages;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Random;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibrep.projectf2.BuildConfig;
import com.example.ibrep.projectf2.Email.Config;
import com.example.ibrep.projectf2.Login;
import com.example.ibrep.projectf2.SQ.DBAdapter;
import com.example.ibrep.projectf2.R;
import com.example.ibrep.projectf2.Email.SendMail;
import com.example.ibrep.projectf2.SQ.MainActivity;
import com.example.ibrep.projectf2.SQ.Spacecraft;
import com.example.ibrep.projectf2.SelectPage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.ibrep.projectf2.SQ.Menu.INHISTORIC;
import static com.example.ibrep.projectf2.SelectPage.MY_PREFS_NAME;

public class KMP extends AppCompatActivity implements View.OnClickListener {

    private String editTextEmail;
    private String editTextSubject;
    String protocolo;
    private EditText nomekmp;
    TextInputLayout nomekmpLay;
    private EditText emailkmp;
    TextInputLayout emailkmpLay;
    private EditText kmkmp;
    TextInputLayout kmkmpLay;

    AlertDialog asksendmail;
    Boolean sendedmail;

    private EditText datekmp;
    TextInputLayout datekmpLay;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String hist;

    //ArrayList<Spacecraft> spacecrafts = new ArrayList<>();

    private FloatingActionButton buttonSendkmp;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kmp);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sendedmail = false;

        editTextEmail = Config.EMAILTO;
        editTextSubject = "KMP";
        nomekmp = findViewById(R.id.Nomekmp);
        nomekmpLay = findViewById(R.id.Nomelaykmp);
        emailkmp = findViewById(R.id.Emailkmp);
        emailkmpLay = findViewById(R.id.Emaillaykmp);
        kmkmp = findViewById(R.id.KMkmp);
        kmkmpLay = findViewById(R.id.KMlaykmp);
        datekmp = findViewById(R.id.SelectedDatekmp);
        datekmpLay = findViewById(R.id.SelectedDatelaykmp);

        datekmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard(view);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(KMP.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                datekmp.setText(day + "/" + (month + 1) + "/" + year);
                                datekmpLay.setError(null);
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });


        kmkmp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    datekmp.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        buttonSendkmp = findViewById(R.id.buttonSendkmp);
        buttonSendkmp.setOnClickListener(this);

        nomekmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomekmpLay.setError(null);
            }
        });
        emailkmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailkmpLay.setError(null);
            }
        });
        kmkmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kmkmpLay.setError(null);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String namecheck = prefs.getString("name", null);
        if (namecheck == null | namecheck.equals("No name defined")) {
            nomekmp.requestFocus();
        } else {
            nomekmp.setText(prefs.getString("name", "No name defined"));
            kmkmp.requestFocus();
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        String emailcheck = settings.getString("String", null);
        if (emailcheck == null | emailcheck.equals("No email defined")) {
            finish();
            System.exit(0);
        } else {
            emailkmp.setText(settings.getString("String", null));

        }
    }




    private void sendEmail() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar  cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);

        Random random = new Random();

        int versionCode = BuildConfig.VERSION_CODE;

        String numero1 = Integer.toString(random.nextInt(10));
        String numero2 = Integer.toString(random.nextInt(10));

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);

        //10 00 19 04 10 35 60 04

        protocolo =  numero1 + numero2 + data_completa.substring(0, 2) + data_completa.substring(3, 5) + data_completa.substring(11, 13) + data_completa.substring(14, 16)+ data_completa.substring(17, 19) + "0" + versionCode;

        editTextSubject = "KMP " + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + " " + protocolo;

        String email = editTextEmail.trim();
        String subject = editTextSubject.trim();
        String message = "<!DOCTYPE html><html><body>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Data: </strong>" + datekmp.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">KM particular: </strong>" + kmkmp.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + nomekmp.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + emailkmp.getText().toString().trim() + "</p>"
                ;

        //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
        hist =
                "\n" + " Data: " + datekmp.getText().toString().trim() + ";" + "\n" +
                        " Nome: " + nomekmp.getText().toString().trim() + ";" + "\n" +
                        " Email: " + emailkmp.getText().toString().trim() + ";" + "\n" +
                        " KM: " + kmkmp.getText().toString().trim() + ";";
        save(hist, editTextSubject);
        buttonSendkmp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        buttonSendkmp.setClickable(true);
    }

    private void save(String name, String assunto)
    {
        DBAdapter db=new DBAdapter(this);
        db.openDB();
        boolean saved=db.add(name, assunto);

        if(saved)
        {
            Toast.makeText(this,"Salvo no dispositivo",Toast.LENGTH_SHORT).show();
            SharedPreferences ISHISTORIC = getSharedPreferences(INHISTORIC, MODE_PRIVATE);
            SharedPreferences.Editor editor = ISHISTORIC.edit();
            editor.putBoolean("haveAnything", true);
            editor.apply();
            /*Intent intent = new Intent(KMP.this, MainActivity.class);
            startActivity(intent);*/

//            nameEditText.setText("");
//            getSpacecrafts();
        }else {
            Toast.makeText(this,"Não foi possível salvar no dispositivo",Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void asksendmail(){
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirme sua solicitação");
        //define a mensagem
        builder.setMessage("Deseja enviar esses dados?");


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                sendedmail = false;
                buttonSendkmp.performClick();
            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        asksendmail = builder.create();
        //Exibe
        asksendmail.show();
    }


    @Override
    public void onClick(View v) {
        if (sendedmail) {
            asksendmail();
        } else {
            if (isNetworkAvailable()) {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (nomekmp.getText().toString().trim().equals("")) {
                    //Negative
                    nomekmpLay.setError("Insira seu nome");
                    nomekmp.requestFocus();
                } else {
                    nomekmpLay.setError(null);
                    if (emailkmp.getText().toString().trim().equals("")) {
                        //Negative
                        emailkmpLay.setError("Insira seu email");
                        emailkmp.requestFocus();
                    } else {
                        emailkmpLay.setError(null);
                        if (kmkmp.getText().toString().trim().equals("")) {
                            //Negative
                            kmkmpLay.setError("Insira a quilometragem");
                            kmkmp.requestFocus();
                        } else {
                            kmkmpLay.setError(null);
                            if (datekmp.getText().toString().trim().equals("")) {
                                //Negative
                                datekmpLay.setError("Insira uma data");
                                datekmp.requestFocus();
                            } else {
                                if (nomekmp.getText().toString().contains(";") || emailkmp.getText().toString().contains(";") || kmkmp.getText().toString().contains(";") || datekmp.getText().toString().contains(";")) {

                                    //mensagem de erro
                                    Toast.makeText(getApplicationContext(),"\";\" é um caractere inválido", Toast.LENGTH_LONG).show();
                                } else {
                                    datekmpLay.setError(null);
                                    sendEmail();
                                    sendedmail = true;
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void CloseKeyBoard(View z) {
        View view2 = this.getCurrentFocus();
        if (view2 != null)

        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
}