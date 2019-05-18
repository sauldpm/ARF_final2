package com.saul.arf.Resumen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.saul.arf.R;

import java.util.ArrayList;

public class AdaptadorCotizacion extends BaseAdapter {

    private ArrayList<String> fotos;
    private Context context;

    public AdaptadorCotizacion(ArrayList<String> fotos, Context context) {
        this.fotos = fotos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public Object getItem(int position) {
        return fotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.grid_item_cotizacion,null);
        ImageView imagen=convertView.findViewById(R.id.grid_cotizacion_item);
        Glide.with(context).load(fotos.get(position)).into(imagen);
        return convertView;
    }
}

