package com.saul.arf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saul.arf.Escenarios.*;
import com.saul.arf.Proyectos.Proyecto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MisEscenariosActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private GridView lista;
    private AdaptadorEscenarios adaptador;
    private int id_usuario;
    private int id_proyecto;
    private String escenarios;
    private Proyecto proyecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_escenarios);
        context=this;
        activity=this;
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        escenarios=getIntent().getExtras().getString("escenarios");
        //Creacion grid (imagenes)
        lista=(GridView) findViewById(R.id.grid);
        try {
            adaptador=new AdaptadorEscenarios(this,escenarios(escenarios));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new EscenarioClickListener(activity,context,id_usuario,id_proyecto,proyecto));
        ///Fin grid
        ImageButton guardarInfoProy = findViewById(R.id.btn_info_escenarios);
        ImageButton regresar = findViewById(R.id.btn_escenarios_regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        guardarInfoProy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarProyectos();
            }
        });
    }

    private void back(){
        Intent intent= new Intent(context, MisProyectosActivity.class);
        intent.putExtra("id",id_usuario);
        context.startActivity(intent);
        activity.finish();
    }

    private void guardarProyectos(){
        Intent intent = new Intent(this, GuardarInfoEscenarios.class);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("proyecto",this.getProyecto());
        startActivity(intent);
    }

    private ArrayList<Escenario> escenarios(String escenarios)throws JSONException {
        int id_escenario;
        String id_escenario_str;
        String nombreProyecto;
        String presupuesto;
        String tamanio;
        String tipoHabitacion;
        String tipoMuebles;
        ArrayList<String> rutasFotos;

        //Leer Json
        ArrayList<Escenario> lista_escenario=new ArrayList<Escenario>();
        JSONObject jsonEscenarios = new JSONObject(escenarios);
        if(jsonEscenarios.get("code").equals(900)){
            return  lista_escenario;
        }

        JSONArray ids_scenarios= jsonEscenarios.getJSONArray("ids");
        JSONObject scenarios = jsonEscenarios.getJSONObject("scenarios");
        for(int x=0;x<scenarios.length();x++){
            id_escenario=(int)ids_scenarios.get(x);
            id_escenario_str=Integer.toString(id_escenario);
            JSONObject scenario=scenarios.getJSONObject(id_escenario_str);
            JSONArray imagenes=scenario.getJSONArray("images");
            //Construyendo Escenario
            nombreProyecto=scenario.getString("Scenario");
            presupuesto=scenario.getString("presupuesto");
            tamanio=scenario.getString("tamañoHabitacion");
            tipoHabitacion=scenario.getString("tipoHabitacion");
            tipoMuebles=scenario.getString("tipoMuebles");
            rutasFotos=new ArrayList<String>();
            for(int y=0;y<imagenes.length();y++) {
                String imagenTemporal=imagenes.get(y).toString();
                //imagenTemporal=imagenTemporal.replace(".png","_thumb.png");
                //Log.d("Imagen",imagenTemporal);
                rutasFotos.add(imagenTemporal);
            }
            lista_escenario.add(new Escenario(id_escenario,nombreProyecto,presupuesto,tamanio,tipoHabitacion,tipoMuebles,rutasFotos));
        }
        return  lista_escenario;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
