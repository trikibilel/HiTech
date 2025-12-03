package tn.hitech.Models;

import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private Date dateCde;
    private Date dateLiv;
    private Etat etatCde;
    private PayMethode moyenPayement;
    private List<LigneCmd> ligneCmds = new ArrayList<>();
    private Client client;

    public Commande() {
        this.id = 1;
        this.dateCde = new Date(22, 12, 2025);
        this.dateLiv = new Date(23, 12, 2025);
        this.etatCde = Etat.CREE;
        this.moyenPayement = PayMethode.CARTE;
        this.client = new Client();
    }

    public Commande(int id, Date dateCde, Date dateLiv, PayMethode moyenPayement, Client client) {
        this.id = id;
        this.dateCde = dateCde;
        this.dateLiv = dateLiv;
        this.etatCde = Etat.CREE;
        this.moyenPayement = moyenPayement;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCde() {
        return dateCde;
    }

    public void setDateCde(Date dateCde) {
        this.dateCde = dateCde;
    }

    public Date getDateLiv() {
        return dateLiv;
    }

    public void setDateLiv(Date dateLiv) {
        this.dateLiv = dateLiv;
    }

    public Etat getEtatCde() {
        return etatCde;
    }

    public void setEtatCde(Etat etatCde) {
        this.etatCde = etatCde;
    }

    public PayMethode getMoyenPayement() {
        return moyenPayement;
    }

    public void setMoyenPayement(PayMethode moyenPayement) {
        this.moyenPayement = moyenPayement;
    }

    public List<LigneCmd> getLigneCmds() {
        return ligneCmds;
    }

    public void setLigneCmds(List<LigneCmd> ligneCmds) {
        this.ligneCmds = ligneCmds;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getCommandeTotal() {
        double total = 0;
        for (LigneCmd ligneCmd : ligneCmds) {
            total += ligneCmd.getTotalTtcLigne();
        }
        return total;
    }

    public void livrer() {
        etatCde = Etat.LIVREE;
        ligneCmds.forEach(c -> {
            c.getArticle().mouvmentStock(-c.getQte());
        });
        System.out.println("## Commande "+ id+" livree");
    }

    public void annuler() {
        etatCde = Etat.ANNULEE;
        ligneCmds.forEach(c -> {
            c.getArticle().mouvmentStock(c.getQte());
        });
        System.out.println("## Commande "+ id+" annulee");
    }

    public void afficher() {
        System.out.println("==".repeat(10) + " Commande n°" + id + "==".repeat(10));
        System.out.println("Date de commande: " + dateCde);
        System.out.println("Date de livraison prévue: " + dateLiv);
        System.out.println("État: " + etatCde.name());
        System.out.println("Moyen de paiement: " + moyenPayement.name());
        System.out.println("\nClient:");
        client.afficher();
        System.out.println("\nArticles commandés:");
        ligneCmds.forEach(LigneCmd::afficher);
        System.out.println("\nTotal de la commande: " + getCommandeTotal() + " DT TTC");
        System.out.println("=".repeat(20) + "fin commande " + id + "=".repeat(20));
        System.out.println();
    }

    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", dateCde='" + dateCde + '\'' +
                ", dateLiv='" + dateLiv + '\'' +
                ", etatCde=" + etatCde +
                ", moyenPayement=" + moyenPayement +
                ", ligneCmds=" + ligneCmds +
                ", client=" + client +
                '}';
    }

    public enum Etat {
        CREE,
        LIVREE,
        ANNULEE
    }

    public enum PayMethode {
        CASH,
        CHECK,
        CARTE
    }
}
