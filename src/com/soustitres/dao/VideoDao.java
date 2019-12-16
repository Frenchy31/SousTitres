package com.soustitres.dao;

import com.soustitres.beans.Video;

import java.util.ArrayList;

public interface VideoDao {
    Integer ajouter(Video video) throws DaoException;
    ArrayList<Video> lister() throws DaoException;

    Video getOne(Integer idVideo) throws DaoException;
    Video getOneByName(String name) throws DaoException;
}
