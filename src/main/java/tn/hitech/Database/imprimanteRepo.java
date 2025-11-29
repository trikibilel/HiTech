package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Imprimante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class imprimanteRepo {

    private final ArticleRepo articleDAO = new ArticleRepo();

    public boolean insert(Imprimante imprimante) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false);

            // First, insert into articles table
            String articleSql = """
                        INSERT INTO articles (designation, image, prix_ht, qte_stock, promo)
                        VALUES (?, ?, ?, ?, ?)
                    """;

            int refArticle;
            try (PreparedStatement pstmt = conn.prepareStatement(articleSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, imprimante.getDesignation());
                pstmt.setString(2, imprimante.getImage());
                pstmt.setDouble(3, imprimante.getPrixHt());
                pstmt.setInt(4, imprimante.getQteStock());
                pstmt.setInt(5, imprimante.getPromo());

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        refArticle = rs.getInt(1);
                        imprimante.setRefArticle(refArticle);
                    } else {
                        throw new SQLException("Failed to get generated article ID");
                    }
                }
            }

            // Then, insert into imprimantes table
            String imprimanteSql = """
                        INSERT INTO imprimantes (ref_article, type, marque, page_par_minute)
                        VALUES (?, ?, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(imprimanteSql)) {
                pstmt.setInt(1, refArticle);
                pstmt.setString(2, imprimante.getType().name());
                pstmt.setString(3, imprimante.getMarque());
                pstmt.setInt(4, imprimante.getPageParMinute());

                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Printer inserted successfully with ID: " + refArticle);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("⚠️ Transaction rolled back");
                } catch (SQLException ex) {
                }
            }
            System.err.println("❌ Error inserting printer: " + e.getMessage());
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

    public Imprimante findById(int refArticle) {
        String sql = """
                    SELECT a.*, i.type, i.marque, i.page_par_minute
                    FROM articles a
                    INNER JOIN imprimantes i ON a.ref_article = i.ref_article
                    WHERE a.ref_article = ?
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, refArticle);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToImprimante(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding printer: " + e.getMessage());
        }
        return null;
    }

    public List<Imprimante> findAll() {
        String sql = """
                    SELECT a.*, i.type, i.marque, i.page_par_minute
                    FROM articles a
                    INNER JOIN imprimantes i ON a.ref_article = i.ref_article
                    ORDER BY a.ref_article
                """;
        List<Imprimante> imprimantes = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                imprimantes.add(mapResultSetToImprimante(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all printers: " + e.getMessage());
        }
        return imprimantes;
    }

    private Imprimante mapResultSetToImprimante(ResultSet rs) throws SQLException {
        Imprimante imprimante = new Imprimante();
        imprimante.setRefArticle(rs.getInt("id"));
        imprimante.setDesignation(rs.getString("designation"));
        imprimante.setImage(rs.getString("image"));
        imprimante.setPrixHt(rs.getDouble("prix_ht"));
        imprimante.setQteStock(rs.getInt("qte_stock"));
        imprimante.setPromo(rs.getInt("promo"));
        imprimante.setType(Imprimante.Type.valueOf(rs.getString("type")));
        imprimante.setMarque(rs.getString("marque"));
        imprimante.setPageParMinute(rs.getInt("page_par_minute"));
        return imprimante;
    }
}