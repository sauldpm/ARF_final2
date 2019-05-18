package com.saul.arf.Resumen;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saul.arf.Proyectos.AdapterItem;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.Cotizacion;
import com.saul.arf.ScenarioCamara.Mueble;

import java.util.ArrayList;


public class AdaptadorTotal extends BaseAdapter {
    private Cotizacion cotizacion;
    private ArrayList<Mueble> muebles;
    private Context context;

    public  AdaptadorTotal(Cotizacion cotizacion,Context context){
        this.cotizacion=cotizacion;
        this.context=context;
        muebles=cotizacion.getMuebles();
    }


    @Override
    public int getCount() {
        return muebles.size();
    }

    @Override
    public Object getItem(int position) {
        return muebles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_total,null);
        TextView descripcion=convertView.findViewById(R.id.descripcion);
        TextView precio=convertView.findViewById(R.id.precio);
        descripcion.setText(muebles.get(position).getName());
        precio.setText(Double.toString(muebles.get(position).getPrice()));
        descripcion.setSelected(true);
        precio.setSelected(true);
        return convertView;
    }
}
