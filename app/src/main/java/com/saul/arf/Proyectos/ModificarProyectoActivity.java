package com.saul.arf.Proyectos;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saul.arf.MisProyectosActivity;
import com.saul.arf.R;
import com.saul.arf.MisProyectosAWS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ModificarProyectoActivity extends AppCompatActivity {

    private Context context;
    private int id_usuario;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_proyecto);
        context=this;
        activity=this;
        id_usuario=getIntent().getExtras().getInt("id");
        final Proyecto proyecto= (Proyecto) getIntent().getExtras().getSerializable("proyecto");
        final EditText client_firstname=findViewById(R.id.client_firstname_modificar);
        final EditText client_lastname=findViewById(R.id.client_lastname_modificar);
        final EditText client_phone=findViewById(R.id.client_phone_modificar);
        final EditText client_email=findViewById(R.id.client_email_modificar);
        Button boton_enviar_modificar=findViewById(R.id.boton_enviar_modificar);
        ImageButton boton_cancelar_modificar=findViewById(R.id.boton_cancelar_modificar);
        client_firstname.setText(proyecto.getClient_firstname(),TextView.BufferType.EDITABLE);
        client_lastname.setText(proyecto.getClient_lastname(),TextView.BufferType.EDITABLE);
        if(!proyecto.getClient_email().equals("SIN DEFINIR"))
            client_email.setText(proyecto.getClient_email(),TextView.BufferType.EDITABLE);
        client_phone.setText(proyecto.getClient_phone(),TextView.BufferType.EDITABLE);

        boton_enviar_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res_mail=false;
                String client_firstname_string=client_firstname.getText().toString();
                String client_lastname_string=client_lastname.getText().toString();
                String client_phone_string=client_phone.getText().toString();
                String client_email_string=client_email.getText().toString();
                if(client_firstname_string.isEmpty()){
                    client_firstname.setError("Campo vacio");
                }
                else if(client_lastname_string.isEmpty())
                {
                    client_lastname.setError("Campo vacio");
                }
                else if(!validarNumero(client_phone_string))
                {
                    client_phone.setError("¡Numero no Valido!");
                }
                else {
                    if(!client_email_string.isEmpty()){
                        if(!validarCorreo(client_email_string))
                            client_email.setError("¡Correo no valido!");
                        else
                            res_mail=true;
                    }
                    else
                        res_mail=true;
                    if(res_mail) {
                        Proyecto aux = new Proyecto(proyecto.getId(), client_firstname_string, client_lastname_string, client_phone_string, client_email_string);
                        MisProyectosAWS x = new MisProyectosAWS(id_usuario, proyecto.getId(), 4, aux);
                        x.execute();
                        Toast toast1 = Toast.makeText(getApplicationContext(), "Se modifico el proyecto", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.BOTTOM, 0, 0);
                        toast1.show();
                        Intent intent = new Intent(context, MisProyectosActivity.class);
                        intent.putExtra("id", id_usuario);
                        context.startActivity(intent);
                        activity.finish();
                    }
                }
            }
        });

        boton_cancelar_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private boolean validarCorreo(String correo){
        String PATRON_CORREO = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern patron = Pattern.compile(PATRON_CORREO);
        Matcher matcher =  patron.matcher(correo);
        return matcher.matches();
    }

    private boolean validarNumero(String numero){
        String PATRON_NUMERO = "[+)(]*(?:[0-9]{8,})[+)(]*";
        Pattern patron = Pattern.compile(PATRON_NUMERO);
        Matcher matcher =  patron.matcher(numero);
        return matcher.matches();
    }

    private void back(){
        Intent intent=new Intent(context,MisProyectosActivity.class);
        intent.putExtra("id",id_usuario);
        context.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

}

