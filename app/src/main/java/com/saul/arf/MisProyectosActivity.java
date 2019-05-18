package com.saul.arf;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.saul.arf.Escenarios.Load;
import com.saul.arf.Proyectos.AdapterItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.Video.TutorialActivity;

import java.util.ArrayList;

public class MisProyectosActivity extends AppCompatActivity {
    private int id_usuario;
    private ListView lista;
    private AdapterItem adaptador;
    private ArrayList<Proyecto> listaProyectos;
    private Context context;
    private Activity activity;
    private ImageButton settings;
    private PopupMenu dropDownMenu;
    private Boolean opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_proyectos);
        id_usuario=getIntent().getExtras().getInt("id");
        ImageButton agregar=findViewById(R.id.agregarProyecto);
        settings=findViewById(R.id.settings);
        dropDownMenu=new PopupMenu(this,settings);
        context=this;
        activity=this;
        String json=null;
        MisProyectosAWS misProyectosAWS=new MisProyectosAWS(id_usuario,1);
        misProyectosAWS.execute();
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AgregarProyectoActivity.class);
                intent.putExtra("id_usuario",id_usuario);
                intent.putExtra("listaProyectos",listaProyectos);
                context.startActivity(intent);
                activity.finish();
            }
        });
        crearDropMenu();

        while ((json=misProyectosAWS.getJson())==null);
        lista=(ListView) findViewById(R.id.list);

        try {
            adaptador=new AdapterItem(this,getArrayList(json),this,id_usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lista.setAdapter(adaptador);
    }

    private ArrayList<Proyecto> getArrayList(String json) throws JSONException {
        ArrayList<Proyecto> listaItems=new ArrayList<Proyecto>();
        JSONObject respuestaProyecto = new JSONObject(json);
        JSONArray proyectos = (JSONArray) respuestaProyecto.get("projects");

        int id;
        String client_firstname;
        String client_lastname;
        String client_phone;
        String client_email;

        for(int x=0;x<proyectos.length();x++){
            JSONObject aux=proyectos.getJSONObject(x);
            id= (int) aux.get("id");
            client_firstname=(String)aux.get("client_firstname");
            client_lastname=(String)aux.get("client_lastname");
            client_phone=(String)aux.get("client_phone");
            client_email=(String)aux.get("client_email");
            listaItems.add(new Proyecto(id,client_firstname,client_lastname,client_phone,client_email));
        }
        listaProyectos=listaItems;
        return listaItems;
    }

    private void crearDropMenu(){
        Menu menu=dropDownMenu.getMenu();
        dropDownMenu.getMenuInflater().inflate(R.menu.menu_settings,menu);;
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ver_tutorial:
                        Intent intent=new Intent(context, TutorialActivity.class);
                        intent.putExtra("id",id_usuario);
                        activity.startActivity(intent);
                        return true;
                    case R.id.cerrar_session_menu:
                        SharedPreferences preferences = getSharedPreferences("arf_session__variables", Context.MODE_PRIVATE);
                        SharedPreferences tutorial = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
                        SharedPreferences.Editor tutorialEditor=tutorial.edit();
                        SharedPreferences.Editor editor = preferences.edit();
                        tutorialEditor.remove("mostrar2");
                        tutorialEditor.commit();
                        editor.clear();
                        editor.commit();
                        Intent intent1=new Intent(context,MainActivity.class);
                        activity.startActivity(intent1);
                        activity.finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });
    }

    private void preguntarVideo(){
        opcion=false;
        final Activity activity=this;
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Desea ver el tutorial?")
        .setCancelable(false)
                .setMultiChoiceItems(R.array.ver_tutorial, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                opcion=!opcion;
                            }
                        })
        .setPositiveButton("Ver tutorial", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Intent intent=new Intent(context, TutorialActivity.class);
                intent.putExtra("id",id_usuario);
                SharedPreferences preferences = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("mostrar2",false);
                editor.commit();
                activity.startActivity(intent);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                if(opcion) {
                    SharedPreferences preferences = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("mostrar1", false);
                    editor.commit();
                }
                dialogo1.cancel();
            }
        })
        .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
        Boolean mostrar1=preferences.getBoolean("mostrar1",true);
        Boolean mostrar2=preferences.getBoolean("mostrar2",true);
        if(mostrar1)
            if(mostrar2)
                preguntarVideo();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}


