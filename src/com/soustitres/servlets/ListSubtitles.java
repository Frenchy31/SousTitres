package com.soustitres.servlets;

import com.soustitres.beans.Video;
import com.soustitres.dao.*;
import com.soustitres.utilities.SubtitlesHandler;
import com.soustitres.utilities.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ListSubtitles extends HttpServlet {

    private VideoDao videoDao;
    private SousTitreDao sousTitreDao;
    private SubtitlesHandler subtitlesHandler;
    private ParagrapheDao paragrapheDao;

    public ListSubtitles(){
        super();
    }

    public void init() throws ServletException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        this.videoDao = daoFactory.getVideoDao();
        this.sousTitreDao = daoFactory.getSousTitreDao();
        this.paragrapheDao = daoFactory.getParagrapheDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Video video = videoDao.getOneByName(request.getParameter("description"));
            if(video!= null){
                traiteFichier(request, video);
            }
            else{
                video = new Video(request.getParameter("description"));
                video.setId(videoDao.ajouter(video));
                traiteFichier(request,video);
            }
//            SubtitlesHandler stHandler = new SubtitlesHandler();
        } catch (DaoException e) {
            request.setAttribute("erreur", e.getMessage());
        }
        //if(ajoute)
            //subtitleHandler.traiteAjoutFichier(request.getParameter("nomVideo"),
                //new Locale(request.getParameter("langue")),
                //request.getPart("fichier") );
        affichageParDefaut(request);
        this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void traiteFichier(HttpServletRequest request, Video video) throws IOException, ServletException, DaoException {
        String description = request.getParameter("description");
        // On récupère le champ du fichier
        Part part = request.getPart("fichier");
        // On vérifie qu'on a bien reçu un fichier
        String nomFichier = Utils.getNomFichier(part);
        // Si on a bien un fichier
        if (nomFichier != null && !nomFichier.isEmpty()) {
            Utils.ecrisFichier(part, nomFichier);
            SubtitlesHandler stHandle = new SubtitlesHandler(nomFichier, video.getId(), new Locale(request.getParameter("langue")), sousTitreDao, paragrapheDao );
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        affichageParDefaut(request);
        this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void affichageParDefaut(HttpServletRequest request) {
        ArrayList<Video> videos;
        try {
            videos = videoDao.lister();
            for (Video video: videos ) {
                video.setSousTitres(sousTitreDao.lister(video.getId()));
            }
            request.setAttribute("videos", videos);
            request.setAttribute("locales", Utils.listCountries());
        } catch (DaoException e) {
            request.setAttribute("erreur", e.getMessage());
        }
    }

}
