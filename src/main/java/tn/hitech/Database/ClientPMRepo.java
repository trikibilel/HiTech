package tn.hitech.Database;

import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.ClientPM;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientPMRepo {

    public boolean insert(ClientPM clientPM) {
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
                pstmt.setString(1, clientPM.getAdresse());
                pstmt.setString(2, clientPM.getEmail());
                pstmt.setInt(3, clientPM.getTelephone());
                pstmt.setBoolean(4, false);


                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        clientId = rs.getInt(1);
                        clientPM.setId(clientId);
                    } else {
                        throw new SQLException("Failed to get generated client ID");
                    }
                }
            }

            // Then, insert into client_pm table
            String clientPmSql = """
                        INSERT INTO client_pm (client_id, matricule, raison_sociale)
                        VALUES (?, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(clientPmSql)) {
                pstmt.setInt(1, clientId);
                pstmt.setString(2, clientPM.getMatricule());
                pstmt.setString(3, clientPM.getRaisonSociale());

                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Corporate client inserted successfully with ID: " + clientId);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("⚠️ Transaction rolled back");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("❌ Error inserting corporate client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public ClientPM findById(int id) {
        String sql = """
                    SELECT c.*, pm.matricule, pm.raison_sociale
                    FROM clients c
                    INNER JOIN client_pm pm ON c.id = pm.client_id
                    WHERE c.id = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClientPM(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding corporate client: " + e.getMessage());
        }
        return null;
    }

    public List<ClientPM> findAll() {
        List<ClientPM> clientPMList = new ArrayList<>();
        String sql = """
                    SELECT c.*, pm.matricule, pm.raison_sociale
                    FROM clients c
                    INNER JOIN client_pm pm ON c.id = pm.client_id
                    WHERE c.is_personne = false
                    ORDER BY c.id
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                clientPMList.add(mapResultSetToClientPM(rs));
            }

            System.out.println("✅ Retrieved " + clientPMList.size() + " corporal clients");

        } catch (SQLException e) {
            System.err.println("❌ Error retrieving corporal clients: " + e.getMessage());
        }

        return clientPMList;
    }

    protected static ClientPM mapResultSetToClientPM(ResultSet rs) throws SQLException {
        ClientPM clientPM = new ClientPM();
        clientPM.setId(rs.getInt("id"));
        clientPM.setAdresse(rs.getString("adresse"));
        clientPM.setEmail(rs.getString("email"));
        clientPM.setTelephone(rs.getInt("telephone"));
        clientPM.setMatricule(rs.getString("matricule"));
        clientPM.setRaisonSociale(rs.getString("raison_sociale"));
        return clientPM;
    }
}