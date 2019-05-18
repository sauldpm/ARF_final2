package com.saul.arf.Escenarios;

import java.io.Serializable;
import java.util.ArrayList;

public class Escenario implements Serializable {
    private int id_proyecto;
    private int id_escenario;
    private String nombreProyecto;
    private String presupuesto;
    private String tamanio;
    private String tipoHabitacion;
    private String tipoMuebles;
    private ArrayList<String> rutasFotos;


    public Escenario(int id_proyecto, String nombreProyecto, String presupuesto,String tamanio,String tipoHabitacion, String tipoMuebles){
        this.id_proyecto=id_proyecto;
        this.nombreProyecto=nombreProyecto;
        this.presupuesto=presupuesto;
        this.tamanio=tamanio;
        this.tipoHabitacion=tipoHabitacion;
        this.tipoMuebles=tipoMuebles;
        this.rutasFotos=new ArrayList<String>();
    }

    public Escenario(int id_proyecto,int id_escenario, String nombreProyecto, String presupuesto,String tamanio,String tipoHabitacion, String tipoMuebles,ArrayList<String> rutasFotos){
        this.id_proyecto=id_proyecto;
        this.id_escenario=id_escenario;
        this.nombreProyecto=nombreProyecto;
        this.presupuesto=presupuesto;
        this.tamanio=tamanio;
        this.tipoHabitacion=tipoHabitacion;
        this.tipoMuebles=tipoMuebles;
        this.rutasFotos=rutasFotos;
    }

    public Escenario(int id_escenario,String nombreProyecto, String presupuesto, String tamanio, String tipoHabitacion, String tipoMuebles,ArrayList<String> rutasFotos){
        this.id_escenario=id_escenario;
        this.nombreProyecto=nombreProyecto;
        this.presupuesto=presupuesto;
        this.tamanio=tamanio;
        this.tipoHabitacion=tipoHabitacion;
        this.tipoMuebles=tipoMuebles;
        this.rutasFotos=rutasFotos;
    }

    public Escenario(String nombreProyecto){
        this.id_proyecto=0;
        this.nombreProyecto=nombreProyecto;
        this.presupuesto="";
        this.tamanio="";
        this.tipoHabitacion="";
        this.tipoMuebles="";
        this.rutasFotos=new ArrayList<String>();
    }

    public int getId_escenario() { return id_escenario; }

    public void setId_escenario(int id_escenario) { this.id_escenario = id_escenario; }

    public void agregarRutaFotos(String ruta){
        rutasFotos.add(ruta);
    }

    public ArrayList<String> getRutasFotos() { return rutasFotos; }

    public void setRutasFotos(ArrayList<String> rutasFotos) { this.rutasFotos = rutasFotos; }

    public int getId_proyecto() { return id_proyecto; }

    public void setId_proyecto(int id_proyecto) { this.id_proyecto = id_proyecto; }

    public String getNombreProyecto() { return nombreProyecto; }

    public void setNombreProyecto(String nombreProyecto) { this.nombreProyecto = nombreProyecto; }

    public String getPresupuesto() { return presupuesto; }

    public void setPresupuesto(String presupuesto) { this.presupuesto = presupuesto; }

    public String getTamanio() { return tamanio; }

    public void setTamanio(String tamanio) { this.tamanio = tamanio; }

    public String getTipoHabitacion() { return tipoHabitacion; }

    public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }

    public String getTipoMuebles() { return tipoMuebles; }

    public void setTipoMuebles(String tipoMuebles) { this.tipoMuebles = tipoMuebles; }

    public String toString (){
        String mensaje=id_escenario+" "+nombreProyecto+" "+presupuesto+" "+tamanio+" "+tipoHabitacion+" "+tipoMuebles;
        return mensaje;
    }
}
