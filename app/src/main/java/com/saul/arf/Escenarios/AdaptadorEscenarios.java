package com.saul.arf.Escenarios;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saul.arf.R;

import java.util.ArrayList;

public class AdaptadorEscenarios extends BaseAdapter {
    private ArrayList<Escenario> lista_escenario;
    private Context context;
    private Activity activity;

    public AdaptadorEscenarios(Context context,ArrayList<Escenario> lista_escenario){
        this.context=context;
        this.lista_escenario=lista_escenario;
    }
    @Override
    public int getCount() {
        return lista_escenario.size();
    }

    @Override
    public Object getItem(int position) {
        return lista_escenario.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.grid_escenarios_item,null);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);
        TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        String nombre_str=lista_escenario.get(position).getNombreProyecto();
        nombre.setText(nombre_str);
        if(lista_escenario.get(position).getRutasFotos().isEmpty())
            Glide.with(context).load(R.drawable.foto_default).into(imagen);
        else{
            String foto=lista_escenario.get(position).getRutasFotos().get(0);
            Glide.with(context).load(foto).into(imagen);
        }
        return convertView;
    }
}
