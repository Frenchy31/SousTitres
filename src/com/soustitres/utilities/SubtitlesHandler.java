package com.soustitres.utilities;

import com.soustitres.beans.Paragraphe;
import com.soustitres.beans.SousTitre;
import com.soustitres.dao.DaoException;
import com.soustitres.dao.ParagrapheDao;
import com.soustitres.dao.SousTitreDao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitlesHandler {
	private ArrayList<Paragraphe> originalSubtitles = null;
	private ArrayList<String> translatedSubtitles = null;
	private static Pattern patternNumPara = Pattern.compile("^\\d+$");
	private static Pattern patternTemps = Pattern.compile("[0-9]+:[0-9]+:[0-9]+,[0-9]+ --> [0-9]+:[0-9]+:[0-9]+,[0-9]+");
	private static Pattern patternEOL = Pattern.compile("^$");
	private static Matcher matcherNumPara;
	private static Matcher matcherTemps;
	private static Matcher matcherEOL;

	public SubtitlesHandler(String fileName, Integer idVideo, Locale langue, SousTitreDao sousTitreDao, ParagrapheDao paragrapheDao) throws DaoException {
		// Gestion de l'ajout d'un fichier de Sous-Titre
		originalSubtitles = new ArrayList<Paragraphe>();
		BufferedReader br;
		if(sousTitreDao.getOne(idVideo,langue) != null)
			throw new DaoException("Le sous-titre existe déjà.");
		SousTitre sousTitre = new SousTitre(idVideo,langue);
		try {
			//Gère l'encodage du fichier en UTF8
			br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.RESOURCES_DIR+fileName), StandardCharsets.UTF_8));
			String line;
			String texte = "";
			Paragraphe paragrapheEnCours = new Paragraphe();

			//Tant que le fichier contient des lignes
			while ((line = br.readLine()) != null) {
				//Vérification des champs
				matcherNumPara = patternNumPara.matcher(line);
				matcherTemps = patternTemps.matcher(line);
				matcherEOL = patternEOL.matcher(line);
				//Si c'est un numéro de paragraphe
				if (matcherNumPara.find())
					paragrapheEnCours.setNumParagraphe(Integer.valueOf(line));
				else if (matcherTemps.find()){
					//Si la ligne correspond aux durées
					paragrapheEnCours.setTempsDebut(line.split("-->")[0].replaceAll(" ", ""));
					paragrapheEnCours.setTempsFin(line.split("-->")[1].replaceAll(" ", ""));
				}
				else if (matcherEOL.find()){
					//Si les paragraphes sont séparés
					paragrapheEnCours.setTexteAffiche(texte);
					texte = "";
					sousTitre.addParagraphe(paragrapheEnCours);
					paragrapheEnCours = new Paragraphe();
				}
				else
					texte += line + " ";
					//Sinon on ajoute un espace au texte lu pour gérer le cas de plusieurs lignes
			}
			//Gestion de l'insertion des nouveaux paragraphes dans la base
				sousTitre.setId(sousTitreDao.ajouter(sousTitre));
			this.originalSubtitles = sousTitre.getParagraphes();
			for (Paragraphe paragraphe : sousTitre.getParagraphes()) {
				paragraphe.setIdSousTitre(sousTitre.getId());
				paragrapheDao.ajouter(paragraphe);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Paragraphe> getSubtitles() {
		return originalSubtitles;
	}
}
