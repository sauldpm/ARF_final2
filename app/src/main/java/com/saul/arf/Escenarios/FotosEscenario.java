package com.saul.arf.Escenarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saul.arf.MisProyectosAWS;
import com.saul.arf.MisProyectosActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;

public class FotosEscenario extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private int id_usuario;
    private int id_proyecto;
    private Escenario escenario;
    private GridView lista;
    private AdaptadorFotos adaptadorFotos;
    private TextView texto_tool_bar;
    private TextView infoEscenario;
    private Proyecto proyecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_escenario);
        context=this;
        activity=this;
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario) getIntent().getExtras().getSerializable("escenario");
        proyecto=(Proyecto)getIntent().getExtras().getSerializable("proyecto");
        texto_tool_bar=findViewById(R.id.texto_imagenes);
        infoEscenario=findViewById(R.id.info_escenario);
        lista=(GridView) findViewById(R.id.grid_imagenes);

        infoEscenario();
        adaptadorFotos=new AdaptadorFotos(context,escenario.getRutasFotos());
        lista.setAdapter(adaptadorFotos);
        lista.setOnItemClickListener(new FotosClickListener(activity,context,id_usuario,id_proyecto,escenario,proyecto));
        texto_tool_bar.setText(escenario.getNombreProyecto());

        ImageButton regresar = findViewById(R.id.btn_imagenes_regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        ImageButton borrarEscenario=findViewById(R.id.btn_escenarios_borrar);
        borrarEscenario.setOnClickListener(this::borrarEscenario);
        ImageButton modificarDatos=findViewById(R.id.btn_escenarios_modificar);
        modificarDatos.setOnClickListener(this::modificarDatos);
    }

    private void back(){
        Intent intent= new Intent(context,Load.class);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("accion",4);
        intent.putExtra("proyecto",proyecto);
        context.startActivity(intent);
        activity.finish();
    }

    private void infoEscenario(){
        StringBuilder text=new StringBuilder();
        text.append("Nombre proyecto: "+escenario.getNombreProyecto()+"\n");
        text.append("Tamaño habitacion: "+escenario.getTamanio()+"\n");
        text.append("Tipo habitacion: "+escenario.getTipoHabitacion()+"\n");
        text.append("Tipo Muebles "+escenario.getTipoMuebles()+"\n");
        text.append("Presupuesto: "+escenario.getPresupuesto()+"\n");
        infoEscenario.setText(text.toString());
    }
    private void borrarEscenario(View view){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle("Confirmación");
        dialogo1.setMessage("¿Seguro que quiere eliminar este escenario?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Intent intent= new Intent(context,Load.class);
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenario",escenario);
                intent.putExtra("proyecto",proyecto);
                intent.putExtra("accion",5);
                context.startActivity(intent);
                activity.finish();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        dialogo1.show();
    }

    private void modificarDatos(View view){
        Intent intent= new Intent(context,ModificarEscenarioActivity.class);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("escenario",escenario);
        intent.putExtra("proyecto",proyecto);
        context.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
