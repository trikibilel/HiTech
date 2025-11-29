package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.ClientPP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientPPRepo {

    public boolean insert(ClientPP clientPP) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false);

            // First, insert into clients table
            String clientSql = """
                        INSERT INTO clients (adresse, email, telephone, is_personne)
                        VALUES (?, ?, ?, ?)
                    """;

            int clientId;
            try (PreparedStatement pstmt = conn.prepareStatement(clientSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, clientPP.getAdresse());
                pstmt.setString(2, clientPP.getEmail());
                pstmt.setInt(3, clientPP.getTelephone());
                pstmt.setBoolean(4, true);

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        clientId = rs.getInt(1);
                        clientPP.setId(clientId);
                    } else {
                        throw new SQLException("Failed to get generated client ID");
                    }
                }
            }

            // Then, insert into client_pp table
            String clientPpSql = """
                        INSERT INTO client_pp (nom, prenom, client_id)
                        VALUES (?, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(clientPpSql)) {
                pstmt.setString(1, clientPP.getNom());
                pstmt.setString(2, clientPP.getPrenom());
                pstmt.setInt(3, clientId);

                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Individual client inserted successfully with ID: " + clientId);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("⚠️ Transaction rolled back");
                } catch (SQLException ex) {
                }
            }
            System.err.println("❌ Error inserting individual client: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return false;
    }

    public ClientPP findById(int id) {
        String sql = """
                    SELECT c.*, pp.nom, pp.prenom
                    FROM clients c
                    INNER JOIN client_pp pp ON c.id = pp.client_id
                    WHERE c.id = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClientPP(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding individual client: " + e.getMessage());
        }
        return null;
    }

    public List<ClientPP> findAll() {
        List<ClientPP> clientPPList = new ArrayList<>();
        String sql = """
                SELECT c.*, pp.nom, pp.prenom
                FROM clients c
                INNER JOIN client_pp pp ON c.id = pp.client_id
                WHERE c.is_personne = true
                ORDER BY c.id
            """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                clientPPList.add(mapResultSetToClientPP(rs));
            }

            System.out.println("✅ Retrieved " + clientPPList.size() + " individual clients");

        } catch (SQLException e) {
            System.err.println("❌ Error retrieving individual clients: " + e.getMessage());
        }

        return clientPPList;
    }

    protected ClientPP mapResultSetToClientPP(ResultSet rs) throws SQLException {
        ClientPP clientPP = new ClientPP();
        clientPP.setId(rs.getInt("id"));
        clientPP.setAdresse(rs.getString("adresse"));
        clientPP.setEmail(rs.getString("email"));
        clientPP.setTelephone(rs.getInt("telephone"));
        clientPP.setNom(rs.getString("nom"));
        clientPP.setPrenom(rs.getString("prenom"));
        return clientPP;
    }
}