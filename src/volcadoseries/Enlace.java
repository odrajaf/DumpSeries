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
public class Enlace {

    int id;
    String enlace;
    String servidor;
    String idioma;
    String calidad;

    public Enlace(int id, String enlace, String servidor, String idioma, String calidad) {
        this.id = id;
        this.enlace = enlace;
        this.servidor = servidor;
        this.idioma = idioma;
        this.calidad = calidad;
    }

    public Enlace(String enlace, String servidor, String idioma, String calidad) {
        this.enlace = enlace;
        this.servidor = servidor;
        this.idioma = idioma;
        this.calidad = calidad;
    }

      
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }
    
    

}
