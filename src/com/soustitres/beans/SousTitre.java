package com.soustitres.beans;

import java.util.ArrayList;
import java.util.Locale;

public class SousTitre {

    private Integer id;
    private ArrayList<Paragraphe> paragraphes = new ArrayList<Paragraphe>();
    Locale locale;
    private Integer idVideo;

    public SousTitre(Integer id, Integer idVideo, Locale locale) {
        this.id = id;
        this.idVideo = idVideo;
        this.locale = locale;
    }

    public SousTitre(Integer idVideo, Locale locale) {
         this.idVideo = idVideo;
         this.locale = locale;
    }

    public void addParagraphe(Paragraphe paragraphe){
        this.paragraphes.add(paragraphe);
    }

    public Integer getId() {
        return id;
    }

    public ArrayList<Paragraphe> getParagraphes() {
        return paragraphes;
    }

    public void setParagraphes(ArrayList<Paragraphe> paragraphes) {
        this.paragraphes = paragraphes;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getIdVideo() {
        return this.idVideo;
    }

    public void setId(int id) {
        this.id = id;
    }
}
