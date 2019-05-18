package com.saul.arf;

import android.os.AsyncTask;
import android.util.Log;
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
import java.util.ArrayList;
import  com.saul.arf.Proyectos.Proyecto;

import javax.net.ssl.HttpsURLConnection;

public class MisProyectosAWS extends AsyncTask<Void,Void,String> {
    private static final String URL_AWS="https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-project";
    private ArrayList<Proyecto> misProyectos;
    private String respuest_aws;
    private int codigo_aws;
    private int id_usuario;
    private int id_proyecto;
    private String json;
    private Proyecto proyecto;

    public MisProyectosAWS(int id_usuario,int codigo_aws) {
        this.id_usuario=id_usuario;
        this.codigo_aws=codigo_aws;
        misProyectos=new ArrayList<Proyecto>();
    }

    public MisProyectosAWS(int id_usuario,int id_proyecto,int codigo_aws,Proyecto proyecto) {
        this.id_usuario=id_usuario;
        this.id_proyecto=id_proyecto;
        this.codigo_aws=codigo_aws;
        this.proyecto=proyecto;
    }

    public MisProyectosAWS(int id_usuario,int codigo_aws,Proyecto proyecto) {
        this.id_usuario=id_usuario;
        this.codigo_aws=codigo_aws;
        this.proyecto=proyecto;
    }

    public MisProyectosAWS(int id_usuario,ArrayList<Proyecto> misProyectos,int codigo_aws) {
        this.id_usuario=id_usuario;
        this.misProyectos = misProyectos;
        this.codigo_aws=codigo_aws;
    }


    @Override
    protected String doInBackground(Void... voids) {
        String result=null;
        switch (codigo_aws)
        {
            case 1:
                result=buscarProyecto();
                break;
            case 2:
                result= agregarProyecto();
                break;
            case 3:
                result= eliminarProyecto();
                break;
            case 4:
                result= modificarProyecto();
                break;
        }
        json=result;
        return result;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String cad){
        super.onPostExecute(cad);
        this.respuest_aws=cad;
    }

    private String buscarProyecto(){
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL(URL_AWS);
            JSONObject peticion=new JSONObject();
            peticion.put("user_id",id_usuario);
            peticion.put("type","get_user_projects");
            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(3000);
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

                /*peticion= new JSONObject(sb.toString());
                int code = peticion.getInt("code");
                String mensaje =  peticion.getString("message");*/

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

    private String agregarProyecto(){

        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL(URL_AWS);
            JSONObject peticion=new JSONObject();
            peticion.put("client_firstname",proyecto.getClient_firstname());
            peticion.put("client_lastname",proyecto.getClient_lastname());
            peticion.put("client_phone",proyecto.getClient_phone());
            peticion.put("client_email",proyecto.getClient_email());
            peticion.put("user_id",id_usuario);
            peticion.put("type","create");
            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(3000);
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

    private String eliminarProyecto(){
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL(URL_AWS);
            JSONObject peticion=new JSONObject();
            peticion.put("project_id",id_usuario);
            peticion.put("type","delete");
            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(3000);
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

                /*peticion= new JSONObject(sb.toString());
                int code = peticion.getInt("code");
                String mensaje =  peticion.getString("message");*/
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

    private String modificarProyecto(){
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL(URL_AWS);
            JSONObject peticion=new JSONObject();
            peticion.put("client_firstname",proyecto.getClient_firstname());
            peticion.put("client_lastname",proyecto.getClient_lastname());
            peticion.put("client_phone",proyecto.getClient_phone());
            peticion.put("client_email",proyecto.getClient_email());
            peticion.put("project_id",id_proyecto);
            peticion.put("type","update");
            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(3000);
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}

