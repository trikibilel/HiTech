package tn.hitech.Database;

import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Article;
import tn.hitech.Models.Commande;
import tn.hitech.Models.LigneCmd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCmdRepo {

    private final ArticleRepo articleDAO = new ArticleRepo();
    private final CommandeRepo commandeDAO = new CommandeRepo();

    public boolean insert(LigneCmd ligneCmd) {
        String sql = """
                    INSERT INTO ligne_cmds (qte, total_ttc_ligne, article_id, commande_id)
                    VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ligneCmd.getQte());
            pstmt.setDouble(2, ligneCmd.getTotalTtcLigne());
            pstmt.setInt(3, ligneCmd.getArticle().getRefArticle());
            pstmt.setInt(4, ligneCmd.getCommande().getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ligneCmd.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Order line inserted successfully with ID: " + ligneCmd.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inserting order line: " + e.getMessage());
        }
        return false;
    }

    public LigneCmd findById(int id) {
        String sql = "SELECT * FROM ligne_cmds WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLigneCmd(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding order line: " + e.getMessage());
        }
        return null;
    }

    public List<LigneCmd> findAll() {
        String sql = "SELECT * FROM ligne_cmds ORDER BY id";
        List<LigneCmd> lignes = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lignes.add(mapResultSetToLigneCmd(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all order lines: " + e.getMessage());
        }
        return lignes;
    }

    public List<LigneCmd> findByCommandeId(int commandeId) {
        String sql = "SELECT * FROM ligne_cmds WHERE commande_id = ? ORDER BY id";
        List<LigneCmd> lignes = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, commandeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lignes.add(mapResultSetToLigneCmd(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding order lines by order: " + e.getMessage());
        }
        return lignes;
    }

    public double getTotalForCommande(int commandeId) {
        String sql = "SELECT SUM(total_ttc_ligne) FROM ligne_cmds WHERE commande_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, commandeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error calculating order total: " + e.getMessage());
        }
        return 0.0;
    }

    private LigneCmd mapResultSetToLigneCmd(ResultSet rs) throws SQLException {
        // Load article
        int articleId = rs.getInt("article_id");
        Article article = articleDAO.findById(articleId);
        // Load commande
        int commandeId = rs.getInt("commande_id");
        Commande commande = commandeDAO.findById(commandeId);
        return new LigneCmd(rs.getInt("id"), rs.getInt("qte"), article, commande);
    }
}