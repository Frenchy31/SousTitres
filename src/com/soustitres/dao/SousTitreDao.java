package com.soustitres.dao;

import com.soustitres.beans.SousTitre;

import java.util.ArrayList;
import java.util.Locale;

public interface SousTitreDao {
    int ajouter(SousTitre sousTitre) throws DaoException;

    SousTitre getOne(Integer idVideo, Locale langue) throws DaoException;

    /**
     * Liste tous les sous-titres d'une video
     * @param idVideo
     * @return
     * @throws DaoException
     */
    ArrayList<SousTitre> lister(Integer idVideo) throws DaoException;
}
