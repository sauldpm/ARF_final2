package com.saul.arf.ScenarioCamara;

import java.io.Serializable;
import java.util.ArrayList;

public class SubCategoria implements Serializable {
    private int id;
    private String nombre;
    private ArrayList<Mueble> muebles;
    private ArrayList<String> arrayListNombresMuebles;

    public SubCategoria(int id,String nombre) {
        this.id=id;
        this.nombre = nombre;
        this.muebles=new ArrayList<Mueble>();
        this.setArrayListNombresMuebles(new ArrayList<String>());
    }

    public void addMueble(Mueble mueble){
        muebles.add(mueble);
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Mueble> getMuebles() {
        return muebles;
    }

    @Override
    public String toString() {
        return nombre+": "+muebles.toString();
    }

    public void setListNombres(){
        ArrayList<String> nombresMuebles = new ArrayList<String>();
        for(int i=0; i<this.muebles.size(); i++){
            nombresMuebles.add(this.muebles.get(i).getName());
        }
        this.setArrayListNombresMuebles(nombresMuebles);
    }

    public void setArrayListNombresMuebles(ArrayList<String> arrayListNombresMuebles) {
        this.arrayListNombresMuebles = arrayListNombresMuebles;
    }

    public ArrayList<String> getArrayListNombresMuebles() {
        return arrayListNombresMuebles;
    }

}
