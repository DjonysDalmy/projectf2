package com.example.ibrep.projectf2.Pages;

import android.Manifest;
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
import android.os.Build;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.storage.internal.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.example.ibrep.projectf2.SQ.Menu.INHISTORIC;
import static com.example.ibrep.projectf2.SelectPage.MY_PREFS_NAME;

public class CRC extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog galeryOrCameraNota, asksendmail;

    Boolean sendedmail;

    private String emailremetente;
    private String assunto;
    String protocolo;

    private EditText editTextNomecrc;
    TextInputLayout editTextNomeLaycrc;
    private EditText editTextEmailcrc;
    TextInputLayout editTextEmailLaycrc;
    private EditText editTextNomeAlunocrc;
    TextInputLayout editTextNomeAlunoLaycrc;
    private EditText editTextNumeroMatriculacrc;
    TextInputLayout editTextNumeroMatriculaLaycrc;
    private EditText editTextQuantChequescrc;
    TextInputLayout editTextQuantChequesLaycrc;
    private ImageView Nota;


    FirebaseStorage storageNota;
    StorageReference storageReferenceNota;
    private Uri filePathNota;
    String stringUriNota;

    private FloatingActionButton buttonSendcrc;

    String hist;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crc);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sendedmail = false;

        storageNota = FirebaseStorage.getInstance();
        storageReferenceNota = storageNota.getReference();

        editTextNomeAlunoLaycrc = findViewById(R.id.NomeAlunolaycrc);
        editTextEmailLaycrc = findViewById(R.id.Emaillaycrc);
        editTextNomeLaycrc = findViewById(R.id.Nomelaycrc);
        editTextNumeroMatriculaLaycrc = findViewById(R.id.NumeroMatriculalaycrc);
        editTextQuantChequesLaycrc = findViewById(R.id.SelectedQuantChequeslaycrc);

        emailremetente = Config.EMAILTO;
        assunto = "CRC"; //Assunto
        editTextNomecrc = findViewById(R.id.Nomecrc);
        editTextEmailcrc = findViewById(R.id.Emailcrc);
        editTextNomeAlunocrc = findViewById(R.id.NomeAlunocrc);
        editTextNumeroMatriculacrc = findViewById(R.id.NumeroMatriculacrc);
        editTextQuantChequescrc = findViewById(R.id.SelectedQuantChequescrc);
        Nota = findViewById(R.id.imgViewcrc);
        Nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                if (filePathNota == null) {
                    Nota.setImageResource(R.drawable.cheques);
                }
                Ch1();
            }
        });

        editTextQuantChequescrc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    Nota.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            /*if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else */if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    filePathNota = imageUri;
                    Nota.setImageURI(filePathNota);
                    // Update UI to reflect image being shared
                } //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    filePathNota = imageUri;
                    Nota.setImageURI(filePathNota);
                    Nota.setImageResource(R.drawable.iconpdf);
                }
            }
        }  else {
            // Handle other intents, such as being started from the home screen
        }


        buttonSendcrc = findViewById(R.id.buttonSendcrc);
        buttonSendcrc.setOnClickListener(this);

        editTextNomecrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNomeLaycrc.setError(null);
            }
        });
        editTextEmailcrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmailLaycrc.setError(null);
            }
        });
        editTextNomeAlunocrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNomeAlunoLaycrc.setError(null);
            }
        });
        editTextNumeroMatriculacrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNumeroMatriculaLaycrc.setError(null);
            }
        });
        editTextQuantChequescrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextQuantChequesLaycrc.setError(null);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String namecheck = prefs.getString("name", null);
        if (namecheck == null | namecheck.equals("No name defined")) {
            editTextNomecrc.requestFocus();
        } else {
            editTextNomecrc.setText(prefs.getString("name", "No name defined"));
            editTextNomeAlunocrc.requestFocus();
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        String emailcheck = settings.getString("String", null);
        if (emailcheck == null | emailcheck.equals("No email defined")) {
            finish();
            System.exit(0);
        } else {
            editTextEmailcrc.setText(settings.getString("String", null));
        }
    }

    public void Ch1() {
        ActivityCompat.requestPermissions(CRC.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }





//    String currentPhotoPath;
//    private File createImageFile() throws IOException {
//         Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
                // Error occurred while creating the File
            //...
//            }
            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }

//    }

    /*Uri uri;

    public void usarCamera(View view) {

        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = new File(diretorio.getPath() + "/" + System.currentTimeMillis() + ".jpg");
        uri  = Uri.fromFile(imagem);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentCamera, 0);
    }


    View view;*/

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
                        builder.setMessage("Imagem dos cheques");


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
                                startActivityForResult(Intent.createChooser(intent, "Select"), 2);
                            }
                        });

                        builder.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                dispatchTakePictureIntent();

                                //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                                //Toast.makeText(RCC.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                            }
                        });

                        //cria o AlertDialog
                        galeryOrCameraNota = builder.create();
                        //Exibe
                        galeryOrCameraNota.show();
                    } else {
                        Toast.makeText(CRC.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();
                    }
                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CRC.this, "Você deve conceder permissão para utilizar esse recurso", Toast.LENGTH_SHORT).show();
                }
                //return;
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    File imgFile = new File(currentPhotoPath);
                    Bitmap selectedImageb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Nota.setImageBitmap(selectedImageb);
                    filePathNota = Uri.fromFile(imgFile);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    //Galeria
                    Uri selectedImage = imageReturnedIntent.getData();
                    Nota.setImageURI(selectedImage);
                    filePathNota = selectedImage;
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Nota.setImageResource(R.drawable.iconpdf);
                    filePathNota = selectedImage;
                }
                break;
        }
    }

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

        protocolo =  numero1 + numero2 + data_completa.substring(0, 2) + data_completa.substring(3, 5) + data_completa.substring(11, 13) + data_completa.substring(14, 16)+ data_completa.substring(17, 19) + "0" + versionCode;

        if (filePathNota != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Enviando imagem...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "CRC/" + "CRC---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)),settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" + UUID.randomUUID().toString());
            ref.putFile(filePathNota).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CRC.this, "Cheques enviados", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CRC.this, "Nâo foi possível enviar os cheques " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonSendcrc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            buttonSendcrc.setClickable(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Enviando cheques " + (int) progress + "%");
                        }
                    });
            Nota.setDrawingCacheEnabled(true);
            Nota.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) Nota.getDrawable()).getBitmap();
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
                        progressDialog.dismiss();
                        sendEmail();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não foi possível criar uma URL válida para os cheques", Toast.LENGTH_SHORT).show();
                        buttonSendcrc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSendcrc.setClickable(true);
                        progressDialog.dismiss();
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Não foi possível encontrar a imagem dos cheques", Toast.LENGTH_SHORT).show();
            Nota.setImageResource(R.drawable.cheque_erro);
            buttonSendcrc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            buttonSendcrc.setClickable(true);
        }
    }

    private void sendEmail() {
        //String Teste1;
        //Teste1 = stringUriNota.toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        Log.i("data_completa", data_completa);

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        assunto = "CRC " + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + " " + protocolo;


        String email = emailremetente.trim();
        String subject = assunto.trim();
        String message = "<!DOCTYPE html><html><body>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + editTextNomeAlunocrc.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">N° Matrícula: </strong>" + editTextNumeroMatriculacrc.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Quantidade de cheques: </strong>" + editTextQuantChequescrc.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNomecrc.getText().toString().trim() + "</p>"
                + "<p style=\"font-size: 10\"><strong style=\" color: blue;\">Email: </strong>" + editTextEmailcrc.getText().toString().trim() + "</p>"
                + "<img src=\"" + stringUriNota + "\" width=\"400\" />"
                + "<br />"
                + "<a href=\"" + stringUriNota + "\">Cheques</a>";

        //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
        hist =
                "\n" + " Nome do aluno: " + editTextNomeAlunocrc.getText().toString().trim() + ";" + "\n" +
                        " Número da matrícula: " + editTextNumeroMatriculacrc.getText().toString().trim() + ";" + "\n" +
                        " Quantidade de cheques: " + editTextQuantChequescrc.getText().toString().trim() + ";" + "\n" +
                        " Nome: " + editTextNomecrc.getText().toString().trim() + ";" + "\n" +
                        " Email: " + editTextEmailcrc.getText().toString().trim() + ";" + "\n" +
                        " Cheque: " + stringUriNota + ";";
        save(hist, assunto);
        buttonSendcrc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        buttonSendcrc.setClickable(true);
    }

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
            /*Intent intent = new Intent(CRC.this, MainActivity.class);
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
                buttonSendcrc.performClick();
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

                if (editTextNomecrc.getText().toString().trim().equals("")) {
                    //Negative
                    editTextNomeLaycrc.setError("Insira seu nome");
                    editTextNomecrc.requestFocus();
                } else {
                    editTextNomeLaycrc.setError(null);
                    if (editTextEmailcrc.getText().toString().trim().equals("")) {
                        //Negative
                        editTextEmailLaycrc.setError("Insira seu Email");
                        editTextEmailcrc.requestFocus();
                    } else {
                        editTextEmailLaycrc.setError(null);
                        if (editTextNomeAlunocrc.getText().toString().trim().equals("")) {
                            //Negative
                            editTextNomeAlunoLaycrc.setError("Insira o nome do aluno");
                            editTextNomeAlunocrc.requestFocus();
                        } else {
                            editTextNomeAlunoLaycrc.setError(null);
                            if (editTextNumeroMatriculacrc.getText().toString().trim().equals("")) {
                                editTextNumeroMatriculaLaycrc.setError("Insira o número da matricula");
                                editTextNumeroMatriculacrc.requestFocus();
                            } else {
                                editTextNumeroMatriculaLaycrc.setError(null);
                                if (editTextQuantChequescrc.getText().toString().trim().equals("")) {
                                    //Negative
                                    editTextQuantChequesLaycrc.setError("Insira a quantidade de cheques");
                                    editTextQuantChequescrc.requestFocus();
                                } else {
                                    if (editTextNomecrc.getText().toString().contains(";") || editTextEmailcrc.getText().toString().contains(";") || editTextNomeAlunocrc.getText().toString().contains(";") || editTextNumeroMatriculacrc.getText().toString().contains(";") || editTextQuantChequescrc.getText().toString().contains(";")) {
                                        //mensagem de erro
                                        Toast.makeText(getApplicationContext(),"\";\" é um caractere inválido", Toast.LENGTH_LONG).show();
                                    } else {
                                        editTextQuantChequesLaycrc.setError(null);
                                        buttonSendcrc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                                        buttonSendcrc.setClickable(false);
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

    public void CloseKeyBoard(View z) {
        View view2 = this.getCurrentFocus();
        if (view2 != null)

        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
}