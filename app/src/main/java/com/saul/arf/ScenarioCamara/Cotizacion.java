package com.saul.arf.ScenarioCamara;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.TransformableNode;
import com.saul.arf.Escenarios.Escenario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Cotizacion implements Serializable {
    private int id_user;
    private int id_proyecto;
    private Escenario escenario;
    private ArrayList<Mueble> muebles;
    private ArrayList<TransformableNode> muebleEnEscenario;
    private ArrayList<String> fotos;
    private Double presupuesto;
    private static Mueble muebleActual;

    public Cotizacion(int id_user, int id_proyecto,Double presupuesto) {
        this.id_user = id_user;
        this.id_proyecto = id_proyecto;
        muebles = new ArrayList<Mueble>();
        muebleEnEscenario = new ArrayList<TransformableNode>();
        fotos = new ArrayList<String>();
        this.presupuesto = presupuesto;
    }

    public void agregarMueble(TransformableNode mueble){
        muebleEnEscenario.add(mueble);
        muebles.add(muebleActual);
        restarPrecioMueble();
    }

    public void restarPrecioMueble(){
        this.presupuesto-=muebleActual.getPrice();
    }
    public void sumarPrecioMueble(Double precio){
        this.presupuesto+=precio;
    }

    public void eliminarMueble(TransformableNode mueble){
        int index=muebleEnEscenario.indexOf(mueble);
        muebles.remove(index);
        muebleEnEscenario.remove(index);
    }

    public void addFoto(String foto){
        fotos.add(foto);
    }

    public Double total(){
        Double total=0.0;
        for (int x=0;x<muebles.size();x++)
            total+=muebles.get(x).getPrice();
        BigDecimal bd = new BigDecimal(total);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        total=bd.doubleValue();
        return total;
    }

    public void borrarMuebles(){
        muebleEnEscenario=null;
    }

    public static  void eliminarMuebleActual() {
        muebleActual=null;
    }

    public static  void setMuebleActual(Mueble m) {
        muebleActual=null;
        muebleActual = m;
    }

    public Mueble getMuebleActual() {
        return muebleActual;
    }

    public ArrayList<Mueble> getMuebles() {
        return muebles;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_proyecto() {
        return id_proyecto;
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }
}

