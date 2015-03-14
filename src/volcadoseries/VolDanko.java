/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volcadoseries;

import java.util.ArrayList;
import static volcadoseries.VolcadoSeries.extraer_html_array;
import static volcadoseries.VolcadoSeries.extraer_texto;
import static volcadoseries.VolcadoSeries.getNombreServidor;

/**
 *
 * @author fajardo
 */
public class VolDanko {

    Persistencia per;
    boolean copiaProfunda =  false;

    public VolDanko() {
        this.per = new Persistencia();
    }

    public ArrayList<String> getServidoresSeriesDanko(String url, Capitulo cap) {
        //Enlaces de un capitulo
        ArrayList<String> array_servidores = new ArrayList<>();

        String file_contents = VolcadoSeries.getContenidoHTML(url);

        String titulo;
        String imagen = ""; //no hay imagenes en esta pagina
        String url_host;
        String servidor;
        String idioma;
        String calidad = "";

        if (file_contents != null) {
            //item_Actual
            titulo = extraer_texto(file_contents, "<h3 class='post-title entry-title'>", "</h3>");
            titulo = titulo.replace("\n", "");
            titulo = titulo.replace(" ver online o descargar", "");

            //this.item_Actual=new Item_menu(titulo,imagen,null,url,descripcion);
            file_contents = extraer_texto(file_contents, "Opciones online (sin descarga)", "Opciones de descarga");
            ArrayList<String> array_aux = extraer_html_array(file_contents, "<img src=", "Ver online");
            file_contents = "";

            for (int i = 0; i < array_aux.size(); i++) {
                String idioma_aux[] = extraer_texto(array_aux.get(i), "'/", "'").split("/");
                idioma = idioma_aux[idioma_aux.length - 1];
                switch (idioma) {
                    case "es.png":
                        idioma = "Español";
                        break;
                    case "vos.png":
                        idioma = "V.O.S.E";
                        break;
                    case "la.png":
                        idioma = "Latino";
                        break;
                    case "ca.png":
                        idioma = "Catalan";
                        break;
                    default:
                        idioma = "VO";
                        break;
                }

                url_host = "http://seriesdanko.com/" + extraer_texto(array_aux.get(i), "href='", "'");

                file_contents = VolcadoSeries.getContenidoHTML(url_host);
                String url_video = extraer_texto(file_contents, "<div id=\"oculto\" style=\"display:none\">", "</div>");
                url_video = extraer_texto(url_video, "<a href=\"", "\"");

                servidor = getNombreServidor(url_video);
                //PARCHE ERROR SERIES DANKO
                int indiceEsBlanco = url_video.indexOf("\t");
                if (servidor.compareTo("Allmyvideos") == 0 && indiceEsBlanco != -1) {
                    url_video = url_video.substring(0, indiceEsBlanco);
                }

                /////////////// ENLACE
                Enlace enlaAInser = per.selectEnlace(cap.getId(), url_video); //comprobamos
                if (enlaAInser == null) {//INSERTAMOS
                    enlaAInser = new Enlace(url_video, servidor, idioma, calidad);
                    per.insertarEnlace(enlaAInser, cap.getId());
                }
                ///////////////

                System.out.println(url_video);
                System.out.println(servidor);
//                System.out.println(idioma);
//                System.out.println(calidad);
            }
        }
        return array_servidores;
    }

    public ArrayList<String> parseseriesdankoGetCapitulos(String params, Serie serieIns) {

        boolean aModificar = false;
        //retorna el listado de capitulos de una serie
        //http://seriesdanko.com//serie.php?serie=1

        ArrayList<String> array_playlist = new ArrayList<>();
        String imagen;

        String file_contents = VolcadoSeries.getContenidoHTML(params);

        String titulo = extraer_texto(file_contents, "<h3 class='post-title entry-title'>", "</h3>");
        titulo = titulo.replace("\n", "");

        ArrayList<String> array_temporadas = extraer_html_array(file_contents, "class='ict'", "</span>");
        //NUM DE TEMPORADAS
        int numTempo = array_temporadas.size();

        if (serieIns.getNumTemporadas() != numTempo) {//ACTUALIZAMOS EL CAMPO DE SERIE
            serieIns.setNumTemporadas(numTempo);
            per.updateSerie(serieIns);
            aModificar = true;
        }
        ///////////

        for (int i = 0; i < numTempo; i++) {
            imagen = extraer_texto(array_temporadas.get(i), "src=", "'");

            ArrayList<String> array_capitulos = extraer_html_array(array_temporadas.get(i), "<a href", "<Br>");
            //NUM DE CAPITULOS
            int numCaps = array_capitulos.size();
            //COMPROBAMOS EN la BD la temporada con el num de capitulos
            //Contamos con el id de serie
            Temporada temp = per.selectTemporada(serieIns.getId(), (i + 1));
            if (temp != null) {
                if (temp.getNumCapitulos() != numCaps) {//ACTUALIZAMOS
                    temp.setNumCapitulos(numCaps);
                    per.updateTemporada(temp);
                    aModificar = true;
                }

            } else { //INSERTAMOS

                temp = new Temporada("", (i + 1), numCaps);
                per.insertarTemporada(temp, serieIns.getId()); //el objeto temp también obtendrá su id
                aModificar = true;

            }
//            System.out.println("Numero de Capítulos: " + numCaps + " Temporada " + (i + 1) + "\n");
            ///////////////

            for (int k = 0; k < numCaps; k++) {

                String text_aux[] = extraer_texto(array_capitulos.get(k), "='", "</a>").split(">");
                String url_capitulo = "http://seriesdanko.com//" + text_aux[0].substring(0, text_aux[0].length() - 1);
                titulo = text_aux[1];

                String idiomas = "";
                //Obtener idiomas
                ArrayList<String> array_flags = extraer_html_array(array_capitulos.get(k), "<img src=", "/>");

                for (int l = 0; l < array_flags.size(); l++) {
                    String flag[] = extraer_texto(array_flags.get(l), "/", " ").split("/");

                    if (flag[flag.length - 1].equals("es.png")) {
                        idiomas = idiomas + " Español";
                    }
                    if (flag[flag.length - 1].equals("la.png")) {
                        idiomas = idiomas + " Latino";
                    }
                    if (flag[flag.length - 1].equals("vos.png")) {
                        idiomas = idiomas + " VOSE";
                    }
                    if (flag[flag.length - 1].equals("vo.png")) {
                        idiomas = idiomas + " VO";
                    }
                    if (flag[flag.length - 1].equals("ca.png")) {
                        idiomas = idiomas + " Catalan";
                    }
                }

                String arrayNumCap[] = titulo.split("Capitulo ");
                String strNumDelCap = arrayNumCap[arrayNumCap.length - 1];
                strNumDelCap = strNumDelCap.replaceAll("\\.", "");
                strNumDelCap = strNumDelCap.trim();
                int numDelCap = Integer.parseInt(strNumDelCap); //prefiero que el error pare la ejeción 

                ///COMPROBACION CAPITULOS
                Capitulo aInsCap = per.selectCapitulo(temp.getId(), numDelCap);
                if (aInsCap == null) {//INSERTAMOS

                    aInsCap = new Capitulo("", numDelCap, idiomas);
                    per.insertarCapitulo(aInsCap, temp.getId());
                    aModificar = true;

                } else {

                    if (aInsCap.getIdiomas().compareTo(idiomas) != 0) { //ACTUALIZAMOS

                        aInsCap.setIdiomas(idiomas);
                        aInsCap.setNombre("");
                        per.updateCapitulo(aInsCap);
                        aModificar = true;
                    }

                }
                //////////////

//                System.out.println(titulo);
//                System.out.println(idiomas);
//                System.out.println(imagen);
//                System.out.println("Importante: " + url_capitulo);
                if (aModificar || copiaProfunda) {
                    getServidoresSeriesDanko(url_capitulo, aInsCap);
                }

            }
        }

        return array_playlist;
    }

    public ArrayList<String> parseseriesdankoBuscar(String params) { //enlace http://seriesdanko.com/series.php?id=0
        ArrayList<String> array_playlist = new ArrayList<>();

        String file_contents = VolcadoSeries.getContenidoHTML(params);

        file_contents = extraer_texto(file_contents, "<!-- Aquí comienza el rollo de la lista de Series -->", "<div class='blog-pager' id='blog-pager'>");
        file_contents = file_contents + "text-align:center;'>";
        ArrayList<String> array_aux = extraer_html_array(file_contents, "<a href=", "text-align:center;'>");
        file_contents = "";

        for (int i = 0; i < array_aux.size(); i++) {
            String url_serie = extraer_texto(array_aux.get(i), "'", "'");
            if (url_serie.contains("../")) {
                url_serie = url_serie.substring(3);
            }
            url_serie = "http://seriesdanko.com//" + url_serie;
            String titulo = extraer_texto(array_aux.get(i), "title='Capitulos de: ", "'");
            String imagen = extraer_texto(array_aux.get(i), "src='", "'");

            System.out.println("\n" + titulo + " - " + url_serie);

            //Comprobamos que está la serie
            Serie serieAInsertar = per.selectSerie(titulo);

            //Si no está en la BD lo insertamos
            if (serieAInsertar == null) {
                serieAInsertar = new Serie(titulo, imagen);
                per.insertarSerie(serieAInsertar);
            }

            parseseriesdankoGetCapitulos(url_serie, serieAInsertar);

        }
        return array_playlist;
    }

    public ArrayList<String> parseseriesdankoNew(String params) {
//retorna el listado de las ultimas series actualizadas
        ArrayList<String> array_playlist = new ArrayList<>();
        String file_contents = VolcadoSeries.getContenidoHTML(params);

        file_contents = extraer_texto(file_contents, "<div class='post hentry'", "<div class='sidebar section' id='sidebar'>");
        ArrayList<String> array_aux = extraer_html_array(file_contents, "<h3 class='post-title entry-title'", "<div class='post-header-line-1'>");
        file_contents = "";
        int l = array_aux.size();
        for (int i = 0; i < l; i++) {
            String titulo = extraer_texto(array_aux.get(i), ">", "</");
            String text_aux = extraer_texto(array_aux.get(i), "<div class='post-header'>", "</div>");
            String url_serie = extraer_texto(text_aux, "href=\"", "\"");

            if (url_serie.startsWith("serie.php?")) {
                url_serie = "http://seriesdanko.com//" + url_serie;
                String imagen = extraer_texto(text_aux, "src='", "'");
                //System.out.println(titulo +" "+ imagen +" "+ url_serie);
                System.out.println("\n" + titulo);
                System.out.println(url_serie);

                //Comprobamos que está la serie
                Serie serieAInsertar = per.selectSerie(titulo);

                //Si no está en la BD lo insertamos
                if (serieAInsertar == null) {
                    serieAInsertar = new Serie(titulo, imagen);
                    per.insertarSerie(serieAInsertar);
                }

                parseseriesdankoGetCapitulos(url_serie, serieAInsertar);
                //array_playlist.push(new Item_menu(titulo, imagen, params.page_uri, url_serie));

            }

        }
        return array_playlist;
    }

}
