package tn.hitech.Models;

public class ClientPP extends Client {
    private String nom;
    private String prenom;

    public ClientPP() {
        super();
        this.nom = "nom";
        this.prenom = "prenom";
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
        System.out.println("## Le client " + prenom + " " + nom +
                " (ID: " + getId() + ") habite à " + getAdresse() +
                ", son email est " + getEmail() +
                " et son téléphone est " + getTelephone() + ".");
    }

    public String toString() {
        return "Client Personne Physique{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
