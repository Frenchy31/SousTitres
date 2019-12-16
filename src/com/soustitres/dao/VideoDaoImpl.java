package com.soustitres.dao;

import com.soustitres.beans.Video;

import java.sql.*;
import java.util.ArrayList;

public class VideoDaoImpl implements VideoDao{

    private DaoFactory daoFactory;

    public VideoDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Integer ajouter(Video video) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("INSERT INTO VIDEO(NOM) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, video.getNom());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        }
        return 0;
    }

    @Override
    public ArrayList<Video> lister() throws DaoException {
        ArrayList<Video> videos = new ArrayList<Video>();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try{
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM VIDEO;");

            while (result.next()){
                Video video =  new Video(result.getInt("id"), result.getString("nom"));
                videos.add(video);
            }
        } catch (SQLException e) {
            throw new DaoException("Connexion à la base de données impossible.");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données");
            }
        }
        return videos;
    }

    @Override
    public Video getOne(Integer idVideo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM VIDEO WHERE ID = ?;");
            statement.setInt(1, idVideo);
            result = statement.executeQuery();
            if (result.next())
                return new Video(result.getInt("id"), result.getString("nom"));
            else
                return null;
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        }
    }

    @Override
    public Video getOneByName(String name) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM VIDEO WHERE NOM = ?;");
            statement.setString(1, name);
            result = statement.executeQuery();
            if (result.next())
                return new Video(result.getInt("id"), result.getString("nom"));
            else
                return null;
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        }
    }
}
