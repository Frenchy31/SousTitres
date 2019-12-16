package com.soustitres.dao;

import com.soustitres.beans.Paragraphe;

import java.sql.*;
import java.util.ArrayList;

public class ParagrapheDaoImpl implements ParagrapheDao {
    private DaoFactory daoFactory;

    public ParagrapheDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public boolean ajouter(Paragraphe paragraphe) throws DaoException {
        Connection connection = null;
        PreparedStatement statement;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("INSERT INTO PARAGRAPHE(ID_SOUSTITRE, TPS_DEBUT, TPS_FIN, NUMERO, TEXTE) VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, paragraphe.getIdSousTitre());
            statement.setString(2, paragraphe.getTempsDebut());
            statement.setString(3, paragraphe.getTempsFin());
            statement.setInt(4, paragraphe.getNumParagraphe());
            statement.setString(5, paragraphe.getTexteAffiche());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données");
            }
        }
    }

    @Override
    public ArrayList<Paragraphe> lister(Integer idSousTitre) throws DaoException {
        ArrayList<Paragraphe> paragraphes = new ArrayList<Paragraphe>();
        Connection connection = null;
        PreparedStatement statement;
        ResultSet result = null;

        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM PARAGRAPHE WHERE ID_SOUSTITRE = ? ORDER BY NUMERO;");
            statement.setInt(1, idSousTitre);
            result = statement.executeQuery();
            while (result.next()){
                Paragraphe paragraphe =  new Paragraphe(result.getInt("id"), result.getInt("numero"), result.getString("tps_debut"), result.getString("tps_fin"), result.getString("texte"));
                paragraphes.add(paragraphe);
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
        return paragraphes;
    }

    @Override
    public boolean updateTexte(Paragraphe paragraphe, String texte) throws DaoException {
        Connection connection = null;
        PreparedStatement statement;
        try{
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement("UPDATE PARAGRAPHE SET TEXTE = ? WHERE ID = ?;");
            statement.setString(1, texte);
            statement.setInt(2, paragraphe.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException("Connexion impossible avec la base");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données");
            }
        }
    }
}
