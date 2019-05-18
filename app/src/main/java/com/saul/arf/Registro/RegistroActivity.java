/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf.Registro;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saul.arf.Login.LoginActivity;
import com.saul.arf.MainActivity;
import com.saul.arf.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.os.SystemClock.sleep;

public class RegistroActivity extends AppCompatActivity {
    private ImageButton btnRegresar;
    private Button btnRegistrarUsr;
    private EditText editTextNombre;
    private EditText editTextCorreo;
    private EditText editTextPass;
    private EditText editTextConfirmPass;
    private RegistroAWS registroAWS;
    private String url_aws;
    private static int codigo_aws;
    private static String usuario_aws;
    private static String mensajeRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnRegistrarUsr = findViewById(R.id.btn_acceso_reg);
        btnRegresar = findViewById(R.id.btn_back_reg);

        editTextNombre = findViewById(R.id.nombre_usr_reg);
        editTextCorreo = findViewById(R.id.correo_usr_reg);
        editTextPass = findViewById(R.id.pwd_usr_reg);
        editTextConfirmPass = findViewById(R.id.pass_usr_repeat);
        this.setCodigo_aws(-1);
        this.setUsuario_aws("");

        btnRegistrarUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String cadena_nombre = editTextNombre.getText().toString();
                final String cadena_email = editTextCorreo.getText().toString();
                final String cadena_pass = editTextPass.getText().toString();
                final String cadena_confirmacion_pass = editTextConfirmPass.getText().toString();

                if(!validarNombre(cadena_nombre)){
                    editTextNombre.setError("¡Nombre no válido!");
                }else if(!validarCorreo(cadena_email)){
                    editTextCorreo.setError("¡Correo no valido!");
                }else if(!validarPassword(cadena_pass)){
                    editTextPass.setError("Ingrese al menos un dígito, una létra mayúscula y un carácter especial.(@#$%^&+=)");
                } else {
                    if(cadena_confirmacion_pass.equals(cadena_pass)){
                        consumirServicioAWS();
                        System.out.println(RegistroActivity.getMensajeRegistro());
                        if(RegistroActivity.getMensajeRegistro().equals("Usuario registrado correctamente")){
                            imprimirMensaje("Usuario registrado correctamente. ¡inície sesión!");
                            IniciarSession();
                            finish();
                        }else if(RegistroActivity.getMensajeRegistro().equals("El usuario ya existe.")){
                            imprimirMensaje("Usuario ya existente,¡verifique su correo!");
                        }else {
                            imprimirMensaje("Error en los datos, ¡verifique nuevamente!");
                        }
                    }else{
                        imprimirMensaje("Las contraseñas no coinciden, ¡verifíque nuevamente!");
                    }
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

    }

    private void back(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private boolean validarNombre(String nombre){
        String PATRON_NOMBRE = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$";
        Pattern patron = Pattern.compile(PATRON_NOMBRE);
        Matcher matcher =  patron.matcher(nombre);
        return matcher.matches();
    }

    private boolean validarCorreo(String correo){
        String PATRON_CORREO = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern patron = Pattern.compile(PATRON_CORREO);
        Matcher matcher =  patron.matcher(correo);
        return matcher.matches();
    }

    private boolean validarPassword(String pass) {
        String PATRON_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern patron = Pattern.compile(PATRON_PASSWORD);
        Matcher matcher = patron.matcher(pass);
        return matcher.matches();
    }


    public void consumirServicioAWS(){
        this.setUrl_aws("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-register");
        this.setRegistroAWS(new RegistroAWS(this, this.getUrl_aws(),editTextNombre.getText().toString(),editTextCorreo.getText().toString(),editTextPass.getText().toString()));
        this.getRegistroAWS().execute();
        sleep(4000);
    }

    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void IniciarSession(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public String getUrl_aws() {
        return url_aws;
    }

    public void setUrl_aws(String url_aws) {
        this.url_aws = url_aws;
    }

    public static int getCodigo_aws() {
        return codigo_aws;
    }

    public static void setCodigo_aws(int codigo_aws) {
        RegistroActivity.codigo_aws = codigo_aws;
    }

    public static String getUsuario_aws() {
        return usuario_aws;
    }

    public static void setUsuario_aws(String usuario_aws) {
        RegistroActivity.usuario_aws = usuario_aws;
    }

    public static String getMensajeRegistro() {
        return mensajeRegistro;
    }

    public static void setMensajeRegistro(String mensajeRegistro) {
        RegistroActivity.mensajeRegistro = mensajeRegistro;
    }

    public RegistroAWS getRegistroAWS() {
        return registroAWS;
    }

    public void setRegistroAWS(RegistroAWS registroAWS) {
        this.registroAWS = registroAWS;
    }
}