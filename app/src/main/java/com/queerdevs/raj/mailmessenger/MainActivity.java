package com.queerdevs.raj.mailmessenger;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import queer.mail.mailmessenger.Gmail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    EditText toemail,email,pass,sub,msg;
    Button send;
    public String emails,subs,msgs,toemails,pwd;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toemail = (EditText)findViewById(R.id.toemail);
        sub = (EditText)findViewById(R.id.sub);
        msg = (EditText)findViewById(R.id.msg);
        send = (Button)findViewById(R.id.send);
        pdialog = new ProgressDialog(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toemails = toemail.getText().toString();

                subs=sub.getText().toString();
                msgs=msg.getText().toString();
                new SendMail().execute();
            }
        });

    }

    private class SendMail extends AsyncTask<Void,Void,Void> {
        /* public SendMail(EditText email, EditText sub, EditText msg) {
         }*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.show();
        }

        private Session session;

        @Override
        protected Void doInBackground(Void... params) {
            Properties props = new Properties();

            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            //Creating a new session
            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(Gmail.email,Gmail.pwd);
                        }
                    });
            try {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress(Gmail.email));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(toemails));
                //Adding subject
                mm.setSubject(subs);
                //Adding message
                mm.setText(msgs);

                //Sending email
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void avoid) {
            //Showing progress dialog while sending email
            pdialog.dismiss();
            toemail.setText("");
            sub.setText("");
            msg.setText("");
            Toast.makeText(MainActivity.this,"Messege sent",Toast.LENGTH_SHORT).show();
        }
    }
}
