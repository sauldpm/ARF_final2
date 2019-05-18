/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf.RecuperarPassword;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

public class RecuperarPasswordAWS extends AsyncTask<Void,Void,String> {

    private String correo;
    private String type_aws;
    private JSONObject json_aws;
    private Context http;
    private String url_aws;

    public RecuperarPasswordAWS (Context context,String urlAws, String correo, String typeAws){

        this.setCorreo(correo);
        this.setType_aws(typeAws);
        this.setUrl_aws(urlAws);
        this.setHttp(context);
    }

    public String getPost(JSONObject params)throws Exception{
        StringBuilder resultado = new StringBuilder();
        boolean first=true;
        Iterator<String> bandera =params.keys();
        while(bandera.hasNext()){
            String index=bandera.next();
            Object value=params.get(index);
            if(first)first=false;
            else resultado .append("&");
            resultado.append(URLEncoder.encode(index,"UTF-8"));
            resultado.append("=");
            resultado.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }
        return resultado.toString();
    }

    @Override
    protected String doInBackground(Void... params) {
        String URLendPont= this.getUrl_aws();
        String resultado="";
        OutputStream out = null;

        try {
            URL url = new URL(URLendPont);
            JSONObject paramsPost = new JSONObject();
            paramsPost.put("type", this.getType_aws());
            paramsPost.put("email", this.getCorreo());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(3000);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(String.valueOf(paramsPost));
            writer.flush();
            writer.close();
            out.close();
            urlConnection.connect();
            if ( urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String renglon = new String("");
                int code = 0;
                String mensaje = new String("");
                while ((renglon = in.readLine()) != null) {
                    sb.append(renglon);
                    this.setJson_aws(new JSONObject(sb.toString()));
                    code = this.getJson_aws().getInt("code");
                    //PasswordActivity.setRequest_code_aws(code);
                    mensaje =  this.getJson_aws().getString("message");
                    //PasswordActivity.setMensaje(mensaje);
                    System.out.println("JSONEMAIL: "+code+" "+mensaje);
                    PasswordActivity.setRequest_code_aws(code);
                    PasswordActivity.setMensaje(mensaje);
                    //break;
                }
                in.close();
                resultado = sb.toString();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    //aws request
    @Override
    protected  void onPostExecute(String cad){
        super.onPostExecute(cad);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getType_aws() {
        return type_aws;
    }

    public void setType_aws(String type_aws) {
        this.type_aws = type_aws;
    }

    public JSONObject getJson_aws() {
        return json_aws;
    }

    public void setJson_aws(JSONObject json_aws) {
        this.json_aws = json_aws;
    }

    public Context getHttp() {
        return http;
    }

    public void setHttp(Context http) {
        this.http = http;
    }

    public String getUrl_aws() {
        return url_aws;
    }

    public void setUrl_aws(String url_aws) {
        this.url_aws = url_aws;
    }
}
