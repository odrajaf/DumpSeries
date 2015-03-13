/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volcadoseries;

/**
 *
 * @author fajardo
 */
public class Capitulo {
    
    int id;
    String nombre;
    int numDelCap;
    String idiomas;

    public Capitulo(String nombre, int numDelCap, String idiomas) {
        this.id = -1;
        this.nombre = nombre;
        this.numDelCap = numDelCap;
        this.idiomas = idiomas;
    }

    public Capitulo(int id, String nombre, int numDelCap, String idiomas) {
        this.id = id;
        this.nombre = nombre;
        this.numDelCap = numDelCap;
        this.idiomas = idiomas;
    }
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumDelCap() {
        return numDelCap;
    }

    public void setNumDelCap(int numDelCap) {
        this.numDelCap = numDelCap;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }
    
    
    
}
