package com.example.ibrep.projectf2.Pages;

import android.Manifest;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.example.ibrep.projectf2.SQ.Menu.INHISTORIC;
import static com.example.ibrep.projectf2.SelectPage.MY_PREFS_NAME;

public class CAB extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog galeryOrCameraNota, galeryOrCameraHodometro, asksendmail, askuri;
    private String editTextEmail;
    private String editTextSubject;
    private EditText editTextNomecab;
    TextInputLayout nomeLay;
    private EditText emailUser;
    TextInputLayout emailUserLay;
    private ImageView nf;
    private ImageView hodometro;
    private FloatingActionButton buttonSendcab;

    TextInputLayout editTextSelectedValorLAY, editTextJustificativaLAY;

    private EditText editTextSelectedValorcab;
    private EditText editTextJustificativa;

    private EditText editTextDate;
    TextInputLayout editTextDateLAY;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String protocolo;

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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab);


        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sendedmail = false;

        storageNota = FirebaseStorage.getInstance();
        editTextDateLAY = findViewById(R.id.SelectedDatelaycab);
        editTextSelectedValorLAY = findViewById(R.id.SelectedValorlaycab);
        editTextJustificativa = findViewById(R.id.Justificativacab);
        editTextJustificativaLAY = findViewById(R.id.Justificativalaycab);
        editTextDate = findViewById(R.id.SelectedDatecab);
        editTextSelectedValorcab = findViewById(R.id.SelectedValorcab);
        storageReferenceNota = storageNota.getReference();
        storageAutorizacao = FirebaseStorage.getInstance();
        storageReferenceAutorizacao = storageAutorizacao.getReference();
        editTextEmail = Config.EMAILTO;
        editTextSubject = "CAB"; //Assunto
        editTextNomecab = findViewById(R.id.Nomecab);
        nomeLay = findViewById(R.id.Nomelaycab);
        emailUser = findViewById(R.id.Emailcab);
        emailUserLay = findViewById(R.id.Emaillaycab);
        nf = findViewById(R.id.imgViewcab);
        nf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                if (filePathNota == null) {
                    nf.setImageResource(R.drawable.ph_add_image);
                }
                Ch1();
            }
        });

        hodometro = findViewById(R.id.imgView2cab);
        hodometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                Ch2();
            }
        });

        // Get intent, action and MIME type
        final Intent intent = getIntent();
        String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("CAB");
            //define a mensagem
            builder.setMessage("Em qual campo você deseja inserir a imagem?");

            builder.setPositiveButton("Nota", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    if (type.startsWith("image/")) {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathNota = imageUri;
                            nf.setImageURI(filePathNota);
                            // Update UI to reflect image being shared
                        } //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathNota = imageUri;
                            nf.setImageURI(filePathNota);
                            nf.setImageResource(R.drawable.iconpdf);
                        }
                    }

                }
            });

            builder.setNegativeButton("Hodômetro", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    if (type.startsWith("image/")) {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathAutorizacao = imageUri;
                            hodometro.setImageURI(filePathAutorizacao);
                            // Update UI to reflect image being shared
                        } //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else {
                        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePathAutorizacao = imageUri;
                            hodometro.setImageURI(filePathAutorizacao);
                            hodometro.setImageResource(R.drawable.iconpdf);
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

        buttonSendcab = findViewById(R.id.buttonSendcab);
        buttonSendcab.setOnClickListener(this);

        editTextNomecab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeLay.setError(null);
            }
        });
        emailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailUserLay.setError(null);
            }
        });
        editTextSelectedValorcab.setOnClickListener(new View.OnClickListener() {
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

        editTextJustificativa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    nf.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        emailUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    editTextSelectedValorcab.requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String namecheck = prefs.getString("name", null);
        if (namecheck == null | namecheck.equals("No name defined")) {
            editTextNomecab.requestFocus();
        } else {
            editTextNomecab.setText(prefs.getString("name", "No name defined"));
            editTextSelectedValorcab.requestFocus();
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        String emailcheck = settings.getString("String", null);
        if (emailcheck == null | emailcheck.equals("No email defined")) {
            finish();
            System.exit(0);
        } else {
            emailUser.setText(settings.getString("String", null));
        }

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard(view);

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CAB.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                editTextDate.setText(day + "/" + (month + 1) + "/" + year);
                                editTextDateLAY.setError(null);
//                                editTextJustificativa.performClick();
//                                editTextJustificativa.requestFocus();
//                                ShowKeyboard();
                                editTextJustificativa.setFocusableInTouchMode(true);
                                ShowKeyboard();
                                editTextJustificativa.requestFocus();
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        editTextSelectedValorcab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
}

    private void ShowKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }



    public void Ch1() {
        ActivityCompat.requestPermissions(CAB.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void Ch2() {
        ActivityCompat.requestPermissions(CAB.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
    }

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
                        builder.setMessage("imagem da nota");


                        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);
                                //Toast.makeText(RCC.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                            }
                        });

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
                        Toast.makeText(CAB.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();

                    }
                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CAB.this, "Você deve conceder permissão para utilizar esse recurso", Toast.LENGTH_SHORT).show();
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
                        builder.setMessage("Imagem do hodômetro");


                        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 2);
                                //Toast.makeText(RCC.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                            }
                        });

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


                        builder.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                dispatchTakePictureIntent2();
                            }
                        });
                        //cria o AlertDialog
                        galeryOrCameraHodometro = builder.create();
                        //Exibe
                        galeryOrCameraHodometro.show();
                    } else {
                        Toast.makeText(CAB.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CAB.this, "Você deve permitir o uso da câmera", Toast.LENGTH_SHORT).show();
                }
            }
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

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //Câmera
                    File imgFile = new File(currentPhotoPath);
                    Bitmap selectedImageb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    nf.setImageBitmap(selectedImageb);
                    filePathNota = Uri.fromFile(imgFile);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    //Galeria
                    Uri selectedImage = imageReturnedIntent.getData();
                    nf.setImageURI(selectedImage);
                    filePathNota = selectedImage;
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    //Câmera
                    File imgFile = new File(currentPhotoPath2);
                    Bitmap selectedImageb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    hodometro.setImageBitmap(selectedImageb);
                    filePathAutorizacao = Uri.fromFile(imgFile);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    //Galeria
                    Uri selectedImage = imageReturnedIntent.getData();
                    hodometro.setImageURI(selectedImage);
                    filePathAutorizacao = selectedImage;
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    nf.setImageResource(R.drawable.iconpdf);
                    filePathNota = selectedImage;
                }
                break;

            case 5:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    hodometro.setImageResource(R.drawable.iconpdf);
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

    ProgressDialog PCAB;
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

        if (filePathNota != null && filePathAutorizacao != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            PCAB = progressDialog;
            PCAB.setTitle("Enviando imagem...");
            PCAB.setCancelable(false);
            PCAB.show();

            final StorageReference ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "CAB/" + "CAB---" + "Nota---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" + UUID.randomUUID().toString());
            ref.putFile(filePathNota).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CAB.this, "Nota enviada", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            PCAB.dismiss();
                            Toast.makeText(CAB.this, "Nâo foi possível enviar a nota " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            buttonSendcab.setClickable(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            PCAB.setMessage("Enviando nota " + (int) progress + "%");
                        }
                    });
            nf.setDrawingCacheEnabled(true);
            nf.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) nf.getDrawable()).getBitmap();
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
                        buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSendcab.setClickable(true);
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            if (filePathNota == null) {
                Toast.makeText(getApplicationContext(), "Não foi possível encontrar a imagem da nota", Toast.LENGTH_SHORT).show();
                nf.setImageResource(R.drawable.ph_add_image_erro);
                buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                buttonSendcab.setClickable(true);
            } else {
                Toast.makeText(getApplicationContext(), "Não foi possível encontrar a imagem do hodômetro", Toast.LENGTH_SHORT).show();
                hodometro.setImageResource(R.drawable.ph_add_image_hodometro_erro);
                buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                buttonSendcab.setClickable(true);
            }
        }
    }

    private void uploadImage2() {
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
        final StorageReference ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "CAB/" + "CAB---" + "Hodometro---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" + UUID.randomUUID().toString());
        ref.putFile(filePathAutorizacao).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CAB.this, "Imagem do hodômetro enviada", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        PCAB.dismiss();
                        Toast.makeText(CAB.this, "Não foi possível enviar a imagem do hodômetro " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSendcab.setClickable(true);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Enviando imagem do hodômetro " + (int) progress + "%");
                    }
                });
        hodometro.setDrawingCacheEnabled(true);
        hodometro.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) hodometro.getDrawable()).getBitmap();
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
                    PCAB.dismiss();
                    progressDialog.dismiss();
                    sendEmail();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível criar uma URL válida para a imagem do hodômetro", Toast.LENGTH_SHORT).show();
                    buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    buttonSendcab.setClickable(true);
                    PCAB.dismiss();
                    progressDialog.dismiss();
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void CloseKeyBoard(View z) {
        View view2 = this.getCurrentFocus();
        if (view2 != null)

        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }


    private void sendEmail() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        editTextSubject = "CAB " + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + " " + protocolo;

        String email = editTextEmail.trim();
        String subject = editTextSubject.trim();
        String message = "<!DOCTYPE html><html><body>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNomecab.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + emailUser.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Valor da nota: </strong>" + editTextSelectedValorcab.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Data da nota: </strong>" + editTextDate.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Hodômetro: </strong>" + editTextJustificativa.getText().toString().trim() + " KM</p>"
                + "<img src=\"" + stringUriNota + "\" width=\"320\" />"
                + "<img src=\"" + stringUriAutorizacao + "\" width=\"320\" />"
                + "<br />"
                + "<a href=\"" + stringUriNota + "\">Imagem da nota</a>"
                + "<br />"
                + "<a href=\"" + stringUriAutorizacao + "\">Imagem hodômetro</a>"
                ;

        //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
        hist =
                "\n" + " Nome: " + editTextNomecab.getText().toString().trim() + ";" + "\n" +
                        " Email: " + emailUser.getText().toString().trim() + ";" + "\n" +
                        " Valor da Nota: " + editTextSelectedValorcab.getText().toString().trim() + ";" + "\n" +
                        " Data: " + editTextDate.getText().toString().trim() + ";" + "\n" +
                        " Hodômetro: " + editTextJustificativa.getText().toString().trim() + ";" + "\n" +
                        " Nota: " + stringUriNota + ";" + "\n" +
                        " Imagem Hodômetro: " + stringUriAutorizacao + ";";

        save(hist, editTextSubject);
        buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        buttonSendcab.setClickable(true);
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
            /*Intent intent = new Intent(CAB.this, MainActivity.class);
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
                buttonSendcab.performClick();
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

                if (editTextNomecab.getText().toString().trim().equals("")) {
                    //Negative
                    nomeLay.setError("Insira seu Nome");
                    editTextNomecab.requestFocus();
                } else {
                    nomeLay.setError(null);
                    if (emailUser.getText().toString().trim().equals("")) {
                        //Negative
                        emailUserLay.setError("Insira seu Email");
                        emailUser.requestFocus();
                    } else {
                        emailUserLay.setError(null);
                        if (editTextSelectedValorcab.getText().toString().trim().equals("")) {
                            //Negative
                            editTextSelectedValorLAY.setError("Insira o valor da nota");
                            editTextSelectedValorcab.requestFocus();
                        } else {
                            editTextSelectedValorLAY.setError(null);
                            if (editTextDate.getText().toString().trim().equals("")) {
                                //Negative
                                editTextDateLAY.setError("Insira a data");
                                editTextDate.requestFocus();
                            } else {
                                editTextDateLAY.setError(null);
                                if (editTextJustificativa.getText().toString().trim().equals("")) {
                                    //Negative
                                    editTextJustificativaLAY.setError("Insira o valor correspondente ao Hodômetro");
                                    editTextJustificativa.requestFocus();
                                } else {
                                    editTextJustificativaLAY.setError(null);

                                    if (editTextNomecab.getText().toString().contains(";") || emailUser.getText().toString().contains(";")) {

                                        //mensagem de erro
                                        Toast.makeText(getApplicationContext(), "\";\" é um caractere inválido", Toast.LENGTH_LONG).show();
                                    } else {
                                        buttonSendcab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                                        buttonSendcab.setClickable(false);
                                        sendedmail = true;
                                        uploadImage();
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



