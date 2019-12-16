package com.soustitres.dao;

import com.soustitres.beans.Paragraphe;

import java.util.ArrayList;

public interface ParagrapheDao {
    boolean ajouter(Paragraphe paragraphe) throws DaoException;
    ArrayList<Paragraphe> lister(Integer idSousTitre) throws DaoException;
    boolean updateTexte(Paragraphe paragraphe, String texte) throws DaoException;
}
