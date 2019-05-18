package com.saul.arf.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import com.saul.arf.Muebles.AdaptadorMuebles;
import com.saul.arf.Muebles.MueblesClickListener;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.Mueble;
import java.util.ArrayList;

public class MessageFragment extends AppCompatDialogFragment {

    private Context context;
    private Activity activity;
    private GridView lista;
    private AdaptadorMuebles adaptador;
    private ArrayList<Mueble> arrayList_muebles;
    private int posicion=0;
    public static AlertDialog alertDialog;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.message_layout,null);
        lista=(GridView) v.findViewById(R.id.grid);
        context=getContext();
        activity=getActivity();
        this.setArrayList_muebles((ArrayList<Mueble>) getActivity().getIntent().getSerializableExtra("objetos"));
        adaptador=new AdaptadorMuebles(this.getContext(),this.getArrayList_muebles());
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new MueblesClickListener(activity,context,arrayList_muebles.get(posicion)));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        this.setAlertDialog(new AlertDialog.Builder(getActivity())
                .setView(v)
                .create());
        return this.getAlertDialog();
    }

    public ArrayList<Mueble> getArrayList_muebles() {
        return arrayList_muebles;
    }

    public void setArrayList_muebles(ArrayList<Mueble> arrayList_muebles) {
        this.arrayList_muebles = arrayList_muebles;
    }
    public static AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public static void finalizar(){
        alertDialog.dismiss();
    }

}
