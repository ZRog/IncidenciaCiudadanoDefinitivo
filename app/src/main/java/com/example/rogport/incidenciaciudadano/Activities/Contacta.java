package com.example.rogport.incidenciaciudadano.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.Mail.Config;
import com.example.rogport.incidenciaciudadano.Mail.SendMail;
import com.example.rogport.incidenciaciudadano.R;

public class Contacta extends AppCompatActivity {
    EditText motivoReporte;
    private int ID;
    private String ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contacta);

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        motivoReporte = (EditText) findViewById(R.id.mMotivoReporte);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void sendEmail(View v){
        String texto = motivoReporte.getText().toString();
        if(!texto.matches("")){

            String email = Config.EMAIL;
            String subject = "";

            String message = motivoReporte.getText().toString().trim();
            message = "Motivo contacto: " + message;

            SendMail sm = new SendMail(this, email, subject, message);
            sm.setAct(Contacta.this);
            sm.execute();

        }
        else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.faltanCampos),Toast.LENGTH_SHORT).show();
        }
    }
}
