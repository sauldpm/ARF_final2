package com.saul.arf.Escenarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.Cotizacion;

import pl.droidsonroids.gif.GifImageView;

public class Load extends AppCompatActivity {
    private GifImageView gifImageView;
    private int id_usuario;
    private int id_proyecto;
    private Proyecto proyecto;
    private Escenario escenario;
    private int accion;
    private Context context;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        context=this;
        activity=this;
        accion=getIntent().getExtras().getInt("accion");
        switch(accion){
            case 1:
                proyectosToEscenarios();
                break;
            case 2:
                escenariosToProyectos();
                break;
            case 3:
                sessionToEscenarios();
                break;
            case 4:
                escenarioToEscenarios();
                break;
            case 5:
                borrarEscenario();
                break;
            case 6:
                modificarEscenario();
                break;
        }
        gifImageView = findViewById(R.id.progressbar);
        gifImageView.setVisibility(View.VISIBLE);
    }

    private void proyectosToEscenarios(){
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(id_usuario,id_proyecto,this.getProyecto(),context,activity,2);
        misEscenariosAWS.execute();
    }
    private void escenariosToProyectos(){
        id_usuario=getIntent().getExtras().getInt("id");
    }
    private void sessionToEscenarios(){
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario) getIntent().getExtras().getSerializable("escenario");
        Cotizacion cotizacion=(Cotizacion) getIntent().getExtras().getSerializable("cotizacion");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(id_usuario,id_proyecto,escenario,context,activity,1);
        misEscenariosAWS.setProyecto(proyecto);
        misEscenariosAWS.setCotizacion(cotizacion);
        misEscenariosAWS.execute();
    }
    private void escenarioToEscenarios(){
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(id_usuario,id_proyecto,this.getProyecto(),context,activity,2);
        misEscenariosAWS.execute();
    }
    private void borrarEscenario(){
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario) getIntent().getExtras().getSerializable("escenario");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(id_usuario,id_proyecto,escenario,context,activity,4);
        misEscenariosAWS.setProyecto(proyecto);
        misEscenariosAWS.execute();
    }

    private  void modificarEscenario(){
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario) getIntent().getExtras().getSerializable("escenario");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(id_usuario,id_proyecto,escenario,context,activity,5);
        misEscenariosAWS.setProyecto(proyecto);
        misEscenariosAWS.execute();
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
