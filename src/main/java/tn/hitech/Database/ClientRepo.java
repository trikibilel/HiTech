package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepo {

    public boolean insert(Client client, boolean isPersonne) {
        String sql = """
                    INSERT INTO clients (adresse, email, telephone, is_personne)
                    VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, client.getAdresse());
            pstmt.setString(2, client.getEmail());
            pstmt.setInt(3, client.getTelephone());
            pstmt.setBoolean(4, isPersonne);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        client.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inserting client: " + e.getMessage());
        }
        return false;
    }

    public static Client findById(int id) {
        String sql = """
                SELECT c.*, pp.nom, pp.prenom, pm.matricule, pm.raison_sociale
                FROM clients c
                LEFT JOIN client_pp pp ON c.id = pp.client_id
                LEFT JOIN client_pm pm ON c.id = pm.client_id
                WHERE c.id = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getBoolean("is_personne"))
                        return ClientPPRepo.mapResultSetToClientPP(rs);
                    else
                        return ClientPMRepo.mapResultSetToClientPM(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding client: " + e.getMessage());
        }
        return null;
    }

    public Client findByEmail(String email) {
        String sql = """
                SELECT c.*, pp.nom, pp.prenom, pm.matricule, pm.raison_sociale
                FROM clients c
                LEFT JOIN client_pp pp ON c.id = pp.client_id
                LEFT JOIN client_pm pm ON c.id = pm.client_id
                WHERE c.email = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getBoolean("is_personne"))
                        return ClientPPRepo.mapResultSetToClientPP(rs);
                    else
                        return ClientPMRepo.mapResultSetToClientPM(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding client by email: " + e.getMessage());
        }
        return null;
    }

    public List<Client> findAll() {
        String sql = """
                SELECT c.*,pp.*,pm.*
                FROM clients c
                LEFT JOIN client_pp pp 
                    ON c.id = pp.client_id
                LEFT JOIN client_pm pm 
                    ON c.id = pm.client_id
                ORDER BY c.id 
                """;
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getBoolean("is_personne"))
                    clients.add(ClientPPRepo.mapResultSetToClientPP(rs));
                else
                    clients.add(ClientPMRepo.mapResultSetToClientPM(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all clients: " + e.getMessage());
        }
        return clients;
    }

    protected Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setAdresse(rs.getString("adresse"));
        client.setEmail(rs.getString("email"));
        client.setTelephone(rs.getInt("telephone"));
        return client;
    }
}