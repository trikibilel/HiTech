package tn.hitech.Database.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitialiser {

    public static void initialise() {
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            createTables(conn, stmt);
        } catch (Exception e) {
            System.err.println("Could not display tables summary: " + e.getMessage());
        }
    }

    public static void createTables(Connection conn, Statement stmt) throws SQLException {
        System.out.println("Starting table creation...");
        // 1. Create clients table (parent table for inheritance)
        String clientsTable = """
                    CREATE TABLE IF NOT EXISTS clients (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        adresse VARCHAR(255) NOT NULL,
                        email VARCHAR(150) NOT NULL UNIQUE,
                        telephone INT NOT NULL,
                        is_personne BIT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        INDEX idx_email (email)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 2. Create client_pp table (physical person clients)
        String clientPpTable = """
                    CREATE TABLE IF NOT EXISTS client_pp (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nom VARCHAR(100) NOT NULL,
                        prenom VARCHAR(100) NOT NULL,
                        client_id INT NOT NULL,
                        FOREIGN KEY (client_id) REFERENCES clients(id) 
                            ON DELETE CASCADE ON UPDATE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 3. Create client_pm table (legal entity clients)
        String clientPmTable = """
                    CREATE TABLE IF NOT EXISTS client_pm (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        matricule VARCHAR(50) NOT NULL UNIQUE,
                        raison_sociale VARCHAR(255) NOT NULL,
                        client_id INT NOT NULL,
                        FOREIGN KEY (client_id) REFERENCES clients(id) 
                            ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_matricule (matricule)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 4. Create articles table (parent table for products)
        String articlesTable = """
                    CREATE TABLE IF NOT EXISTS articles (
                        ref_article INT AUTO_INCREMENT PRIMARY KEY,
                        designation VARCHAR(255) NOT NULL,
                        image VARCHAR(500),
                        prix_ht DOUBLE NOT NULL,
                        qte_stock INT NOT NULL DEFAULT 0,
                        promo INT DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        INDEX idx_designation (designation),
                        INDEX idx_prix (prix_ht),
                        CHECK (prix_ht >= 0),
                        CHECK (qte_stock >= 0),
                        CHECK (promo >= 0 AND promo <= 100)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 5. Create smartphones table
        String smartphonesTable = """
                    CREATE TABLE IF NOT EXISTS smartphones (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        marque VARCHAR(100) NOT NULL,
                        stockage INT NOT NULL,
                        ram INT NOT NULL,
                        os VARCHAR(50) NOT NULL,
                        taille_ecran DOUBLE NOT NULL,
                        ref_article INT NOT NULL,
                        FOREIGN KEY (ref_article) REFERENCES articles(ref_article) 
                            ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_marque (marque),
                        INDEX idx_os (os),
                        CHECK (stockage > 0),
                        CHECK (ram > 0),
                        CHECK (taille_ecran > 0)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 6. Create imprimantes table
        String imprimantesTable = """
                    CREATE TABLE IF NOT EXISTS imprimantes (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        type VARCHAR(50) NOT NULL,
                        marque VARCHAR(100) NOT NULL,
                        page_par_minute INT NOT NULL,
                        ref_article INT NOT NULL,
                        FOREIGN KEY (ref_article) REFERENCES articles(ref_article) 
                            ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_type (type),
                        INDEX idx_marque (marque),
                        CHECK (page_par_minute > 0)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 7. Create commandes table
        String commandesTable = """
                    CREATE TABLE IF NOT EXISTS commandes (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        date_cde VARCHAR(50) NOT NULL,
                        date_liv VARCHAR(50),
                        etat_cde VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE',
                        moyen_payement VARCHAR(50),
                        client_id INT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (client_id) REFERENCES clients(id) 
                            ON DELETE RESTRICT ON UPDATE CASCADE,
                        INDEX idx_client (client_id),
                        INDEX idx_etat (etat_cde),
                        INDEX idx_date_cde (date_cde)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // 8. Create ligne_cmds table (order lines)
        String ligneCmdsTable = """
                    CREATE TABLE IF NOT EXISTS ligne_cmds (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        qte INT NOT NULL,
                        total_ttc_ligne DOUBLE NOT NULL,
                        article_id INT NOT NULL,
                        commande_id INT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (article_id) REFERENCES articles(ref_article) 
                            ON DELETE RESTRICT ON UPDATE CASCADE,
                        FOREIGN KEY (commande_id) REFERENCES commandes(id) 
                            ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_commande (commande_id),
                        INDEX idx_article (article_id),
                        CHECK (qte > 0),
                        CHECK (total_ttc_ligne >= 0)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        // Execute table creation in the correct order (respecting foreign key dependencies)
        stmt.executeUpdate(clientsTable);
        stmt.executeUpdate(clientPpTable);
        stmt.executeUpdate(clientPmTable);
        stmt.executeUpdate(articlesTable);
        stmt.executeUpdate(smartphonesTable);
        stmt.executeUpdate(imprimantesTable);
        stmt.executeUpdate(commandesTable);
        stmt.executeUpdate(ligneCmdsTable);

    }

    public static void dropAllTables() {
        try (Connection conn = DbConnection.getConnection(); Statement stmt = conn.createStatement()) {

            System.out.println("⚠️  Dropping all tables...");

            // Disable foreign key checks temporarily
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");

            // Drop tables in reverse order
            String[] tables = {"ligne_cmds", "commandes", "imprimantes", "smartphones", "client_pm", "client_pp", "articles", "clients"};

            for (String table : tables) {
                stmt.executeUpdate("DROP TABLE IF EXISTS " + table);
            }

            // Re-enable foreign key checks
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("All tables dropped successfully!");

        } catch (Exception e) {
            System.err.println("Error dropping tables:");
        }
    }

    public static void main(String[] args) {
        dropAllTables();
        initialise();
    }
}