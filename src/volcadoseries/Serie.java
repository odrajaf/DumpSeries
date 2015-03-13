/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volcadoseries;

import java.util.Date;

/**
 *
 * @author fajardo
 */
public class Serie {

    int id;
    String nombre;
    String imagen;
    String sinopsis;
    Date agno;
    String pais;
    String directores;
    String guionistas;
    String productores;
    int numTemporadas;

    public Serie(String nombre, String imagen) {
        this.id = -1;
        this.nombre = nombre;
        this.imagen = imagen;
        this.sinopsis = "";
        this.agno = new Date();
        this.pais = "";
        this.directores = "";
        this.guionistas = "";
        this.productores = "";
        this.numTemporadas = 0;
    }

    public Serie(int id, String nombre, String imagen, String sinopsis, Date agno,
            String pais, String directores, String guionistas, String productores, int numTemporadas) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.sinopsis = sinopsis;
        this.agno = agno;
        this.pais = pais;
        this.directores = directores;
        this.guionistas = guionistas;
        this.productores = productores;
        this.numTemporadas = numTemporadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumTemporadas() {
        return numTemporadas;
    }

    public void setNumTemporadas(int numTemporadas) {
        this.numTemporadas = numTemporadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Date getAgno() {
        return agno;
    }

    public void setAgno(Date agno) {
        this.agno = agno;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDirectores() {
        return directores;
    }

    public void setDirectores(String directores) {
        this.directores = directores;
    }

    public String getGuionistas() {
        return guionistas;
    }

    public void setGuionistas(String guionistas) {
        this.guionistas = guionistas;
    }

    public String getProductores() {
        return productores;
    }

    public void setProductores(String productores) {
        this.productores = productores;
    }

}
