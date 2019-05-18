package com.saul.arf.Escenarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.SessionActivity;

import static java.lang.Thread.sleep;

public class ModificarEscenarioActivity extends AppCompatActivity {
    private int id_usuario;
    private int id_proyecto;
    private Escenario escenario;
    private Activity activity;
    private Context context;
    private ImageButton regresar;
    private Button modificar;
    private TextView nombre;
    private TextView tipo;
    private TextView tamanio;
    private TextView tipoMueble;
    private TextView presupuesto;
    private Proyecto proyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_escenario);
        activity=this;
        context=this;
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario)getIntent().getExtras().getSerializable("escenario");
        proyecto=(Proyecto)getIntent().getExtras().getSerializable("proyecto");
        regresar=findViewById(R.id.btn_escenarios_modificar_regresar);
        modificar=findViewById(R.id.btn_enviar_modificar_escenario);
        nombre=findViewById(R.id.modificar_escenario_nombre);
        tipo=findViewById(R.id.modificar_escenario_tipo);
        tamanio=findViewById(R.id.modificar_escenario_tamano);
        tipoMueble=findViewById(R.id.modificar_escenario_tipo_m);
        presupuesto=findViewById(R.id.modificar_escenario_presupuesto);
        setTextInTextView();

        regresar.setOnClickListener(this::regresar);
        modificar.setOnClickListener(this::modificar);
    }

    private void setTextInTextView(){
        nombre.setText(escenario.getNombreProyecto(),TextView.BufferType.EDITABLE);
        tipo.setText(escenario.getTipoHabitacion(),TextView.BufferType.EDITABLE);
        tamanio.setText(escenario.getTamanio(),TextView.BufferType.EDITABLE);
        tipoMueble.setText(escenario.getTipoMuebles(),TextView.BufferType.EDITABLE);
        presupuesto.setText(escenario.getPresupuesto(),TextView.BufferType.EDITABLE);
    }
    private void modificar(View view){
        String nombre_str = nombre.getText().toString();
        String tipo_str =tipo.getText().toString();
        String tamanio_str= tamanio.getText().toString();
        String tipoMueble_str = tipoMueble.getText().toString();
        String presupuesto_str = presupuesto.getText().toString();
        if(nombre_str.isEmpty()){
            nombre.setError("¡Ingrese un nombre!");
        }
        else if(tamanio_str.isEmpty()){
            tamanio.setError("¡Ingrese el tamaño!");
        }
        else if(tipo_str.isEmpty()){
            tipo.setError("¡Ingrese el tipo de habitación!");
        }
        else if(tipoMueble_str.isEmpty()){
            tipoMueble.setError("¡Ingrese el tipo de mueble!");
        }
        else{
            if(presupuesto_str.isEmpty())
                presupuesto_str="";

            Escenario escenarioNew=new Escenario(escenario.getId_escenario(),nombre_str,presupuesto_str,tamanio_str,tipo_str,tipoMueble_str,escenario.getRutasFotos());

            Intent intent=new Intent(context, Load.class);
            intent.putExtra("escenario",escenarioNew);
            intent.putExtra("id",id_usuario);
            intent.putExtra("id_proyecto",id_proyecto);
            intent.putExtra("accion",6);
            intent.putExtra("proyecto",proyecto);
            activity.startActivity(intent);
            activity.finish();
        }
    }
    private void regresar(View view){
        Intent intent= new Intent(context,FotosEscenario.class);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("escenario",escenario);
        intent.putExtra("proyecto",proyecto);
        context.startActivity(intent);
        activity.finish();
    }
}
