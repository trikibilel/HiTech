package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Client;
import tn.hitech.Models.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeRepo {

    private final ClientRepo clientDAO = new ClientRepo();

    public boolean insert(Commande commande) {
        String sql = """
                    INSERT INTO commandes (date_cde, date_liv, etat_cde, moyen_payement, client_id)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, commande.getDateCde());
            pstmt.setString(2, commande.getDateLiv());
            pstmt.setString(3, commande.getEtatCde().name());
            pstmt.setString(4, commande.getMoyenPayement().name());
            pstmt.setInt(5, commande.getClient().getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        commande.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Order inserted successfully with ID: " + commande.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inserting order: " + e.getMessage());
        }
        return false;
    }

    public boolean updateStatus(int id, String newStatus) {
        String sql = "UPDATE commandes SET etat_cde = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Order status updated successfully: " + id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
        }
        return false;
    }

    public Commande findById(int id) {
        String sql = "SELECT * FROM commandes WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCommande(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding order: " + e.getMessage());
        }
        return null;
    }

    public List<Commande> findAll() {
        String sql = "SELECT * FROM commandes ORDER BY id DESC";
        List<Commande> commandes = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all orders: " + e.getMessage());
        }
        return commandes;
    }

    public List<Commande> findByClientId(int clientId) {
        String sql = "SELECT * FROM commandes WHERE client_id = ? ORDER BY id DESC";
        List<Commande> commandes = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    commandes.add(mapResultSetToCommande(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding orders by client: " + e.getMessage());
            e.printStackTrace();
        }
        return commandes;
    }

    private Commande mapResultSetToCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        commande.setDateCde(rs.getString("date_cde"));
        commande.setDateLiv(rs.getString("date_liv"));
        commande.setEtatCde(Commande.Etat.valueOf(rs.getString("etat_cde")));
        commande.setMoyenPayement(Commande.PayMethode.valueOf(rs.getString("moyen_payement")));

        // Load client
        int clientId = rs.getInt("client_id");
        Client client = clientDAO.findById(clientId);
        commande.setClient(client);

        return commande;
    }
}