package com.saul.arf.Muebles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.saul.arf.Menu.MenuActivity;
import com.saul.arf.Menu.MessageFragment;
import com.saul.arf.ScenarioCamara.Cotizacion;
import com.saul.arf.ScenarioCamara.Mueble;

public class MueblesClickListener implements AdapterView.OnItemClickListener {
    private Activity activity;
    private Context context;
    private int id_usuario;
    private Mueble mueble;

    public MueblesClickListener(Activity activity,Context context,Mueble mueble){
        this.activity=activity;
        this.context=context;
        this.mueble=mueble;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mueble item = (Mueble) parent.getItemAtPosition(position);
        MenuActivity.createModel(item.getModelo3D());
        Cotizacion.setMuebleActual(item);
        MessageFragment.finalizar();
        System.out.println("MUEBLE"+item.toString());
    }
}
