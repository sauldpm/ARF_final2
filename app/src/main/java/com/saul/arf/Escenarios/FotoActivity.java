package com.saul.arf.Escenarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.MisProyectosAWS;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;

public class FotoActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private int id_usuario;
    private int id_proyecto;
    private Escenario escenario;
    private String foto;
    private Proyecto proyecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        context=this;
        activity=this;
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");
        escenario=(Escenario)getIntent().getExtras().getSerializable("escenario");
        foto=getIntent().getExtras().getString("foto");
        proyecto=(Proyecto)getIntent().getExtras().getSerializable("proyecto");
        ImageView imagen =findViewById(R.id.imagen_foto);
        Glide.with(this).load(foto).into(imagen);

        ImageButton regresar = findViewById(R.id.btn_foto_regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        ImageButton borrar=findViewById(R.id.btn_foto_borrar);
        borrar.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { borrar(); }});
    }

    private  void back(){
        Intent intent= new Intent(context,FotosEscenario.class);
        intent.putExtra("id",id_usuario);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("escenario",escenario);
        intent.putExtra("proyecto",proyecto);
        context.startActivity(intent);
        activity.finish();
    }
    private void borrar(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle("Confirmación");
        dialogo1.setMessage("¿ Seguro que quiere eliminar esta imagen?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                MisEscenariosAWS misEscenariosAWS=new MisEscenariosAWS(foto,context,activity,3);
                misEscenariosAWS.execute();
                escenario.getRutasFotos().remove(foto);
                Intent intent= new Intent(context,FotosEscenario.class);
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenario",escenario);
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

    @Override
    public void onBackPressed() {
        back();
    }
}
