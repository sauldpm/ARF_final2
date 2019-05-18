package com.saul.arf.Escenarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.MisProyectosAWS;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.LoadSessionActivity;
import com.saul.arf.SessionActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class GuardarInfoEscenarios extends AppCompatActivity {
    private ImageButton btn_cancelar;
    private Button btn_guardarInfo;
    private EditText et_nombreProyecto;
    private EditText et_presupuesto;
    private EditText editTextTamanio;
    private EditText editTextTipoHabitacion;
    private EditText editTextTipoMuebles;
    private Context context;
    private Activity activity;
    private int id_usuario;
    private int id_proyecto;
    private Proyecto proyecto;
    private String escenarios;
    private String nombreEscenario;
    private String tamanioEscenario;
    private String tipoHabitacion;
    private String tipoMuebles;
    private String presupuesto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_info_escenario);
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenarios=getIntent().getExtras().getString("escenarios");
        this.setProyecto((Proyecto)getIntent().getExtras().getSerializable("proyecto"));
        context=this;
        activity=this;

        btn_cancelar = findViewById(R.id.btn_cancelar);
        btn_guardarInfo = findViewById(R.id.btn_guardarInfo);
        et_nombreProyecto = findViewById(R.id.et_nombreProyecto);
        et_presupuesto = findViewById(R.id.ed_presupuesto);
        editTextTamanio = findViewById(R.id.spinner_tam);
        editTextTipoHabitacion = findViewById(R.id.spinner_tipoHab);
        editTextTipoMuebles = findViewById(R.id.spinner_muebles);

        btn_guardarInfo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombreProyecto = et_nombreProyecto.getText().toString();
                String presupuesto_str =et_presupuesto.getText().toString();
                String tamanio_str= editTextTamanio.getText().toString();
                String tipoHabitacion = editTextTipoHabitacion.getText().toString();
                String tipoMuebles = editTextTipoMuebles.getText().toString();
                if(!validarNombre(nombreProyecto)){
                    et_nombreProyecto.setError("¡Nombre inválido!");
                }
                if(tamanio_str.isEmpty()){
                    editTextTamanio.setError("¡Ingrese un tamaño!");
                }
                if(!validarNombre(tipoHabitacion)){
                    editTextTipoHabitacion.setError("¡Tipo de habitación inválido!");
                }
                if(!validarNombre(tipoMuebles)){
                    editTextTipoMuebles.setError("¡Tipo de mueble inválido!");
                }
                if(presupuesto_str.isEmpty()){
                    et_presupuesto.setError("¡Presupuesto invalido!");
                    presupuesto_str="";
                }
                else{

                    Escenario escenario=new Escenario(id_proyecto,nombreProyecto,presupuesto_str,tamanio_str,tipoHabitacion,tipoMuebles);

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent(context, LoadSessionActivity.class);
                    intent.putExtra("Escenario",escenario);
                    intent.putExtra("id",id_usuario);
                    intent.putExtra("id_proyecto",id_proyecto);
                    intent.putExtra("proyecto",proyecto);
                    intent.putExtra("escenarios",escenarios);
                    activity.startActivity(intent);
                    finish();
                }
            }
        });


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación")
                .setMessage("¿Seguro que desea regresar?. Se perderán los datos colocados")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        back();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        dialogo.cancel();
                    }
                });
        alert.create();
        alert.show();
    }
    private void back(){
        Intent intent=new Intent(context, MisEscenariosActivity.class);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("id",id_usuario);
        intent.putExtra("proyecto",proyecto);
        intent.putExtra("id_proyecto",id_proyecto);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    public String getNombreEscenario() {
        return nombreEscenario;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }

    public String getTamanioEscenario() {
        return tamanioEscenario;
    }

    public void setTamanioEscenario(String tamanioEscenario) {
        this.tamanioEscenario = tamanioEscenario;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public String getTipoMuebles() {
        return tipoMuebles;
    }

    public void setTipoMuebles(String tipoMuebles) {
        this.tipoMuebles = tipoMuebles;
    }

    public String getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(String presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    private boolean validarNombre(String nombre){
        String PATRON_NOMBRE = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$";
        Pattern patron = Pattern.compile(PATRON_NOMBRE);
        Matcher matcher =  patron.matcher(nombre);
        return matcher.matches();
    }
}