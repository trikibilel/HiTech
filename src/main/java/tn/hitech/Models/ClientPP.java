package tn.hitech.Models;

public class ClientPP extends Client {
    private String nom;
    private String prenom;

    public ClientPP() {
    }

    public ClientPP(int id, String adresse, String email, int telephone, String nom, String prenom) {
        super(id, adresse, email, telephone);
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void afficher() {
        System.out.println("-".repeat(20 * 6));
        System.out.print("| " + nom + " ".repeat(20 - nom.length()) + " |");
        System.out.print("| " + prenom + " ".repeat(20 - prenom.length()) + " |");
        System.out.print("| " + getAdresse() + " ".repeat(20 - getAdresse().length()) + " |");
        System.out.print("| " + getTelephone() + " ".repeat(20 - String.valueOf(getTelephone()).length()) + " |");
        System.out.println("| " + getEmail() + " ".repeat(20 - getEmail().length()) + " |");
    }

    public String toString() {
        return "Client Personne Physique{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
