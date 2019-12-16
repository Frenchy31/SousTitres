package com.soustitres.beans;

import java.util.ArrayList;

public class Video {
    private int id;
    private ArrayList<SousTitre> sousTitres = new ArrayList<SousTitre>();
    private String nom;

    public Video(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Video(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public ArrayList<SousTitre> getSousTitres() {
        return sousTitres;
    }

    public void setSousTitres(ArrayList<SousTitre> sousTitres) {
        this.sousTitres = sousTitres;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
