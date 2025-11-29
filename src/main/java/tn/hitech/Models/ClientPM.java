package tn.hitech.Models;

public class ClientPM extends Client {
    private String matricule;
    private String raisonSociale;

    public ClientPM() {
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
        System.out.println("-".repeat(20 * 6));
        System.out.print("| " + raisonSociale + " ".repeat(20 - raisonSociale.length()) + " |");
        System.out.print("| " + matricule + " ".repeat(20 - matricule.length()) + " |");
        System.out.print("| " + getAdresse() + " ".repeat(20 - getAdresse().length()) + " |");
        System.out.print("| " + getTelephone() + " ".repeat(20 - String.valueOf(getTelephone()).length()) + " |");
        System.out.println("| " + getEmail() + " ".repeat(20 - getEmail().length()) + " |");
    }

    public String toString() {
        return "Client Personne Morale{" +
                "matricule='" + matricule + '\'' +
                ", raisonSociale='" + raisonSociale + '\'' +
                '}';
    }
}
