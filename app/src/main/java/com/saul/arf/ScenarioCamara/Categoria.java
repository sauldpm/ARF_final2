package com.saul.arf.ScenarioCamara;

import java.io.Serializable;
import java.util.ArrayList;

public class Categoria implements Serializable {
    private int id;
    private String nombre;
    private ArrayList<SubCategoria> subCategorias;
    private ArrayList<String> arrayListNombresSubCategorias;

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.subCategorias=new ArrayList<SubCategoria>();
        this.arrayListNombresSubCategorias = new ArrayList<String>();
    }
    public Categoria(int id, String nombre, ArrayList<SubCategoria> subCategorias) {
        this.id = id;
        this.nombre = nombre;
        this.subCategorias= subCategorias;
        this.arrayListNombresSubCategorias = new ArrayList<String>();
        //this.setListNombres();
    }

    public void addSubCategoria(SubCategoria subCategoria){
        subCategorias.add(subCategoria);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<SubCategoria> getSubCategorias() {
        return subCategorias;
    }

    public String toString() {
        return nombre+": "+subCategorias.toString()+"\n";
    }

    public void setArrayListNombresSubCategorias(ArrayList<String> arrayListNombresSubCategorias) {
        this.arrayListNombresSubCategorias = arrayListNombresSubCategorias;
    }

    public void setListNombres(){
        ArrayList<String> nombresSubcategorias = new ArrayList<>();
        for(int i=0; i<this.subCategorias.size(); i++){
            nombresSubcategorias.add(this.subCategorias.get(i).getNombre());
        }
        this.setArrayListNombresSubCategorias(nombresSubcategorias);
    }

    public ArrayList<String> getArrayListNombresSubCategorias() {
        return arrayListNombresSubCategorias;
    }
}
