package tn.hitech.Models;

public class ClientPM extends Client {
    private String matricule;
    private String raisonSociale;

    public ClientPM() {
        super();
        this.matricule = "matricule";
        this.raisonSociale = "raisonSociale";
    }

    public ClientPM(int id, String adresse, String email, int telephone, String matricule, String raisonSociale) {
        super(id, adresse, email, telephone);
        this.matricule = matricule;
        this.raisonSociale = raisonSociale;
    }


    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonsociale) {
        this.raisonSociale = raisonsociale;
    }

    @Override
    public void afficher() {
        System.out.println("## La société " + raisonSociale +
                " (matricule: " + matricule + ") " +
                "est située à " + getAdresse() +
                ", son email est " + getEmail() +
                " et son téléphone est " + getTelephone() + ".");
    }

    public String toString() {
        return "Client Personne Morale{" +
                "matricule='" + matricule + '\'' +
                ", raisonSociale='" + raisonSociale + '\'' +
                '}';
    }
}
