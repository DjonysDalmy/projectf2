package com.example.ibrep.projectf2.Email;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.ibrep.projectf2.Login;
import com.example.ibrep.projectf2.Pages.RCC;
import com.example.ibrep.projectf2.SQ.MainActivity;
import com.example.ibrep.projectf2.SelectPage;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Belal on 10/30/2015.
 */

//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void,Void,Void> {

    public static final Boolean send = false;

//    Multipart multipart = new MimeMultipart("related");
//    MimeBodyPart htmlPart = new MimeBodyPart();

    //Declarando Variáveis
    private Context context;
    private Session session;

    //Informações para enviar email
    private String email;
    private String subject;
    private String message;

    //'Progressdialog' para mostrar enquanto envia email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String subject, String message){
        //Inicializando variáveis
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Mostrando 'progress dialog' enquanto envia email
        progressDialog = ProgressDialog.show(context,"Enviando mensagem","Por favor espere...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Encerrando o progress dialog
        progressDialog.dismiss();
        //Mensagem de sucesso
        Toast.makeText(context,"Mensagem enviada",Toast.LENGTH_LONG).show();
    }


    @Override
    protected Void doInBackground(Void... params) {


        //Criando 'properties'
        Properties props = new Properties();

        //configurando 'properties' para gmail
        //"If you are not using gmail you may need to change the values"
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        //props.put("mail.smtp.starttls.enable","true");

        //Criando uma nova session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Autenticando a senha
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {

            /*htmlPart.setText(messageBody, "utf-8", "html");
            multipart.addBodyPart(htmlPart);
            MimeBodyPart imgPart = new MimeBodyPart();
            imgPart.attachFile();

            imgPart.setContentID("");
            multipart.addBodyPart(imgPart);*/

            //Criando MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            //Remetente
            mm.setFrom(new InternetAddress(Config.EMAIL));
            //Destinatario
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Assunto
            mm.setSubject(subject);
            //Mensagem
            mm.setContent(message, "text/html; charset=utf-8");
//            mm.setContent(multipart);



            //BodyPart messageBodyPart = new MimeBodyPart();
            //Multipart multipart = new MimeMultipart();


            //Enviando email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

