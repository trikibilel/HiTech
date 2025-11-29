package tn.hitech.Models;

public class LigneCmd {
    private int id;
    private int qte;
    private double totalTtcLigne;
    private Article article;
    private Commande commande;


    public LigneCmd(int id, int qte, Article article, Commande commande) {
        this.id = id;
        this.qte = qte;
        this.totalTtcLigne = article.calculerPrixTtc() * qte;
        this.article = article;
        this.commande = commande;
        this.commande.getLigneCmds().add(this);
    }

    public LigneCmd() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getTotalTtcLigne() {
        return totalTtcLigne;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public void afficher() {
        int colSize = 20;
        System.out.println("-".repeat(colSize * 4));
        System.out.print("| " + article.getRefArticle() + " ".repeat(20 - String.valueOf(article.getRefArticle()).length()) + " |");
        System.out.print("| " + article.getDesignation() + " ".repeat(20 - article.getDesignation().length()) + " |");
        System.out.print("| " + qte + " ".repeat(20 - String.valueOf(qte).length()) + " |");
        System.out.println("| " + totalTtcLigne + " ".repeat(20 - String.valueOf(totalTtcLigne).length()) + " |");
    }
}
