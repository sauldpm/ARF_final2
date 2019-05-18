package com.saul.arf.Escenarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.saul.arf.R;

import java.util.ArrayList;

public class AdaptadorFotos extends BaseAdapter {
    private ArrayList<String> fotos;
    private Context context;

    public AdaptadorFotos(Context context,ArrayList<String> fotos){
        this.context=context;
        this.fotos=fotos;
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
        convertView= LayoutInflater.from(context).inflate(R.layout.grid_fotos_item,null);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen_fotos);
        String foto=fotos.get(position);
        Glide.with(context).load(foto).into(imagen);
        return convertView;
    }
}
