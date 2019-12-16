package com.soustitres.servlets;

import com.soustitres.beans.Video;
import com.soustitres.dao.*;
import com.soustitres.utilities.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Locale;

import static com.soustitres.utilities.Utils.TAILLE_TAMPON;


public class Download extends HttpServlet {
    private DaoFactory daoFactory;
    private ParagrapheDao paragrapheDao;
    private SousTitreDao sousTitreDao;
    private VideoDao videoDao;

    public void init(){
        this.daoFactory = DaoFactory.getInstance();
        this.paragrapheDao = daoFactory.getParagrapheDao();
        this.sousTitreDao = daoFactory.getSousTitreDao();
        this.videoDao = daoFactory.getVideoDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathFichier = "";
        try {
            Video video = videoDao.getOne(Integer.valueOf(request.getParameter("idVideo")));
            pathFichier = Utils.creeFichierTraduit(video.getNom(), new Locale(request.getParameter("langue")),
                    paragrapheDao.lister(sousTitreDao.getOne(video.getId(), new Locale(request.getParameter("langue"))).getId()));
        } catch (DaoException e) {
            request.setAttribute("erreur", e.getMessage());
        }

        /* Vérifie qu'un fichier a bien été fourni */
        if ( pathFichier == null || "/".equals( pathFichier ) ) {
            /* Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas */
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        /* Décode le nom de fichier récupéré, susceptible de contenir des espaces et autres caractères spéciaux, et prépare l'objet File */
        File fichier = new File( pathFichier );

        /* Vérifie que le fichier existe bien */
        if ( !fichier.exists() ) {
            /* Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas */
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /* Récupère le type du fichier */
        String type = getServletContext().getMimeType( fichier.getName() );

        /* Si le type de fichier est inconnu, alors on initialise un type par défaut */
        if ( type == null ) {
            type = "application/octet-stream";
        }

        /* Initialise la réponse HTTP */
        response.reset();
        response.setBufferSize( TAILLE_TAMPON );
        response.setContentType( type );
        response.setHeader( "Content-Length", String.valueOf( fichier.length() ) );
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"" );
        /* Prépare les flux */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux */
            entree = new BufferedInputStream( new FileInputStream( fichier ), TAILLE_TAMPON );
            sortie = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );
            /* Lit le fichier et écrit son contenu dans la réponse HTTP */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur= entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
