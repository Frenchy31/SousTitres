package com.soustitres.dao;

import com.soustitres.beans.SousTitre;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class SousTitreDaoImpl implements SousTitreDao {
    private final DaoFactory daoFactory;

    public SousTitreDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public int ajouter(SousTitre sousTitre) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        Integer autoKey = 0;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("INSERT INTO SOUS_TITRE(ID_VIDEO,LANGUE) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, sousTitre.getIdVideo());
            statement.setString(2, sousTitre.getLocale().getLanguage());
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
    public SousTitre getOne(Integer idVideo, Locale langue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM SOUS_TITRE WHERE ID_VIDEO = ? AND LANGUE = ?;");
            statement.setInt(1, idVideo);
            statement.setString(2, langue.getLanguage());
            result = statement.executeQuery();
            if (result.next())
                return new SousTitre(result.getInt("id"),result.getInt("id_video"), new Locale (result.getString("langue")));
            else
                return null;
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        }

    }

    @Override
    public ArrayList<SousTitre> lister(Integer idVideo) throws DaoException {
        ArrayList<SousTitre> sousTitres = new ArrayList<SousTitre>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM SOUS_TITRE WHERE ID_VIDEO = ?;");
            statement.setString(1, String.valueOf(idVideo));
            result = statement.executeQuery();

            while (result.next()){
                Integer id = result.getInt("id");
                Locale langue = new Locale(result.getString("langue"));

                SousTitre sousTitre = new SousTitre(id, langue);
                sousTitres.add(sousTitre);
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
        return sousTitres;
    }
}
