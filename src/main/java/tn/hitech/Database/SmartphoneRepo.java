package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Smartphone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SmartphoneRepo {

    private final ArticleRepo articleDAO = new ArticleRepo();

    public boolean insert(Smartphone smartphone) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, insert into articles table
            String articleSql = """
                        INSERT INTO articles (designation, image, prix_ht, qte_stock, promo)
                        VALUES (?, ?, ?, ?, ?)
                    """;

            int refArticle;
            try (PreparedStatement pstmt = conn.prepareStatement(articleSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, smartphone.getDesignation());
                pstmt.setString(2, smartphone.getImage());
                pstmt.setDouble(3, smartphone.getPrixHt());
                pstmt.setInt(4, smartphone.getQteStock());
                pstmt.setInt(5, smartphone.getPromo());

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        refArticle = rs.getInt(1);
                        smartphone.setRefArticle(refArticle);
                    } else {
                        throw new SQLException("Failed to get generated article ID");
                    }
                }
            }

            // Then, insert into smartphones table
            String smartphoneSql = """
                        INSERT INTO smartphones (ref_article, marque, stockage, ram, os, taille_ecran)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(smartphoneSql)) {
                pstmt.setInt(1, refArticle);
                pstmt.setString(2, smartphone.getMarque());
                pstmt.setInt(3, smartphone.getStockage());
                pstmt.setInt(4, smartphone.getRam());
                pstmt.setString(5, smartphone.getOs());
                pstmt.setDouble(6, smartphone.getTailleEcran());

                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Smartphone inserted successfully with ID: " + refArticle);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("⚠️ Transaction rolled back");
                } catch (SQLException ex) {
                }
            }
            System.err.println("❌ Error inserting smartphone: " + e.getMessage());
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

    public Smartphone findById(int refArticle) {
        String sql = """
                    SELECT a.*, s.marque, s.stockage, s.ram, s.os, s.taille_ecran
                    FROM articles a
                    INNER JOIN smartphones s ON a.ref_article = s.ref_article
                    WHERE a.ref_article = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, refArticle);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSmartphone(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding smartphone: " + e.getMessage());
        }
        return null;
    }

    public List<Smartphone> findAll() {
        String sql = """
                    SELECT a.*, s.marque, s.stockage, s.ram, s.os, s.taille_ecran
                    FROM articles a
                    INNER JOIN smartphones s ON a.ref_article = s.ref_article
                    ORDER BY a.ref_article
                """;
        List<Smartphone> smartphones = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                smartphones.add(mapResultSetToSmartphone(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all smartphones: " + e.getMessage());
        }
        return smartphones;
    }

    private Smartphone mapResultSetToSmartphone(ResultSet rs) throws SQLException {
        Smartphone smartphone = new Smartphone();
        smartphone.setRefArticle(rs.getInt("ref_article"));
        smartphone.setDesignation(rs.getString("designation"));
        smartphone.setImage(rs.getString("image"));
        smartphone.setPrixHt(rs.getDouble("prix_ht"));
        smartphone.setQteStock(rs.getInt("qte_stock"));
        smartphone.setPromo(rs.getInt("promo"));
        smartphone.setMarque(rs.getString("marque"));
        smartphone.setStockage(rs.getInt("stockage"));
        smartphone.setRam(rs.getInt("ram"));
        smartphone.setOs(rs.getString("os"));
        smartphone.setTailleEcran(rs.getDouble("taille_ecran"));
        return smartphone;
    }
}