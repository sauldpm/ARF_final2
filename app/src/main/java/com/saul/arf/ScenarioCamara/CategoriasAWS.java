package com.saul.arf.ScenarioCamara;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.saul.arf.Escenarios.Escenario;
import com.saul.arf.Proyectos.Proyecto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CategoriasAWS extends AsyncTask<Void,Void,String> {
    private int id_user;
    private int id_proyecto;
    private Proyecto proyecto;
    private Escenario escenario;
    private Activity activity;
    private Context context;
    private String escenarios;

    public CategoriasAWS(int id_user, int id_proyecto,Proyecto proyecto ,Escenario escenario, Activity activity, Context context,String escenarios) {
        this.id_user = id_user;
        this.id_proyecto = id_proyecto;
        this.escenario = escenario;
        this.activity = activity;
        this.context = context;
        this.setProyecto(proyecto);
        this.escenarios=escenarios;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-asset-crud");
            JSONObject peticion=new JSONObject();
            peticion.put("type","getCategoriesByUser");
            peticion.put("userId",Integer.toString(id_user));
            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(60000);
            conexion.setConnectTimeout(3000);
            conexion.setRequestMethod("POST");
            conexion.setDoInput(true);
            conexion.setDoOutput(true);
            out= new BufferedOutputStream(conexion.getOutputStream());

            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.write(String.valueOf(peticion));
            writer.flush();
            writer.close();
            out.close();
            conexion.connect();

            if ( conexion.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String renglon = "";
                while ((renglon = in.readLine()) != null) {
                    sb.append(renglon);
                    break;
                }
                Log.d("Respuesta",""+renglon);
                Log.d("Respuesta 2",""+sb.toString());
                in.close();
                resultado = sb.toString();
            } else
                resultado = new String("Error :" + conexion.getResponseCode());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String cad){
        MueblesAWS mueblesAWS=new MueblesAWS(id_user,id_proyecto,this.getProyecto(),escenario,activity,context,cad,escenarios);
        mueblesAWS.execute();
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
