/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volcadoseries;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.XYChart;

/**
 *
 * @author fajardo
 */
public class VolcadoSeries implements Runnable {

    char caracter;
    VolDanko seriesDanko;

    public VolcadoSeries(char caracter, VolDanko seriesDanko) {
        this.caracter = caracter;
        this.seriesDanko = seriesDanko;
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public void run() {
        seriesDanko.parseseriesdankoBuscar("http://seriesdanko.com/series.php?id=" + caracter);
    }

    public static void main(String[] args) {

        int numHilos = 10;
        Thread[] p1 = new Thread[numHilos];

        int numChar = 'A';
        char caracter = (char) numChar;
        while (caracter <= 'Z') {

            for (int i = 0; i < numHilos; i++) {
                if (p1[i] == null || !p1[i].isAlive()) {
                    if (caracter <= 'Z') {
                        Runnable proceso1 = new VolcadoSeries(caracter, new VolDanko());
                        p1[i] = new Thread(proceso1);
                        p1[i].start();
                        caracter = (char) ++numChar;
                    }
                }
            }

        }
//         VolDanko seriesDankocapError = new VolDanko();
//         seriesDankocapError.getServidoresSeriesDanko("http://seriesdanko.com/capitulo.php?serie=137&temp=5&cap=10", new Capitulo(15343, "", 10,""));
    }

    public static String getContenidoHTML(String urlWeb) {
        String contenido = "";
        try {
            URL url = new URL(urlWeb);
            URLConnection uc = url.openConnection();
            uc.connect();
            //Creamos el objeto con el que vamos a leer
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                contenido += inputLine + "\n";
            }
            in.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(VolcadoSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VolcadoSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contenido;
    }

    public static String extraer_texto(String texto, String cadena_inicial, String cadena_final) {
        //Extrae el texto de una cadena pasando una cadena inicial y otra final
        //Si no existe la cadena inicial o la cadena final dentro del texto retorna un texto vacio
        int pos_ini;
        int pos_final;

        pos_ini = texto.indexOf(cadena_inicial);
        if (pos_ini == -1) {
            return "";
        }

        texto = texto.substring(pos_ini + cadena_inicial.length());
        pos_final = texto.indexOf(cadena_final);
        if (pos_final == -1) {
            return "";
        }

        texto = texto.substring(0, pos_final);
        return texto;
    }

    public static ArrayList<String> extraer_html_array(String texto_html, String cadena_inicial, String cadena_final) {
        //Estrae el texto de manera repetitiva pasando cadena inicial, y final, y lo saca en un array
        int pos_ini;
        int pos_final;

        ArrayList<String> array_aux = new ArrayList<>();
        String aux_string;
        pos_ini = texto_html.indexOf(cadena_inicial);

        while (pos_ini != -1) {
            texto_html = texto_html.substring(pos_ini);
            pos_final = texto_html.indexOf(cadena_final);
            aux_string = texto_html.substring(0, pos_final + cadena_final.length());
            array_aux.add(aux_string);
            texto_html = texto_html.substring(pos_final);
            pos_ini = texto_html.indexOf(cadena_inicial);
        }
        return array_aux;
    }

    public static String getNombreServidor(String url) {
        String auxUrl[] = url.split("\\.");
        String aDevolver = "";
        try {
            if (auxUrl.length > 0) {
                int indi = url.indexOf("http://www.");
                if (indi != -1) {
                    aDevolver = auxUrl[1];
                } else {
                    aDevolver = auxUrl[0].substring(7); //http://
                }

                aDevolver = aDevolver.substring(0, 1).toUpperCase() + aDevolver.substring(1);
            }
        } catch (Exception ex) {
            aDevolver = "porDefecto";
        }
        return aDevolver;
    }

//
//    public static ArrayList<String> getServidoresPelisPekes(String url) {
//
//        String file_contents = VolcadoSeries.getContenidoHTML(url);
//
//        String titulo;
//        String imagen;
//        String url_video;
//        String servidor;
//        String idioma;
//        String calidad = "";
//        String descripcion;
//
//        //item_Actual
//        titulo = extraer_texto(file_contents, "<h1 class=\"peliculatitulo\">", "</h1>");
//        System.out.println(file_contents);
//        imagen = extraer_texto(file_contents, "alt=\"ver pelicula \" title=\"ver pelicula \" src=\"", "\">");
//        descripcion = extraer_texto(file_contents, "<b>Sinopsis:</b> ", " <div");
//
//        //this.item_Actual=new Item_menu(titulo,imagen,null,url,descripcion);
//        String aux_string = extraer_texto(file_contents, "<ul class=\"tabs\">", "</ul>");
//        ArrayList<String> array_aux = extraer_html_array(aux_string, "<li", "</li>");
//        //file_contents = "";
//        
//        System.out.println(aux_string);
//        System.out.println(descripcion);
//
//        ArrayList<String> array_servidores = new ArrayList<>();
//
//        for (int i = 0; i < array_aux.size(); i++) {
//
//            servidor = extraer_texto(array_aux.get(i), "<div class=\"", "\">");
//            if (servidor.equals("vkontakte")) {
//                servidor = "vk";
//            }
//            idioma = extraer_texto(array_aux.get(i), "menu\">", "</a>");
//            url_video = extraer_texto(file_contents, "<div class=\"tab_content\" id=\"' + (i+1) + '_content\"", "</div>");
//            url_video = extraer_texto(url_video, "<iframe src=\"", "\"");
//
//            System.out.println(url_video);
//            System.out.println(servidor);
//            System.out.println(idioma);
//            System.out.println(calidad);
////				var params={
////						"url_host" : url_video,
////						"servidor" : servidor,
////						"idioma" : idioma,
////						"calidad" : calidad
////						};
//
////				var objHost=HostFactory.createHost(servidor,params)
////				if (objHost)
////					{
////						array_servidores.push(objHost);
////					}				
////				}
//            array_servidores.add(servidor);
//
//        }
//        file_contents = "";
//        return array_servidores;
//    }
}
