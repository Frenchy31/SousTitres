package com.soustitres.utilities;

import com.soustitres.beans.Paragraphe;
import com.soustitres.dao.DaoException;

import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public abstract class Utils  {

    public static final int TAILLE_TAMPON = 10240;
    public static final String RESOURCES_DIR = "D:\\Travail\\IDEA_Projects\\SousTitres\\web\\resources\\"; // A changer
    /**
     * Renvoie la liste des différents pays au format suivant : "FR / France"
     * @return
     */
    public static ArrayList<String> listCountries() {
        ArrayList<String> locales = new ArrayList<String>();
        for (String countryCode : Locale.getISOCountries()) {
            Locale obj = new Locale("", countryCode);
            locales.add(obj.getCountry() + " / " + obj.getDisplayCountry());
        }
        return locales;
    }

    /**
     * Créé un nouveau fichier sous-titres qui sera renvoyé à l'utilisateur
     * @param titreVideo
     * @param langue
     * @param paragraphes
     * @throws DaoException
     * @return
     */
    public static String creeFichierTraduit(String titreVideo, Locale langue, ArrayList<Paragraphe> paragraphes) throws DaoException {
        titreVideo = titreVideo.replace(' ' , '_');
        titreVideo = titreVideo.replace('.', '_');
        String filePath = RESOURCES_DIR + titreVideo + "_" + langue.getCountry() + ".srt";
        ArrayList<String> lignesFichier = new ArrayList<String>();
        for(Paragraphe paragraphe : paragraphes){
            lignesFichier.add(String.valueOf(paragraphe.getNumParagraphe()));
            lignesFichier.add(paragraphe.getTempsDebut() + " --> " + paragraphe.getTempsFin());
            lignesFichier.add(paragraphe.getTexteAffiche());
            lignesFichier.add("\r");
        }

        final File fichier = new File(filePath);
        try {
            // Creation du fichier
            fichier.createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier);
            try {
                for (String ligne : lignesFichier ) {
                    if(!String.valueOf(ligne).equals("\r"))
                        writer.write(ligne + "\r");
                    else
                        writer.write(ligne);
                }
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }

        return filePath;
    }

    public static String getNomFichier(Part part) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ){
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }

    public static void ecrisFichier(Part part, String nomFichier) throws IOException {
        String nomChamp = part.getName();
        // Corrige un bug du fonctionnement d'Internet Explorer
        nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
                .substring(nomFichier.lastIndexOf('\\') + 1)    ;

        // On écrit définitivement le fichier sur le disque
        ecrireFichier(part, nomFichier, RESOURCES_DIR);
    }

    private static void ecrireFichier(Part part, String nomFichier, String chemin) throws IOException {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ((longueur = entree.read(tampon)) > 0) {
                sortie.write(tampon, 0, longueur);
            }
        } finally {
            try {
                sortie.close();
            } catch (IOException ignore) {
            }
            try {
                entree.close();
            } catch (IOException ignore) {
            }
        }
    }
}
