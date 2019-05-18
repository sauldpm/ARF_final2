package com.saul.arf.Escenarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.saul.arf.Proyectos.Proyecto;

public class EscenarioClickListener implements AdapterView.OnItemClickListener  {
    private Activity activity;
    private Context context;
    private int id_usuario;
    private int id_proyecto;
    private Proyecto proyecto;

    public EscenarioClickListener(Activity activity,Context context,int id_usuario,int id_proyecto,Proyecto proyecto){
        this.activity=activity;
        this.context=context;
        this.id_usuario=id_usuario;
        this.id_proyecto=id_proyecto;
        this.proyecto=proyecto;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Escenario item = (Escenario) parent.getItemAtPosition(position);
        Intent intent = new Intent(context, FotosEscenario.class);
        intent.putExtra("escenario",item);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("proyecto",proyecto);
        activity.startActivity(intent);
    }
}
