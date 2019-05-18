package com.saul.arf.Escenarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.ScenarioCamara.Cotizacion;
import com.saul.arf.ScenarioCamara.Mueble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MisEscenariosAWS extends AsyncTask<Void,Void,String> {
private Escenario escenario;
private Context context;
private Activity activity;
private int id_proyecto;
private int id_usuario;
private int accion;
private Proyecto proyecto;
private String foto;
private String respuesta;
private Cotizacion cotizacion;

    public MisEscenariosAWS(Escenario escenario,Context context,Activity activity,int accion)
    {
        this.escenario=escenario;
        this.context=context;
        this.activity=activity;
        this.accion=accion;
    }
    public MisEscenariosAWS(int id_usuario,int id_proyecto,Escenario escenario,Context context,Activity activity,int accion)
    {
        this.id_usuario=id_usuario;
        this.id_proyecto=id_proyecto;
        this.escenario=escenario;
        this.context=context;
        this.activity=activity;
        this.accion=accion;
    }
    public MisEscenariosAWS(String foto,Context context,Activity activity,int accion)
    {
        this.foto=foto;
        this.context=context;
        this.activity=activity;
        this.accion=accion;
    }

    public MisEscenariosAWS(int id_usuario,int id_proyecto,Proyecto proyecto,Context context,Activity activity,int accion)
    {
        this.id_usuario=id_usuario;
        this.id_proyecto=id_proyecto;
        this.context=context;
        this.activity=activity;
        this.accion=accion;
        this.setProyecto(proyecto);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String respuesta=null;
        switch(accion){
            case 1:
                respuesta=guardarInfoEscenario();
                break;
            case 2:
                respuesta=cargarEscenarios();
                break;
            case 3:
                respuesta=borrarFotoEscenarios();
                break;
            case 4:
                respuesta=borrarEscenario();
                break;
            case 5:
                respuesta=modificarEscenario();
                break;
        }
        return  respuesta;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String cad){
        Intent intent=new Intent(context,MisEscenariosActivity.class);
        switch(accion){
            case 1:
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenarios",respuesta);
                intent.putExtra("proyecto",this.getProyecto());
                activity.startActivity(intent);
                activity.finish();
                break;
            case 2://cargarEscenarios
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenarios",respuesta);
                intent.putExtra("proyecto",this.getProyecto());
                activity.startActivity(intent);
                activity.finish();
                break;
            case 4:
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenarios",respuesta);
                intent.putExtra("proyecto",this.getProyecto());
                activity.startActivity(intent);
                activity.finish();
                break;
            case 5:
                Intent intent2=new Intent(context,FotosEscenario.class);
                intent2.putExtra("id",id_usuario);
                intent2.putExtra("id_proyecto",id_proyecto);
                intent2.putExtra("escenario",escenario);
                intent2.putExtra("proyecto",this.getProyecto());
                activity.startActivity(intent2);
                Toast.makeText(activity, "Se guardaron los cambios", Toast.LENGTH_LONG).show();
                activity.finish();
                break;
        }
    }

    private String modificarEscenario(){
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-update-scenario");
            JSONObject peticion=new JSONObject();
            peticion.put("type","updateInfo");
            peticion.put("name",escenario.getNombreProyecto());
            peticion.put("tipoHabitacion",escenario.getTipoHabitacion());
            peticion.put("tamañoHabitacion",escenario.getTamanio());
            peticion.put("tipoMuebles",escenario.getTipoMuebles());
            peticion.put("presupuesto",escenario.getPresupuesto());
            peticion.put("scenarioId",Integer.toString(escenario.getId_escenario()));
            Log.d("ID",peticion.toString());
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

    private String borrarEscenario(){
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-delete-scenario");
            JSONObject peticion=new JSONObject();
            peticion.put("scenarioId",Integer.toString(escenario.getId_escenario()));
            Log.d("ID",""+escenario.getId_escenario());
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
                in.close();
                cargarEscenarios();
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

    private String borrarFotoEscenarios(){
        OutputStream out=null;
        String resultado=null;
        try {
            URL url=new URL("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-update-scenario");
            JSONObject peticion=new JSONObject();
            peticion.put("type","deleteImage");
            peticion.put("s3Link",foto);

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
                in.close();
                respuesta=resultado = sb.toString();
                Log.d("Respuesta",respuesta);
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

    private String cargarEscenarios(){
        OutputStream out=null;
        String resultado=null;
        try {
            URL url=new URL("https://0kg5bbzwbc.execute-api.us-west-2.amazonaws.com/dev/arf-get-scenario");
            JSONObject peticion=new JSONObject();
            peticion.put("project_id",Integer.toString(id_proyecto));
            peticion.put("type","getAll");

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
                in.close();
                respuesta=resultado = sb.toString();
                Log.d("Respuesta",respuesta);
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
    private String guardarInfoEscenario()
    {
        OutputStream out=null;
        String resultado = null;
        try {
            URL url=new URL("http://52.35.69.186:8082/arf-create-scenario");
            JSONObject peticion=new JSONObject();
            peticion.put("project_id",escenario.getId_proyecto());
            peticion.put("scenarioName",escenario.getNombreProyecto());
            peticion.put("tipoHabitacion",escenario.getTipoHabitacion());
            peticion.put("tamañoHabitacion",escenario.getTamanio());
            peticion.put("tipoMuebles",escenario.getTipoHabitacion());
            peticion.put("presupuesto",escenario.getPresupuesto());
            peticion.put("images",imagenesArray());
            peticion.put("profit",0);
            peticion.put("furnitures",furnitureArray());
            Log.d("Enviado ",peticion.toString());

            HttpURLConnection conexion=(HttpURLConnection)url.openConnection();
            conexion.setReadTimeout(60000);
            conexion.setConnectTimeout(3000);
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
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
                cargarEscenarios();
                resultado = sb.toString();
            } else {
                resultado = new String("Error :" + conexion.getResponseCode());
                cargarEscenarios();
                Log.d("Error",""+resultado);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public JSONArray furnitureArray() throws IOException {
        ArrayList<Mueble> furniture=cotizacion.getMuebles();
        JSONArray furnitureID = new JSONArray();
        for(int y=0;y<furniture.size();y++) {
            furnitureID.put(furniture.get(y).getId());
        }
        return  furnitureID;
    }
    public JSONArray imagenesArray() throws IOException {
        ArrayList<String> nombres=escenario.getRutasFotos();
        JSONArray imagenes = new JSONArray();
        for(int y=0;y<nombres.size();y++) {
            Log.d("RUTA",""+nombres.get(y));
            String encodedImage = Base64.encodeToString(loadImage64(nombres.get(y)), Base64.DEFAULT);
            imagenes.put(encodedImage);
        }
        return  imagenes;
    }
    public byte[] loadImage64(String url) throws IOException {
        File file= new File(url);
        if(file.exists()){
            int lenght=(int)file.length();
            BufferedInputStream reader=new BufferedInputStream(new FileInputStream(file));
            byte[] bytes=new byte[lenght];
            reader.read(bytes,0,lenght);
            reader.close();
            return bytes;
        }
        else{
            Log.d("R","No se encontro recurso");
            return null;
        }
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
