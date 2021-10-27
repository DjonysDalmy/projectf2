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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CRD extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog galeryOrCameraNota, asksendmail;

    Boolean sendedmail;

    private String emailremetente;
    private String assunto;

    String protocolo;

    private EditText editTextNomecrd;
    TextInputLayout editTextNomeLaycrd;
    private EditText editTextEmailcrd;
    TextInputLayout editTextEmailLaycrd;
    private EditText editTextNomeAlunocrd;
    TextInputLayout editTextNomeAlunoLaycrd;
    private EditText editTextNumeroMatriculacrd;
    TextInputLayout editTextNumeroMatriculaLaycrd;
    private EditText editTextValorcrd;
    TextInputLayout editTextValorLaycrd;
    private ImageView Nota;

    private EditText editTextDatecrd;
    TextInputLayout editTextDateLaycrd;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    FirebaseStorage storageNota;
    StorageReference storageReferenceNota;
    private Uri filePathNota;
    String stringUriNota;

    private FloatingActionButton buttonSendcrd;

    String hist;
    Boolean CCCAceso;
    Switch CCCswitch;

    SharedPreferences CCC;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crd);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        CCC = getSharedPreferences("CCC", MODE_PRIVATE);

        SharedPreferences emailverify = getSharedPreferences(Login.PREFS_NAME, 0);
        if (emailverify.getString("String", null).equals("bruna.baptista@ibrep.com.br")) {
            CCCAceso = true;
        } else {
            CCCAceso = CCC.getBoolean("CCC", false);
        }
        CCCswitch = findViewById(R.id.CCCswitch);

        if (CCCAceso) {
            CCCswitch.setVisibility(View.VISIBLE);
        } else {
            CCCswitch.setVisibility(View.GONE);
        }

        CCCswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CCCswitch.isChecked()) {
                    editTextDateLaycrd.setHint("Data do cartão*");
                    Nota.setImageResource(R.drawable.ph_add_image3);
                } else {
                    editTextDateLaycrd.setHint("Data do depósito*");
                    Nota.setImageResource(R.drawable.ph_add_image);
                }
            }
        });

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sendedmail = false;

        storageNota = FirebaseStorage.getInstance();
        storageReferenceNota = storageNota.getReference();

        editTextNomeAlunoLaycrd = findViewById(R.id.NomeAlunolaycrd);
        editTextEmailLaycrd = findViewById(R.id.Emaillaycrd);
        editTextNomeLaycrd = findViewById(R.id.Nomelaycrd);
        editTextNumeroMatriculaLaycrd = findViewById(R.id.NumeroMatriculalaycrd);
        editTextValorLaycrd = findViewById(R.id.SelectedValorlaycrd);
        editTextDateLaycrd = findViewById(R.id.SelectedDatelaycrd);

        emailremetente = Config.EMAILTO;
        assunto = "CRD"; //Assunto
        editTextNomecrd = findViewById(R.id.Nomecrd);
        editTextEmailcrd = findViewById(R.id.Emailcrd);
        editTextNomeAlunocrd = findViewById(R.id.NomeAlunocrd);
        editTextNumeroMatriculacrd = findViewById(R.id.NumeroMatriculacrd);
        editTextValorcrd = findViewById(R.id.SelectedValorcrd);
        editTextDatecrd = findViewById(R.id.SelectedDatecrd);
        Nota = findViewById(R.id.imgViewcrd);
        Nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CloseKeyBoard(arg0);
                if (filePathNota == null) {
                    if (CCCswitch.isChecked()) {
                        editTextDateLaycrd.setHint("Data do cartão*");
                        Nota.setImageResource(R.drawable.ph_add_image3);
                    } else {
                        editTextDateLaycrd.setHint("Data do depósito*");
                        Nota.setImageResource(R.drawable.ph_add_image);
                    }
                }
                Ch1();
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
                    Nota.setImageResource(R.drawable.iconpdf);
                }
            }
        }  else {
            // Handle other intents, such as being started from the home screen
        }

        editTextDatecrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard(view);
                editTextDateLaycrd.setError(null);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CRD.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                editTextDatecrd.setText(day + "/" + (month + 1) + "/" + year);
                                editTextDateLaycrd.setError(null);
                                Nota.performClick();
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        editTextValorcrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    editTextDatecrd.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        buttonSendcrd = findViewById(R.id.buttonSendcrd);
        buttonSendcrd.setOnClickListener(this);

        editTextNomecrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNomeLaycrd.setError(null);
            }
        });
        editTextEmailcrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmailLaycrd.setError(null);
            }
        });
        editTextNomeAlunocrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNomeAlunoLaycrd.setError(null);
            }
        });
        editTextNumeroMatriculacrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextNumeroMatriculaLaycrd.setError(null);
            }
        });
        editTextValorcrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextValorLaycrd.setError(null);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String namecheck = prefs.getString("name", null);
        if (namecheck == null | namecheck.equals("No name defined")) {
            editTextNomecrd.requestFocus();
        } else {
            editTextNomecrd.setText(prefs.getString("name", "No name defined"));
            editTextNomeAlunocrd.requestFocus();
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        String emailcheck = settings.getString("String", null);
        if (emailcheck == null | emailcheck.equals("No email defined")) {
            finish();
            System.exit(0);
        } else {
            editTextEmailcrd.setText(settings.getString("String", null));
        }
    }

    public void Ch1() {
        ActivityCompat.requestPermissions(CRD.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                        builder.setMessage("Imagem da nota");


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
                            }
                        });

                        //cria o AlertDialog
                        galeryOrCameraNota = builder.create();
                        //Exibe
                        galeryOrCameraNota.show();
                    } else {
                        Toast.makeText(CRD.this, "Você não permitiu o uso deste recurso", Toast.LENGTH_SHORT).show();

                    }
                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CRD.this, "Você deve conceder permissão para utilizar esse recurso", Toast.LENGTH_SHORT).show();
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
                    //Câmera
//                    Bundle extras = imageReturnedIntent.getExtras();
//                    if (extras != null) {
//                        imageBitmap = (Bitmap) extras.get("data");
//                        Nota.setImageBitmap(imageBitmap);
//                    Uri selectedImage = data.getData();
//                    imgViewNota.setImageURI(selectedImage);
//                    filePathNota = selectedImage;
//                    }
//                    filePathNota = getImageUri(this, imageBitmap);
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

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

        protocolo = numero1 + numero2 + data_completa.substring(0, 2) + data_completa.substring(3, 5) + data_completa.substring(11, 13) + data_completa.substring(14, 16)+ data_completa.substring(17, 19) + "0" + versionCode;

        if (filePathNota != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Enviando imagem...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference ref;
            if (CCCswitch.isChecked()) {
                ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "CCC/" + "CCC---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" + UUID.randomUUID().toString());
            } else {
                ref = storageReferenceNota.child("images/" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "/" + "CRD/" + "CRD---" + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + "---" + protocolo + "---" + UUID.randomUUID().toString());
            }
            ref.putFile(filePathNota).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CRD.this, "Nota enviada", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CRD.this, "Nâo foi possível enviar a nota " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonSendcrd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            buttonSendcrd.setClickable(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Enviando nota " + (int) progress + "%");
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
                        Toast.makeText(getApplicationContext(), "Não foi possível criar uma URL válida para a nota", Toast.LENGTH_SHORT).show();
                        buttonSendcrd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        buttonSendcrd.setClickable(true);
                        progressDialog.dismiss();
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Não foi possível encontrar a imagem da nota", Toast.LENGTH_SHORT).show();
            if (CCCswitch.isChecked()) {
                editTextDateLaycrd.setHint("Data do cartão*");
                Nota.setImageResource(R.drawable.ph_add_image_erro3);
            } else {
                editTextDateLaycrd.setHint("Data do depósito*");
                Nota.setImageResource(R.drawable.ph_add_image_erro);
            }
            buttonSendcrd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            buttonSendcrd.setClickable(true);
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

        String type;
        String message;

        if (CCCswitch.isChecked()) {
            //editTextDateLaycrd.setHint("Data do cartão*");
            //Nota.setImageResource(R.drawable.ph_add_image3);
            type = "CCC ";
            message = "<!DOCTYPE html><html><body>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data do cartão: </strong>" + editTextDatecrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor: </strong>" + editTextValorcrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + editTextNomeAlunocrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">N° Matricula: </strong>" + editTextNumeroMatriculacrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNomecrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + editTextEmailcrd.getText().toString().trim() + "</p>"
                    + "<img src=\"" + stringUriNota + "\" width=\"400\" />"
                    + "<br />"
                    + "<a href=\"" + stringUriNota + "\">Imagem do comprovante</a>";
        } else {
            //editTextDateLaycrd.setHint("Data do depósito*");
            //Nota.setImageResource(R.drawable.ph_add_image);
            type = "CRD ";
            message = "<!DOCTYPE html><html><body>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Data do depósito: </strong>" + editTextDatecrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Valor: </strong>" + editTextValorcrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome do aluno: </strong>" + editTextNomeAlunocrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">N° Matricula: </strong>" + editTextNumeroMatriculacrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Nome: </strong>" + editTextNomecrd.getText().toString().trim() + "</p>"
                    + "<p style=\"font-size: 8\"><strong style=\" color: blue;\">Email: </strong>" + editTextEmailcrd.getText().toString().trim() + "</p>"
                    + "<img src=\"" + stringUriNota + "\" width=\"400\" />"
                    + "<br />"
                    + "<a href=\"" + stringUriNota + "\">Imagem da nota</a>";
        }

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        assunto = type + (settings.getString("String", null).substring(settings.getString("String", null).indexOf(settings.getString("String", null).charAt(0)), settings.getString("String", null).indexOf("@", settings.getString("String", null).indexOf("@")))) + " " + protocolo;


        String email = emailremetente.trim();
        String subject = assunto.trim();

        //        String message = "<!DOCTYPE html><html><body><img src=\"-----CODEHERE------\">";

        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
        hist =
                "\n" + " Data da nota: " + editTextDatecrd.getText().toString().trim() + ";" + "\n" +
                        " Nome do aluno: " + editTextNomeAlunocrd.getText().toString().trim() + ";" + "\n" +
                        " Número da matrícula: " + editTextNumeroMatriculacrd.getText().toString().trim() + ";" + "\n" +
                        " Valor: R$ " + editTextValorcrd.getText().toString().trim() + ";" + "\n" +
                        " Nome: " + editTextNomecrd.getText().toString().trim() + ";" + "\n" +
                        " Email: " + editTextEmailcrd.getText().toString().trim() + ";" + "\n" +
                        " Nota: " + stringUriNota + ";";
        save(hist, assunto);
        buttonSendcrd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        buttonSendcrd.setClickable(true);
    }

    private void save(String name, String assunto) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        boolean saved = db.add(name, assunto);

        if (saved) {
            Toast.makeText(this, "Salvo no dispositivo", Toast.LENGTH_SHORT).show();
            SharedPreferences ISHISTORIC = getSharedPreferences(INHISTORIC, MODE_PRIVATE);
            SharedPreferences.Editor editor = ISHISTORIC.edit();
            editor.putBoolean("haveAnything", true);
            editor.apply();
            /*Intent intent = new Intent(CRD.this, MainActivity.class);
            startActivity(intent);*/

//            nameEditText.setText("");
//            getSpacecrafts();
        } else {
            Toast.makeText(this, "Não foi possível salvar no dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void asksendmail() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirme sua solicitação");
        //define a mensagem
        builder.setMessage("Deseja enviar esses dados?");


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                sendedmail = false;
                buttonSendcrd.performClick();
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

                if (editTextNomecrd.getText().toString().trim().equals("")) {
                    //Negative
                    editTextNomeLaycrd.setError("Insira seu nome");
                    editTextNomecrd.requestFocus();
                } else {
                    editTextNomeLaycrd.setError(null);
                    if (editTextEmailcrd.getText().toString().trim().equals("")) {
                        //Negative
                        editTextEmailLaycrd.setError("Insira seu Email");
                        editTextEmailcrd.requestFocus();
                    } else {
                        editTextEmailLaycrd.setError(null);
                        if (editTextNomeAlunocrd.getText().toString().trim().equals("")) {
                            //Negative
                            editTextNomeAlunoLaycrd.setError("Insira o nome do aluno");
                            editTextNomeAlunocrd.requestFocus();
                        } else {
                            editTextNomeAlunoLaycrd.setError(null);
                            if (editTextNumeroMatriculacrd.getText().toString().trim().equals("")) {
                                editTextNumeroMatriculaLaycrd.setError("Insira o número da matricula");
                                editTextNumeroMatriculacrd.requestFocus();
                            } else {
                                editTextNumeroMatriculaLaycrd.setError(null);
                                if (editTextValorcrd.getText().toString().trim().equals("")) {
                                    //Negative
                                    editTextValorLaycrd.setError("Insira um valor");
                                    editTextValorcrd.requestFocus();
                                } else {
                                    editTextValorLaycrd.setError(null);
                                    if (editTextDatecrd.getText().toString().trim().equals("")) {
                                        //Negative
                                        editTextDateLaycrd.setError("Insira a data");
                                        editTextDatecrd.requestFocus();
                                    } else {
                                        if (editTextNomecrd.getText().toString().contains(";") || editTextEmailcrd.getText().toString().contains(";") || editTextDatecrd.getText().toString().contains(";") || editTextNomeAlunocrd.getText().toString().contains(";") || editTextNumeroMatriculacrd.getText().toString().contains(";") || editTextValorcrd.getText().toString().contains(";")) {

                                            //mensagem de erro
                                            Toast.makeText(getApplicationContext(),"\";\" é um caractere inválido", Toast.LENGTH_LONG).show();
                                        } else {
                                            editTextDateLaycrd.setError(null);
                                            buttonSendcrd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                                            buttonSendcrd.setClickable(false);
                                            sendedmail = true;
                                            uploadImage();
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

    public void CloseKeyBoard(View z) {
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
}