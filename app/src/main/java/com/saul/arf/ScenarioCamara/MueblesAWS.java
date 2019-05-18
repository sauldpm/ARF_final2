package com.saul.arf.ScenarioCamara;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.saul.arf.Escenarios.Escenario;
import com.saul.arf.MainActivity;
import com.saul.arf.Menu.MenuActivity;
import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.SessionActivity;

import org.json.JSONArray;
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

import javax.net.ssl.HttpsURLConnection;


public class MueblesAWS extends AsyncTask<Void,Void,String> {
    private int id_user;
    private int id_proyecto;
    private Proyecto proyecto;
    private Escenario escenario;
    private Activity activity;
    private Context context;
    private String categorias;
    private String escenarios;

    public MueblesAWS(int id_user, int id_proyecto,Proyecto proyecto, Escenario escenario, Activity activity, Context context, String categorias,String escenarios) {
        this.id_user = id_user;
        this.id_proyecto = id_proyecto;
        this.escenario = escenario;
        this.activity = activity;
        this.context = context;
        this.categorias = categorias;
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
            peticion.put("type","getByUser");
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
        ArrayList<Categoria> arrayListCategorias= null;
        try {
            arrayListCategorias = makeCategorias(categorias,cad);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(arrayListCategorias.size()==0) {
            sinMuebles();
            return;
        }

        Intent intent=new Intent(context, MenuActivity.class);
        //Intent intent=new Intent(context, SessionActivity.class);
        intent.putExtra("id",id_user);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("Escenario",escenario);
        intent.putExtra("proyecto",this.getProyecto());
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("c",arrayListCategorias);
        activity.startActivity(intent);
        //activity.finish();
    }

    private void sinMuebles(){
       if(id_user==40){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Advertencia")
                    .setCancelable(false)
                    .setMessage("Es necesario crear en su cuenta categorías,subcategorias y muebles")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(context, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    })
                    .show();
        }
        else {
           AlertDialog.Builder builder=new AlertDialog.Builder(context);
           builder.setTitle("Advertencia")
                   .setCancelable(false)
                   .setMessage("Es necesario crear en su cuenta categorías,subcategorias y muebles")
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Intent intent=new Intent(context, MisEscenariosActivity.class);
                           intent.putExtra("id",id_user);
                           intent.putExtra("id_proyecto",id_proyecto);
                           intent.putExtra("proyecto",proyecto);
                           intent.putExtra("escenarios",escenarios);
                           activity.startActivity(intent);
                           activity.finish();
                       }
                   })
                   .show();
        }
    }

    private ArrayList<Categoria> makeCategorias(String categoriasJSON,String mueblesJSON) throws JSONException {
        ArrayList<Categoria> arrayListCategorias=new ArrayList<Categoria>();


        ArrayList<Mueble> muebles=makeMuebles(mueblesJSON);

        JSONObject jsonCategorias = new JSONObject(categoriasJSON);
        if(jsonCategorias.getInt("code")==-2) {
            return arrayListCategorias;
        }

        JSONArray jsonArrayCategories=jsonCategorias.getJSONArray("categories");

        for(int x=0;x<jsonArrayCategories.length();x++){
            JSONObject category=jsonArrayCategories.getJSONObject(x);
            int categoryId=category.getInt("categoryId");
            String name=category.getString("name");
            JSONArray subcategories=category.getJSONArray("subcategories");
            ArrayList<SubCategoria> arrayListSubCategorias=new ArrayList<SubCategoria>();
                for (int y=0;y<subcategories.length();y++){
                    int subcategoryId=subcategories.getJSONObject(y).getInt("id");
                    String subcategoryName=subcategories.getJSONObject(y).getString("name");
                    SubCategoria subCategory=new SubCategoria(subcategoryId,subcategoryName);
                    for(int z=0;z<muebles.size();z++){
                        if( (muebles.get(z).getCategoryId()==categoryId) && (muebles.get(z).getSubCategoryId()==subcategoryId) ){
                            subCategory.addMueble(muebles.get(z));
                        }
                    }
                    arrayListSubCategorias.add(subCategory);
                }
            arrayListCategorias.add(new Categoria(categoryId,name,arrayListSubCategorias));
        }
        return arrayListCategorias;
    }

    private ArrayList<Mueble> makeMuebles(String mueblesJSON) throws JSONException {
        ArrayList<Mueble> muebles=new ArrayList<Mueble>();
        JSONObject jsonMuebles = new JSONObject(mueblesJSON);

        if(jsonMuebles.getInt("code")==-1)
            return muebles;

        JSONArray jsonArrayMuebles= jsonMuebles.getJSONArray("furnitures");

        for(int x=0;x<jsonArrayMuebles.length();x++){
            JSONObject mueble=jsonArrayMuebles.getJSONObject(x);
            int id=mueble.getInt("id");
            String name=mueble.getString("name");
            Double price=mueble.getDouble("price");
            String category=mueble.getString("category");
            int categoryId=mueble.getInt("categoryId");
            String subCategory=mueble.getString("subcategory");
            int subCategoryId=mueble.getInt("subcategoryId");
            String modelo3D=mueble.getString("asset");
            String image=mueble.getString("image");
            image=image.replace(".png","_thumb.png");
            muebles.add(new Mueble(id,name,price,category,categoryId,subCategory,subCategoryId,modelo3D,image));
        }
        return muebles;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

}
