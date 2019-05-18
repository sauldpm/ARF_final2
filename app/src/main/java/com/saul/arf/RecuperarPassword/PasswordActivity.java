/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf.RecuperarPassword;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.saul.arf.R;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.droidsonroids.gif.GifImageView;

public class PasswordActivity extends AppCompatActivity {

    private Context http;
    private String url;
    private String emailUser;
    private String type;
    private JSONObject user_json_aws;
    private String send_code_aws;
    private static int request_code_aws;
    static String mensaje;
    private Button btn_enviar_correo;
    private Button btn_salir;
    private EditText editText_correo;
    private RecuperarPasswordAWS recuperarPasswordAWS;
    private ObtenerCodigoAWS obtenerCodigoAWS;
    private EditText editText_codigo;
    private Button btn_envia_codigo;
    private Button btn_cancela_codigo;
    private GifImageView gifImageView;
    private GifImageView gifImageView2;

    public PasswordActivity(){
        this.setHttp(http);
        this.setEmailUser("");
        this.setUser_json_aws(new JSONObject());
        this.setSend_code_aws("");
        PasswordActivity.setRequest_code_aws(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        editText_correo = findViewById(R.id.editText_correo_recovery);
        btn_enviar_correo = findViewById(R.id.btn_eviar_correo);
        btn_salir = findViewById(R.id.btnCancelar);
        editText_codigo =  findViewById(R.id.editText_codigo);
        btn_envia_codigo = findViewById(R.id.btn_eviar_codigo);
        btn_cancela_codigo = findViewById(R.id.btn_cancelar_codigo);
        editText_codigo.setVisibility(View.GONE);
        btn_envia_codigo.setVisibility(View.GONE);
        btn_cancela_codigo.setVisibility(View.GONE);
        gifImageView = findViewById(R.id.progressbar);
        gifImageView.setVisibility(View.GONE);
        gifImageView2 = findViewById(R.id.progressbar2);
        gifImageView2.setVisibility(View.GONE);

        btn_enviar_correo.setOnClickListener((View view) -> {
            final String correo = editText_correo.getText().toString();
            this.setEmailUser(correo);
            System.out.println("CORREO desde Onclick" +this.getEmailUser()+" correo original: "+correo);
            if(validarCorreo(correo)){
                this.gifImageView.setVisibility(View.VISIBLE);
                PasswordActivity.setRequest_code_aws(0);
                RecuperarPasswordAWS();
                while(true){

                    if(PasswordActivity.getRequest_code_aws() == -3){
                        imprimirMensaje("¡No se encontró ninguna cuenta con ese correo!");
                        break;
                    }else if(PasswordActivity.getRequest_code_aws() == 1000 ){
                        imprimirMensaje("¡Código enviado a su bandeja de entrada!");
                        break;
                    }
                }
                if(PasswordActivity.getRequest_code_aws() == 1000 ){
                    editText_correo.setFocusable(false);
                    gifImageView.setVisibility(View.GONE);
                    btn_enviar_correo.setVisibility(View.GONE);
                    btn_salir.setVisibility(View.GONE);
                    editText_codigo.setVisibility(View.VISIBLE);
                    btn_envia_codigo.setVisibility(View.VISIBLE);
                    btn_cancela_codigo.setVisibility(View.VISIBLE);
                }else{
                    gifImageView.setVisibility(View.GONE);
                }
            }else{
                editText_correo.setError("¡Correo no valido!");
            }
        });

        btn_envia_codigo.setOnClickListener((View view) -> {
            final String codigoRecuperacion = editText_codigo.getText().toString();
            if(!editText_codigo.getText().toString().isEmpty()){
                gifImageView2.setVisibility(View.VISIBLE);
                this.setSend_code_aws(codigoRecuperacion);
                enviarCodigoAWS();
                PasswordActivity.setRequest_code_aws(0);
                System.out.println("CODIGO:"+PasswordActivity.getRequest_code_aws()+" PASS"+PasswordActivity.getMensaje() );
                while(true){
                    if ( PasswordActivity.getRequest_code_aws() == -3){
                        gifImageView2.setVisibility(View.GONE);
                        imprimirMensaje("¡"+PasswordActivity.getMensaje()+", Intente de nuevo!");
                        break;
                    }
                    if ( PasswordActivity.getRequest_code_aws() == -4){
                        gifImageView2.setVisibility(View.GONE);
                        imprimirMensaje("¡"+PasswordActivity.getMensaje()+", Intente más tarde!");
                        break;
                    }
                    if(PasswordActivity.getRequest_code_aws() == 1001){
                        enviarInfoUsuario();
                        imprimirMensaje("¡"+PasswordActivity.getMensaje()+", Ingrese nueva contraseña!");
                        ingresarNuevaContraseña();
                        finish();
                        break;
                    }
                }
            }else{
                editText_codigo.setError("¡Ingrese un código valido!");
            }
        });

        btn_salir.setOnClickListener((View view) -> {
            finish();
        });

        btn_cancela_codigo.setOnClickListener((View view) -> {
            finish();
        });
    }

    private boolean validarCorreo(String correo){
        String PATRON_CORREO = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern patron = Pattern.compile(PATRON_CORREO);
        Matcher matcher =  patron.matcher(correo);
        return matcher.matches();
    }

    public void RecuperarPasswordAWS(){
        this.setType("send_mail"); 
        this.setUrl("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-recover-account");
        this.setRecuperarPasswordAWS(new RecuperarPasswordAWS(this, this.getUrl(),this.getEmailUser(),this.getType()));
        this.getRecuperarPasswordAWS().execute();
    }

    public void enviarCodigoAWS(){
        this.setType("validate");
        this.setObtenerCodigoAWS(new ObtenerCodigoAWS(this, this.getUrl(),this.getSend_code_aws(), this.getEmailUser(),this.getType()));
        this.getObtenerCodigoAWS().execute();
    }

    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void ingresarNuevaContraseña(){
        Intent intent = new Intent(this, ResetActivity.class);
        startActivity(intent);
    }

    public void enviarInfoUsuario(){
        ResetActivity.emailUser = this.getEmailUser();
        ResetActivity.setSend_code_aws(this.getSend_code_aws());
    }

    public static int getRequest_code_aws() {
        int codigo = PasswordActivity.request_code_aws;
        return codigo;
    }

    public static void setRequest_code_aws(int request_code_aws) {
        PasswordActivity.request_code_aws = request_code_aws;
        System.out.println("SETTER:"+PasswordActivity.request_code_aws);
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser_json_aws(JSONObject user_json_aws) {
        this.user_json_aws = user_json_aws;
    }

    public String getSend_code_aws() {
        return send_code_aws;
    }

    public void setSend_code_aws(String send_code_aws) {
        this.send_code_aws = send_code_aws;
    }

    public RecuperarPasswordAWS getRecuperarPasswordAWS() {
        return recuperarPasswordAWS;
    }

    public void setRecuperarPasswordAWS(RecuperarPasswordAWS recuperarPasswordAWS) {
        this.recuperarPasswordAWS = recuperarPasswordAWS;
    }
    public static String getMensaje() {
        return PasswordActivity.mensaje;
    }

    public static void setMensaje(String mensaje) {
        PasswordActivity.mensaje = mensaje;
    }

    public ObtenerCodigoAWS getObtenerCodigoAWS() {
        return obtenerCodigoAWS;
    }

    public void setObtenerCodigoAWS(ObtenerCodigoAWS obtenerCodigoAWS) {
        this.obtenerCodigoAWS = obtenerCodigoAWS;
    }
}
