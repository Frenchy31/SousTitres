package com.soustitres.servlets;

import com.soustitres.beans.Paragraphe;
import com.soustitres.beans.SousTitre;
import com.soustitres.beans.Video;
import com.soustitres.dao.*;
import com.soustitres.utilities.Utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

@WebServlet("/EditSubtitle")
public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private SousTitreDao sousTitreDao;
    private DaoFactory daoFactory;
	private ParagrapheDao paragrapheDao;
	private VideoDao videoDao;

	public void init(){
    	this.daoFactory = DaoFactory.getInstance();
    	this.videoDao = this.daoFactory.getVideoDao();
    	this.sousTitreDao = this.daoFactory.getSousTitreDao();
    	this.paragrapheDao = this.daoFactory.getParagrapheDao();
    	Locale.setDefault(new Locale("FR"));
	}
	//Récupère et gère les données obligatoirement affichées par la page
	private void affichageParDefaut(HttpServletRequest request) {
		Video video;
		SousTitre sousTitre;
		SousTitre sousTitreTraduit;
		try {
			//new SubtitlesHandler(context.getRealPath("/resources/password_presentation.srt"),1, new Locale("FR"), sousTitreDao, paragrapheDao);
			video = videoDao.getOne(Integer.valueOf(request.getParameter("idVideo")));
			sousTitre = sousTitreDao.getOne(Integer.valueOf(request.getParameter("idVideo")), new Locale(request.getParameter("langue")));
			request.setAttribute("idVideo", video.getId());
			request.setAttribute("titreVideo", video.getNom());
			request.setAttribute("langue", sousTitre.getLocale());
			request.setAttribute("paragraphes", paragrapheDao.lister(sousTitre.getId()));
			if(request.getParameter("langueTrad") != null ) {
				sousTitreTraduit = sousTitreDao.getOne(Integer.valueOf(request.getParameter("idVideo")), new Locale(request.getParameter("langueTrad")));
				if(sousTitreTraduit != null) {
					request.setAttribute("langueTraduction", sousTitre.getLocale().getDisplayLanguage().toLowerCase());
					ArrayList<Paragraphe> paragraphesTraduits = paragrapheDao.lister(sousTitreTraduit.getId());
					request.setAttribute("paragraphesTraduits", paragraphesTraduits);
				}
			}
				request.setAttribute("locales", Utils.listCountries());
		} catch (DaoException e) {
			request.setAttribute("erreur", e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		ServletContext context = getServletContext();
		affichageParDefaut(request);
//		try {
//			subtitles = new SubtitlesHandler(context.getRealPath(),1, new Locale("FR"), sousTitreDao, paragrapheDao);
//		} catch (DaoException e) {
//			request.setAttribute("erreur", e.getMessage());
//		}
			this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// On récupère et on traite les paramètres envoyés pas le formulaire
			HashMap<String, String[]> parameters = new HashMap<>(request.getParameterMap());

			// On vérifie qu'un sous-titre dans la même langue n'existe pas déjà
			SousTitre sousTitre = sousTitreDao.getOne(Integer.parseInt(request.getParameter("idVideo")), new Locale(request.getParameter("langueTrad")));

			// On retire les champs inutiles des paramètres
			parameters.remove("idVideo");
			parameters.remove("langueTrad");

			//Si le sous-titre n'existe, on va gérer la création des nouveaux paragraphes associés
			if(sousTitre == null){
				sousTitre = new SousTitre(Integer.parseInt(request.getParameter("idVideo")), new Locale(request.getParameter("langueTrad")));
				Integer newSousTitreId = sousTitreDao.ajouter(sousTitre);
				traiteAjoutParagraphes(parameters, newSousTitreId);
			}
			else{
				//Sinon on ne s'occupe que de la mise à jour.
				ArrayList<Paragraphe> paragraphes = paragrapheDao.lister(sousTitre.getId())	;
				traiteUpdateParagraphes(paragraphes, parameters);
			}
		} catch (DaoException e) {
		    request.setAttribute("erreur", e.getMessage());
		}
		Video video = null;
		try {
			video = videoDao.getOne(Integer.valueOf(request.getParameter("idVideo")));
		} catch (DaoException e) {
			request.setAttribute("erreur", e.getMessage());
		}
		affichageParDefaut(request);
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	/**
	 * Compare les textes pour gérer ou non la mise à jour de la base
	 * @param paragraphes
	 * @param parameters
	 * @throws DaoException
	 */
	private void traiteUpdateParagraphes(ArrayList<Paragraphe> paragraphes, HashMap<String, String[]> parameters) throws DaoException {
		for(int line = 1 ; line<=paragraphes.size() ; line++){
			String keyTexte = "line"+line;
			if(!String.valueOf(paragraphes.get(line-1).getTexteAffiche()).equals(String.valueOf(parameters.get(keyTexte)[0])))
				paragrapheDao.updateTexte(paragraphes.get(line-1), String.valueOf(parameters.get(keyTexte)[0]));
		}
	}

	/**
	 * Créé les nouveaux paragraphes et gère les ajouts dans la base
	 * @param parameters
	 * @param newSousTitreId
	 * @throws DaoException
	 */
	private void traiteAjoutParagraphes(HashMap<String, String[]> parameters, Integer newSousTitreId) throws DaoException {
		for (int line = 1 ; line<=parameters.size()/3 ; line++) {
			String keyTpsDebut = "tpsDebut"+line;
			String keyTpsFin = "tpsFin"+line;
			String keyTexte = "line"+line;
			Paragraphe paragraphe = new Paragraphe();
			paragraphe.setIdSousTitre(newSousTitreId);
			paragraphe.setNumParagraphe(line);
			paragraphe.setTempsDebut(parameters.get(keyTpsDebut)[0]);
			paragraphe.setTempsFin(parameters.get(keyTpsFin)[0]);
			paragraphe.setTexteAffiche(parameters.get(keyTexte)[0]);
			paragrapheDao.ajouter(paragraphe);
		}
	}
}