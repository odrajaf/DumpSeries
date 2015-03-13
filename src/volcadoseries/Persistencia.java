/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volcadoseries;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fajardo
 */
public class Persistencia {

    Connection con;
    ResultSet rs;
    Statement stat;

    public Persistencia() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/seriesTrol", "root", "nemesis");

            // Creamos un Statement para poder hacer peticiones a la bd
            stat = (Statement) con.createStatement();
        } catch (SQLException sqlex) {

            System.err.print("ERROR al conectar");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertarSerie(Serie aInsertar) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "INSERT INTO `seriesTrol`.`series`("
                    + "`nombre`,"
                    + "`imagen`,"
                    + "`sinopsis`,"
                    + "`agno`,"
                    + "`pais`,"
                    + "`directores`,"
                    + "`guionistas`,"
                    + "`productores`)"
                    + "VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            prepareInsert.setString(i++, aInsertar.getNombre());
            prepareInsert.setString(i++, aInsertar.getImagen());
            prepareInsert.setString(i++, aInsertar.getSinopsis());
            prepareInsert.setDate(i++, new Date(aInsertar.getAgno().getTime()));
            prepareInsert.setString(i++, aInsertar.getPais());
            prepareInsert.setString(i++, aInsertar.getDirectores());
            prepareInsert.setString(i++, aInsertar.getGuionistas());
            prepareInsert.setString(i++, aInsertar.getProductores());

            int numInser = prepareInsert.executeUpdate();

            ResultSet generatedKeys = prepareInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                long idSerie = generatedKeys.getLong(1);
                aInsertar.setId((int) idSerie);

            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            prepareInsert.close();

            if (numInser == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateSerie(Serie aActualizar) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "UPDATE `seriesTrol`.`series`\n"
                    + "SET\n"
                    + "`imagen` = ?,\n"
                    + "`sinopsis` = ?,\n"
                    + "`agno` = ?,\n"
                    + "`pais` = ?,\n"
                    + "`directores` = ?,\n"
                    + "`guionistas` = ?,\n"
                    + "`productores` = ?,\n"
                    + "`numTemporadas` = ?\n"
                    + "WHERE `id` = ?;");
            int i = 1;
            prepareInsert.setString(i++, aActualizar.getImagen());
            prepareInsert.setString(i++, aActualizar.getSinopsis());
            prepareInsert.setDate(i++, new Date(aActualizar.getAgno().getTime()));
            prepareInsert.setString(i++, aActualizar.getPais());
            prepareInsert.setString(i++, aActualizar.getDirectores());
            prepareInsert.setString(i++, aActualizar.getGuionistas());
            prepareInsert.setString(i++, aActualizar.getProductores());
            prepareInsert.setInt(i++, aActualizar.getNumTemporadas());
            prepareInsert.setInt(i++, aActualizar.getId());

            int numInser = prepareInsert.executeUpdate();
            prepareInsert.close();

            if (numInser == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Serie selectSerie(String nombre) {
        Serie serieBuscada = null;
        try {
            String seleccionar = "SELECT * FROM series WHERE nombre LIKE '" + nombre + "%' LIMIT 1";
            rs = stat.executeQuery(seleccionar);

            //id, nombre, imagen, sinopsis, agno, pais, directores, guionistas, productores
            while (rs.next()) {
                serieBuscada = new Serie(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("imagen"),
                        rs.getString("sinopsis"),
                        rs.getDate("agno"),
                        rs.getString("pais"),
                        rs.getString("directores"),
                        rs.getString("guionistas"),
                        rs.getString("productores"),
                        rs.getInt("numTemporadas"));
            }

            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serieBuscada;
    }

    public Temporada selectTemporada(int idSerie, int numTemporada) {
        Temporada tempoBuscada = null;
        try {
            String seleccionar = "SELECT temporadas.id,\n"
                    + " temporadas.nombre,\n"
                    + " temporadas.numNombre,\n"
                    + " temporadas.numCapitulos\n"
                    + "  FROM temporadas \n"
                    + "LEFT JOIN serieTemporada \n"
                    + "ON serieTemporada.idTemporada = temporadas.id \n"
                    + "WHERE  serieTemporada.idSerie = " + String.valueOf(idSerie)
                    + " AND temporadas.numNombre = " + String.valueOf(numTemporada); //" + nombre + "%' LIMIT 1";
            rs = stat.executeQuery(seleccionar);

            //id, nombre, imagen, sinopsis, agno, pais, directores, guionistas, productores
            while (rs.next()) {
                tempoBuscada = new Temporada(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("numNombre"),
                        rs.getInt("numCapitulos"));
            }

            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempoBuscada;
    }

    public boolean insertarTemporada(Temporada aInserTemp, int idSerie) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "INSERT INTO `seriesTrol`.`temporadas`\n"
                    + "(`nombre`,\n"
                    + "`numNombre`,\n"
                    + "`numCapitulos`)\n"
                    + "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            prepareInsert.setString(i++, aInserTemp.getNombre());
            prepareInsert.setInt(i++, aInserTemp.getNumTemporada());
            prepareInsert.setInt(i++, aInserTemp.getNumCapitulos());

            int numInser = prepareInsert.executeUpdate();
            int numRel = 0;

            ResultSet generatedKeys = prepareInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                long idTemporada = generatedKeys.getLong(1);
                aInserTemp.setId((int) idTemporada);
                //Insertamos en tabla serieTemporada
                PreparedStatement preRel = con.prepareStatement(
                        "INSERT INTO `seriesTrol`.`serieTemporada` "
                        + "(`idSerie`,"
                        + "`idTemporada`)"
                        + "VALUES(?,?)");

                i = 1;
                preRel.setLong(i++, idSerie);
                preRel.setLong(i++, idTemporada);
                numRel = preRel.executeUpdate();

                preRel.close();

            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            prepareInsert.close();

            if (numInser == 0 || numRel == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateTemporada(Temporada temp) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "UPDATE `seriesTrol`.`temporadas`\n"
                    + "SET\n"
                    + "`nombre` = ?,\n"
                    + "`numNombre` = ?,\n"
                    + "`numCapitulos` = ?\n"
                    + "WHERE `id` = ? ;");
            int i = 1;

            prepareInsert.setString(i++, temp.getNombre());
            prepareInsert.setInt(i++, temp.getNumTemporada());
            prepareInsert.setInt(i++, temp.getNumCapitulos());

            prepareInsert.setInt(i++, temp.getId());

            int numInser = prepareInsert.executeUpdate();
            prepareInsert.close();

            if (numInser == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean insertarCapitulo(Capitulo aInserCap, int idTemp) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "INSERT INTO `seriesTrol`.`capitulos`\n"
                    + "(`nombre`,\n"
                    + "`num`,\n"
                    + "`idiomas`)\n"
                    + "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            prepareInsert.setString(i++, aInserCap.getNombre());
            prepareInsert.setInt(i++, aInserCap.getNumDelCap());
            prepareInsert.setString(i++, aInserCap.getIdiomas());

            int numInser = prepareInsert.executeUpdate();
            int numRel = 0;

            ResultSet generatedKeys = prepareInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                long idCap = generatedKeys.getLong(1);
                aInserCap.setId((int) idCap);

                PreparedStatement preRel = con.prepareStatement(
                        "INSERT INTO `seriesTrol`.`temporadaCapitulo` "
                        + "(`idTemporada`,"
                        + "`idCapitulo`)"
                        + "VALUES(?,?)");

                i = 1;

                preRel.setLong(i++, idTemp);
                preRel.setLong(i++, idCap);
                numRel = preRel.executeUpdate();

                preRel.close();

            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            prepareInsert.close();

            if (numInser == 0 || numRel == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Capitulo selectCapitulo(int idTemporada, int numDelCap) {
        Capitulo capBuscada = null;
        try {
            String seleccionar = "SELECT capitulos.id,\n"
                    + " capitulos.nombre,\n"
                    + " capitulos.num,\n"
                    + " capitulos.idiomas\n"
                    + "  FROM capitulos \n"
                    + "LEFT JOIN temporadaCapitulo \n"
                    + "ON temporadaCapitulo.idCapitulo = capitulos.id \n"
                    + "WHERE  temporadaCapitulo.idTemporada = " + String.valueOf(idTemporada)
                    + "	AND capitulos.num = " + String.valueOf(numDelCap) + ";";
            rs = stat.executeQuery(seleccionar);

            //id, nombre, imagen, sinopsis, agno, pais, directores, guionistas, productores
            while (rs.next()) {
                capBuscada = new Capitulo(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("num"),
                        rs.getString("idiomas"));
            }

            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return capBuscada;
    }

    public boolean updateCapitulo(Capitulo cap) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "UPDATE `seriesTrol`.`capitulos`\n"
                    + "SET\n"
                    + "`nombre` = ?,\n"
                    + "`idiomas` = ?\n"
                    + "WHERE `id` = ? ;");
            int i = 1;

            prepareInsert.setString(i++, cap.getNombre());
            prepareInsert.setString(i++, cap.getIdiomas());

            prepareInsert.setInt(i++, cap.getId());

            int numInser = prepareInsert.executeUpdate();
            prepareInsert.close();

            if (numInser == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Enlace selectEnlace(int idCapitulo, String enlace) {
        Enlace enlaBuscada = null;
        try {
            String seleccionar = "SELECT enlaces.id,\n"
                    + " enlaces.enlace,\n"
                    + " enlaces.servidor,\n"
                    + " enlaces.idioma,\n"
                    + " enlaces.calidad\n"
                    + "  FROM enlaces \n"
                    + "LEFT JOIN enlaceCapitulo \n"
                    + "ON enlaceCapitulo.idEnlace = enlaces.id \n"
                    + "WHERE  enlaceCapitulo.idCapitulo = " + idCapitulo + "\n"
                    + "	AND enlaces.enlace = '" + enlace + "'";
            rs = stat.executeQuery(seleccionar);

            //id, nombre, imagen, sinopsis, agno, pais, directores, guionistas, productores
            while (rs.next()) {
                enlaBuscada = new Enlace(rs.getInt("id"),
                        rs.getString("enlace"),
                        rs.getString("servidor"),
                        rs.getString("idioma"),
                        rs.getString("calidad"));
            }

            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enlaBuscada;
    }

    public boolean insertarEnlace(Enlace enlaAInser, int idCap) {
        try {

            PreparedStatement prepareInsert = con.prepareStatement(
                    "INSERT INTO `seriesTrol`.`enlaces`\n"
                    + "(`enlace`,"
                    + "`servidor`,"
                    + "`idioma`,"
                    + "`calidad`) "
                    + "VALUES "
                    + "(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            prepareInsert.setString(i++, enlaAInser.getEnlace());
            prepareInsert.setString(i++, enlaAInser.getServidor());
            prepareInsert.setString(i++, enlaAInser.getIdioma());
            prepareInsert.setString(i++, enlaAInser.getCalidad());

            int numInser = prepareInsert.executeUpdate();
            int numRel = 0;

            ResultSet generatedKeys = prepareInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                long idEnl = generatedKeys.getLong(1);
                enlaAInser.setId((int) idEnl);

                PreparedStatement preRel = con.prepareStatement(
                        "INSERT INTO `seriesTrol`.`enlaceCapitulo` "
                        + "(`idCapitulo`,"
                        + "`idEnlace`)"
                        + "VALUES(?,?)");

                i = 1;

                preRel.setLong(i++, idCap);
                preRel.setLong(i++, idEnl);
                numRel = preRel.executeUpdate();

                preRel.close();

            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            prepareInsert.close();

            if (numInser == 0 || numRel == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void cerrarConexion() {
        try {
            rs.close();
            stat.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
