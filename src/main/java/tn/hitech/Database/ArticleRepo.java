package tn.hitech.Database;


import tn.hitech.Database.config.DbConnection;
import tn.hitech.Models.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepo {


    public boolean insert(Article article) {
        String sql = """
                    INSERT INTO articles (designation, image, prix_ht, qte_stock, promo)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, article.getDesignation());
            pstmt.setString(2, article.getImage());
            pstmt.setDouble(3, article.getPrixHt());
            pstmt.setInt(4, article.getQteStock());
            pstmt.setInt(5, article.getPromo());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        article.setRefArticle(rs.getInt(1));
                    }
                }
                System.out.println("✅ Article inserted successfully with ID: " + article.getRefArticle());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inserting article: " + e.getMessage());
        }
        return false;
    }

    public Article findById(int refArticle) {
        String sql = "SELECT * FROM articles WHERE ref_article = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, refArticle);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToArticle(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding article: " + e.getMessage());
        }
        return null;
    }

    public List<Article> findAll() {
        String sql = "SELECT * FROM articles ORDER BY ref_article";
        List<Article> articles = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding all articles: " + e.getMessage());
        }
        return articles;
    }

    public boolean updateStock(int refArticle, int newQte) {
        String sql = "UPDATE articles SET qte_stock = ? WHERE ref_article = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQte);
            pstmt.setInt(2, refArticle);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Stock updated successfully for article: " + refArticle);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating stock: " + e.getMessage());
        }
        return false;
    }

    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setRefArticle(rs.getInt("ref_article"));
        article.setDesignation(rs.getString("designation"));
        article.setImage(rs.getString("image"));
        article.setPrixHt(rs.getDouble("prix_ht"));
        article.setQteStock(rs.getInt("qte_stock"));
        article.setPromo(rs.getInt("promo"));
        return article;
    }
}