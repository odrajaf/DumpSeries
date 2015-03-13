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
public class Temporada {

    int id;
    String nombre;
    int numTemporada;
    int numCapitulos;

    public Temporada(String nombre, int numTemporada, int numCaps) {
        this.id = -1;
        this.nombre = nombre;
        this.numTemporada = numTemporada;
        this.numCapitulos = numCaps;
    }

    public Temporada(int id, String nombre, int numTemporada, int numCaps) {
        this.id = id;
        this.nombre = nombre;
        this.numTemporada = numTemporada;
        this.numCapitulos = numCaps;
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

    public int getNumCapitulos() {
        return numCapitulos;
    }

    public void setNumCapitulos(int numCapitulos) {
        this.numCapitulos = numCapitulos;
    }
    

    public int getNumTemporada() {
        return numTemporada;
    }

    public void setNumTemporada(int numTemporada) {
        this.numTemporada = numTemporada;
    }

}
