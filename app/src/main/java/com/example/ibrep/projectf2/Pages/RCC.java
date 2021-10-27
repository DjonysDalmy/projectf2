package com.example.ibrep.projectf2.Pages;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibrep.projectf2.BuildConfig;
import com.example.ibrep.projectf2.Email.Config;
import com.example.ibrep.projectf2.Login;
import com.example.ibrep.projectf2.SQ.DBAdapter;
import com.example.ibrep.projectf2.R;
import com.example.ibrep.projectf2.Email.SendMail;
import com.example.ibrep.projectf2.SQ.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.angmarch.views.NiceSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.example.ibrep.projectf2.SQ.Menu.INHISTORIC;
import static com.example.ibrep.projectf2.SelectPage.MY_PREFS_NAME;
import static com.sun.mail.util.ASCIIUtility.getBytes;

public class RCC extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private AlertDialog galeryOrCameraNota, galeryOrCameraAutorizacao, asksendmail, askuri;
    private FloatingActionButton buttonSend;

    private String emailremetente;
    private String assunto;
    private RadioGroup radioGroupCartaoReembolso, radioGroupCentroDeCusto;
    private RadioButton pptionCataoReembolsoCARTAO, optionCentroDeCustoREEMBOLSO;
    private RadioButton optionCentroDeCustoCOMERCIAL,optionCentroDeCustoADMINISTRATIVO,optionCentroDeCustoDIRETORIA,optionCentroDeCustoINVESTIMENTOS,optionCentroDeCustoPEDAGOGICO,optionCentroDeCustoTECNOLOGIA,optionCentroDeCustoFINANCEIRO;
    private String stringSelectedCartaoReembolso, stringSelectedCentroDeCusto;
    TextView textViewRadioCartaoReembolo;
    TextView textViewRadioCentroDeCusto;

    private EditText editTextNome;
    TextInputLayout editTextNomeLAY;
    private EditText editTextMessageEmail;
    TextInputLayout editTextMessageEmailLAY;
    private EditText editTextMessageNF;
    TextInputLayout editTextMessageNFLAY;
    private EditText editTextSelectedValor;
    TextInputLayout editTextSelectedValorLAY;
    private EditText editTextJustificativa;
    TextInputLayout editTextJustificativaLAY;

    EditText editTextDate;
    TextInputLayout editTextDateLAY;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String protocolo;

    private ImageView imgViewNota;
    private ImageView imgView2Autorizacao;

    private String stringCategoria, stringSubcategoria;
    private NiceSpinner spinnerCategoria, spinnerSubcategoria;

    List<String> listSpinnerCategoria;
    List<String> listSpinnerSubcategoria1;
    List<String> listSpinnerSubcategoria2;
    List<String> listSpinnerSubcategoria3;
    List<String> listSpinnerSubcategoria4;
    List<String> listSpinnerSubcategoria5;
    List<String> listSpinnerSubcategoria6;
    List<String> listSpinnerSubcategoria7;
    List<String> listSpinnerSubcategoria8;
    List<String> listSpinnerSubcategoria9;
    List<String> listSpinnerSubcategoria10;
    List<String> listSpinnerSubcategoria11;
    List<String> listSpinnerSubcategoria12;
    List<String> listSpinnerSubcategoria13;
    List<String> listSpinnerSubcategoria14;
    List<String> listSpinnerSubcategoria15;
    List<String> listSpinnerSubcategoria16;
    List<String> listSpinnerSubcategoria17;
    List<String> listSpinnerSubcategoria18;



    FirebaseStorage storageNota;
    StorageReference storageReferenceNota;
    private Uri filePathNota;
    String stringUriNota;

    FirebaseStorage storageAutorizacao;
    StorageReference storageReferenceAutorizacao;
    private Uri filePathAutorizacao;
    String stringUriAutorizacao;

    String hist;

    Boolean sendedmail;

    Switch RCCswitch;
    Boolean orcamento;
    String justificativa;

    Boolean ADM;

    SharedPreferences Admin;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(), bankNames[position], Toast.LENGTH_LONG);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rcc);

        // Get intent, action and MIME type
        final Intent intent = getIntent();
        String action = intent.getAction();
        final String type = intent.getType();

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Admin = getSharedPreferences("Admin", MODE_PRIVATE);

        SharedPreferences emailverify = getSharedPreferences(Login.PREFS_NAME, 0);
        if (emailverify.getString("String", null).equals("dhthdtjtyj@ibrep.com.br")
                || emailverify.getString("String", null).equals("ftjftjtjfs@ibrep.com.br")
                || emailverify.getString("String", null).equals("ftjftjfgndt@ibrep.com.br")
                || emailverify.getString("String", null).equals("dfjrjdtyukk@ibrep.com.br")
                || emailverify.getString("String", null).equals("dfjyrtdjyt@ibrep.com.br")
                || emailverify.getString("String", null).equals("gkghjxdfth@ibrep.com.br")
                || emailverify.getString("String", null).equals("rtfjhhjkgky@ibrep.com.br")) {
            ADM = true;
        } else {
            ADM = Admin.getBoolean("Admin", false);
        }

        RCCswitch = findViewById(R.id.RCCswitch);
        orcamento = false;

        if (ADM) {
            RCCswitch.setVisibility(View.VISIBLE);
            listSpinnerCategoria = new LinkedList<>(Arrays.asList(
                    "1.OBZ-",
                    "2.OBZ-",
                    "3.OBZ-",
                    "4.OBZ-",
                    "5.OBZ-",
                    "6.OBZ-",
                    "7.OBZ-",
                    "8.OBZ-",
                    "9.OBZ-",
                    "10.OBZ-",
                    "11.OBZ-",
                    "12.OBZ-",
                    "13.OBZ-",
                    "14.OBZ-",
                    "15.OBZ-",
                    "16.OBZ-",
                    "17.OBZ-",
                    "18.OBZ-"));
        } else {
            RCCswitch.setVisibility(View.GONE);
            listSpinnerCategoria = new LinkedList<>(Arrays.asList(
                    "1.OBZ-",
                    "2.OBZ-"));
        }


        RCCswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RCCswitch.isChecked()) {
                    orcamento = true;
                } else {
                    orcamento = false;
                }
            }
        });

        sendedmail = false;

        storageNota = FirebaseStorage.getInstance();
        storageReferenceNota = storageNota.getReference();

        storageAutorizacao = FirebaseStorage.getInstance();
        storageReferenceAutorizacao = storageAutorizacao.getReference();

        emailremetente = Config.EMAILTO;
        assunto = "RCC"; //Assunto
        radioGroupCartaoReembolso = findViewById(R.id.radioGroupCarRemrcc);
        pptionCataoReembolsoCARTAO = findViewById(R.id.cardrcc);
        optionCentroDeCustoREEMBOLSO = findViewById(R.id.rembrcc);
        radioGroupCentroDeCusto = findViewById(R.id.radioGroupcencusrcc);
        optionCentroDeCustoCOMERCIAL = findViewById(R.id.comercialrcc);
        editTextNome = findViewById(R.id.Nomercc);
        editTextMessageEmail = findViewById(R.id.Emailrcc);
        editTextMessageNF = findViewById(R.id.editTextMessageNFrcc);
        editTextSelectedValor = findViewById(R.id.SelectedValorrcc);
        editTextJustificativa = findViewById(R.id.Justificativarcc);
        buttonSend = findViewById(R.id.buttonSendrcc);
        editTextDate = findViewById(R.id.SelectedDatercc);
        optionCentroDeCustoREEMBOLSO = findViewById(R.id.rembrcc);
        pptionCataoReembolsoCARTAO = findViewById(R.id.cardrcc);
        optionCentroDeCustoCOMERCIAL = findViewById(R.id.comercialrcc);
        optionCentroDeCustoADMINISTRATIVO = findViewById(R.id.administrativorcc);
        optionCentroDeCustoDIRETORIA = findViewById(R.id.diretoriarcc);
        optionCentroDeCustoINVESTIMENTOS = findViewById(R.id.investimentosrcc);
        optionCentroDeCustoPEDAGOGICO = findViewById(R.id.pedagogicorcc);
        optionCentroDeCustoTECNOLOGIA = findViewById(R.id.tecnologiarcc);
        optionCentroDeCustoFINANCEIRO = findViewById(R.id.financeirorcc);
        editTextNomeLAY = findViewById(R.id.Nomelayrcc);
        editTextMessageEmailLAY = findViewById(R.id.Emaillayrcc);
        editTextMessageNFLAY = findViewById(R.id.editTextMessageNFlayrcc);
        editTextSelectedValorLAY = findViewById(R.id.SelectedValorlayrcc);
        editTextDateLAY = findViewById(R.id.SelectedDatelayrcc);
        editTextJustificativaLAY = findViewById(R.id.Justificativalayrcc);
        textViewRadioCartaoReembolo = findViewById(R.id.TxvRadio1rcc);
        textViewRadioCentroDeCusto = findViewById(R.id.TxvRadio2rcc);

        spinnerCategoria = findViewById(R.id.nice_spinnerrcc);
        spinnerCategoria.attachDataSource(listSpinnerCategoria);

        if (ADM) {
            optionCentroDeCustoADMINISTRATIVO.setVisibility(View.VISIBLE);
            optionCentroDeCustoDIRETORIA.setVisibility(View.VISIBLE);
            optionCentroDeCustoFINANCEIRO.setVisibility(View.VISIBLE);
            optionCentroDeCustoTECNOLOGIA.setVisibility(View.VISIBLE);
            optionCentroDeCustoINVESTIMENTOS.setVisibility(View.VISIBLE);
            optionCentroDeCustoPEDAGOGICO.setVisibility(View.VISIBLE);
            listSpinnerCategoria = new LinkedList<>(Arrays.asList(
                    "1.OBZ-",
                    "2.OBZ-",
                    "3.OBZ-",
                    "4.OBZ",
                    "5.OBZ-",
                    "6.OBZ-",
                    "7.OBZ-",
                    "8.OBZ",
                    "9.OBZ-",
                    "10.OBZ-",
                    "11.OBZ- ",
                    "12.OBZ- ",
                    "13.OBZ-",
                    "14.OBZ-",
                    "15.OBZ-",
                    "16.OBZ-",
                    "17.OBZ- ",
                    "18.OBZ- "));
        } else {
            optionCentroDeCustoADMINISTRATIVO.setVisibility(View.GONE);
            optionCentroDeCustoDIRETORIA.setVisibility(View.GONE);
            optionCentroDeCustoFINANCEIRO.setVisibility(View.GONE);
            optionCentroDeCustoTECNOLOGIA.setVisibility(View.GONE);
            optionCentroDeCustoINVESTIMENTOS.setVisibility(View.GONE);
            optionCentroDeCustoPEDAGOGICO.setVisibility(View.GONE);
            listSpinnerCategoria = new LinkedList<>(Arrays.asList(
                    "1.OBZ- ",
                    "2.OBZ-"));
        }

        spinnerCategoria.attachDataSource(listSpinnerCategoria);


        spinnerSubcategoria = findViewById(R.id.nice_spinner2rcc);
        List<String> datasetP = new LinkedList<>(Arrays.asList(
                "1.1 OBZ-",
                "1.2 OBZ-",
                "1.3 OBZ- ",
                "1.4 OBZ-",
                "1.5 OBZ-",
                "1.6 OBZ-",
                "1.7 OBZ- ",
                "1.8 OBZ-",
                "1.9 OBZ-",
                "1.10 OBZ-"));
        spinnerSubcategoria.attachDataSource(datasetP);
        listSpinnerSubcategoria1 = datasetP;
        listSpinnerSubcategoria2 = new LinkedList<>(Arrays.asList(
                "2.1 OBZ-",
                "2.2 OBZ-"));
        listSpinnerSubcategoria3 = new LinkedList<>(Arrays.asList(
                "3.1 OBZ-",
                "3.2 OBZ-"));
        listSpinnerSubcategoria4 = new LinkedList<>(Arrays.asList(
                "4.1 OBZ- ",
                "4.2 OBZ-"));
        listSpinnerSubcategoria5 = new LinkedList<>(Arrays.asList(
                "5.1 OBZ-",
                "5.2 OBZ- ",
                "5.3 OBZ d",
                "5.4 OBZ- d",
                "5.5 OBZ- "));
        listSpinnerSubcategoria6 = new LinkedList<>(Arrays.asList(
                "6.1 OBZ- ",
                "6.2 OBZ- ",
                "6.3 OBZ- ",
                "6.4 OBZ-",
                "6.5 OBZ- "));
        listSpinnerSubcategoria7 = new LinkedList<>(Arrays.asList(
                "7.1 OBZ-o"));
        listSpinnerSubcategoria8 = new LinkedList<>(Arrays.asList(
                "8.1 OBZ- e",
                "8.2 OBZ- de  e ",
                "8.3 OBZ- higine  "));
        listSpinnerSubcategoria9 = new LinkedList<>(Arrays.asList(
                "9.1 OBZ-",
                "9.2 OBZ- e  de ",
                "9.3 OBZ- e "));
        listSpinnerSubcategoria10 = new LinkedList<>(Arrays.asList(
                "10.1 OBZ- de "));
        listSpinnerSubcategoria11 = new LinkedList<>(Arrays.asList(
                "11.1 OBZ-  ",
                "11.2 OBZ- e ",
                "11.3 OBZ-"));
        listSpinnerSubcategoria12 = new LinkedList<>(Arrays.asList(
                "12.1 OBZ- ",
                "12.2 OBZ- e ",
                "12.3 OBZ-  de "));
        listSpinnerSubcategoria13 = new LinkedList<>(Arrays.asList(
                "13.1 OBZ-",
                "13.2 OBZ-",
                "13.3 OBZ-",
                "13.4 OBZ-",
                "13.5 OBZ-",
                "13.6 OBZ-",
                "13.7 OBZ-"));
        listSpinnerSubcategoria14 = new LinkedList<>(Arrays.asList(
                "14.1 OBZ-",
                "14.2 OBZ- ",
                "14.3 OBZ-",
                "14.4 OBZ-/",
                "14.5 OBZ-/",
                "14.6 OBZ- ",
                "14.7 OBZ-/",
                "14.8 OBZ-  e ",
                "14.9 OBZ- ",
                "14.10 OBZ- de ",
                "14.11 OBZ- e ",
                "14.12 OBZ-/",
                "14.13 OBZ- e "));
        listSpinnerSubcategoria15 = new LinkedList<>(Arrays.asList(
                "15.1 OBZ- ",
                "15.2 OBZ- e "));
        listSpinnerSubcategoria16 = new LinkedList<>(Arrays.asList(
                "16.1 OBZ- e  de ",
                "16.2 OBZ- e ",
                "16.3 OBZ- e "));
        listSpinnerSubcategoria17 = new LinkedList<>(Arrays.asList(
                "17.1 OBZ-",
                "17.2 OBZ-",
                "17.3 OBZ-"));
        listSpinnerSubcategoria18 = new LinkedList<>(Arrays.asList(
                "18.1 OBZ- ",
                "18.2 OBZ- de  ",
                "18.3 OBZ-",
                "18.4 OBZ- de ",
                "18.5 OBZ-  - ",
                "18.6 OBZ-  da  - ",
                "18.7 OBZ-",
                "18.8 OBZ- ",
                "18.9 OBZ- de ",
                "18.10 OBZ-  e ",
                "18.11 OBZ-",
                "18.12 OBZ-",
                "18.13 OBZ- de ",
                "18.14 OBZ- ",
                "18.15 OBZ-/",
                "18.16 OBZ- /",
                "18.17 OBZ- / ",
                "18.18 OBZ- à "));



        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (spinnerCategoria.getSelectedItem().toString().equals("1.OBZ- ")) {
                    spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria1);
                }
                if (spinnerCategoria.getSelectedItem().toString().equals("2.OBZ-")) {
                    spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria2);
                }
                if (ADM) {
                    if (spinnerCategoria.getSelectedItem().toString().equals("3.OBZ- ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria3);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("4.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria4);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("5.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria5);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("6.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria6);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("7.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria7);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("8.OBZ- de ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria8);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("9.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria9);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("10.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria10);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("11.OBZ- ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria11);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("12.OBZ- ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria12);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("13.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria13);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("14.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria14);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("15.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria15);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("16.OBZ-")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria16);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("17.OBZ- ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria17);
                    }
                    if (spinnerCategoria.getSelectedItem().toString().equals("18.OBZ- ")) {
                        spinnerSubcategoria.attachDataSource(listSpinnerSubcategoria18);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });


        /*Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/


        editTextNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNomeLAY.setError(null);
            }
        });

        editTextMessageEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextMessageEmailLAY.setError(null);
            }
        });

        editTextSelectedValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSelectedValorLAY.setError(null);
            }
        });

        editTextJustificativa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextJustificativaLAY.setError(null);
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard(view);

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RCC.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                editTextDate.setText(day + "/" + (month + 1) + "/" + year);
                                editTextDateLAY.setError(null);
//                                editTextJustificativa.performClick();
//                                editTextJustificativa.requestFocus();
//                                ShowKeyboard();
                                editTextJustificativa.setFocusableInTouchMode(true);
                                editTextJustificativa.requestFocus();
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        radioGroupCartaoReembolso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            View view;

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CloseKeyBoard(view);
                if (checkedId == R.id.cardrcc) {
                    Toast.makeText(getApplicationContext(), "Cartão corporativo",
                            Toast.LENGTH_SHORT).show();
                    textViewRadioCartaoReembolo.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.rembrcc) {
                    Toast.makeText(getApplicationContext(), "Reembolso",
                            Toast.LENGTH_SHORT).show();
                    textViewRadioCartaoReembolo.setTextColor(Color.parseColor("#808080"));
                }
            }
        });


        radioGroupCentroDeCusto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            View view;

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CloseKeyBoard(view);
                if (checkedId == R.id.comercialrcc) {
                    Toast.makeText(getApplicationContext(), "hi9p", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.administrativorcc) {
                    Toast.makeText(getApplicationContext(), "drrh", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.diretoriarcc) {
                    Toast.makeText(getApplicationContext(), "ddrhdrh", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.financeirorcc) {
                    Toast.makeText(getApplicationContext(), "segsgftegb", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.investimentosrcc) {
                    Toast.makeText(getApplicationContext(), "segtseg", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.tecnologiarcc) {
                    Toast.makeText(getApplicationContext(), "jtyyjtyj", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                } else if (checkedId == R.id.pedagogicorcc) {
                    Toast.makeText(getApplicationContext(), "esg", Toast.LENGTH_SHORT).show();
                    textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#808080"));
                }
            }
        });


        buttonSend.setOnClickListener(this);
        imgViewNota = findViewById(R.id.imgViewrcc);
        imgViewNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                if (filePathNota == null) {
                    imgViewNota.setImageResource(R.drawable.ph_add_image);
                }
                Ch1();
            }
        });

        imgView2Autorizacao = findViewById(R.id.imgView2rcc);
        imgView2Autorizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                Ch2();

                /*Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                startActivityForResult(pickPhoto, 1);*/
            }
        });

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("RCC");
            //define a mensagem
            builder.setMessage("Em qual campo você deseja inserir a imagem?");

            builder.setPositiveButton("Nota", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    if (type.startsWith("image/")) {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathNota = imageUri;
                            imgViewNota.setImageURI(filePathNota);
                            // Update UI to reflect image being shared
                        } //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathNota = imageUri;
                            imgViewNota.setImageResource(R.drawable.iconpdf);
                        }
                    }
                }
            });

            builder.setNegativeButton("Autorização", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    if (type.startsWith("image/")) {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathAutorizacao = imageUri;
                            imgView2Autorizacao.setImageURI(filePathAutorizacao);
                            // Update UI to reflect image being shared
                        } //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathAutorizacao = imageUri;
                            imgView2Autorizacao.setImageURI(filePathAutorizacao);
                            imgView2Autorizacao.setImageResource(R.drawable.iconpdf);
                        }
                    }

                }
            });

            //cria o AlertDialog
            askuri = builder.create();
            //Exibe
            askuri.show();

        }  else {
            // Handle other intents, such as being started from the home screen
        }

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String namecheck = prefs.getString("name", null);
        if (namecheck == null | namecheck.equals("No name defined")) {
            editTextNome.requestFocus();
        } else {
            editTextNome.setText(prefs.getString("name", null));
            editTextMessageNF.requestFocus();
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        String emailcheck = settings.getString("String", null);
        if (emailcheck == null | emailcheck.equals("No email defined")) {        editTextJustificativa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    editTextDate.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });
            finish();
            System.exit(0);
        } else {
            editTextMessageEmail.setText(settings.getString("String", null));
        }


    }


    public void Ch1() {
        ActivityCompat.requestPermissions(RCC.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void Ch2() {
        ActivityCompat.requestPermissions(RCC.this, new String[]{Manifest.permission.CAMERA}, 2);
    }


    private void sendEmail() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar  cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        assunto = "RCC " + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + " " + protocolo;

        if (ADM) {
            if (orcamento) {
                justificativa = editTextJustificativa.getText().toString().trim() + " (Possui orçamento)";
            } else {
                justificativa = editTextJustificativa.getText().toString().trim() + " (Não possui orçamento)";
            }
        } else {
            justificativa = editTextJustificativa.getText().toString().trim();
        }

        if (stringUriAutorizacao != null) {

            String email = emailremetente.trim();
            String subject = assunto.trim();
            String message = "<!DOCTYPE html><html><body>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data da nota: </strong>" + editTextDate.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">NF/CF: </strong>" + editTextMessageNF.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor da nota: </strong>R$ " + editTextSelectedValor.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNome.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + editTextMessageEmail.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Reembolso/Cartão: </strong>" + stringSelectedCartaoReembolso + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Centro de custo: </strong>" + stringSelectedCentroDeCusto + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Categoria: </strong>" + stringCategoria.trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Subcategoria: </strong>" + stringSubcategoria.trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Justificativa: </strong>" + justificativa + "</p>"
                    //+ "<img src=\"" + stringUriNota + "\" />"
                    //+ Teste1
                    + "<img src=\"" + stringUriNota + "\" width=\"270\" />"
                    + "<img src=\"" + stringUriAutorizacao + "\" width=\"270\" />"
                    + "<br />"
                    + "<a href=\"" + stringUriNota + "\">Imagem da nota</a>"
                    + "<br />"
                    + "<a href=\"" + stringUriAutorizacao + "\">Imagem autorização</a>"
                    //+ "<img src=\"data:image/png;base64, " + img_str.trim() + "\" />"
                    //+ "<img src=\"data:image/png;base64, " + img_str2.trim() + "\" />"
                    ;

            //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

            SendMail sm = new SendMail(this, email, subject, message);

            sm.execute();
            hist =
                    "\n" + " Data da nota: " + editTextDate.getText().toString().trim() + ";" + "\n" +
                            " NF/CF: " + editTextMessageNF.getText().toString().trim() + ";" + "\n" +
                            " Nome: " + editTextNome.getText().toString().trim() + ";" + "\n" +
                            " Email: " + editTextMessageEmail.getText().toString().trim() + ";" + "\n" +
                            " Reembolso/Cartão: " + stringSelectedCartaoReembolso + ";" + "\n" +
                            " Centro de custo: " + stringSelectedCentroDeCusto + ";" + "\n" +
                            " Categoria: " + stringCategoria.trim() + ";" + "\n" +
                            " Subcategoria: " + stringSubcategoria.trim() + ";" + "\n" +
                            " Valor: R$ " + editTextSelectedValor.getText().toString().trim() + ";" + "\n" +
                            " Justificativa: " + editTextJustificativa.getText().toString().trim() + justificativa + ";" + "\n" +
                            " Nota: " + stringUriNota + ";" + "\n" +
                            " Autorização: " + stringUriAutorizacao + ";";
        } else {
            String email = emailremetente.trim();
            String subject = assunto.trim();
            String message = "<!DOCTYPE html><html><body>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data da nota: </strong>" + editTextDate.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">NF/CF: </strong>" + editTextMessageNF.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor da nota: </strong>R$ " + editTextSelectedValor.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNome.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + editTextMessageEmail.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Reembolso/Cartão: </strong>" + stringSelectedCartaoReembolso + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Centro de custo: </strong>" + stringSelectedCentroDeCusto + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Categoria: </strong>" + stringCategoria.trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Subcategoria: </strong>" + stringSubcategoria.trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Justificativa: </strong>" + justificativa + "</p>"
                    //+ "<img src=\"" + stringUriNota + "\" />"
                    //+ Teste1
                    + "<img src=\"" + stringUriNota + "\" width=\"270\" />"
                    + "<br />"
                    + "<a href=\"" + stringUriNota + "\">Imagem da nota</a>"
                    + "<br />"
                    //+ "<img src=\"data:image/png;base64, " + img_str.trim() + "\" />"
                    //+ "<img src=\"data:image/png;base64, " + img_str2.trim() + "\" />"
                    ;

            //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

            SendMail sm = new SendMail(this, email, subject, message);

            sm.execute();
            hist =
                    "\n" + " Data da nota: " + editTextDate.getText().toString().trim() + ";" + "\n" +
                            " NF/CF: " + editTextMessageNF.getText().toString().trim() + ";" + "\n" +
                            " Nome: " + editTextNome.getText().toString().trim() + ";" + "\n" +
                            " Email: " + editTextMessageEmail.getText().toString().trim() + ";" + "\n" +
                            " Reembolso/Cartão: " + stringSelectedCartaoReembolso + ";" + "\n" +
                            " Centro de custo: " + stringSelectedCentroDeCusto + ";" + "\n" +
                            " Categoria: " + stringCategoria.trim() + ";" + "\n" +
                            " Subcategoria: " + stringSubcategoria.trim() + ";" + "\n" +
                            " Valor: R$ " + editTextSelectedValor.getText().toString().trim() + ";" + "\n" +
                            " Justificativa: " + editTextJustificativa.getText().toString().trim() + justificativa + ";" + "\n" +
                            " Nota: " + stringUriNota + ";";
        }
            save(hist, assunto);
            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            buttonSend.setClickable(true);
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
            /*Intent intent = new Intent(RCC.this, MainActivity.class);
            startActivity(intent);*/

//            nameEditText.setText("");
//            getSpacecrafts();
        }else {
            Toast.makeText(this,"Não foi possível salvar no dispositivo",Toast.LENGTH_SHORT).show();
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }
    }

    String currentPhotoPath2;

    private File createImageFile2() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath2 = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent2() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile2();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 3);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        ImageView imgView = findViewById(R.id.imgViewrcc);
        ImageView imgView2 = findViewById(R.id.imgView2rcc);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //Câmera
                    File imgFile = new File(currentPhotoPath);
                    Bitmap selectedImageb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgViewNota.setImageBitmap(selectedImageb);
                    filePathNota = Uri.fromFile(imgFile);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    //Galeria
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgView.setImageURI(selectedImage);
                    filePathNota = selectedImage;
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    //Câmera
                    File imgFile = new File(currentPhotoPath2);
                    Bitmap selectedImageb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgView2Autorizacao.setImageBitmap(selectedImageb);
                    filePathAutorizacao = Uri.fromFile(imgFile);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    //Galeria
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgView2.setImageURI(selectedImage);
                    filePathAutorizacao = selectedImage;
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgViewNota.setImageResource(R.drawable.iconpdf);
                    filePathNota = selectedImage;
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgView2Autorizacao.setImageResource(R.drawable.iconpdf);
                    filePathAutorizacao = selectedImage;
                }
                break;
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    private Uri getImageUri2(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        //Cria o gerador do AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        //define o titulo
                        builder.setTitle("Selecione");
                        //define a mensagem
                        builder.setMessage("Image da nota");

                        builder.setNeutralButton("Arquivo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();

                                String[] mimeTypes =
                                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                                "text/plain",
                                                "application/pdf",
                                                "application/zip"};

                                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select"), 4);
                            }
                        });


                        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);
                                //Toast.makeText(RCC.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                            }
                        });


                        builder.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                dispatchTakePictureIntent();
                            }
                        });

                        //cria o AlertDialog
                        galeryOrCameraNota = builder.create();
                        //Exibe
                        galeryOrCameraNota.show();
                    } else {
                        Toast.makeText(RCC.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();

                    }
                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(RCC.this, "Você deve conceder permissão para utilizar esse recurso", Toast.LENGTH_SHORT).show();
                }
                //return;
            }
            break;
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        //Cria o gerador do AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        //define o titulo
                        builder.setTitle("Selecione");
                        //define a mensagem
                        builder.setMessage("Imagem da autorização");

                        builder.setNeutralButton("Arquivo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();

                                String[] mimeTypes =
                                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                                "text/plain",
                                                "application/pdf",
                                                "application/zip"};

                                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select"), 5);
                            }
                        });


                        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 2);
                                //Toast.makeText(RCC.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                            }
                        });


                        builder.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                dispatchTakePictureIntent2();
                            }
                        });
                        //cria o AlertDialog
                        galeryOrCameraAutorizacao = builder.create();
                        //Exibe
                        galeryOrCameraAutorizacao.show();
                    } else {
                        Toast.makeText(RCC.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RCC.this, "Você deve permitir o uso da câmera", Toast.LENGTH_SHORT).show();
                }
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

    public static void HideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert methodManager != null && view != null;
        methodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void ShowKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /*protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }*/

    ProgressDialog PRCC;
    private void uploadImage() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date date = new Date();
        Calendar  cal = Calendar.getInstance();
        cal.setTime(date);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);

        Random random = new Random();

        int versionCode = BuildConfig.VERSION_CODE;

        String numero1 = Integer.toString(random.nextInt(10));
        String numero2 = Integer.toString(random.nextInt(10));

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);

        //10 00 19 04 10 35 60 04

        protocolo = numero1 + numero2 + data_completa.substring(0, 2) + data_completa.substring(3, 5) + data_completa.substring(11, 13) + data_completa.substring(14, 16)+ data_completa.substring(17, 19) + "0" + versionCode;

        if (filePathNota != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            PRCC = progressDialog;
            PRCC.setCancelable(false);
            PRCC.setTitle("Enviando imagem...");
            PRCC.show();

            final StorageReference ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "RCC/" + "RCC---" + "Nota---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" /*+ wifiIpAddress(this) + "---"*/ + UUID.randomUUID().toString());

            ref.putFile(filePathNota).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RCC.this, "Nota enviada", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            PRCC.dismiss();
                            Toast.makeText(RCC.this, "Nâo foi possível enviar a nota " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            buttonSend.setClickable(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            PRCC.setMessage("Enviando nota " + (int) progress + "%");
                        }
                    });
            imgViewNota = findViewById(R.id.imgViewrcc);

            imgViewNota.setDrawingCacheEnabled(true);
            imgViewNota.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imgViewNota.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReferenceNota.putBytes(data);
            uploadTask = ref.putFile(filePathNota);

            final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else {

                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        stringUriNota = downloadUri.toString();
                        uploadImage2();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não foi possível criar uma URL válida para a nota", Toast.LENGTH_SHORT).show();
                        buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSend.setClickable(true);
                        // Handle failures
                        // ...
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Não foi possível encontrar a imagem da nota", Toast.LENGTH_SHORT).show();
            imgViewNota.setImageResource(R.drawable.ph_add_image_erro);
            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            buttonSend.setClickable(true);
        }
    }

    private void uploadImage2() {
        if (filePathAutorizacao != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Enviando imagem...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
            Date data1 = new Date();
            Calendar  cal = Calendar.getInstance();
            cal.setTime(data1);
            Date data_atual = cal.getTime();
            String data_completa = dateFormat.format(data_atual);
            Log.i("data_completa", data_completa);
            SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
            final StorageReference ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "RCC/" +  "RCC---" + "Autorização---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + /*"---" + wifiIpAddress(this) +*/ "---" + UUID.randomUUID().toString());
            ref.putFile(filePathAutorizacao).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RCC.this, "Autorização enviada", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            PRCC.dismiss();
                            Toast.makeText(RCC.this, "Não foi possível enviar a autorização " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            buttonSend.setClickable(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Enviando autorização " + (int) progress + "%");
                        }
                    });
            imgView2Autorizacao = findViewById(R.id.imgView2rcc);

            imgView2Autorizacao.setDrawingCacheEnabled(true);
            imgView2Autorizacao.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imgView2Autorizacao.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReferenceNota.putBytes(data);
            uploadTask = ref.putFile(filePathAutorizacao);
            final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        stringUriAutorizacao = downloadUri.toString();
                        progressDialog.dismiss();
                        PRCC.dismiss();
                        sendEmail();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não foi possível criar uma URL válida para a autorização", Toast.LENGTH_SHORT).show();
                        buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSend.setClickable(true);
                        progressDialog.dismiss();
                        PRCC.dismiss();
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            PRCC.dismiss();
            sendEmail();
        }
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
                buttonSend.performClick();
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
                if (editTextNome.getText().toString().trim().equals("")) {
                    //Negative
                    editTextNomeLAY.setError("Insira seu nome");
                    editTextNome.requestFocus();
                } else {
                    editTextNomeLAY.setError(null);
                    if (editTextMessageEmail.getText().toString().trim().equals("")) {
                        //Negative
                        editTextMessageEmailLAY.setError("Insira seu Email");
                        editTextMessageEmail.requestFocus();
                    } else {
                        editTextMessageEmailLAY.setError(null);
                        if (editTextSelectedValor.getText().toString().trim().equals("")) {
                            //Negative
                            editTextSelectedValorLAY.setError("Insira o valor da nota");
                            editTextSelectedValor.requestFocus();
                        } else {
                            editTextSelectedValorLAY.setError(null);
                            if (editTextDate.getText().toString().trim().equals("")) {
                                //Negative
                                editTextDateLAY.setError("Insira a data da nota");
                                editTextSelectedValor.requestFocus();
                                editTextDate.requestFocus();
                            } else {
                                editTextDateLAY.setError(null);
                                if (editTextJustificativa.getText().toString().trim().equals("")) {
                                    //Negative
                                    editTextJustificativaLAY.setError("Insira a jutificativa");
                                    editTextJustificativa.requestFocus();
                                } else {

                                    editTextJustificativaLAY.setError(null);

                                    int selectedId = radioGroupCartaoReembolso.getCheckedRadioButtonId();
                                    if (selectedId == pptionCataoReembolsoCARTAO.getId()) {
                                        stringSelectedCartaoReembolso = "Cartão Corporativo";
                                    } else if (selectedId == optionCentroDeCustoREEMBOLSO.getId()) {
                                        stringSelectedCartaoReembolso = "Reembolso";
                                    } else {
                                        stringSelectedCartaoReembolso = "null";
                                    }


                                    int selectedId2 = radioGroupCentroDeCusto.getCheckedRadioButtonId();
                                    if (selectedId2 == optionCentroDeCustoCOMERCIAL.getId()) {
                                        stringSelectedCentroDeCusto = "Comercial";
                                    } else if (selectedId2 == optionCentroDeCustoADMINISTRATIVO.getId()) {
                                        stringSelectedCentroDeCusto = "Administrativo";
                                    } else if (selectedId2 == optionCentroDeCustoDIRETORIA.getId()) {
                                        stringSelectedCentroDeCusto = "Diretoria";
                                    } else if (selectedId2 == optionCentroDeCustoINVESTIMENTOS.getId()) {
                                        stringSelectedCentroDeCusto = "Investimentos";
                                    } else if (selectedId2 == optionCentroDeCustoPEDAGOGICO.getId()) {
                                        stringSelectedCentroDeCusto = "Pedagógico";
                                    } else if (selectedId2 == optionCentroDeCustoTECNOLOGIA.getId()) {
                                        stringSelectedCentroDeCusto = "Tecnologia";
                                    } else if (selectedId2 == optionCentroDeCustoFINANCEIRO.getId()) {
                                        stringSelectedCentroDeCusto = "Financeiro";
                                    } else {
                                        stringSelectedCentroDeCusto = "null";
                                    }

                                    if (stringSelectedCartaoReembolso.trim().equals("null")) {
                                        //Negative
                                        textViewRadioCartaoReembolo.setTextColor(Color.parseColor("#FF0000"));
                                    } else {
                                        if (stringSelectedCentroDeCusto.trim().equals("null")) {
                                            //Negative
                                            textViewRadioCentroDeCusto.setTextColor(Color.parseColor("#FF0000"));
                                        } else {
                                            if (editTextNome.getText().toString().contains(";") || editTextMessageEmail.getText().toString().contains(";") || editTextSelectedValor.getText().toString().contains(";") || editTextDate.getText().toString().contains(";") || editTextJustificativa.getText().toString().contains(";") || editTextMessageNF.getText().toString().contains(";")) {
                                                //mensagem de erro
                                                Toast.makeText(getApplicationContext(),"\";\" é um caractere inválido", Toast.LENGTH_LONG).show();
                                            } else {
                                                buttonSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                                                buttonSend.setClickable(false);


                                                stringCategoria = spinnerCategoria.getSelectedItem().toString();
                                                stringSubcategoria = spinnerSubcategoria.getSelectedItem().toString();
                                                sendedmail = true;
                                                uploadImage();
                                            }
                                        }
                                    }
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
}