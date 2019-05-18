/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf.RecuperarPassword;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.saul.arf.Login.LoginActivity;
import com.saul.arf.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.droidsonroids.gif.GifImageView;

public class ResetActivity extends AppCompatActivity {

    private String type;
    public static String emailUser;
    private static String send_code_aws;
    private Context http;
    private String url;
    private String new_password;
    private ResetPasswordAWS resetPassword;
    private static int request_code_aws;
    public static String mensaje;
    private Button btn_enviar_password;
    private Button btn_salir;
    private EditText editTextNewPassword;
    private EditText editText_NewPasswordConfirm;
    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        btn_enviar_password = findViewById(R.id.btn_eviar_correo);
        btn_salir = findViewById(R.id.btnCancelar);
        editTextNewPassword = findViewById(R.id.new_pass_text);
        editText_NewPasswordConfirm = findViewById(R.id.new_pass_text_confirm);
        gifImageView = findViewById(R.id.progressbar1);
        gifImageView.setVisibility(View.GONE);

        btn_enviar_password.setOnClickListener((View view) -> {
            final String contrasenia = editTextNewPassword.getText().toString();
            final String confirmContrasenia = editText_NewPasswordConfirm.getText().toString();
            if(!validarPassword(contrasenia)){
                editTextNewPassword.setError("Ingrese al menos un dígito, una letra mayúscula y un carácter especial.");
            }else if(!validarPassword(confirmContrasenia)){
                editText_NewPasswordConfirm.setError("Ingrese al menos un dígito, una letra mayúscula y un carácter especial.");
            }else{
                if(contrasenia.equals(confirmContrasenia)){
                    this.gifImageView.setVisibility(View.VISIBLE);
                    this.setNew_password(contrasenia);
                    ResetActivity.setRequest_code_aws(0);
                    enviarNuevaContrasenia();
                    while(true){
                        if(ResetActivity.getRequest_code_aws() == 1002){
                            imprimirMensaje(ResetActivity.mensaje+" ¡Inicie sesion nuevamente!");
                            irInicioSession();
                            finish();
                            break;
                        }else if (ResetActivity.getRequest_code_aws()<0){
                            imprimirMensaje("¡Error en petición, intente mas tarde!");
                            break;
                        }else{
                            continue;
                        }
                    }
                }else{
                    imprimirMensaje("¡Error, las contraseñas no coincíden, intente nuevamente!");
                }
            }
        });

        btn_salir.setOnClickListener((View view) -> {
            finish();
        });
    }

    public void enviarNuevaContrasenia(){
        this.setUrl("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-recover-account");
        this.setType("reset");
        this.setResetPassword(new ResetPasswordAWS(this,this.getUrl(),this.getType(),ResetActivity.emailUser,this.getSend_code_aws(),this.getNew_password()));
        this.getResetPassword().execute();
    }

    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence texto = mensaje;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, texto, duration);
        toast.show();
    }

    private boolean validarPassword(String pass) {
        String PATRON_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern patron = Pattern.compile(PATRON_PASSWORD);
        Matcher matcher = patron.matcher(pass);
        return matcher.matches();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String getSend_code_aws() {
        String codigo_recuperacion =ResetActivity.send_code_aws;
        return codigo_recuperacion;
    }

    public static void setSend_code_aws(String send_code_aws) {
        ResetActivity.send_code_aws = send_code_aws;
    }

    public Context getHttp() {
        return http;
    }

    public void setHttp(Context http) {
        this.http = http;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public ResetPasswordAWS getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(ResetPasswordAWS resetPassword) {
        this.resetPassword = resetPassword;
    }

    public static int getRequest_code_aws() {
        return request_code_aws;
    }

    public static void setRequest_code_aws(int request_code_aws) {
        ResetActivity.request_code_aws = request_code_aws;
    }

    public void irInicioSession(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
