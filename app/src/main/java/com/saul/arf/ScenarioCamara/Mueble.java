package com.saul.arf.ScenarioCamara;

import java.io.Serializable;
import java.util.ArrayList;

public class Mueble implements Serializable {
    private  int id;
    private String name;
    private Double price;
    private String category;
    private int categoryId;
    private String subCategory;
    private int subCategoryId;
    private String modelo3D;
    private String image;
    private ArrayList<String> rutasFotos;


    public Mueble(int id, String name, Double price, String category, int categoryId, String subCategory, int subCategoryId, String modelo3D, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.categoryId = categoryId;
        this.subCategory = subCategory;
        this.subCategoryId = subCategoryId;
        this.modelo3D = modelo3D;
        this.image=image;
    }

    public String getImage() { return image; }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getModelo3D() {
        return modelo3D;
    }

    @Override
    public String toString() {
        return id+" "+name+" "+price+" "+category+" "+categoryId+" "+subCategoryId+" "+subCategory+" "+modelo3D+" "+image;
    }

    public ArrayList<String> getRutasFotos() {
        return rutasFotos;
    }

    public void setRutasFotos(ArrayList<String> rutasFotos) {
        this.rutasFotos = rutasFotos;
    }

    public void agregarRutaFotos(String ruta){
        rutasFotos.add(ruta);
    }
}