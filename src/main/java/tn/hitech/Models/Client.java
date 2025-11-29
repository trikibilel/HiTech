package tn.hitech.Models;

public class Client {
    private int id;
    private String adresse;
    private String email;
    private int telephone;

    public Client() {
    }

    public Client(int id, String adresse, String email, int telephone) {
        this.id = id;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void afficher() {}

    public String toString() {
        return "Client{" + "id=" + id + ", adresse='" + adresse + '\'' + ", email='" + email + '\'' + ", telephone=" + telephone + '}';
    }
}
