package com.soustitres.beans;

public class Paragraphe {
    private Integer id;
    private Integer idSousTitre;
    private Integer numParagraphe;
    private String tempsDebut;
    private String tempsFin;
    private String texteAffiche;

    public Paragraphe(Integer id, Integer numParagraphe, String tempsDebut, String tempsFin, String texteAffiche) {
        this.id = id;
        this.numParagraphe = numParagraphe;
        this.tempsDebut = tempsDebut;
        this.tempsFin = tempsFin;
        this.texteAffiche = texteAffiche;
    }

    public Paragraphe() {

    }

    public Integer getId() {
        return id;
    }

    public int getNumParagraphe() {
        return numParagraphe;
    }

    public void setNumParagraphe(Integer numParagraphe) {
        this.numParagraphe = numParagraphe;
    }

    public String getTempsDebut() {
        return tempsDebut;
    }

    public void setTempsDebut(String tempsDebut) {
        this.tempsDebut = tempsDebut;
    }

    public String getTempsFin() {
        return tempsFin;
    }

    public void setTempsFin(String tempsFin) {
        this.tempsFin = tempsFin;
    }

    public String getTexteAffiche() {
        return texteAffiche;
    }

    public void setTexteAffiche(String texteAffiche) {
        this.texteAffiche = texteAffiche;
    }

    public void setIdSousTitre(Integer idSousTitre) {
        this.idSousTitre = idSousTitre;
    }

    public int getIdSousTitre() {
        return this.idSousTitre;
    }
}