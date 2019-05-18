/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf.Login;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.saul.arf.MainActivity;
import com.saul.arf.MisProyectosActivity;
import com.saul.arf.RecuperarPassword.PasswordActivity;
import com.saul.arf.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    private Button btnAcceso;
    private EditText correoUsr;
    private EditText passUsr;
    private ServicioAWS servicioAWS;
    private String url_aws;
    private Button btnRecuperar;
    private ImageButton btnBack;
    private static int codigo_aws;
    private static String usuario_aws;
    private static int id_usuario;
    private static GifImageView gifImageView;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRecuperar = findViewById(R.id.btn_recuperar_pass);
        btnAcceso = findViewById(R.id.btn_acceso_login);
        correoUsr = findViewById(R.id.correo_usr_login);
        passUsr = findViewById(R.id.pwd_usr_login);
        gifImageView = findViewById(R.id.progressbar);
        btnBack=findViewById(R.id.btn_back_login);
        checkBox=findViewById(R.id.guardar_sesion);
        gifImageView.setVisibility(View.GONE);

        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             final String correo = correoUsr.getText().toString();
             final String pass =  passUsr.getText().toString();
             if(validarCorreo(correo)){
                 if(pass.isEmpty()){
                    passUsr.setError("¡Ingrese una contraseña!");
                 }else{
                     gifImageView.setVisibility(View.VISIBLE);
                     consumirServicioAWS();
                     while (true){
                         if (LoginActivity.getCodigo_aws() == 1004) {
                             imprimirMensaje("¡Bienvenido " + LoginActivity.getUsuario_aws() + "!");
                             iniciarSessionAWS();
                             if(checkBox.isChecked())
                                guardarPreferencias();
                             finish();
                             break;
                         } else if (LoginActivity.getCodigo_aws() == -5) {
                             gifImageView.setVisibility(View.GONE);
                             imprimirMensaje("¡Datos incorrectos! Revíse la información proporcionada.");
                             break;
                         } else if (LoginActivity.getCodigo_aws() == -1) {
                             gifImageView.setVisibility(View.GONE);
                             imprimirMensaje("¡Error en la conexión! Inténte más tarde." + LoginActivity.getUsuario_aws());
                             break;
                         }
                     }
                 }
             }else{
                 correoUsr.setError("¡Correo inválido!");
             }
            }
        });

        btnRecuperar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                recuperarPass();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

    }
    public void regresar(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean validarCorreo(String correo){
        String PATRON_CORREO = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern patron = Pattern.compile(PATRON_CORREO);
        Matcher matcher =  patron.matcher(correo);
        return matcher.matches();
    }

    public void consumirServicioAWS(){
        this.setCodigo_aws(0);
        this.setUsuario_aws("");
        this.setUrl_aws("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-login");
        this.setServicioAWS(new ServicioAWS(this, this.getUrl_aws(),correoUsr.getText().toString(),passUsr.getText().toString()));
        this.getServicioAWS().execute();
    }

    public void iniciarSessionAWS(){
        Intent intent = new Intent(this, MisProyectosActivity.class);
        intent.putExtra("id",LoginActivity.getId_usuario());
        startActivity(intent);
    }

    public void recuperarPass(){
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
    }

    private void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("variables_sesion",Context.MODE_PRIVATE);
        String usuarioArf = preferences.getString("user_arf","EMPTY_USER");
        int idArf = preferences.getInt("id_arf",0);
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("arf_session__variables",Context.MODE_PRIVATE);
        String usuario_arf = new String(this.getUsuario_aws());
        int id_usuario_arf = this.getId_usuario();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_arf",usuario_arf);
        editor.putInt("id_arf", id_usuario_arf);
        editor.putString("correo_usuario",correoUsr.getText().toString());
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        regresar();
    }

    public ServicioAWS getServicioAWS() {
        return servicioAWS;
    }

    public void setServicioAWS(ServicioAWS servicioAWS) {
        this.servicioAWS = servicioAWS;
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
        LoginActivity.codigo_aws = codigo_aws;
    }

    public static String getUsuario_aws() {
        return usuario_aws;
    }

    public static void setUsuario_aws(String usuario_aws ) {
        LoginActivity.usuario_aws = usuario_aws;
    }

    public static int getId_usuario() {
        return id_usuario;
    }

    public static void setId_usuario(int id_usuario) {
        LoginActivity.id_usuario = id_usuario;
    }

}