package com.saul.arf.Proyectos;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saul.arf.Escenarios.Load;
import com.saul.arf.R;
import com.saul.arf.MisProyectosAWS;
import java.util.ArrayList;




public class AdapterItem extends BaseAdapter{
    private ArrayList<Proyecto> lista;
    private Context contexto;
    private Activity activity;
    private int id_usurio;

    public AdapterItem(Context contexto,ArrayList<Proyecto> lista) {
        this.contexto=contexto;
        this.lista = lista;
    }

    public AdapterItem(Context contexto,ArrayList<Proyecto> lista,Activity activity,int id_usurio) {
        this.contexto=contexto;
        this.lista = lista;
        this.activity=activity;
        this.id_usurio=id_usurio;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Proyecto item=(Proyecto) getItem(position);
        convertView= LayoutInflater.from(contexto).inflate(R.layout.item,null);
        TextView client_firstname=(TextView)convertView.findViewById(R.id.client_firstname);
        TextView client_lastname=(TextView)convertView.findViewById(R.id.client_lastname);
        //TextView client_phone=(TextView)convertView.findViewById(R.id.client_phone);
        //TextView client_email=(TextView)convertView.findViewById(R.id.client_email);
        FloatingActionButton eliminar=(FloatingActionButton)convertView.findViewById(R.id.eliminar);
        FloatingActionButton cambiar=(FloatingActionButton)convertView.findViewById(R.id.cambiar);
        final LinearLayout layout=convertView.findViewById(R.id.layout_informacion);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(contexto, Load.class);
                intent.putExtra("id",id_usurio);
                intent.putExtra("id_proyecto",item.getId());
                intent.putExtra("proyecto",new Proyecto(item.getClient_firstname(),item.getClient_lastname(),item.getClient_phone(),item.getClient_email()));
                intent.putExtra("accion",1);
                contexto.startActivity(intent);
                activity.finish();
            }
        });
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, ModificarProyectoActivity.class);
                intent.putExtra("proyecto",item);
                intent.putExtra("id",id_usurio);
                contexto.startActivity(intent);
                activity.finish();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(contexto);
                dialogo1.setTitle("Confirmación");
                dialogo1.setMessage("¿Seguro que quiere eliminar este proyecto?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        String respuesta;
                        MisProyectosAWS cancel=new MisProyectosAWS(item.getId(),3,item);
                        cancel.execute();
                        while ((respuesta=cancel.getJson())==null);
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                    }
                });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                    }
                });
                dialogo1.show();
            }
        });

        client_firstname.setText(item.getClient_firstname());
        client_lastname.setText(item.getClient_lastname());
        //client_phone.setText(item.getClient_phone());
        //client_email.setText(item.getClient_email());

        return convertView;
    }
}

