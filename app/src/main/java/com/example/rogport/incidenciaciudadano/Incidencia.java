package com.example.rogport.incidenciaciudadano;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RogPort on 20/11/2016.
 */

public class Incidencia {

    private int id,likes;
    private String imagen;
    private String ubicacion,descripcion,fecha;
    private List<String> tokens = new ArrayList<String>();
    private List<Double> latlon = new ArrayList<Double>();

    public Incidencia(int id, String imagen, String ubi, String desc,List<Double> latlon, String dia) {
        this.id = id;
        this.imagen = imagen;
        this.ubicacion = ubi;
        this.descripcion = desc;
        this.likes = 0;
        this.tokens.add("");
        this.tokens.add("");
        this.tokens.add("");
        this.latlon = latlon;
        this.fecha = dia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Double> getLatlon() {
        return latlon;
    }

    public void setLatlon(List<Double> latlon) {
        this.latlon = latlon;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Incidencia(){}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
