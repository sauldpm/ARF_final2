package com.saul.arf.Resumen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.saul.arf.Escenarios.Load;
import com.saul.arf.MainActivity;
import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.Cotizacion;

public class ResumenActivity extends AppCompatActivity {
    private GridView fotos;
    private AdaptadorCotizacion adaptadorCotizacion;
    private Cotizacion cotizacion;
    private Button verCotizacion;
    private Button guardar;
    private Proyecto proyecto;
    private String escenarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        cotizacion=(Cotizacion) getIntent().getExtras().get("cotizacion");
        proyecto=(Proyecto)getIntent().getExtras().get("proyecto");
        escenarios=getIntent().getExtras().getString("escenarios");
        Log.d("Cotizaion",""+cotizacion.getEscenario().getRutasFotos().size());

        fotos=(GridView) findViewById(R.id.grid_cotizacion);
        adaptadorCotizacion=new AdaptadorCotizacion(cotizacion.getEscenario().getRutasFotos(), this);
        fotos.setAdapter(adaptadorCotizacion);


        verCotizacion=findViewById(R.id.ver_cotizacion);
        verCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verCotizacion();
            }
        });

        guardar=findViewById(R.id.guardar_cotizacion);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCotizacion();
            }
        });
    }

    public void verCotizacion(){
        Intent intent=new Intent(this,TotalActivity.class);
        intent.putExtra("cotizacion",cotizacion);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("proyecto",proyecto);
        this.startActivity(intent);
    }

    public void guardarCotizacion(){
        Intent intent=new Intent(this, Load.class);
        intent.putExtra("id",cotizacion.getId_user());
        intent.putExtra("id_proyecto",cotizacion.getId_proyecto());
        intent.putExtra("escenario",cotizacion.getEscenario());
        intent.putExtra("accion",3);
        intent.putExtra("proyecto",proyecto);
        intent.putExtra("cotizacion",cotizacion);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }
    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación")
                .setMessage("¿Seguro que desea regresar?. Se perderán los datos guardados")
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
        Intent intent=new Intent(this, MisEscenariosActivity.class);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("id",cotizacion.getId_user());
        intent.putExtra("id_proyecto",cotizacion.getId_proyecto());
        intent.putExtra("proyecto",proyecto);
        startActivity(intent);
        finish();
    }
}


